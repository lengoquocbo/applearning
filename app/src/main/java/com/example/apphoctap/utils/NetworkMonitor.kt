package com.example.apphoctap.utils

interface NetworkMonitor {
    fun isNetworkAvailable(): Boolean
    fun startMonitoring(onNetworkStatusChanged: (Boolean) -> Unit)
    fun stopMonitoring()
}