package com.mnhyim.consumerapp

import android.database.Cursor
import com.mnhyim.consumerapp.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.AVATAR_URL
import com.mnhyim.consumerapp.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.LOGIN
import com.mnhyim.consumerapp.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.TYPE
import com.mnhyim.consumerapp.DatabaseContract.DatabaseContract.FavoriteColumns.Companion._ID

object MappingHelper {
    fun mapCursorToArrayList(favoritesCursor: Cursor?): ArrayList<User> {
        val favoritesList = ArrayList<User>()

        favoritesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val login = getString(getColumnIndexOrThrow(LOGIN))
                val avatar_url = getString(getColumnIndexOrThrow(AVATAR_URL))
                val type = getString(getColumnIndexOrThrow(TYPE))

                val x = User(id, login, avatar_url, type)
                favoritesList.add(x)
            }
        }
        return favoritesList
    }
}