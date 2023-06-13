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
import androidx.compose.ui.tooling.preview.Preview
import org.tirasweel.drivelogger.fragments.LogListFragment
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme
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

@Preview
@Composable
private fun DriveLogListScreenPreview() {
    DriveLoggerTheme {
        DriveLogListScreen(
            modifier = Modifier.fillMaxWidth(),
            driveLogListViewModel = DriveLogListViewModel(),
        )
    }
}
