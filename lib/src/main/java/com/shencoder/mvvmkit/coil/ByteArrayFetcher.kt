package com.shencoder.mvvmkit.coil

import coil.bitmap.BitmapPool
import coil.decode.DataSource
import coil.decode.Options
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.fetch.SourceResult
import coil.size.Size
import okio.buffer
import okio.source
import java.io.ByteArrayInputStream
import coil.loadAny

/**
 * [loadAny] 图片加载框架不支持 [ByteArray] ，需要自己实现
 *
 * @author  ShenBen
 * @date    2020/12/30 11:05
 * @email   714081644@qq.com
 */
class ByteArrayFetcher : Fetcher<ByteArray> {

    override suspend fun fetch(
        pool: BitmapPool,
        data: ByteArray,
        size: Size,
        options: Options
    ): FetchResult {
        return SourceResult(
            source = ByteArrayInputStream(data).source().buffer(),
            mimeType = null,
            dataSource = DataSource.MEMORY
        )
    }

    override fun key(data: ByteArray): String? = null
}