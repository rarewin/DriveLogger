package org.tirasweel.drivelogger.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.tirasweel.drivelogger.interfaces.Destination
import org.tirasweel.drivelogger.interfaces.DriveLogList

class AppViewModel : ViewModel() {

    var currentMode: MutableState<Destination> = mutableStateOf(DriveLogList)

    /** 戻るボタンが使えるかどうか */
    var canBack = mutableStateOf(false)

}