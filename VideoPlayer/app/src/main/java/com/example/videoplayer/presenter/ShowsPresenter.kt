package com.example.videoplayer.presenter

import com.example.videoplayer.contract.ShowsContract
import com.example.videoplayer.model.Shows
import com.example.videoplayer.service.ShowsModel

class ShowsPresenter(var showsView: ShowsContract.View?) : ShowsContract.Presenter, ShowsContract.Model.OnFinishedListener {
    private val showsModel: ShowsContract.Model = ShowsModel()

    override fun getShowsFromJSON() {
        // Starting to get data from json
//        showsView?.startLoading()
        showsModel.getShows(this)
    }

    override fun onShowClick(adTag: String, url: String) {
        showsView?.changeShow(adTag, url)
    }

    override fun onFinished(shows: Shows) {
        // Done getting data from json
        showsView?.setShowsRecyclerView(shows)
//        showsView?.stopLoading()
    }

    override fun onFailure(t: Throwable) {
        showsView?.onResponseFailure(t)
//        showsView?.stopLoading()
    }

    override fun onDestroy() {
        showsView = null
    }
}