package org.tirasweel.drivelogger.classes

import androidx.annotation.StringRes
import org.tirasweel.drivelogger.R

/**
 * 位置情報取得間隔のプリセット
 */
enum class LocationIntervalType(
    val intervalMillis: Long,
    @param:StringRes val menuTextId: Int
) {
    Seconds5(5_000L, R.string.location_interval_5s),
    Seconds10(10_000L, R.string.location_interval_10s),
    Seconds30(30_000L, R.string.location_interval_30s),
    Minutes1(60_000L, R.string.location_interval_1m);

    companion object {
        val DEFAULT = Seconds10

        fun fromIntervalMillis(intervalMillis: Long): LocationIntervalType {
            return entries.find { it.intervalMillis == intervalMillis } ?: DEFAULT
        }
    }
}
