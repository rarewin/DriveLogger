package org.tirasweel.drivelogger.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.tirasweel.drivelogger.interfaces.LogListInteractionListener
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme
import org.tirasweel.drivelogger.viewmodels.DriveLogViewModel

@Composable
fun DriveLogListScreen(
    modifier: Modifier = Modifier,
    driveLogViewModel: DriveLogViewModel,
    clickListener: LogListInteractionListener? = null,
    appBarClickListener: DriveLogListTopAppBarClickListener? = null,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            DriveLogListTopAppBar(
                modifier = Modifier,
                driveLogViewModel = driveLogViewModel,
                clickListener = appBarClickListener,
            )
        },
        bottomBar = {
            DriveLogNavigationBar()
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(all = 16.dp),
                onClick = {
                    clickListener?.onFabAddClicked()
                    // navController.navigate(DriveLogEdit.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add",
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { contentPadding ->
        val driveLogs by driveLogViewModel.driveLogList

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
                    modifier = Modifier.fillMaxWidth(),
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
            driveLogViewModel = DriveLogViewModel(),
        )
    }
}
