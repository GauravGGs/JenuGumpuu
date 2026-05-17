package com.example.jenugumpuu.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "harvest_table")
data class Harvest(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val date: String,
    val location: String,
    val quantity: Double,
    val floralSource: String,
    val grade: String,
    val batchId: String,
    val retailPrice: Double,
    val wholesalePrice: Double,
    val filteringCost: Double
)