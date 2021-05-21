package com.example.videoplayer.contract

import android.util.Log
import com.example.videoplayer.model.Shows

public interface ShowsContract {

    interface Model {
        // Would be used in case of making an API and getting a response
        interface OnFinishedListener {
            fun onFinished(shows: Shows)
            fun onFailure(t: Throwable)
        }

        fun getShows(onFinishedListener: OnFinishedListener)
    }

    interface View {
        // Used to denote when to show a loading indicator while pulling data
        fun startLoading()
        fun stopLoading()
        fun setShowsRecyclerView(shows: Shows)
        fun onResponseFailure(t: Throwable)
        fun changeShow(adTag: String, url: String)
    }

    interface Presenter {
        fun onDestroy()
        fun getShowsFromJSON()
        fun onShowClick(adTag: String, url: String)
    }
}