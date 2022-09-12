package org.tirasweel.drivelogger.db

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

//enum class MileageUnit : RealmObject {
//    KiloMeter,
//    Mile;
//
//    var enum: MileageUnit
//        get() {
//            return valueOf(enumDescription)
//        }
//        set(newValue) {
//            enumDescription = newValue.name
//        }
//    private var enumDescription: String = MileageUnit.KiloMeter.name
//}

class DriveLog : RealmObject {
    /**
     * ID
     */
    @PrimaryKey
    var id: Long = 128

    /**
     * 作成日
     */
    var createdDate: Long = 0

    /**
     * 更新日
     */
    var updatedDate: Long = 0

    /**
     * 日付
     */
    var date: Long = 0

    /**
     * 走行距離x1,000
     */
    var milliMileage: Long = 0

    /**
     * 距離の単位
     */
    // var mileageUnit: MileageUnit = MileageUnit.KiloMeter

    /**
     * メモ
     */
    var memo: String = ""
}