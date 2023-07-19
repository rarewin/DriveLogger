package org.tirasweel.drivelogger.ui.compose.common

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme

@Composable
fun ConfirmDialog(
    modifier: Modifier = Modifier,
    isDisplayed: MutableState<Boolean>,
    onResponse: (Boolean) -> Unit,
    @StringRes titleId: Int? = null,
    @StringRes textId: Int,
    @StringRes confirmStringId: Int = R.string.title_ok,
    @StringRes dismissStringId: Int? = R.string.title_cancel,
) {
    if (isDisplayed.value) {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = {
                if (dismissStringId != null) {
                    isDisplayed.value = false
                    onResponse(false)
                }
            },
            title = {
                if (titleId != null) {
                    Text(text = stringResource(id = titleId))
                }
            },
            text = {
                Text(text = stringResource(id = textId))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        isDisplayed.value = false
                        onResponse(true)
                    }
                ) {
                    Text(stringResource(id = confirmStringId))
                }
            },
            dismissButton = {
                if (dismissStringId != null) {
                    TextButton(
                        onClick = {
                            isDisplayed.value = false
                            onResponse(false)
                        }
                    ) {
                        Text(stringResource(id = dismissStringId))
                    }
                }
            },
        )
    }
}

@Preview
@Composable
private fun ConfirmDialogPreview() {
    val isDisplayed = remember { mutableStateOf(true) }
    DriveLoggerTheme {
        ConfirmDialog(
            isDisplayed = isDisplayed,
            onResponse = { _ -> },
            textId = R.string.message_overwrite_drivelog,
        )
    }
}
