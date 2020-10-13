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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.lang.ref.WeakReference
import java.net.URL


class MainActivity : AppCompatActivity() {
    //    private var lastRequest: String? = ""
    var pictures: ArrayList<User>? = null
    var a: AsyncTaskImpl? = null

//    private lateinit var mMyBroadcastReceiver: MyBroadcastReceiver
//    companion object {
//        const val accessKey = "RN2wHFLxJnmhFf-ZxhdoYO_WVKHAE1hq66CUbs3S3r4"
//        const val secretKey = "pjUHVmf1FMlJZBfHAIJXvb9xvs_YOQmv9CdpLzkKoVI"
//        const val link = "https://api.unspl
//        ash.com/search/photos"
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        progress.visibility = View.INVISIBLE
//        mMyBroadcastReceiver = MyBroadcastReceiver()
        val intentFilter = IntentFilter("loaded")
//        registerReceiver(mMyBroadcastReceiver, intentFilter)
        a = (lastCustomNonConfigurationInstance as? AsyncTaskImpl) ?: AsyncTaskImpl(this)
//        a?.attachActivity(this)
        method()
//        permissions()
    }

    private fun method() {
        a?.activityRef = WeakReference(null)
        a = AsyncTaskImpl(this).apply {
            execute("house")
            attachActivity(this@MainActivity)
        }
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

    override fun onRetainCustomNonConfigurationInstance(): Any? = a

    private fun createRecyclerView() {
        val viewManager = LinearLayoutManager(this)
        myRecyclerView.apply {
            layoutManager = viewManager
            val myAdapter = pictures?.let {
                UserAdapter(it, applicationContext) { user ->
                    val string = user.highQ?.substring(0)
                    var bundle = Bundle()
                    bundle.putParcelable("user", user)
                    val intent = Intent(this@MainActivity, FullScreenPicture::class.java).apply {
//                        putExtra("link", string)
                        putExtras(bundle)
//                        putExtra("bundle", bundle)
                    }
                    startActivity(intent)
                }
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

//    fun next(result: ArrayList<User>) {
//        pictures = result
////        progress.visibility = View.VISIBLE
//        createRecyclerView()
//    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        pictures = savedInstanceState.getParcelableArrayList("items")
//        method()
        if (pictures != null)
            createRecyclerView()
    }

    //
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("items", pictures)
    }

    fun setMoviesInUI(curList: ArrayList<User>?) {
//        if (curList != null) {
//            next(curList)
//        }
        a = null
        pictures = curList
        createRecyclerView()
    }
//    override fun onResume() {
//        super.onResume()
//        if (pictures == null) AsyncTaskImpl(this).execute("house")
//    }

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

//class ImagesService(name: String = "") : IntentService(name) {
//
//    init {
//        setIntentRedelivery(true)
//    }
//
//    override fun onHandleIntent(intent: Intent?) {
////        val url = intent?.extras!!.getString("link")
////        val a = BitmapFactory.decodeStream(URL(url).openConnection().getInputStream())
////        val fileOutput = openFileOutput("temp.txt", MODE_PRIVATE)
////        fileOutput.use {
////            a.compress(Bitmap.CompressFormat.PNG, 100, fileOutput)
////            fileOutput.flush()
////        }
////        val responseIntent = Intent("loaded")
////        responseIntent.putExtra("progress", "ready")
////        sendBroadcast(responseIntent)
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
//    }
//
//}

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