package com.shencoder.mvvmkit.base.view

import android.app.Dialog
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.shencoder.mvvmkit.base.repository.IRepository
import com.shencoder.mvvmkit.base.viewmodel.BaseViewModel
import com.shencoder.loadingdialog.LoadingDialog
import com.weikaiyun.fragmentation.SupportActivity

/**
 *
 * @author  ShenBen
 * @date    2021/03/16 15:32
 * @email   714081644@qq.com
 */
abstract class BaseSupportActivity<VM : BaseViewModel<out IRepository>, VDB : ViewDataBinding> :
    SupportActivity() {

    protected val mTag = javaClass.simpleName
    protected lateinit var mBinding: VDB
    protected lateinit var mViewModel: VM
    private val mLoadingDialog by lazy { initLoadingDialog() }

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeCreateView()
        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
        init()
        initView()
        initData(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(mViewModel)
        mBinding.unbind()
    }

    protected open fun beforeCreateView() {

    }

    /**
     * 布局id
     */
    protected abstract fun getLayoutId(): Int

    /**
     * 注入ViewModel
     */
    protected abstract fun injectViewModel(): Lazy<VM>

    /**
     * 布局文件中ViewModel的id
     */
    protected abstract fun getViewModelId(): Int

    protected abstract fun initView()

    protected abstract fun initData(savedInstanceState: Bundle?)

    private fun init() {
        mViewModel = injectViewModel().value
        mBinding.setVariable(getViewModelId(), mViewModel)
        lifecycle.addObserver(mViewModel)
        mBinding.lifecycleOwner = this
        //注意：子类不可再重新执行此方法，已防止崩溃，具体的回调请看[baseLiveDataObserver(String)]
        mViewModel.baseLiveData.observe(this, {
            baseLiveDataObserver(it)
        })
    }

    /**
     * 由[BaseViewModel.baseLiveData]发送的消息都将在此回调，子类可以重写这个方法
     * 默认处理了
     * [BaseViewModel.SHOW_LOADING_DIALOG]
     * [BaseViewModel.DISMISS_LOADING_DIALOG]
     * [BaseViewModel.BACK_PRESSED]
     */
    protected open fun baseLiveDataObserver(str: String) {
        when (str) {
            BaseViewModel.SHOW_LOADING_DIALOG -> {
                showLoadingDialog()
            }
            BaseViewModel.DISMISS_LOADING_DIALOG -> {
                dismissLoadingDialog()
            }
            BaseViewModel.BACK_PRESSED -> {
                onBackPressedSupport()
            }
        }
    }


    /**
     * 可以重写这个方法自定义dialog
     */
    protected open fun initLoadingDialog(): Dialog {
        return LoadingDialog.createDefault(this)
    }

    protected fun showLoadingDialog() {
        mLoadingDialog.run {
            if (isShowing.not()) {
                show()
            }
        }
    }

    protected fun dismissLoadingDialog() {
        mLoadingDialog.run {
            if (isShowing) {
                dismiss()
            }
        }
    }
}