package org.tirasweel.drivelogger.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

import org.tirasweel.drivelogger.interfaces.ChartableLog

@Serializable
@Entity(tableName = "drive_log")
data class DriveLog(
    /** ID */
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    /** 作成日 */
    var createdDate: Long = 0,

    /** 更新日 */
    var updatedDate: Long = 0,

    /** 日付 */
    var date: Long = 0,

    /** 走行距離x1,000 */
    var milliMileage: Long = -1,

    /** 燃費 */
    var fuelEfficient: Double? = null,

    /** 合計走行距離x1,000 */
    var totalMilliMileage: Long? = null,

    /** メモ */
    var memo: String = ""
) : ChartableLog {
    override val logDate: Long get() = date
    override val logMilliMileage: Long get() = milliMileage
    override val logTotalMilliMileage: Long? get() = totalMilliMileage
    override val logFuelEfficiency: Double? get() = fuelEfficient
}
