package com.example.ui

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.MurottalAudioPlayer
import com.example.data.PreferencesManager
import com.example.data.QuranDatabase
import com.example.model.AudioPlayingInfo
import com.example.model.Ayah
import com.example.model.LastRead
import com.example.model.ReadingSettings
import com.example.model.RevelationType
import com.example.model.Surah
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    data object Splash : Screen()
    data object Home : Screen()
    data class SurahDetail(val surahNumber: Int, val targetAyahNumber: Int? = null) : Screen()
    data object DoaKhatam : Screen()
}

enum class FilterCategory {
    ALL,
    BOOKMARKED,
    MAKKIYAH,
    MADANIYAH
}

class QuranViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesManager = PreferencesManager(application)
    val audioPlayer = MurottalAudioPlayer(application, viewModelScope)

    val lastRead: StateFlow<LastRead?> = preferencesManager.lastReadFlow
    val bookmarks: StateFlow<Set<String>> = preferencesManager.bookmarksFlow
    val settings: StateFlow<ReadingSettings> = preferencesManager.settingsFlow
    val playbackInfo: StateFlow<AudioPlayingInfo?> = audioPlayer.playbackInfo

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Splash)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow(FilterCategory.ALL)
    val selectedFilter: StateFlow<FilterCategory> = _selectedFilter.asStateFlow()

    private val _userMessage = MutableSharedFlow<String>()
    val userMessage: SharedFlow<String> = _userMessage.asSharedFlow()

    private val allSurahs = QuranDatabase.getSurahs(application)

    fun getSurah(number: Int): Surah? = allSurahs.find { it.number == number }

    val filteredSurahs: StateFlow<List<Surah>> = combine(
        _searchQuery,
        _selectedFilter,
        bookmarks
    ) { query, filter, bookmarkedSet ->
        allSurahs.filter { surah ->
            val matchesQuery = if (query.isBlank()) {
                true
            } else {
                surah.nameLatin.contains(query, ignoreCase = true) ||
                        surah.nameArabic.contains(query, ignoreCase = true) ||
                        surah.meaningId.contains(query, ignoreCase = true) ||
                        surah.number.toString() == query.trim()
            }

            val matchesFilter = when (filter) {
                FilterCategory.ALL -> true
                FilterCategory.MAKKIYAH -> surah.revelationType == RevelationType.MAKKIYAH
                FilterCategory.MADANIYAH -> surah.revelationType == RevelationType.MADANIYAH
                FilterCategory.BOOKMARKED -> {
                    bookmarkedSet.any { it.startsWith("${surah.number}:") }
                }
            }

            matchesQuery && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), allSurahs)

    init {
        audioPlayer.autoPlayNext = settings.value.autoPlayNextAyah
    }

    val preferences = preferencesManager

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun openSurah(activity: android.app.Activity, surahNumber: Int, targetAyahNumber: Int? = null) {
        com.example.ads.IronSourceManager.onSurahOpened(activity, preferencesManager) {
            navigateTo(Screen.SurahDetail(surahNumber, targetAyahNumber))
        }
    }

    fun updateIronSourceKey(activity: android.app.Activity, key: String) {
        preferencesManager.saveIronSourceAppKey(key)
        com.example.ads.IronSourceManager.init(activity, preferencesManager)
        viewModelScope.launch {
            _userMessage.emit("IronSource App Key berhasil diperbarui")
        }
    }

    fun onSplashFinished() {
        if (_currentScreen.value is Screen.Splash) {
            _currentScreen.value = Screen.Home
        }
    }

    fun navigateBack(): Boolean {
        if (_currentScreen.value !is Screen.Home && _currentScreen.value !is Screen.Splash) {
            _currentScreen.value = Screen.Home
            return true
        }
        return false
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onFilterSelected(filter: FilterCategory) {
        _selectedFilter.value = filter
    }

    fun markAsLastRead(surah: Surah, ayahNumber: Int) {
        preferencesManager.saveLastRead(
            surahNum = surah.number,
            surahName = surah.nameLatin,
            surahArabic = surah.nameArabic,
            ayahNum = ayahNumber
        )
        viewModelScope.launch {
            _userMessage.emit("Ditandai: ${surah.nameLatin} ayat $ayahNumber sebagai Terakhir Dibaca")
        }
    }

    fun toggleBookmark(surah: Surah, ayahNumber: Int) {
        val added = preferencesManager.toggleBookmark(surah.number, ayahNumber)
        viewModelScope.launch {
            val msg = if (added) {
                "Ayat $ayahNumber ditambahkan ke Favorit"
            } else {
                "Ayat $ayahNumber dihapus dari Favorit"
            }
            _userMessage.emit(msg)
        }
    }

    fun isAyahBookmarked(surahNumber: Int, ayahNumber: Int): Boolean {
        return bookmarks.value.contains("$surahNumber:$ayahNumber")
    }

    fun isLastRead(surahNumber: Int, ayahNumber: Int): Boolean {
        val lr = lastRead.value ?: return false
        return lr.surahNumber == surahNumber && lr.ayahNumber == ayahNumber
    }

    fun playAyahAudio(surah: Surah, ayahNumber: Int) {
        audioPlayer.playAyah(surah, ayahNumber)
    }

    fun playFullSurahAudio(surah: Surah) {
        audioPlayer.playFullSurah(surah)
    }

    fun toggleAudioPlayPause() {
        audioPlayer.togglePlayPause()
    }

    fun playNextAyah() {
        audioPlayer.playNext()
    }

    fun playPreviousAyah() {
        audioPlayer.playPrevious()
    }

    fun stopAudio() {
        audioPlayer.stop()
    }

    fun updateSettings(newSettings: ReadingSettings) {
        preferencesManager.updateSettings(newSettings)
        audioPlayer.autoPlayNext = newSettings.autoPlayNextAyah
    }

    fun copyAyah(surah: Surah, ayah: Ayah) {
        val textToCopy = buildString {
            append("${ayah.arabic}\n\n")
            append("${ayah.latin}\n\n")
            append("\"${ayah.indonesian}\"\n")
            append("(QS. ${surah.nameLatin}: ${ayah.numberInSurah})")
        }
        val clipboard = getApplication<Application>().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Ayat Al-Quran", textToCopy)
        clipboard.setPrimaryClip(clip)
        viewModelScope.launch {
            _userMessage.emit("Ayat ${ayah.numberInSurah} berhasil disalin ke papan klip")
        }
    }

    fun shareAyah(surah: Surah, ayah: Ayah) {
        val shareText = buildString {
            append("${ayah.arabic}\n\n")
            append("${ayah.latin}\n\n")
            append("\"${ayah.indonesian}\"\n\n")
            append("(QS. ${surah.nameLatin}: ${ayah.numberInSurah})\n")
            append("Dibagikan melalui aplikasi Surat Pendek Al-Quran")
        }
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "QS. ${surah.nameLatin}: ${ayah.numberInSurah}")
            putExtra(Intent.EXTRA_TEXT, shareText)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val chooser = Intent.createChooser(intent, "Bagikan Ayat Al-Qur'an").apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        getApplication<Application>().startActivity(chooser)
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.close()
    }
}
