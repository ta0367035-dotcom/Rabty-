package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.CountryList
import com.example.localization.AppLanguage
import com.example.localization.RabtaStrings
import com.example.model.Country
import com.example.ui.RabtaViewModel
import com.example.ui.Screen
import com.example.ui.theme.*

@Composable
fun SplashScreen(viewModel: RabtaViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(RabtaGreenPrimary, Color(0xFF042B15))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // App Emblem
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.rabta_brand_icon),
                    contentDescription = "Rabta Logo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "رابطہ",
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "RABTA",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 4.sp,
                color = RabtaGold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Connect Everyone, Everywhere",
                fontSize = 15.sp,
                color = Color.White.copy(alpha = 0.9f)
            )

            Text(
                text = "ہر وقت، ہر جگہ، آپ کے ساتھ",
                fontSize = 16.sp,
                color = RabtaGold,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            CircularProgressIndicator(
                color = RabtaGold,
                modifier = Modifier.size(32.dp),
                strokeWidth = 3.dp
            )

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = RabtaGold,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "End-to-End Encrypted Platform",
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun OnboardingScreen(viewModel: RabtaViewModel, language: AppLanguage) {
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(20.dp))

                // Hero Graphic
                Card(
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.rabta_hero_banner),
                        contentDescription = "Rabta Hero Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Welcome to Rabta",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = RabtaGreenPrimary
                )

                Text(
                    text = "رابطہ میں خوش آمدید",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = RabtaGold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "A world-class global communication platform. Chat, crystal-clear audio & video calls, status updates, and dedicated SIM services.",
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = RabtaStrings.get("terms_agree", language),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.navigateTo(Screen.PHONE_REGISTRATION) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("btn_agree_continue"),
                    colors = ButtonDefaults.buttonColors(containerColor = RabtaGreenPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Agree & Continue",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = { viewModel.navigateTo(Screen.LANGUAGE_SETTINGS) },
                    modifier = Modifier.testTag("btn_choose_language")
                ) {
                    Icon(Icons.Default.Language, contentDescription = null, tint = RabtaGreenPrimary)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Language: ${language.displayName} (${language.nativeName})",
                        color = RabtaGreenPrimary,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneRegistrationScreen(viewModel: RabtaViewModel, language: AppLanguage) {
    var selectedCountry by remember { mutableStateOf(viewModel.regCountry.value) }
    var phoneNumber by remember { mutableStateOf(viewModel.regPhoneNumber.value) }
    var showCountryPicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Enter Your Phone Number") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(Screen.ONBOARDING) }) {
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Rabta will send a 6-digit SMS code to verify your phone number. Carrier SMS charges may apply.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Country Selector Card
                OutlinedCard(
                    onClick = { showCountryPicker = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("country_selector_btn"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = selectedCountry.flag, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = selectedCountry.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = selectedCountry.dialCode,
                                fontSize = 16.sp,
                                color = RabtaGreenPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Phone Input
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    leadingIcon = {
                        Text(
                            text = "${selectedCountry.dialCode} ",
                            fontWeight = FontWeight.Bold,
                            color = RabtaGreenPrimary,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("phone_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            Button(
                onClick = {
                    viewModel.regCountry.value = selectedCountry
                    viewModel.regPhoneNumber.value = phoneNumber
                    viewModel.navigateTo(Screen.OTP_VERIFICATION)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("btn_next_otp"),
                colors = ButtonDefaults.buttonColors(containerColor = RabtaGreenPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Next",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Country picker bottom sheet
        if (showCountryPicker) {
            var searchQuery by remember { mutableStateOf("") }
            val filteredCountries = remember(searchQuery) {
                if (searchQuery.isBlank()) CountryList.allCountries
                else CountryList.allCountries.filter {
                    it.name.contains(searchQuery, ignoreCase = true) || it.dialCode.contains(searchQuery)
                }
            }

            ModalBottomSheet(
                onDismissRequest = { showCountryPicker = false }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Choose a Country",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search country or code...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyColumn(modifier = Modifier.heightIn(max = 380.dp)) {
                        items(filteredCountries) { country ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedCountry = country
                                        showCountryPicker = false
                                    }
                                    .padding(vertical = 12.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = country.flag, fontSize = 22.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(text = country.name, fontSize = 16.sp)
                                }
                                Text(
                                    text = country.dialCode,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = RabtaGreenPrimary
                                )
                            }
                            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerificationScreen(viewModel: RabtaViewModel, language: AppLanguage) {
    var otpCode by remember { mutableStateOf(viewModel.regOtp.value) }
    var secondsRemaining by remember { mutableStateOf(45) }

    LaunchedEffect(Unit) {
        while (secondsRemaining > 0) {
            kotlinx.coroutines.delay(1000)
            secondsRemaining--
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(RabtaStrings.get("otp_verify_title", language)) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(Screen.PHONE_REGISTRATION) }) {
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${RabtaStrings.get("otp_sent_to", language)} ${viewModel.regCountry.value.dialCode} ${viewModel.regPhoneNumber.value}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(30.dp))

                // OTP Display Box
                OutlinedTextField(
                    value = otpCode,
                    onValueChange = { if (it.length <= 6) otpCode = it },
                    label = { Text("6-Digit Code") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("otp_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 24.sp,
                        letterSpacing = 8.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                if (secondsRemaining > 0) {
                    Text(
                        text = "Resend OTP in 00:${if (secondsRemaining < 10) "0$secondsRemaining" else secondsRemaining}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    TextButton(onClick = { secondsRemaining = 60 }) {
                        Text("Resend SMS Code", color = RabtaGreenPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Button(
                onClick = {
                    viewModel.regOtp.value = otpCode
                    viewModel.navigateTo(Screen.PROFILE_SETUP)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("btn_verify_otp"),
                colors = ButtonDefaults.buttonColors(containerColor = RabtaGreenPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = RabtaStrings.get("verify_btn", language),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(viewModel: RabtaViewModel, language: AppLanguage) {
    var fullName by remember { mutableStateOf(viewModel.regFullName.value) }
    var email by remember { mutableStateOf(viewModel.regEmail.value) }
    var statusBio by remember { mutableStateOf("Available on Rabta 🇵🇰") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile Information") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = RabtaGreenPrimary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Please provide your name and an optional profile photo for contacts on Rabta.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Avatar with Camera icon
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(RabtaGreenPrimary.copy(alpha = 0.15f))
                        .border(2.dp, RabtaGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Change photo",
                        tint = RabtaGreenPrimary,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name (Required)") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("fullname_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address (Optional)") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = statusBio,
                    onValueChange = { statusBio = it },
                    label = { Text("About / Status Bio") },
                    leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    viewModel.regFullName.value = fullName
                    viewModel.regEmail.value = email
                    viewModel.onRegisterSubmitted()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("btn_submit_profile"),
                colors = ButtonDefaults.buttonColors(containerColor = RabtaGreenPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Submit for Approval",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun PendingApprovalScreen(viewModel: RabtaViewModel, language: AppLanguage) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(20.dp))

                // Pending Approval Shield Icon
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(RabtaGold.copy(alpha = 0.15f))
                        .border(2.dp, RabtaGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.HourglassTop,
                        contentDescription = "Pending",
                        tint = RabtaGold,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = RabtaStrings.get("pending_approval", language),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = RabtaGreenPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = RabtaStrings.get("pending_approval_desc", language),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Verification details card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Applicant:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = viewModel.regFullName.value, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Phone Number:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                text = "${viewModel.regCountry.value.dialCode} ${viewModel.regPhoneNumber.value}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Status:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = RabtaGold.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "Under Review",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB45309),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Instant Demo Approval Button
                Button(
                    onClick = { viewModel.approveAccountFromDemo() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_instant_approve_demo"),
                    colors = ButtonDefaults.buttonColors(containerColor = RabtaGreenPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Enter App (Admin Approved)")
                }

                // Check Status via Admin Panel
                OutlinedButton(
                    onClick = { viewModel.navigateTo(Screen.ADMIN_DASHBOARD) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_open_admin_panel"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Outlined.AdminPanelSettings, contentDescription = null, tint = RabtaGreenPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Open Admin Verification Portal", color = RabtaGreenPrimary)
                }

                TextButton(
                    onClick = { viewModel.navigateTo(Screen.SUPPORT) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Contact Support Regarding Verification", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
