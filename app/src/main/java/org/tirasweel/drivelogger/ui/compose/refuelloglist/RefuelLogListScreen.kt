package org.tirasweel.drivelogger.ui.compose.refuelloglist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.db.RefuelLog
import org.tirasweel.drivelogger.ui.compose.DriveLogNavigationBar
import org.tirasweel.drivelogger.utils.DateFormatConverter.Companion.toLocaleDateString
import org.tirasweel.drivelogger.viewmodels.RefuelLogViewModel
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefuelLogListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    refuelLogViewModel: RefuelLogViewModel,
    onFabAddClicked: () -> Unit,
    onItemClicked: (RefuelLog) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.screen_title_refuel_logging)) }
            )
        },
        bottomBar = {
            DriveLogNavigationBar(navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onFabAddClicked) {
                Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.description_fab_new_drivelog))
            }
        }
    ) { paddingValues ->
        val logs = refuelLogViewModel.refuelLogList.value
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(logs) { log ->
                RefuelLogRow(
                    log = log,
                    modifier = Modifier.clickable { onItemClicked(log) }
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun RefuelLogRow(
    log: RefuelLog,
    modifier: Modifier = Modifier
) {
    ListItem(
        modifier = modifier,
        headlineContent = {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = Date(log.date).toLocaleDateString(),
                    modifier = Modifier.weight(1f)
                )
                log.fuelEfficiency?.let {
                    Text(text = stringResource(id = R.string.text_km_l, it))
                }
            }
        },
        supportingContent = {
            Column {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = log.totalMilliMileage?.let {
                            stringResource(id = R.string.text_km, it / 1000.0)
                        } ?: "--- km",
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "[ " + stringResource(id = R.string.text_km, log.milliMileage / 1000.0) + " ]",
                        modifier = Modifier.weight(1f)
                    )
                    Text(text = stringResource(id = R.string.text_l, log.fuelAmount))
                }
                if (log.memo.isNotEmpty()) {
                    Text(text = log.memo)
                }
            }
        }
    )
}
