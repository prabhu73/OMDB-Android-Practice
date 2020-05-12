package com.myomdbapplication.connectivity

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkInfo
import android.os.Build
import androidx.annotation.RequiresApi
import com.myomdbapplication.connectivity.ConnectivityProvider.ConnectivityState

interface ConnectivityProvider {
    interface ConnectivityStateListener {
        fun onStateChange(state: ConnectivityState)
    }

    fun addListener(listener: ConnectivityStateListener)
    fun removeListener(listener: ConnectivityStateListener)

    fun getNetworkState(): ConnectivityState

    @Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")
    sealed class ConnectivityState {
        object NotConnectedState : ConnectivityState()

        sealed class ConnectedState(val hasInternet: Boolean) : ConnectivityState() {

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            data class Connected(val capabilities: NetworkCapabilities) : ConnectedState(
                capabilities.hasCapability(NET_CAPABILITY_INTERNET)
            )

            @Suppress("DEPRECATION")
            data class ConnectedLegacy(val networkInfo: NetworkInfo) : ConnectedState(
                networkInfo.isConnectedOrConnecting
            )
        }
    }

    companion object {
        fun createProvider(context: Context): ConnectivityProvider {
            val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ConnectivityProviderImpl(cm)
            } else {
                ConnectivityProviderLegacyImpl(context, cm)
            }
        }
    }
}

fun ConnectivityState.hasInternet(): Boolean = (this as? ConnectivityState.ConnectedState)?.hasInternet == true