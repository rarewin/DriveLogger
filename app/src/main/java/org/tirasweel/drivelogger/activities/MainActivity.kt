package org.tirasweel.drivelogger.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import org.tirasweel.drivelogger.BuildConfig
import org.tirasweel.drivelogger.ui.compose.DriveLoggerApp
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme
import org.tirasweel.drivelogger.viewmodels.AppViewModel
import org.tirasweel.drivelogger.viewmodels.DriveLogViewModel
import org.tirasweel.drivelogger.viewmodels.RefuelLogViewModel

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG: String =
            "${BuildConfig.APPLICATION_ID}.MainActivity"
    }

    private val appBarViewModel: AppViewModel by viewModels()
    private val driveLogViewModel: DriveLogViewModel by viewModels()
    private val refuelLogViewModel: RefuelLogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DriveLoggerTheme {
                DriveLoggerApp(
                    driveLogViewModel = driveLogViewModel,
                    refuelLogViewModel = refuelLogViewModel,
                    appBarViewModel = appBarViewModel,
                )
            }
        }
    }
}