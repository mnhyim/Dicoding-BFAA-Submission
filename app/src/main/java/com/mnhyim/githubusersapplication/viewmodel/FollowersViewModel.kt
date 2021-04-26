package com.mnhyim.githubusersapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.mnhyim.githubusersapplication.BuildConfig
import com.mnhyim.githubusersapplication.model.User
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowersViewModel : ViewModel() {
    private val TAG: String = FollowersViewModel::class.java.simpleName

    private val listUsers = MutableLiveData<ArrayList<User>>()
    private var errorDesc = MutableLiveData<String>()

    fun setFollowers(username: String) {
        val listItems = ArrayList<User>()

        val client = AsyncHttpClient()
        client.addHeader("Authorization", BuildConfig.GITHUB_API_TOKEN)
        client.addHeader("User-Agent", "request")

        val url = "https://api.github.com/users/$username/followers"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                val resultArr = JSONArray(result)
                for (i in 0 until resultArr.length()) {
                    val userTemp = resultArr.getJSONObject(i)
                    val id = userTemp.getInt("id")
                    val login = userTemp.getString("login")
                    val avatarURL = userTemp.getString("avatar_url")
                    val type = userTemp.getString("type")

                    val userItem = User(id, login, avatarURL, type, null, null, null, null)
                    Log.d(TAG, "setFollowers: onSuccess = $userItem")

                    listItems.add(userItem)
                }

                listUsers.postValue(listItems)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "[Followers] Error $statusCode : Bad Request"
                    403 -> "[Followers] Error $statusCode : Forbidden"
                    404 -> "[Followers] Error $statusCode : Not Found"
                    0 -> "[Followers] Error $statusCode : No Internet Connection"
                    else -> "[Followers] Error $statusCode : ${error.message}"
                }
                errorDesc.postValue(errorMessage)
                Log.d(TAG, "setFollowers: onFailure = " + error.message.toString())
            }
        })
    }

    fun getUsers(): LiveData<ArrayList<User>> {
        return listUsers
    }

    fun getError(): LiveData<String> {
        return errorDesc
    }
}