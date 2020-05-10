package com.myomdbapplication.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.myomdbapplication.R
import com.myomdbapplication.databinding.ActivityOmdbMainBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class OmdbMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOmdbMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOmdbMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findNavController(R.id.nav_host_fragment).setGraph(R.navigation.app_nav)
    }
}
