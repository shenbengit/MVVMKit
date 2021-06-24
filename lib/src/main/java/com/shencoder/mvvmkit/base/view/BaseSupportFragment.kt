package com.shencoder.mvvmkit.base.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.shencoder.mvvmkit.base.repository.IRepository
import com.shencoder.mvvmkit.base.viewmodel.BaseViewModel
import com.shencoder.loadingdialog.LoadingDialog
import com.weikaiyun.fragmentation.SupportFragment

/**
 *
 * @author  ShenBen
 * @date    2021/03/16 16:39
 * @email   714081644@qq.com
 */
abstract class BaseSupportFragment<VM : BaseViewModel<out IRepository>, VDB : ViewDataBinding> :
    SupportFragment() {

    protected lateinit var mBinding: VDB
    protected lateinit var mViewModel: VM
    private val mLoadingDialog by lazy { initLoadingDialog() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initView()
        initData(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lifecycle.removeObserver(mViewModel)
        mBinding.unbind()
    }

    protected abstract fun getLayoutId(): Int

    protected abstract fun injectViewModel(): Lazy<VM>

    protected abstract fun getVariableId(): Int

    protected abstract fun initView()

    protected abstract fun initData(savedInstanceState: Bundle?)

    private fun init() {
        mViewModel = injectViewModel().value
        mBinding.setVariable(getVariableId(), mViewModel)
        lifecycle.addObserver(mViewModel)
        mBinding.lifecycleOwner = this
        //注意：子类不可再重新执行此方法，已防止崩溃，具体的回调请看[baseLiveDataObserver(String)]
        mViewModel.baseLiveData.observe(viewLifecycleOwner, {
            baseLiveDataObserver(it)
        })
    }

    /**
     * 由[BaseViewModel.baseLiveData]发送的消息都将在此回调，子类可以重写这个方法
     * 默认处理了
     * [BaseViewModel.SHOW_LOADING_DIALOG]
     * [BaseViewModel.DISMISS_LOADING_DIALOG]
     */
    protected open fun baseLiveDataObserver(str: String) {
        when (str) {
            BaseViewModel.SHOW_LOADING_DIALOG -> {
                showLoadingDialog()
            }
            BaseViewModel.DISMISS_LOADING_DIALOG -> {
                dismissLoadingDialog()
            }
        }
    }


    /**
     * 可以重写这个方法自定义dialog
     */
    protected open fun initLoadingDialog(): Dialog {
        return LoadingDialog.createDefault(requireContext())
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