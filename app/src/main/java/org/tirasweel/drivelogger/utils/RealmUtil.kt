package org.tirasweel.drivelogger.utils

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.migration.AutomaticSchemaMigration
import io.realm.kotlin.query.Sort
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

        fun getNewDriveLogId(realm: Realm): Long {
            val maxIdLog =
                realm.query<DriveLog>().sort("id", Sort.DESCENDING).limit(1).find()
            val maxId = maxIdLog.firstOrNull()?.id

            return maxId?.plus(1) ?: 1L
        }
    }
}