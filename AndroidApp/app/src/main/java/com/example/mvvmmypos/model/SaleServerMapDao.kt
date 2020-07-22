package com.example.mvvmmypos.model

import androidx.room.*

@Dao
interface SaleServerMapDao {
    @Query("SELECT * FROM sale_server_map")
    fun getAll(): List<SaleServerMap>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(saleServerMap: SaleServerMap)

    @Query("DELETE FROM sale_server_map")
    fun deleteAll()
}