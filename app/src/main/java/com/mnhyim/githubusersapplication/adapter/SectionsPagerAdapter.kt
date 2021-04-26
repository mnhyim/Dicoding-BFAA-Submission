package com.mnhyim.githubusersapplication.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mnhyim.githubusersapplication.view.fragments.FollowersFragment
import com.mnhyim.githubusersapplication.view.fragments.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity, private val username: Bundle) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> FollowersFragment()
            1 -> FollowingFragment()
            else -> FollowersFragment()
        }
        fragment.arguments = username

        return fragment
    }
}