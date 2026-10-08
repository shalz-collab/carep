package com.example

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.ui.navigation.PreCareNavGraph
import com.example.ui.theme.PreCareTheme
import com.example.ui.util.LocaleManager
import com.example.ui.util.ProvideAppLanguage
import com.example.ui.util.SupportedLanguage
import com.example.ui.viewmodel.PreCareViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: PreCareViewModel by viewModels()

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("precare_prefs", Context.MODE_PRIVATE)
        val langCode = prefs.getString("app_lang_code", "en") ?: "en"
        val localizedContext = LocaleManager.setLocale(newBase, langCode)
        super.attachBaseContext(localizedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val currentTheme by viewModel.appTheme.collectAsState()
            val currentLanguageName by viewModel.appLanguage.collectAsState()
            val supportedLanguage = SupportedLanguage.fromDisplayName(currentLanguageName)

            ProvideAppLanguage(language = supportedLanguage) {
                PreCareTheme(appTheme = currentTheme) {
                    PreCareNavGraph(viewModel = viewModel)
                }
            }
        }
    }
}
