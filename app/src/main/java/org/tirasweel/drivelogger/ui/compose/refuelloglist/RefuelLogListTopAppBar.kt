package org.tirasweel.drivelogger.ui.compose.refuelloglist

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.classes.SortOrderType
import org.tirasweel.drivelogger.ui.compose.driveloglist.DriveLogListSortingMenuItem
import org.tirasweel.drivelogger.viewmodels.RefuelLogViewModel

interface RefuelLogListTopAppBarClickListener {
    fun onClickExport()
    fun onClickImport()
    fun onSortOrderChanged(sortOrderType: SortOrderType)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefuelLogListTopAppBar(
    modifier: Modifier = Modifier,
    refuelLogViewModel: RefuelLogViewModel,
    initialImportExportMenuExpanded: Boolean = false,
    initialSortMenuExpanded: Boolean = false,
    clickListener: RefuelLogListTopAppBarClickListener? = null,
) {
    var importExportMenuExpanded by remember { mutableStateOf(initialImportExportMenuExpanded) }
    var sortMenuExpanded by remember { mutableStateOf(initialSortMenuExpanded) }

    TopAppBar(
        modifier = modifier,
        title = { Text(stringResource(id = R.string.screen_title_refuel_logging)) },
        actions = {
            Box(modifier = Modifier) {
                IconButton(onClick = { importExportMenuExpanded = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_import_export_24),
                        contentDescription = null,
                    )
                }
                DropdownMenu(
                    expanded = importExportMenuExpanded,
                    onDismissRequest = { importExportMenuExpanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.menu_title_import)) },
                        onClick = {
                            clickListener?.onClickImport()
                            importExportMenuExpanded = false
                        },
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.menu_title_export)) },
                        onClick = {
                            clickListener?.onClickExport()
                            importExportMenuExpanded = false
                        },
                    )
                }
            }
            Box(modifier = Modifier) {
                IconButton(onClick = { sortMenuExpanded = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_sort_24),
                        contentDescription = stringResource(R.string.menu_title_sort),
                    )
                }
                DropdownMenu(
                    expanded = sortMenuExpanded,
                    onDismissRequest = { sortMenuExpanded = false },
                ) {
                    SortOrderType.values().forEach { sortOrderType ->
                        DriveLogListSortingMenuItem(
                            currentSortOrder = refuelLogViewModel.logListState.sortOrder,
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
        }
    )
}
