package org.tirasweel.drivelogger.interfaces

import android.location.Location
import kotlinx.coroutines.flow.Flow

/**
 * GPSおよび位置情報取得を管理するインターフェース
 */
interface LocationClient {

    /**
     * 位置情報パーミッションが付与されているかどうか
     */
    fun hasLocationPermission(): Boolean

    /**
     * 端末のGPS（位置情報サービス）が有効になっているかどうか
     */
    fun isGpsEnabled(): Boolean

    /**
     * 現在地を1回取得（給油ログなどでのスタンド位置記録に使用）
     */
    suspend fun getCurrentLocation(): Location?

    /**
     * 継続的な位置情報更新ストリーム（走行軌跡記録などのGPS ON時に使用）
     * Flowの収集開始でGPSトラッキングがONになり、収集停止でOFFになる
     *
     * @param intervalMillis 更新間隔（ミリ秒）
     * @param minUpdateDistanceMeters 最小移動距離（メートル）
     */
    fun getLocationUpdates(
        intervalMillis: Long = 5000L,
        minUpdateDistanceMeters: Float = 0f
    ): Flow<Location>
}
