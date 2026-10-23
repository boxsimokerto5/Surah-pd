package com.example.model

enum class RevelationType(val displayName: String) {
    MAKKIYAH("Makkiyah"),
    MADANIYAH("Madaniyah")
}

data class Ayah(
    val numberInSurah: Int,
    val globalNumber: Int,
    val arabic: String,
    val latin: String,
    val indonesian: String,
    val audioUrl: String
)

data class Surah(
    val number: Int,
    val nameLatin: String,
    val nameArabic: String,
    val meaningId: String,
    val revelationType: RevelationType,
    val totalAyahs: Int,
    val description: String,
    val ayahs: List<Ayah>,
    val audioFullSurahUrl: String
)

data class LastRead(
    val surahNumber: Int,
    val surahName: String,
    val surahArabic: String,
    val ayahNumber: Int,
    val timestamp: Long
)

data class BookmarkItem(
    val surahNumber: Int,
    val surahName: String,
    val ayahNumber: Int,
    val arabicPreview: String,
    val timestamp: Long
)

data class ReadingSettings(
    val arabicFontSizeSp: Float = 28f,
    val showLatin: Boolean = true,
    val showTranslation: Boolean = true,
    val autoPlayNextAyah: Boolean = true
)

enum class AudioPlaybackState {
    IDLE,
    BUFFERING,
    PLAYING,
    PAUSED,
    ERROR
}

data class AudioPlayingInfo(
    val surahNumber: Int,
    val surahName: String,
    val ayahNumber: Int,
    val totalAyahs: Int,
    val state: AudioPlaybackState = AudioPlaybackState.IDLE,
    val errorMessage: String? = null
)
