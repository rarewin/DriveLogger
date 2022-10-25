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
    var id: Long

    /**
     * 作成日
     */
    var createdDate: Long

    /**
     * 更新日
     */
    var updatedDate: Long

    /**
     * 日付
     */
    var date: Long

    /**
     * 走行距離x1,000
     */
    var milliMileage: Long

    /**
     * 距離の単位
     */
    // var mileageUnit: MileageUnit = MileageUnit.KiloMeter

    /**
     * メモ
     */
    var memo: String = ""

    constructor() {
        id = 0
        createdDate = 0
        updatedDate = 0
        date = 0
        milliMileage = 0
    }

    constructor(log: DriveLog) {
        id = log.id
        createdDate = log.createdDate
        updatedDate = log.updatedDate
        date = log.date
        milliMileage = log.milliMileage
    }
}