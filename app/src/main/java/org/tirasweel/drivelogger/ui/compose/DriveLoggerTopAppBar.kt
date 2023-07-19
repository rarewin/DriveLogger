package org.tirasweel.drivelogger.ui.compose

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme
import org.tirasweel.drivelogger.viewmodels.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriveLoggerTopAppBar(
    modifier: Modifier = Modifier,
    appBarViewModel: AppViewModel,
) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = {},
        title = {
            Text(text = appBarViewModel.currentMode.value.title)
        },
        actions = {},
    )
}

@Preview
@Composable
private fun DriveLoggerTopAppBarPreview() {
    DriveLoggerTheme {
        DriveLoggerTopAppBar(
            modifier = Modifier,
            appBarViewModel = AppViewModel(),
        )
    }
}