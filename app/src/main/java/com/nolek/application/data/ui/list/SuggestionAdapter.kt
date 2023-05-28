package com.nolek.application.data.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nolek.application.R

class SuggestionAdapter(var currentSearch: String, var suggestions: List<String>) :
    RecyclerView.Adapter<SuggestionAdapter.ViewHolder>(),
    Filterable {
    var SuggestionFilteredList = ArrayList(suggestions)

    var onItemClick: ((text: String) -> Unit)? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var image: ImageView
        lateinit var text: TextView
        lateinit var data: String
        fun SetData(data: String, displayTitle: String, imageId: Int) {
            this.data = data
            image = itemView.findViewById(R.id.suggestion_icon)
            text = itemView.findViewById(R.id.suggestion_title)

            image.setImageResource(imageId)
            text.text = displayTitle

            itemView.setOnClickListener {
                onItemClick?.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.suggestion_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        val isCurrentSearchEmpty = if (currentSearch.isEmpty()) 0 else 1
        return SuggestionFilteredList.size + isCurrentSearchEmpty
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var pos = position
        if (!currentSearch.isEmpty()) {
            pos--
            if (position == 0) {
                holder.SetData(
                    currentSearch,
                    "Search for \"$currentSearch\" in index",
                    R.drawable.baseline_manage_search_24
                )
                return
            }
        }
        holder.SetData(
            SuggestionFilteredList[pos],
            SuggestionFilteredList[pos],
            R.drawable.baseline_schedule_24
        )
    }

    override fun getFilter(): Filter {
        return suggestionsFilter
    }

    private val suggestionsFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<String> = ArrayList()
            if (constraint.isEmpty()) {
                filteredList.addAll(suggestions)
            } else {
                val predicate = constraint.toString().trim()
/*                if(!currentSearch.contains(predicate, ignoreCase = true))
                    currentSearch = ""*/

                for (item in suggestions) {
                    if (item.contains(predicate, ignoreCase = true)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            SuggestionFilteredList.clear()
            SuggestionFilteredList.addAll(results.values as Collection<String>)
            notifyDataSetChanged()
        }
    }
}