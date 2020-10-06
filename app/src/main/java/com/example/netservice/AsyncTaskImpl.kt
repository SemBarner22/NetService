package com.example.netservice

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import org.json.JSONArray
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

class AsyncTaskImpl(activity: MainActivity) : AsyncTask<String, Void, MutableList<User>?>() {
    private val activityRef = WeakReference(activity)
    private var pictures: MutableList<User>? = emptyList<User>().toMutableList()
//    "https://api.unsplash.com/search/photos?query=slave&client_id=RN2wHFLxJnmhFf-ZxhdoYO_WVKHAE1hq66CUbs3S3r4"
    override fun doInBackground(vararg p0: String?): MutableList<User>? {
        val c = URL("https://api.unsplash.com/photos/random?count=30&client_id=857bc8e8cbd3f72ecc4c7338bd61543ad0120c2610f415aa671ead06a468c057")
            .openConnection() as HttpURLConnection
        val json = JSONArray(c.inputStream.use { it.reader().readText() })
        var i = 0
        while (i != json.length()) {
            val it = json[i] as JSONObject
            pictures?.add(
                User(
                    it.get("alt_description").toString(),
                    URL(
                        (it.get("urls") as JSONObject)
                            .get("thumb").toString()
                    ).openConnection().getInputStream().use { BitmapFactory.decodeStream(it) },
                    (it.get("urls") as JSONObject).get("full").toString()
                )
            )
            ++i
        }
        return pictures
    }

    override fun onPostExecute(result: MutableList<User>?) {
        val activity = activityRef.get()
        result?.let { activity?.next(it) }
    }
}