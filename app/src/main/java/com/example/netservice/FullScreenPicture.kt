package com.example.netservice

import android.app.IntentService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.main.fullscreen.*
import java.io.ByteArrayOutputStream
import java.lang.ref.WeakReference
import java.net.URL

class FullScreenPicture : AppCompatActivity() {
    private var image: Bitmap? = null
    private var myBroadCastReceiver: BroadcastReceiver? = null
    private var imageUrl: String? = null

    private class ImageLoader(activity: FullScreenPicture) : AsyncTask<String, Void, Bitmap>() {
        private val activityWeakRef = WeakReference(activity)

        override fun doInBackground(vararg params: String?): Bitmap {
            val url = URL(params[0])
            return url.openConnection().getInputStream().use {
                BitmapFactory.decodeStream(it)
            }
        }

        override fun onPostExecute(result: Bitmap?) {
            val activity = activityWeakRef.get()
            activity?.onImageLoaded(result)
        }
    }
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fullscreen)
        imageUrl = intent.extras?.getString("link")
//        startService(Intent(this, LoadFullImage::class.java).apply {
////            putExtra(PICTURE_TAG, intent.getStringExtra(PICTURE_TAG))
//        })

//        myBroadCastReceiver = object : BroadcastReceiver() {
//            override fun onReceive(context: Context?, intent: Intent?) {
//                Log.e("SC", "Received")
//                val byteArray = intent?.getByteArrayExtra("byteArray")
//                if (byteArray != null) {
//                    val byteImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//                    picture.setImageBitmap(byteImage)
//                }
//            }
//        }
//
//        LocalBroadcastManager.getInstance(this)
//            .registerReceiver(myBroadCastReceiver as BroadcastReceiver,
//                IntentFilter("response").apply {
//                    addCategory(Intent.CATEGORY_DEFAULT)
//                })
//        Log.e("SC", "Activity created")
    }


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
//                picture.setImageBitmap(openFileInput("temp.txt").use { BitmapFactory.decodeStream(it) })
//                pb.visibility = View.GONE
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

    internal fun onImageLoaded(result: Bitmap?) {
        image = result
        picture.setImageBitmap(result)
    }

    override fun onResume() {
        super.onResume()
        if (image == null) ImageLoader(this).execute(imageUrl)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("image", image)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        image = savedInstanceState.getParcelable("image")
        if (image != null) {
            onImageLoaded(image)
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(myBroadCastReceiver!!)
//    }

}

class LoadFullImage : IntentService("LoadFullImage") {

    init {
        setIntentRedelivery(true)
    }

    override fun onHandleIntent(intent: Intent?) {
        val url = intent?.getStringExtra("link") ?: return
//        val bundle: Bundle = intent?.extras ?: return
//        val url = (bundle["user"] as User).highQ
        val image = BitmapFactory.decodeStream(URL(url).openConnection().getInputStream())
        if (image == null)
            Log.e("SC", "No image")
        else
            Log.e("SC", "Yes image")
        LocalBroadcastManager.getInstance(baseContext).sendBroadcast(Intent().apply {
            action = "response"
            addCategory(Intent.CATEGORY_DEFAULT)
            putExtra(
                "byteArray", ByteArrayOutputStream().apply {
                    image.compress(Bitmap.CompressFormat.JPEG, 100, this)
                }.toByteArray()
            )
//        val url = intent?.extras!!.getString("link")
//        var url1 = URL(url)
//        var b: Bitmap = url1.openConnection().getInputStream().use {
//            BitmapFactory.decodeStream(it)
//        }
////        val a = BitmapFactory.decodeStream(URL(url).openConnection().getInputStream())
//        val fileOutput = openFileOutput("temp.txt", MODE_PRIVATE)
//        fileOutput.use {
//            b.compress(Bitmap.CompressFormat.PNG, 100, fileOutput)
//            fileOutput.flush()
//        }
//        val responseIntent = Intent("loaded")
//        responseIntent.putExtra("progress", "ready")
//        sendBroadcast(responseIntent)
        })
    }
}

