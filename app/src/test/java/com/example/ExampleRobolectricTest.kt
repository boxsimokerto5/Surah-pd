package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.PreferencesManager
import com.example.data.QuranDatabase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Surat Pendek Al-Quran", appName)
    }

    @Test
    fun `verify Quran database surahs loaded`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val surahs = QuranDatabase.getSurahs(context)
        assertTrue(surahs.size >= 38)
        val alFatihah = QuranDatabase.getSurahByNumber(context, 1)
        assertNotNull(alFatihah)
        assertEquals("Al-Fatihah", alFatihah?.nameLatin)
        assertEquals(7, alFatihah?.ayahs?.size)
        val anNaba = QuranDatabase.getSurahByNumber(context, 78)
        assertNotNull(anNaba)
        assertEquals(40, anNaba?.totalAyahs)
    }

    @Test
    fun `verify last read preferences persistence`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val prefs = PreferencesManager(context)
        prefs.saveLastRead(114, "An-Nas", "الناس", 3)

        val lastRead = prefs.lastReadFlow.value
        assertNotNull(lastRead)
        assertEquals(114, lastRead?.surahNumber)
        assertEquals("An-Nas", lastRead?.surahName)
        assertEquals(3, lastRead?.ayahNumber)
    }

    @Test
    fun `verify surah open counter increments and resets at 5`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val prefs = PreferencesManager(context)
        prefs.resetSurahOpenCount()
        assertEquals(0, prefs.getSurahOpenCount())

        assertEquals(1, prefs.incrementSurahOpenCount())
        assertEquals(2, prefs.incrementSurahOpenCount())
        assertEquals(3, prefs.incrementSurahOpenCount())
        assertEquals(4, prefs.incrementSurahOpenCount())
        val countAtFive = prefs.incrementSurahOpenCount()
        assertEquals(5, countAtFive)

        prefs.resetSurahOpenCount()
        assertEquals(0, prefs.getSurahOpenCount())
    }

    @Test
    fun `verify splash screen transitions to home`() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val viewModel = com.example.ui.QuranViewModel(app)
        assertEquals(com.example.ui.Screen.Splash, viewModel.currentScreen.value)
        viewModel.onSplashFinished()
        assertEquals(com.example.ui.Screen.Home, viewModel.currentScreen.value)
    }
}
