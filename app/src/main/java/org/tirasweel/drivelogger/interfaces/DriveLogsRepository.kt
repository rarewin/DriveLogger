package org.tirasweel.drivelogger.interfaces

import org.tirasweel.drivelogger.classes.SortOrderType
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.db.DriveLogDao

interface DriveLogsRepository {
    fun getDriveLogs(sortOrder: SortOrderType): List<DriveLog>

    /**
     * id のログを削除する
     * @param id 削除するログのID
     */
    fun deleteDriveLog(id: Long)

    /**
     * id のログを取得する
     * @param id 取得するログのID
     * @param dataHandler 取得したデータのハンドラー
     */
    fun getDriveLog(id: Long, dataHandler: (log: DriveLog) -> Unit)

    /**
     * id のログを作成/編集する
     * @param id 設定するログのID。新規作成ならnullを指定する
     * @param dataHandler 新規/編集データの設定をするためのハンドラー
     */
    fun setDriveLog(id: Long?, dataHandler: (log: DriveLog) -> Unit)
}

class RoomDriveLogsRepository(private val driveLogDao: DriveLogDao) : DriveLogsRepository {

    override fun getDriveLogs(sortOrder: SortOrderType): List<DriveLog> {
        return when (sortOrder) {
            SortOrderType.AscendingDate -> driveLogDao.getAllSortedByDateAsc()
            SortOrderType.DescendingDate -> driveLogDao.getAllSortedByDateDesc()
        }
    }

    override fun deleteDriveLog(id: Long) {
        driveLogDao.deleteById(id)
    }

    override fun getDriveLog(id: Long, dataHandler: (log: DriveLog) -> Unit) {
        driveLogDao.getById(id)?.let {
            dataHandler(it)
        }
    }

    override fun setDriveLog(id: Long?, dataHandler: (log: DriveLog) -> Unit) {
        if (id != null) {
            driveLogDao.getById(id)?.let { log ->
                dataHandler(log)
                driveLogDao.update(log)
            }
        } else {
            val newLog = DriveLog()
            dataHandler(newLog)
            // Room will auto-generate ID if it's 0 and set as autoGenerate = true
            driveLogDao.insert(newLog)
        }
    }
}
