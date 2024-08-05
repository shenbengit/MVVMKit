package com.shencoder.mvvmkit.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread

/**
 *
 *
 * @date    2023/11/06 21:14
 *
 */
class NetworkObserverManager private constructor() {

    private object Holder {
        val INSTANCE = NetworkObserverManager()
    }

    companion object {
        @JvmStatic
        fun getInstance() = Holder.INSTANCE
    }

    private lateinit var connectivityManager: ConnectivityManager

    private val listenerList = mutableListOf<Listener>()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) = onConnectivityChange(network, true)
        override fun onLost(network: Network) = onConnectivityChange(network, false)
    }
    private val handler = Handler(Looper.getMainLooper())

    private var lastOnline = false

    val isOnline: Boolean
        get() = connectivityManager.allNetworks.any { it.isOnline() }

    fun init(context: Context) {
        connectivityManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.applicationContext.getSystemService(ConnectivityManager::class.java)
        } else {
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkCallback)

        lastOnline = isOnline
    }

    fun addListener(listener: Listener) {
        listenerList.add(listener)
    }

    fun removeListener(listener: Listener) {
        listenerList.remove(listener)
    }

    fun shutdown() {
        if (this::connectivityManager.isInitialized) {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }

    private fun onConnectivityChange(network: Network, isOnline: Boolean) {
        val isAnyOnline = connectivityManager.allNetworks.any {
            if (it == network) {
                // Don't trust the network capabilities for the network that just changed.
                isOnline
            } else {
                it.isOnline()
            }
        }
        if (isAnyOnline == lastOnline) {
            return
        }
        lastOnline = isOnline

        handler.post {
            listenerList.forEach { listener -> listener.onConnectivityChange(isAnyOnline) }
        }
    }

    private fun Network.isOnline(): Boolean {
        val capabilities = connectivityManager.getNetworkCapabilities(this)
        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun interface Listener {

        @MainThread
        fun onConnectivityChange(isOnline: Boolean)
    }
}