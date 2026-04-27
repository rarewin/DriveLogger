package org.tirasweel.drivelogger.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface RefuelLogDao {
    @Query("SELECT * FROM refuel_log")
    fun getAll(): List<RefuelLog>

    @Query("SELECT * FROM refuel_log ORDER BY date DESC")
    fun getAllSortedByDateDesc(): List<RefuelLog>

    @Query("SELECT * FROM refuel_log ORDER BY date ASC")
    fun getAllSortedByDateAsc(): List<RefuelLog>

    @Query("SELECT * FROM refuel_log WHERE id = :id")
    fun getById(id: Long): RefuelLog?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(refuelLog: RefuelLog): Long

    @Update
    fun update(refuelLog: RefuelLog)

    @Delete
    fun delete(refuelLog: RefuelLog)

    @Query("DELETE FROM refuel_log WHERE id = :id")
    fun deleteById(id: Long)

    @Query("SELECT MAX(id) FROM refuel_log")
    fun getMaxId(): Long?

    /** 指定した日付より前の最新の記録を取得する（燃費計算用） */
    @Query("SELECT * FROM refuel_log WHERE date < :date ORDER BY date DESC LIMIT 1")
    fun getPreviousLog(date: Long): RefuelLog?

    /** 指定したIDの記録より日付が前の最新の記録を取得する（編集時の燃費計算用） */
    @Query("SELECT * FROM refuel_log WHERE date < :date AND id != :id ORDER BY date DESC LIMIT 1")
    fun getPreviousLog(date: Long, id: Long): RefuelLog?
}
