package org.tirasweel.drivelogger.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme
import org.tirasweel.drivelogger.viewmodels.DriveLogViewModel

@Composable
fun DriveLoggerApp(
    modifier: Modifier = Modifier,
    driveLogViewModel: DriveLogViewModel,
) {
    val navController = rememberNavController()
    // val currentBackStack by navController.currentBackStackEntryAsState()
    // val currentDestination = currentBackStack?.destination
    // val currentScreen = driveLogScreenList.find { it.route == currentDestination?.route }

    DriveLoggerNavHost(
        modifier = modifier,
        navController = navController,
        driveLogViewModel = driveLogViewModel,
    )
}

@Preview
@Composable
private fun DriveLoggerAppPreview() {
    DriveLoggerTheme {
        DriveLoggerApp(
            driveLogViewModel = DriveLogViewModel(),
        )
    }
}