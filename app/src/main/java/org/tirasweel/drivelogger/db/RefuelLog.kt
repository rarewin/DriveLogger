package org.tirasweel.drivelogger.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "refuel_log")
data class RefuelLog(
    /** ID */
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    /** 作成日 */
    var createdDate: Long = 0,

    /** 更新日 */
    var updatedDate: Long = 0,

    /** 日付 */
    var date: Long = 0,

    /** 合計走行距離x1,000 (km) */
    var totalMilliMileage: Long? = null,

    /** 今回の走行距離x1,000 (km) */
    var milliMileage: Long = 0,

    /** 給油量 (L) */
    var fuelAmount: Double = 0.0,

    /** 合計金額 */
    var fuelPrice: Long? = null,

    /** 燃費 (km/L) - 前回の給油記録からの計算値 */
    var fuelEfficiency: Double? = null,

    /** メモ */
    var memo: String = ""
)
