package org.tirasweel.drivelogger.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.tirasweel.drivelogger.classes.SortOrderType
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.utils.RealmUtil

class DriveLogListViewModel : ViewModel() {

    private val realm = RealmUtil.createRealm()

    private var _driveLogsStateFlow: MutableStateFlow<List<DriveLog>>

    val driveLogsStateFlow: StateFlow<List<DriveLog>>
        get() = _driveLogsStateFlow.asStateFlow()

    private var _sortOrder: MutableState<SortOrderType> =
        mutableStateOf(SortOrderType.DescendingDate)

    val sortOrder: State<SortOrderType>
        get() = _sortOrder

    init {
        _driveLogsStateFlow = MutableStateFlow(getDriveLogs())
    }

    fun setDriveLogOrder(orderType: SortOrderType) {
        _sortOrder.value = orderType
        reloadDriveLogs()
    }

    fun reloadDriveLogs() {
        _driveLogsStateFlow.value = getDriveLogs()
    }

    private fun getDriveLogs(): List<DriveLog> = realm.query<DriveLog>()
        .sort(_sortOrder.value.property, _sortOrder.value.order)
        .find()
}