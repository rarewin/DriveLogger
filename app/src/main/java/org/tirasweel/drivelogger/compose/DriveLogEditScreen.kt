package org.tirasweel.drivelogger.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.tirasweel.drivelogger.R

interface DriveLogEditScreenClickListener {
    fun onClickBack()

    fun onClickSave()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriveLogEditTopAppBar(
    modifier: Modifier = Modifier,
    clickListener: DriveLogEditScreenClickListener? = null,
) {
    TopAppBar(
        modifier = modifier,
        title = {},
        navigationIcon = {
            Box(
                modifier = Modifier
            ) {
                IconButton(onClick = { clickListener?.onClickBack() }) {
                    Icon(
                        modifier = Modifier,
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                        contentDescription = null,
                    )
                }
            }
        },
        actions = {
            Box(
                modifier = Modifier,
            ) {
                IconButton(onClick = { clickListener?.onClickSave() }) {
                    Icon(
                        modifier = Modifier,
                        painter = painterResource(id = R.drawable.ic_baseline_save_alt_24),
                        contentDescription = null,
                    )
                }
            }
        }
    )
}

@Composable
fun DriveLogEditScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            DriveLogEditTopAppBar(
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxWidth()
        ) {
            TextField(
                modifier = modifier.padding(
                    horizontal = 10.dp,
                    vertical = 5.dp,
                ),
                value = "",
                placeholder = {
                    Text(stringResource(R.string.hint_input_date))
                },
                onValueChange = {},
                maxLines = 1,
            )

            val textMileage = remember { mutableStateOf(TextFieldValue()) }

            TextField(
                modifier = modifier.padding(
                    horizontal = 10.dp,
                    vertical = 5.dp,
                ),
                value = textMileage.value,
                placeholder = {
                    Text(stringResource(R.string.hint_mileage))
                },
                onValueChange = { textMileage.value = it },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            val textFuelEfficient = remember { mutableStateOf(TextFieldValue()) }

            TextField(
                modifier = modifier.padding(
                    horizontal = 10.dp,
                    vertical = 5.dp,
                ),
                value = textFuelEfficient.value,
                placeholder = {
                    Text(stringResource(R.string.hint_fuel_efficient))
                },
                onValueChange = { textFuelEfficient.value = it },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            val textTotalMileage = remember { mutableStateOf(TextFieldValue()) }

            TextField(
                modifier = modifier.padding(
                    horizontal = 10.dp,
                    vertical = 5.dp,
                ),
                value = textTotalMileage.value,
                placeholder = {
                    Text(stringResource(R.string.hint_total_mileage))
                },
                onValueChange = { textTotalMileage.value = it },
                maxLines = 1,
            )

            val textMemo = remember { mutableStateOf(TextFieldValue()) }

            TextField(
                modifier = modifier.padding(
                    horizontal = 10.dp,
                    vertical = 5.dp,
                ),
                value = textMemo.value,
                placeholder = {
                    Text(stringResource(R.string.hint_memo))
                },
                onValueChange = { textMemo.value = it },
                minLines = 3,
            )
        }
    }
}

@Preview
@Composable
private fun DriveLogEditScreenPreview() {
    Surface {
        DriveLogEditScreen(
            modifier = Modifier.fillMaxWidth()
        )
    }
}