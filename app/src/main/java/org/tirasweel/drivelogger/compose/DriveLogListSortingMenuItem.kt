package org.tirasweel.drivelogger.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.tirasweel.drivelogger.classes.SortOrderType

@Composable
fun DriveLogListSortingMenuItem(
    modifier: Modifier = Modifier,
    currentSortOrder: MutableState<SortOrderType>,
    sortOrderType: SortOrderType,
    @StringRes textId: Int,
    onSortOrderChanged: (SortOrderType) -> Unit,
) {
    DropdownMenuItem(
        modifier = modifier,
        text = {
            Row {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = (if (currentSortOrder.value == sortOrderType) {
                        MaterialTheme.colorScheme.primary  // TODO
                    } else {
                        Color.Transparent
                    }),
                    modifier = Modifier.padding(end = 8.dp),
                )
                Text(stringResource(textId)) // R.string.sort_date_ascending
            }
        },
        onClick = {
            currentSortOrder.value = sortOrderType
            onSortOrderChanged(sortOrderType)
        },
    )
}
