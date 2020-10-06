package com.example.netservice

import android.app.IntentService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL


class MainActivity : AppCompatActivity() {

    lateinit var pictures: List<User>
//    private lateinit var mMyBroadcastReceiver: MyBroadcastReceiver
//    companion object {
//        const val accessKey = "RN2wHFLxJnmhFf-ZxhdoYO_WVKHAE1hq66CUbs3S3r4"
//        const val secretKey = "pjUHVmf1FMlJZBfHAIJXvb9xvs_YOQmv9CdpLzkKoVI"
//        const val link = "https://api.unsplash.com/search/photos"
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        progress.visibility = View.INVISIBLE
//        mMyBroadcastReceiver = MyBroadcastReceiver()
        val intentFilter = IntentFilter("loaded")
//        registerReceiver(mMyBroadcastReceiver, intentFilter)
        AsyncTaskImpl(this).execute("house")
//        permissions()
    }


//    private fun permissions() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
//                != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
//                != PackageManager.PERMISSION_GRANTED
//
//        ) {
//            ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS),
//                    myRequestId
//            )
//        } else {
//        createRecyclerView()
//        }
//    }


    private fun createRecyclerView() {
        val viewManager = LinearLayoutManager(this)
        myRecyclerView.apply {
            layoutManager = viewManager
            val myAdapter = UserAdapter(pictures) {
                startActivity(
                    Intent(this@MainActivity, FullScreenPicture::class.java).putExtra(
                        "link",
                        it.highQ
                    )
                )
            }
            adapter = myAdapter
//            adapter = pictures?.let {
//                    list ->
//                UserAdapter(list) {
//                    Intent(this@MainActivity, FullScreenPicture::class.java).putExtra(
//                        "link",
//                        it.highQ
//                    )
//                }
//                        u ->
//                    val intentService = Intent(this@MainActivity, ImagesService::class.java)
//                    progress.visibility = View.INVISIBLE
////                    print("fffffffffffffffffffffff")
//                    intentService.putExtras(Bundle().apply {
//                        putString("link", u.highQ)
//                    })
//                    startService(intentService)
//                }
        }
//            setHasFixedSize(true)
    }
//    }

    fun next(result: List<User>) {
        pictures = result
//        progress.visibility = View.VISIBLE
        createRecyclerView()
    }
}
//    inner class MyBroadcastReceiver : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
////            loaded = true
////            progress.visibility = View.GONE
//
////            if (intent!!.getStringExtra("progress") == "ready") {
////                hi_res.background = BitmapDrawable(
////                    resources,
////                    (openFileInput("temp.txt").use { BitmapFactory.decodeStream(it) })
////                )
////            hi_res.setImageBitmap(openFileInput("temp.txt").use { BitmapFactory.decodeStream(it) })
//            }
//        }
//    }
//}

    class ImagesService(name: String = "") : IntentService(name) {

        override fun onHandleIntent(intent: Intent?) {
            val url = intent?.extras!!.getString("link")
            val a = BitmapFactory.decodeStream(URL(url).openConnection().getInputStream())
            val fileOutput = openFileOutput("temp.txt", MODE_PRIVATE)
            fileOutput.use {
                a.compress(Bitmap.CompressFormat.PNG, 100, fileOutput)
                fileOutput.flush()
            }
            val responseIntent = Intent("loaded")
            responseIntent.putExtra("progress", "ready")
            sendBroadcast(responseIntent)
        }

    }
//    @SuppressLint("SetTextI18n")
//    fun searching(view: View) {
//        finding.visibility = View.VISIBLE
//        finding.setText("dd")
//    }
//
//    fun find(view: View) {
//        finding.visibility = View.INVISIBLE
//    }


//            fun onKey(
//                v: View?,
//                keyCode: Int,
//                event: KeyEvent
//            ): Boolean {
//                if (event.action == KeyEvent.ACTION_DOWN &&
//                    keyCode == KeyEvent.KEYCODE_ENTER
//                ) {
//                    return true
//                }
//                return false
//            }
//        })