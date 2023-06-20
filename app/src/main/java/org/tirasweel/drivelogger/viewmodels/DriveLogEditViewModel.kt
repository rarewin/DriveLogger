package org.tirasweel.drivelogger.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import io.realm.kotlin.ext.query
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.utils.DateFormatConverter.Companion.toLocalDateString
import org.tirasweel.drivelogger.utils.RealmUtil

class DriveLogEditViewModel : ViewModel() {

    private val realm = RealmUtil.createRealm()

    private var _driveLog: MutableState<DriveLog?> = mutableStateOf(null)

    /**
     * 日付
     */
    private var _textDate = mutableStateOf("")

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

    val driveLog: State<DriveLog?>
        get() = _driveLog

    fun setDriveLog(id: Long) {
        realm.query<DriveLog>("id == $0", id).find().firstOrNull()?.also { log ->
            _driveLog.value = log

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
        TODO("Not yet implemented")
    }
}