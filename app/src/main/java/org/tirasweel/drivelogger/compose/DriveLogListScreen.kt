package org.tirasweel.drivelogger.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.tirasweel.drivelogger.fragments.LogListFragment
import org.tirasweel.drivelogger.viewmodels.DriveLogListViewModel

@Composable
fun DriveLogListScreen(
    modifier: Modifier = Modifier,
    driveLogListViewModel: DriveLogListViewModel,
    clickListener: LogListFragment.LogListInteractionListener? = null,
    appBarClickListener: DriveLogListTopAppBarClickListener? = null,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            DriveLogListTopAppBar(
                modifier = Modifier,
                driveLogListViewModel = driveLogListViewModel,
                clickListener = appBarClickListener,
            )
        }
    ) { contentPadding ->
        val driveLogs by driveLogListViewModel.driveLogsStateFlow.collectAsState()
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

/*
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
            sortOrder = SortOrderType.DescendingDate,
        )
    }
}
*/
