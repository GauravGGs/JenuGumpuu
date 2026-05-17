package com.example.jenugumpuu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.jenugumpuu.R
import com.example.jenugumpuu.model.Harvest
import java.util.Locale

class HarvestAdapter : ListAdapter<Harvest, HarvestAdapter.HarvestViewHolder>(HarvestDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HarvestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_harvest, parent, false)
        return HarvestViewHolder(view)
    }

    override fun onBindViewHolder(holder: HarvestViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HarvestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvBatchId: TextView = itemView.findViewById(R.id.tvBatchId)
        private val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        private val tvDetails: TextView = itemView.findViewById(R.id.tvDetails)
        private val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        private val tvGradeIcon: TextView = itemView.findViewById(R.id.tvGradeIcon)

        fun bind(harvest: Harvest) {
            tvBatchId.text = harvest.batchId
            tvLocation.text = harvest.location
            tvDetails.text = String.format("%s | %s | Grade: %s", harvest.date, harvest.floralSource, harvest.grade)
            tvQuantity.text = String.format(Locale.getDefault(), "%.1f KG", harvest.quantity)
            
            // Set Icon based on Grade
            tvGradeIcon.text = when(harvest.grade) {
                "Grade A", "A" -> "🥇"
                "Grade B", "B" -> "🥈"
                "Grade C", "C" -> "🥉"
                else -> "🍯"
            }
        }
    }

    class HarvestDiffCallback : DiffUtil.ItemCallback<Harvest>() {
        override fun areItemsTheSame(oldItem: Harvest, newItem: Harvest): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Harvest, newItem: Harvest): Boolean = oldItem == newItem
    }
}