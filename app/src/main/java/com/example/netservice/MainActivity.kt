package com.example.netservice

import android.app.IntentService
import android.app.Service
import android.content.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fullscreen.*
import java.io.ByteArrayOutputStream
import java.lang.ref.WeakReference


class MainActivity : AppCompatActivity() {
    //    private var lastRequest: String? = ""
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.e("SC", "Received")
            previewsDownloaded = true
            bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
//            onListLoaded()
        }
    }

    private var previewsDownloaded = false
    var pictures: ArrayList<User>? = null

//    private lateinit var mMyBroadcastReceiver: MyBroadcastReceiver
//    companion object {
//        const val accessKey = "RN2wHFLxJnmhFf-ZxhdoYO_WVKHAE1hq66CUbs3S3r4"
//        const val secretKey = "pjUHVmf1FMlJZBfHAIJXvb9xvs_YOQmv9CdpLzkKoVI"
//        const val link = "https://api.unspl
//        ash.com/search/photos"
//    }


    //    private var myBroadCastReceiver: BroadcastReceiver? = null
//    lateinit var service: ImagesService
    private var mBound: Boolean = false
    private var serviceIntent: Intent? = null
    private lateinit var myAdapter: UserAdapter
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            mBound = true
            val binder = service as ImagesService.LocalBinder

            pictures = binder.getPictures()
//            service.getPictures()
            Log.e("SC", "onServiceConnected")
            pictures?.let { myAdapter.updateData(it) }
            Log.e("SC", "DataUpdated")
//            createRecyclerView(pictures)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    //    var localBinder: LocalBinder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        serviceIntent = Intent(applicationContext, ImagesService::class.java)
        Log.e("SC", "Started0")
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        if (previewsDownloaded) {
            bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (mBound) unbindService(connection)
    }


    override fun onStart() {
        super.onStart()
        Log.e("SC", "Started")
        startService(serviceIntent)
        registerReceiver(broadcastReceiver, IntentFilter("PreviewsDownloaded"))
        val viewManager = LinearLayoutManager(this@MainActivity)
        Log.e("SC", "Started2")
        myAdapter = UserAdapter(ArrayList(), this@MainActivity) { user ->
//            val string = user.highQ?.substring(0)
            //                    var bundle = Bundle()
            //                    bundle.putParcelable("user", user)
            val intent = Intent(this@MainActivity, FullScreenPicture::class.java).apply {
                putExtra("link", user.highQ)
                //                        putExtras(bundle)
                //                        putExtra("bundle", bundle)
            }
            startActivity(intent)
        }
        Log.e("SC", "Started3")
        myRecyclerView.apply {
            layoutManager = viewManager
            adapter = myAdapter
        }
        Log.e("SC", "Started4")
    }
//        progress.visibility = View.INVISIBLE
//        mMyBroadcastReceiver = MyBroadcastReceiver()
//        val intent = Intent(this, ImagesService::class.java)
//        val serviceIntent = Intent(this, ImagesService::class.java)
//        service = ImagesService(this)
//        localBinder = LocalBinder(service)
//        myBroadCastReceiver = object : BroadcastReceiver() {
//            override fun onReceive(context: Context?, intent: Intent?) {
//                val byteArray = intent?.getByteArrayExtra("byteArray")
//                if (byteArray != null) {
//                    val byteImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//                    picture.setImageBitmap(byteImage)
//                }
//            }
//        }

//        LocalBroadcastManager.getInstance(this)
//            .registerReceiver(myBroadCastReceiver as BroadcastReceiver,
//                IntentFilter("response").apply {
//                    addCategory(Intent.CATEGORY_DEFAULT)
//                })
//        val intentFilter = IntentFilter("loaded")
//        registerReceiver(mMyBroadcastReceiver, intentFilter)
//        permissions()


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

//    override fun onRetainCustomNonConfigurationInstance(): Any? = a

//fun createRecyclerView(curList: ArrayList<User>?) {
////        val pictures = localBinder?.getPictures()
//    val viewManager = LinearLayoutManager(this)
//    myRecyclerView.apply {
//        adapter = myAdapter
//        layoutManager = viewManager
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
//    }
//            setHasFixedSize(true)
//}
//    }

//    fun next(result: ArrayList<User>) {
//        pictures = result
////        progress.visibility = View.VISIBLE
//        createRecyclerView()
//    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        serviceIntent = savedInstanceState.getParcelable("serviceIntent")
        previewsDownloaded = savedInstanceState.getBoolean("previewsDownloaded")
        mBound = savedInstanceState.getBoolean("bound")
        if (previewsDownloaded) {
            bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
        }

//        pictures = savedInstanceState.getParcelableArrayList("items")
////        method()
//        if (pictures != null)
//            createRecyclerView()
    }

    //
//    //
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("previewsDownloaded", previewsDownloaded)
        outState.putBoolean("bound", mBound)
        outState.putParcelable("serviceIntent", serviceIntent)
    }
}

//    override fun onResume() {
//        super.onResume()
//        if (pictures == null) AsyncTaskImpl(this).execute("house")
//    }

//}
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