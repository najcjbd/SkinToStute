# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
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

# Keep classes for skin-to-statue conversion
-keep class com.skintostatue.android.core.** { *; }
-keep class com.skintostatue.android.core.model.** { *; }
-keep class com.skintostatue.android.core.processor.** { *; }
-keep class com.skintostatue.android.core.generator.** { *; }

# Keep Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Keep AndroidX Compose
-keep class androidx.compose.** { *; }
-keep interface androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Keep Gson serialization
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Keep NBT library
-keep class com.tadukoo.jnbt.** { *; }
-dontwarn com.tadukoo.jnbt.**

# Keep OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# Keep Coil image loading
-keep class coil.** { *; }
-dontwarn coil.**

# Keep data store generated classes
-keep class androidx.datastore.** { *; }
-dontwarn androidx.datastore.**

# Preserve line numbers for debugging
-keepattributes SourceFile,LineNumberTable