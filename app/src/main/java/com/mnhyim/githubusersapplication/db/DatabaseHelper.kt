package com.mnhyim.githubusersapplication.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.AVATAR_URL
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.COMPANY
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.LOCATION
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.LOGIN
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.NAME
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.PUBLIC_REPOS
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.TYPE
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion._ID

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dbgithubapps"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_FAVORITES = "CREATE TABLE $TABLE_NAME" +
                " ($_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $LOGIN TEXT NOT NULL," +
                " $AVATAR_URL TEXT NOT NULL," +
                " $TYPE TEXT NOT NULL," +
                " $NAME TEXT NOT NULL," +
                " $COMPANY TEXT NOT NULL," +
                " $LOCATION TEXT NOT NULL," +
                " $PUBLIC_REPOS TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}