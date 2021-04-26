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
import org.json.JSONObject

class DetailViewModel : ViewModel() {
    private val TAG: String = DetailViewModel::class.java.simpleName

    val usersDetail = MutableLiveData<User>()
    private var errorDesc = MutableLiveData<String>()

    fun setUsersDetail(username: String?) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", BuildConfig.GITHUB_API_TOKEN)
        client.addHeader("User-Agent", "request")

        val url = "https://api.github.com/users/$username"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                val resultObj = JSONObject(result)

                val id = resultObj.getInt("id")
                val login = resultObj.getString("login")
                val avatarURL = resultObj.getString("avatar_url")
                val type = resultObj.getString("type")
                val name = resultObj.getString("name")
                val company = resultObj.getString("company")
                val location = resultObj.getString("location")
                val repos = resultObj.getString("public_repos")
                val userItem = User(id, login, avatarURL, type, name, company, location, repos)

                usersDetail.postValue(userItem)
                Log.d(TAG, "setUsersDetail: onSuccess = $userItem")

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "[Detail] Error $statusCode : Bad Request"
                    403 -> "[Detail] Error $statusCode : Forbidden"
                    404 -> "[Detail] Error $statusCode : Not Found"
                    0 -> "[Detail] Error $statusCode : No Internet Connection"
                    else -> "[Detail] Error $statusCode : ${error.message}"
                }
                errorDesc.postValue(errorMessage)
                Log.d(TAG, "setUsersDetail: onFailure = " + error.message.toString())
            }
        })
    }

    fun getUsers(): LiveData<User> {
        return usersDetail
    }

    fun getError(): LiveData<String> {
        return errorDesc
    }
}