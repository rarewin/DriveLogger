package org.tirasweel.drivelogger.ui.compose.refuellogedit

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
import androidx.compose.ui.unit.dp
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.ui.compose.common.ConfirmDialog
import org.tirasweel.drivelogger.ui.compose.drivelogedit.DriveLogEditTextFormPlain
import org.tirasweel.drivelogger.viewmodels.RefuelLogViewModel

interface RefuelLogEditScreenClickListener {
    fun onClickBack()
    fun onClickSave()
    fun onClickDelete()
    fun onConfirmDiscardModification(confirm: Boolean)
    fun onConfirmOverwrite(confirm: Boolean)
    fun onConfirmDelete(confirm: Boolean)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefuelLogEditScreen(
    modifier: Modifier = Modifier,
    clickListener: RefuelLogEditScreenClickListener? = null,
    refuelLogViewModel: RefuelLogViewModel,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            RefuelLogEditTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                clickListener = clickListener,
                refuelLogViewModel = refuelLogViewModel,
            )
        }
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
                            refuelLogViewModel.uiState.isDatePickerDisplayed.value = true
                        },
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                    )
                },
                value = refuelLogViewModel.logFormState.textDate.value,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text(stringResource(R.string.hint_input_date)) }
            )

            DriveLogEditTextFormPlain(
                modifier = fieldsModifier,
                data = refuelLogViewModel.logFormState.textMileage,
                hintStringId = R.string.hint_mileage,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            DriveLogEditTextFormPlain(
                modifier = fieldsModifier,
                data = refuelLogViewModel.logFormState.textFuelAmount,
                hintStringId = R.string.hint_fuel_amount,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            DriveLogEditTextFormPlain(
                modifier = fieldsModifier,
                data = refuelLogViewModel.logFormState.textFuelPrice,
                hintStringId = R.string.hint_fuel_price,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            DriveLogEditTextFormPlain(
                modifier = fieldsModifier,
                data = refuelLogViewModel.logFormState.textTotalMileage,
                hintStringId = R.string.hint_total_mileage,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            DriveLogEditTextFormPlain(
                modifier = fieldsModifier,
                data = refuelLogViewModel.logFormState.textMemo,
                hintStringId = R.string.hint_memo,
            )
        }
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = refuelLogViewModel.logFormState.date.value,
    )

    if (refuelLogViewModel.uiState.isDatePickerDisplayed.value) {
        DatePickerDialog(
            onDismissRequest = { refuelLogViewModel.uiState.isDatePickerDisplayed.value = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        refuelLogViewModel.logFormState.date.value = it
                        refuelLogViewModel.uiState.isDatePickerDisplayed.value = false
                    }
                }) { Text(stringResource(id = R.string.title_ok)) }
            },
            dismissButton = {
                TextButton(onClick = { refuelLogViewModel.uiState.isDatePickerDisplayed.value = false }) {
                    Text(stringResource(id = R.string.title_cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    ConfirmDialog(
        isDisplayed = refuelLogViewModel.uiState.isConfirmDialogForDiscardModificationDisplayed,
        onResponse = { clickListener?.onConfirmDiscardModification(it) },
        textId = R.string.message_discard_modification_refuellog,
    )

    ConfirmDialog(
        isDisplayed = refuelLogViewModel.uiState.isConfirmDialogForDeleteLogDisplayed,
        onResponse = { clickListener?.onConfirmDelete(it) },
        textId = R.string.message_remove_refuellog,
    )

    ConfirmDialog(
        isDisplayed = refuelLogViewModel.uiState.isConfirmDialogForOverwriteLog,
        onResponse = { clickListener?.onConfirmOverwrite(it) },
        textId = R.string.message_overwrite_refuellog,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefuelLogEditTopAppBar(
    modifier: Modifier = Modifier,
    clickListener: RefuelLogEditScreenClickListener? = null,
    refuelLogViewModel: RefuelLogViewModel,
) {
    val isEditMode = refuelLogViewModel.logFormState.isEditingMode()

    TopAppBar(
        modifier = modifier,
        title = {},
        navigationIcon = {
            IconButton(onClick = {
                if (refuelLogViewModel.logFormState.isEdited()) {
                    refuelLogViewModel.uiState.isConfirmDialogForDiscardModificationDisplayed.value = true
                } else {
                    clickListener?.onClickBack()
                }
            }) {
                Icon(painterResource(id = R.drawable.ic_baseline_arrow_back_24), contentDescription = null)
            }
        },
        actions = {
            IconButton(
                onClick = { clickListener?.onClickSave() },
                enabled = refuelLogViewModel.canSave()
            ) {
                Icon(painterResource(id = R.drawable.ic_baseline_save_alt_24), contentDescription = null)
            }
            if (isEditMode) {
                IconButton(onClick = {
                    refuelLogViewModel.uiState.isConfirmDialogForDeleteLogDisplayed.value = true
                }) {
                    Icon(painterResource(id = R.drawable.ic_baseline_delete_forever_24), contentDescription = null)
                }
            }
        }
    )
}
