package com.mnhyim.githubusersapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: Int,
    var login: String,
    var avatar_url: String?,
    var type: String?,
    var name: String?,
    var company: String?,
    var location: String?,
    var public_repos: String?
) : Parcelable
