package com.shencoder.mvvmkitdemo.http.bean

import com.shencoder.mvvmkit.http.bean.BaseResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/**
 *
 * @author Shenben
 * @date 2024/9/29 14:55
 * @description
 * @since
 */
@JsonClass(generateAdapter = true)
class PageResponse<T>(
    @Json(name = "code")
    val code: Int,
    @Json(name = "data")
    val data: PageData<T>,
    @Json(name = "msg")
    val msg: String
) : BaseResponse<PageData<T>> {

    override fun isSuccess(): Boolean {
        return code == 200
    }

    override fun getResponseCode(): Int {
        return code
    }

    override fun getResponseMsg(): String {
        return msg
    }

    override fun getResponseData(): PageData<T> {
        return data
    }
}

@JsonClass(generateAdapter = true)
data class PageData<T>(
    @Json(name = "total")
    val total: Int,
    @Json(name = "list")
    val list: List<T>
)