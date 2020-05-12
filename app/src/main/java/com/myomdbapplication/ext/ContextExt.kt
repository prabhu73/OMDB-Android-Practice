package com.myomdbapplication.ext

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Context.hideKeyboard(view: View) {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.isNetworkAvailable(): Boolean? {
    val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var isWifiConn = false
    var isMobileConn = false
    connMgr.allNetworks.forEach { network ->
        connMgr.getNetworkInfo(network).apply {
            if (type == ConnectivityManager.TYPE_WIFI) {
                isWifiConn = isWifiConn or isConnected
            }
            if (type == ConnectivityManager.TYPE_MOBILE) {
                isMobileConn = isMobileConn or isConnected
            }
        }
    }
    return isWifiConn || isMobileConn
}