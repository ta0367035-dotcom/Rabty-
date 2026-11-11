package com.example.data

import com.example.model.ChatMessage
import com.example.model.MessageStatus
import com.example.model.MessageType
import kotlinx.coroutines.delay

object RabtaAiService {

    // Transcribe audio voice notes to text
    suspend fun transcribeVoiceNote(message: ChatMessage): String {
        delay(1200) // Simulated neural model inference
        return when {
            message.content.contains("conference", ignoreCase = true) || message.chatId == "chat_5" ->
                "Assalam o Alaikum! This is Ali from the Lahore tech conference. The Rabta platform performance and audio fidelity here are exceptional."
            message.content.contains("call", ignoreCase = true) ->
                "Are you available for a quick voice discussion regarding the project?"
            else ->
                "Assalam o Alaikum! Just checking in to see if you received the latest project updates on Rabta."
        }
    }

    // Generate response from Rabta AI Assistant
    suspend fun generateAiChatResponse(
        prompt: String,
        history: List<ChatMessage>
    ): ChatMessage {
        delay(1000)
        val clean = prompt.trim()

        val replyText = when {
            clean.contains("map", ignoreCase = true) || clean.contains("restaurant", ignoreCase = true) || clean.contains("location", ignoreCase = true) || clean.contains("where", ignoreCase = true) -> {
                "📍 **Google Maps Grounding Result:**\n\n1. **Monal Islamabad**: Daman-e-Koh, Pir Sohawa Rd, Islamabad (Rating: 4.6 ★)\n2. **Haveli Restaurant**: Fort Road, Food Street, Lahore (Rating: 4.7 ★)\n3. **Al Baik**: King Fahd Rd, Dubai & Riyadh (Rating: 4.8 ★)\n\n*Would you like me to share directions or pin this location to your chat?*"
            }
            clean.contains("translate", ignoreCase = true) || clean.contains("urdu", ignoreCase = true) || clean.contains("arabic", ignoreCase = true) -> {
                "🌐 **Rabta Smart Translation:**\n\n• **English:** Connect Everyone, Everywhere.\n• **Urdu:** ہر وقت، ہر جگہ، آپ کے ساتھ — سب کو ہر جگہ جوڑیں\n• **Arabic:** تواصل مع الجميع في كل مكان\n\n*Rabta supports 9+ international languages with native RTL typography.*"
            }
            clean.contains("sim", ignoreCase = true) || clean.contains("call", ignoreCase = true) -> {
                "📞 **Rabta Telecom & SIM Calling:**\nRabta features dedicated Cellular SIM integration. When mobile data or Wi-Fi is unavailable, tap the golden Phone icon to dial directly through your device's cellular network."
            }
            clean.contains("security", ignoreCase = true) || clean.contains("encrypt", ignoreCase = true) -> {
                "🔒 **Rabta Security Architecture:**\nAll private one-to-one chats, voice notes, calls, and backups use end-to-end cryptographic protocols. Not even administrators or Rabta servers can read your messages."
            }
            clean.contains("hello", ignoreCase = true) || clean.contains("hi", ignoreCase = true) || clean.contains("salam", ignoreCase = true) -> {
                "Walaikum Assalam! I am **Rabta AI**, your verified intelligent assistant. You can ask me to:\n\n✨ Transcribe voice notes\n🌍 Translate messages in 9 languages\n🗺️ Look up Google Maps places\n🎨 Generate AI stickers & images\n📄 Export and summarize chat transcripts"
            }
            else -> {
                "Thank you for reaching out to **Rabta AI**. I have analyzed your query: \"$clean\". \n\nRabta is engineered for lightning-fast worldwide communication with end-to-end encryption, multi-turn AI reasoning, and native SIM integration. How else may I assist you today?"
            }
        }

        return ChatMessage(
            id = "ai_msg_${System.currentTimeMillis()}",
            chatId = "chat_ai",
            senderId = "rabta_ai",
            senderName = "Rabta AI Assistant 🤖",
            content = replyText,
            timestamp = "Just now",
            type = MessageType.TEXT,
            status = MessageStatus.READ,
            isFromMe = false
        )
    }

    // Generate AI sticker / image preview
    fun generateAiSticker(prompt: String): String {
        return "🎨 AI Generated Asset for: \"$prompt\" • Rabta Nano Studio"
    }
}
