package org.tirasweel.drivelogger.compose

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
import androidx.compose.ui.tooling.preview.Preview
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriveLogListTopAppBar(
    modifier: Modifier = Modifier,
    initialImportExportMenuExpanded: Boolean = false,
    initialSortMenuExpanded: Boolean = false,
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
                    onClick = { /*TODO*/ },
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
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.sort_date_ascending)) },
                    onClick = { /*TODO*/ },
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.sort_date_descending)) },
                    onClick = { /*TODO*/ },
                )
            }
        }
    })
}

@Preview
@Composable
fun DriveLogListTopAppBarPreview() {
    DriveLoggerTheme {
        DriveLogListTopAppBar(modifier = Modifier)
    }
}