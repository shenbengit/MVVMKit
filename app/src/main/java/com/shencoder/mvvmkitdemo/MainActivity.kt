package com.shencoder.mvvmkitdemo

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import com.shencoder.mvvmkit.base.view.BaseSupportActivity
import com.shencoder.mvvmkit.util.MoshiUtils
import com.shencoder.mvvmkit.util.NullSafeMoshiUtils
import com.shencoder.mvvmkit.util.base64ToByteArray
import com.shencoder.mvvmkit.util.toBase64
import com.shencoder.mvvmkitdemo.databinding.ActivityMainBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseSupportActivity<MainViewModel, ActivityMainBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun injectViewModel(): Lazy<MainViewModel> {
        return viewModel()
    }

    override fun getViewModelId(): Int {
        return 0
    }

    override fun initView() {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_test_bg)
        val base64 = bitmap.toBase64()
        val bytes = base64.base64ToByteArray()
//        mBinding.iv.load(bytes){
//
//        }

        val list = listOf(Bean("a", 1), Bean("b", 2), Bean("c", 3), Bean("d", 4))
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val types = Types.newParameterizedType(List::class.java, Bean::class.java)

        val adapter = moshi.adapter<List<Bean>>(types)
        val json = MoshiUtils.toJson(list)
        val toJson = MoshiUtils.toJson(Bean("asdasd", 123123))
        Log.i(TAG, "initView: $toJson, map: ${MoshiUtils.fromJsonToMap<Any>(toJson)}")


        val toList1 = MoshiUtils.fromJsonToList<Bean>(json)
        val toList2 = NullSafeMoshiUtils.fromJsonToList(json, Bean::class.java)
        Log.i(TAG, "initView: $toList1")
        Log.i(TAG, "initView: $toList2")
    }


    override fun initData(savedInstanceState: Bundle?) {

    }

}