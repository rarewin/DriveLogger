package org.tirasweel.drivelogger.ui.compose.refuellist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.tirasweel.drivelogger.activities.ScreenMode
import org.tirasweel.drivelogger.ui.compose.bottomnav.DriveLoggerBottomNavigation
import org.tirasweel.drivelogger.ui.compose.bottomnav.DriveLoggerBottomNavigationClickListener
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme

@Composable
fun RefuelListScreen(
    modifier: Modifier = Modifier,
    bottomNavigationClickListener: DriveLoggerBottomNavigationClickListener,
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            DriveLoggerBottomNavigation(
                currentMode = ScreenMode.RefuelLoggingScreen,
                driveLoggerBottomNavigationClickListener = bottomNavigationClickListener,
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxWidth()
        ) {

        }
    }
}

@Preview
@Composable
private fun RefuelLogListScreenPreview() {
    DriveLoggerTheme {
        RefuelListScreen(
            bottomNavigationClickListener = object : DriveLoggerBottomNavigationClickListener {}
        )
    }
}