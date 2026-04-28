package org.tirasweel.drivelogger.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DriveLog::class, RefuelLog::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun driveLogDao(): DriveLogDao
    abstract fun refuelLogDao(): RefuelLogDao
}
