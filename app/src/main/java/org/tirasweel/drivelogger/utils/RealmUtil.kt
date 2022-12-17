package org.tirasweel.drivelogger.utils

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.migration.AutomaticSchemaMigration
import org.tirasweel.drivelogger.db.DriveLog

class RealmUtil {
    companion object {
        /**
         * Realmのインスタンスを作成する
         */
        fun createRealm(): Realm {
            val config = RealmConfiguration.Builder(schema = setOf(DriveLog::class))
                .schemaVersion(1L)
                .migration(AutomaticSchemaMigration { })
                .build()

            return Realm.open(config)
        }
    }
}