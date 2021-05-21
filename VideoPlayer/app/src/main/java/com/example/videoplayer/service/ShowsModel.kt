package com.example.videoplayer.service

import android.util.Log
import com.example.videoplayer.contract.ShowsContract
import com.example.videoplayer.model.Show
import com.example.videoplayer.model.Shows
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

// This class is used to create the Shows model
class ShowsModel : ShowsContract.Model {

    override fun getShows(onFinishedListener: ShowsContract.Model.OnFinishedListener) {
        val url = "http://a.jsrdn.com/test/interview.json";
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                response.body?.let {
                    val json = JSONObject(it.string())
                    val adTag = getAdTagFromJSON(json)
                    val showList = getShowListFromJSON(json)
                    val shows = Shows(adTag, showList)

                    onFinishedListener.onFinished(shows)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("RESPONSE", "Failed: $e")
                onFinishedListener.onFailure(e)
            }
        })
    }

    fun getAdTagFromJSON(json: JSONObject): String {
        return json.getString("adtag")
    }

    fun getShowListFromJSON(json: JSONObject) : List<Show> {
        val showList: ArrayList<Show> = ArrayList()

        try {
            val showsArray = json.getJSONArray("shows")

            // Loop to get shows
            for (i in 0 until showsArray.length()) {
                val showJSONObj = showsArray.getJSONObject(i)
                val id = showJSONObj.getInt("id")
                val category = showJSONObj.getString("category")
                val title = showJSONObj.getString("title")
                val description = showJSONObj.getString("description")

                // create a object for getting content data from showJSONObj
                val content = showJSONObj.getJSONObject("content")
                val video = content.getString("video")
                val thumbnail = content.getString("thumbnail")

                val show = Show(id, category, title, description, video, thumbnail)
                showList.add(show)
            }
        } catch (e: JSONException) {
            //exception
            e.printStackTrace()
        }
        return showList
    }
}