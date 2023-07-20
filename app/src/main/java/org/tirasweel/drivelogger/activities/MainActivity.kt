package org.tirasweel.drivelogger.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import org.tirasweel.drivelogger.ui.compose.DriveLoggerApp
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme
import org.tirasweel.drivelogger.viewmodels.DriveLogViewModel

class MainActivity : ComponentActivity() {

    private val driveLogViewModel: DriveLogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DriveLoggerTheme {
                DriveLoggerApp(
                    driveLogViewModel = driveLogViewModel,
                )
            }
        }
    }
}