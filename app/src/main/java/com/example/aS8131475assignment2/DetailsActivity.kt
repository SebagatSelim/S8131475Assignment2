package com.example.aS8131475assignment2

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.aS8131475assignment2.util.toDisplayLabel
import com.google.android.material.appbar.MaterialToolbar

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        findViewById<MaterialToolbar>(R.id.toolbar).setNavigationOnClickListener { finish() }

        val container = findViewById<LinearLayout>(R.id.detailsContainer)

        val extras = intent.extras
        if (extras == null || extras.isEmpty) {
            addRow(container, "Error", "No details were passed to this screen")
            return
        }

        // Show the fields in the order the API sent them, description last in its own card
        val keys = intent.getStringArrayListExtra(EXTRA_FIELD_ORDER) ?: extras.keySet().toList()
        for (key in keys) {
            if (key == EXTRA_FIELD_ORDER || key.equals("description", ignoreCase = true)) continue
            val value = extras.getString(key) ?: continue
            addRow(container, key.toDisplayLabel(), value)
        }

        val description = extras.getString("description")
        if (!description.isNullOrBlank()) {
            findViewById<TextView>(R.id.descriptionText).text = description
            findViewById<View>(R.id.descriptionCard).visibility = View.VISIBLE
        }
    }

    private fun addRow(container: LinearLayout, label: String, value: String) {
        val row = layoutInflater.inflate(R.layout.item_detail_row, container, false)
        row.findViewById<TextView>(R.id.detailLabel).text = label
        row.findViewById<TextView>(R.id.detailValue).text = value
        container.addView(row)
    }

    companion object {
        const val EXTRA_FIELD_ORDER = "FIELD_ORDER"
    }
}
