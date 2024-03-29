package com.shencoder.mvvmkit.base.view

import android.app.Activity
import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.shencoder.mvvmkit.base.repository.IRepository
import com.shencoder.mvvmkit.base.viewmodel.BaseViewModel
import com.shencoder.loadingdialog.LoadingDialog
import com.tencent.mmkv.MMKV
import com.weikaiyun.fragmentation.SupportFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel

/**
 *
 * @author  ShenBen
 * @date    2021/03/16 16:39
 * @email   714081644@qq.com
 */
abstract class BaseSupportFragment<VM : BaseViewModel<out IRepository>, VDB : ViewDataBinding> :
    SupportFragment() {
    private var _binding: VDB? = null

    /**
     * 仅在[onCreateView]->[onDestroyView]之间有效，不可在其他生命周期之外调用
     */
    protected val mBinding: VDB
        get() = _binding!!

    protected lateinit var mViewModel: VM
    private lateinit var mLoadingDialog: Dialog

    /**
     * 代替[SharedPreferences]
     */
    protected val mmkv: MMKV by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initView()
        initData(savedInstanceState)
    }

    override fun onDestroyView() {
        dismissLoadingDialog()

        super.onDestroyView()
        viewLifecycleOwner.lifecycle.removeObserver(mViewModel)
        _binding?.unbind()
        _binding = null
    }

    /**
     * 布局id
     */
    protected abstract fun getLayoutId(): Int

    /**
     * 注入ViewModel
     * @see [viewModel]
     * @see [sharedViewModel]
     * @see [sharedStateViewModel]
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
        viewLifecycleOwner.lifecycle.addObserver(mViewModel)
        mBinding.lifecycleOwner = viewLifecycleOwner
        //注意：子类不可再重新执行此方法，已防止崩溃，具体的回调请看[baseLiveDataObserver(String)]
        mViewModel.baseLiveData.observe(viewLifecycleOwner) {
            baseLiveDataObserver(it)
        }
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
        if (canShowLoadingDialog()) {
            if (this::mLoadingDialog.isInitialized.not()) {
                mLoadingDialog = initLoadingDialog()
            }
            mLoadingDialog.run {
                if (isShowing.not()) {
                    show()
                }
            }
        }
    }

    protected fun dismissLoadingDialog() {
        if (this::mLoadingDialog.isInitialized.not()) {
            return
        }
        mLoadingDialog.run {
            if (isShowing) {
                dismiss()
            }
        }
    }

    /**
     * 是否可以显示[mLoadingDialog]
     * 考虑到有可能[Activity]和[Fragment]共用[ViewModel]的情况
     */
    protected open fun canShowLoadingDialog() = true
}