package com.example.netservice

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.lang.ref.WeakReference
import java.net.URL

class LoadFullImage : Service() {

    private var binder: IBinder = LoadBinder(this)
    var image: Bitmap? = null

    private class AsyncLoader(service: LoadFullImage) : AsyncTask<String, Void, Bitmap?>() {
        private val serviceRef = WeakReference(service)

        override fun doInBackground(vararg params: String?): Bitmap? {
            try {
                val url = URL(params[0])
                return url.openConnection().getInputStream().use {
                    BitmapFactory.decodeStream(it)
                }
            } catch (e : Exception) {
                return null
            }
        }

        override fun onPostExecute(result: Bitmap?) {
            serviceRef.get()?.onPostExecute(result)
        }
    }

    class LoadBinder(private val service: LoadFullImage) : Binder() {
        fun getService() : LoadFullImage {
            return service
        }
    }

    fun onPostExecute(result: Bitmap?) {
        if (result == null) {
            return
        }
        image = result
        ByteArrayOutputStream().apply {
            image?.compress(Bitmap.CompressFormat.JPEG, 50, this)
        }.toByteArray()
        val intent = Intent("response")
        sendBroadcast(intent)
    }

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStart(intent: Intent?, startId: Int) {
        val url = intent?.extras!!.getString("link")
        Log.e("LoadFullImage", "fullImage")
        AsyncLoader(this).execute(url)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }
}