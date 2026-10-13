package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.data.AppDatabase
import com.example.data.DecorationRepository
import com.example.ui.DecorationViewModel
import com.example.ui.DecorationViewModelFactory
import com.example.ui.HomeScreen
import com.example.ui.theme.RavindraTheme

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: DecorationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext, lifecycleScope)
        val repository = DecorationRepository(database.decorationDao())
        val factory = DecorationViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[DecorationViewModel::class.java]

        setContent {
            RavindraTheme {
                HomeScreen(viewModel = viewModel)
            }
        }
    }
}
