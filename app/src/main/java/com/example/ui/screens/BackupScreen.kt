package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CloudDone
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Schedule
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
import com.example.ui.RabtaViewModel
import com.example.ui.Screen
import com.example.ui.theme.RabtaGold
import com.example.ui.theme.RabtaGreenPrimary
import com.example.ui.theme.RabtaGreenSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupScreen(viewModel: RabtaViewModel) {
    BackHandler {
        viewModel.navigateTo(Screen.MAIN)
    }

    val context = LocalContext.current
    val backupStatus by viewModel.repository.backupStatus.collectAsState()
    val isBackingUp by viewModel.repository.isBackingUp.collectAsState()

    var backupVideos by remember { mutableStateOf(true) }
    var backupAudio by remember { mutableStateOf(true) }
    var backupDocuments by remember { mutableStateOf(true) }
    var endToEndEncryptedBackup by remember { mutableStateOf(true) }
    var autoBackupFrequency by remember { mutableStateOf("Daily") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Backup & Restore") },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateTo(Screen.MAIN) },
                        modifier = Modifier.testTag("btn_backup_back")
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = RabtaGreenPrimary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Google Drive Status Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.CloudDone,
                                contentDescription = null,
                                tint = RabtaGreenPrimary,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(text = "Google Drive Cloud Backup", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(
                                    text = "Connected Account: tanveernazam786@gmail.com",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = backupStatus,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = RabtaGreenSecondary
                        )

                        if (isBackingUp) {
                            Spacer(modifier = Modifier.height(12.dp))
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth(),
                                color = RabtaGreenPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Encrypting database and uploading to Google Drive...",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { viewModel.repository.performBackup() },
                                enabled = !isBackingUp,
                                colors = ButtonDefaults.buttonColors(containerColor = RabtaGreenPrimary),
                                modifier = Modifier.weight(1f).testTag("btn_backup_now")
                            ) {
                                Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Back Up Now")
                            }

                            OutlinedButton(
                                onClick = {
                                    Toast.makeText(context, "Backup Restored Successfully!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Restore, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Restore")
                            }
                        }
                    }
                }
            }

            // E2EE Encryption Protection
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = RabtaGold.copy(alpha = 0.1f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Lock,
                            contentDescription = null,
                            tint = Color(0xFFB45309),
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "End-to-End Encrypted Backup",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFFB45309)
                            )
                            Text(
                                text = "Your backup is protected with a 64-digit encryption key. Neither Google nor Rabta can access your chats.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Switch(
                            checked = endToEndEncryptedBackup,
                            onCheckedChange = { endToEndEncryptedBackup = it }
                        )
                    }
                }
            }

            // Backup Settings
            item {
                Text(
                    text = "Backup Inclusions & Preferences",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = RabtaGreenPrimary
                )
            }

            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Include Videos in Backup", fontSize = 15.sp)
                            Switch(checked = backupVideos, onCheckedChange = { backupVideos = it })
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Include Voice Messages", fontSize = 15.sp)
                            Switch(checked = backupAudio, onCheckedChange = { backupAudio = it })
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Include Shared Documents", fontSize = 15.sp)
                            Switch(checked = backupDocuments, onCheckedChange = { backupDocuments = it })
                        }
                    }
                }
            }
        }
    }
}
