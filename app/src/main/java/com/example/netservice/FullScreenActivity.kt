package com.example.netservice


import android.content.*
import android.graphics.Bitmap
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fullscreen.*


class FullScreenActivity : AppCompatActivity() {
    private var imageUrl: String? = null
    private var loadImageServiceIntent: Intent? = null
    private var mBound: Boolean = false
    private var isDownloaded = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fullscreen)
        imageUrl = intent.extras?.getString("link")
        loadImageServiceIntent = Intent(applicationContext, LoadFullImage::class.java)
        loadImageServiceIntent!!.putExtra("link", imageUrl)
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            mBound = true
            val binder = service as LoadFullImage.LoadBinder
            picture.setImageBitmap(binder.getService().image)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            isDownloaded = true
            bindService(loadImageServiceIntent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(broadcastReceiver, IntentFilter("response"))
        if (isDownloaded) {
            bindService(loadImageServiceIntent, connection, Context.BIND_AUTO_CREATE)
        }
        if (!isDownloaded) {
            startService(loadImageServiceIntent)
        }
    }

    override fun onStop() {
        super.onStop()
        if (mBound) unbindService(connection)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        loadImageServiceIntent = savedInstanceState.getParcelable("intent")
        isDownloaded = savedInstanceState.getBoolean("isDownloaded")
        mBound = savedInstanceState.getBoolean("mBound")
        if (isDownloaded) {
            bindService(loadImageServiceIntent, connection, Context.BIND_AUTO_CREATE)
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isDownloaded", isDownloaded)
        outState.putBoolean("mBound", mBound)
        outState.putParcelable("intent", loadImageServiceIntent)
    }

}