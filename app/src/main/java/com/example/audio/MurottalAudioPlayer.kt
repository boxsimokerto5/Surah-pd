package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import com.example.model.AudioPlaybackState
import com.example.model.AudioPlayingInfo
import com.example.model.Surah
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MurottalAudioPlayer(
    private val context: Context,
    private val scope: CoroutineScope
) {
    private var mediaPlayer: MediaPlayer? = null
    private var currentSurah: Surah? = null
    private var currentAyahIndex: Int = 0

    private val _playbackInfo = MutableStateFlow<AudioPlayingInfo?>(null)
    val playbackInfo: StateFlow<AudioPlayingInfo?> = _playbackInfo.asStateFlow()

    var onAyahChanged: ((surahNumber: Int, ayahNumber: Int) -> Unit)? = null
    var autoPlayNext: Boolean = true

    fun playAyah(surah: Surah, ayahNumber: Int) {
        val ayah = surah.ayahs.find { it.numberInSurah == ayahNumber } ?: return
        val index = surah.ayahs.indexOf(ayah)
        if (index == -1) return

        currentSurah = surah
        currentAyahIndex = index

        startPlayback(surah, ayah.numberInSurah, ayah.audioUrl)
    }

    fun playFullSurah(surah: Surah) {
        if (surah.ayahs.isEmpty()) return
        playAyah(surah, surah.ayahs.first().numberInSurah)
    }

    private fun startPlayback(surah: Surah, ayahNumber: Int, audioUrl: String) {
        stop()

        _playbackInfo.value = AudioPlayingInfo(
            surahNumber = surah.number,
            surahName = surah.nameLatin,
            ayahNumber = ayahNumber,
            totalAyahs = surah.totalAyahs,
            state = AudioPlaybackState.BUFFERING
        )

        try {
            val player = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(audioUrl)
                setOnPreparedListener { mp ->
                    mp.start()
                    _playbackInfo.value = _playbackInfo.value?.copy(
                        state = AudioPlaybackState.PLAYING
                    )
                    onAyahChanged?.invoke(surah.number, ayahNumber)
                }
                setOnCompletionListener {
                    handlePlaybackComplete()
                }
                setOnErrorListener { _, what, extra ->
                    Log.e("MurottalAudioPlayer", "MediaPlayer error: what=$what, extra=$extra")
                    _playbackInfo.value = _playbackInfo.value?.copy(
                        state = AudioPlaybackState.ERROR,
                        errorMessage = "Gagal memutar audio. Pastikan koneksi internet aktif."
                    )
                    true
                }
            }
            mediaPlayer = player
            player.prepareAsync()
        } catch (e: Exception) {
            Log.e("MurottalAudioPlayer", "Exception in startPlayback", e)
            _playbackInfo.value = _playbackInfo.value?.copy(
                state = AudioPlaybackState.ERROR,
                errorMessage = e.localizedMessage ?: "Terjadi kesalahan pemutaran"
            )
        }
    }

    private fun handlePlaybackComplete() {
        val surah = currentSurah ?: return
        if (autoPlayNext && currentAyahIndex < surah.ayahs.size - 1) {
            currentAyahIndex++
            val nextAyah = surah.ayahs[currentAyahIndex]
            startPlayback(surah, nextAyah.numberInSurah, nextAyah.audioUrl)
        } else {
            _playbackInfo.value = _playbackInfo.value?.copy(
                state = AudioPlaybackState.IDLE
            )
        }
    }

    fun togglePlayPause() {
        val player = mediaPlayer ?: return
        val currentInfo = _playbackInfo.value ?: return

        if (player.isPlaying) {
            player.pause()
            _playbackInfo.value = currentInfo.copy(state = AudioPlaybackState.PAUSED)
        } else {
            player.start()
            _playbackInfo.value = currentInfo.copy(state = AudioPlaybackState.PLAYING)
        }
    }

    fun playNext() {
        val surah = currentSurah ?: return
        if (currentAyahIndex < surah.ayahs.size - 1) {
            currentAyahIndex++
            val nextAyah = surah.ayahs[currentAyahIndex]
            startPlayback(surah, nextAyah.numberInSurah, nextAyah.audioUrl)
        }
    }

    fun playPrevious() {
        val surah = currentSurah ?: return
        if (currentAyahIndex > 0) {
            currentAyahIndex--
            val prevAyah = surah.ayahs[currentAyahIndex]
            startPlayback(surah, prevAyah.numberInSurah, prevAyah.audioUrl)
        }
    }

    fun stop() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.reset()
                it.release()
            }
        } catch (e: Exception) {
            Log.e("MurottalAudioPlayer", "Error stopping player", e)
        } finally {
            mediaPlayer = null
        }
    }

    fun close() {
        stop()
        _playbackInfo.value = null
    }
}
