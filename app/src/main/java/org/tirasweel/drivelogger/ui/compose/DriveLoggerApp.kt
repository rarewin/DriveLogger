package org.tirasweel.drivelogger.ui.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme
import org.tirasweel.drivelogger.viewmodels.AppViewModel
import org.tirasweel.drivelogger.viewmodels.DriveLogViewModel
import org.tirasweel.drivelogger.viewmodels.RefuelLogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriveLoggerApp(
    modifier: Modifier = Modifier,
    driveLogViewModel: DriveLogViewModel,
    refuelLogViewModel: RefuelLogViewModel,
    appBarViewModel: AppViewModel,
) {
    val navController = rememberNavController()

    Scaffold(
        modifier = modifier,
        topBar = {
            DriveLoggerTopAppBar(
                appBarViewModel = appBarViewModel,
            )
        },
        bottomBar = {
            DriveLogNavigationBar(
                onItemClicked = { screenMode ->
                    appBarViewModel.currentMode.value = screenMode
                    navController.navigate(screenMode.route)
                },
                appBarViewModel = appBarViewModel,
            )
        },
    ) { contentPadding ->
        DriveLoggerNavHost(
            modifier = Modifier.padding(contentPadding),
            navController = navController,
            driveLogViewModel = driveLogViewModel,
            refuelLogViewModel = refuelLogViewModel,
        )
    }
}

@Preview
@Composable
private fun DriveLoggerAppPreview() {
    DriveLoggerTheme {
        DriveLoggerApp(
            driveLogViewModel = DriveLogViewModel(),
            refuelLogViewModel = RefuelLogViewModel(),
            appBarViewModel = AppViewModel(),
        )
    }
}