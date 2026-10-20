package org.tirasweel.drivelogger.fake

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.tirasweel.drivelogger.classes.LocationIntervalType
import org.tirasweel.drivelogger.interfaces.LocationSettingsRepository

class FakeLocationSettingsRepository(
    initialInterval: LocationIntervalType = LocationIntervalType.DEFAULT
) : LocationSettingsRepository {

    private val _locationInterval = MutableStateFlow(initialInterval)
    override val locationInterval: StateFlow<LocationIntervalType> =
        _locationInterval.asStateFlow()

    override fun getLocationInterval(): LocationIntervalType = _locationInterval.value

    override fun setLocationInterval(interval: LocationIntervalType) {
        _locationInterval.value = interval
    }
}
