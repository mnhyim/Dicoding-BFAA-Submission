package com.mnhyim.githubusersapplication.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.LOGIN
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.mnhyim.githubusersapplication.db.DatabaseContract.DatabaseContract.FavoriteColumns.Companion._ID
import java.sql.SQLException

class FavoritesHelper(context: Context) {
    private var TAG: String = FavoritesHelper::class.java.simpleName

    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME

        private var INSTANCE: FavoritesHelper? = null
        fun getInstance(context: Context): FavoritesHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoritesHelper(context)
            }
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        Log.d(TAG, "FavoritesHelper: queryAll")
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }

    fun queryById(id: String): Cursor {
        Log.d(TAG, "FavoritesHelper: queryById")
        return database.query(
            DATABASE_TABLE,
            null,
            "$_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    fun queryByLogin(login: String?): Cursor {
        Log.d(TAG, "FavoritesHelper: queryByLogin")
        return database.query(
            DATABASE_TABLE,
            null,
            "$LOGIN = ?",
            arrayOf(login),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long {
        Log.d(TAG, "FavoritesHelper: insert")
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        Log.d(TAG, "FavoritesHelper: update")
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }

    fun deleteById(id: String): Int {
        Log.d(TAG, "FavoritesHelper: deleteById")
        return database.delete(DATABASE_TABLE, "$_ID = ?", arrayOf(id))
    }

}