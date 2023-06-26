package org.tirasweel.drivelogger.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.classes.SortOrderType
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme
import org.tirasweel.drivelogger.viewmodels.DriveLogListViewModel

@Composable
fun DriveLogListSortMenu(
    modifier: Modifier = Modifier,
    driveLogListViewModel: DriveLogListViewModel,
) {
    DropdownMenuItem(
        modifier = modifier,
        text = {
            Row {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = (if (driveLogListViewModel.sortOrder.value == SortOrderType.AscendingDate) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Transparent
                    }),
                    modifier = Modifier.padding(end = 8.dp),
                )
                Text(stringResource(R.string.sort_date_ascending))
            }
        },
        onClick = {
            driveLogListViewModel.setDriveLogOrder(SortOrderType.AscendingDate)
        },
    )
    DropdownMenuItem(
        text = {
            Row {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = (if (driveLogListViewModel.sortOrder.value == SortOrderType.DescendingDate) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Transparent
                    }),
                    modifier = Modifier.padding(end = 8.dp),
                )
                Text(stringResource(R.string.sort_date_descending))
            }
        },
        onClick = {
            driveLogListViewModel.setDriveLogOrder(SortOrderType.DescendingDate)
        },
    )
}

@Preview
@Composable
private fun DriveLogListSortMenuPreview() {
    DriveLoggerTheme {
        Surface(
            modifier = Modifier,
        ) {
            DropdownMenu(
                modifier = Modifier,
                expanded = true,
                onDismissRequest = {},
            ) {
                DriveLogListSortMenu(
                    modifier = Modifier.fillMaxWidth(),
                    driveLogListViewModel = DriveLogListViewModel(),
                )
            }
        }
    }
}