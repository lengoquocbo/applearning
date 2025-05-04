package com.example.apphoctap.view.student.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apphoctap.databinding.ItemBannerBinding



class BannerAdapter(private val bannerList: List<Int>) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    class BannerViewHolder(val binding: ItemBannerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val imageUrl = bannerList[position]

        // Chuyển 180dp, 380dp -> pixel
        val density = holder.itemView.context.resources.displayMetrics.density
        val sizeheighPx = (200 * density).toInt()
        val sizewidthPx = (380 * density).toInt()

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .override(sizewidthPx, sizeheighPx) // resize về 180dp x 180dp
            .fitCenter()  // hoặc .circleCrop() nếu là avatar
            .into(holder.binding.bannerImage)
    }

    override fun getItemCount(): Int = bannerList.size
}
