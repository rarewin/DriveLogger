package org.tirasweel.drivelogger.fake

import org.tirasweel.drivelogger.db.DriveLog

object FakeDataSource {
    val driveLogList = listOf(
        DriveLog().apply {
            id = 0
            createdDate = 0
        },
        DriveLog().apply {
            id = 1
        },
        DriveLog().apply {
            id = 2
        },
    )
}