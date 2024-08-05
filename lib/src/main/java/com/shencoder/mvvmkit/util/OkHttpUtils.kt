package com.shencoder.mvvmkit.util

import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 *
 *
 * @date    2023/08/08 21:34
 *
 */

fun OkHttpClient.Builder.ignoreSSL() = apply {
    //https 证书忽略
    val trustManager: X509TrustManager = object : X509TrustManager {

        override fun checkClientTrusted(
            chain: Array<out X509Certificate>?,
            authType: String?
        ) {
        }

        override fun checkServerTrusted(
            chain: Array<out X509Certificate>?,
            authType: String?
        ) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }

    }

    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, arrayOf<TrustManager>(trustManager), SecureRandom())
    val sslSocketFactory = sslContext.socketFactory
    val hostnameVerifier = HostnameVerifier { _, _ -> true }

    sslSocketFactory(sslSocketFactory, trustManager)

    hostnameVerifier(hostnameVerifier)
}