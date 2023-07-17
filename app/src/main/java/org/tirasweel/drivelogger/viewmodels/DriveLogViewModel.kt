package org.tirasweel.drivelogger.viewmodels

import android.icu.util.Calendar
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import org.tirasweel.drivelogger.classes.SortOrderType
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.utils.DateFormatConverter.Companion.toLocaleDateString
import org.tirasweel.drivelogger.utils.RealmUtil
import java.util.Date

class DriveLogViewModel : ViewModel() {

    /** Realm操作 */
    private val realm = RealmUtil.createRealm()

    /** ダイアログやボタンなどのUI状態 */
    inner class UiState {
        /** 編集を破棄するかの確認ダイアログ表示状態 */
        var isConfirmDialogForDiscardModificationDisplayed = mutableStateOf(false)

        /** 削除確認ダイアログ表示状態 */
        var isConfirmDialogForDeleteLogDisplayed = mutableStateOf(false)

        /** 保存確認ダイアログ表示状態 */
        var isConfirmDialogForSaveLog = mutableStateOf(false)

        /** DatePicker表示状態 */
        var isDatePickerDisplayed = mutableStateOf(false)
    }

    /** ログの編集フォームの状態 */
    inner class LogFormState {

        /** 日付用タイムスタンプ  */
        var date = mutableStateOf(Calendar.getInstance().timeInMillis)

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
                totalMilliMileage = this@LogFormState.textTotalMileage.value.toLongOrNull()
                memo = this@LogFormState.textMemo.value
            }
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
    var logForm = LogFormState()

    /** リスト状態のインスタンス */
    var logListState = LogListState()

    /** 編集中ログ */
    private var _driveLog: MutableState<DriveLog?> = mutableStateOf(null)

    private val initialDateValue = Calendar.getInstance().timeInMillis

    /** ドライブログのリスト */
    private var _driveLogList: MutableState<List<DriveLog>>

    val driveLogList: State<List<DriveLog>>
        get() = _driveLogList

    init {
        _driveLogList = mutableStateOf(getDriveLogs())
    }

    private fun getDriveLogs(): List<DriveLog> = realm.query<DriveLog>()
        .sort(logListState.sortOrder.value.property, logListState.sortOrder.value.order)
        .find()

    val driveLog: State<DriveLog?>
        get() = _driveLog

    fun setDriveLog(id: Long) {
        realm.query<DriveLog>("id == $0", id).find().firstOrNull()?.also { log ->
            _driveLog.value = log
        }
    }

    fun clearDriveLog() {
        _driveLog.value = null
    }

    fun deleteCurrentLog() {
        realm.writeBlocking {
            _driveLog.value?.let { log ->
                findLatest(log)?.let {
                    delete(it)
                }
            }
        }
    }

    /**
     * @brief 現在の編集内容とdriveLogを比較して, 編集されているかチェックする
     * @return 編集されていたらtrue
     */
    internal fun isEdited(): Boolean {

        return _driveLog.value?.let { log ->
            return logForm.toDriveLogOrNull()?.let { edited ->
                return !(log.date == edited.date &&
                        log.milliMileage == edited.milliMileage &&
                        log.fuelEfficient == edited.fuelEfficient &&
                        log.memo == edited.memo)

            } ?: true // 元の値があるのにgetEditedDriveLog()がnullなのは何かしら編集されているはず
        } ?: (logForm.date.value != initialDateValue ||
                logForm.textMileage.value.isNotEmpty() ||
                logForm.textFuelEfficient.value.isNotEmpty() ||
                logForm.textMileage.value.isNotEmpty() ||
                logForm.textMemo.value.isNotEmpty()
                )
    }

    /**
     * セーブ可能かどうか確認する
     * @return セーブ可能ならtrue
     */
    internal fun canSave(): Boolean {
        return (isEdited() && logForm.toDriveLogOrNull() != null)
    }

    fun saveCurrentLog() {
        if (!canSave()) {
            // TODO: エラーダイアログ等の表示
            return
        }

        logForm.toDriveLogOrNull()?.let { edited ->
            realm.writeBlocking {
                _driveLog.value?.let { log ->
                    findLatest(log)?.apply {
                        date = edited.date
                        milliMileage = edited.milliMileage
                        fuelEfficient = edited.fuelEfficient
                        totalMilliMileage = edited.totalMilliMileage
                        memo = edited.memo
                    }
                } ?: run {
                    edited.id = getNewDriveLogId()
                    copyToRealm(edited)
                }
            }
        }
    }

    private fun getNewDriveLogId(): Long {
        val maxId = realm.query<DriveLog>()
            .sort("id", Sort.DESCENDING)
            .limit(1)
            .find()
            .firstOrNull()?.id

        return maxId?.plus(1) ?: 1L
    }
}