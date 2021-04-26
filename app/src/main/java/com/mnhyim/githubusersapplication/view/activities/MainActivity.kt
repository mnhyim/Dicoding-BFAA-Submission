package com.mnhyim.githubusersapplication.view.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mnhyim.githubusersapplication.R
import com.mnhyim.githubusersapplication.adapter.UserAdapter
import com.mnhyim.githubusersapplication.databinding.ActivityMainBinding
import com.mnhyim.githubusersapplication.model.User
import com.mnhyim.githubusersapplication.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var userAdapter: UserAdapter
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel


    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        setRecyclerView()

        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val detailIntent = Intent(this@MainActivity, DetailActivity::class.java)
                detailIntent.putExtra(EXTRA_USERNAME, data.login)
                detailIntent.putExtra(EXTRA_ID, data.id)
                startActivity(detailIntent)
            }
        })

        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        if (mainViewModel.isEmpty()) {
            mainViewModel.setRandomUsers()
        }

        mainViewModel.getUsers().observe(this, { user ->
            if (user != null) {
                userAdapter.setData(user)
                showLoading(false)
            }
        })

        mainViewModel.getError().observe(this, { e ->
            if (e != null) {
                Toast.makeText(this@MainActivity, e.toString(), Toast.LENGTH_LONG).show()
                showLoading(false)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showLoading(true)
                userAdapter.emptyData()
                mainViewModel.setSearchUsers(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_fav -> {
                val mIntent = Intent(this@MainActivity, FavoritesActivity::class.java)
                startActivity(mIntent)
            }
            R.id.action_preferences -> {
                val mIntent = Intent(this@MainActivity, PreferencesActivity::class.java)
                startActivity(mIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setRecyclerView() {
        userAdapter = UserAdapter()
        userAdapter.notifyDataSetChanged()
        mainBinding.rvUsers.layoutManager = LinearLayoutManager(this)
        mainBinding.rvUsers.adapter = userAdapter
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            mainBinding.progressBar.visibility = View.VISIBLE
        } else {
            mainBinding.progressBar.visibility = View.GONE
        }
    }
}