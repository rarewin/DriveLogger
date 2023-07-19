package org.tirasweel.drivelogger.ui.compose

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.interfaces.Destination
import org.tirasweel.drivelogger.interfaces.DriveLogList
import org.tirasweel.drivelogger.interfaces.RefuelLogList
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme
import org.tirasweel.drivelogger.viewmodels.AppViewModel

@Composable
fun DriveLogNavigationBar(
    modifier: Modifier = Modifier,
    onItemClicked: (Destination) -> Unit,
    appBarViewModel: AppViewModel,
) {
    var currentMode = appBarViewModel.currentMode.value

    NavigationBar(
        modifier = modifier,
    ) {
        NavigationBarItem(
            selected = (currentMode == DriveLogList),
            onClick = {
                if (currentMode != DriveLogList) {
                    currentMode = DriveLogList
                    onItemClicked(DriveLogList)
                }
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_directions_car_24),
                    contentDescription = null,
                )
            },
            label = { Text(stringResource(id = R.string.screen_title_drive_logging)) }
        )
        NavigationBarItem(
            selected = (currentMode == RefuelLogList),
            onClick = {
                if (currentMode != RefuelLogList) {
                    currentMode = RefuelLogList
                    onItemClicked(RefuelLogList)
                }
            },
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
    DriveLoggerTheme {
        DriveLogNavigationBar(
            modifier = Modifier,
            appBarViewModel = AppViewModel(),
            onItemClicked = { _ ->

            }
        )
    }
}