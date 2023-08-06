package com.example.diagnalcloneapp.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.diagnalcloneapp.model.PosterItemDTO
import com.example.diagnalcloneapp.R
import com.example.diagnalcloneapp.core.Utils
import com.example.diagnalcloneapp.databinding.ListItemBinding

class PosterListingAdapter(
    private val list: ArrayList<PosterItemDTO.Page.ContentItems.Content> = arrayListOf()) :

    RecyclerView.Adapter<PosterListingAdapter.PosterViewHolder>() {
    private lateinit var  binding : ListItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding=DataBindingUtil.inflate(
            layoutInflater,
            R.layout.list_item,
            parent,
            false
        )
        return PosterViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    fun addData(data: List<PosterItemDTO.Page.ContentItems.Content>) {
        data.forEach {
            list.add(it)
        }
        notifyItemRangeInserted(list.size, data.size)
    }

    fun clearAllData() {
        list.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PosterViewHolder, position: Int) {
        val item = list[position]
        return holder.bind(item)
    }

    inner class PosterViewHolder(
        val binding: ListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PosterItemDTO.Page.ContentItems.Content) {
            if(position==0|| position==1|| position==2){
                binding.root.setPadding(0, 0,0, 0)
            }else{
                binding.root.setPadding(0, Utils.dpToPx(binding.root.context, 40),0,0)
            }
            Glide.with(binding.root.context).load(
                getImage(item.posterImage)).into(binding.ivPoster)
            binding.tvPoster.text=item.name
        }
    }

    fun getImage(imageName: String?): Drawable {
        var  img :Drawable?=null
        when(imageName){
            "poster1.jpg"->{
                img=binding.root.context.resources.getDrawable(R.drawable.poster1)
            }
            "poster2.jpg"->{
                img=binding.root.context.resources.getDrawable(R.drawable.poster2)
            }
            "poster3.jpg"->{
                img=binding.root.context.resources.getDrawable(R.drawable.poster3)
            }
            "poster4.jpg"->{
                img=binding.root.context.resources.getDrawable(R.drawable.poster4)
            }
            "poster5.jpg"->{
                img=binding.root.context.resources.getDrawable(R.drawable.poster5)
            } "poster6.jpg"->{
                img=binding.root.context.resources.getDrawable(R.drawable.poster6)
            }
            "poster7.jpg"->{
                img=binding.root.context.resources.getDrawable(R.drawable.poster7)
            }
            "poster8.jpg"->{
                img=binding.root.context.resources.getDrawable(R.drawable.poster8)
            }
            "poster9.jpg"->{
                img=binding.root.context.resources.getDrawable(R.drawable.poster9)
            }
            else->{
                img=binding.root.context.resources.getDrawable(R.drawable.placeholder_for_missing_posters)
            }
        }
        return img!!
    }



}