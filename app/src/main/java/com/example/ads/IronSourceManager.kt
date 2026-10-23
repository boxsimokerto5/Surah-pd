package com.example.ads

import android.app.Activity
import android.util.Log
import com.example.BuildConfig
import com.example.data.PreferencesManager
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.sdk.LevelPlayInterstitialListener
import com.unity3d.mediation.LevelPlayAdError
import com.unity3d.mediation.LevelPlayAdInfo
import com.unity3d.mediation.interstitial.LevelPlayInterstitialAd
import com.unity3d.mediation.interstitial.LevelPlayInterstitialAdListener

object IronSourceManager {

    private const val TAG = "IronSourceManager"
    const val SURAH_THRESHOLD = 5

    // Configured IronSource credentials
    const val DEFAULT_APP_KEY = "28f10a08d"
    const val DEFAULT_INTERSTITIAL_AD_UNIT_ID = "c9y11av4i4h024pu"

    private var isInitialized = false
    private var onAdClosedCallback: (() -> Unit)? = null
    private var levelPlayInterstitialAd: LevelPlayInterstitialAd? = null

    fun getActiveAppKey(preferencesManager: PreferencesManager): String {
        val prefKey = preferencesManager.getIronSourceAppKey()
        if (prefKey.isNotEmpty()) return prefKey
        val buildConfigKey = runCatching { BuildConfig.IRONSOURCE_APP_KEY }.getOrDefault("")
        if (buildConfigKey.isNotEmpty()) return buildConfigKey
        return DEFAULT_APP_KEY
    }

    fun getActiveAdUnitId(preferencesManager: PreferencesManager): String {
        val prefId = preferencesManager.getIronSourceAdUnitId()
        if (prefId.isNotEmpty()) return prefId
        val buildConfigId = runCatching { BuildConfig.IRONSOURCE_INTERSTITIAL_AD_UNIT_ID }.getOrDefault("")
        if (buildConfigId.isNotEmpty()) return buildConfigId
        return DEFAULT_INTERSTITIAL_AD_UNIT_ID
    }

    fun init(activity: Activity, preferencesManager: PreferencesManager) {
        val appKey = getActiveAppKey(preferencesManager)
        val adUnitId = getActiveAdUnitId(preferencesManager)

        if (appKey.isEmpty() || appKey == "YOUR_IRONSOURCE_APP_KEY") {
            Log.d(TAG, "IronSource App Key belum dikonfigurasi. Menggunakan mode placeholder.")
            return
        }

        try {
            // Setup listener legacy untuk backward compatibility
            @Suppress("DEPRECATION")
            IronSource.setLevelPlayInterstitialListener(object : LevelPlayInterstitialListener {
                override fun onAdReady(adInfo: AdInfo) {
                    Log.d(TAG, "IronSource Legacy Interstitial Ready")
                }

                override fun onAdLoadFailed(error: IronSourceError) {
                    Log.w(TAG, "IronSource Legacy Interstitial Load Failed: ${error.errorMessage}")
                }

                override fun onAdOpened(adInfo: AdInfo) {
                    Log.d(TAG, "IronSource Legacy Interstitial Opened")
                }

                override fun onAdShowSucceeded(adInfo: AdInfo) {
                    Log.d(TAG, "IronSource Legacy Interstitial Show Succeeded")
                }

                override fun onAdShowFailed(error: IronSourceError, adInfo: AdInfo) {
                    Log.e(TAG, "IronSource Legacy Interstitial Show Failed: ${error.errorMessage}")
                    onAdClosedCallback?.invoke()
                    onAdClosedCallback = null
                    loadInterstitial()
                }

                override fun onAdClicked(adInfo: AdInfo) {
                    Log.d(TAG, "IronSource Legacy Interstitial Clicked")
                }

                override fun onAdClosed(adInfo: AdInfo) {
                    Log.d(TAG, "IronSource Legacy Interstitial Closed")
                    onAdClosedCallback?.invoke()
                    onAdClosedCallback = null
                    loadInterstitial()
                }
            })

            IronSource.init(activity, appKey, object : com.ironsource.mediationsdk.sdk.InitializationListener {
                override fun onInitializationComplete() {
                    Log.d(TAG, "IronSource onInitializationComplete dengan App Key: $appKey")
                    isInitialized = true

                    // Setup LevelPlay Interstitial Ad dengan Ad Unit ID spesifik di UI thread
                    activity.runOnUiThread {
                        if (adUnitId.isNotBlank()) {
                            setupLevelPlayInterstitial(adUnitId)
                        }
                        loadInterstitial()
                    }
                }
            }, IronSource.AD_UNIT.INTERSTITIAL)
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing IronSource", e)
        }
    }

    private fun setupLevelPlayInterstitial(adUnitId: String) {
        try {
            val ad = LevelPlayInterstitialAd(adUnitId)
            ad.setListener(object : LevelPlayInterstitialAdListener {
                override fun onAdLoaded(adInfo: LevelPlayAdInfo) {
                    Log.d(TAG, "LevelPlay Interstitial Loaded: $adInfo")
                }

                override fun onAdLoadFailed(error: LevelPlayAdError) {
                    Log.w(TAG, "LevelPlay Interstitial Load Failed: $error")
                }

                override fun onAdDisplayed(adInfo: LevelPlayAdInfo) {
                    Log.d(TAG, "LevelPlay Interstitial Displayed: $adInfo")
                }

                override fun onAdDisplayFailed(error: LevelPlayAdError, adInfo: LevelPlayAdInfo) {
                    Log.e(TAG, "LevelPlay Interstitial Display Failed: $error")
                    onAdClosedCallback?.invoke()
                    onAdClosedCallback = null
                    loadInterstitial()
                }

                override fun onAdClicked(adInfo: LevelPlayAdInfo) {
                    Log.d(TAG, "LevelPlay Interstitial Clicked")
                }

                override fun onAdClosed(adInfo: LevelPlayAdInfo) {
                    Log.d(TAG, "LevelPlay Interstitial Closed")
                    onAdClosedCallback?.invoke()
                    onAdClosedCallback = null
                    loadInterstitial()
                }

                override fun onAdInfoChanged(adInfo: LevelPlayAdInfo) {}
            })
            levelPlayInterstitialAd = ad
            Log.d(TAG, "LevelPlayInterstitialAd disetup dengan Ad Unit ID: $adUnitId")
        } catch (e: Throwable) {
            Log.e(TAG, "Gagal membuat LevelPlayInterstitialAd", e)
        }
    }

    fun loadInterstitial() {
        try {
            levelPlayInterstitialAd?.loadAd()
            @Suppress("DEPRECATION")
            IronSource.loadInterstitial()
        } catch (e: Exception) {
            Log.e(TAG, "Error loading interstitial", e)
        }
    }

    /**
     * Checks if 5 surahs have been opened. If yes, shows Interstitial and resets counter to 0.
     * Always calls [onContinue] so the user experience continues smoothly.
     */
    fun onSurahOpened(
        activity: Activity,
        preferencesManager: PreferencesManager,
        onContinue: () -> Unit
    ) {
        val count = preferencesManager.incrementSurahOpenCount()
        Log.d(TAG, "Surat dibuka. Hitungan sekarang: $count / $SURAH_THRESHOLD")

        if (count >= SURAH_THRESHOLD) {
            preferencesManager.resetSurahOpenCount()

            val lpAd = levelPlayInterstitialAd
            if (isInitialized && lpAd != null && lpAd.isAdReady()) {
                Log.d(TAG, "Threshold $SURAH_THRESHOLD tercapai! Menampilkan LevelPlay Interstitial...")
                onAdClosedCallback = onContinue
                lpAd.showAd(activity)
            } else if (isInitialized && @Suppress("DEPRECATION") IronSource.isInterstitialReady()) {
                Log.d(TAG, "Threshold $SURAH_THRESHOLD tercapai! Menampilkan Legacy IronSource Interstitial...")
                onAdClosedCallback = onContinue
                @Suppress("DEPRECATION")
                IronSource.showInterstitial(activity)
            } else {
                Log.d(TAG, "Threshold $SURAH_THRESHOLD tercapai, tapi iklan belum siap. Memuat ulang iklan...")
                loadInterstitial()
                onContinue()
            }
        } else {
            onContinue()
        }
    }

    fun onResume(activity: Activity) {
        try {
            IronSource.onResume(activity)
        } catch (e: Exception) {
            Log.e(TAG, "IronSource onResume error", e)
        }
    }

    fun onPause(activity: Activity) {
        try {
            IronSource.onPause(activity)
        } catch (e: Exception) {
            Log.e(TAG, "IronSource onPause error", e)
        }
    }
}
