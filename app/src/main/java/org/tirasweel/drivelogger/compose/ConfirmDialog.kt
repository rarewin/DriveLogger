package org.tirasweel.drivelogger.compose

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme

@Composable
fun ConfirmDialog(
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { /*TODO*/ },
        confirmButton = { /*TODO*/ },
    )
}

@Preview
@Composable
private fun ConfirmDialog() {
    DriveLoggerTheme {
        ConfirmDialog()
    }
}
