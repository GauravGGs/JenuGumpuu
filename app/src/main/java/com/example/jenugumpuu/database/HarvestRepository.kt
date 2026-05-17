package com.example.jenugumpuu.database

import androidx.lifecycle.LiveData
import com.example.jenugumpuu.model.Harvest

class HarvestRepository(private val harvestDao: HarvestDao) {

    val allHarvests: LiveData<List<Harvest>> = harvestDao.getAllHarvests()
    val totalStock: LiveData<Double?> = harvestDao.getTotalStock()

    suspend fun insert(harvest: Harvest) {
        harvestDao.insertHarvest(harvest)
    }

    suspend fun deleteAll() {
        harvestDao.deleteAllHarvests()
    }
}