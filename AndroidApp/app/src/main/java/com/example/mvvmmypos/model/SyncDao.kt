package com.example.mvvmmypos.model

import androidx.room.*

@Dao
interface SyncDao {
    @Query("SELECT * FROM sync")
    fun getAll(): List<Sync>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(sync: Sync)

    @Query("DELETE FROM sync")
    fun deleteAll()

    @Query("SELECT * FROM sync " +
            "WHERE sync_id=(SELECT MAX(sync_id) from sync where success=1) " +
            "limit 1")
    fun getLastSync(): Sync
}