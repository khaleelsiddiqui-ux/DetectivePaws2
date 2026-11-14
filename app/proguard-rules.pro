# Code shrinking is off by default (see app/build.gradle). If you turn it on, keep the JS bridge:
-keepclassmembers class com.detectivepaws.game.MainActivity$AdsBridge {
    @android.webkit.JavascriptInterface <methods>;
}
