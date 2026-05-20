# Keep Kotlin serialization
-keepattributes *Annotation*
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.froyder.personaltrainer.**$$serializer { *; }
-keepclassmembers class com.froyder.personaltrainer.** {
    *** Companion;
}

# Keep Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# Keep Ktor
-keep class io.ktor.** { *; }

# Keep gitlive Firebase
-keep class dev.gitlive.** { *; }

# Keep data models
-keep class com.froyder.personaltrainer.data.** { *; }

# Ktor debug detector - not available on Android
-dontwarn java.lang.management.ManagementFactory
-dontwarn java.lang.management.RuntimeMXBean
-dontwarn io.ktor.util.debug.**