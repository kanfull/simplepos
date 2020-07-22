package com.example.mvvmmypos.model

import androidx.room.*

@Entity(tableName = "sale")
data class Sale(
    @ColumnInfo(name = "sale_description")
    val saleName: String,

    @ColumnInfo(name = "update_time")
    var updateTime: Long,

    @ColumnInfo(name = "created_time")
    var createdTime: Long
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sale_id")
    var saleId: Long = 0
}