package org.tirasweel.drivelogger.ui.compose

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.activities.ScreenMode

@Composable
fun DriveLogNavigationBar(modifier: Modifier = Modifier) {
    NavigationBar(
        modifier = modifier,
    ) {
        var currentMode by remember { mutableStateOf(ScreenMode.DriveLoggingScreen) }

        NavigationBarItem(
            selected = (currentMode == ScreenMode.DriveLoggingScreen),
            onClick = { currentMode = ScreenMode.DriveLoggingScreen },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_directions_car_24),
                    contentDescription = null,
                )
            },
            label = { Text(stringResource(id = R.string.screen_title_drive_logging)) }
        )
        NavigationBarItem(
            selected = (currentMode == ScreenMode.RefuelLoggingScreen),
            onClick = { /* currentMode = ScreenMode.RefuelLoggingScreen */ },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_local_gas_station_24),
                    contentDescription = null,
                )
            },
            label = { Text(stringResource(id = R.string.screen_title_refuel_logging)) }
        )
    }
}

@Preview
@Composable
private fun DriveLogNavigationBarPreview() {
    DriveLogNavigationBar(modifier = Modifier)
}