package org.tirasweel.drivelogger.viewmodels

import android.icu.util.Calendar
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.tirasweel.drivelogger.DriveLogger
import org.tirasweel.drivelogger.classes.SortOrderType
import org.tirasweel.drivelogger.db.RefuelLog
import org.tirasweel.drivelogger.interfaces.RefuelLogsRepository
import org.tirasweel.drivelogger.utils.DateFormatConverter.Companion.toLocaleDateString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.Date

class RefuelLogViewModel(
    private val refuelLogsRepository: RefuelLogsRepository,
) : ViewModel() {

    inner class UiState {
        var isConfirmDialogForDiscardModificationDisplayed = mutableStateOf(false)
        var isConfirmDialogForDeleteLogDisplayed = mutableStateOf(false)
        var isConfirmDialogForOverwriteLog = mutableStateOf(false)
        var isDatePickerDisplayed = mutableStateOf(false)
    }

    inner class LogFormState {
        internal var editingLog: MutableState<RefuelLog?> = mutableStateOf(null)
        private var initialDateValue = Calendar.getInstance().timeInMillis
        var date = mutableStateOf(initialDateValue)
        val textDate: State<String> get() = mutableStateOf(Date(date.value).toLocaleDateString())

        /** 積算距離 (km) */
        var textTotalMileage = mutableStateOf("")

        /** 今回の走行距離 (km) */
        var textMileage = mutableStateOf("")

        /** 給油量 (L) */
        var textFuelAmount = mutableStateOf("")

        /** 金額 */
        var textFuelPrice = mutableStateOf("")

        /** メモ */
        var textMemo = mutableStateOf("")

        fun setEditingRefuelLog(id: Long) {
            refuelLogsRepository.getRefuelLog(id) { log ->
                editingLog.value = log
                date.value = log.date
                textTotalMileage.value = log.totalMilliMileage?.let { "${it / 1000.0}" } ?: ""
                textMileage.value = "${log.milliMileage / 1000.0}"
                textFuelAmount.value = log.fuelAmount.toString()
                textFuelPrice.value = log.fuelPrice?.toString() ?: ""
                textMemo.value = log.memo
            }
        }

        internal fun toRefuelLogOrNull(): RefuelLog? {
            return RefuelLog().apply {
                date = this@LogFormState.date.value

                totalMilliMileage = this@LogFormState.textTotalMileage.value
                    .toDoubleOrNull()
                    ?.times(1000)
                    ?.toLong()

                val mileage: Double = this@LogFormState.textMileage.value
                    .toDoubleOrNull()
                    ?.times(1000) ?: return null
                milliMileage = mileage.toLong()

                fuelAmount = this@LogFormState.textFuelAmount.value.toDoubleOrNull() ?: return null
                fuelPrice = this@LogFormState.textFuelPrice.value.toLongOrNull()
                memo = this@LogFormState.textMemo.value
            }
        }

        internal fun isEdited(): Boolean {
            return editingLog.value?.let { log ->
                toRefuelLogOrNull()?.let { edited ->
                    !(log.date == edited.date &&
                            log.totalMilliMileage == edited.totalMilliMileage &&
                            log.milliMileage == edited.milliMileage &&
                            log.fuelAmount == edited.fuelAmount &&
                            log.fuelPrice == edited.fuelPrice &&
                            log.memo == edited.memo)
                } ?: true
            } ?: (date.value != initialDateValue ||
                    textTotalMileage.value.isNotEmpty() ||
                    textMileage.value.isNotEmpty() ||
                    textFuelAmount.value.isNotEmpty() ||
                    textFuelPrice.value.isNotEmpty() ||
                    textMemo.value.isNotEmpty())
        }

        internal fun isEditingMode(): Boolean = (editingLog.value != null)

        fun resetLogForm() {
            editingLog.value = null
            date.value = initialDateValue
            textTotalMileage.value = ""
            textMileage.value = ""
            textFuelAmount.value = ""
            textFuelPrice.value = ""
            textMemo.value = ""
        }
    }

    inner class LogListState {
        var sortOrder = mutableStateOf(SortOrderType.DescendingDate)
    }

    var uiState = UiState()
    var logFormState = LogFormState()
    var logListState = LogListState()

    private var _refuelLogList: MutableState<List<RefuelLog>> = mutableStateOf(listOf())
    val refuelLogList: State<List<RefuelLog>> get() = _refuelLogList

    init {
        updateRefuelLogList()
    }

    fun updateRefuelLogList() {
        _refuelLogList.value = refuelLogsRepository.getRefuelLogs(logListState.sortOrder.value)
    }

    fun deleteEditingLog() {
        logFormState.editingLog.value?.let { log ->
            refuelLogsRepository.deleteRefuelLog(log.id)
            updateRefuelLogList()
        }
    }

    internal fun canSave(): Boolean {
        return (logFormState.isEdited() && logFormState.toRefuelLogOrNull() != null)
    }

    fun saveCurrentLog() {
        if (!canSave()) return

        logFormState.toRefuelLogOrNull()?.let { edited ->
            val id = logFormState.editingLog.value?.id

            // 燃費計算: 今回の走行距離 (milliMileage) は必須
            if (edited.fuelAmount > 0 && edited.milliMileage > 0) {
                edited.fuelEfficiency = (edited.milliMileage / 1000.0) / edited.fuelAmount
            }

            // 総走行距離が未入力の場合、前回の記録があれば補完を試みる
            if (edited.totalMilliMileage == null) {
                val previousLog = refuelLogsRepository.getPreviousLog(edited.date, id)
                previousLog?.totalMilliMileage?.let { prevTotal ->
                    edited.totalMilliMileage = prevTotal + edited.milliMileage
                }
            }

            refuelLogsRepository.setRefuelLog(id) { log ->
                log.apply {
                    date = edited.date
                    totalMilliMileage = edited.totalMilliMileage
                    milliMileage = edited.milliMileage
                    fuelAmount = edited.fuelAmount
                    fuelPrice = edited.fuelPrice
                    fuelEfficiency = edited.fuelEfficiency
                    memo = edited.memo
                }
            }
            updateRefuelLogList()
        }
    }

    fun exportRefuelLogLists(outputStream: OutputStream) {
        outputStream.use { stream ->
            val jsonString = Json.encodeToString(_refuelLogList.value)
            stream.write(jsonString.toByteArray())
        }
    }

    fun importRefuelLogLists(inputStream: InputStream) {
        inputStream.use { stream ->
            val jsonString = stream.bufferedReader().use { it.readText() }
            val logs: List<RefuelLog> = Json.decodeFromString(jsonString)
            refuelLogsRepository.insertRefuelLogs(logs)
            updateRefuelLogList()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as DriveLogger)
                val refuelLogsRepository = application.container.refuelLogsRepository
                RefuelLogViewModel(refuelLogsRepository = refuelLogsRepository)
            }
        }
    }
}
