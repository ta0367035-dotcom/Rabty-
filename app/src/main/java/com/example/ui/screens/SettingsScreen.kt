package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.RabtaStrings
import com.example.ui.RabtaViewModel
import com.example.ui.Screen
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    viewModel: RabtaViewModel,
    language: AppLanguage
) {
    val currentUser by viewModel.repository.currentUser.collectAsState()
    var showThemeDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // User Profile Header
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(RabtaGreenPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentUser.name.take(2).uppercase(),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentUser.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = currentUser.phoneNumber,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = currentUser.statusBio,
                            fontSize = 13.sp,
                            color = RabtaGold
                        )
                    }

                    IconButton(onClick = { }) {
                        Icon(Icons.Default.QrCode, contentDescription = "QR Code", tint = RabtaGreenPrimary)
                    }
                }
            }
        }

        // Section: VIP Admin Console
        item {
            SettingsCategoryHeader(title = "VIP Administration")
            SettingsItemRow(
                icon = Icons.Outlined.AdminPanelSettings,
                iconColor = RabtaGold,
                title = "Admin Dashboard",
                subtitle = "User approval queue, platform metrics, support tickets",
                onClick = { viewModel.navigateTo(Screen.ADMIN_DASHBOARD) }
            )
        }

        // Section: Account & Security
        item {
            SettingsCategoryHeader(title = "Account & Privacy")
            SettingsItemRow(
                icon = Icons.Outlined.Key,
                iconColor = RabtaGreenPrimary,
                title = "Account & Security",
                subtitle = "Security notifications, two-step verification, change number",
                onClick = { }
            )
            SettingsItemRow(
                icon = Icons.Outlined.Lock,
                iconColor = RabtaGreenPrimary,
                title = "Privacy & Encryption",
                subtitle = "End-to-end encryption keys, disappearing messages",
                onClick = { }
            )
            SettingsItemRow(
                icon = Icons.Outlined.Devices,
                iconColor = RabtaGreenPrimary,
                title = "Linked Devices",
                subtitle = "Link Rabta Web and Desktop applications via QR code",
                onClick = { }
            )
        }

        // Section: Data & Backup
        item {
            SettingsCategoryHeader(title = "Chats & Storage")
            SettingsItemRow(
                icon = Icons.Outlined.CloudUpload,
                iconColor = Color(0xFF2563EB),
                title = "Backup & Restore",
                subtitle = "Google Drive cloud backup, automatic encrypted backups",
                onClick = { viewModel.navigateTo(Screen.BACKUP_RESTORE) }
            )
            SettingsItemRow(
                icon = Icons.Outlined.Storage,
                iconColor = RabtaGreenPrimary,
                title = "Storage & Data",
                subtitle = "Network usage, auto-download settings, SIM data saver",
                onClick = { }
            )
            SettingsItemRow(
                icon = Icons.Outlined.Notifications,
                iconColor = RabtaGreenPrimary,
                title = "Notifications",
                subtitle = "Message, group & incoming call ringtones, high priority",
                onClick = { }
            )
        }

        // Section: App Preferences
        item {
            SettingsCategoryHeader(title = "Preferences")
            SettingsItemRow(
                icon = Icons.Outlined.Language,
                iconColor = RabtaGreenPrimary,
                title = "App Language",
                subtitle = "${language.displayName} (${language.nativeName})",
                onClick = { viewModel.navigateTo(Screen.LANGUAGE_SETTINGS) }
            )
            SettingsItemRow(
                icon = Icons.Outlined.DarkMode,
                iconColor = RabtaGreenPrimary,
                title = "Appearance",
                subtitle = "Theme mode (Light, Dark, System Default)",
                onClick = { showThemeDialog = true }
            )
        }

        // Section: Support & Info
        item {
            SettingsCategoryHeader(title = "Support & Legal")
            SettingsItemRow(
                icon = Icons.Outlined.HelpOutline,
                iconColor = RabtaSimAmber,
                title = "Help & Customer Support",
                subtitle = "Submit issue tickets, contact support, FAQs",
                onClick = { viewModel.navigateTo(Screen.SUPPORT) }
            )
            SettingsItemRow(
                icon = Icons.Outlined.Info,
                iconColor = RabtaGreenPrimary,
                title = "About Rabta",
                subtitle = "Version 1.0.0 (Global Edition) • Connect Everyone Everywhere",
                onClick = { showAboutDialog = true }
            )
            SettingsItemRow(
                icon = Icons.Outlined.DeleteOutline,
                iconColor = RabtaCallRed,
                title = "Delete My Account",
                subtitle = "Permanently remove your account, chats and encryption keys",
                onClick = { }
            )
        }
    }

    // Appearance Dialog
    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Choose Appearance", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.repository.setThemeMode(null)
                                showThemeDialog = false
                            }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = viewModel.repository.isDarkTheme.collectAsState().value == null, onClick = null)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("System Default")
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.repository.setThemeMode(false)
                                showThemeDialog = false
                            }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = viewModel.repository.isDarkTheme.collectAsState().value == false, onClick = null)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Light Mode")
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.repository.setThemeMode(true)
                                showThemeDialog = false
                            }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = viewModel.repository.isDarkTheme.collectAsState().value == true, onClick = null)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Dark Mode")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) { Text("Close") }
            }
        )
    }

    // About Rabta Dialog
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🇵🇰", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("RABTA (رابطہ)", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Connect Everyone, Everywhere.", fontWeight = FontWeight.SemiBold, color = RabtaGreenPrimary)
                    Text(text = "ہر وقت، ہر جگہ، آپ کے ساتھ", color = RabtaGold, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "RABTA is an independently developed, world-class communication platform providing end-to-end encrypted messaging, crystal-clear audio & video calls, status updates, native cellular SIM Calling, and cloud backup.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Version: 1.0.0 VIP Release\nBuilt with modern Jetpack Compose & Kotlin.", fontSize = 12.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) { Text("OK") }
            }
        )
    }
}

@Composable
fun SettingsCategoryHeader(title: String) {
    Text(
        text = title,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = RabtaGreenPrimary,
        modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 6.dp)
    )
}

@Composable
fun SettingsItemRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(22.dp))
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
