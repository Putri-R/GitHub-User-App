package com.myapp.githubuser.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myapp.githubuser.data.response.GithubResponse
import com.myapp.githubuser.data.response.ItemsItem
import com.myapp.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private val _listAccount = MutableLiveData<List<ItemsItem>>()
    val listAccount: LiveData<List<ItemsItem>> = _listAccount

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        searchGithubUsers("arif")
    }

    fun searchGithubUsers(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchUsers(query)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listAccount.value = responseBody.items?.filterNotNull()
                    }
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}