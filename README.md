# MVVMKit
使用Kotlin搭建Android MVVM快速开发框架。

## 引入
`注意：2.x.x版本与1.x.x版本不兼容`

### 将JitPack存储库添加到您的项目中(项目根目录下build.gradle文件)
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
### 添加依赖(项目目录下build.gradle文件)
[![](https://jitpack.io/v/shenbengit/MVVMKit.svg)](https://jitpack.io/#shenbengit/MVVMKit)
> 从v1.1.0版本开始，**minSdkVersion**调整为**21**，分支[1.0.x](https://github.com/shenbengit/MVVMKit/tree/1.0.x)版本**minSdkVersion**为**19**； 
```gradle
plugins {
    ...
    id 'kotlin-kapt'
}

android{
    ...
    buildFeatures {
        //启用databinding
        dataBinding = true
    }
}

dependencies {
    implementation 'com.github.shenbengit:MVVMKit:Tag'
}
```

## 演示用例
详见[SrsRtcAndroidClient](https://github.com/shenbengit/SrsRtcAndroidClient)

> 如果您想快速使用，您需要先了解Kotlin语法、Databinding、Koin等相关知识。

## 项目中集成的三方组件

### Android Jetpack 
- [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle):生命周期感知型组件可执行操作来响应另一个组件（如 Activity 和 Fragment）的生命周期状态的变化；
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata):LiveData 是一种可观察的数据存储器类。与常规的可观察类不同，LiveData 具有生命周期感知能力，意指它遵循其他应用组件（如 Activity、Fragment 或 Service）的生命周期；
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel):ViewModel 类旨在以注重生命周期的方式存储和管理界面相关的数据；
### 网络请求
- [okhttp](https://github.com/square/okhttp):OkHttp is an HTTP client that’s efficient;
- [Retrofit](https://github.com/square/retrofit):A type-safe HTTP client for Android and Java;
### 依赖注入
- [koin](https://github.com/InsertKoinIO/koin):一个实用的轻量级Kotlin依赖注入框架；
### 其他
- [Moshi](https://github.com/square/moshi):Moshi是一个适用于Android、Java和Kotlin的JSON 库;
- [Glide](https://github.com/bumptech/glide):一个适用于Android的图像加载和缓存库，专注于平滑滚动；
- [coroutines](https://github.com/Kotlin/kotlinx.coroutines):Kotlin coroutines;
- [MMKV](https://github.com/Tencent/MMKV):MMKV是基于 mmap 内存映射的 key-value 组件，底层序列化/反序列化使用 protobuf 实现，性能高，稳定性强，代替SharedPreferences；
- [SFragmentation](https://github.com/weikaiyun/SFragmentation):框架负责管理fragment的各种操作，相比于google新出的navigation框架，更加灵活多变，易于使用；
- [Toasty](https://github.com/GrenderG/Toasty):吐司；
- [LoadingDialog](https://github.com/shenbengit/LoadingDialog):Android LoadingDialog;
- [BRV](https://github.com/liangjingkanji/BRV):Android 快速构建 RecyclerView, 比 BRVAH 更简单强大;

## 快速使用
### 初始化
建议在**Application**中执行.
```kotlin
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        InitEnvironment.init(this, object : InitEnvironment.ConfigurationEnvironment {

            override val debug: Boolean
                get() = BuildConfig.DEBUG

            override val mmkvMode: Int
                get() = super.mmkvMode

            override val mmkvCryptKey: String?
                get() = super.mmkvCryptKey

            override fun logV(tag: String, msg: () -> Any) {
                Log.v(tag, msg().toString())
            }

            override fun logD(tag: String, msg: () -> Any) {
                if (debug) {
                    Log.d(tag, msg().toString())
                }
            }

            override fun logI(tag: String, msg: () -> Any) {
                Log.i(tag, msg().toString())
            }

            override fun logW(tag: String, msg: () -> Any) {
                Log.w(tag, msg().toString())
            }

            override fun logE(tag: String, msg: () -> Any) {
                Log.e(tag, msg().toString())
            }
        }) {
            val koinApplication =
                KoinAndroidApplication
                    .create(
                        this,
                        if (BuildConfig.DEBUG) Level.ERROR else Level.ERROR
                    )
                    .modules(appModule)
            globalInit(koinApplication)

//            initToasty()
//            initMMKV()
//            initFragmentation()
//            initKoin(koinApplication)
        }
    }
}
```
### 基类
#### BaseSupportActivity    
一般来说我们自己的项目都会有自己的**BaseActivity**用于封装基础逻辑，所以需要我们的基类要继承**BaseSupportActivity**，自定义相关方法。
```kotlin
abstract class BaseActivity<VM : BaseViewModel<out IRepository>, VDB : ViewDataBinding> :
    BaseSupportActivity<VM, VDB>(), CustomAdapt {

    override fun isBaseOnWidth(): Boolean {
        return true
    }

    override fun getSizeInDp(): Float {
        return Constant.DEFAULT_SIZE_IN_DP
    }

    override fun initLoadingDialog(): Dialog {
        return LoadingDialog.builder(this)
            .setHintText(getString(R.string.loading))
            .create()
    }
}
```
使用示例
```kotlin
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    /**
     * 如果不重写，将反射获取
     */
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

    }

    override fun initData(savedInstanceState: Bundle?) {

    }
}
```
#### BaseSupportFragment    
同BaseSupportActivity.
```kotlin
abstract class BaseFragment<VM : BaseViewModel<out IRepository>, VDB : ViewDataBinding> :
    BaseSupportFragment<VM, VDB>(), CustomAdapt {
    
    override fun isBaseOnWidth(): Boolean {
        return true
    }

    override fun getSizeInDp(): Float {
        return Constant.DEFAULT_SIZE_IN_DP
    }

    override fun initLoadingDialog(): Dialog {
        return LoadingDialog.builder(requireContext())
            .setHintText(getString(R.string.loading))
            .create()
    }
}
```
使用示例
```kotlin
class TestFragment : BaseFragment<DefaultViewModel, FragmentTestBinding>() {

    /**
     * 如果不重写，将反射获取
     */
//    override fun getLayoutId(): Int {
//        return R.layout.fragment_test
//    }

    /**
     * 注入ViewModel
     * @see [viewModel]
     * @see [sharedViewModel]
     * @see [sharedStateViewModel]
     */
    override fun injectViewModel(): Lazy<DefaultViewModel> {
        return viewModel()
    }

    override fun getViewModelId(): Int {
        return BR.viewModel
    }

    override fun initView() {
    }

    override fun initData(savedInstanceState: Bundle?) {
    }
}
```
#### BaseViewModel    
ViewMode的基类，感知View生命周期；    
```kotlin
class MainViewModel(
    application: Application,
    repo: BaseNothingRepository
) : BaseViewModel<BaseNothingRepository>(application, repo) {

    override fun onCreate(owner: LifecycleOwner) {
        ...
    }
    
    override fun onDestroy(owner: LifecycleOwner) {
        ...
    }
}
```
> 如果你的ViewModel中不需要写相关逻辑，比较简单，则可以用**DefaultViewModel**进行占位。

#### BaseRepository
数据仓库基类，在创建BaseViewModel需要使用，自行创建并继承相关类即可；
```kotlin
/**
 * 网络请求和本地数据库均需要使用的情况
 */
open class BaseBothRepository<T : IRemoteDataSource, R : ILocalDataSource>(
    protected val remoteDataSource: T,
    protected val localDataSource: R
) : BaseRepository()

/**
 * 仅使用本地数据库
 */
open class BaseLocalRepository<T : ILocalDataSource>(
    protected val remoteDataSource: T
) : BaseRepository()

/**
 * 仅使用网络请求
 */
open class BaseRemoteRepository<T : IRemoteDataSource>(
    protected val remoteDataSource: T
) : BaseRepository()

/**
 * 不需要数据
 */
class BaseNothingRepository : BaseRepository()
```
> 如果你没有数据请求相关操作，则可以用**BaseNothingRepository**进行占位。

### 网络请求（Retrofit + Coroutines）
#### BaseRetrofitClient  
自定义RetrofitClient，示例：
```kotlin
class RetrofitClient : BaseRetrofitClient() {

    companion object {
        private const val TAG = "RetrofitClient"
        private const val DEFAULT_MILLISECONDS: Long = 30
    }

    private lateinit var apiService: ApiService

    override fun generateOkHttpBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        val interceptor = HttpLoggerInterceptor { message -> logI(TAG) { message } }
        interceptor.level = HttpLoggerInterceptor.Level.BODY

        return builder.readTimeout(DEFAULT_MILLISECONDS, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.SECONDS)
            .connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
    }

    override fun generateRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        return builder.apply {
            val moshi = Moshi.Builder()
                .addLast(NullSafeKotlinJsonAdapterFactory())
                .addLast(NullSafeStandardJsonAdapters.FACTORY)
                .build()
            addConverterFactory(MoshiConverterFactory.create(moshi))
        }
    }

    /**
     * 动态修改Retrofit-baseUrl
     */
    fun setBaseUrl(baseUrl: String) {
        apiService = getApiService(ApiService::class.java, baseUrl, false)
    }

    fun getApiService(): ApiService {
        if (this::apiService.isInitialized.not()) {
            setBaseUrl("https://api.github.com/")
        }
        return apiService
    }

}
```
#### 文件下载（DownloadRetrofitClient）
支持md5文件校验，暂不支持断点续传；    
使用示例：
```kotlin
lifecycleScope.launch {
    // 1
    downloadFile(url = "", filePath = "", md5 = "") {
        start {

        }
        progress { totalSize, downloadSize, progress ->

        }
        success { file, fileMd5, md5VerifySuccess ->

        }
        error {

        }
    }.startDownload()

    // 2
    DownloadFile(url = "", filePath = "", md5 = "").start {

    }.progress { totalSize, downloadSize, progress ->

    }.success { file, fileMd5, md5VerifySuccess ->

    }.error {

    }.startDownload()
}
```
#### BaseResponse（网络请求接口返回继承基类）
这个是在BaseViewModel中快速使用网络请求所用，不强制使用。    
一般来说网络请求结果返回都会有对应的状态返回，如成功、失败、错误等。    
这样在返回结果实体bean中去继承此类，则可以快速执行条件判断，如下：
```kotlin
open class ApiResponse<T>(
    @Json(name = "code")
    val code: Int,
    @Json(name = "data")
    val data: T?,
    @Json(name = "msg")
    val msg: String
) : BaseResponse<T> {

    override fun isSuccess(): Boolean {
        return code == 200
    }

    override fun getResponseCode(): Int {
        return code
    }

    override fun getResponseMsg(): String {
        return msg
    }

    override fun getResponseData(): T? {
        return data
    }
}
```
```kotlin
data class UserInfoBean(
    @Json(name = "createdAt")
    val createdAt: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "userId")
    val userId: String,
    @Json(name = "userType")
    val userType: String,
    @Json(name = "username")
    val username: String
)
```
```kotlin
interface ApiService {
    /**
     * 获取所有用户信息
     * 仅查询客户端；
     * query clients only.
     */
    @GET("/srs_rtc/user/getAllUserInfo")
    suspend fun getAllUser(): ApiResponse<List<UserInfoBean>>
}
```
```kotlin
class MainViewModel(
    application: Application,
    repo: BaseNothingRepository
) : BaseViewModel<BaseNothingRepository>(application, repo) {

    private val retrofitClient : RetrofitClient by inject()
    
    override fun onCreate(owner: LifecycleOwner) {
        httpRequest({
            retrofitClient.getApiService().getAllUser()
        }, onSuccess = {
            //请求成功
            val data = it.data
            if (data == null) {//之所以判断是否为null，是因为考虑到有的网络请求返回值是只有状态值，而没有内容。
                toastWarning("userList is null.")
                return@httpRequest
            }
            // 如果确保data不可能为null，则可使用
//            val data = it.requireData
            analyticalData(data, unSelectedSet)
        }, onFailure = {
            //请求失败
            XLog.w("getAllUser failed,code:${it.code}, msg: ${it.msg}")
            toastWarning("getAllUser failed: ${it.msg}")
        }, onError = {
            //网络请求错误
            XLog.w("getAllUser error: ${it.throwable.message}")
            toastWarning("getAllUser error: ${it.throwable.message}")
        }, isShowLoadingDialog = true)
    }
}
```
#### httpRequest 
网络请求快速调用，返回类需要实现[BaseResponse](https://github.com/shenbengit/MVVMKit/blob/master/lib/src/main/java/com/shencoder/mvvmkit/http/bean/BaseResponse.kt)接口，用例见上；    
对CoroutineScope进行了扩展，可以快速使用，同时也对**BaseSupportActivity**、**BaseSupportFragment**、**BaseViewModel**，并且支持快速显示LoadingDialog
```kotlin
fun <T> CoroutineScope.httpRequest(
    block: suspend CoroutineScope.() -> BaseResponse<T>,
    onStart: () -> Unit = {},
    onSuccess: (ResultStatus.Success<T>) -> Unit = {},
    onFailure: (ResultStatus.Failure) -> Unit = {},
    onError: (ResultStatus.Error) -> Unit = {},
    onComplete: () -> Unit = {}
) = launch(Dispatchers.Main) {
    onStart()

    val result = runCatching { block() }

    result.fold({
        if (it.isSuccess()) {
            onSuccess(ResultStatus.onSuccess(it.getResponseData()))
        } else {
            //处理公共的失败事件
            val handled = DefaultHttpRequestHandler.defaultHttpRequestFailure?.invoke(
                it.getResponseCode(),
                it.getResponseMsg()
            ) ?: false
            if (!handled) {
                // 如果公共事件处理了，则不回调
                onFailure(ResultStatus.onFailure(it.getResponseCode(), it.getResponseMsg()))
            }
        }
    }, {
        //处理公共的异常事件
        DefaultHttpRequestHandler.defaultHttpRequestError?.invoke(it)
        onError(ResultStatus.onError(it))
    })

    onComplete()
}
```


#### DefaultHttpRequestHandler 
公共处理网络请求失败的方式，需搭配[httpRequest](https://github.com/shenbengit/MVVMKit/blob/83b7991e36372e6d3ef90945c010b2f903958fb7/lib/src/main/java/com/shencoder/mvvmkit/ext/CoroutineScopeExt.kt#L27)使用
```kotlin
// 默认处理公共的失败结果
// 如果返回true，则不会回调[httpRequest]里的[onFailure]函数
DefaultHttpRequestHandler.defaultHttpRequestFailure = { code, msg ->
    when (code) {
        401 -> {
            // 跳转到登录页

            true
        }

        else -> false
    }
}
// 默认处理异常的失败结果
DefaultHttpRequestHandler.defaultHttpRequestError = { error ->

}
```


### Koin(依赖注入)
这一部分需要你先去了解Koin相关知识（比Dagger简单多了）    
本库中在初始化时已经默认加入了一些要默认注入的库：
```kotlin
private val defaultModule = module {
    factory { BaseNothingRepository() }
    viewModel { DefaultViewModel(get(), get()) }
}

/**
 * 单例模式
 */
private val singleModule = module {
    //默认加入了MMKV
    single { MMKV.defaultMMKV() }
    single { DownloadRetrofitClient() }
}

internal val appModule = listOf(defaultModule, singleModule)
```
比如说上面的RetrofitClient，我们就可以这样：
```kotlin
private val singleModule = module {
    single { RetrofitClient() }
}
val appModule = mutableListOf(singleModule)
```
用的时候直接这样：
```kotlin
private val retrofitClient : RetrofitClient by inject()
```
其他更多示例，请看[SrsRtcAndroidClient](https://github.com/shenbengit/SrsRtcAndroidClient)、[AppModule](https://github.com/shenbengit/SrsRtcAndroidClient/blob/master/app/src/main/java/com/shencoder/srs_rtc_android_client/di/AppModule.kt)、[ViewModelModule](https://github.com/shenbengit/SrsRtcAndroidClient/blob/master/app/src/main/java/com/shencoder/srs_rtc_android_client/di/ViewModelModule.kt)，代码是最好的老师。

### Databinding
本库中封装了一些Databinding中常用的一些方法，具体可以看下[DataBindingAdapter](https://github.com/shenbengit/MVVMKit/blob/master/lib/src/main/java/com/shencoder/mvvmkit/binding/DataBindingAdapter.kt)，方便快速开发。

### 其他
#### Toast(吐司)扩展方法
这一步对Toast进行了扩展，在Activity、Fragment、Dialog、ViewModel中可以直接使用，如：
```kotlin
toastError("error")
toastSuccess("success")
toastWarning("warning")
toastNormal("normal")
```

#### dp、sp转px
全局即可调用；    
```
// dp转px
dp2px(25f)
// sp转px
sp2px(12f)
// 
R.dimen.cardview_compat_inset_shadow.getDimensionPixelSize()
```

#### 点击防抖
全局防抖
```
btn.clickWithTrigger { 

}
```
#### 保存图片到相册
详见[SaveImageExt](https://github.com/shenbengit/MVVMKit/blob/master/lib/src/main/java/com/shencoder/mvvmkit/ext/SaveImageExt.kt)
#### 快速创建ShapeDrawable
详见[ShapeDrawableExt](https://github.com/shenbengit/MVVMKit/blob/master/lib/src/main/java/com/shencoder/mvvmkit/ext/ShapeDrawableExt.kt)
#### 16进制、md5、加解密方法
从[commons-codec](https://github.com/apache/commons-codec)复制来的，版本v1.7.1;    
详见[codec](https://github.com/shenbengit/MVVMKit/tree/master/lib/src/main/java/com/shencoder/mvvmkit/util/codec) 目录

#### 日志
全局即可调用；    
使用函数来传递message，这样有个好处，可以控制在不执行是，message函数不执行，比如在开发者模式打印堆栈信息，在生成模式不打印；
```
logV(TAG) { "message" }
logD(TAG) { "message" }
logI(TAG) { "message" }
logW(TAG) { "message" }
logE(TAG) { "message" }

```
#### NullSafeMoshi
详见[moshi](https://github.com/shenbengit/MVVMKit/tree/master/lib/src/main/java/com/shencoder/mvvmkit/util/moshi) 目录和[NullSafeMoshiUtils](https://github.com/shenbengit/MVVMKit/blob/master/lib/src/main/java/com/shencoder/mvvmkit/util/NullSafeMoshiUtils.kt)    

#### NetworkObserverManager 
网络监听，**BaseSupportActivity**、**BaseSupportFragment**已经默认添加了监听，只需重写**onConnectivityChange**方法即可
```
val listener = NetworkObserverManager.Listener { isOnline ->
    // 是否有网
}
NetworkObserverManager.getInstance().addListener(listener)

NetworkObserverManager.getInstance().removeListener(listener)
```
#### AppManager
管理Activity和App前后台监听
详见[AppManager](https://github.com/shenbengit/MVVMKit/blob/master/lib/src/main/java/com/shencoder/mvvmkit/util/AppManager.kt)

#### 其他扩展和工具类
还有一些功能方法封装，如MoshiUtil、Nv21ToBitmap等，具体在[ext](https://github.com/shenbengit/MVVMKit/tree/master/lib/src/main/java/com/shencoder/mvvmkit/ext)和[util](https://github.com/shenbengit/MVVMKit/tree/master/lib/src/main/java/com/shencoder/mvvmkit/util)包下，可自行查看。

# [LICENSE](https://github.com/shenbengit/MVVMKit/blob/master/LICENSE)


