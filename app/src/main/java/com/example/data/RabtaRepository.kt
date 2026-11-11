package com.example.data

import com.example.localization.AppLanguage
import com.example.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RabtaRepository {
    private val scope = CoroutineScope(Dispatchers.Default)

    // Current logged-in user state
    private val _currentUser = MutableStateFlow(
        RabtaUser(
            id = "user_me",
            name = "Tanveer Nazam",
            phoneNumber = "+92 300 1234567",
            country = CountryList.defaultCountry,
            email = "tanveernazam786@gmail.com",
            statusBio = "Connect Everyone, Everywhere 🇵🇰",
            accountStatus = AccountStatus.APPROVED, // starts approved for instant preview, can be toggled to test approval flow
            isAdmin = true
        )
    )
    val currentUser: StateFlow<RabtaUser> = _currentUser.asStateFlow()

    // Language setting
    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    // Dark theme override (null = system default, true = dark, false = light)
    private val _isDarkTheme = MutableStateFlow<Boolean?>(null)
    val isDarkTheme: StateFlow<Boolean?> = _isDarkTheme.asStateFlow()

    // Conversations matching the user's promo screenshot
    private val _conversations = MutableStateFlow<List<Conversation>>(
        listOf(
            Conversation(
                id = "chat_ai",
                title = "Rabta AI Assistant 🤖",
                lastMessage = "✨ Ask me anything, transcribe audio, or translate",
                lastMessageTime = "Just now",
                unreadCount = 0,
                isPinned = true,
                isOnline = true,
                isAiBot = true,
                recipientPhone = "AI Intelligence"
            ),
            Conversation(
                id = "chat_1",
                title = "Ahmad Khan",
                lastMessage = "Assalam o Alaikum! Are you free for a call?",
                lastMessageTime = "9:20 PM",
                unreadCount = 2,
                isOnline = true,
                recipientPhone = "+92 301 9876543",
                avatarUrl = "avatar_1"
            ),
            Conversation(
                id = "chat_2",
                title = "Family Group",
                type = ConversationType.GROUP,
                lastMessage = "📷 Photo: Gathering at grandfather's house",
                lastMessageTime = "8:45 PM",
                unreadCount = 5,
                groupMembersCount = 12,
                avatarUrl = "avatar_group"
            ),
            Conversation(
                id = "chat_3",
                title = "Sara Malik",
                lastMessage = "Good Night, see you tomorrow at the office!",
                lastMessageTime = "8:30 PM",
                unreadCount = 0,
                isOnline = false,
                recipientPhone = "+92 321 4567890",
                avatarUrl = "avatar_2"
            ),
            Conversation(
                id = "chat_4",
                title = "Office Team",
                type = ConversationType.GROUP,
                lastMessage = "✓ Meeting scheduled tomorrow at 10:00 AM",
                lastMessageTime = "7:50 PM",
                unreadCount = 1,
                isPinned = true,
                groupMembersCount = 28,
                avatarUrl = "avatar_office"
            ),
            Conversation(
                id = "chat_5",
                title = "Ali Raza",
                lastMessage = "🎤 Voice message (0:45)",
                lastMessageTime = "7:20 PM",
                unreadCount = 0,
                isOnline = true,
                recipientPhone = "+92 333 1122334",
                avatarUrl = "avatar_3"
            ),
            Conversation(
                id = "chat_6",
                title = "Fatima Zahra",
                lastMessage = "📍 Shared location: F-7 Markaz, Islamabad",
                lastMessageTime = "Yesterday",
                unreadCount = 0,
                recipientPhone = "+92 345 5566778",
                avatarUrl = "avatar_4"
            ),
            Conversation(
                id = "chat_7",
                title = "Tariq Mahmood (SIM)",
                lastMessage = "Call me directly on cellular SIM if data drops",
                lastMessageTime = "Yesterday",
                unreadCount = 0,
                recipientPhone = "+92 300 8899001",
                avatarUrl = "avatar_5"
            )
        )
    )
    val conversations: StateFlow<List<Conversation>> = _conversations.asStateFlow()

    // Messages per conversation
    private val _messages = MutableStateFlow<Map<String, List<ChatMessage>>>(
        mapOf(
            "chat_ai" to listOf(
                ChatMessage(
                    id = "ai_welcome",
                    chatId = "chat_ai",
                    senderId = "chat_ai",
                    senderName = "Rabta AI Assistant 🤖",
                    content = "Assalam o Alaikum! I am Rabta AI Assistant. I can transcribe voice notes, translate messages into 9 languages, look up places using Google Maps, or help with any inquiry. How can I help you today?",
                    timestamp = "9:00 AM",
                    isFromMe = false
                )
            ),
            "chat_1" to listOf(
                ChatMessage(
                    id = "m1",
                    chatId = "chat_1",
                    senderId = "chat_1",
                    senderName = "Ahmad Khan",
                    content = "Assalam o Alaikum Tanveer bhai! How is the new Rabta platform coming along?",
                    timestamp = "9:15 PM",
                    isFromMe = false
                ),
                ChatMessage(
                    id = "m2",
                    chatId = "chat_1",
                    senderId = "user_me",
                    senderName = "Tanveer Nazam",
                    content = "Walaikum Assalam Ahmad! It is going great. We have built full end-to-end encrypted messaging, voice/video calls, and dedicated SIM calls!",
                    timestamp = "9:18 PM",
                    isFromMe = true,
                    status = MessageStatus.READ
                ),
                ChatMessage(
                    id = "m3",
                    chatId = "chat_1",
                    senderId = "chat_1",
                    senderName = "Ahmad Khan",
                    content = "SubhanAllah, that's amazing! Are you free for a quick call now?",
                    timestamp = "9:20 PM",
                    isFromMe = false
                )
            ),
            "chat_5" to listOf(
                ChatMessage(
                    id = "m5_1",
                    chatId = "chat_5",
                    senderId = "chat_5",
                    senderName = "Ali Raza",
                    content = "Voice note from Lahore conference",
                    timestamp = "7:18 PM",
                    type = MessageType.VOICE,
                    voiceDurationSeconds = 45,
                    isFromMe = false
                ),
                ChatMessage(
                    id = "m5_2",
                    chatId = "chat_5",
                    senderId = "user_me",
                    senderName = "Tanveer Nazam",
                    content = "Received loud and clear Ali! Beautiful audio quality.",
                    timestamp = "7:20 PM",
                    isFromMe = true,
                    status = MessageStatus.READ
                )
            )
        )
    )
    val messages: StateFlow<Map<String, List<ChatMessage>>> = _messages.asStateFlow()

    // Calls records
    private val _calls = MutableStateFlow<List<CallRecord>>(
        listOf(
            CallRecord(
                id = "c1",
                contactName = "Ahmad Khan",
                contactPhone = "+92 301 9876543",
                type = CallType.VIDEO,
                direction = CallDirection.INCOMING,
                timestamp = "Today, 9:22 PM",
                durationSeconds = 180
            ),
            CallRecord(
                id = "c2",
                contactName = "Ali Raza",
                contactPhone = "+92 333 1122334",
                type = CallType.AUDIO,
                direction = CallDirection.OUTGOING,
                timestamp = "Today, 7:25 PM",
                durationSeconds = 64
            ),
            CallRecord(
                id = "c3",
                contactName = "Tariq Mahmood",
                contactPhone = "+92 300 8899001",
                type = CallType.SIM_CALL,
                direction = CallDirection.OUTGOING,
                timestamp = "Yesterday, 3:15 PM",
                durationSeconds = 120
            ),
            CallRecord(
                id = "c4",
                contactName = "Sara Malik",
                contactPhone = "+92 321 4567890",
                type = CallType.AUDIO,
                direction = CallDirection.MISSED,
                timestamp = "Yesterday, 11:40 AM",
                durationSeconds = 0
            )
        )
    )
    val calls: StateFlow<List<CallRecord>> = _calls.asStateFlow()

    // Status updates
    private val _statusUpdates = MutableStateFlow<List<StatusUpdate>>(
        listOf(
            StatusUpdate(
                id = "s1",
                userId = "u1",
                userName = "Ahmad Khan",
                textContent = "Alhamdulillah! Rabta is officially live worldwide 🌍✨",
                timestamp = "15 minutes ago",
                isViewed = false,
                viewCount = 34,
                backgroundHex = 0xFF085429
            ),
            StatusUpdate(
                id = "s2",
                userId = "u2",
                userName = "Sara Malik",
                textContent = "Connecting hearts and minds. Connect Everyone, Everywhere! 🇵🇰",
                timestamp = "45 minutes ago",
                isViewed = false,
                viewCount = 52,
                backgroundHex = 0xFF107C41
            ),
            StatusUpdate(
                id = "s3",
                userId = "u3",
                userName = "Ali Raza",
                textContent = "Testing high-fidelity audio notes on Rabta. Stunning clarity!",
                timestamp = "2 hours ago",
                isViewed = true,
                viewCount = 89,
                backgroundHex = 0xFF1E3A8A
            )
        )
    )
    val statusUpdates: StateFlow<List<StatusUpdate>> = _statusUpdates.asStateFlow()

    // Support tickets
    private val _supportTickets = MutableStateFlow<List<SupportTicket>>(
        listOf(
            SupportTicket(
                id = "RABTA-90412",
                fullName = "Hamza Farooq",
                email = "hamza.farooq@example.com",
                phone = "+92 302 9911223",
                category = TicketCategory.REGISTRATION,
                subject = "Account Approval expedited review",
                description = "I registered my international number and request approval for community broadcasting.",
                status = TicketStatus.UNDER_REVIEW,
                createdAt = "Nov 11, 2026",
                adminResponse = "Hello Hamza, your verification has been reviewed and approved by Rabta Trust & Safety."
            ),
            SupportTicket(
                id = "RABTA-84920",
                fullName = "Dr. Zeeshan",
                email = "zeeshan.med@example.com",
                phone = "+971 50 1234567",
                category = TicketCategory.SIM_CALL,
                subject = "SIM Call permissions on dual SIM device",
                description = "Would like guidance on selecting SIM 1 vs SIM 2 for cellular dial-out.",
                status = TicketStatus.RESOLVED,
                createdAt = "Nov 10, 2026",
                adminResponse = "Rabta invokes Android's native Telecom manager which prompts for default SIM preference."
            )
        )
    )
    val supportTickets: StateFlow<List<SupportTicket>> = _supportTickets.asStateFlow()

    // Registered users list for Admin Dashboard
    private val _allUsersForAdmin = MutableStateFlow<List<RabtaUser>>(
        listOf(
            RabtaUser(
                id = "u_pending_1",
                name = "Khurram Shahzad",
                phoneNumber = "+92 334 7788990",
                country = CountryList.allCountries[0],
                email = "khurram@example.com",
                accountStatus = AccountStatus.APPROVAL_PENDING,
                registrationDate = "Nov 11, 2026"
            ),
            RabtaUser(
                id = "u_pending_2",
                name = "Ayesha Siddiqui",
                phoneNumber = "+971 55 9871234",
                country = CountryList.allCountries[1],
                email = "ayesha.s@example.com",
                accountStatus = AccountStatus.APPROVAL_PENDING,
                registrationDate = "Nov 11, 2026"
            ),
            RabtaUser(
                id = "u_approved_1",
                name = "Ahmad Khan",
                phoneNumber = "+92 301 9876543",
                country = CountryList.allCountries[0],
                accountStatus = AccountStatus.APPROVED,
                registrationDate = "Nov 09, 2026"
            ),
            RabtaUser(
                id = "u_approved_2",
                name = "Sara Malik",
                phoneNumber = "+92 321 4567890",
                country = CountryList.allCountries[0],
                accountStatus = AccountStatus.APPROVED,
                registrationDate = "Nov 10, 2026"
            ),
            RabtaUser(
                id = "u_suspended_1",
                name = "Spam Bot 404",
                phoneNumber = "+1 555 0192834",
                country = CountryList.allCountries[3],
                accountStatus = AccountStatus.SUSPENDED,
                registrationDate = "Nov 08, 2026"
            )
        )
    )
    val allUsersForAdmin: StateFlow<List<RabtaUser>> = _allUsersForAdmin.asStateFlow()

    // Backup state
    private val _backupStatus = MutableStateFlow("Google Drive: Last backed up Today, 4:00 AM (48.2 MB)")
    val backupStatus: StateFlow<String> = _backupStatus.asStateFlow()

    private val _isBackingUp = MutableStateFlow(false)
    val isBackingUp: StateFlow<Boolean> = _isBackingUp.asStateFlow()

    // Methods
    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
    }

    fun setThemeMode(dark: Boolean?) {
        _isDarkTheme.value = dark
    }

    fun sendMessage(chatId: String, content: String, type: MessageType = MessageType.TEXT, voiceDuration: Int = 0) {
        val currentList = _messages.value[chatId] ?: emptyList()
        val newMessage = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            chatId = chatId,
            senderId = _currentUser.value.id,
            senderName = _currentUser.value.name,
            content = content,
            timestamp = "Just now",
            type = type,
            isFromMe = true,
            status = MessageStatus.SENT,
            voiceDurationSeconds = voiceDuration
        )
        val updatedMap = _messages.value.toMutableMap()
        updatedMap[chatId] = currentList + newMessage
        _messages.value = updatedMap

        // Update conversation snippet
        _conversations.value = _conversations.value.map { conv ->
            if (conv.id == chatId) {
                conv.copy(lastMessage = content, lastMessageTime = "Just now")
            } else conv
        }

        // Simulate reply after 1.5 seconds for interactive realistic feel
        scope.launch {
            delay(1500)
            val updatedWithDelivered = (_messages.value[chatId] ?: emptyList()).map {
                if (it.id == newMessage.id) it.copy(status = MessageStatus.READ) else it
            }
            val map2 = _messages.value.toMutableMap()
            map2[chatId] = updatedWithDelivered
            _messages.value = map2

            // Contact typing and reply
            delay(1000)
            val replyMessage = if (chatId == "chat_ai") {
                RabtaAiService.generateAiChatResponse(content, currentList)
            } else {
                ChatMessage(
                    id = "reply_${System.currentTimeMillis()}",
                    chatId = chatId,
                    senderId = chatId,
                    senderName = _conversations.value.find { it.id == chatId }?.title ?: "Contact",
                    content = "Great! Received your message on Rabta. End-to-end encrypted and crystal clear.",
                    timestamp = "Just now",
                    isFromMe = false
                )
            }
            val map3 = _messages.value.toMutableMap()
            map3[chatId] = (map3[chatId] ?: emptyList()) + replyMessage
            _messages.value = map3

            _conversations.value = _conversations.value.map { conv ->
                if (conv.id == chatId) {
                    conv.copy(lastMessage = replyMessage.content, lastMessageTime = "Just now")
                } else conv
            }
        }
    }

    fun transcribeMessage(chatId: String, messageId: String) {
        scope.launch {
            val currentList = _messages.value[chatId] ?: return@launch
            val targetMsg = currentList.find { it.id == messageId } ?: return@launch

            // Set transcribing state
            val updating = currentList.map {
                if (it.id == messageId) it.copy(isTranscribing = true) else it
            }
            val map1 = _messages.value.toMutableMap()
            map1[chatId] = updating
            _messages.value = map1

            val result = RabtaAiService.transcribeVoiceNote(targetMsg)

            // Set transcribed result
            val completed = (_messages.value[chatId] ?: emptyList()).map {
                if (it.id == messageId) it.copy(isTranscribing = false, transcription = result) else it
            }
            val map2 = _messages.value.toMutableMap()
            map2[chatId] = completed
            _messages.value = map2
        }
    }

    fun addStatus(text: String, backgroundHex: Long = 0xFF085429) {
        val newStatus = StatusUpdate(
            id = "status_${System.currentTimeMillis()}",
            userId = _currentUser.value.id,
            userName = _currentUser.value.name,
            textContent = text,
            timestamp = "Just now",
            isViewed = false,
            viewCount = 1,
            backgroundHex = backgroundHex
        )
        _statusUpdates.value = listOf(newStatus) + _statusUpdates.value
    }

    fun addCallRecord(name: String, phone: String, type: CallType, direction: CallDirection = CallDirection.OUTGOING) {
        val newCall = CallRecord(
            id = "call_${System.currentTimeMillis()}",
            contactName = name,
            contactPhone = phone,
            type = type,
            direction = direction,
            timestamp = "Just now",
            durationSeconds = if (direction == CallDirection.MISSED) 0 else 45
        )
        _calls.value = listOf(newCall) + _calls.value
    }

    fun submitSupportTicket(fullName: String, email: String, phone: String, category: TicketCategory, subject: String, description: String): String {
        val ticketId = "RABTA-${(10000..99999).random()}"
        val newTicket = SupportTicket(
            id = ticketId,
            fullName = fullName,
            email = email,
            phone = phone,
            category = category,
            subject = subject,
            description = description,
            status = TicketStatus.SUBMITTED,
            createdAt = "Nov 11, 2026"
        )
        _supportTickets.value = listOf(newTicket) + _supportTickets.value
        return ticketId
    }

    // Admin operations
    fun adminApproveUser(userId: String) {
        _allUsersForAdmin.value = _allUsersForAdmin.value.map {
            if (it.id == userId) it.copy(accountStatus = AccountStatus.APPROVED) else it
        }
        if (_currentUser.value.id == userId) {
            _currentUser.value = _currentUser.value.copy(accountStatus = AccountStatus.APPROVED)
        }
    }

    fun adminRejectUser(userId: String) {
        _allUsersForAdmin.value = _allUsersForAdmin.value.map {
            if (it.id == userId) it.copy(accountStatus = AccountStatus.REJECTED) else it
        }
    }

    fun adminSuspendUser(userId: String) {
        _allUsersForAdmin.value = _allUsersForAdmin.value.map {
            if (it.id == userId) it.copy(accountStatus = AccountStatus.SUSPENDED) else it
        }
    }

    fun adminReactivateUser(userId: String) {
        _allUsersForAdmin.value = _allUsersForAdmin.value.map {
            if (it.id == userId) it.copy(accountStatus = AccountStatus.APPROVED) else it
        }
    }

    fun adminReplyTicket(ticketId: String, reply: String, status: TicketStatus) {
        _supportTickets.value = _supportTickets.value.map {
            if (it.id == ticketId) it.copy(adminResponse = reply, status = status) else it
        }
    }

    fun performBackup() {
        scope.launch {
            _isBackingUp.value = true
            delay(2000)
            _isBackingUp.value = false
            _backupStatus.value = "Google Drive: Last backed up Just now (49.8 MB) • End-to-End Encrypted"
        }
    }

    fun registerNewUser(name: String, phone: String, country: Country, email: String) {
        _currentUser.value = RabtaUser(
            id = "user_me",
            name = name,
            phoneNumber = "${country.dialCode} $phone",
            country = country,
            email = email,
            accountStatus = AccountStatus.APPROVAL_PENDING,
            registrationDate = "Nov 11, 2026",
            isAdmin = true
        )
    }

    fun approveCurrentUser() {
        _currentUser.value = _currentUser.value.copy(accountStatus = AccountStatus.APPROVED)
    }

    fun resetToPendingApproval() {
        _currentUser.value = _currentUser.value.copy(accountStatus = AccountStatus.APPROVAL_PENDING)
    }
}
