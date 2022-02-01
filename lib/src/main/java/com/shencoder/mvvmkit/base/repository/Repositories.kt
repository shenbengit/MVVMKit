package com.shencoder.mvvmkit.base.repository

/**
 *
 * @author  ShenBen
 * @date    2021/03/16 17:25
 * @email   714081644@qq.com
 */

/**
 * 网络请求和本地数据库均需要使用的情况
 */
open class BaseBothRepository<T : IRemoteDataSource, R : ILocalDataSource>(
    protected val remoteDataSource: T,
    protected val localDataSource: R
) : BaseRepository()

/**
 * 仅使用本地数据库
 */
open class BaseLocalRepository<T : ILocalDataSource>(
    protected val remoteDataSource: T
) : BaseRepository()

/**
 * 仅使用网络请求
 */
open class BaseRemoteRepository<T : IRemoteDataSource>(
    protected val remoteDataSource: T
) : BaseRepository()

/**
 * 不需要数据
 */
class BaseNothingRepository : BaseRepository()