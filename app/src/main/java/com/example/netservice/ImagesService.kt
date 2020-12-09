package com.example.netservice

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Binder
import android.os.IBinder
import android.util.Log


class ImagesService : Service() {

//    private fun isNetworkConnected(): Boolean {
//        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
//    }
    private var binder: IBinder = ImagesBinder(this)
    var pictures: ArrayList<User>? = null


    class ImagesBinder(private val service: ImagesService) : Binder() {
        fun getService() : ImagesService {
            return service
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    fun onPostExecute(curList: ArrayList<User>?) {
        pictures = curList
        val intent = Intent("images")
        sendBroadcast(intent)
    }


    override fun onStart(intent: Intent?, startId: Int) {
        Log.e("service", "images service")
        AsyncPreviewLoader(this@ImagesService).apply {
            execute()
        }
    }

    override fun onCreate() {
        super.onCreate()
//        Log.e("service", "images service")
//        AsyncPreviewLoader(this@ImagesService).apply {
//            execute()
//        }
    }
}
