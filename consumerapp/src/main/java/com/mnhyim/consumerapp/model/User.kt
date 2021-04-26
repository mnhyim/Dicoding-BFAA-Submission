package com.mnhyim.consumerapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: Int,
    var login: String,
    var avatar_url: String?,
    var type: String?
) : Parcelable
