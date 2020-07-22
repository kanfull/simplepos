package com.example.mvvmmypos.model

import androidx.room.*

@Entity(tableName = "sync")
data class Sync(
    @ColumnInfo(name = "sync_time")
    val syncTime: Long,

    @ColumnInfo(name = "success")
    val success: Long
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sync_id")
    var syncId: Long = 0

}