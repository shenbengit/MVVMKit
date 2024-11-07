package com.shencoder.mvvmkitdemo

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.shencoder.mvvmkit.base.view.BaseSupportActivity
import com.shencoder.mvvmkit.ext.base64ToByteArray
import com.shencoder.mvvmkit.ext.clickWithTrigger
import com.shencoder.mvvmkit.ext.dp2px
import com.shencoder.mvvmkit.ext.getColor
import com.shencoder.mvvmkit.ext.getDimensionPixelSize
import com.shencoder.mvvmkit.ext.getDrawable
import com.shencoder.mvvmkit.ext.gradientDrawable
import com.shencoder.mvvmkit.ext.logI
import com.shencoder.mvvmkit.ext.shapeDrawable
import com.shencoder.mvvmkit.http.DownloadFile
import com.shencoder.mvvmkit.http.downloadFile
import com.shencoder.mvvmkit.network.NetworkObserverManager
import com.shencoder.mvvmkit.util.AppUtils
import com.shencoder.mvvmkit.util.MoshiUtils
import com.shencoder.mvvmkit.util.NullSafeMoshiUtils
import com.shencoder.mvvmkit.util.mmkv.asLiveData
import com.shencoder.mvvmkit.util.mmkv.globalMmkv
import com.shencoder.mvvmkit.util.mmkv.mmkv
import com.shencoder.mvvmkit.util.mmkv.mmkvString
import com.shencoder.mvvmkit.util.toBase64
import com.shencoder.mvvmkitdemo.databinding.ActivityMainBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseSupportActivity<MainViewModel, ActivityMainBinding>() {

//    override fun getLayoutId(): Int {
//        return R.layout.activity_main
//    }

    override fun injectViewModel(): Lazy<MainViewModel> {
        return viewModel()
    }

    override fun getViewModelId(): Int {
        return BR.viewModel
    }

    override fun initView() {
        val fragment = findFragment(TestFragment::class.java)
        if (fragment == null) {
            loadRootFragment(R.id.flRoot, TestFragment.newInstance())
        }

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
        logI(TAG, "initView: $toJson, map: ${MoshiUtils.fromJsonToMap<Any>(toJson)}")


        val toList1 = MoshiUtils.fromJsonToList<Bean>(json)
        val toList2 = NullSafeMoshiUtils.fromJsonToList(json, Bean::class.java)
        logI(TAG, "initView: $toList1")
        logI(TAG, "initView: $toList2")

        mmkv.encode("AAA", "123")
        globalMmkv.encode("BBB", "456")
        logI(TAG, "initView: mmkv: ${mmkv.decodeString("AAA")}")
        logI(
            TAG,
            "initView: globalMmkv: ${globalMmkv.decodeString("BBB")}, ${globalMmkv.decodeString("BBB1")}"
        )
        val adc by mmkvString(default = "", mmkv = mmkv).asLiveData()
        adc.observe(this) {
            logI(TAG, "initView: mmkv: adc $it")
        }
        adc.value=""
//        adc = "123123"
        logI(TAG, "initView: mmkv: adc $adc")
//        startActivity<MainActivity> {
//            putExtra("AAA", "123")
//        }

        val md5 = AppUtils.getAppSignaturesMd5()
        val sha1 = AppUtils.getAppSignaturesSha1()
        val sha256 = AppUtils.getAppSignaturesSha256()
        logI(TAG, "initView: getAppSignaturesMd5: $md5")
        logI(TAG, "initView: getAppSignaturesSha1: $sha1")
        logI(TAG, "initView: getAppSignaturesSha256: $sha256")

        logI(TAG, "initView: isAppFirstTimeInstall: ${AppUtils.isAppFirstTimeInstall()}")
        logI(TAG, "initView: isAppUpgraded: ${AppUtils.isAppUpgraded()}")
        logI(TAG, "initView: isAppDebug: ${AppUtils.isAppDebug()}")
        logI(TAG, "initView: getAppVersionCode: ${AppUtils.getAppVersionCode()}")
        logI(TAG, "initView: getAppVersionName: ${AppUtils.getAppVersionName()}")
        logI(TAG, "initView: getPackageName: ${AppUtils.getPackageName()}")
        logI(TAG, "initView: isDeviceRooted: ${AppUtils.isDeviceRooted()}")
        logI(
            TAG,
            "initView: isDevelopmentSettingsEnabled: ${AppUtils.isDevelopmentSettingsEnabled()}"
        )
        logI(TAG, "initView: isUsbDebugSettingsEnabled: ${AppUtils.isUsbDebugSettingsEnabled()}")

//        binding.iv2.setImageDrawable(
//            shapeDrawable(
//                solidColor = Color.BLUE,
//                cornerTopLeftRadius = dp2px(25f).toFloat(),
//                cornerTopRightRadius = dp2px(25f).toFloat(),
//                cornerBottomRightRadius = dp2px(25f).toFloat(),
//                cornerBottomLeftRadius = dp2px(25f).toFloat(),
//                strokeColor = Color.WHITE,
//                strokeWidth = dp2px(5f)
//            )
//        )
        val color = R.color.white.getColor()
        val drawable = R.drawable.a.getDrawable()

        binding.iv2.setImageDrawable(
            gradientDrawable(
                colors = intArrayOf(
                    Color.parseColor("#8BC34A"),
                    Color.parseColor("#FFC54E"),
                    Color.parseColor("#FF9326"),
                ),
                orientation = GradientDrawable.Orientation.LEFT_RIGHT,
                gradientCenterX = 0.2f,
                gradientCenterY = 0.2f,
                cornerTopLeftRadius = dp2px(25f).toFloat(),
                cornerTopRightRadius = dp2px(25f).toFloat(),
                cornerBottomRightRadius = dp2px(25f).toFloat(),
                cornerBottomLeftRadius = dp2px(25f).toFloat(),
                strokeColor = Color.WHITE,
                strokeWidth = dp2px(5f)
            )
        )

//        lifecycleScope.launch {
//            downloadFile(url = "", filePath = "", md5 = "") {
//                start {
//
//                }
//                progress { totalSize, downloadSize, progress ->
//
//                }
//                success { file, fileMd5, md5VerifySuccess ->
//
//                }
//                error {
//
//                }
//            }.startDownload()
//
//            DownloadFile(url = "", filePath = "", md5 = "").start {
//
//            }.progress { totalSize, downloadSize, progress ->
//
//            }.success { file, fileMd5, md5VerifySuccess ->
//
//            }.error {
//
//            }.startDownload()
//        }

    }

    override fun initData(savedInstanceState: Bundle?) {

    }

}