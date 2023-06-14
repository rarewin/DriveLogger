package org.tirasweel.drivelogger.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.tirasweel.drivelogger.db.DriveLog

class DriveLogEditViewModel : ViewModel() {

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

    fun setDriveLog(driveLog: DriveLog?) {
        _textMileage = mutableStateOf(
            driveLog?.milliMileage?.let {
                (it / 1000.0).toString()
            } ?: ""
        )

        _textFuelEfficient = mutableStateOf(
            driveLog?.fuelEfficient?.toString() ?: ""
        )

        _textTotalMileage = mutableStateOf(
            driveLog?.fuelEfficient?.toString() ?: ""
        )

        _textMemo = mutableStateOf(driveLog?.memo ?: "")
    }
}