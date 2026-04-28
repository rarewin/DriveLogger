package org.tirasweel.drivelogger.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `refuel_log` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`createdDate` INTEGER NOT NULL, " +
                    "`updatedDate` INTEGER NOT NULL, " +
                    "`date` INTEGER NOT NULL, " +
                    "`totalMilliMileage` INTEGER, " +
                    "`milliMileage` INTEGER NOT NULL, " +
                    "`fuelAmount` REAL NOT NULL, " +
                    "`fuelPrice` INTEGER, " +
                    "`fuelEfficiency` REAL, " +
                    "`memo` TEXT NOT NULL)"
        )
    }
}
