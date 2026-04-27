package org.tirasweel.drivelogger.data

import android.content.Context
import androidx.room.Room
import org.tirasweel.drivelogger.db.AppDatabase
import org.tirasweel.drivelogger.interfaces.DriveLogsRepository
import org.tirasweel.drivelogger.interfaces.RefuelLogsRepository
import org.tirasweel.drivelogger.interfaces.RoomDriveLogsRepository
import org.tirasweel.drivelogger.interfaces.RoomRefuelLogsRepository

interface AppContainer {
    val driveLogsRepository: DriveLogsRepository
    val refuelLogsRepository: RefuelLogsRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, "drive_logger_db")
            .allowMainThreadQueries() // Maintain similar behavior to Realm's writeBlocking for now, but consider migrating to coroutines
            .fallbackToDestructiveMigration()
            .build()
    }

    override val driveLogsRepository: DriveLogsRepository by lazy {
        RoomDriveLogsRepository(database.driveLogDao())
    }

    override val refuelLogsRepository: RefuelLogsRepository by lazy {
        RoomRefuelLogsRepository(database.refuelLogDao())
    }
}
