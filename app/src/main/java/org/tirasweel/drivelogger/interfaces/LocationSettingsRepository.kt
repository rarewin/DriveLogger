package org.tirasweel.drivelogger.interfaces

import kotlinx.coroutines.flow.StateFlow
import org.tirasweel.drivelogger.classes.LocationIntervalType

/**
 * GPS・位置情報取得に関するユーザー設定を管理するリポジトリインターフェース
 */
interface LocationSettingsRepository {
    val locationInterval: StateFlow<LocationIntervalType>

    fun getLocationInterval(): LocationIntervalType

    fun setLocationInterval(interval: LocationIntervalType)
}
