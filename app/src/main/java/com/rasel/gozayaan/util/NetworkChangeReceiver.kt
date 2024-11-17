package com.rasel.gozayaan.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NetworkChangeReceiver : BroadcastReceiver() {
    private var connectionChangeCallback: ConnectionChangeCallback? = null
    override fun onReceive(context: Context, intent: Intent) {
        val isConnected = context.isNetworkAvailable()
        if (connectionChangeCallback != null) {
            connectionChangeCallback!!.onConnectionChange(isConnected)
        }
    }

    fun setConnectionChangeCallback(connectionChangeCallback: ConnectionChangeCallback?) {
        this.connectionChangeCallback = connectionChangeCallback
    }

    interface ConnectionChangeCallback {
        fun onConnectionChange(isConnected: Boolean)
    }

    companion object {
        const val ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE"
    }
}
