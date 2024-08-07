package org.tirasweel.drivelogger.ui.compose.driveloglist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import org.tirasweel.drivelogger.classes.SortOrderType
import org.tirasweel.drivelogger.fake.FakeDriveLogsRepository
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme
import org.tirasweel.drivelogger.viewmodels.DriveLogViewModel

interface DriveLogListTopAppBarClickListener {
    fun onClickExport()

    fun onSortOrderChanged(sortOrderType: SortOrderType)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriveLogListTopAppBar(
    modifier: Modifier = Modifier,
    driveLogViewModel: DriveLogViewModel,
    initialImportExportMenuExpanded: Boolean = false,
    initialSortMenuExpanded: Boolean = false,
    clickListener: DriveLogListTopAppBarClickListener? = null,
) {
    var importExportMenuExpanded by remember { mutableStateOf(initialImportExportMenuExpanded) }
    var sortMenuExpanded by remember { mutableStateOf(initialSortMenuExpanded) }

    TopAppBar(modifier = modifier, title = {}, actions = {
        Box(modifier = modifier) {
            IconButton(onClick = { importExportMenuExpanded = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_import_export_24),
                    modifier = modifier,
                    contentDescription = null,
                )
            }
            DropdownMenu(
                modifier = modifier,
                expanded = importExportMenuExpanded,
                onDismissRequest = { importExportMenuExpanded = false },
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.menu_title_export)) },
                    onClick = {
                        clickListener?.onClickExport()
                        importExportMenuExpanded = false
                    },
                )
            }
        }
        Box(modifier = modifier) {
            IconButton(onClick = { sortMenuExpanded = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_sort_24),
                    modifier = modifier,
                    contentDescription = null,
                )
            }
            DropdownMenu(
                modifier = modifier,
                expanded = sortMenuExpanded,
                onDismissRequest = { sortMenuExpanded = false },
            ) {
                SortOrderType.values().forEach { sortOrderType ->
                    DriveLogListSortingMenuItem(
                        currentSortOrder = driveLogViewModel.logListState.sortOrder,
                        sortOrderType = sortOrderType,
                        textId = sortOrderType.menuTextId,
                        onSortOrderChanged = {
                            sortMenuExpanded = false
                            clickListener?.onSortOrderChanged(it)
                        },
                    )
                }
            }
        }
    })
}

@Preview
@Composable
fun DriveLogListTopAppBarPreview() {
    DriveLoggerTheme {
        Surface(
            modifier = Modifier.fillMaxWidth()
        ) {
            DriveLogListTopAppBar(
                modifier = Modifier,
                driveLogViewModel = DriveLogViewModel(
                    driveLogsRepository = FakeDriveLogsRepository(),
                ),
            )
        }
    }
}
