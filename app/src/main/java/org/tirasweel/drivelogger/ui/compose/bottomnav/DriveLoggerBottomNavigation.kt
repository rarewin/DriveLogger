package org.tirasweel.drivelogger.ui.compose.bottomnav

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
import org.tirasweel.drivelogger.activities.ScreenMode
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme

/**
 * BottomNavigationのクリックリスナー
 */
interface DriveLoggerBottomNavigationClickListener {

    /**
     * モード変更
     * @param mode  スクリーンモード
     */
    fun onModeChanged(mode: ScreenMode) {}
}

@Composable
fun DriveLoggerBottomNavigation(
    modifier: Modifier = Modifier,
    currentMode: ScreenMode,
    driveLoggerBottomNavigationClickListener: DriveLoggerBottomNavigationClickListener,
) {
    NavigationBar(
        modifier = modifier,
    ) {
        NavigationBarItem(
            selected = (currentMode == ScreenMode.DriveLoggingScreen),
            onClick = { driveLoggerBottomNavigationClickListener.onModeChanged(ScreenMode.DriveLoggingScreen) },
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
            onClick = { driveLoggerBottomNavigationClickListener.onModeChanged(ScreenMode.RefuelLoggingScreen) },
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
private fun DriveLoggerBottomNavigationPreview() {
    DriveLoggerTheme {
        DriveLoggerBottomNavigation(
            modifier = Modifier,
            currentMode = ScreenMode.DriveLoggingScreen,
            driveLoggerBottomNavigationClickListener = object :
                DriveLoggerBottomNavigationClickListener {
                override fun onModeChanged(mode: ScreenMode) {}
            }
        )
    }
}