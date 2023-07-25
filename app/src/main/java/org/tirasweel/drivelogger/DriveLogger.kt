package org.tirasweel.drivelogger

import android.app.Application
import timber.log.Timber.DebugTree
import timber.log.Timber.Forest.plant


class DriveLogger : Application() {
    override fun onCreate() {
        super.onCreate()

        plant(DebugTree())
    }
}