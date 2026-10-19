package org.tirasweel.drivelogger.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.location.Location
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.tirasweel.drivelogger.DriveLogger
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.activities.MainActivity
import org.tirasweel.drivelogger.interfaces.LocationClient
import timber.log.Timber

class LocationTrackingService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient
    private var trackingJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        locationClient = (application as DriveLogger).container.locationClient
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val intervalMillis = intent.getLongExtra(
                    EXTRA_INTERVAL_MILLIS,
                    DEFAULT_INTERVAL_MILLIS
                )
                startTracking(intervalMillis)
            }
            ACTION_STOP -> stopTracking()
        }
        return START_STICKY
    }

    private fun startTracking(intervalMillis: Long) {
        if (!locationClient.hasLocationPermission()) {
            Timber.w("Location permission is not granted. Cannot start LocationTrackingService.")
            stopSelf()
            return
        }

        createNotificationChannel()
        val notification = buildNotification()

        val foregroundServiceType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
        } else {
            0
        }

        ServiceCompat.startForeground(
            this,
            NOTIFICATION_ID,
            notification,
            foregroundServiceType
        )

        _isTracking.value = true
        trackingJob?.cancel()
        trackingJob = locationClient.getLocationUpdates(intervalMillis = intervalMillis)
            .catch { e ->
                Timber.e(e, "Failed to receive location updates")
            }
            .onEach { location ->
                _lastLocation.value = location
                Timber.d("Location update: lat=${location.latitude}, lon=${location.longitude}")
            }
            .launchIn(serviceScope)
    }

    private fun stopTracking() {
        trackingJob?.cancel()
        trackingJob = null
        _isTracking.value = false
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        trackingJob?.cancel()
        _isTracking.value = false
        serviceScope.cancel()
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            getString(R.string.notification_channel_location_tracking),
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun buildNotification(): Notification {
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_title_location_tracking))
            .setContentText(getString(R.string.notification_text_location_tracking))
            .setSmallIcon(R.drawable.baseline_directions_car_24)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    companion object {
        const val ACTION_START = "org.tirasweel.drivelogger.action.START_LOCATION_TRACKING"
        const val ACTION_STOP = "org.tirasweel.drivelogger.action.STOP_LOCATION_TRACKING"
        const val EXTRA_INTERVAL_MILLIS = "extra_interval_millis"
        const val DEFAULT_INTERVAL_MILLIS = 10_000L

        private const val NOTIFICATION_CHANNEL_ID = "location_tracking_channel"
        private const val NOTIFICATION_ID = 1001

        private val _isTracking = MutableStateFlow(false)
        val isTracking: StateFlow<Boolean> = _isTracking.asStateFlow()

        private val _lastLocation = MutableStateFlow<Location?>(null)
        val lastLocation: StateFlow<Location?> = _lastLocation.asStateFlow()

        fun start(context: Context, intervalMillis: Long = DEFAULT_INTERVAL_MILLIS) {
            val intent = Intent(context, LocationTrackingService::class.java).apply {
                action = ACTION_START
                putExtra(EXTRA_INTERVAL_MILLIS, intervalMillis)
            }
            ContextCompat.startForegroundService(context, intent)
        }

        fun stop(context: Context) {
            val intent = Intent(context, LocationTrackingService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }
    }
}
