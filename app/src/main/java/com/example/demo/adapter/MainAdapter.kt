package com.example.demo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.demo.home.MainActivity
import com.example.demo.databinding.AdapterPhotosBinding
import com.example.demo.model.Photos
import com.bumptech.glide.load.model.LazyHeaders

import com.bumptech.glide.load.model.GlideUrl
import com.example.demo.listeners.ItemClickListeners


class MainAdapter(private val context: MainActivity, private var photos: List<Photos>,private val itemClickListeners: ItemClickListeners) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Photos>() {
        override fun areItemsTheSame(oldItem: Photos, newItem: Photos): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Photos, newItem: Photos): Boolean {
            return oldItem == newItem

        }
    }

    val differ = AsyncListDiffer(this, differCallback)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            AdapterPhotosBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PhotosHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as PhotosHolder).bind(differ.currentList[holder.adapterPosition])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    inner class PhotosHolder(private val view: AdapterPhotosBinding) :
        RecyclerView.ViewHolder(view.root) {



        init {
            view.imgGoToDetailId.setOnClickListener {
                itemClickListeners.onItemClick(adapterPosition)
            }
        }
        fun bind(photos: Photos) {

            view.txtTitleId.text = photos.title
            view.txtAlbumId.text = photos.id.toString()

            val url = GlideUrl(
                photos.url, LazyHeaders.Builder()
                    .addHeader("User-Agent", "your-user-agent")
                    .build()
            )
            Glide.with(context)
                .load(url) // image url
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(view.imgPhotoId);


        }


    }
}