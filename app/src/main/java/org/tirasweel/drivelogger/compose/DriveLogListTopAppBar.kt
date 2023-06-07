package org.tirasweel.drivelogger.compose

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriveLogListTopAppBar(
    modifier: Modifier = Modifier,
) {
    TopAppBar(modifier = modifier,
        title = {},
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_import_export_24),
                    modifier = modifier,
                    contentDescription = null,
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_sort_24),
                    modifier = modifier,
                    contentDescription = null,
                )
            }
        }
    )
}

@Preview
@Composable
fun DriveLogListTopAppBarPreview() {
    DriveLoggerTheme {
        DriveLogListTopAppBar(modifier = Modifier)
    }
}