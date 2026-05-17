package com.example.jenugumpuu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jenugumpuu.adapter.HarvestAdapter
import com.example.jenugumpuu.model.Harvest
import com.example.jenugumpuu.viewmodel.HarvestViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: HarvestViewModel
    private lateinit var adapter: HarvestAdapter

    override fun attachBaseContext(newBase: Context) {
        val lang = LocaleHelper.getPersistedLocale(newBase)
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[HarvestViewModel::class.java]

        val mainScrollView = findViewById<ScrollView>(R.id.mainScrollView)
        val etDate = findViewById<EditText>(R.id.etDate)
        val etLocation = findViewById<EditText>(R.id.etLocation)
        val etQuantity = findViewById<EditText>(R.id.etQuantity)
        val etMoisture = findViewById<EditText>(R.id.etMoisture)
        val spinnerFloral = findViewById<Spinner>(R.id.spinnerFloral)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val tvTotalStock = findViewById<TextView>(R.id.tvTotalStock)
        val tvProfit = findViewById<TextView>(R.id.tvProfit)
        val rvBatches = findViewById<RecyclerView>(R.id.rvBatches)

        val cardHarvest = findViewById<CardView>(R.id.cardHarvest)
        val cardGrading = findViewById<CardView>(R.id.cardGrading)
        val cardPrice = findViewById<CardView>(R.id.cardPrice)
        val cardBatch = findViewById<CardView>(R.id.cardBatch)
        val formContainer = findViewById<CardView>(R.id.formContainer)

        val navHome = findViewById<View>(R.id.navHome)
        val navReports = findViewById<View>(R.id.navReports)
        val navBatches = findViewById<View>(R.id.navBatches)
        val navSettings = findViewById<View>(R.id.navSettings)

        // Setup RecyclerView for Batch Tracking
        adapter = HarvestAdapter()
        rvBatches.layoutManager = LinearLayoutManager(this)
        rvBatches.adapter = adapter

        // Check if we need to force reset (Clean Slate)
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val isFirstRun = sharedPref.getBoolean("is_first_run_clean_v2", true)
        if (isFirstRun) {
            resetDataToRequestedSet()
            sharedPref.edit().putBoolean("is_first_run_clean_v2", false).apply()
        }

        findViewById<TextView>(R.id.app_name_tv)?.setOnLongClickListener {
            resetDataToRequestedSet()
            Toast.makeText(this, "Database Reset", Toast.LENGTH_SHORT).show()
            true
        }
        
        viewModel.allHarvests.observe(this) { harvests ->
            adapter.submitList(harvests)
            var totalProfit = 0.0
            for (h in harvests) {
                totalProfit += (h.retailPrice * h.quantity) - (h.filteringCost * h.quantity)
            }
            tvProfit.text = String.format(Locale.getDefault(), "₹%.0f", totalProfit)
        }

        // Set current date as default
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        etDate.setText(sdf.format(Date()))

        val floralList = listOf("Coffee Blossom", "Wildflower", "Neem Blossom", "Sunflower")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, floralList)
        spinnerFloral.adapter = spinnerAdapter

        btnSave.setOnClickListener {
            val date = etDate.text.toString()
            val location = etLocation.text.toString()
            val quantityStr = etQuantity.text.toString()
            val moistureStr = etMoisture.text.toString()

            if (location.isEmpty() || quantityStr.isEmpty() || moistureStr.isEmpty()) {
                Toast.makeText(this, getString(R.string.save_harvest), Toast.LENGTH_SHORT).show() // Use a placeholder for demo
                return@setOnClickListener
            }

            val quantity = quantityStr.toDoubleOrNull() ?: 0.0
            val moisture = moistureStr.toIntOrNull() ?: 20
            val grade = when {
                moisture <= 15 -> "Grade A"
                moisture <= 20 -> "Grade B"
                else -> "Grade C"
            }

            val floralSource = spinnerFloral.selectedItem.toString()
            var retailPrice = 400.0
            var wholesalePrice = 240.0
            val filteringCost = 50.0

            when (floralSource) {
                "Coffee Blossom" -> { retailPrice = 420.0; wholesalePrice = 250.0 }
                "Wildflower" -> { retailPrice = 390.0; wholesalePrice = 220.0 }
                "Neem Blossom" -> { retailPrice = 450.0; wholesalePrice = 270.0 }
                "Sunflower" -> { retailPrice = 380.0; wholesalePrice = 210.0 }
            }

            val batchId = "BATCH-${System.currentTimeMillis() % 10000}"
            val harvest = Harvest(
                date = date, location = location, quantity = quantity,
                floralSource = floralSource, grade = grade, batchId = batchId,
                retailPrice = retailPrice, wholesalePrice = wholesalePrice, filteringCost = filteringCost
            )

            viewModel.insert(harvest)
            Toast.makeText(this, "Saved! ID: $batchId", Toast.LENGTH_SHORT).show()
            etLocation.text.clear()
            etQuantity.text.clear()
            etMoisture.text.clear()
        }

        viewModel.totalStock.observe(this) { total ->
            tvTotalStock.text = String.format(Locale.getDefault(), "%.1f KG", total ?: 0.0)
        }

        // Dashboard Interactions
        cardHarvest.setOnClickListener { mainScrollView.smoothScrollTo(0, formContainer.top) }
        cardGrading.setOnClickListener { startActivity(Intent(this, GradingActivity::class.java)) }
        cardPrice.setOnClickListener { startActivity(Intent(this, PriceMonitorActivity::class.java)) }
        cardBatch.setOnClickListener { mainScrollView.smoothScrollTo(0, rvBatches.top) }
        
        // Bottom Nav
        navHome.setOnClickListener { mainScrollView.smoothScrollTo(0, 0) }
        navReports.setOnClickListener { startActivity(Intent(this, GuidelinesActivity::class.java)) }
        navBatches.setOnClickListener { mainScrollView.smoothScrollTo(0, rvBatches.top) }
        navSettings.setOnClickListener { startActivity(Intent(this, SettingsActivity::class.java)) }
    }

    private fun resetDataToRequestedSet() {
        viewModel.deleteAll()
        val samples = listOf(
            Harvest(date = "11-05-2026", location = "Kodagu Forest", quantity = 25.0, floralSource = "Coffee Blossom", grade = "Grade A", batchId = "BATCH-1001", retailPrice = 420.0, wholesalePrice = 250.0, filteringCost = 50.0),
            Harvest(date = "12-05-2026", location = "BR Hills", quantity = 18.0, floralSource = "Wildflower", grade = "Grade B", batchId = "BATCH-1002", retailPrice = 390.0, wholesalePrice = 220.0, filteringCost = 50.0),
            Harvest(date = "14-05-2026", location = "Chikkamagaluru", quantity = 30.0, floralSource = "Neem Blossom", grade = "Grade A", batchId = "BATCH-1003", retailPrice = 450.0, wholesalePrice = 270.0, filteringCost = 50.0),
            Harvest(date = "15-05-2026", location = "Bandipur Forest", quantity = 22.0, floralSource = "Sunflower", grade = "Grade A", batchId = "BATCH-1004", retailPrice = 380.0, wholesalePrice = 210.0, filteringCost = 50.0),
            Harvest(date = "16-05-2026", location = "Agumbe Forest", quantity = 15.0, floralSource = "Wildflower", grade = "Grade C", batchId = "BATCH-1005", retailPrice = 390.0, wholesalePrice = 220.0, filteringCost = 50.0)
        )
        for (sample in samples) viewModel.insert(sample)
    }
}