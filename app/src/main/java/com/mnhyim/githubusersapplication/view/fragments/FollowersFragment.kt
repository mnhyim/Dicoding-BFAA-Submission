package com.mnhyim.githubusersapplication.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mnhyim.githubusersapplication.adapter.FollowersAdapter
import com.mnhyim.githubusersapplication.databinding.FragmentFollowersBinding
import com.mnhyim.githubusersapplication.view.activities.DetailActivity
import com.mnhyim.githubusersapplication.viewmodel.FollowersViewModel

class FollowersFragment : Fragment() {
    private lateinit var followersBinding: FragmentFollowersBinding
    private lateinit var followersAdapter: FollowersAdapter
    private lateinit var followersViewModel: FollowersViewModel

    companion object {
        private const val ARG_USERNAME = "username"

        fun newInstance(username: String): FollowersFragment {
            val fragment = FollowersFragment()

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
        followersBinding = FragmentFollowersBinding.inflate(layoutInflater)
        return followersBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()

        followersViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowersViewModel::class.java)

        followersViewModel.setFollowers(
            requireArguments().getString(DetailActivity.EXTRA_USERNAME).toString()
        )

        followersViewModel.getUsers().observe(viewLifecycleOwner, { user ->
            if (user != null) {
                showLoading(false)
                followersAdapter.setData(user)
            }
        })

        followersViewModel.getError().observe(viewLifecycleOwner, { e ->
            if (e != null) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
                showLoading(false)
            }
        })
    }

    private fun setRecyclerView() {
        followersAdapter = FollowersAdapter()
        followersAdapter.notifyDataSetChanged()
        followersBinding.rvFollowers.layoutManager = LinearLayoutManager(context)
        followersBinding.rvFollowers.adapter = followersAdapter
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            followersBinding.progressBar.visibility = View.VISIBLE
        } else {
            followersBinding.progressBar.visibility = View.GONE
        }
    }
}
