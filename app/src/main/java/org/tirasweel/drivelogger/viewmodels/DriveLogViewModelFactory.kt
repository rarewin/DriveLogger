package org.tirasweel.drivelogger.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.tirasweel.drivelogger.fake.FakeDriveLogsRepository

class DriveLogViewModelFactory(private val driveLogsRepository: FakeDriveLogsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DriveLogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DriveLogViewModel(driveLogsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}