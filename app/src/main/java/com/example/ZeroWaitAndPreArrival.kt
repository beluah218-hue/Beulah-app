package com.example

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

// Data state for Pre-Arrival Prep
data class PreArrivalPrepState(
    var isFastingDone: Boolean = false,
    var bloodPressure: String = "",
    var bloodSugar: String = "",
    var temperature: String = "",
    var pulse: String = "",
    var uploadedReportsCount: Int = 0,
    var allergyNotes: String = "",
    var isPrePassGenerated: Boolean = false,
    var isCheckedInAtHospital: Boolean = false
)

object PreArrivalManager {
    var state by mutableStateOf(PreArrivalPrepState())
    var currentServingToken by mutableStateOf(39)
    var userTokenNumber by mutableStateOf(42)
}

@Composable
fun ZeroWaitQueueScreen(
    doctor: Doctor = DoctorRepository.doctors.first(),
    onNavigateToPrep: () -> Unit,
    onBackToHome: () -> Unit
) {
    val context = LocalContext.current
    val voiceOverHelper = LocalVoiceOverHelper.current
    val prepState = PreArrivalManager.state

    var servingToken by remember { mutableIntStateOf(PreArrivalManager.currentServingToken) }
    val userToken = PreArrivalManager.userTokenNumber
    val tokensAhead = maxOf(0, userToken - servingToken)
    val estimatedMins = tokensAhead * 4

    var isGeofenceCheckingIn by remember { mutableStateOf(false) }

    // Simulation ticker for token progress
    LaunchedEffect(Unit) {
        while (servingToken < userToken) {
            delay(15000) // update every 15s in simulation
            if (servingToken < userToken) {
                servingToken++
                PreArrivalManager.currentServingToken = servingToken
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBackToHome) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "Zero-Wait Queue".localized,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Apollo Clinic • OPD Desk 3".localized,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                VoiceOverIconButton(
                    onSpeak = {
                        val statusMsg = if (tokensAhead == 0) {
                            if (LanguageManager.currentLanguage == AppLanguage.TAMIL) {
                                "இப்போது உங்கள் முறை! மருத்துவர் ${doctor.name} அவர்களைச் சந்திக்க அறை 204-க்கு நேரடியாகச் செல்லவும்."
                            } else {
                                "It is your turn now! Please proceed directly to Room 204 for Dr. ${doctor.name}."
                            }
                        } else {
                            if (LanguageManager.currentLanguage == AppLanguage.TAMIL) {
                                "உங்கள் டோக்கன் $userToken. தற்போது அழைக்கப்படும் டோக்கன் $servingToken. உங்களுக்கு முன் $tokensAhead நோயாளிகள் உள்ளனர். தோராயமான காத்திருப்பு நேரம் $estimatedMins நிமிடங்கள்."
                            } else {
                                "Your token is $userToken. OPD is currently serving token $servingToken. There are $tokensAhead patients ahead. Estimated wait time is $estimatedMins minutes."
                            }
                        }
                        voiceOverHelper?.speak(statusMsg)
                    },
                    contentDescription = "Read Queue Status"
                )
            }
        }

        // Live Queue Status Radar Banner
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth().shadow(6.dp, RoundedCornerShape(24.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = if (prepState.isCheckedInAtHospital) Color(0xFF2E7D32) else Color(0xFFE65100),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(Color.White, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (prepState.isCheckedInAtHospital) "GEOFENCE VERIFIED".localized else "LIVE OPD RADAR".localized,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        if (prepState.isPrePassGenerated) {
                            Surface(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "⚡ EXPRESS PASS".localized,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Token Counters side by side
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "CURRENT SERVING".localized,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                            Text(
                                "#$servingToken",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text("Desk 3 • OPD", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }

                        Divider(
                            modifier = Modifier
                                .height(60.dp)
                                .width(1.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "YOUR TOKEN".localized,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                            Text(
                                "#$userToken",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Text("Fast-Track Token", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Estimated Wait Box
                    Surface(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.AccessTime,
                                    contentDescription = "Wait time",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = if (tokensAhead == 0) "NOW SERVING YOUR TURN!".localized else "Est. Wait Time: ~$estimatedMins mins".localized,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "$tokensAhead patients ahead in queue".localized,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            CircularProgressIndicator(
                                progress = { if (userToken == 0) 1f else (servingToken.toFloat() / userToken.toFloat()) },
                                modifier = Modifier.size(32.dp),
                                strokeWidth = 3.dp
                            )
                        }
                    }
                }
            }
        }

        // Doctor Info & Assigned Room
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = doctor.imageUrl,
                        contentDescription = doctor.name,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${doctor.titlePrefix} ${doctor.name}".trim(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = doctor.specialtyDisplay.localized,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Place, contentDescription = "Room", modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "OPD Room 204 • 2nd Floor, Apollo Clinic".localized,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Action 1: Geofence Hospital Arrival Check-In
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (prepState.isCheckedInAtHospital) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = "Check in",
                                tint = if (prepState.isCheckedInAtHospital) Color(0xFF2E7D32) else MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Hospital Arrival Check-In".localized,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (prepState.isCheckedInAtHospital) Color(0xFF1B5E20) else MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }

                        if (prepState.isCheckedInAtHospital) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Checked In", tint = Color(0xFF2E7D32))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (prepState.isCheckedInAtHospital)
                            "You are checked in! The nurse station at Room 204 has been notified of your presence.".localized
                        else
                            "Tap when you enter Apollo Clinic. Geofence & Bluetooth auto-checkin fast-tracks your token.".localized,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            isGeofenceCheckingIn = true
                            PreArrivalManager.state = PreArrivalManager.state.copy(isCheckedInAtHospital = true)
                            Toast.makeText(context, "Hospital Arrival Checked In! Fast-track active.".localized, Toast.LENGTH_LONG).show()
                            val checkinSpeak = if (LanguageManager.currentLanguage == AppLanguage.TAMIL) {
                                "மருத்துவமனை வருகை வெற்றிகரமாக பதிவானது! உங்கள் டோக்கன் அறை 204 கவுண்டரில் முன்னுரிமை அளிக்கப்படுகிறது."
                            } else {
                                "Arrival checked in successfully! Your token is prioritized at Room 204 counter."
                            }
                            voiceOverHelper?.speak(checkinSpeak)
                        },
                        enabled = !prepState.isCheckedInAtHospital,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("geofence_checkin_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        if (isGeofenceCheckingIn && !prepState.isCheckedInAtHospital) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Verifying Hospital Geofence...".localized)
                        } else if (prepState.isCheckedInAtHospital) {
                            Icon(Icons.Default.Verified, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Checked In at Apollo Clinic".localized)
                        } else {
                            Icon(Icons.Default.MyLocation, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Confirm Hospital Arrival (1-Tap Check-In)".localized)
                        }
                    }
                }
            }
        }

        // Action 2: Pre-Arrival Preparation Portal Shortcut Banner
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToPrep() }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Assignment,
                            contentDescription = "Pre-Arrival",
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Pre-Arrival Preparation System".localized,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Text(
                                text = if (prepState.isPrePassGenerated) "Vitals & Fasting Verified • Fast Pass Ready" else "Pre-enter Vitals, Fasting & Scan Reports for Zero-Wait Entry",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Icon(
                        Icons.Default.ArrowForwardIos,
                        contentDescription = "Go",
                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PreArrivalPrepScreen(
    doctor: Doctor = DoctorRepository.doctors.first(),
    onBackToQueue: () -> Unit
) {
    val context = LocalContext.current
    val voiceOverHelper = LocalVoiceOverHelper.current
    var prepState by remember { mutableStateOf(PreArrivalManager.state) }

    var bpText by remember { mutableStateOf(prepState.bloodPressure) }
    var bsText by remember { mutableStateOf(prepState.bloodSugar) }
    var tempText by remember { mutableStateOf(prepState.temperature) }
    var pulseText by remember { mutableStateOf(prepState.pulse) }
    var allergyText by remember { mutableStateOf(prepState.allergyNotes) }
    var fastingChecked by remember { mutableStateOf(prepState.isFastingDone) }

    var isScanningReport by remember { mutableStateOf(false) }
    var reportsCount by remember { mutableIntStateOf(prepState.uploadedReportsCount) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBackToQueue) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "Pre-Arrival Preparation".localized,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Fast-track your consultation before arriving".localized,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                VoiceOverIconButton(
                    onSpeak = {
                        val guide = "Pre-Arrival checklist for Dr. ${doctor.name}. Please enter your Blood Pressure, Blood Sugar, and Fasting status. Uploading lab reports in advance enables Zero-Wait OPD entry."
                        voiceOverHelper?.speak(guide)
                    },
                    contentDescription = "Read Checklist Audio"
                )
            }
        }

        // Section 1: Pre-Consultation Vitals Entry
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MonitorHeart, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Pre-Consultation Vitals Logger".localized,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = bpText,
                            onValueChange = { bpText = it },
                            label = { Text("Blood Pressure".localized, fontSize = 11.sp) },
                            placeholder = { Text("120/80") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                        )

                        OutlinedTextField(
                            value = bsText,
                            onValueChange = { bsText = it },
                            label = { Text("Blood Sugar".localized, fontSize = 11.sp) },
                            placeholder = { Text("105 mg/dL") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = tempText,
                            onValueChange = { tempText = it },
                            label = { Text("Body Temp (°F)".localized, fontSize = 11.sp) },
                            placeholder = { Text("98.6") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        OutlinedTextField(
                            value = pulseText,
                            onValueChange = { pulseText = it },
                            label = { Text("Pulse Rate".localized, fontSize = 11.sp) },
                            placeholder = { Text("72 bpm") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                }
            }
        }

        // Section 2: Fasting & Pre-Check Requirements
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Restaurant, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Fasting & Pre-Check Status".localized,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            .clickable { fastingChecked = !fastingChecked }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "8-Hour Fasting Completed".localized,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Required for FBS, HbA1c & Lipid profile lab checks".localized,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }

                        Switch(
                            checked = fastingChecked,
                            onCheckedChange = { fastingChecked = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = allergyText,
                        onValueChange = { allergyText = it },
                        label = { Text("Allergies & Current Medications".localized, fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2
                    )
                }
            }
        }

        // Section 3: Lab Report & Medical Scan Pre-Upload
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CloudUpload, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Pre-Arrival Document Upload".localized,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Surface(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "$reportsCount Report Attached".localized,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = {
                            isScanningReport = true
                            reportsCount++
                            Toast.makeText(context, "Scanning & Analyzing Lab Report with AI...".localized, Toast.LENGTH_SHORT).show()
                            isScanningReport = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.PhotoCamera, contentDescription = "Scan Report")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Scan / Upload Previous Prescription & Reports".localized)
                    }
                }
            }
        }

        // Section 4: AI Pre-Arrival SOAP Note Generator & Digital Express Pass Button
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Psychology, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI Pre-Consultation SOAP Summary".localized,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            val bpDisplay = if (bpText.isNotBlank()) bpText else "Not entered"
                            val bsDisplay = if (bsText.isNotBlank()) bsText else "Not entered"
                            Text(
                                text = "Subjective: Patient-entered pre-consultation notes\n" +
                                        "Objective: BP: $bpDisplay • Sugar: $bsDisplay • Fasting: ${if (fastingChecked) "Completed" else "Pending"}\n" +
                                        "Plan: Direct Fast-Track OPD Entry (Room 204)",
                                fontSize = 12.sp,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val newState = prepState.copy(
                                bloodPressure = bpText,
                                bloodSugar = bsText,
                                temperature = tempText,
                                pulse = pulseText,
                                allergyNotes = allergyText,
                                isFastingDone = fastingChecked,
                                uploadedReportsCount = reportsCount,
                                isPrePassGenerated = true
                            )
                            PreArrivalManager.state = newState
                            prepState = newState
                            Toast.makeText(context, "Pre-Arrival Pass Generated & Sent to OPD!".localized, Toast.LENGTH_LONG).show()
                            val prepassSpeak = if (LanguageManager.currentLanguage == AppLanguage.TAMIL) {
                                "வருகைக்கு முந்தைய கடவுச்சீட்டு வெற்றிகரமாக உருவாக்கப்பட்டது. உங்கள் உடல் தகவல்கள் மருத்துவர் ${doctor.name} அவர்களுக்கு அனுப்பப்பட்டது."
                            } else {
                                "Pre-Arrival Pass successfully generated. Your vitals and fasting details have been forwarded to Doctor ${doctor.name}. Proceed to Zero-Wait Queue."
                            }
                            voiceOverHelper?.speak(prepassSpeak)
                            onBackToQueue()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("generate_prepass_btn"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save Vitals & Generate Express Fast-Pass".localized, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// Compact Banner Component for HomeScreen & ConfirmationScreen
@Composable
fun ZeroWaitHomeWidget(
    onOpenQueue: () -> Unit,
    onOpenPrep: () -> Unit
) {
    val prepState = PreArrivalManager.state
    val serving = PreArrivalManager.currentServingToken
    val userTok = PreArrivalManager.userTokenNumber

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(Color(0xFF2E7D32), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ZERO-WAIT OPD LIVE".localized,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (prepState.isPrePassGenerated) {
                    Surface(
                        color = Color(0xFF2E7D32),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "PRE-PASS ACTIVE".localized,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Token #$userTok • Dr. T Sailaja".localized,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Currently Serving: #$serving (OPD Desk 3)".localized,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }

                IconButton(
                    onClick = onOpenQueue,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .size(40.dp)
                ) {
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = "Track Queue",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onOpenQueue,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.ConfirmationNumber, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Live Queue Radar".localized, fontSize = 12.sp)
                }

                Button(
                    onClick = onOpenPrep,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Assignment, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Pre-Arrival Prep".localized, fontSize = 12.sp)
                }
            }
        }
    }
}
