package org.tirasweel.drivelogger

import android.app.Application
import org.tirasweel.drivelogger.data.AppContainer
import org.tirasweel.drivelogger.data.DefaultAppContainer
import timber.log.Timber

class DriveLogger : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()

        container = DefaultAppContainer()

        Timber.plant(Timber.DebugTree())
    }
}
