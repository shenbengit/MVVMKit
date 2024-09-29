package com.shencoder.mvvmkitdemo

import com.shencoder.mvvmkitdemo.http.bean.GitHubSearchUsersBean
import retrofit2.http.GET
import retrofit2.http.Query


/**
 *
 * @author Shenben
 * @date 2024/9/29 14:42
 * @description
 * @since
 */
interface ApiService {

    @GET("search/users")
    suspend fun searchUsers(@Query("q") user: String): GitHubSearchUsersBean

}