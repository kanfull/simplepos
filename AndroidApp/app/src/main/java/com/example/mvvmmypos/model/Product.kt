package com.example.mvvmmypos.model

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "product")
data class Product(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    @SerializedName("product_id")
    val productId: Long,

    @ColumnInfo(name = "product_name")
    @SerializedName("product_name")
    val productName: String,

    @ColumnInfo(name = "update_time")
    @SerializedName("update_time")
    var updateTime: Long,

    @ColumnInfo(name = "created_time")
    @SerializedName("created_time")
    var createdTime: Long

)