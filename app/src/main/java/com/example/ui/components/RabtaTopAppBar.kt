package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Security
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
import com.example.ui.theme.RabtaGold
import com.example.ui.theme.RabtaGreenPrimary
import com.example.ui.theme.RabtaSimBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RabtaTopAppBar(
    viewModel: RabtaViewModel,
    language: AppLanguage,
    onSearchClick: () -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = RabtaGreenPrimary,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 4.dp)
            ) {
                // Pakistan flag crescent badge
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0F5132)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🇵🇰",
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "رابطہ",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Rabta",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RabtaGold
                        )
                    }
                    Text(
                        text = RabtaStrings.get("tagline_urdu", language),
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        },
        actions = {
            // Direct Native SIM Call quick trigger
            IconButton(
                onClick = {
                    viewModel.promptSimCall("Cellular Call (SIM)", "+923001234567")
                },
                modifier = Modifier.testTag("action_sim_call")
            ) {
                Icon(
                    imageVector = Icons.Default.PhoneInTalk,
                    contentDescription = "SIM Call",
                    tint = RabtaGold
                )
            }

            // Search
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier.testTag("action_search")
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White
                )
            }

            // 3 dots menu
            Box {
                IconButton(
                    onClick = { showMenu = true },
                    modifier = Modifier.testTag("action_menu")
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Admin Dashboard (VIP)") },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.AdminPanelSettings,
                                contentDescription = null,
                                tint = RabtaGreenPrimary
                            )
                        },
                        onClick = {
                            showMenu = false
                            viewModel.navigateTo(Screen.ADMIN_DASHBOARD)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Backup & Restore") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.CloudSync,
                                contentDescription = null,
                                tint = RabtaGreenPrimary
                            )
                        },
                        onClick = {
                            showMenu = false
                            viewModel.navigateTo(Screen.BACKUP_RESTORE)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Help & Support Tickets") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.HelpOutline,
                                contentDescription = null,
                                tint = RabtaGreenPrimary
                            )
                        },
                        onClick = {
                            showMenu = false
                            viewModel.navigateTo(Screen.SUPPORT)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("App Language") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Language,
                                contentDescription = null,
                                tint = RabtaGreenPrimary
                            )
                        },
                        onClick = {
                            showMenu = false
                            viewModel.navigateTo(Screen.LANGUAGE_SETTINGS)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Pending Approval Preview") },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Security,
                                contentDescription = null,
                                tint = RabtaGold
                            )
                        },
                        onClick = {
                            showMenu = false
                            viewModel.repository.resetToPendingApproval()
                            viewModel.navigateTo(Screen.PENDING_APPROVAL)
                        }
                    )
                }
            }
        }
    )
}
