package com.rasel.androidbaseapp.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.media.AudioAttributes
import android.os.Build
import android.provider.Settings
import androidx.multidex.MultiDexApplication
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.rasel.androidbaseapp.BuildConfig
import com.rasel.androidbaseapp.cache.preferences.PreferenceProvider
import com.rasel.androidbaseapp.core.theme.ThemeUtils
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject


@HiltAndroidApp
class BaseApplication : MultiDexApplication() {

    @Inject
    lateinit var themeUtils: ThemeUtils

    @Inject
    lateinit var preferencesHelper: PreferenceProvider

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        themeUtils.setNightMode(preferencesHelper.isNightMode)

        // used this logger throughout the app for logging
        /*Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })*/

        createNotificationChannel()
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Channel Leave Request" //getString(R.string.channel_received_leave_request)
            val descriptionText =
                "Channel for Received Leave Request" //getString(R.string.channel_leave_request_description)
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel =
                NotificationChannel(CHANNEL_ID_RECEIVED_LEAVE_REQUEST, name, importance).apply {
                    description = descriptionText
                }
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            channel.enableLights(true)
            channel.lightColor = Color.RED
            // channel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, )
            channel.enableVibration(true)
            channel.setShowBadge(true)


            // Register the channel with the system
            //   val notificationManager: NotificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val audioAttributes: AudioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            channel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, audioAttributes)

            val notificationManager = getSystemService(
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}