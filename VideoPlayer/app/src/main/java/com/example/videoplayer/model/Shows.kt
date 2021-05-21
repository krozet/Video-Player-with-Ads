package com.example.videoplayer.model

class Shows(val adTag: String, val showList: List<Show>) {
    private var showsByCategoryHashMap = hashMapOf<String, MutableList<Show>>()
    var categoriesList = ArrayList<String>()

    init {
        // sort shows into hashmap of lists based on category
        for(show in showList) {
            if (!showsByCategoryHashMap.containsKey(show.category)) {
                showsByCategoryHashMap[show.category] = mutableListOf()
                categoriesList.add(show.category)
            }
            showsByCategoryHashMap[show.category]?.add(show)
        }
    }

    fun getFirstShow() : String {
        return getShowListInCategory(categoriesList[0])[0].video
    }

    fun getShowListInCategory(category: String): MutableList<Show> {
        return showsByCategoryHashMap[category]!!
    }
}

data class Show(val id: Int, val category: String, val title: String,
                val description: String, val video: String, val thumbnail: String)