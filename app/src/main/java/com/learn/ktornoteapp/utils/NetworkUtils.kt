package com.learn.ktornoteapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun isNetworkManager(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
    return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}