package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CallEnd
import androidx.compose.material.icons.outlined.PhoneInTalk
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.RabtaStrings
import com.example.model.CallDirection
import com.example.model.CallRecord
import com.example.model.CallType
import com.example.ui.RabtaViewModel
import com.example.ui.theme.*

@Composable
fun CallsScreen(
    viewModel: RabtaViewModel,
    language: AppLanguage
) {
    val calls by viewModel.repository.calls.collectAsState()
    var showNewCallDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // SIM Call Feature Highlight Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = RabtaSimBlue.copy(alpha = 0.08f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(RabtaSimBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.PhoneInTalk,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Rabta SIM Cellular Calling",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = RabtaSimBlue
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Dial mobile network numbers via your native SIM when internet or data is unavailable.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = RabtaStrings.get("call_history", language),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(calls, key = { it.id }) { call ->
                CallRecordRow(
                    call = call,
                    onAudioCall = {
                        viewModel.startCall(call.contactName, call.contactPhone, CallType.AUDIO)
                    },
                    onVideoCall = {
                        viewModel.startCall(call.contactName, call.contactPhone, CallType.VIDEO)
                    },
                    onSimCall = {
                        viewModel.promptSimCall(call.contactName, call.contactPhone)
                    }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            }
        }

        // New Call Dial Floating Action Button
        FloatingActionButton(
            onClick = { showNewCallDialog = true },
            containerColor = RabtaGreenPrimary,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .testTag("fab_new_call")
        ) {
            Icon(Icons.Default.Call, contentDescription = "New Call")
        }
    }

    if (showNewCallDialog) {
        var dialPhone by remember { mutableStateOf("") }
        var contactName by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showNewCallDialog = false },
            title = { Text("Start a Call", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = contactName,
                        onValueChange = { contactName = it },
                        label = { Text("Contact Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = dialPhone,
                        onValueChange = { dialPhone = it },
                        label = { Text("Phone Number (+92 ...)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            showNewCallDialog = false
                            viewModel.startCall(contactName.ifBlank { "Contact" }, dialPhone, CallType.AUDIO)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RabtaGreenPrimary)
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Audio")
                    }
                    Button(
                        onClick = {
                            showNewCallDialog = false
                            viewModel.startCall(contactName.ifBlank { "Contact" }, dialPhone, CallType.VIDEO)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RabtaGreenSecondary)
                    ) {
                        Icon(Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Video")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewCallDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun CallRecordRow(
    call: CallRecord,
    onAudioCall: () -> Unit,
    onVideoCall: () -> Unit,
    onSimCall: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    if (call.type == CallType.SIM_CALL) RabtaSimBlue
                    else RabtaGreenSecondary
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = when (call.type) {
                    CallType.VIDEO -> Icons.Default.Videocam
                    CallType.SIM_CALL -> Icons.Default.PhoneInTalk
                    else -> Icons.Default.Call
                },
                contentDescription = null,
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = call.contactName,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = when (call.direction) {
                        CallDirection.INCOMING -> Icons.Default.CallReceived
                        CallDirection.OUTGOING -> Icons.Default.CallMade
                        CallDirection.MISSED -> Icons.Default.CallMissed
                    },
                    contentDescription = null,
                    tint = if (call.direction == CallDirection.MISSED) RabtaCallRed else RabtaGreenSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${call.timestamp} • ${if (call.type == CallType.SIM_CALL) "SIM" else "Internet"}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (call.type == CallType.SIM_CALL) {
                IconButton(onClick = onSimCall) {
                    Icon(Icons.Default.PhoneInTalk, contentDescription = "SIM Dial", tint = RabtaSimBlue)
                }
            } else if (call.type == CallType.VIDEO) {
                IconButton(onClick = onVideoCall) {
                    Icon(Icons.Default.Videocam, contentDescription = "Video Call", tint = RabtaGreenPrimary)
                }
            } else {
                IconButton(onClick = onAudioCall) {
                    Icon(Icons.Default.Call, contentDescription = "Audio Call", tint = RabtaGreenPrimary)
                }
            }
        }
    }
}
