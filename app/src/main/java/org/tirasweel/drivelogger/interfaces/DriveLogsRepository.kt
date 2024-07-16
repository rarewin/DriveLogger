package org.tirasweel.drivelogger.interfaces

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import org.tirasweel.drivelogger.classes.SortOrderType
import org.tirasweel.drivelogger.db.DriveLog
import org.tirasweel.drivelogger.utils.RealmUtil

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

class RealmDriveLogsRepository(private val realm: Realm) : DriveLogsRepository {

    override fun getDriveLogs(sortOrder: SortOrderType): List<DriveLog> =
        realm.query<DriveLog>().sort(sortOrder.property, sortOrder.order).find()

    override fun deleteDriveLog(id: Long) {
        realm.writeBlocking {
            realm.query<DriveLog>("id == $0", id).find().firstOrNull()?.let {
                delete(it)
            }
        }
    }

    override fun getDriveLog(id: Long, dataHandler: (log: DriveLog) -> Unit) {
        realm.query<DriveLog>("id == $0", id).find().firstOrNull()?.also { log ->
            dataHandler(log)
        }
    }

    override fun setDriveLog(id: Long?, dataHandler: (log: DriveLog) -> Unit) {
        val newLog = DriveLog()
        dataHandler(newLog)

        id?.let {
            realm.writeBlocking {
                realm.query<DriveLog>("id == $0", id).find().firstOrNull()?.also { log ->
                    findLatest(log)?.apply(dataHandler)
                }
            }
        } ?: run {
            newLog.id = getNewDriveLogId()
            realm.writeBlocking {
                copyToRealm(newLog)
            }
        }
    }

    /**
     * 新規IDを取得する
     * @return 新規ID
     */
    private fun getNewDriveLogId(): Long {
        val maxId = realm.query<DriveLog>()
            .sort("id", Sort.DESCENDING)
            .limit(1)
            .find()
            .firstOrNull()?.id

        return maxId?.plus(1) ?: 1L
    }
}