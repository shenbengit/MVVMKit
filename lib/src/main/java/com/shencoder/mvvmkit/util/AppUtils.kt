package com.shencoder.mvvmkit.util

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.content.pm.SigningInfo
import android.os.Build
import android.os.Process
import android.provider.Settings
import com.shencoder.mvvmkit.util.codec.binary.Hex
import com.shencoder.mvvmkit.util.codec.digest.DigestUtils
import kotlin.system.exitProcess


/**
 * @see com.shencoder.mvvmkit.util.codec 依赖下的工具类
 *
 * @author Shenben
 * @date 2024/10/8 11:22
 * @description
 * @since
 */
object AppUtils {

    @JvmStatic
    fun isDeviceRooted() = RootUtils.isDeviceRooted()

    /**
     * Is development settings enabled
     * 开发者模式是否打开
     * @return
     */
    @JvmStatic
    fun isDevelopmentSettingsEnabled(): Boolean {
        return Settings.Global.getInt(
            AppManager.context.contentResolver,
            Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
            0
        ) != 0
    }

    /**
     * Is usb debug settings enabled
     * USB调试是否打开
     * @return
     */
    @JvmStatic
    fun isUsbDebugSettingsEnabled(): Boolean {
        return Settings.Global.getInt(
            AppManager.context.contentResolver,
            Settings.Global.ADB_ENABLED,
            0
        ) != 0
    }

    @JvmStatic
    fun isAppDebug(): Boolean {
        return (AppManager.context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }

    @JvmStatic
    fun isAppForeground() = AppManager.isAppForeground

    @JvmStatic
    fun getPackageName(): String = AppManager.context.packageName

    @JvmStatic
    @JvmOverloads
    fun getPackageInfo(flags: Int = 0): PackageInfo? {
        return kotlin.runCatching {
            AppManager.context.run {
                packageManager.getPackageInfo(packageName, flags)
            }
        }.getOrNull()
    }

    @JvmStatic
    @JvmOverloads
    fun relaunchApp(isKillProcess: Boolean = true) {
        val context = AppManager.context
        val intent =
            context.packageManager.getLaunchIntentForPackage(context.packageName) ?: return
        intent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        context.startActivity(intent)

        if (!isKillProcess) return
        Process.killProcess(Process.myPid())
        exitProcess(0)
    }

    @JvmStatic
    fun exitApp() {
        AppManager.finishAllActivity()

        Process.killProcess(Process.myPid())
        exitProcess(0)
    }

    /**
     * 当前app版本是否是首次安装
     *
     * @return
     */
    @JvmStatic
    fun isAppFirstTimeInstall(): Boolean {
        val packageInfo = getPackageInfo() ?: return false
        return packageInfo.firstInstallTime == packageInfo.lastUpdateTime
    }

    /**
     * 当前app是否是升级版本
     *
     * @return
     */
    @JvmStatic
    fun isAppUpgraded(): Boolean {
        val packageInfo = getPackageInfo() ?: return false
        return packageInfo.firstInstallTime != packageInfo.lastUpdateTime
    }

    @JvmStatic
    fun getAppVersionCode(): Long {
        val packageInfo = getPackageInfo() ?: return -1L
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            packageInfo.versionCode.toLong()
        }
    }

    @JvmStatic
    fun getAppVersionName(): String {
        val packageInfo = getPackageInfo() ?: return ""
        return packageInfo.versionName ?: ""
    }

    /**
     * Get app signatures
     * 获取应用签名信息
     *
     * @return
     */
    @JvmStatic
    fun getAppSignatures(): Array<Signature>? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val packageInfo = getPackageInfo(PackageManager.GET_SIGNING_CERTIFICATES) ?: return null
            val signingInfo: SigningInfo = packageInfo.signingInfo ?: return null
            if (signingInfo.hasMultipleSigners()) {
                signingInfo.apkContentsSigners
            } else {
                signingInfo.signingCertificateHistory
            }
        } else {
            val packageInfo = getPackageInfo(PackageManager.GET_SIGNATURES) ?: return null
            packageInfo.signatures
        }
    }

    @JvmStatic
    @JvmOverloads
    fun getAppSignaturesMd5(toLowerCase: Boolean = true): List<String> {
        val signatures = getAppSignatures() ?: return emptyList()
        return signatures.map { signature ->
            String(Hex.encodeHex(DigestUtils.md5(signature.toByteArray()), toLowerCase))
        }
    }

    @JvmStatic
    @JvmOverloads
    fun getAppSignaturesSha1(toLowerCase: Boolean = true): List<String> {
        val signatures = getAppSignatures() ?: return emptyList()
        return signatures.map { signature ->
            String(Hex.encodeHex(DigestUtils.sha1(signature.toByteArray()), toLowerCase))
        }
    }

    @JvmStatic
    @JvmOverloads
    fun getAppSignaturesSha256(toLowerCase: Boolean = true): List<String> {
        val signatures = getAppSignatures() ?: return emptyList()
        return signatures.map { signature ->
            String(Hex.encodeHex(DigestUtils.sha256(signature.toByteArray()), toLowerCase))
        }
    }

}