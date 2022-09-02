package com.shencoder.mvvmkitdemo

import android.graphics.BitmapFactory
import android.os.Bundle
import com.shencoder.mvvmkit.base.view.BaseSupportActivity
import com.shencoder.mvvmkit.base.viewmodel.DefaultViewModel
import com.shencoder.mvvmkit.util.base64ToByteArray
import com.shencoder.mvvmkit.util.toBase64
import com.shencoder.mvvmkitdemo.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseSupportActivity<DefaultViewModel,ActivityMainBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun injectViewModel(): Lazy<DefaultViewModel> {
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
    }


    override fun initData(savedInstanceState: Bundle?) {

    }

}