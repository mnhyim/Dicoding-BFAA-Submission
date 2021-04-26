package com.mnhyim.githubusersapplication.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mnhyim.githubusersapplication.adapter.FavoritesAdapter
import com.mnhyim.githubusersapplication.databinding.ActivityFavoriteBinding
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.mnhyim.githubusersapplication.db.MappingHelper
import com.mnhyim.githubusersapplication.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoritesActivity : AppCompatActivity() {
    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var favoriteBinding: ActivityFavoriteBinding
    private var TAG: String = FavoritesActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(favoriteBinding.root)

        setRecyclerView()
        loadFavoritesAsync()

        favoritesAdapter.setOnItemClickCallback(object : FavoritesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val detailIntent = Intent(this@FavoritesActivity, DetailActivity::class.java)
                detailIntent.putExtra(MainActivity.EXTRA_USERNAME, data.login)
                startActivity(detailIntent)
            }
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onRestart() {
        super.onRestart()
        loadFavoritesAsync()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setRecyclerView() {
        favoriteBinding.rvFavorites.layoutManager = LinearLayoutManager(this)
        favoriteBinding.rvFavorites.setHasFixedSize(true)
        favoritesAdapter = FavoritesAdapter(this)
        favoriteBinding.rvFavorites.adapter = favoritesAdapter
    }

    private fun loadFavoritesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            Log.d(TAG, "loadFavoritesAsync: URI = $CONTENT_URI")
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favorites = deferredFavorites.await()
            Log.d(TAG, "loadFavoritesAsync: favorites = $favorites")
            if (favorites.size > 0) {
                favoritesAdapter.mData = favorites
                favoriteBinding.tvFavoritesInfo.visibility = View.GONE
                favoriteBinding.imgFavoritesInfo.visibility = View.GONE
            } else {
                favoriteBinding.tvFavoritesInfo.visibility = View.VISIBLE
                favoriteBinding.imgFavoritesInfo.visibility = View.VISIBLE
                favoritesAdapter.mData = ArrayList()
            }
        }
    }
}