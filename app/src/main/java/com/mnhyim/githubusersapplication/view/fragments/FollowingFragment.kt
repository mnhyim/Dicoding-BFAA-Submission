package com.mnhyim.githubusersapplication.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mnhyim.githubusersapplication.adapter.FollowingAdapter
import com.mnhyim.githubusersapplication.databinding.FragmentFollowingBinding
import com.mnhyim.githubusersapplication.view.activities.DetailActivity
import com.mnhyim.githubusersapplication.viewmodel.FollowingViewModel

class FollowingFragment : Fragment() {
    private lateinit var followingBinding: FragmentFollowingBinding
    private lateinit var followingAdapter: FollowingAdapter
    private lateinit var followingViewModel: FollowingViewModel

    companion object {
        private const val ARG_USERNAME = "username"

        fun newInstance(username: String): FollowingFragment {
            val fragment = FollowingFragment()

            val bundle = Bundle().apply {
                putString(ARG_USERNAME, username)
            }

            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        followingBinding = FragmentFollowingBinding.inflate(layoutInflater)
        return followingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()

        followingViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowingViewModel::class.java)
        followingViewModel.setFollowing(
            requireArguments().getString(DetailActivity.EXTRA_USERNAME).toString()
        )
        followingViewModel.getUsers().observe(viewLifecycleOwner, { user ->
            if (user != null) {
                showLoading(false)
                followingAdapter.setData(user)
            }
        })
        followingViewModel.getError().observe(viewLifecycleOwner, { e ->
            if (e != null) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
                showLoading(false)
            }
        })
    }

    private fun setRecyclerView() {
        followingAdapter = FollowingAdapter()
        followingAdapter.notifyDataSetChanged()
        followingBinding.rvFollowing.layoutManager = LinearLayoutManager(context)
        followingBinding.rvFollowing.adapter = followingAdapter
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            followingBinding.progressBar.visibility = View.VISIBLE
        } else {
            followingBinding.progressBar.visibility = View.GONE
        }
    }
}