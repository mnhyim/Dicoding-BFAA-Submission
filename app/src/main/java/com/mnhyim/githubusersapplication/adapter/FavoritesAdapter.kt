package com.mnhyim.githubusersapplication.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mnhyim.githubusersapplication.R
import com.mnhyim.githubusersapplication.databinding.ListItemUserBinding
import com.mnhyim.githubusersapplication.model.User

class FavoritesAdapter(private val activity: Activity) :
    RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {
    var mData = ArrayList<User>()
        set(value) {
            field.clear()
            field.addAll(value)
            notifyDataSetChanged()
        }

    private var onItemClickCallback: OnItemClickCallback? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_user, parent, false)
        return FavoritesViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListItemUserBinding.bind(itemView)
        fun bind(user: User) {
            with(itemView) {
                binding.tvItemName.text = user.login
                binding.tvItemType.text = user.type
                Glide.with(itemView.context)
                    .load(user.avatar_url)
                    .apply(RequestOptions().override(72, 72))
                    .into(binding.imgItemAvatar)
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(user) }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}