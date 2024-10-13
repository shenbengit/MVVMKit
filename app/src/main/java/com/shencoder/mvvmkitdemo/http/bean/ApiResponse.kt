package com.shencoder.mvvmkitdemo.http.bean

import com.shencoder.mvvmkit.http.bean.BaseResponse
import com.squareup.moshi.Json


/**
 *
 * @author Shenben
 * @date 2024/9/29 14:47
 * @description
 * @since
 */
open class ApiResponse<T>(
    @Json(name = "code")
    val code: Int,
    @Json(name = "data")
    val data: T?,
    @Json(name = "msg")
    val msg: String
) : BaseResponse<T> {

    override fun isSuccess(): Boolean {
        return code == 200
    }

    override fun getResponseCode(): Int {
        return code
    }

    override fun getResponseMsg(): String {
        return msg
    }

    override fun getResponseData(): T? {
        return data
    }

    override fun toString(): String {
        return "ApiResponse(code=$code, data=$data, msg='$msg')"
    }

}