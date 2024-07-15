package org.tirasweel.drivelogger.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import org.tirasweel.drivelogger.ui.compose.DriveLoggerApp
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme
import org.tirasweel.drivelogger.viewmodels.DriveLogViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DriveLoggerTheme {
                DriveLoggerApp(
                    driveLogViewModel = viewModel(factory = DriveLogViewModel.Factory),
                )
            }
        }
    }
}