package com.example.netservice

import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

class AsyncPreviewLoader(service: ImagesService) :
    AsyncTask<String, Void, ArrayList<User>?>() {
    var serviceRef = WeakReference(service)
    override fun doInBackground(vararg p0: String?): ArrayList<User>? {
        try {
            val picts: ArrayList<User> = ArrayList()
            val c =
                URL(
                    "https://api.unsplash.com/photos/random?count=30" +
                            "&client_id=857bc8e8cbd3f72ecc4c7338bd61543ad0120c2610f415aa671ead06a468c057"
                )
                    .openConnection() as HttpURLConnection
            Log.e("SC", "StartedTask")
            val json = JSONArray(c.inputStream.use { it.reader().readText() })
            var i = 0
            while (i != json.length()) {
                val it = json[i] as JSONObject
                picts.add(
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
            return picts
        } catch (e: Exception) {
            return null
        }
    }

    override fun onPostExecute(result: ArrayList<User>?) {
        serviceRef.get()?.onPostExecute(result)
    }
}