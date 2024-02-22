package com.rasel.androidbaseapp.remote.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.rasel.androidbaseapp.util.NoInternetException
import com.rasel.androidbaseapp.util.isNetworkAvailable
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor(context: Context
) : Interceptor {

    private val applicationContext = context.applicationContext

      override fun intercept(chain: Interceptor.Chain): Response {
        if(!applicationContext.isNetworkAvailable()) throw NoInternetException("Make sure you have an active data connection")

        return chain.proceed(chain.request())

      /*    var request = chain.request()
          request = if (isNetworkAvailable())
              request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
          else
              request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
        return  chain.proceed(request)*/

         /* val builder = chain.request().newBuilder()
          builder.addHeader("Accept", "application/json")*/

    }
   /* private fun isNetworkAvailable(): Boolean {

        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw      = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }*/

}

/*class NetworkConnectionInterceptor(private val ctx: WeakReference<Context>) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (ctx.get() !is Application) throw IllegalArgumentException("You mast provide an application context to don't cause memory leaks")


        return ctx.get()?.takeIf {
            it.isConnected() }?.run {
            return@run chain.request().run(chain::proceed)
        } ?: throw ConnectivityException()
    }

    class ConnectivityException : IOException() {
        override val message: String = "Connection Not Found Exception"
    }
} */