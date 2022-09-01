package com.shencoder.mvvmkit.coil

import coil.ImageLoader
import coil.decode.DataSource
import coil.decode.ImageSource
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.fetch.SourceResult
import okio.BufferedSource
import coil.load
import coil.request.Options


/**
 * [load]直接使用流显示图片
 *
 * @author  ShenBen
 * @date    2021/01/27 15:53
 * @email   714081644@qq.com
 */
class BufferedSourceFetcher(
    private val data: BufferedSource,
    private val options: Options
) : Fetcher {

    override suspend fun fetch(): FetchResult {
        return SourceResult(
            source = ImageSource(data, options.context),
            mimeType = "application/octet-stream",
            dataSource = DataSource.NETWORK
        )
    }

    class Factory : Fetcher.Factory<BufferedSource> {
        override fun create(
            data: BufferedSource,
            options: Options,
            imageLoader: ImageLoader
        ): Fetcher {
            return BufferedSourceFetcher(data, options)
        }
    }
}