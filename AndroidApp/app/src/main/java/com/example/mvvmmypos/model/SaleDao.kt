package com.example.mvvmmypos.model

import androidx.room.*

@Dao
interface SaleDao {
    @Query("SELECT * FROM sale")
    fun getAll(): List<Sale>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(sale: Sale):Long

    @Query("DELETE FROM sale")
    fun deleteAll()

    @Query("SELECT * FROM sale where update_time>:dt")
    fun getFromDate(dt:Long): List<Sale>
}