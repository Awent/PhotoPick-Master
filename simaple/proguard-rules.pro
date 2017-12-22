# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/Awen/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
# app javabean classes
-keep class com.awen.photo.photopick.bean.** { *; }

##---------------6.0权限 start----------------------------------
-keepattributes Annotation
-keepclassmembers class ** {
    @kr.co.namee.permissiongen.PermissionSuccess public *;
    @kr.co.namee.permissiongen.PermissionFail public *;
}
##---------------6.0权限 end----------------------------------

##---------------fresco start----------------------------------
# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}
##---------------fresco end----------------------------------
