package org.tirasweel.drivelogger.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DriveLog::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun driveLogDao(): DriveLogDao
}
