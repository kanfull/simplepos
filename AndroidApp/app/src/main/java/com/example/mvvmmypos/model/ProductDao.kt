package com.example.mvvmmypos.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {
    @Query("SELECT * FROM product")
    fun getAll(): List<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(product: List<Product>)

    @Query("DELETE FROM product")
    fun deleteAll()

    @Query("SELECT product.product_id," +
            "product.product_name," +
            "price.price_id,price.price_value," +
            "promotion.promotion_id,promotion.promotion_price " +
            "FROM product INNER JOIN price " +
            "ON product.product_id = price.product_id " +
            "INNER JOIN promotion " +
            "ON product.product_id = promotion.product_id " +
            "WHERE price.valid=1 and promotion.valid=1")
    fun getAllProductItem():List<ProductItem>


}