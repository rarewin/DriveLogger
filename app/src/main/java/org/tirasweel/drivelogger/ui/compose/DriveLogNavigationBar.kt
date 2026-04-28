package org.tirasweel.drivelogger.ui.compose

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.tirasweel.drivelogger.DriveLogList
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.RefuelLogList

@Composable
fun DriveLogNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = modifier,
    ) {
        NavigationBarItem(
            selected = (currentRoute == DriveLogList.route),
            onClick = {
                if (currentRoute != DriveLogList.route) {
                    navController.navigate(DriveLogList.route) {
                        popUpTo(DriveLogList.route) { inclusive = true }
                    }
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
            selected = (currentRoute == RefuelLogList.route),
            onClick = {
                if (currentRoute != RefuelLogList.route) {
                    navController.navigate(RefuelLogList.route) {
                        popUpTo(DriveLogList.route)
                    }
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
    DriveLogNavigationBar(navController = rememberNavController())
}
