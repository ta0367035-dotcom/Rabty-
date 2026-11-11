package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.RabtaViewModel
import com.example.ui.Screen
import com.example.ui.screens.*
import com.example.ui.theme.RabtaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val rabtaViewModel: RabtaViewModel = viewModel()
            val currentScreen by rabtaViewModel.currentScreen.collectAsState()
            val currentLanguage by rabtaViewModel.repository.currentLanguage.collectAsState()
            val isDarkThemePref by rabtaViewModel.repository.isDarkTheme.collectAsState()
            val activeChatId by rabtaViewModel.activeChatId.collectAsState()

            val effectiveDarkTheme = isDarkThemePref ?: isSystemInDarkTheme()
            val layoutDirection = if (currentLanguage.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

            CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                RabtaTheme(darkTheme = effectiveDarkTheme) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        when (currentScreen) {
                            Screen.SPLASH -> SplashScreen(rabtaViewModel)
                            Screen.ONBOARDING -> OnboardingScreen(rabtaViewModel, currentLanguage)
                            Screen.PHONE_REGISTRATION -> PhoneRegistrationScreen(rabtaViewModel, currentLanguage)
                            Screen.OTP_VERIFICATION -> OtpVerificationScreen(rabtaViewModel, currentLanguage)
                            Screen.PROFILE_SETUP -> ProfileSetupScreen(rabtaViewModel, currentLanguage)
                            Screen.PENDING_APPROVAL -> PendingApprovalScreen(rabtaViewModel, currentLanguage)
                            Screen.MAIN -> MainScreen(rabtaViewModel, currentLanguage)
                            Screen.CHAT_DETAIL -> ChatDetailScreen(
                                viewModel = rabtaViewModel,
                                chatId = activeChatId ?: "chat_1",
                                language = currentLanguage
                            )
                            Screen.ACTIVE_CALL -> ActiveCallScreen(rabtaViewModel)
                            Screen.ADMIN_DASHBOARD -> AdminDashboardScreen(rabtaViewModel)
                            Screen.SUPPORT -> SupportScreen(rabtaViewModel)
                            Screen.BACKUP_RESTORE -> BackupScreen(rabtaViewModel)
                            Screen.LANGUAGE_SETTINGS -> LanguagePickerScreen(rabtaViewModel)
                        }
                    }
                }
            }
        }
    }
}
