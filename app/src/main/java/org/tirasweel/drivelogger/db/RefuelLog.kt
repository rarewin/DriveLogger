package org.tirasweel.drivelogger.db

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
class RefuelLog : RealmObject {
    /** ID */
    @PrimaryKey
    var id: Long = 0

    /** 作成日 */
    var createdDate: Long = 0

    /** 更新日 */
    var updatedDate: Long = 0

    /** 日付 */
    var date: Long = 0

    /** 給油量(ml) */
    var fuelMilliliters: Long = 0

    /** 走行距離 */
    var milliMileage: Long = 0

    /** */
    var fuelMilliPricePerLiter: Long = 0

    /** 合計走行距離 */
    var totalMilliMileage: Long? = null

    /** メモ */
    var memo: String = ""
}