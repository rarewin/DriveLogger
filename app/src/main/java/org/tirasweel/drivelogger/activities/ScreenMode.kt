package org.tirasweel.drivelogger.activities

import org.tirasweel.drivelogger.DriveLogList
import org.tirasweel.drivelogger.DriveLoggerDestination
import org.tirasweel.drivelogger.RefuelLogList

enum class ScreenMode {
    DriveLoggingScreen,
    RefuelLoggingScreen;

    val destination: DriveLoggerDestination
        get() = when (this) {
            DriveLoggingScreen -> DriveLogList
            RefuelLoggingScreen -> RefuelLogList
        }
}
