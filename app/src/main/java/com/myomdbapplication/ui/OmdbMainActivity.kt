package com.myomdbapplication.ui

import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.myomdbapplication.R
import com.myomdbapplication.connectivity.ConnectivityProvider
import com.myomdbapplication.connectivity.ConnectivityProvider.ConnectivityState
import com.myomdbapplication.connectivity.ConnectivityProvider.ConnectivityStateListener
import com.myomdbapplication.connectivity.hasInternet
import com.myomdbapplication.databinding.ActivityOmdbMainBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel

enum class SnackbarAction {
    NETWORK_NOT_AVAILABLE,
    API_RETRY
}

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class OmdbMainActivity : AppCompatActivity(), ConnectivityStateListener {

    private lateinit var binding: ActivityOmdbMainBinding
    private val viewModel: OmdbViewModel by viewModel()
    private val provider: ConnectivityProvider by lazy { ConnectivityProvider.createProvider(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        provider.addListener(this)
        binding = ActivityOmdbMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        findNavController(R.id.nav_host_fragment).setGraph(R.navigation.app_nav)
    }

    override fun onStateChange(state: ConnectivityState) {
        viewModel.connectivityState.postValue(state.hasInternet())
    }

    fun displaySnackBar(@StringRes msg: Int, @StringRes action: Int, actionState: SnackbarAction) {
        val snackbar = Snackbar.make(binding.root, msg, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(action) {
            when (actionState) {
                SnackbarAction.NETWORK_NOT_AVAILABLE -> {
                    startActivity(
                        Intent("android.settings.WIRELESS_SETTINGS")
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                    viewModel.isNetworkNotAvailable = true
                }
                SnackbarAction.API_RETRY -> {
                }
            }
            if (snackbar.isShown) snackbar.dismiss()
        }
        snackbar.show()
    }

    override fun onStop() {
        super.onStop()
        provider.removeListener(this)
    }
}
