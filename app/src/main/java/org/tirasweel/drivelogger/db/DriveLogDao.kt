package org.tirasweel.drivelogger.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DriveLogDao {
    @Query("SELECT * FROM drive_log")
    fun getAll(): List<DriveLog>

    @Query("SELECT * FROM drive_log ORDER BY date DESC")
    fun getAllSortedByDateDesc(): List<DriveLog>

    @Query("SELECT * FROM drive_log ORDER BY date ASC")
    fun getAllSortedByDateAsc(): List<DriveLog>

    @Query("SELECT * FROM drive_log WHERE id = :id")
    fun getById(id: Long): DriveLog?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(driveLog: DriveLog): Long

    @Update
    fun update(driveLog: DriveLog)

    @Delete
    fun delete(driveLog: DriveLog)

    @Query("DELETE FROM drive_log WHERE id = :id")
    fun deleteById(id: Long)

    @Query("SELECT MAX(id) FROM drive_log")
    fun getMaxId(): Long?
}
