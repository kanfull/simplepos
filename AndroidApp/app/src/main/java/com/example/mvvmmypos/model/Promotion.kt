package com.example.mvvmmypos.model

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "promotion")
data class Promotion(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "promotion_id")
    @SerializedName("promotion_id")
    val priceId: Long,

    @ColumnInfo(name = "product_id")
    @SerializedName("product_id")
    val productId: Long,

    @ColumnInfo(name = "promotion_price")
    @SerializedName("promotion_price")
    val promotionPrice: Double,

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