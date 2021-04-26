package com.mnhyim.githubusersapplication.db.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.AUTHORITY
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.mnhyim.githubusersapplication.db.FavoritesHelper

class FavoritesProvider : ContentProvider() {

    private var TAG: String = FavoritesProvider::class.java.simpleName

    companion object {
        private const val FAVORITES = 1
        private const val FAVORITES_ID = 2
        private const val FAVORITES_LOGIN = 3
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        private lateinit var favoritesHelper: FavoritesHelper
    }

    init {
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVORITES)
        sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", FAVORITES_ID)
        sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/*", FAVORITES_LOGIN)
    }

    override fun onCreate(): Boolean {
        favoritesHelper = FavoritesHelper.getInstance(context as Context)
        favoritesHelper.open()
        return true
    }

    override fun query(
        uri: Uri,
        strings: Array<String>?,
        s: String?,
        strings1: Array<String>?,
        s1: String?
    ): Cursor? {
        Log.d(TAG, "" + sUriMatcher.match(uri))
        return when (sUriMatcher.match(uri)) {
            FAVORITES -> {
                Log.d(TAG, "query: URI = FAVORITES")
                favoritesHelper.queryAll()
            }
            FAVORITES_ID -> {
                Log.d(TAG, "query: URI = FAVORITES_ID")
                favoritesHelper.queryById(uri.lastPathSegment.toString())
            }
            FAVORITES_LOGIN -> {
                Log.d(TAG, "query: URI = FAVORITES_LOGIN")
                favoritesHelper.queryByLogin(uri.lastPathSegment.toString())
            }
            else -> {
                Log.d(TAG, "query_else: URI = $uri")
                null
            }
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (FAVORITES) {
            sUriMatcher.match(uri) -> favoritesHelper.insert(values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated: Int = when (FAVORITES_ID) {
            sUriMatcher.match(uri) -> favoritesHelper.update(uri.lastPathSegment.toString(), values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        Log.d(TAG, "delete : $uri")
        val deleted: Int = when (FAVORITES_ID) {
            sUriMatcher.match(uri) -> {
                favoritesHelper.deleteById(uri.lastPathSegment.toString())
            }
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }
}