package com.example.netservice

import android.app.IntentService
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL



class ImagesService(activity: MainActivity?) : Service() {

    constructor() : this(null) {}
    private var binder: IBinder = LocalBinder(this)
    var activityRef = WeakReference(activity)
    var a: AsyncTaskImpl? = null
    var pictures: ArrayList<User>? = null

//    init {
//        setIntentRedelivery(true)
//    }

    //    private fun method() {
//        a?.activityRef = WeakReference(null)
//
//    }

    class LocalBinder(private val service: ImagesService) : Binder() {
        fun getPictures(): ArrayList<User>? {
            return service.pictures
        }
//        fun getService(): ImagesService = ImagesService
    }

    override fun onBind(intent: Intent?): IBinder? {
        return if (pictures == null) null else binder
    }

    fun setMoviesInUI(curList: ArrayList<User>?) {
//        if (curList != null) {
//            next(curList)
//        }
//        a = null
        Log.e("SC", "setMoviesInUI")
        pictures = curList
        val intent = Intent("PreviewsDownloaded")
        sendBroadcast(intent)
//        activityRef.get()?.createRecyclerView(curList)
//        LocalBroadcastManager.getInstance(baseContext).sendBroadcast(Intent().apply {
//            action = "response"
//            addCategory(Intent.CATEGORY_DEFAULT)
//            putExtra(
//                "byteArray", ByteArrayOutputStream().apply {
//                    image.compress(Bitmap.CompressFormat.JPEG, 10, this)
//                }.toByteArray()
//            )
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("SC", "StartedService")
        AsyncTaskImpl(this@ImagesService).apply {
            execute("house")
//                activityRef.get()?.let { it1 -> attachActivity(it1) }
        }
    }

//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

//        a = activityRef.get()?.let { AsyncTaskImpl(it, this) }
////        a?.attachActivity(this)
//        method()

//        val url = intent?.extras!!.getString("link")
//        val a = BitmapFactory.decodeStream(URL(url).openConnection().getInputStream())
//        val fileOutput = openFileOutput("temp.txt", MODE_PRIVATE)
//        fileOutput.use {
//            a.compress(Bitmap.CompressFormat.PNG, 100, fileOutput)
//            fileOutput.flush()
//        }
//        val responseIntent = Intent("loaded")
//        responseIntent.putExtra("progress", "ready")
//        sendBroadcast(responseIntent)
//        val url = intent?.getStringExtra("link") ?: return
//        val image = BitmapFactory.decodeStream(URL(url).openConnection().getInputStream())
//
//        LocalBroadcastManager.getInstance(baseContext).sendBroadcast(Intent().apply {
//            action = "response"
//            addCategory(Intent.CATEGORY_DEFAULT)
//            putExtra("byteArray", ByteArrayOutputStream().apply {
//                image.compress(Bitmap.CompressFormat.JPEG, 100, this)
//            }.toByteArray()
//            )
//        })
//        return 0
//    }

}
//class FullScreenPicture : AppCompatActivity() {
//
//    private var loaded = false
//    private lateinit var mMyBroadcastReceiver: MyBroadcastReceiver
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fullscreen)
//        mMyBroadcastReceiver = MyBroadcastReceiver()
//        val intentFilter = IntentFilter("loaded")
//        registerReceiver(mMyBroadcastReceiver, intentFilter)
//        val url = intent?.getStringExtra("link")
//        if (savedInstanceState != null) {
//            loaded = savedInstanceState.getBoolean("loaded")
//            if (loaded) {
//                .setImageBitmap(openFileInput("temp.txt").use { BitmapFactory.decodeStream(it) })
//                p.visibility = View.GONE
//            }
//        } else {
//            val intentService = Intent(this, LoadFullImage::class.java)
//            intentService.putExtras(Bundle().apply {
//                putString("link", url)
//            })
//            startService(intentService)
//        }
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        outState.putBoolean("loaded", loaded)
//        super.onSaveInstanceState(outState)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        unregisterReceiver(mMyBroadcastReceiver)
//    }
//
//    inner class MyBroadcastReceiver : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            loaded = true
//            if (intent!!.getStringExtra("progress") == "ready") {
//                picture.setImageBitmap(openFileInput("temp.txt").use { BitmapFactory.decodeStream(it) })
//                pb.visibility = View.GONE
//            }
//        }
//    }


//    class ImagesService(name: String = "") : IntentService(name) {
//
//        override fun onHandleIntent(intent: Intent?) {
//            val url = intent?.extras!!.getString("link")
//            val a = BitmapFactory.decodeStream(URL(url).openConnection().getInputStream())
//            val fileOutput = openFileOutput("temp.txt", MODE_PRIVATE)
//            fileOutput.use {
//                a.compress(Bitmap.CompressFormat.PNG, 100, fileOutput)
//                fileOutput.flush()
//            }
//            val responseIntent = Intent("loaded")
//            responseIntent.putExtra("progress", "ready")
//            sendBroadcast(responseIntent)
//        }
//
//    }
//}

//        finding.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
//            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
//                //Perform Code
////                finding.text.toString()
//
//                finding.text.clear()
////                val inputManager: InputMethodManager =
////                    this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
////                inputManager.hideSoftInputFromWindow(
////                    this.currentFocus!!.windowToken,
////                    InputMethodManager.HIDE_NOT_ALWAYS
//                return@OnKeyListener true
//            }
//            false
//        })