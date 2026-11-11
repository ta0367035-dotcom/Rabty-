package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.CountryList
import com.example.data.RabtaAiService
import com.example.data.RabtaRepository
import com.example.model.AccountStatus
import com.example.model.CallType
import com.example.model.ChatMessage
import com.example.model.MessageType
import com.example.util.PdfExportUtil
import kotlinx.coroutines.runBlocking
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
    assertEquals("Rabta", appName)
  }

  @Test
  fun `verify countries and default country is Pakistan`() {
    val defaultCountry = CountryList.defaultCountry
    assertEquals("Pakistan", defaultCountry.name)
    assertEquals("+92", defaultCountry.dialCode)
    assertEquals("🇵🇰", defaultCountry.flag)
    assertTrue(CountryList.allCountries.isNotEmpty())
  }

  @Test
  fun `verify rabta repository initialization and messaging`() {
    val repo = RabtaRepository()
    val user = repo.currentUser.value
    assertNotNull(user)
    assertEquals(AccountStatus.APPROVED, user.accountStatus)

    // Test sending message
    repo.sendMessage("chat_1", "Testing Rabta messaging engine")
    val chatMessages = repo.messages.value["chat_1"]
    assertNotNull(chatMessages)
    assertTrue(chatMessages!!.any { it.content == "Testing Rabta messaging engine" })
  }

  @Test
  fun `verify pdf transcript generation`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val sampleMessages = listOf(
      ChatMessage(
        id = "m1",
        chatId = "chat_1",
        senderId = "user_me",
        senderName = "Tanveer Nazam",
        content = "Testing PDF Export from Rabta",
        timestamp = "10:00 AM",
        isFromMe = true
      )
    )
    val pdfFile = PdfExportUtil.exportChatToPdf(context, "Ahmad Khan", sampleMessages)
    assertNotNull(pdfFile)
    assertTrue(pdfFile!!.exists())
    assertTrue(pdfFile.length() > 0)
  }

  @Test
  fun `verify audio transcription and ai chatbot`() = runBlocking {
    val voiceMsg = ChatMessage(
      id = "v1",
      chatId = "chat_5",
      senderId = "chat_5",
      senderName = "Ali Raza",
      content = "Voice note from Lahore conference",
      timestamp = "7:18 PM",
      type = MessageType.VOICE,
      voiceDurationSeconds = 45
    )
    val transcript = RabtaAiService.transcribeVoiceNote(voiceMsg)
    assertNotNull(transcript)
    assertTrue(transcript.isNotBlank())

    val aiResponse = RabtaAiService.generateAiChatResponse("Hello Rabta AI", emptyList())
    assertNotNull(aiResponse)
    assertTrue(aiResponse.content.contains("Rabta AI"))
  }

  @Test
  fun `verify support ticket generation and admin resolution`() {
    val repo = RabtaRepository()
    val ticketId = repo.submitSupportTicket(
      fullName = "Test User",
      email = "test@example.com",
      phone = "+92 300 0000000",
      category = com.example.model.TicketCategory.SIM_CALL,
      subject = "Test SIM Inquiry",
      description = "Inquiry regarding cellular SIM dial-out"
    )
    assertTrue(ticketId.startsWith("RABTA-"))

    val ticket = repo.supportTickets.value.find { it.id == ticketId }
    assertNotNull(ticket)

    repo.adminReplyTicket(ticketId, "Inquiry resolved", com.example.model.TicketStatus.RESOLVED)
    val resolvedTicket = repo.supportTickets.value.find { it.id == ticketId }
    assertEquals(com.example.model.TicketStatus.RESOLVED, resolvedTicket?.status)
  }

  @Test
  fun `verify call record creation`() {
    val repo = RabtaRepository()
    val initialCalls = repo.calls.value.size
    repo.addCallRecord("Sara Malik", "+92 321 4567890", CallType.SIM_CALL)
    assertEquals(initialCalls + 1, repo.calls.value.size)
  }
}
