package org.tirasweel.drivelogger.data

import android.content.Context
import androidx.room.Room
import org.tirasweel.drivelogger.db.AppDatabase
import org.tirasweel.drivelogger.interfaces.DriveLogsRepository
import org.tirasweel.drivelogger.interfaces.RoomDriveLogsRepository

interface AppContainer {
    val driveLogsRepository: DriveLogsRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, "drive_logger_db")
            .allowMainThreadQueries() // Maintain similar behavior to Realm's writeBlocking for now, but consider migrating to coroutines
            .build()
    }

    override val driveLogsRepository: DriveLogsRepository by lazy {
        RoomDriveLogsRepository(database.driveLogDao())
    }
}
