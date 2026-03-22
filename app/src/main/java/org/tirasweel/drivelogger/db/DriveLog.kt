package org.tirasweel.drivelogger.db

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class DriveLog : RealmObject {
    /** ID */
    @PrimaryKey
    var id: Long = 0

    /** 作成日 */
    var createdDate: Long = 0

    /** 更新日 */
    var updatedDate: Long = 0

    /** 日付 */
    var date: Long = 0

    /** 走行距離x1,000 */
    var milliMileage: Long = -1

    /** 燃費 */
    var fuelEfficient: Double? = null

    /** 合計走行距離x1,000 */
    var totalMilliMileage: Long? = null

    /** メモ */
    var memo: String = ""
}
