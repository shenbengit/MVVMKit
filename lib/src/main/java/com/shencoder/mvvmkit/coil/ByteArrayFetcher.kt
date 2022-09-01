package com.shencoder.mvvmkit.coil

import coil.ImageLoader
import coil.decode.DataSource
import coil.decode.ImageSource
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.fetch.SourceResult
import coil.loadAny
import coil.request.Options
import okio.Buffer

/**
 * [loadAny] 图片加载框架不支持 [ByteArray] ，需要自己实现
 *
 * @author  ShenBen
 * @date    2020/12/30 11:05
 * @email   714081644@qq.com
 */
class ByteArrayFetcher(
    private val data: ByteArray,
    private val options: Options
) : Fetcher {


    override suspend fun fetch(): FetchResult {
        val source = try {
            Buffer().apply { write(data) }
        } finally {
        }
        return SourceResult(
            source = ImageSource(source,options.context),
            mimeType = null,
            dataSource = DataSource.MEMORY
        )
    }

    class Factory : Fetcher.Factory<ByteArray> {

        override fun create(data: ByteArray, options: Options, imageLoader: ImageLoader): Fetcher {
            return ByteArrayFetcher(data, options)
        }
    }
}