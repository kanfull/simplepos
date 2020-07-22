package com.example.mvvmmypos.model

import androidx.room.*

@Dao
interface SaleItemDao {
    @Query("SELECT * FROM sale_item")
    fun getAll(): List<SaleItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(saleItem: SaleItem):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(item: List<SaleItem>)

    @Query("DELETE FROM sale_item")
    fun deleteAll()

    @Query("SELECT * FROM sale_item where update_time>:dt and sale_id=:saleId")
    fun getFromDateById(dt:Long,saleId:Long): List<SaleItem>
}