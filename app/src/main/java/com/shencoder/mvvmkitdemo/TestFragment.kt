package com.shencoder.mvvmkitdemo

import android.os.Bundle
import com.shencoder.mvvmkit.base.view.BaseSupportFragment
import com.shencoder.mvvmkit.base.viewmodel.DefaultViewModel
import com.shencoder.mvvmkitdemo.databinding.FragmentTestBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 *
 * @author Shenben
 * @date 2024/9/30 11:03
 * @description
 * @since
 */
class TestFragment : BaseSupportFragment<DefaultViewModel, FragmentTestBinding>() {

    companion object {
        @JvmStatic
        fun newInstance(): TestFragment {
            val args = Bundle()
            val fragment = TestFragment()
            fragment.arguments = args
            return fragment
        }
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