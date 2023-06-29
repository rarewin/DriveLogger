package org.tirasweel.drivelogger.viewmodels

import android.icu.util.Calendar
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.utils.DateFormatConverter.Companion.toLocalDateString
import org.tirasweel.drivelogger.utils.DateFormatConverter.Companion.toLocaleDateString
import org.tirasweel.drivelogger.utils.RealmUtil
import java.util.Date

class DriveLogEditViewModel : ViewModel() {

    private val realm = RealmUtil.createRealm()

    private var _driveLog: MutableState<DriveLog?> = mutableStateOf(null)

    private val initialDateValue = Calendar.getInstance().timeInMillis

    private var _date: MutableState<Long> = mutableStateOf(initialDateValue)

    /**
     * 日付
     */
    private var _textDate: MutableState<String> =
        mutableStateOf(Date(_date.value).toLocaleDateString())

    /**
     * 走行距離
     */
    private var _textMileage = mutableStateOf("")

    /**
     * 燃費
     */
    private var _textFuelEfficient = mutableStateOf("")

    /**
     * 総走行距離
     */
    private var _textTotalMileage = mutableStateOf("")

    /**
     * メモ
     */
    private var _textMemo = mutableStateOf("")

    val date: State<Long>
        get() = _date

    fun setDate(dateMillis: Long) {
        _date.value = dateMillis
        _textDate = mutableStateOf(Date(_date.value).toLocaleDateString())  // TODO: 自動で連携する方法を探す
    }

    val driveLog: State<DriveLog?>
        get() = _driveLog

    var isDatePickerDisplayed = mutableStateOf(false)

    fun setDriveLog(id: Long) {
        realm.query<DriveLog>("id == $0", id).find().firstOrNull()?.also { log ->
            _driveLog.value = log

            _date.value = log.date
            _textDate.value = log.date.toLocalDateString()
            _textMileage.value = "${log.milliMileage / 1000.0}"
            _textFuelEfficient.value = log.fuelEfficient.toString()

            log.totalMilliMileage?.let { milliTotalMileage ->
                _textTotalMileage.value = "${milliTotalMileage / 1000.0}"
            }

            _textMemo.value = log.memo
        }
    }

    val textDate: State<String>
        get() = _textDate

    fun setTextDate(value: String) {
        _textDate.value = value
    }

    val textMileage: State<String>
        get() = _textMileage

    fun setTextMileage(value: String) {
        _textMileage.value = value
    }

    val textFuelEfficient: State<String>
        get() = _textFuelEfficient

    fun setTextFuelEfficient(value: String) {
        _textFuelEfficient.value = value
    }

    val textTotalMileage: State<String>
        get() = _textTotalMileage

    fun setTextTotalMileage(value: String) {
        _textTotalMileage.value = value
    }

    val textMemo: State<String>
        get() = _textMemo

    fun setTextMemo(value: String) {
        _textMemo.value = value
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
            return getEditedDriveLog()?.let { edited ->
                return !(log.date == edited.date &&
                        log.milliMileage == edited.milliMileage &&
                        log.fuelEfficient == edited.fuelEfficient &&
                        log.memo == edited.memo)

            } ?: true // 元の値があるのにgetEditedDriveLog()がnullなのは何かしら編集されているはず
        } ?: (_date.value != initialDateValue ||
                _textMileage.value.isNotEmpty() ||
                _textFuelEfficient.value.isNotEmpty() ||
                _textMileage.value.isNotEmpty() ||
                _textMemo.value.isNotEmpty())
    }

    /**
     * セーブ可能かどうか確認する
     * @return セーブ可能ならtrue
     */
    internal fun canSave(): Boolean {
        return (isEdited() && getEditedDriveLog() != null)
    }

    /**
     * @brief 現在編集中の内容からDriveLogを生成する
     * @return 生成されたDriveLogインスタンス
     */
    private fun getEditedDriveLog(): DriveLog? {
        return DriveLog().apply {
            date = _date.value

            val mileage: Double = _textMileage.value.toDoubleOrNull() ?: return null
            milliMileage = (mileage * 1000.0).toLong()

            fuelEfficient = _textFuelEfficient.value.toDoubleOrNull()

            _textTotalMileage.value.toDoubleOrNull()?.let { totalMileage ->
                totalMilliMileage = (totalMileage * 1000).toLong()
            }

            memo = _textMemo.value

            if ((milliMileage < 0)
                || (fuelEfficient?.let { (it < 0) } == true)
                || (totalMilliMileage?.let { (it < 0) } == true)
            ) {
                return null
            }
        }
    }

    fun saveCurrentLog() {
        if (!isEdited()) {
            return
        }

        getEditedDriveLog()?.let { edited ->
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