package com.shencoder.mvvmkitdemo

import android.os.Bundle
import com.shencoder.mvvmkit.base.view.BaseSupportActivity
import com.shencoder.mvvmkit.base.viewmodel.DefaultViewModel
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

    }


    override fun initData(savedInstanceState: Bundle?) {

    }

}