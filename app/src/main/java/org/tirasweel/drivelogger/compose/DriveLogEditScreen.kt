package org.tirasweel.drivelogger.compose

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.viewmodels.DriveLogEditViewModel

interface DriveLogEditScreenClickListener {
    fun onClickBack()

    fun onClickSave()

//    fun onClickDate()

    fun onClickDelete()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriveLogEditTopAppBar(
    modifier: Modifier = Modifier,
    clickListener: DriveLogEditScreenClickListener? = null,
    driveLogEditViewModel: DriveLogEditViewModel,
) {
    val isEditMode = (driveLogEditViewModel.driveLog.value != null)

    TopAppBar(
        modifier = modifier,
        title = {},
        navigationIcon = {
            Box(
                modifier = Modifier
            ) {
                IconButton(
                    onClick = { clickListener?.onClickBack() },
                ) {
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
                IconButton(
                    onClick = { clickListener?.onClickSave() },
                    enabled = driveLogEditViewModel.canSave(),
                ) {
                    Icon(
                        modifier = Modifier,
                        painter = painterResource(id = R.drawable.ic_baseline_save_alt_24),
                        contentDescription = null,
                    )
                }
            }
            if (isEditMode) {
                Box(
                    modifier = Modifier,
                ) {
                    IconButton(
                        onClick = { clickListener?.onClickDelete() },
                    ) {
                        Icon(
                            modifier = Modifier,
                            painter = painterResource(id = R.drawable.ic_baseline_delete_forever_24),
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriveLogEditScreen(
    modifier: Modifier = Modifier,
    clickListener: DriveLogEditScreenClickListener? = null,
    driveLogEditViewModel: DriveLogEditViewModel,
) {
    val debugTag = object {}.javaClass.enclosingMethod?.name

    Scaffold(
        modifier = modifier,
        topBar = {
            DriveLogEditTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                clickListener = clickListener,
                driveLogEditViewModel = driveLogEditViewModel,
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxWidth()
        ) {
            TextField(
                modifier = modifier
                    .padding(
                        horizontal = 10.dp,
                        vertical = 5.dp,
                    ),
                leadingIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            // clickListener?.onClickDate()
                            driveLogEditViewModel.isDatePickerDisplayed.value = true
                        },
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                    )
                },
                value = driveLogEditViewModel.textDate.value,
                placeholder = {
                    Text(stringResource(R.string.hint_input_date))
                },
                onValueChange = {},
                maxLines = 1,
                readOnly = true,
            )

            val isErrorOnMileage =
                (driveLogEditViewModel.textMileage.value.toDoubleOrNull() == null)

            TextField(
                modifier = modifier.padding(
                    horizontal = 10.dp,
                    vertical = 5.dp,
                ),
                value = driveLogEditViewModel.textMileage.value,
                placeholder = {
                    Text(stringResource(R.string.hint_mileage))
                },
                onValueChange = { driveLogEditViewModel.setTextMileage(it) },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = isErrorOnMileage,
                supportingText = {
                    if (isErrorOnMileage) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.hint_required),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                trailingIcon = {
                    if (isErrorOnMileage) {
                        Icon(
                            Icons.Filled.Info,
                            "error",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
            )

            TextField(
                modifier = modifier.padding(
                    horizontal = 10.dp,
                    vertical = 5.dp,
                ),
                value = driveLogEditViewModel.textFuelEfficient.value,
                placeholder = {
                    Text(stringResource(R.string.hint_fuel_efficient))
                },
                onValueChange = { driveLogEditViewModel.setTextFuelEfficient(it) },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            TextField(
                modifier = modifier.padding(
                    horizontal = 10.dp,
                    vertical = 5.dp,
                ),
                value = driveLogEditViewModel.textTotalMileage.value,
                placeholder = {
                    Text(stringResource(R.string.hint_total_mileage))
                },
                onValueChange = { driveLogEditViewModel.setTextTotalMileage(it) },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            TextField(
                modifier = modifier.padding(
                    horizontal = 10.dp,
                    vertical = 5.dp,
                ),
                value = driveLogEditViewModel.textMemo.value,
                placeholder = {
                    Text(stringResource(R.string.hint_memo))
                },
                onValueChange = { driveLogEditViewModel.setTextMemo(it) },
                minLines = 3,
            )
        }
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = driveLogEditViewModel.date.value,  // TODO: 時差で日付がズレる
    )

    if (driveLogEditViewModel.isDatePickerDisplayed.value) {
        DatePickerDialog(
            modifier = Modifier,
            onDismissRequest = { driveLogEditViewModel.isDatePickerDisplayed.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        Log.d(debugTag, "date: ${datePickerState.selectedDateMillis}")
                        datePickerState.selectedDateMillis?.let {
                            driveLogEditViewModel.setDate(it)
                            driveLogEditViewModel.isDatePickerDisplayed.value = false
                        }
                    },
                    enabled = (datePickerState.selectedDateMillis != null),
                ) {
                    Text(stringResource(id = R.string.title_ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        driveLogEditViewModel.isDatePickerDisplayed.value = false
                    }
                ) {
                    Text(stringResource(id = R.string.title_cancel))
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Preview
@Composable
private fun DriveLogEditScreenPreview() {
    Surface {
        DriveLogEditScreen(
            modifier = Modifier.fillMaxWidth(),
            driveLogEditViewModel = DriveLogEditViewModel(),
        )
    }
}