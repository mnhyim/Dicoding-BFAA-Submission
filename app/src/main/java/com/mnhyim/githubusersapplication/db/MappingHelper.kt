package com.mnhyim.githubusersapplication.db

import android.database.Cursor
import android.util.Log
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.AVATAR_URL
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.COMPANY
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.LOCATION
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.LOGIN
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.NAME
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.PUBLIC_REPOS
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.TYPE
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion._ID
import com.mnhyim.githubusersapplication.model.User

object MappingHelper {
    private val TAG: String = MappingHelper::class.java.simpleName

    fun mapCursorToArrayList(favoritesCursor: Cursor?): ArrayList<User> {
        val favoritesList = ArrayList<User>()

        favoritesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val login = getString(getColumnIndexOrThrow(LOGIN))
                val avatar_url = getString(getColumnIndexOrThrow(AVATAR_URL))
                val type = getString(getColumnIndexOrThrow(TYPE))
                val name = getString(getColumnIndexOrThrow(NAME))
                val company = getString(getColumnIndexOrThrow(COMPANY))
                val location = getString(getColumnIndexOrThrow(LOCATION))
                val public_repos = getString(getColumnIndexOrThrow(PUBLIC_REPOS))

                val x = User(id, login, avatar_url, type, name, company, location, public_repos)
                Log.d(TAG, "MappingHelper: mapCursorToArrayList = $x")
                favoritesList.add(x)
            }
        }
        return favoritesList
    }
}