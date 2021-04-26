package com.mnhyim.consumerapp

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mnhyim.consumerapp.databinding.ListItemUserBinding

class UserAdapter(private val activity: Activity) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    var mData = ArrayList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val mView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_user, parent, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListItemUserBinding.bind(itemView)
        fun bind(user: User) {
            with(itemView) {
                binding.tvItemName.text = user.login
                binding.tvItemType.text = user.type
                Glide.with(itemView.context)
                    .load(user.avatar_url)
                    .apply(RequestOptions().override(72, 72))
                    .into(binding.imgItemAvatar)
            }
        }
    }

    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }
}