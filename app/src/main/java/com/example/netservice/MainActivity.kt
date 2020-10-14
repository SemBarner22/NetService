package com.example.netservice

import android.content.*
import android.content.res.Configuration
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            isDownloaded = true
            progress.visibility = View.INVISIBLE
            bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    private var isDownloaded = false
    var pictures: ArrayList<User>? = null

    private var mBound: Boolean = false
    private var serviceIntent: Intent? = null
    private lateinit var myAdapter: UserAdapter
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            mBound = true
            val binder = service as ImagesService.LocalBinder
            pictures = binder.getPictures()
            pictures?.let { p ->
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
        serviceIntent = Intent(applicationContext, ImagesService::class.java)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        if (isDownloaded) {
            bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (mBound) unbindService(connection)
    }


    override fun onStart() {
        super.onStart()
        startService(serviceIntent)
        registerReceiver(broadcastReceiver, IntentFilter("images"))
        val orientation = resources.configuration.orientation
        var viewManager = GridLayoutManager(this@MainActivity, 2)
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewManager = GridLayoutManager(this@MainActivity, 3)
        }
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
        myRecyclerView.apply {
            layoutManager = viewManager
            adapter = myAdapter
        }
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        serviceIntent = savedInstanceState.getParcelable("intent")
        isDownloaded = savedInstanceState.getBoolean("isDownloaded")
        mBound = savedInstanceState.getBoolean("mBound")
        if (isDownloaded) {
            progress.visibility = View.INVISIBLE
            bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isDownloaded", isDownloaded)
        outState.putBoolean("mBound", mBound)
        outState.putParcelable("intent", serviceIntent)
    }
}
