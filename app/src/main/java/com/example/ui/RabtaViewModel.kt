package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CountryList
import com.example.data.RabtaRepository
import com.example.localization.AppLanguage
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class Screen {
    SPLASH,
    ONBOARDING,
    PHONE_REGISTRATION,
    OTP_VERIFICATION,
    PROFILE_SETUP,
    PENDING_APPROVAL,
    MAIN,
    CHAT_DETAIL,
    ACTIVE_CALL,
    ADMIN_DASHBOARD,
    SUPPORT,
    BACKUP_RESTORE,
    LANGUAGE_SETTINGS
}

enum class MainTab {
    CHATS,
    GROUPS,
    STATUS,
    CALLS,
    SETTINGS
}

data class ActiveCallSession(
    val contactName: String,
    val contactPhone: String,
    val type: CallType,
    val isMuted: Boolean = false,
    val isSpeakerOn: Boolean = false,
    val isVideoEnabled: Boolean = true,
    val isFrontCamera: Boolean = true
)

class RabtaViewModel(
    val repository: RabtaRepository = RabtaRepository()
) : ViewModel() {

    private val _currentScreen = MutableStateFlow(Screen.SPLASH)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _activeTab = MutableStateFlow(MainTab.CHATS)
    val activeTab: StateFlow<MainTab> = _activeTab.asStateFlow()

    private val _activeChatId = MutableStateFlow<String?>("chat_1")
    val activeChatId: StateFlow<String?> = _activeChatId.asStateFlow()

    private val _activeCallSession = MutableStateFlow<ActiveCallSession?>(null)
    val activeCallSession: StateFlow<ActiveCallSession?> = _activeCallSession.asStateFlow()

    // SIM dialog states
    private val _simCallTarget = MutableStateFlow<Pair<String, String>?>(null) // name, phone
    val simCallTarget: StateFlow<Pair<String, String>?> = _simCallTarget.asStateFlow()

    private val _simSmsTarget = MutableStateFlow<Pair<String, String>?>(null) // name, phone
    val simSmsTarget: StateFlow<Pair<String, String>?> = _simSmsTarget.asStateFlow()

    // Status preview dialog
    private val _viewingStatus = MutableStateFlow<StatusUpdate?>(null)
    val viewingStatus: StateFlow<StatusUpdate?> = _viewingStatus.asStateFlow()

    // Registration in-progress state
    var regCountry = MutableStateFlow(CountryList.defaultCountry)
    var regPhoneNumber = MutableStateFlow("3001234567")
    var regOtp = MutableStateFlow("786921")
    var regFullName = MutableStateFlow("Tanveer Nazam")
    var regEmail = MutableStateFlow("tanveernazam786@gmail.com")

    init {
        // Automatically transition from splash to main or pending approval based on user status
        viewModelScope.launch {
            kotlinx.coroutines.delay(1800)
            val user = repository.currentUser.value
            _currentScreen.value = when (user.accountStatus) {
                AccountStatus.APPROVAL_PENDING -> Screen.PENDING_APPROVAL
                AccountStatus.APPROVED -> Screen.MAIN
                else -> Screen.ONBOARDING
            }
        }
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun selectTab(tab: MainTab) {
        _activeTab.value = tab
    }

    fun openChat(chatId: String) {
        _activeChatId.value = chatId
        _currentScreen.value = Screen.CHAT_DETAIL
    }

    fun startCall(name: String, phone: String, type: CallType) {
        if (type == CallType.SIM_CALL) {
            _simCallTarget.value = Pair(name, phone)
        } else {
            _activeCallSession.value = ActiveCallSession(
                contactName = name,
                contactPhone = phone,
                type = type
            )
            repository.addCallRecord(name, phone, type, CallDirection.OUTGOING)
            _currentScreen.value = Screen.ACTIVE_CALL
        }
    }

    fun endCall() {
        _activeCallSession.value = null
        _currentScreen.value = Screen.MAIN
    }

    fun toggleMute() {
        _activeCallSession.value = _activeCallSession.value?.let {
            it.copy(isMuted = !it.isMuted)
        }
    }

    fun toggleSpeaker() {
        _activeCallSession.value = _activeCallSession.value?.let {
            it.copy(isSpeakerOn = !it.isSpeakerOn)
        }
    }

    fun toggleVideo() {
        _activeCallSession.value = _activeCallSession.value?.let {
            it.copy(isVideoEnabled = !it.isVideoEnabled)
        }
    }

    fun flipCamera() {
        _activeCallSession.value = _activeCallSession.value?.let {
            it.copy(isFrontCamera = !it.isFrontCamera)
        }
    }

    fun promptSimCall(name: String, phone: String) {
        _simCallTarget.value = Pair(name, phone)
    }

    fun dismissSimCall() {
        _simCallTarget.value = null
    }

    fun promptSimSms(name: String, phone: String) {
        _simSmsTarget.value = Pair(name, phone)
    }

    fun dismissSimSms() {
        _simSmsTarget.value = null
    }

    fun viewStatusStory(status: StatusUpdate) {
        _viewingStatus.value = status
    }

    fun dismissStatusStory() {
        _viewingStatus.value = null
    }

    fun onRegisterSubmitted() {
        repository.registerNewUser(
            name = regFullName.value,
            phone = regPhoneNumber.value,
            country = regCountry.value,
            email = regEmail.value
        )
        _currentScreen.value = Screen.PENDING_APPROVAL
    }

    fun approveAccountFromDemo() {
        repository.approveCurrentUser()
        _currentScreen.value = Screen.MAIN
    }
}
