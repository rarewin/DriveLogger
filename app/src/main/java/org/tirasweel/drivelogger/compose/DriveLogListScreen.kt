package org.tirasweel.drivelogger.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.fragments.LogListFragment
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme

@Composable
fun DriveLogListScreen(
    modifier: Modifier = Modifier,
    driveLogs: List<DriveLog>,
    clickListener: LogListFragment.LogListInteractionListener? = null,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            DriveLogListTopAppBar(
                modifier = modifier,
            )
        }
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier.padding(contentPadding)
        ) {
            items(
                items = driveLogs,
                key = { driveLog -> driveLog.id }
            ) { driveLog ->
                DriveLogRow(
                    driveLog = driveLog,
                    clickListener = clickListener,
                    modifier = modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun DriveLogListScreenPreview() {
    val driveLogs = listOf(
        DriveLog().apply {
            id = 0
            createdDate = 111
            date = 111111
            milliMileage = 2300
            fuelEfficient = 17.1
        },
        DriveLog().apply {
            id = 1
            createdDate = 110
            date = 11122301
            milliMileage = 2330
            fuelEfficient = 23.1
        },
    )

    DriveLoggerTheme {
        DriveLogListScreen(
            modifier = Modifier.fillMaxWidth(),
            driveLogs = driveLogs,
        )
    }
}