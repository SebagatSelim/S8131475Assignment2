package com.example.aS8131475assignment2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aS8131475assignment2.util.toDisplayLabel

class EntityAdapter(
    private val entities: List<Map<String, String>>,
    private val onItemClick: (Map<String, String>) -> Unit
) : RecyclerView.Adapter<EntityAdapter.EntityViewHolder>() {

    class EntityViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(com.example.aS8131475assignment2.R.id.entityTitle)
        val subtitleText: TextView = itemView.findViewById(com.example.aS8131475assignment2.R.id.entitySubtitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(com.example.aS8131475assignment2.R.layout.item_entity, parent, false)
        return EntityViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntityViewHolder, position: Int) {
        val entity = entities[position]

        // Exclude "description" from the summary shown in the list
        val summaryFields = entity.filterKeys { it != "description" }

        val fields = summaryFields.entries.toList()
        holder.titleText.text = fields.firstOrNull()?.value ?: "Item ${position + 1}"
        holder.subtitleText.text = fields.drop(1).joinToString("\n") { "${it.key.toDisplayLabel()}: ${it.value}" }

        holder.itemView.setOnClickListener {
            onItemClick(entity)
        }
    }

    override fun getItemCount(): Int = entities.size
}