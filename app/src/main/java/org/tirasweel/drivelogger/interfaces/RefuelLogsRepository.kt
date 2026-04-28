package org.tirasweel.drivelogger.interfaces

import org.tirasweel.drivelogger.classes.SortOrderType
import org.tirasweel.drivelogger.db.RefuelLog
import org.tirasweel.drivelogger.db.RefuelLogDao

interface RefuelLogsRepository {
    fun getRefuelLogs(sortOrder: SortOrderType): List<RefuelLog>

    fun deleteRefuelLog(id: Long)

    fun getRefuelLog(id: Long, dataHandler: (log: RefuelLog) -> Unit)

    fun setRefuelLog(id: Long?, dataHandler: (log: RefuelLog) -> Unit)

    fun insertRefuelLogs(logs: List<RefuelLog>)

    fun getPreviousLog(date: Long, currentId: Long?): RefuelLog?
}

class RoomRefuelLogsRepository(private val refuelLogDao: RefuelLogDao) : RefuelLogsRepository {

    override fun getRefuelLogs(sortOrder: SortOrderType): List<RefuelLog> {
        return when (sortOrder) {
            SortOrderType.AscendingDate -> refuelLogDao.getAllSortedByDateAsc()
            SortOrderType.DescendingDate -> refuelLogDao.getAllSortedByDateDesc()
        }
    }

    override fun deleteRefuelLog(id: Long) {
        refuelLogDao.deleteById(id)
    }

    override fun getRefuelLog(id: Long, dataHandler: (log: RefuelLog) -> Unit) {
        refuelLogDao.getById(id)?.let {
            dataHandler(it)
        }
    }

    override fun setRefuelLog(id: Long?, dataHandler: (log: RefuelLog) -> Unit) {
        if (id != null) {
            refuelLogDao.getById(id)?.let { log ->
                dataHandler(log)
                refuelLogDao.update(log)
            }
        } else {
            val newLog = RefuelLog()
            dataHandler(newLog)
            refuelLogDao.insert(newLog)
        }
    }

    override fun insertRefuelLogs(logs: List<RefuelLog>) {
        logs.forEach { log ->
            val newLog = log.copy(id = 0)
            refuelLogDao.insert(newLog)
        }
    }

    override fun getPreviousLog(date: Long, currentId: Long?): RefuelLog? {
        return if (currentId != null) {
            refuelLogDao.getPreviousLog(date, currentId)
        } else {
            refuelLogDao.getPreviousLog(date)
        }
    }
}
