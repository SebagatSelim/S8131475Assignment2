package com.example.aS8131475assignment2

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aS8131475assignment2.adapter.EntityAdapter
import com.example.aS8131475assignment2.viewmodel.DashboardState
import com.example.aS8131475assignment2.viewmodel.DashboardViewModel
import com.google.android.material.appbar.MaterialToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardActivity : AppCompatActivity() {

    private val viewModel: DashboardViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        val recyclerView = findViewById<RecyclerView>(R.id.entityRecyclerView)
        val loadingSpinner = findViewById<ProgressBar>(R.id.dashboardLoading)
        val errorText = findViewById<TextView>(R.id.dashboardError)

        recyclerView.layoutManager = LinearLayoutManager(this)

        toolbar.inflateMenu(R.menu.menu_dashboard)
        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_logout) {
                logout()
                true
            } else {
                false
            }
        }

        val keypass = intent.getStringExtra("KEYPASS")
        if (keypass.isNullOrBlank()) {
            errorText.text = "No keypass received"
            errorText.visibility = android.view.View.VISIBLE
        } else {
            viewModel.loadDashboard(keypass)
        }

        viewModel.dashboardState.observe(this) { state ->
            when (state) {
                is DashboardState.Loading -> {
                    loadingSpinner.visibility = android.view.View.VISIBLE
                    errorText.visibility = android.view.View.GONE
                }
                is DashboardState.Success -> {
                    loadingSpinner.visibility = android.view.View.GONE
                    errorText.visibility = android.view.View.GONE
                    toolbar.subtitle = getString(R.string.dashboard_item_count, state.entities.size)

                    recyclerView.adapter = EntityAdapter(state.entities) { selectedEntity ->
                        val intent = Intent(this, DetailsActivity::class.java)
                        for ((key, value) in selectedEntity) {
                            intent.putExtra(key, value)
                        }
                        // A Bundle doesn't keep insertion order, so pass the API's field order separately
                        intent.putStringArrayListExtra(DetailsActivity.EXTRA_FIELD_ORDER, ArrayList(selectedEntity.keys))
                        startActivity(intent)
                    }
                }
                is DashboardState.Error -> {
                    loadingSpinner.visibility = android.view.View.GONE
                    errorText.text = state.message
                    errorText.visibility = android.view.View.VISIBLE
                }
            }
        }
    }

    // There's no stored session, so logging out just returns to a fresh login screen
    private fun logout() {
        val intent = Intent(this, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}
