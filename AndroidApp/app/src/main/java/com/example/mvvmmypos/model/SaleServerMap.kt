package com.example.mvvmmypos.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sale_server_map",primaryKeys= [ "sale_id", "sale_server_id" ])
data class SaleServerMap (

    @ColumnInfo(name = "sale_id")
    val saleId: Long,

    @ColumnInfo(name = "sale_server_id")
    val saleServerId: Long
)