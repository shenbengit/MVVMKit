# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#指定压缩级别
-optimizationpasses 5

#混淆时采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification

#将文件来源重命名为“SourceFile”字符串
-renamesourcefileattribute SourceFile

-keepattributes Signature, InnerClasses, EnclosingMethod
#保留行号
-keepattributes SourceFile, LineNumberTable

-keepattributes *Annotation*
-keepattributes *JavascriptInterface*

-keepclassmembers class *{
     public<init>(org.json.JSONObject);
}
-keepclassmembers enum *{
      public static **[] values();
      public static ** valueOf(java.lang.String);
}

# 不混淆native方法
-keepclasseswithmembernames class * {
    native <methods>;
}

#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# 保留 Serializable 序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 不混淆实现了parcelable接口的类成员
-keep class * implements android.os.Parcelable{
    public static final android.os.Parcelable$Creator *;
}

-keep @androidx.annotation.Keep class * {*;}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <init>(...);
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <fields>;
}

#-keepclasseswithmembers class kotlin.Metadata { public *; }
#
#-keepclassmembers class kotlin.reflect.** { public *; }
#
#-keepclassmembers class kotlin.* { public *; }