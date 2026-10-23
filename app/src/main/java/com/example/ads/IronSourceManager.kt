package com.example.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.data.PreferencesManager
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.sdk.LevelPlayInterstitialListener

object IronSourceManager {

    private const val TAG = "IronSourceManager"
    const val SURAH_THRESHOLD = 5

    // Default placeholder key. User can update this anytime.
    const val DEFAULT_APP_KEY = "YOUR_IRONSOURCE_APP_KEY"

    private var isInitialized = false
    private var onAdClosedCallback: (() -> Unit)? = null

    fun init(activity: Activity, preferencesManager: PreferencesManager) {
        val appKey = preferencesManager.getIronSourceAppKey().ifEmpty { DEFAULT_APP_KEY }
        if (appKey.isEmpty() || appKey == "YOUR_IRONSOURCE_APP_KEY") {
            Log.d(TAG, "IronSource App Key belum dikonfigurasi. Menggunakan mode placeholder.")
            return
        }

        try {
            IronSource.setLevelPlayInterstitialListener(object : LevelPlayInterstitialListener {
                override fun onAdReady(adInfo: AdInfo) {
                    Log.d(TAG, "IronSource Interstitial Ready")
                }

                override fun onAdLoadFailed(error: IronSourceError) {
                    Log.w(TAG, "IronSource Interstitial Load Failed: ${error.errorMessage}")
                }

                override fun onAdOpened(adInfo: AdInfo) {
                    Log.d(TAG, "IronSource Interstitial Opened")
                }

                override fun onAdShowSucceeded(adInfo: AdInfo) {
                    Log.d(TAG, "IronSource Interstitial Show Succeeded")
                }

                override fun onAdShowFailed(error: IronSourceError, adInfo: AdInfo) {
                    Log.e(TAG, "IronSource Interstitial Show Failed: ${error.errorMessage}")
                    onAdClosedCallback?.invoke()
                    onAdClosedCallback = null
                    loadInterstitial()
                }

                override fun onAdClicked(adInfo: AdInfo) {
                    Log.d(TAG, "IronSource Interstitial Clicked")
                }

                override fun onAdClosed(adInfo: AdInfo) {
                    Log.d(TAG, "IronSource Interstitial Closed")
                    onAdClosedCallback?.invoke()
                    onAdClosedCallback = null
                    loadInterstitial()
                }
            })

            IronSource.init(activity, appKey, IronSource.AD_UNIT.INTERSTITIAL)
            isInitialized = true
            loadInterstitial()
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing IronSource", e)
        }
    }

    fun loadInterstitial() {
        try {
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
            // Reset counter back to 0
            preferencesManager.resetSurahOpenCount()

            if (isInitialized && IronSource.isInterstitialReady()) {
                Log.d(TAG, "Threshold $SURAH_THRESHOLD tercapai! Menampilkan iklan Interstitial IronSource...")
                onAdClosedCallback = onContinue
                IronSource.showInterstitial(activity)
            } else {
                Log.d(TAG, "Threshold $SURAH_THRESHOLD tercapai, tapi iklan belum siap atau key belum diisi. Melanjutkan...")
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
