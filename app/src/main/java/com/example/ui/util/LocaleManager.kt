package com.example.ui.util

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

object LocaleManager {

    /**
     * Applies the selected language code ("en", "ta", "te", "hi")
     * using both AndroidX AppCompatDelegate (per-app language standard)
     * and fallback updateConfiguration for immediate runtime Context reflection.
     */
    fun setLocale(context: Context, languageCode: String): Context {
        // Update per-app language via AppCompatDelegate
        val appLocale = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(appLocale)

        // Apply to Context Configuration
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val res = context.resources
        val config = Configuration(res.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocales(LocaleList(locale))
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
        }

        @Suppress("DEPRECATION")
        res.updateConfiguration(config, res.displayMetrics)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context.createConfigurationContext(config)
        } else {
            context
        }
    }

    fun getLanguageCodeFromDisplayName(name: String): String {
        return when {
            name.contains("Tamil", ignoreCase = true) || name.contains("தமிழ்") -> "ta"
            name.contains("Telugu", ignoreCase = true) || name.contains("తెలుగు") -> "te"
            name.contains("Hindi", ignoreCase = true) || name.contains("हिंदी") -> "hi"
            name.contains("Spanish", ignoreCase = true) || name.contains("Español", ignoreCase = true) -> "es"
            else -> "en"
        }
    }
}
