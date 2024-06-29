package com.myapp.githubuser.data.retrofit

import com.myapp.githubuser.data.response.DetailUserResponse
import retrofit2.Call
import retrofit2.http.*
import com.myapp.githubuser.data.response.GithubResponse
import com.myapp.githubuser.data.response.ItemsItem

interface ApiService {
    @GET("search/users")
    fun searchUsers(
        @Query("q") query: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<ItemsItem>>
}