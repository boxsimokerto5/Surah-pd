package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.LastRead
import com.example.model.ReadingSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("quran_surat_pendek_prefs", Context.MODE_PRIVATE)

    private val _lastReadFlow = MutableStateFlow<LastRead?>(loadLastRead())
    val lastReadFlow: StateFlow<LastRead?> = _lastReadFlow.asStateFlow()

    private val _bookmarksFlow = MutableStateFlow<Set<String>>(loadBookmarks())
    val bookmarksFlow: StateFlow<Set<String>> = _bookmarksFlow.asStateFlow()

    private val _settingsFlow = MutableStateFlow(loadSettings())
    val settingsFlow: StateFlow<ReadingSettings> = _settingsFlow.asStateFlow()

    private fun loadLastRead(): LastRead? {
        val surahNum = prefs.getInt(KEY_LAST_SURAH_NUM, -1)
        if (surahNum == -1) return null
        val surahName = prefs.getString(KEY_LAST_SURAH_NAME, "") ?: ""
        val surahArabic = prefs.getString(KEY_LAST_SURAH_ARABIC, "") ?: ""
        val ayahNum = prefs.getInt(KEY_LAST_AYAH_NUM, 1)
        val time = prefs.getLong(KEY_LAST_TIMESTAMP, System.currentTimeMillis())
        return LastRead(surahNum, surahName, surahArabic, ayahNum, time)
    }

    fun saveLastRead(surahNum: Int, surahName: String, surahArabic: String, ayahNum: Int) {
        val now = System.currentTimeMillis()
        prefs.edit()
            .putInt(KEY_LAST_SURAH_NUM, surahNum)
            .putString(KEY_LAST_SURAH_NAME, surahName)
            .putString(KEY_LAST_SURAH_ARABIC, surahArabic)
            .putInt(KEY_LAST_AYAH_NUM, ayahNum)
            .putLong(KEY_LAST_TIMESTAMP, now)
            .apply()

        _lastReadFlow.value = LastRead(surahNum, surahName, surahArabic, ayahNum, now)
    }

    private fun loadBookmarks(): Set<String> {
        return prefs.getStringSet(KEY_BOOKMARKS, emptySet()) ?: emptySet()
    }

    fun toggleBookmark(surahNum: Int, ayahNum: Int): Boolean {
        val key = "$surahNum:$ayahNum"
        val current = _bookmarksFlow.value.toMutableSet()
        val isAdded: Boolean
        if (current.contains(key)) {
            current.remove(key)
            isAdded = false
        } else {
            current.add(key)
            isAdded = true
        }
        prefs.edit().putStringSet(KEY_BOOKMARKS, current).apply()
        _bookmarksFlow.value = current
        return isAdded
    }

    fun isBookmarked(surahNum: Int, ayahNum: Int): Boolean {
        return _bookmarksFlow.value.contains("$surahNum:$ayahNum")
    }

    private fun loadSettings(): ReadingSettings {
        val fontSize = prefs.getFloat(KEY_FONT_SIZE, 28f)
        val latin = prefs.getBoolean(KEY_SHOW_LATIN, true)
        val translation = prefs.getBoolean(KEY_SHOW_TRANSLATION, true)
        val autoPlay = prefs.getBoolean(KEY_AUTO_PLAY, true)
        return ReadingSettings(
            arabicFontSizeSp = fontSize,
            showLatin = latin,
            showTranslation = translation,
            autoPlayNextAyah = autoPlay
        )
    }

    fun updateSettings(settings: ReadingSettings) {
        prefs.edit()
            .putFloat(KEY_FONT_SIZE, settings.arabicFontSizeSp)
            .putBoolean(KEY_SHOW_LATIN, settings.showLatin)
            .putBoolean(KEY_SHOW_TRANSLATION, settings.showTranslation)
            .putBoolean(KEY_AUTO_PLAY, settings.autoPlayNextAyah)
            .apply()
        _settingsFlow.value = settings
    }

    fun getSurahOpenCount(): Int {
        return prefs.getInt(KEY_SURAH_OPEN_COUNTER, 0)
    }

    fun incrementSurahOpenCount(): Int {
        val count = getSurahOpenCount() + 1
        prefs.edit().putInt(KEY_SURAH_OPEN_COUNTER, count).apply()
        return count
    }

    fun resetSurahOpenCount() {
        prefs.edit().putInt(KEY_SURAH_OPEN_COUNTER, 0).apply()
    }

    fun getIronSourceAppKey(): String {
        return prefs.getString(KEY_IRONSOURCE_APP_KEY, "") ?: ""
    }

    fun saveIronSourceAppKey(key: String) {
        prefs.edit().putString(KEY_IRONSOURCE_APP_KEY, key.trim()).apply()
    }

    fun getIronSourceAdUnitId(): String {
        return prefs.getString(KEY_IRONSOURCE_AD_UNIT_ID, "") ?: ""
    }

    fun saveIronSourceAdUnitId(id: String) {
        prefs.edit().putString(KEY_IRONSOURCE_AD_UNIT_ID, id.trim()).apply()
    }

    companion object {
        private const val KEY_LAST_SURAH_NUM = "last_surah_num"
        private const val KEY_LAST_SURAH_NAME = "last_surah_name"
        private const val KEY_LAST_SURAH_ARABIC = "last_surah_arabic"
        private const val KEY_LAST_AYAH_NUM = "last_ayah_num"
        private const val KEY_LAST_TIMESTAMP = "last_timestamp"

        private const val KEY_BOOKMARKS = "bookmarked_ayahs"

        private const val KEY_FONT_SIZE = "setting_font_size"
        private const val KEY_SHOW_LATIN = "setting_show_latin"
        private const val KEY_SHOW_TRANSLATION = "setting_show_translation"
        private const val KEY_AUTO_PLAY = "setting_auto_play"

        private const val KEY_SURAH_OPEN_COUNTER = "surah_open_counter"
        private const val KEY_IRONSOURCE_APP_KEY = "ironsource_app_key"
        private const val KEY_IRONSOURCE_AD_UNIT_ID = "ironsource_ad_unit_id"
    }
}
