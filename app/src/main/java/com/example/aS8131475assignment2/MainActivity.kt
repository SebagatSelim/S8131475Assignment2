package com.example.aS8131475assignment2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.aS8131475assignment2.viewmodel.LoginState
import com.example.aS8131475assignment2.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val usernameInput = findViewById<EditText>(R.id.usernameInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val errorText = findViewById<TextView>(R.id.errorText)
        val loadingSpinner = findViewById<ProgressBar>(R.id.loadingSpinner)

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            viewModel.login(username, password)
        }

        viewModel.loginState.observe(this) { state ->
            when (state) {
                is LoginState.Idle -> {
                    loadingSpinner.visibility = android.view.View.GONE
                    errorText.visibility = android.view.View.GONE
                }
                is LoginState.Loading -> {
                    loadingSpinner.visibility = android.view.View.VISIBLE
                    errorText.visibility = android.view.View.GONE
                    loginButton.isEnabled = false
                }
                is LoginState.Success -> {
                    loadingSpinner.visibility = android.view.View.GONE
                    loginButton.isEnabled = true
                    val intent = Intent(this, DashboardActivity::class.java)
                    intent.putExtra("KEYPASS", state.keypass)
                    startActivity(intent)
                    finish()
                }
                is LoginState.Error -> {
                    loadingSpinner.visibility = android.view.View.GONE
                    loginButton.isEnabled = true
                    errorText.text = state.message
                    errorText.visibility = android.view.View.VISIBLE
                }
            }
        }
    }
}