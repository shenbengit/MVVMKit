package com.shencoder.mvvmkit.coil

import coil.bitmap.BitmapPool
import coil.decode.DataSource
import coil.decode.Options
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.fetch.SourceResult
import coil.size.Size
import okio.BufferedSource
import coil.loadAny


/**
 * [loadAny]直接使用流显示图片
 *
 * @author  ShenBen
 * @date    2021/01/27 15:53
 * @email   714081644@qq.com
 */
class BufferedSourceFetcher : Fetcher<BufferedSource> {
    override suspend fun fetch(
        pool: BitmapPool,
        data: BufferedSource,
        size: Size,
        options: Options
    ): FetchResult {
        return SourceResult(
            source = data,
            mimeType = "application/octet-stream",
            dataSource = DataSource.NETWORK
        )
    }

    override fun key(data: BufferedSource): String? {
        return null
    }
}