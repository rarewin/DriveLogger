package org.tirasweel.drivelogger.utils

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.tirasweel.drivelogger.db.DriveLog

class RealmUtil {
    companion object {
        /**
         * Realmのインスタンスを作成する
         */
        fun createRealm(): Realm {
            val config = RealmConfiguration.Builder(schema = setOf(DriveLog::class))
                .build()

            return Realm.open(config)
        }
    }
}