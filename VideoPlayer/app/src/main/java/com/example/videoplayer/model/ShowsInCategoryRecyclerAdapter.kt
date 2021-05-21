package com.example.videoplayer.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.videoplayer.R
import com.example.videoplayer.presenter.ShowsPresenter
import kotlinx.android.synthetic.main.show_in_category.view.*


class ShowsInCategoryRecyclerAdapter (private val showList : List<Show>, private val adTag: String, private val showsPresenter: ShowsPresenter)
    : RecyclerView.Adapter<ShowsInCategoryRecyclerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {

        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.show_in_category,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return showList.size
    }

    // TODO handle displaying the thumbnails differently, wouldn't use Glide here
    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        val show = showList[position]
        Glide.with(holder.imageView.context)
            .load(show.thumbnail)
            .into(holder.imageView)
        holder.imageView.setOnClickListener {
            showsPresenter.onShowClick(adTag, show.video)
        }
    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val imageView: ImageView = itemView.showImageView
    }
}