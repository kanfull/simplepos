package com.example.mvvmmypos.model

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "price")
data class Price(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "price_id")
    @SerializedName("price_id")
    val priceId: Long,

    @ColumnInfo(name = "product_id")
    @SerializedName("product_id")
    val productId: Long,

    @ColumnInfo(name = "price_value")
    @SerializedName("price_value")
    val priceValue: Double,

    @ColumnInfo(name = "valid")
    @SerializedName("valid")
    val valid: Int,

    @ColumnInfo(name = "update_time")
    @SerializedName("update_time")
    var updateTime: Long=0,

    @ColumnInfo(name = "created_time")
    @SerializedName("created_time")
    var createdTime: Long=0
)