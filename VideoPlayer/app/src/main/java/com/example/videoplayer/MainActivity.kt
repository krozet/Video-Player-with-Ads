package com.example.videoplayer

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videoplayer.contract.ShowsContract
import com.example.videoplayer.model.CategoriesRecyclerViewAdapter
import com.example.videoplayer.model.Shows
import com.example.videoplayer.presenter.ShowsPresenter
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

import kotlinx.android.synthetic.main.activity_main.*
/*
* Author: Keawa Rozet
*
* Thank you for taking the time to review my Video Player app
*
* This was my first time utilizing ads and a video player in Android.
* I was very grateful for this opportunity to have a chance to learn while showing you what I am able to accomplish in a small period of time.
*
* Few things to note about the code:
* - This app is coded using an MVP structure
* - I am displaying the shows through a nested recyclerview solution
* - The main technical algorithm of note can be found in the Shows.kt inside of init
*   where I take the parsed json object (now turned into a Shows object which contains a list of shows)
*   and I iterate through the list and place them into a Hashmap of <String,List> pairs to sort each show (value) into
*   their respected category (key) by only iterating through the list once
* - Currently works on Pixel 3 physical device, in the process of making it run on an emulated device
*
* */

class MainActivity : Activity(), ShowsContract.View {
    private var player: SimpleExoPlayer? = null
    private var adsLoader: ImaAdsLoader? = null
    private var showsPresenter: ShowsPresenter? = null
    private var adTag: String = ""
    private var showURL: String = ""
    private lateinit var recyclerView: RecyclerView
    private var videoLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Removing night mode for now, could create light and dark themes if needed
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        showsPresenter = ShowsPresenter(this)
        showsPresenter?.getShowsFromJSON()

        MultiDex.install(this)
        adsLoader = ImaAdsLoader.Builder(this).build()
    }

    override fun startLoading() {
        // TODO implement some sort of loading indicator
    }

    override fun stopLoading() {
    }

    override fun setShowsRecyclerView(shows: Shows) {
        recyclerView = categoriesRecyclerView
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity,
                RecyclerView.VERTICAL, false)
            adapter = CategoriesRecyclerViewAdapter(shows, showsPresenter!!)
        }

        // Set first Show in list to play when loaded
        if (adTag.isEmpty() && showURL.isEmpty()) {
            showURL = shows.getFirstShow()
            adTag = shows.adTag
            if(!videoLoaded) {
                preparePlayer()
            }
        }
    }

    override fun onResponseFailure(t: Throwable) {
        Log.d("RESPONSE", "Failure: $t")
        // TODO implement logic to handle failure
        // perhaps attempt another call after some time if connected to internet
    }

    override fun changeShow(adTag: String, showURL: String) {
        onStop()
        this.adTag = adTag
        this.showURL = showURL
        onStart()
    }

    override fun onStart() {
        super.onStart()
        if (adTag.isNotEmpty() && showURL.isNotEmpty()) {
            playVideo()
        }
    }

    override fun onResume() {
        super.onResume()
        playVideo()
    }

    private fun playVideo() {
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer()
            if (playerView != null) {
                playerView.onResume()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pauseVideo()
    }

    override fun onStop() {
        super.onStop()
        pauseVideo()
    }

    private fun pauseVideo() {
        if (Util.SDK_INT > 23) {
            if (playerView != null) {
                playerView.onPause()
            }
            releasePlayer()
        }
    }

    override fun onDestroy() {
        adsLoader!!.release()
        super.onDestroy()
    }

    private fun releasePlayer() {
        adsLoader!!.setPlayer(null)
        playerView.player = null
        player?.release()
        player = null
    }

    private fun initAdsLoader() {
        adsLoader = ImaAdsLoader.Builder(this).build()
    }

    private fun initializePlayer() {
        // Set up the factory for media sources, passing the ads loader and ad view providers.
        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(this, Util.getUserAgent(this, getString(R.string.app_name)))
        val mediaSourceFactory: MediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory)
            .setAdsLoaderProvider { adsLoader }
            .setAdViewProvider(playerView)

        // Create a SimpleExoPlayer and set it as the player for content and ads.
        player = SimpleExoPlayer.Builder(this).setMediaSourceFactory(mediaSourceFactory).build()
        playerView.player = player
        if (adsLoader == null) initAdsLoader()
        adsLoader!!.setPlayer(player)

        if (adTag.isNotEmpty() && showURL.isNotEmpty()) {
            preparePlayer()
        } else {
            videoLoaded = false
        }
    }

    private fun preparePlayer() {
        runOnUiThread {
            // Create the MediaItem to play, specifying the content URI and ad tag URI.
            val contentUri = Uri.parse(showURL)
            val adTagUri = Uri.parse(adTag)
            val mediaItem = MediaItem.Builder().setUri(contentUri).setAdTagUri(adTagUri).build()

            // Prepare the content and ad to be played with the SimpleExoPlayer.
            player!!.setMediaItem(mediaItem)
            player!!.prepare()

            // Set PlayWhenReady. If true, content and ads will autoplay.
            player!!.playWhenReady = false
            videoLoaded = true
        }

    }
}