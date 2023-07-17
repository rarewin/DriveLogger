package org.tirasweel.drivelogger.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.ui.theme.DriveLoggerTheme


/**
 *
 */
@Composable
fun DriveLogEditForm(
    modifier: Modifier = Modifier,
    editedLog: DriveLog,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
    }
}


/**
 *
 */
@Composable
fun DriveLogEditTextFormPlain(
    modifier: Modifier = Modifier,
    data: MutableState<String> = mutableStateOf(""),
    @StringRes hintStringId: Int,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    isError: Boolean = false,
    maxLines: Int = 1,
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = data.value,
        placeholder = {
            Text(
                text = stringResource(hintStringId)
            )
        },
        isError = isError,
        trailingIcon = {
            if (isError) {
                Icon(
                    Icons.Filled.Info,
                    "error",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        },
        onValueChange = { data.value = it },
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
    )
}

@Preview
@Composable
private fun DriveLogEditTextFormPlainPreview() {
    DriveLoggerTheme {
        Surface {
            DriveLogEditTextFormPlain(
                hintStringId = R.string.hint_mileage,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}