package com.mnhyim.githubusersapplication.db

import android.net.Uri
import android.provider.BaseColumns

class DatabaseContract : BaseColumns {
    object DatabaseContract {
        const val AUTHORITY = "com.mnhyim.githubusersapplication"
        const val SCHEME = "content"

        class FavoriteColumns : BaseColumns {
            companion object {
                const val TABLE_NAME = "favorites"
                const val _ID = "_id"
                const val LOGIN = "login"
                const val AVATAR_URL = "avatar_url"
                const val TYPE = "type"
                const val NAME = "name"
                const val COMPANY = "company"
                const val LOCATION = "location"
                const val PUBLIC_REPOS = "public_repos"

                val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()
            }
        }
    }
}