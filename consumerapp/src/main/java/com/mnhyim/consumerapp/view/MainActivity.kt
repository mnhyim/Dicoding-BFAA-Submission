package com.mnhyim.consumerapp

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mnhyim.consumerapp.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.mnhyim.consumerapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var TAG: String = MainActivity::class.java.simpleName

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var favoritesAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        setRecyclerView()
        loadFavoritesAsync()

        if (checkEmpty()) {
            activityMainBinding.tvFavoritesInfo.visibility = View.VISIBLE
            activityMainBinding.imgFavoritesInfo.visibility = View.VISIBLE
        } else {
            activityMainBinding.tvFavoritesInfo.visibility = View.GONE
            activityMainBinding.imgFavoritesInfo.visibility = View.GONE
        }

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadFavoritesAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
    }

    private fun setRecyclerView() {
        favoritesAdapter = UserAdapter(this)
        favoritesAdapter.notifyDataSetChanged()
        activityMainBinding.rvFavorites.layoutManager = LinearLayoutManager(this)
        activityMainBinding.rvFavorites.adapter = favoritesAdapter
    }

    private fun loadFavoritesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favorites = deferredFavorites.await()
            Log.d(TAG, "loadFavoritesAsync: $favorites")
            if (favorites.size > 0) {
                favoritesAdapter.setData(favorites)
                activityMainBinding.tvFavoritesInfo.visibility = View.GONE
                activityMainBinding.imgFavoritesInfo.visibility = View.GONE
            } else {
                activityMainBinding.tvFavoritesInfo.visibility = View.VISIBLE
                activityMainBinding.imgFavoritesInfo.visibility = View.VISIBLE
                favoritesAdapter.setData(ArrayList())
            }
        }
    }

    private fun checkEmpty(): Boolean {
        val cont = MappingHelper.mapCursorToArrayList(
            contentResolver.query(
                CONTENT_URI,
                null,
                null,
                null,
                null
            )
        ).size
        return cont <= 0
    }
}