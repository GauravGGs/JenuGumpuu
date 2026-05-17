package com.example.jenugumpuu.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.jenugumpuu.database.AppDatabase
import com.example.jenugumpuu.database.HarvestRepository
import com.example.jenugumpuu.model.Harvest
import kotlinx.coroutines.launch

class HarvestViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HarvestRepository
    val allHarvests: LiveData<List<Harvest>>
    val totalStock: LiveData<Double?>

    init {
        val harvestDao = AppDatabase.getDatabase(application).harvestDao()
        repository = HarvestRepository(harvestDao)
        allHarvests = repository.allHarvests
        totalStock = repository.totalStock
    }

    fun insert(harvest: Harvest) = viewModelScope.launch {
        repository.insert(harvest)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}