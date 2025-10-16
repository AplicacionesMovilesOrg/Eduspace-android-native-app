package upc.edu.pe.eduspace

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import upc.edu.pe.eduspace.core.data.LanguagePreferences
import upc.edu.pe.eduspace.core.navigation.AppNav
import upc.edu.pe.eduspace.core.ui.theme.AppTheme
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var languagePreferences: LanguagePreferences

    private var currentLanguage: String = "en"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        runBlocking {
            currentLanguage = languagePreferences.languageFlow.first()
        }

        setContent {
            val language by languagePreferences.languageFlow.collectAsState(initial = currentLanguage)

            LaunchedEffect(language) {
                if (language != currentLanguage) {
                    currentLanguage = language
                    recreate()
                }
            }

            AppTheme {
                AppNav()
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val prefs = LanguagePreferences(newBase)
        currentLanguage = runBlocking { prefs.languageFlow.first() }

        val locale = Locale.forLanguageTag(currentLanguage)
        Locale.setDefault(locale)

        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)

        val context = newBase.createConfigurationContext(config)
        super.attachBaseContext(context)
    }
}