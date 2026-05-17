package com.example.jenugumpuu

import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jenugumpuu.adapter.HarvestAdapter
import com.example.jenugumpuu.viewmodel.HarvestViewModel

class BatchTrackerActivity : AppCompatActivity() {

    private lateinit var viewModel: HarvestViewModel
    private lateinit var adapter: HarvestAdapter

    override fun attachBaseContext(newBase: Context) {
        val lang = LocaleHelper.getPersistedLocale(newBase)
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_batch_tracker)

        viewModel = ViewModelProvider(this)[HarvestViewModel::class.java]

        val rvBatchTracker = findViewById<RecyclerView>(R.id.rvBatchTracker)
        val searchView = findViewById<SearchView>(R.id.searchView)
        val btnBack = findViewById<Button>(R.id.btnBackBatch)

        adapter = HarvestAdapter()
        rvBatchTracker.layoutManager = LinearLayoutManager(this)
        rvBatchTracker.adapter = adapter

        viewModel.allHarvests.observe(this) { harvests ->
            adapter.submitList(harvests)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterBatches(newText)
                return true
            }
        })

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun filterBatches(query: String?) {
        val currentList = viewModel.allHarvests.value ?: return
        if (query.isNullOrBlank()) {
            adapter.submitList(currentList)
        } else {
            val filtered = currentList.filter { 
                it.batchId.contains(query, ignoreCase = true) || 
                it.location.contains(query, ignoreCase = true)
            }
            adapter.submitList(filtered)
        }
    }
}