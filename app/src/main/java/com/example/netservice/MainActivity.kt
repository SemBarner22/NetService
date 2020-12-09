package com.example.netservice

import android.content.*
import android.content.res.Configuration
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            progress.visibility = View.INVISIBLE
            bindService(imagesIntent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    private var isDownloaded = false
    var pictures: ArrayList<User>? = null

    private var mBound: Boolean = false
    private var imagesIntent: Intent? = null
    private lateinit var myAdapter: UserAdapter
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            mBound = true
            val binder = service as ImagesService.ImagesBinder
            pictures = binder.getService().pictures
            pictures?.let { p ->
                isDownloaded = true
                myAdapter.apply {
                    this.pictures = p
                    notifyDataSetChanged()
                }
            }
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imagesIntent = Intent(applicationContext, ImagesService::class.java)
        setContentView(R.layout.activity_main)
        Log.e("start", "activity")
    }

    override fun onResume() {
        super.onResume()
        if (isDownloaded) {
            bindService(imagesIntent, connection, Context.BIND_AUTO_CREATE)
        }
        registerReceiver(broadcastReceiver, IntentFilter("images"))
        if (!isDownloaded) {
            Log.e("service", "service started")
            startService(imagesIntent)
        }
        val orientation = resources.configuration.orientation
        var viewManager = GridLayoutManager(this@MainActivity, 2)
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewManager = GridLayoutManager(this@MainActivity, 3)
        }
        myAdapter = UserAdapter(ArrayList(), this@MainActivity) { user ->
//            val string = user.highQ?.substring(0)
            //                    var bundle = Bundle()
            //                    bundle.putParcelable("user", user)
            val intent = Intent(this@MainActivity, FullScreenActivity::class.java).apply {
                putExtra("link", user.highQ)
                //                        putExtras(bundle)
                //                        putExtra("bundle", bundle)
            }
            startActivity(intent)
        }
        myRecyclerView.apply {
            layoutManager = viewManager
            adapter = myAdapter
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
        imagesIntent = savedInstanceState.getParcelable("intent")
        isDownloaded = savedInstanceState.getBoolean("isDownloaded")
        mBound = savedInstanceState.getBoolean("mBound")
        if (isDownloaded) {
            progress.visibility = View.INVISIBLE
            bindService(imagesIntent, connection, Context.BIND_AUTO_CREATE)
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isDownloaded", isDownloaded)
        outState.putBoolean("mBound", mBound)
        outState.putParcelable("intent", imagesIntent)
    }
}
