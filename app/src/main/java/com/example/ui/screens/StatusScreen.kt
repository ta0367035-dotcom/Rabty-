package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.RabtaStrings
import com.example.model.StatusUpdate
import com.example.ui.RabtaViewModel
import com.example.ui.theme.RabtaGold
import com.example.ui.theme.RabtaGreenPrimary
import com.example.ui.theme.RabtaGreenSecondary
import com.example.ui.theme.RabtaStatusUnseen

@Composable
fun StatusScreen(
    viewModel: RabtaViewModel,
    language: AppLanguage
) {
    val statusUpdates by viewModel.repository.statusUpdates.collectAsState()
    val viewingStatus by viewModel.viewingStatus.collectAsState()
    var showAddStatusDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // My Status Card
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showAddStatusDialog = true }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(RabtaGreenPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("TN", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(RabtaGreenSecondary)
                                .align(Alignment.BottomEnd),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = RabtaStrings.get("my_status", language),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = RabtaStrings.get("tap_to_add_status", language),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.padding(top = 14.dp)
                )
            }

            // Recent Updates Header
            item {
                Text(
                    text = RabtaStrings.get("recent_updates", language),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Status items
            items(statusUpdates, key = { it.id }) { update ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.viewStatusStory(update) }
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Status Ring
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .border(
                                width = 2.5.dp,
                                color = if (update.isViewed) Color.Gray.copy(alpha = 0.5f) else RabtaStatusUnseen,
                                shape = CircleShape
                            )
                            .padding(3.dp)
                            .clip(CircleShape)
                            .background(Color(update.backgroundHex)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = update.userName.take(2).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = update.userName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = update.timestamp,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.Visibility,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${update.viewCount}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Add Status Button
        FloatingActionButton(
            onClick = { showAddStatusDialog = true },
            containerColor = RabtaGreenPrimary,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .testTag("fab_add_status")
        ) {
            Icon(Icons.Default.CameraAlt, contentDescription = "Add Status")
        }
    }

    // Add Status Dialog
    if (showAddStatusDialog) {
        var statusText by remember { mutableStateOf("") }
        var selectedColor by remember { mutableStateOf(0xFF085429) }

        AlertDialog(
            onDismissRequest = { showAddStatusDialog = false },
            title = { Text("Update Rabta Status", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Status updates disappear automatically after 24 hours.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedTextField(
                        value = statusText,
                        onValueChange = { statusText = it },
                        label = { Text("What's on your mind?") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        listOf(0xFF085429, 0xFF107C41, 0xFF1E3A8A, 0xFF7C2D12, 0xFF581C87).forEach { colorHex ->
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(colorHex))
                                    .clickable { selectedColor = colorHex }
                                    .then(
                                        if (selectedColor == colorHex) Modifier.border(2.dp, RabtaGold, CircleShape)
                                        else Modifier
                                    )
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (statusText.isNotBlank()) {
                            viewModel.repository.addStatus(statusText, selectedColor)
                        }
                        showAddStatusDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RabtaGreenPrimary)
                ) {
                    Text("Post Status")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddStatusDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Story Preview Modal
    viewingStatus?.let { status ->
        AlertDialog(
            onDismissRequest = { viewModel.dismissStatusStory() },
            containerColor = Color(status.backgroundHex),
            titleContentColor = Color.White,
            textContentColor = Color.White,
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(status.userName, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                        Text(status.timestamp, fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                    IconButton(onClick = { viewModel.dismissStatusStory() }) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }
            },
            text = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = status.textContent,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissStatusStory() }) {
                    Text("Close", color = RabtaGold, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
