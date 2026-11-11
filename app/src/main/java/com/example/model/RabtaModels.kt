package com.example.model

enum class AccountStatus {
    REGISTRATION_PENDING,
    OTP_PENDING,
    VERIFIED,
    APPROVAL_PENDING,
    APPROVED,
    REJECTED,
    SUSPENDED
}

data class Country(
    val name: String,
    val dialCode: String,
    val flag: String,
    val code: String
)

data class RabtaUser(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val country: Country,
    val email: String = "",
    val statusBio: String = "Available on Rabta",
    val avatarUrl: String = "",
    val accountStatus: AccountStatus = AccountStatus.APPROVED,
    val isOnline: Boolean = false,
    val lastSeen: String = "Just now",
    val registrationDate: String = "Nov 11, 2026",
    val isAdmin: Boolean = false
)

enum class MessageType {
    TEXT,
    VOICE,
    IMAGE,
    VIDEO,
    DOCUMENT,
    LOCATION,
    CONTACT
}

enum class MessageStatus {
    SENDING,
    SENT,
    DELIVERED,
    READ
}

data class ChatMessage(
    val id: String,
    val chatId: String,
    val senderId: String,
    val senderName: String,
    val content: String,
    val timestamp: String,
    val timestampEpoch: Long = System.currentTimeMillis(),
    val type: MessageType = MessageType.TEXT,
    val status: MessageStatus = MessageStatus.READ,
    val isFromMe: Boolean = false,
    val mediaUrl: String? = null,
    val voiceDurationSeconds: Int = 0,
    val isStarred: Boolean = false,
    val isPinned: Boolean = false,
    val reaction: String? = null,
    val replyToMessageId: String? = null,
    val replyToSnippet: String? = null,
    val transcription: String? = null,
    val isTranscribing: Boolean = false
)

enum class ConversationType {
    DIRECT,
    GROUP
}

data class Conversation(
    val id: String,
    val title: String,
    val avatarUrl: String = "",
    val type: ConversationType = ConversationType.DIRECT,
    val lastMessage: String = "",
    val lastMessageTime: String = "",
    val unreadCount: Int = 0,
    val isPinned: Boolean = false,
    val isArchived: Boolean = false,
    val isMuted: Boolean = false,
    val isOnline: Boolean = false,
    val isTyping: Boolean = false,
    val recipientPhone: String = "",
    val groupMembersCount: Int = 0,
    val isAiBot: Boolean = false
)

enum class CallType {
    AUDIO,
    VIDEO,
    GROUP_AUDIO,
    GROUP_VIDEO,
    SIM_CALL
}

enum class CallDirection {
    INCOMING,
    OUTGOING,
    MISSED
}

data class CallRecord(
    val id: String,
    val contactName: String,
    val contactPhone: String,
    val contactAvatar: String = "",
    val type: CallType = CallType.AUDIO,
    val direction: CallDirection = CallDirection.OUTGOING,
    val timestamp: String = "Today, 10:45 AM",
    val durationSeconds: Int = 42
)

data class StatusUpdate(
    val id: String,
    val userId: String,
    val userName: String,
    val userAvatar: String = "",
    val textContent: String = "",
    val mediaUrl: String? = null,
    val timestamp: String = "25 minutes ago",
    val isViewed: Boolean = false,
    val viewCount: Int = 18,
    val backgroundHex: Long = 0xFF085429
)

enum class TicketCategory {
    REGISTRATION,
    OTP,
    ACCOUNT_APPROVAL,
    MESSAGING,
    GROUPS,
    STATUS,
    AUDIO_CALLS,
    VIDEO_CALLS,
    SIM_CALL,
    SMS,
    BACKUP,
    SECURITY,
    OTHER
}

enum class TicketStatus {
    SUBMITTED,
    UNDER_REVIEW,
    IN_PROGRESS,
    RESOLVED,
    CLOSED
}

data class SupportTicket(
    val id: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val category: TicketCategory,
    val subject: String,
    val description: String,
    val status: TicketStatus = TicketStatus.SUBMITTED,
    val createdAt: String = "Nov 11, 2026",
    val adminResponse: String? = null
)
