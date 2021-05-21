package com.example.videoplayer.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videoplayer.R
import com.example.videoplayer.presenter.ShowsPresenter
import kotlinx.android.synthetic.main.category_card.view.*

class CategoriesRecyclerViewAdapter (private val shows : Shows, private val showsPresenter: ShowsPresenter) : RecyclerView.Adapter<CategoriesRecyclerViewAdapter.ViewHolder>(){
    private val viewPool = RecyclerView.RecycledViewPool()
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_card, parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return shows.categoriesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        val category = shows.categoriesList[position]
        holder.textView.categoryTitleTextView.text = category
        val childLayoutManager = LinearLayoutManager(holder.recyclerView.context, RecyclerView.HORIZONTAL, false)
        childLayoutManager.initialPrefetchItemCount = 7
        holder.recyclerView.apply {
            layoutManager = childLayoutManager
            adapter = ShowsInCategoryRecyclerAdapter(shows.getShowListInCategory(category), shows.adTag, showsPresenter)
            setRecycledViewPool(viewPool)
        }
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val recyclerView : RecyclerView = itemView.showsInCategoryRecyclerView
        val textView: TextView = itemView.categoryTitleTextView
    }
}