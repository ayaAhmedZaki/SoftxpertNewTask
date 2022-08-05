package com.example.softxpertnewtask.search

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.example.softxpertnewtask.R
import com.example.softxpertnewtask.databinding.MovieItemBinding
import com.example.softxpertnewtask.shared.SharedConstants
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SearchResultAdapter(val onItemClicked: (Result) -> Unit): RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    private var data: List<Result> = emptyList()

    fun setData(movieList: List<Result>) {
        this.data = movieList
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(moviesDataItem: Result) {
            val bind = MovieItemBinding.bind(itemView)
            itemView.setOnClickListener {
                onItemClicked(moviesDataItem)
            }
            bind.apply {
                movieName.text = moviesDataItem.title


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val date =  LocalDate.parse(moviesDataItem.release_date
                        , DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    year.text = date.year.toString()
                } else {
                    year.text = moviesDataItem.release_date
                }

                Glide.with(movieImg)
                    .asBitmap()
                    .load("${SharedConstants.IMAGE_URL}${moviesDataItem.poster_path}")
                    .placeholder(R.color.md_grey_300)
                    .error(R.color.md_grey_300)
                    .into(BitmapImageViewTarget(movieImg))
            }
        }

    }




}