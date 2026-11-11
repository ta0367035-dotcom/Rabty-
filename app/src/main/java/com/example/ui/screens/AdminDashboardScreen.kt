package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.model.AccountStatus
import com.example.model.RabtaUser
import com.example.model.TicketStatus
import com.example.ui.RabtaViewModel
import com.example.ui.Screen
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(viewModel: RabtaViewModel) {
    BackHandler {
        viewModel.navigateTo(Screen.MAIN)
    }

    val allUsers by viewModel.repository.allUsersForAdmin.collectAsState()
    val allTickets by viewModel.repository.supportTickets.collectAsState()

    var selectedAdminTab by remember { mutableStateOf(0) } // 0: Overview & Users, 1: Support Tickets
    var userFilter by remember { mutableStateOf<AccountStatus?>(null) }
    var selectedUserForDetail by remember { mutableStateOf<RabtaUser?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("RABTA Admin Console", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = RabtaGold
                            ) {
                                Text(
                                    text = "SUPER ADMIN",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Global Security & User Verification",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateTo(Screen.MAIN) },
                        modifier = Modifier.testTag("btn_admin_back")
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Tab Selector
            TabRow(
                selectedTabIndex = selectedAdminTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = RabtaGreenPrimary
            ) {
                Tab(
                    selected = selectedAdminTab == 0,
                    onClick = { selectedAdminTab = 0 },
                    text = { Text("User Approvals (${allUsers.size})") }
                )
                Tab(
                    selected = selectedAdminTab == 1,
                    onClick = { selectedAdminTab = 1 },
                    text = { Text("Support Tickets (${allTickets.size})") }
                )
            }

            if (selectedAdminTab == 0) {
                // Metrics & User Management
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Privacy Guarantee Banner
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = RabtaGold.copy(alpha = 0.12f)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Security, contentDescription = null, tint = Color(0xFFB45309))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Zero-Knowledge Architecture: Admins have account verification rights but ZERO access to users' end-to-end encrypted chats or media.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    // Key Metric Cards
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MetricCard(
                                label = "Pending Approvals",
                                value = "${allUsers.count { it.accountStatus == AccountStatus.APPROVAL_PENDING }}",
                                color = RabtaGoldBright,
                                modifier = Modifier.weight(1f)
                            )
                            MetricCard(
                                label = "Approved Users",
                                value = "${allUsers.count { it.accountStatus == AccountStatus.APPROVED }}",
                                color = RabtaGreenSecondary,
                                modifier = Modifier.weight(1f)
                            )
                            MetricCard(
                                label = "Suspended",
                                value = "${allUsers.count { it.accountStatus == AccountStatus.SUSPENDED }}",
                                color = RabtaCallRed,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Filter Chips
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = userFilter == null,
                                onClick = { userFilter = null },
                                label = { Text("All") }
                            )
                            FilterChip(
                                selected = userFilter == AccountStatus.APPROVAL_PENDING,
                                onClick = { userFilter = AccountStatus.APPROVAL_PENDING },
                                label = { Text("Pending") }
                            )
                            FilterChip(
                                selected = userFilter == AccountStatus.APPROVED,
                                onClick = { userFilter = AccountStatus.APPROVED },
                                label = { Text("Approved") }
                            )
                            FilterChip(
                                selected = userFilter == AccountStatus.SUSPENDED,
                                onClick = { userFilter = AccountStatus.SUSPENDED },
                                label = { Text("Suspended") }
                            )
                        }
                    }

                    // Users List
                    val filteredList = if (userFilter == null) allUsers else allUsers.filter { it.accountStatus == userFilter }
                    items(filteredList) { user ->
                        AdminUserCard(
                            user = user,
                            onApprove = { viewModel.repository.adminApproveUser(user.id) },
                            onReject = { viewModel.repository.adminRejectUser(user.id) },
                            onSuspend = { viewModel.repository.adminSuspendUser(user.id) },
                            onReactivate = { viewModel.repository.adminReactivateUser(user.id) },
                            onViewDetails = { selectedUserForDetail = user }
                        )
                    }
                }
            } else {
                // Support Tickets List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(allTickets) { ticket ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = ticket.id, fontWeight = FontWeight.Bold, color = RabtaGreenPrimary, fontSize = 14.sp)
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = if (ticket.status == TicketStatus.RESOLVED) RabtaGreenSecondary.copy(alpha = 0.2f) else RabtaGold.copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = ticket.status.name,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (ticket.status == TicketStatus.RESOLVED) RabtaGreenPrimary else Color(0xFFB45309),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(text = ticket.subject, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "From: ${ticket.fullName} (${ticket.phone}) • ${ticket.createdAt}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = ticket.description, fontSize = 13.sp)

                                ticket.adminResponse?.let { resp ->
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Text(text = "Official Admin Response:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RabtaGreenPrimary)
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(text = resp, fontSize = 13.sp)
                                        }
                                    }
                                }

                                if (ticket.status != TicketStatus.RESOLVED) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Button(
                                        onClick = {
                                            viewModel.repository.adminReplyTicket(
                                                ticket.id,
                                                "Thank you for contacting Rabta Support. Your ticket has been processed and resolved.",
                                                TicketStatus.RESOLVED
                                            )
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = RabtaGreenPrimary),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Mark as Resolved & Send Response")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    selectedUserForDetail?.let { user ->
        AlertDialog(
            onDismissRequest = { selectedUserForDetail = null },
            title = { Text("User Verification Details", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("User ID: ${user.id}", fontSize = 13.sp)
                    Text("Name: ${user.name}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("Phone: ${user.phoneNumber}", fontSize = 14.sp)
                    Text("Country: ${user.country.name} (${user.country.flag})", fontSize = 14.sp)
                    Text("Email: ${user.email.ifBlank { "Not provided" }}", fontSize = 13.sp)
                    Text("Registered: ${user.registrationDate}", fontSize = 13.sp)
                    Text("Current Status: ${user.accountStatus.name}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedUserForDetail = null }) { Text("Close") }
            }
        )
    }
}

@Composable
fun MetricCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = label, fontSize = 11.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}

@Composable
fun AdminUserCard(
    user: RabtaUser,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onSuspend: () -> Unit,
    onReactivate: () -> Unit,
    onViewDetails: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = user.country.flag, fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = user.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = user.phoneNumber, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (user.accountStatus) {
                        AccountStatus.APPROVED -> RabtaGreenSecondary.copy(alpha = 0.2f)
                        AccountStatus.APPROVAL_PENDING -> RabtaGold.copy(alpha = 0.2f)
                        AccountStatus.SUSPENDED -> RabtaCallRed.copy(alpha = 0.2f)
                        else -> Color.Gray.copy(alpha = 0.2f)
                    }
                ) {
                    Text(
                        text = user.accountStatus.name.replace("_", " "),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (user.accountStatus) {
                            AccountStatus.APPROVED -> RabtaGreenPrimary
                            AccountStatus.APPROVAL_PENDING -> Color(0xFFB45309)
                            AccountStatus.SUSPENDED -> RabtaCallRed
                            else -> Color.Gray
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (user.accountStatus == AccountStatus.APPROVAL_PENDING) {
                    Button(
                        onClick = onApprove,
                        colors = ButtonDefaults.buttonColors(containerColor = RabtaGreenPrimary),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Approve")
                    }
                    OutlinedButton(
                        onClick = onReject,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = RabtaCallRed),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Reject")
                    }
                } else if (user.accountStatus == AccountStatus.APPROVED) {
                    OutlinedButton(
                        onClick = onSuspend,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = RabtaCallRed),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Suspend")
                    }
                    OutlinedButton(
                        onClick = onViewDetails,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Details")
                    }
                } else if (user.accountStatus == AccountStatus.SUSPENDED) {
                    Button(
                        onClick = onReactivate,
                        colors = ButtonDefaults.buttonColors(containerColor = RabtaGreenPrimary),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Reactivate")
                    }
                }
            }
        }
    }
}
