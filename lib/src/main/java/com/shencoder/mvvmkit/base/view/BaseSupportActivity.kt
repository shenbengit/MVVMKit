package com.shencoder.mvvmkit.base.view

import android.app.Dialog
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.shencoder.mvvmkit.base.repository.IRepository
import com.shencoder.mvvmkit.base.viewmodel.BaseViewModel
import com.shencoder.loadingdialog.LoadingDialog
import com.shencoder.mvvmkit.ext.inflateBinding
import com.shencoder.mvvmkit.network.NetworkObserverManager
import com.weikaiyun.fragmentation.SupportActivity
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.fragment.android.setupKoinFragmentFactory
import org.koin.androidx.scope.activityScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

/**
 *
 * @author  ShenBen
 * @date    2021/03/16 15:32
 * @email   714081644@qq.com
 */
abstract class BaseSupportActivity<VM : BaseViewModel<out IRepository>, VDB : ViewDataBinding> :
    SupportActivity(), NetworkObserverManager.Listener, AndroidScopeComponent,
    IViewDataBinding<VDB> {

    protected val TAG = javaClass.simpleName

    private var _binding: VDB? = null

    /**
     * 仅在[onCreate]->[onDestroy]之间有效，不可在其他生命周期之外调用
     */
    protected val mBinding: VDB
        get() = _binding!!

    protected lateinit var mViewModel: VM
    private lateinit var mLoadingDialog: Dialog

    override val scope: Scope by activityScope()

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeCreateView()
        val layoutId = getLayoutId()
        _binding = if (layoutId == 0) {
            inflateBinding(layoutInflater)
        } else {
            DataBindingUtil.setContentView(this, getLayoutId())
        }
        setContentView(mBinding.root)
        init()
        initView()
        initData(savedInstanceState)
    }

    override fun onDestroy() {
        dismissLoadingDialog()
        super.onDestroy()
        lifecycle.removeObserver(mViewModel)
        _binding?.unbind()
        _binding = null
        NetworkObserverManager.getInstance().removeListener(this)
    }

    protected open fun beforeCreateView() {

    }

    /**
     * 布局id
     * @return 0:使用反射获取
     */
    protected open fun getLayoutId(): Int = 0

    /**
     * 注入ViewModel
     * @see [viewModel]
     */
    protected abstract fun injectViewModel(): Lazy<VM>

    /**
     * 布局文件中ViewModel的id
     */
    protected abstract fun getViewModelId(): Int

    protected abstract fun initView()

    protected abstract fun initData(savedInstanceState: Bundle?)

    private fun init() {
        setupKoinFragmentFactory(scope)

        mViewModel = injectViewModel().value
        mBinding.setVariable(getViewModelId(), mViewModel)
        lifecycle.addObserver(mViewModel)
        mBinding.lifecycleOwner = this
        //注意：子类不可再重新执行此方法，已防止崩溃，具体的回调请看[baseLiveDataObserver(String)]
        mViewModel.baseLiveData.observe(this) {
            baseLiveDataObserver(it)
        }
        NetworkObserverManager.getInstance().addListener(this)
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
        if (this::mLoadingDialog.isInitialized.not()) {
            mLoadingDialog = initLoadingDialog()
        }
        mLoadingDialog.run {
            if (isShowing.not()) {
                show()
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
     * 网络连接状态回调
     *
     * @param isOnline
     */
    override fun onConnectivityChange(isOnline: Boolean) {

    }

    override fun onCloseScope() {

    }
}