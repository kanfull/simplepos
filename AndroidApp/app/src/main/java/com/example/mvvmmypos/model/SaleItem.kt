package com.example.mvvmmypos.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sale_item")
data class SaleItem (
    @ColumnInfo(name = "sale_id")
    val saleId: Long,

    @ColumnInfo(name = "product_id")
    val productId: Long,

    @ColumnInfo(name = "price_id")
    val priceId: Long,

    @ColumnInfo(name = "promotion_id")
    val promotionId: Long,

    @ColumnInfo(name = "sale_price")
    val salePrice: Double,

    @ColumnInfo(name = "unit")
    val unit: Int,

    @ColumnInfo(name = "update_time")
    var updateTime: Long,

    @ColumnInfo(name = "created_time")
    var createdTime: Long
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sale_item_id")
    var saleItemId: Long = 0
}