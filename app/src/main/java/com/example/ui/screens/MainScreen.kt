package com.example.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.localization.AppLanguage
import com.example.ui.MainTab
import com.example.ui.RabtaViewModel
import com.example.ui.components.RabtaBottomNav
import com.example.ui.components.RabtaTopAppBar
import com.example.ui.components.SimCallDialog
import com.example.ui.components.SimSmsDialog

@Composable
fun MainScreen(
    viewModel: RabtaViewModel,
    language: AppLanguage
) {
    val activeTab by viewModel.activeTab.collectAsState()
    val simCallTarget by viewModel.simCallTarget.collectAsState()
    val simSmsTarget by viewModel.simSmsTarget.collectAsState()

    Scaffold(
        topBar = {
            RabtaTopAppBar(
                viewModel = viewModel,
                language = language,
                onSearchClick = { }
            )
        },
        bottomBar = {
            RabtaBottomNav(
                viewModel = viewModel,
                activeTab = activeTab,
                language = language
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeTab) {
                MainTab.CHATS -> ChatsScreen(viewModel, language)
                MainTab.GROUPS -> GroupsScreen(viewModel, language)
                MainTab.STATUS -> StatusScreen(viewModel, language)
                MainTab.CALLS -> CallsScreen(viewModel, language)
                MainTab.SETTINGS -> SettingsScreen(viewModel, language)
            }
        }
    }

    // Cellular SIM Dialogs
    simCallTarget?.let { (name, phone) ->
        SimCallDialog(
            contactName = name,
            phoneNumber = phone,
            viewModel = viewModel,
            language = language
        )
    }

    simSmsTarget?.let { (name, phone) ->
        SimSmsDialog(
            contactName = name,
            phoneNumber = phone,
            viewModel = viewModel,
            language = language
        )
    }
}
