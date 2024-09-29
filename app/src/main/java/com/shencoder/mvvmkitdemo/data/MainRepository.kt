package com.shencoder.mvvmkitdemo.data

import com.shencoder.mvvmkit.base.repository.BaseRemoteRepository


/**
 *
 * @author Shenben
 * @date 2024/9/24 15:21
 * @description
 * @since
 */
class MainRepository(remoteDataSource: MainRemoteDataSource) :
    BaseRemoteRepository<MainRemoteDataSource>(remoteDataSource) {

    suspend fun searchUsers(user: String) = remoteDataSource.searchUsers(user)
}