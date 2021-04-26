package com.mnhyim.githubusersapplication.view.activities

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mnhyim.githubusersapplication.R
import com.mnhyim.githubusersapplication.adapter.SectionsPagerAdapter
import com.mnhyim.githubusersapplication.databinding.ActivityDetailBinding
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.AVATAR_URL
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.COMPANY
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.LOCATION
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.LOGIN
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.NAME
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.PUBLIC_REPOS
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.TYPE
import com.mnhyim.githubusersapplication.db.MappingHelper
import com.mnhyim.githubusersapplication.model.User
import com.mnhyim.githubusersapplication.viewmodel.DetailViewModel


class DetailActivity : AppCompatActivity() {
    private val TAG: String = DetailActivity::class.java.simpleName

    private lateinit var detailBinding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    companion object {
        const val EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        val userName = intent.getStringExtra(EXTRA_USERNAME)

        if (checkFavorited(userName).size == 0) {
            detailBinding.fabAdd.setImageResource(R.drawable.ic_baseline_star_24)
        } else {
            detailBinding.fabAdd.setImageResource(R.drawable.ic_baseline_remove_24)
        }

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailViewModel::class.java
        )

        detailViewModel.setUsersDetail(userName)
        detailViewModel.getUsers().observe(this, { user ->
            if (user != null) {
                setDataToView(user)
                showLoading(false)
                setTabLayout(userName)
                detailBinding.fabAdd.setOnClickListener {
                    addFavorites(user)
                }
            }
        })

        detailViewModel.getError().observe(this, { e ->
            if (e != null) {
                Toast.makeText(this@DetailActivity, e.toString(), Toast.LENGTH_LONG).show()
                showLoading(false)
            }
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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

    private fun setTabLayout(userName: String?) {
        val usernameBundle = Bundle()
        usernameBundle.putString(EXTRA_USERNAME, userName)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, usernameBundle)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)

        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    private fun setDataToView(user: User) {
        detailBinding.tvDetailName.text =
            if (user.name == "null") getString(R.string.no_data) else user.name
        detailBinding.tvDetailUsername.text =
            if (user.login == "null") getString(R.string.no_data) else user.login
        detailBinding.tvDetailCompany.text =
            if (user.company == "null") getString(R.string.no_data) else user.company
        detailBinding.tvDetailRepo.text =
            if (user.public_repos == "null") getString(R.string.no_data) else user.public_repos
        detailBinding.tvDetailLocation.text =
            if (user.location == "null") getString(R.string.no_data) else user.location
        Glide.with(applicationContext)
            .load(user.avatar_url)
            .apply(RequestOptions().override(128, 128))
            .into(detailBinding.imgDetailAvatar)
    }

    private fun checkFavorited(user: String?): ArrayList<User> {
        val uriWithLogin = Uri.parse("$CONTENT_URI/$user")
        Log.d(TAG, "checkFavorited: URI = $uriWithLogin")

        val query = contentResolver.query(uriWithLogin, null, null, null, null)

        return MappingHelper.mapCursorToArrayList(query)
    }

    private fun addFavorites(user: User) {
        if (checkFavorited(user.login).size == 0) {
            val values = ContentValues()

            values.put(LOGIN, user.login)
            values.put(AVATAR_URL, user.avatar_url)
            values.put(TYPE, user.type)
            values.put(NAME, user.name)
            values.put(COMPANY, user.company)
            values.put(LOCATION, user.location)
            values.put(PUBLIC_REPOS, user.public_repos)

            contentResolver.insert(CONTENT_URI, values)

            detailBinding.fabAdd.setImageResource(R.drawable.ic_baseline_remove_24)
            Toast.makeText(this@DetailActivity, R.string.fav_add_succed, Toast.LENGTH_SHORT).show()
        } else {
            val uriWithId =
                Uri.parse(CONTENT_URI.toString() + "/" + checkFavorited(user.login)[0].id)
            contentResolver.delete(uriWithId, null, null)
            detailBinding.fabAdd.setImageResource(R.drawable.ic_baseline_star_24)
            Toast.makeText(this@DetailActivity, R.string.fav_remove, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            detailBinding.progressBar.visibility = View.VISIBLE
        } else {
            detailBinding.progressBar.visibility = View.GONE
        }
    }
}