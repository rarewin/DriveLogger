package org.tirasweel.drivelogger.ui.compose.drivelogedit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import org.tirasweel.drivelogger.ui.compose.bottomnav.DriveLoggerBottomNavigationClickListener
import org.tirasweel.drivelogger.ui.compose.common.ConfirmDialog
import org.tirasweel.drivelogger.viewmodels.DriveLogViewModel
import timber.log.Timber

interface DriveLogEditScreenClickListener {
    fun onClickBack()

    fun onClickSave()

    fun onClickDelete()

    /** 変更破棄確認ダイアログの結果 */
    fun onConfirmDiscardModification(confirm: Boolean)

    /** 上書き確認ダイアログの結果 */
    fun onConfirmOverwrite(confirm: Boolean)

    /** 削除確認ダイアログの結果 */
    fun onConfirmDelete(confirm: Boolean)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriveLogEditTopAppBar(
    modifier: Modifier = Modifier,
    clickListener: DriveLogEditScreenClickListener? = null,
    driveLogViewModel: DriveLogViewModel,
) {
    val isEditMode = (driveLogViewModel.logFormState.isEditingMode())

    TopAppBar(
        modifier = modifier,
        title = {},
        navigationIcon = {
            Box(
                modifier = Modifier
            ) {
                IconButton(
                    onClick = {
                        if (driveLogViewModel.logFormState.isEdited()) {
                            driveLogViewModel
                                .uiState
                                .isConfirmDialogForDiscardModificationDisplayed
                                .value = true
                        } else {
                            clickListener?.onClickBack()
                        }
                    },
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
                    enabled = driveLogViewModel.canSave(),
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
                        onClick = {
                            driveLogViewModel.uiState
                                .isConfirmDialogForDeleteLogDisplayed
                                .value = true
                        },
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
    driveLogViewModel: DriveLogViewModel,
    bottomNavigationClickListener: DriveLoggerBottomNavigationClickListener,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            DriveLogEditTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                clickListener = clickListener,
                driveLogViewModel = driveLogViewModel,
            )
        },
//        bottomBar = {
//            DriveLoggerBottomNavigation(
//                currentMode = ScreenMode.DriveLoggingScreen,
//                driveLoggerBottomNavigationClickListener = bottomNavigationClickListener,
//            )
//        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxWidth()
        ) {
            val fieldsModifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .fillMaxWidth()
            TextField(
                modifier = fieldsModifier,
                leadingIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            // clickListener?.onClickDate()
                            driveLogViewModel.uiState.isDatePickerDisplayed.value = true
                        },
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                    )
                },
                value = driveLogViewModel.logFormState.textDate.value,
                placeholder = {
                    Text(stringResource(R.string.hint_input_date))
                },
                onValueChange = {},
                maxLines = 1,
                readOnly = true,
            )

            val isErrorOnMileage =
                (driveLogViewModel.logFormState.textMileage.value.toDoubleOrNull() == null)

            DriveLogEditTextFormPlain(
                modifier = fieldsModifier,
                data = driveLogViewModel.logFormState.textMileage,
                hintStringId = R.string.hint_mileage,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = isErrorOnMileage,
            )

            DriveLogEditTextFormPlain(
                modifier = fieldsModifier,
                data = driveLogViewModel.logFormState.textFuelEfficient,
                hintStringId = R.string.hint_fuel_efficient,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            DriveLogEditTextFormPlain(
                modifier = fieldsModifier,
                data = driveLogViewModel.logFormState.textTotalMileage,
                hintStringId = R.string.hint_total_mileage,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            DriveLogEditTextFormPlain(
                modifier = fieldsModifier,
                data = driveLogViewModel.logFormState.textMemo,
                hintStringId = R.string.hint_memo,
            )
        }
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = driveLogViewModel.logFormState.date.value,  // TODO: 時差で日付がズレる
    )

    if (driveLogViewModel.uiState.isDatePickerDisplayed.value) {
        DatePickerDialog(
            modifier = Modifier,
            onDismissRequest = {
                driveLogViewModel.uiState.isDatePickerDisplayed.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        Timber.d("date: ${datePickerState.selectedDateMillis}")
                        datePickerState.selectedDateMillis?.let {
                            driveLogViewModel.logFormState.date.value = it
                            driveLogViewModel.uiState.isDatePickerDisplayed.value = false
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
                        driveLogViewModel.uiState.isDatePickerDisplayed.value = false
                    }
                ) {
                    Text(stringResource(id = R.string.title_cancel))
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // 変更破棄確認ダイアログ
    ConfirmDialog(
        isDisplayed = driveLogViewModel.uiState.isConfirmDialogForDiscardModificationDisplayed,
        onResponse = { response ->
            clickListener?.onConfirmDiscardModification(response)
        },
        textId = R.string.message_discard_modification_drivelog,
    )

    // 削除確認ダイアログ
    ConfirmDialog(
        isDisplayed = driveLogViewModel.uiState.isConfirmDialogForDeleteLogDisplayed,
        onResponse = { response ->
            clickListener?.onConfirmDelete(response)
        },
        textId = R.string.message_remove_drivelog,
    )

    // 上書き保存確認ダイアログ
    ConfirmDialog(
        isDisplayed = driveLogViewModel.uiState.isConfirmDialogForOverwriteLog,
        onResponse = { response ->
            clickListener?.onConfirmOverwrite(response)
        },
        textId = R.string.message_overwrite_drivelog,
    )
}

@Preview
@Composable
private fun DriveLogEditScreenPreview() {
    Surface {
        DriveLogEditScreen(
            modifier = Modifier.fillMaxWidth(),
            driveLogViewModel = DriveLogViewModel(),
            bottomNavigationClickListener = object : DriveLoggerBottomNavigationClickListener {},
        )
    }
}
