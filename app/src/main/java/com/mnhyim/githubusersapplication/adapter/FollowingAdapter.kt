package com.mnhyim.githubusersapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.mnhyim.githubusersapplication.R
import com.mnhyim.githubusersapplication.databinding.ListItemUserBinding
import com.mnhyim.githubusersapplication.model.User

class FollowingAdapter : RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>() {
    private val mData = ArrayList<User>()

    inner class FollowingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListItemUserBinding.bind(itemView)
        fun bind(user: User) {
            with(itemView) {
                binding.tvItemName.text = user.login
                binding.tvItemType.text = user.type
                com.bumptech.glide.Glide.with(itemView.context)
                    .load(user.avatar_url)
                    .apply(RequestOptions().override(72, 72))
                    .into(binding.imgItemAvatar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        val mView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_user, parent, false)
        return FollowingViewHolder(mView)
    }

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        holder.bind(mData[position])

    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }
}