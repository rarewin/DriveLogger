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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.tirasweel.drivelogger.DriveLogger
import org.tirasweel.drivelogger.classes.SortOrderType
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.interfaces.DriveLogsRepository
import org.tirasweel.drivelogger.utils.DateFormatConverter.Companion.toLocaleDateString
import java.io.File
import java.io.FileWriter
import java.util.Date

class DriveLogViewModel(
    private val driveLogsRepository: DriveLogsRepository,
) : ViewModel() {

    /** ダイアログやボタンなどのUI状態 */
    inner class UiState {
        /** 編集を破棄するかの確認ダイアログ表示状態 */
        var isConfirmDialogForDiscardModificationDisplayed = mutableStateOf(false)

        /** 削除確認ダイアログ表示状態 */
        var isConfirmDialogForDeleteLogDisplayed = mutableStateOf(false)

        /** 保存確認ダイアログ表示状態 */
        var isConfirmDialogForOverwriteLog = mutableStateOf(false)

        /** DatePicker表示状態 */
        var isDatePickerDisplayed = mutableStateOf(false)

        /** エクスポート上書き確認 */
        var isConfirmDialogForOverwriteExport = mutableStateOf(false)
    }

    /** ログの編集フォームの状態 */
    inner class LogFormState {
        internal var editingLog: MutableState<DriveLog?> = mutableStateOf(null)

        /** フォーム作成時のタイムスタンプ */
        private var initialDateValue = Calendar.getInstance().timeInMillis  // TODO: リセット必要

        /** 日付用タイムスタンプ  */
        var date = mutableStateOf(initialDateValue)

        /** 日付 */
        val textDate: State<String>
            get() {
                return mutableStateOf(Date(date.value).toLocaleDateString())
            }

        /** 走行距離 */
        var textMileage = mutableStateOf("")

        /** 燃費 */
        var textFuelEfficient = mutableStateOf("")

        /** 総走行距離 */
        var textTotalMileage = mutableStateOf("")

        /** メモ */
        var textMemo = mutableStateOf("")

        /** 編集中のログを設定 */
        fun setEditingDriveLog(id: Long) {
            driveLogsRepository.getDriveLog(id) { log ->
                editingLog.value = log

                date.value = log.date

                textMileage.value = "${log.milliMileage / 1000.0}"

                log.fuelEfficient?.let { fuelEfficient ->
                    textFuelEfficient.value = fuelEfficient.toString()
                }

                log.totalMilliMileage?.let { totalMilliMileage ->
                    textTotalMileage.value = "${totalMilliMileage / 1000.0}"
                }

                textMemo.value = log.memo
            }
        }

        /**
         * @brief 現在編集中の内容からDriveLogを生成する
         * @return 生成されたDriveLogインスタンス
         */
        internal fun toDriveLogOrNull(): DriveLog? {
            return DriveLog().apply {
                date = this@LogFormState.date.value

                val mileage: Double = this@LogFormState.textMileage.value
                    .toDoubleOrNull()
                    ?.times(1000) ?: return null

                milliMileage = mileage.toLong()

                fuelEfficient = this@LogFormState.textFuelEfficient.value.toDoubleOrNull()

                totalMilliMileage = this@LogFormState.textTotalMileage.value
                    .toDoubleOrNull()
                    ?.times(1000)
                    ?.toLong()

                memo = this@LogFormState.textMemo.value
            }
        }

        /**
         * @brief 現在の編集内容とdriveLogを比較して, 編集されているかチェックする
         * @return 編集されていたらtrue
         */
        internal fun isEdited(): Boolean {

            return editingLog.value?.let { log ->
                return logFormState.toDriveLogOrNull()?.let { edited ->
                    return !(log.date == edited.date &&
                            log.milliMileage == edited.milliMileage &&
                            log.fuelEfficient == edited.fuelEfficient &&
                            log.memo == edited.memo)

                } ?: true // 元の値があるのにgetEditedDriveLog()がnullなのは何かしら編集されているはず
            } ?: (logFormState.date.value != initialDateValue ||
                    logFormState.textMileage.value.isNotEmpty() ||
                    logFormState.textFuelEfficient.value.isNotEmpty() ||
                    logFormState.textMileage.value.isNotEmpty() ||
                    logFormState.textMemo.value.isNotEmpty()
                    )
        }

        internal fun isEditingMode(): Boolean = (editingLog.value != null)

        /**
         * ログ入力フォームをリセットする
         */
        fun resetLogForm() {
            logFormState = LogFormState()
            logFormState.editingLog.value = null
        }
    }

    /** ログのリストの状態 */
    inner class LogListState {
        /** ソート順設定 */
        var sortOrder = mutableStateOf(SortOrderType.DescendingDate)
    }

    /** 共通UI状態のインスタンス */
    var uiState = UiState()

    /** ログフォーム状態のインスタンス */
    var logFormState = LogFormState()

    /** リスト状態のインスタンス */
    var logListState = LogListState()

    /** ドライブログのリスト */
    private var _driveLogList: MutableState<List<DriveLog>>

    val driveLogList: State<List<DriveLog>>
        get() = _driveLogList

    init {
        _driveLogList = mutableStateOf(getDriveLogs())
    }

    private fun getDriveLogs(): List<DriveLog> =
        driveLogsRepository.getDriveLogs(logListState.sortOrder.value)

    fun updateDriveLogList() {
        _driveLogList.value = getDriveLogs()
    }

    /**
     * 編集中のログをDBから削除する
     */
    fun deleteEditingLog() {
        logFormState.editingLog.value?.let { log ->
            driveLogsRepository.deleteDriveLog(log.id)
            updateDriveLogList()
        }
    }

    /**
     * セーブ可能かどうか確認する
     * @return セーブ可能ならtrue
     */
    internal fun canSave(): Boolean {
        return (logFormState.isEdited() && logFormState.toDriveLogOrNull() != null)
    }

    fun saveCurrentLog() {
        if (!canSave()) {
            // TODO: エラーダイアログ等の表示
            return
        }

        logFormState.toDriveLogOrNull()?.let { edited ->
            val id = logFormState.editingLog.value?.id

            driveLogsRepository.setDriveLog(id) { log ->
                log.apply {
                    date = edited.date
                    milliMileage = edited.milliMileage
                    fuelEfficient = edited.fuelEfficient
                    totalMilliMileage = edited.totalMilliMileage
                    memo = edited.memo
                }
            }

            updateDriveLogList()
        }
    }

//    private fun getNewDriveLogId(): Long = driveLogsRepository.getNewDriveLogId()

    fun exportDriveLogLists(file: File) {
        val writer = FileWriter(file)

        _driveLogList.value.forEach { log ->
            writer.write(Json.encodeToString(log))
        }

        writer.close()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as DriveLogger)
                val driveLogsRepository = application.container.driveLogsRepository
                DriveLogViewModel(driveLogsRepository = driveLogsRepository)
            }
        }
    }
}