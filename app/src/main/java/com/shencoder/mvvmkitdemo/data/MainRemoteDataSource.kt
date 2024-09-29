package com.shencoder.mvvmkitdemo.data

import com.shencoder.mvvmkit.base.repository.IRemoteDataSource
import com.shencoder.mvvmkitdemo.http.RetrofitClient


/**
 *
 * @author Shenben
 * @date 2024/9/24 15:22
 * @description
 * @since
 */
class MainRemoteDataSource(private val retrofitClient: RetrofitClient) : IRemoteDataSource {

    suspend fun searchUsers(user: String) = retrofitClient.getApiService().searchUsers(user)

}