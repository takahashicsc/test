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

-keep class androidx.appcompat.widget.** { *; }
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

# for DexGuard only
-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

# Class names are needed in reflection
-keepnames class com.amazonaws.**
-keepnames class com.amazon.**

# Enums are not obfuscated correctly in combination with Gson
-keepclassmembers enum * { *; }

# Request handlers defined in request.handlers
-keep class com.amazonaws.services.**.*Handler

# The following are referenced but aren't required to run
-dontwarn com.fasterxml.jackson.**

# Android 6.0 release removes support for the Apache HTTP client
-dontwarn org.apache.http.**

# The SDK has several references of Apache HTTP client
-dontwarn com.amazonaws.http.**
-dontwarn com.amazonaws.metrics.**