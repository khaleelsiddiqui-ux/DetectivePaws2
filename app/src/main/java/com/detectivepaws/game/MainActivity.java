package com.detectivepaws.game;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.window.OnBackInvokedDispatcher;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Hosts the HTML5 game (app/src/main/assets/www/index.html) in a full-screen WebView
 * and connects its "watch an ad" buttons to Google AdMob.
 */
public class MainActivity extends Activity {

    private WebView webView;
    private RewardedAd rewardedAd;
    private InterstitialAd interstitialAd;
    private boolean rewardEarned = false;
    private final AtomicBoolean adsStarted = new AtomicBoolean(false);
    private ConsentInformation consentInformation;
    private TextToSpeech tts;
    private boolean ttsReady = false;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        webView.setBackgroundColor(0xFF2B2350);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);              // saves coins, best score and skins
        s.setMediaPlaybackRequiresUserGesture(false);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true; // the game never navigates away
            }
        });
        webView.addJavascriptInterface(new AdsBridge(), "AndroidAds");
        webView.addJavascriptInterface(new VoiceBridge(), "AndroidVoice");
        setupVoice();
        setContentView(webView);
        webView.loadUrl("file:///android_asset/www/index.html");

        hideSystemBars();
        setupBackButton();
        requestConsentThenStartAds();
    }

    // ---------------- Privacy consent (required by AdMob for EU/UK players) ----------------

    private void requestConsentThenStartAds() {
        consentInformation = UserMessagingPlatform.getConsentInformation(this);
        ConsentRequestParameters params = new ConsentRequestParameters.Builder().build();
        consentInformation.requestConsentInfoUpdate(
                this,
                params,
                () -> UserMessagingPlatform.loadAndShowConsentFormIfRequired(this, formError -> {
                    if (consentInformation.canRequestAds()) startAds();
                }),
                requestError -> startAds());
        if (consentInformation.canRequestAds()) startAds();
    }

    private void startAds() {
        if (!adsStarted.compareAndSet(false, true)) return;
        new Thread(() -> MobileAds.initialize(this, status -> runOnUiThread(() -> {
            loadRewarded();
            loadInterstitial();
        }))).start();
    }

    // ---------------- Rewarded ads ("Continue", "x2 Coins", "Free Coins") ----------------

    private void loadRewarded() {
        RewardedAd.load(this, getString(R.string.admob_rewarded_id), new AdRequest.Builder().build(),
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedAd ad) {
                        rewardedAd = ad;
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError error) {
                        rewardedAd = null;
                        webView.postDelayed(MainActivity.this::loadRewarded, 30000);
                    }
                });
    }

    private void showRewarded() {
        if (rewardedAd == null) {
            sendAdResult(false);
            if (adsStarted.get()) loadRewarded();
            return;
        }
        rewardEarned = false;
        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                rewardedAd = null;
                sendAdResult(rewardEarned);
                loadRewarded();
                hideSystemBars();
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError error) {
                rewardedAd = null;
                sendAdResult(false);
                loadRewarded();
            }
        });
        rewardedAd.show(this, rewardItem -> rewardEarned = true);
    }

    // ---------------- Interstitial ads (between some games) ----------------

    private void loadInterstitial() {
        InterstitialAd.load(this, getString(R.string.admob_interstitial_id), new AdRequest.Builder().build(),
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(InterstitialAd ad) {
                        interstitialAd = ad;
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError error) {
                        interstitialAd = null;
                        webView.postDelayed(MainActivity.this::loadInterstitial, 30000);
                    }
                });
    }

    private void showInterstitial() {
        if (interstitialAd == null) {
            sendAdResult(true); // no ad ready: just carry on with the game
            if (adsStarted.get()) loadInterstitial();
            return;
        }
        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                interstitialAd = null;
                sendAdResult(true);
                loadInterstitial();
                hideSystemBars();
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError error) {
                interstitialAd = null;
                sendAdResult(true);
                loadInterstitial();
            }
        });
        interstitialAd.show(this);
    }

    private void sendAdResult(boolean ok) {
        webView.evaluateJavascript("window.Ads && window.Ads._done(" + ok + ")", null);
    }

    /** Methods the game's JavaScript can call: window.AndroidAds.showRewarded() etc. */
    public class AdsBridge {
        @JavascriptInterface
        public void showRewarded() {
            runOnUiThread(MainActivity.this::showRewarded);
        }

        @JavascriptInterface
        public void showInterstitial() {
            runOnUiThread(MainActivity.this::showInterstitial);
        }
    }

    // ---------------- Character voices (the phone's text-to-speech) ----------------

    private void setupVoice() {
        tts = new TextToSpeech(this, status -> {
            if (status != TextToSpeech.SUCCESS) return;
            int r = tts.setLanguage(Locale.US);
            if (r == TextToSpeech.LANG_MISSING_DATA || r == TextToSpeech.LANG_NOT_SUPPORTED) {
                r = tts.setLanguage(Locale.ENGLISH);
            }
            ttsReady = r != TextToSpeech.LANG_MISSING_DATA && r != TextToSpeech.LANG_NOT_SUPPORTED;
            // Tell the game when each line starts, each word as it is spoken, and when it ends (for lip sync).
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String id) { talkEvent("start", id, 0, 0); }

                @Override
                public void onDone(String id) { talkEvent("done", id, 0, 0); }

                @Override
                @SuppressWarnings("deprecation")
                public void onError(String id) { talkEvent("done", id, 0, 0); }

                @Override
                public void onStop(String id, boolean interrupted) { talkEvent("done", id, 0, 0); }

                @Override
                public void onRangeStart(String id, int start, int end, int frame) { talkEvent("word", id, start, end); }
            });
        });
    }

    private void talkEvent(String event, String id, int start, int end) {
        if (id == null || !id.matches("u\\d+")) return;
        runOnUiThread(() -> webView.evaluateJavascript(
                "window.__talk && window.__talk('" + event + "','" + id + "'," + start + "," + end + ")", null));
    }

    /** Methods the game's JavaScript can call: window.AndroidVoice.speak(text, pitch, rate, queue, id). */
    public class VoiceBridge {
        @JavascriptInterface
        public void speak(String text, float pitch, float rate, boolean queue, String id) {
            runOnUiThread(() -> {
                if (!ttsReady || tts == null) return;
                tts.setPitch(pitch);
                tts.setSpeechRate(rate);
                tts.speak(text, queue ? TextToSpeech.QUEUE_ADD : TextToSpeech.QUEUE_FLUSH, null, id);
            });
        }

        @JavascriptInterface
        public void stop() {
            runOnUiThread(() -> {
                if (tts != null) tts.stop();
            });
        }
    }

    // ---------------- Full screen + back button ----------------

    private void hideSystemBars() {
        if (Build.VERSION.SDK_INT >= 30) {
            getWindow().setDecorFitsSystemWindows(false);
            WindowInsetsController c = getWindow().getInsetsController();
            if (c != null) {
                c.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                c.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) hideSystemBars();
    }

    private void setupBackButton() {
        if (Build.VERSION.SDK_INT >= 33) {
            getOnBackInvokedDispatcher().registerOnBackInvokedCallback(
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT, this::handleBack);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onBackPressed() {
        handleBack(); // Android 12 and older
    }

    private void handleBack() {
        webView.evaluateJavascript(
                "window.onBackPressedFromApp ? window.onBackPressedFromApp() : 'exit'",
                value -> {
                    if (value == null || !value.contains("handled")) finish();
                });
    }

    @Override
    protected void onPause() {
        if (tts != null) tts.stop();
        webView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        hideSystemBars();
    }

    @Override
    protected void onDestroy() {
        if (tts != null) tts.shutdown();
        webView.destroy();
        super.onDestroy();
    }
}
