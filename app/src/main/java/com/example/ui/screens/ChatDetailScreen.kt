package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.RabtaAiService
import com.example.localization.AppLanguage
import com.example.model.CallType
import com.example.model.ChatMessage
import com.example.model.MessageStatus
import com.example.model.MessageType
import com.example.ui.RabtaViewModel
import com.example.ui.Screen
import com.example.ui.theme.*
import com.example.util.PdfExportUtil
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    viewModel: RabtaViewModel,
    chatId: String,
    language: AppLanguage
) {
    val context = LocalContext.current

    BackHandler {
        viewModel.navigateTo(Screen.MAIN)
    }

    val conversations by viewModel.repository.conversations.collectAsState()
    val allMessages by viewModel.repository.messages.collectAsState()
    val chat = conversations.find { it.id == chatId } ?: conversations.first()
    val messageList = allMessages[chatId] ?: emptyList()

    var inputMessage by remember { mutableStateOf("") }
    var isRecordingVoice by remember { mutableStateOf(false) }
    var recordingTimer by remember { mutableStateOf(0) }
    var showAttachmentMenu by remember { mutableStateOf(false) }
    var showChatMenu by remember { mutableStateOf(false) }
    var showAiImageDialog by remember { mutableStateOf(false) }
    var selectedReactionMessageId by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // Scroll to bottom when message list changes
    LaunchedEffect(messageList.size) {
        if (messageList.isNotEmpty()) {
            listState.animateScrollToItem(messageList.size - 1)
        }
    }

    // Voice record timer
    LaunchedEffect(isRecordingVoice) {
        if (isRecordingVoice) {
            recordingTimer = 0
            while (isRecordingVoice) {
                kotlinx.coroutines.delay(1000)
                recordingTimer++
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = RabtaGreenPrimary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateTo(Screen.MAIN) },
                        modifier = Modifier.testTag("btn_chat_back")
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (chat.isAiBot) RabtaGold else RabtaGreenSecondary),
                            contentAlignment = Alignment.Center
                        ) {
                            if (chat.isAiBot) {
                                Icon(Icons.Default.SmartToy, contentDescription = null, tint = Color.Black, modifier = Modifier.size(24.dp))
                            } else {
                                Text(
                                    text = chat.title.take(2).uppercase(),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = chat.title,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                                if (chat.isAiBot) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.Verified, contentDescription = "Verified AI", tint = RabtaGold, modifier = Modifier.size(15.dp))
                                }
                            }
                            Text(
                                text = if (chat.isAiBot) "Always active • Multi-turn AI" else if (chat.isOnline) "online" else "End-to-End Encrypted",
                                fontSize = 11.sp,
                                color = if (chat.isOnline || chat.isAiBot) RabtaGold else Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                },
                actions = {
                    // Quick Download PDF action icon
                    IconButton(
                        onClick = {
                            val pdf = PdfExportUtil.exportChatToPdf(context, chat.title, messageList)
                            if (pdf != null) {
                                PdfExportUtil.openOrSharePdf(context, pdf)
                            } else {
                                Toast.makeText(context, "Failed to create PDF", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.testTag("action_download_pdf")
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = "Download PDF", tint = RabtaGold)
                    }

                    // 1. Internet Video Call
                    IconButton(
                        onClick = {
                            viewModel.startCall(chat.title, chat.recipientPhone.ifEmpty { "+92 301 9876543" }, CallType.VIDEO)
                        },
                        modifier = Modifier.testTag("action_video_call")
                    ) {
                        Icon(Icons.Default.Videocam, contentDescription = "Video Call")
                    }

                    // 2. Internet Audio Call
                    IconButton(
                        onClick = {
                            viewModel.startCall(chat.title, chat.recipientPhone.ifEmpty { "+92 301 9876543" }, CallType.AUDIO)
                        },
                        modifier = Modifier.testTag("action_audio_call")
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "Audio Call")
                    }

                    // 3. Dedicated SIM Cellular Call
                    IconButton(
                        onClick = {
                            viewModel.promptSimCall(chat.title, chat.recipientPhone.ifEmpty { "+92 301 9876543" })
                        },
                        modifier = Modifier.testTag("action_sim_call_in_chat")
                    ) {
                        Icon(Icons.Default.PhoneInTalk, contentDescription = "SIM Call", tint = RabtaGold)
                    }

                    // 4. Chat Menu
                    Box {
                        IconButton(onClick = { showChatMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Options")
                        }
                        DropdownMenu(
                            expanded = showChatMenu,
                            onDismissRequest = { showChatMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Download Chat as PDF 📄") },
                                leadingIcon = { Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = RabtaGreenPrimary) },
                                onClick = {
                                    showChatMenu = false
                                    val pdf = PdfExportUtil.exportChatToPdf(context, chat.title, messageList)
                                    if (pdf != null) {
                                        PdfExportUtil.openOrSharePdf(context, pdf)
                                    } else {
                                        Toast.makeText(context, "Could not generate PDF", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("SIM SMS (Cellular)") },
                                leadingIcon = { Icon(Icons.Default.Sms, contentDescription = null, tint = RabtaSimAmber) },
                                onClick = {
                                    showChatMenu = false
                                    viewModel.promptSimSms(chat.title, chat.recipientPhone.ifEmpty { "+92 301 9876543" })
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Generate AI Sticker 🎨") },
                                leadingIcon = { Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = RabtaGold) },
                                onClick = {
                                    showChatMenu = false
                                    showAiImageDialog = true
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Clear Chat") },
                                onClick = { showChatMenu = false }
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // E2EE System Header Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = RabtaGold.copy(alpha = 0.15f),
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color(0xFFB45309),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Messages and calls are end-to-end encrypted.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Message List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messageList, key = { it.id }) { msg ->
                    MessageBubble(
                        message = msg,
                        onLongClick = { selectedReactionMessageId = msg.id },
                        onTranscribe = {
                            viewModel.repository.transcribeMessage(chatId, msg.id)
                        }
                    )
                }
            }

            // Quick emoji reaction bar if message clicked
            AnimatedVisibility(visible = selectedReactionMessageId != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val emojis = listOf("❤️", "👍", "😂", "😮", "😢", "🙏")
                    emojis.forEach { emoji ->
                        Text(
                            text = emoji,
                            fontSize = 24.sp,
                            modifier = Modifier
                                .clickable {
                                    selectedReactionMessageId = null
                                }
                                .padding(4.dp)
                        )
                    }
                }
            }

            // Attachment Sheet
            AnimatedVisibility(visible = showAttachmentMenu) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        AttachmentIcon(Icons.Default.PictureAsPdf, "PDF Export", Color(0xFFDC2626)) {
                            showAttachmentMenu = false
                            val pdf = PdfExportUtil.exportChatToPdf(context, chat.title, messageList)
                            if (pdf != null) PdfExportUtil.openOrSharePdf(context, pdf)
                        }
                        AttachmentIcon(Icons.Default.AutoAwesome, "AI Sticker", RabtaGold) {
                            showAttachmentMenu = false
                            showAiImageDialog = true
                        }
                        AttachmentIcon(Icons.Default.LocationOn, "Maps Location", Color(0xFF16A34A)) {
                            showAttachmentMenu = false
                            viewModel.repository.sendMessage(
                                chatId,
                                "📍 Google Maps Pin: F-7 Markaz, Islamabad (33.7215° N, 73.0560° E)",
                                MessageType.LOCATION
                            )
                        }
                        AttachmentIcon(Icons.Default.Image, "Gallery", Color(0xFF9333EA)) {
                            showAttachmentMenu = false
                            viewModel.repository.sendMessage(chatId, "📷 Shared photo from gallery", MessageType.IMAGE)
                        }
                        AttachmentIcon(Icons.Default.Sms, "SIM SMS", RabtaSimAmber) {
                            showAttachmentMenu = false
                            viewModel.promptSimSms(chat.title, chat.recipientPhone.ifEmpty { "+92 301 9876543" })
                        }
                    }
                }
            }

            // Bottom Input Bar
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isRecordingVoice) {
                        // Voice recording active indicator
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(24.dp))
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(RabtaCallRed)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Recording audio: 00:${if (recordingTimer < 10) "0$recordingTimer" else recordingTimer}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = RabtaCallRed
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Send Voice Note Button
                        IconButton(
                            onClick = {
                                val duration = if (recordingTimer > 0) recordingTimer else 8
                                isRecordingVoice = false
                                viewModel.repository.sendMessage(
                                    chatId,
                                    "🎤 Voice message (0:${if (duration < 10) "0$duration" else duration})",
                                    MessageType.VOICE,
                                    duration
                                )
                            },
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(RabtaGreenPrimary)
                                .testTag("btn_send_voice_note")
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Send Voice", tint = Color.White)
                        }
                    } else {
                        IconButton(onClick = { }) {
                            Icon(Icons.Outlined.Mood, contentDescription = "Emoji", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        OutlinedTextField(
                            value = inputMessage,
                            onValueChange = { inputMessage = it },
                            placeholder = {
                                Text(
                                    if (chat.isAiBot) "Ask Rabta AI anything..." else "Type a message...",
                                    fontSize = 14.sp
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("chat_text_input"),
                            shape = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            ),
                            maxLines = 4
                        )

                        IconButton(
                            onClick = { showAttachmentMenu = !showAttachmentMenu },
                            modifier = Modifier.testTag("btn_attachment")
                        ) {
                            Icon(Icons.Default.AttachFile, contentDescription = "Attachment", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        if (inputMessage.isNotBlank()) {
                            IconButton(
                                onClick = {
                                    val text = inputMessage
                                    inputMessage = ""
                                    viewModel.repository.sendMessage(chatId, text, MessageType.TEXT)
                                },
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(RabtaGreenPrimary)
                                    .testTag("btn_send_message")
                            ) {
                                Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                            }
                        } else {
                            IconButton(
                                onClick = { isRecordingVoice = true },
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(RabtaGreenPrimary)
                                    .testTag("btn_record_voice")
                            ) {
                                Icon(Icons.Default.Mic, contentDescription = "Voice Record", tint = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }

    // AI Sticker / Image Generation Modal
    if (showAiImageDialog) {
        var stickerPrompt by remember { mutableStateOf("Emerald Islamic calligraphy with golden star") }

        AlertDialog(
            onDismissRequest = { showAiImageDialog = false },
            icon = {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(RabtaGold.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = RabtaGold)
                }
            },
            title = { Text("Create AI Sticker / Image", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Enter a prompt to create an AI-powered image or sticker using Rabta AI Studio:",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedTextField(
                        value = stickerPrompt,
                        onValueChange = { stickerPrompt = it },
                        label = { Text("Prompt") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showAiImageDialog = false
                        val stickerResult = RabtaAiService.generateAiSticker(stickerPrompt)
                        viewModel.repository.sendMessage(chatId, stickerResult, MessageType.IMAGE)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RabtaGreenPrimary)
                ) {
                    Text("Generate & Send")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAiImageDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun MessageBubble(
    message: ChatMessage,
    onLongClick: () -> Unit,
    onTranscribe: () -> Unit
) {
    val isOut = message.isFromMe
    val bubbleColor = if (isOut) {
        if (MaterialTheme.colorScheme.background == RabtaDarkBackground) RabtaDarkChatBubbleOut
        else RabtaLightChatBubbleOut
    } else {
        if (MaterialTheme.colorScheme.background == RabtaDarkBackground) RabtaDarkChatBubbleIn
        else RabtaLightChatBubbleIn
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        contentAlignment = if (isOut) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isOut) 16.dp else 2.dp,
                bottomEnd = if (isOut) 2.dp else 16.dp
            ),
            color = bubbleColor,
            tonalElevation = 1.dp,
            shadowElevation = 1.dp,
            modifier = Modifier
                .widthIn(max = 320.dp)
                .clickable { onLongClick() }
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                if (message.type == MessageType.VOICE) {
                    // Voice note waveform player
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        var isPlaying by remember { mutableStateOf(false) }
                        IconButton(
                            onClick = { isPlaying = !isPlaying },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(RabtaGreenPrimary)
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play/Pause",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            // Waveform simulation
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                val heights = listOf(8, 16, 24, 12, 20, 14, 28, 18, 10, 22, 16, 8)
                                heights.forEach { h ->
                                    Box(
                                        modifier = Modifier
                                            .width(3.dp)
                                            .height(h.dp)
                                            .background(RabtaGreenPrimary, RoundedCornerShape(2.dp))
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "0:${if (message.voiceDurationSeconds < 10) "0${message.voiceDurationSeconds}" else "${message.voiceDurationSeconds}"}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Voice Transcription Section
                    if (message.isTranscribing) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .background(RabtaGreenPrimary.copy(alpha = 0.08f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(12.dp),
                                strokeWidth = 2.dp,
                                color = RabtaGreenPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Transcribing voice audio...", fontSize = 11.sp, color = RabtaGreenPrimary)
                        }
                    } else if (message.transcription != null) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Translate, contentDescription = null, tint = RabtaGreenPrimary, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Audio Transcription:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RabtaGreenPrimary)
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "\"${message.transcription}\"",
                                    fontSize = 12.sp,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                )
                            }
                        }
                    } else {
                        // Transcribe Button
                        FilledTonalButton(
                            onClick = onTranscribe,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .height(32.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(containerColor = RabtaGreenPrimary.copy(alpha = 0.12f))
                        ) {
                            Icon(Icons.Default.Translate, contentDescription = null, tint = RabtaGreenPrimary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Transcribe Voice Note", fontSize = 11.sp, color = RabtaGreenPrimary, fontWeight = FontWeight.SemiBold)
                        }
                    }
                } else {
                    Text(
                        text = message.content,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message.timestamp,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (isOut) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = when (message.status) {
                                MessageStatus.READ -> Icons.Default.DoneAll
                                MessageStatus.DELIVERED -> Icons.Default.DoneAll
                                else -> Icons.Default.Done
                            },
                            contentDescription = null,
                            tint = if (message.status == MessageStatus.READ) Color(0xFF53BDEB) else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AttachmentIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = label, tint = Color.White, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
    }
}
