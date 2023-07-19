package org.tirasweel.drivelogger.ui.compose.driveloglist

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
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.interfaces.LogListInteractionListener
import org.tirasweel.drivelogger.ui.compose.DriveLogNavigationBar
import org.tirasweel.drivelogger.ui.compose.common.ConfirmDialog
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme
import org.tirasweel.drivelogger.viewmodels.DriveLogViewModel

@Composable
fun DriveLogListScreen(
    modifier: Modifier = Modifier,
    driveLogViewModel: DriveLogViewModel,
    clickListener: LogListInteractionListener? = null,
) {
    val driveLogs by driveLogViewModel.driveLogList

    LazyColumn(
        modifier = modifier,
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

    ConfirmDialog(
        isDisplayed = driveLogViewModel.uiState.isConfirmDialogForOverwriteExport,
        onResponse = { response ->
            clickListener?.onConfirmOverwriteExport(response)
        },
        textId = R.string.message_export_file_already_exists,
    )
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
