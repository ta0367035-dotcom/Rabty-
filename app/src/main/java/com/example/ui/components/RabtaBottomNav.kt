package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.example.localization.AppLanguage
import com.example.localization.RabtaStrings
import com.example.ui.MainTab
import com.example.ui.RabtaViewModel
import com.example.ui.theme.RabtaGold
import com.example.ui.theme.RabtaGreenPrimary
import com.example.ui.theme.RabtaGreenSecondary

@Composable
fun RabtaBottomNav(
    viewModel: RabtaViewModel,
    activeTab: MainTab,
    language: AppLanguage,
    unreadChatsCount: Int = 8
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        windowInsets = WindowInsets.navigationBars,
        tonalElevation = NavigationBarDefaults.Elevation
    ) {
        // 1. Chats
        NavigationBarItem(
            selected = activeTab == MainTab.CHATS,
            onClick = { viewModel.selectTab(MainTab.CHATS) },
            icon = {
                BadgedBox(
                    badge = {
                        if (unreadChatsCount > 0) {
                            Badge(containerColor = RabtaGreenSecondary, contentColor = Color.White) {
                                Text("$unreadChatsCount")
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (activeTab == MainTab.CHATS) Icons.Filled.Chat else Icons.Outlined.Chat,
                        contentDescription = RabtaStrings.get("chats", language)
                    )
                }
            },
            label = { Text(RabtaStrings.get("chats", language)) },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = RabtaGreenPrimary.copy(alpha = 0.15f),
                selectedIconColor = RabtaGreenPrimary,
                selectedTextColor = RabtaGreenPrimary
            ),
            modifier = Modifier.testTag("nav_chats")
        )

        // 2. Groups
        NavigationBarItem(
            selected = activeTab == MainTab.GROUPS,
            onClick = { viewModel.selectTab(MainTab.GROUPS) },
            icon = {
                Icon(
                    imageVector = if (activeTab == MainTab.GROUPS) Icons.Filled.Groups else Icons.Outlined.Groups,
                    contentDescription = RabtaStrings.get("groups", language)
                )
            },
            label = { Text(RabtaStrings.get("groups", language)) },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = RabtaGreenPrimary.copy(alpha = 0.15f),
                selectedIconColor = RabtaGreenPrimary,
                selectedTextColor = RabtaGreenPrimary
            ),
            modifier = Modifier.testTag("nav_groups")
        )

        // 3. Status
        NavigationBarItem(
            selected = activeTab == MainTab.STATUS,
            onClick = { viewModel.selectTab(MainTab.STATUS) },
            icon = {
                Icon(
                    imageVector = if (activeTab == MainTab.STATUS) Icons.Filled.DonutLarge else Icons.Outlined.DonutLarge,
                    contentDescription = RabtaStrings.get("status", language)
                )
            },
            label = { Text(RabtaStrings.get("status", language)) },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = RabtaGreenPrimary.copy(alpha = 0.15f),
                selectedIconColor = RabtaGreenPrimary,
                selectedTextColor = RabtaGreenPrimary
            ),
            modifier = Modifier.testTag("nav_status")
        )

        // 4. Calls
        NavigationBarItem(
            selected = activeTab == MainTab.CALLS,
            onClick = { viewModel.selectTab(MainTab.CALLS) },
            icon = {
                Icon(
                    imageVector = if (activeTab == MainTab.CALLS) Icons.Filled.Call else Icons.Outlined.Call,
                    contentDescription = RabtaStrings.get("calls", language)
                )
            },
            label = { Text(RabtaStrings.get("calls", language)) },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = RabtaGreenPrimary.copy(alpha = 0.15f),
                selectedIconColor = RabtaGreenPrimary,
                selectedTextColor = RabtaGreenPrimary
            ),
            modifier = Modifier.testTag("nav_calls")
        )

        // 5. Settings
        NavigationBarItem(
            selected = activeTab == MainTab.SETTINGS,
            onClick = { viewModel.selectTab(MainTab.SETTINGS) },
            icon = {
                Icon(
                    imageVector = if (activeTab == MainTab.SETTINGS) Icons.Filled.Settings else Icons.Outlined.Settings,
                    contentDescription = RabtaStrings.get("settings", language)
                )
            },
            label = { Text(RabtaStrings.get("settings", language)) },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = RabtaGreenPrimary.copy(alpha = 0.15f),
                selectedIconColor = RabtaGreenPrimary,
                selectedTextColor = RabtaGreenPrimary
            ),
            modifier = Modifier.testTag("nav_settings")
        )
    }
}
