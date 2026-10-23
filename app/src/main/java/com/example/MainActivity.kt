package com.example

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.ads.IronSourceManager
import com.example.ui.QuranViewModel
import com.example.ui.Screen
import com.example.ui.components.MurottalBottomPlayer
import com.example.ui.screens.DoaKhatamScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SurahDetailScreen
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {

    private val viewModel: QuranViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inisialisasi IronSource SDK
        IronSourceManager.init(this, viewModel.preferences)

        setContent {
            MyApplicationTheme {
                QuranApp(viewModel = viewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        IronSourceManager.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        IronSourceManager.onPause(this)
    }
}

@Composable
fun QuranApp(viewModel: QuranViewModel) {
    val context = LocalContext.current as? Activity
    val currentScreen by viewModel.currentScreen.collectAsState()
    val filteredSurahs by viewModel.filteredSurahs.collectAsState()
    val lastRead by viewModel.lastRead.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val playbackInfo by viewModel.playbackInfo.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.userMessage.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                is Screen.Home -> {
                    HomeScreen(
                        surahs = filteredSurahs,
                        lastRead = lastRead,
                        searchQuery = searchQuery,
                        onSearchQueryChanged = viewModel::onSearchQueryChanged,
                        selectedFilter = selectedFilter,
                        onFilterSelected = viewModel::onFilterSelected,
                        onSurahSelected = { surahNumber, targetAyah ->
                            if (context != null) {
                                viewModel.openSurah(context, surahNumber, targetAyah)
                            } else {
                                viewModel.navigateTo(
                                    Screen.SurahDetail(
                                        surahNumber = surahNumber,
                                        targetAyahNumber = targetAyah
                                    )
                                )
                            }
                        },
                        onPlaySurah = { surah ->
                            viewModel.playFullSurahAudio(surah)
                        },
                        onOpenDoaKhatam = {
                            viewModel.navigateTo(Screen.DoaKhatam)
                        }
                    )
                }

                is Screen.SurahDetail -> {
                    val surah = viewModel.getSurah(screen.surahNumber)
                    if (surah != null) {
                        SurahDetailScreen(
                            surah = surah,
                            targetAyahNumber = screen.targetAyahNumber,
                            settings = settings,
                            playbackInfo = playbackInfo,
                            isAyahBookmarked = { ayahNum ->
                                viewModel.isAyahBookmarked(surah.number, ayahNum)
                            },
                            isLastRead = { ayahNum ->
                                viewModel.isLastRead(surah.number, ayahNum)
                            },
                            onBackClick = {
                                viewModel.navigateBack()
                            },
                            onMarkLastRead = { ayah ->
                                viewModel.markAsLastRead(surah, ayah.numberInSurah)
                            },
                            onToggleBookmark = { ayah ->
                                viewModel.toggleBookmark(surah, ayah.numberInSurah)
                            },
                            onPlayAyah = { ayah ->
                                viewModel.playAyahAudio(surah, ayah.numberInSurah)
                            },
                            onPlayFullSurah = {
                                viewModel.playFullSurahAudio(surah)
                            },
                            onCopyAyah = { ayah ->
                                viewModel.copyAyah(surah, ayah)
                            },
                            onShareAyah = { ayah ->
                                viewModel.shareAyah(surah, ayah)
                            },
                            onUpdateSettings = { newSettings ->
                                viewModel.updateSettings(newSettings)
                            }
                        )
                    } else {
                        viewModel.navigateBack()
                    }
                }

                is Screen.DoaKhatam -> {
                    DoaKhatamScreen(
                        onBackClick = {
                            viewModel.navigateBack()
                        }
                    )
                }
            }

            // Bottom Floating Murottal Player
            MurottalBottomPlayer(
                playbackInfo = playbackInfo,
                onTogglePlayPause = viewModel::toggleAudioPlayPause,
                onPrevious = viewModel::playPreviousAyah,
                onNext = viewModel::playNextAyah,
                onStop = viewModel::stopAudio,
                onPlayerClick = {
                    playbackInfo?.let { info ->
                        if (context != null) {
                            viewModel.openSurah(context, info.surahNumber, info.ayahNumber)
                        } else {
                            viewModel.navigateTo(
                                Screen.SurahDetail(
                                    surahNumber = info.surahNumber,
                                    targetAyahNumber = info.ayahNumber
                                )
                            )
                        }
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
