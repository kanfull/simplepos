package com.example.mvvmmypos.model

import androidx.room.*

@Entity
data class ProductItem (
    @ColumnInfo(name = "product_id")
    val productId: Long,

    @ColumnInfo(name = "product_name")
    val productName: String,

    @ColumnInfo(name = "price_id")
    val priceId:Long,

    @ColumnInfo(name = "price_value")
    val price: Double,

    @ColumnInfo(name = "promotion_id")
    val promotionId:Long,

    @ColumnInfo(name = "promotion_price")
    val promotionPrice: Double){

}