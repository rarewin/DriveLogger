package org.tirasweel.drivelogger.db

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index

class DriveLog: RealmObject {
    /**
     * ID
     */
    @Index
    var id: ObjectId = ObjectId.create()
}