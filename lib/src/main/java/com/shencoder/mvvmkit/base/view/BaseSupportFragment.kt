package com.shencoder.mvvmkit.base.view

import android.app.Activity
import android.app.Dialog
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
import com.shencoder.mvvmkit.ext.inflateBinding
import com.shencoder.mvvmkit.network.NetworkObserverManager
import com.weikaiyun.fragmentation.SupportFragment
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.core.scope.Scope

/**
 *
 * @author  ShenBen
 * @date    2021/03/16 16:39
 * @email   714081644@qq.com
 */
abstract class BaseSupportFragment<VM : BaseViewModel<out IRepository>, VDB : ViewDataBinding> :
    SupportFragment(), NetworkObserverManager.Listener, AndroidScopeComponent,
    IViewDataBinding<VDB> {

    protected val TAG by lazy { this.javaClass.simpleName }

    private var _binding: VDB? = null

    /**
     * 仅在[onCreateView]->[onDestroyView]之间有效，不可在其他生命周期之外调用
     */
    protected val binding: VDB
        get() = _binding!!

    protected lateinit var viewModel: VM
    private lateinit var loadingDialog: Dialog

    override val scope: Scope by fragmentScope()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutId = getLayoutId()
        _binding = if (layoutId == 0) {
            inflateBinding(inflater, container)
        } else {
            DataBindingUtil.inflate(inflater, layoutId, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initView()
        initData(savedInstanceState)
    }

    override fun onDestroyView() {
        if (isRegisterNetwork()) {
            NetworkObserverManager.getInstance().removeListener(this)
        }
        dismissLoadingDialog()
        super.onDestroyView()
        viewLifecycleOwner.lifecycle.removeObserver(viewModel)
        _binding?.unbind()
        _binding = null
    }

    /**
     * 布局id
     * @return 0:使用反射获取
     */
    protected open fun getLayoutId(): Int = 0

    /**
     * 注入ViewModel
     * @see [viewModel]
     * @see [activityViewModel]
     */
    protected abstract fun injectViewModel(): Lazy<VM>

    /**
     * 布局文件中ViewModel的id
     */
    protected abstract fun getViewModelId(): Int

    protected abstract fun initView()

    protected abstract fun initData(savedInstanceState: Bundle?)

    private fun init() {
        viewModel = injectViewModel().value
        binding.setVariable(getViewModelId(), viewModel)
        viewLifecycleOwner.lifecycle.addObserver(viewModel)
        binding.lifecycleOwner = viewLifecycleOwner
        //注意：子类不可再重新执行此方法，已防止崩溃，具体的回调请看[baseLiveDataObserver(String)]
        viewModel.baseLiveData.observe(viewLifecycleOwner) {
            baseLiveDataObserver(it)
        }

        if (isRegisterNetwork()) {
            NetworkObserverManager.getInstance().addListener(this)
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

    fun showLoadingDialog() {
        if (canShowLoadingDialog()) {
            if (this::loadingDialog.isInitialized.not()) {
                loadingDialog = initLoadingDialog()
            }
            loadingDialog.run {
                if (isShowing.not()) {
                    show()
                }
            }
        }
    }

    fun dismissLoadingDialog() {
        if (this::loadingDialog.isInitialized.not()) {
            return
        }
        loadingDialog.run {
            if (isShowing) {
                dismiss()
            }
        }
    }

    /**
     * 是否可以显示[loadingDialog]
     * 考虑到有可能[Activity]和[Fragment]共用[ViewModel]的情况
     */
    protected open fun canShowLoadingDialog() = true

    /**
     * 是否监听网络变化
     * @see NetworkObserverManager
     * @see onConnectivityChange
     */
    protected open fun isRegisterNetwork() = false

    /**
     * 网络连接状态回调
     * @see isRegisterNetwork
     * @param isOnline
     */
    override fun onConnectivityChange(isOnline: Boolean) {

    }

    override fun onCloseScope() {

    }
}