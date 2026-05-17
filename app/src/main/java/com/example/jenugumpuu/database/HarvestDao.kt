package com.example.jenugumpuu.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.jenugumpuu.model.Harvest

@Dao
interface HarvestDao {

    @Insert
    suspend fun insertHarvest(harvest: Harvest)

    @Query("SELECT SUM(quantity) FROM harvest_table")
    fun getTotalStock(): LiveData<Double?>

    @Query("SELECT * FROM harvest_table")
    fun getAllHarvests(): LiveData<List<Harvest>>

    @Query("DELETE FROM harvest_table")
    suspend fun deleteAllHarvests()
}