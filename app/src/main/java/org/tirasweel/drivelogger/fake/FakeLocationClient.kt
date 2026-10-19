package org.tirasweel.drivelogger.fake

import android.location.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.tirasweel.drivelogger.interfaces.LocationClient

class FakeLocationClient(
    var isPermissionGranted: Boolean = true,
    var isGpsServiceEnabled: Boolean = true,
    var mockLocation: Location? = null
) : LocationClient {

    override fun hasLocationPermission(): Boolean = isPermissionGranted

    override fun isGpsEnabled(): Boolean = isGpsServiceEnabled

    override suspend fun getCurrentLocation(): Location? = mockLocation

    override fun getLocationUpdates(
        intervalMillis: Long,
        minUpdateDistanceMeters: Float
    ): Flow<Location> = emptyFlow()
}
