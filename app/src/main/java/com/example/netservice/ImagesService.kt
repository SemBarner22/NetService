package com.example.netservice

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder


class ImagesService : Service() {

    private var binder: IBinder = LocalBinder(this)
    var pictures: ArrayList<User>? = null


    class LocalBinder(private val service: ImagesService) : Binder() {
        fun getPictures(): ArrayList<User>? {
            return service.pictures
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

    override fun onCreate() {
        super.onCreate()
        AsyncPreviewLoader(this@ImagesService).apply {
            execute()
        }
    }
}
