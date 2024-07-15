package org.tirasweel.drivelogger.fake

import org.tirasweel.drivelogger.classes.SortOrderType
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.interfaces.DriveLogsRepository

class FakeDriveLogsRepository: DriveLogsRepository {
    override fun getDriveLogs(sortOrder: SortOrderType): List<DriveLog> =
        FakeDataSource.driveLogList

    override fun deleteDriveLog(id: Long) {
        TODO("Not yet implemented")
    }

    override fun getDriveLog(id: Long, dataHandler: (log: DriveLog) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun setDriveLog(id: Long?, dataHandler: (log: DriveLog) -> Unit) {
        TODO("Not yet implemented")
    }
}