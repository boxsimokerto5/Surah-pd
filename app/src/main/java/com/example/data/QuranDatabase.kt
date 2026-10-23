package com.example.data

import android.content.Context
import com.example.model.Ayah
import com.example.model.RevelationType
import com.example.model.Surah
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

object QuranDatabase {

    private var cachedSurahs: List<Surah>? = null

    fun getSurahs(context: Context): List<Surah> {
        cachedSurahs?.let { return it }

        synchronized(this) {
            cachedSurahs?.let { return it }
            val loaded = loadFromJson(context)
            cachedSurahs = loaded
            return loaded
        }
    }

    fun getSurahByNumber(context: Context, number: Int): Surah? {
        return getSurahs(context).find { it.number == number }
    }

    private fun loadFromJson(context: Context): List<Surah> {
        val list = mutableListOf<Surah>()
        try {
            val inputStream = context.assets.open("quran_juz_amma.json")
            val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            val jsonString = reader.use { it.readText() }

            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val number = obj.getInt("number")
                val nameLatin = obj.getString("nameLatin")
                val nameArabic = obj.getString("nameArabic")
                val meaningId = obj.getString("meaningId")
                val revTypeStr = obj.optString("revelationType", "MAKKIYAH")
                val revelationType = if (revTypeStr.equals("MADANIYAH", ignoreCase = true)) {
                    RevelationType.MADANIYAH
                } else {
                    RevelationType.MAKKIYAH
                }
                val totalAyahs = obj.getInt("totalAyahs")
                val description = obj.optString("description", "")
                val audioFullSurahUrl = obj.optString("audioFullSurahUrl", "")

                val ayahsJsonArray = obj.getJSONArray("ayahs")
                val ayahs = mutableListOf<Ayah>()
                for (j in 0 until ayahsJsonArray.length()) {
                    val aObj = ayahsJsonArray.getJSONObject(j)
                    val numInSurah = aObj.getInt("numberInSurah")
                    val arabic = aObj.getString("arabic")
                    val latin = aObj.getString("latin")
                    val indonesian = aObj.getString("indonesian")
                    val audioUrl = aObj.optString("audioUrl", "")

                    ayahs.add(
                        Ayah(
                            numberInSurah = numInSurah,
                            globalNumber = 0,
                            arabic = arabic,
                            latin = latin,
                            indonesian = indonesian,
                            audioUrl = audioUrl
                        )
                    )
                }

                list.add(
                    Surah(
                        number = number,
                        nameLatin = nameLatin,
                        nameArabic = nameArabic,
                        meaningId = meaningId,
                        revelationType = revelationType,
                        totalAyahs = totalAyahs,
                        description = description,
                        ayahs = ayahs,
                        audioFullSurahUrl = audioFullSurahUrl
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }
}
