package org.tirasweel.drivelogger.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.tirasweel.drivelogger.classes.LocationIntervalType
import org.tirasweel.drivelogger.interfaces.LocationSettingsRepository

class SharedPreferencesLocationSettingsRepository(
    context: Context
) : LocationSettingsRepository {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _locationInterval = MutableStateFlow(loadLocationInterval())
    override val locationInterval: StateFlow<LocationIntervalType> =
        _locationInterval.asStateFlow()

    override fun getLocationInterval(): LocationIntervalType {
        return _locationInterval.value
    }

    override fun setLocationInterval(interval: LocationIntervalType) {
        prefs.edit().putLong(KEY_LOCATION_INTERVAL_MILLIS, interval.intervalMillis).apply()
        _locationInterval.value = interval
    }

    private fun loadLocationInterval(): LocationIntervalType {
        val savedMillis = prefs.getLong(
            KEY_LOCATION_INTERVAL_MILLIS,
            LocationIntervalType.DEFAULT.intervalMillis
        )
        return LocationIntervalType.fromIntervalMillis(savedMillis)
    }

    companion object {
        private const val PREFS_NAME = "drive_logger_location_prefs"
        private const val KEY_LOCATION_INTERVAL_MILLIS = "location_interval_millis"
    }
}
