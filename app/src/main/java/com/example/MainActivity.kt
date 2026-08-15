package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

enum class Screen {
    LOGIN,
    HOME,
    SPECIALIST,
    BOOK_APPOINTMENT,
    CONFIRMATION,
    SYMPTOM_CHECKER,
    PROFILE,
    DASHBOARD,
    ZERO_WAIT_QUEUE,
    PRE_ARRIVAL_PREP,
    DEPARTMENTS,
    DOCTOR_DASHBOARD,
    ADMIN_DASHBOARD,
    EMERGENCY,
    LAB_REPORTS,
    PHARMACY,
    BILLING,
    BED_MANAGEMENT,
    NOTIFICATIONS,
    PATIENT_JOURNEY
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppScreen()
            }
        }
    }
}

@Composable
fun MainAppScreen() {
    val voiceOverHelper = rememberVoiceOverHelper()
    CompositionLocalProvider(LocalVoiceOverHelper provides voiceOverHelper) {
        MainAppContent()
    }
}

@Composable
fun MainAppContent() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context) }
    val userRepository = remember { UserRepository(database.userDao()) }
    val userFromDb: UserEntity? by userRepository.userFlow.collectAsStateWithLifecycle(initialValue = null)

    var currentScreen by remember { mutableStateOf(Screen.LOGIN) }
    var userName by remember { mutableStateOf("") }
    var userAge by remember { mutableStateOf("") }
    var userGender by remember { mutableStateOf("") }
    var userBloodGroup by remember { mutableStateOf("O+") }
    var userEmail by remember { mutableStateOf("") }
    var userPhone by remember { mutableStateOf("") }
    var userAllergies by remember { mutableStateOf("Penicillin, Peanuts (Mild)") }
    var primaryDoctorName by remember { mutableStateOf("Dr. Sathish Krishnan") }
    var primaryDoctorPhone by remember { mutableStateOf("+1 (555) 234-5678") }
    var emergencyContactName by remember { mutableStateOf("Sarah Jenkins (Spouse)") }
    var emergencyContactPhone by remember { mutableStateOf("+1 (555) 987-6543") }
    var enableDailyHealthTips by remember { mutableStateOf(true) }
    var enableConsultationReminders by remember { mutableStateOf(true) }
    var selectedDoctor by remember { mutableStateOf<Doctor?>(null) }
    
    // Simple state to track booked appointments to show on Home Screen
    var bookedAppointments by remember { mutableStateOf<List<Doctor>>(emptyList()) }
    
    var selectedBookingDate by remember { mutableStateOf(DateTimeHelper.getCurrentDate()) }
    var selectedBookingTime by remember { mutableStateOf(DateTimeHelper.getCurrentTime()) }
    var selectedBookingNotes by remember { mutableStateOf("") }
    
    // Track booked date, time slot and notes for each doctor
    var bookedAppointmentsDetails by remember {
        mutableStateOf<Map<String, Triple<String, String, String>>>(
            mapOf(
                "sathish_krishnan" to Triple(DateTimeHelper.getCurrentDate(), DateTimeHelper.getCurrentTime(), "Routine Checkup")
            )
        )
    }

    LaunchedEffect(userFromDb) {
        userFromDb?.let { user ->
            if (user.isLoggedIn) {
                if (user.name.isNotBlank()) userName = user.name
                if (user.email.isNotBlank()) userEmail = user.email
                if (user.phone.isNotBlank()) userPhone = user.phone
                if (user.age.isNotBlank()) userAge = user.age
                if (user.gender.isNotBlank()) userGender = user.gender
                if (user.bloodGroup.isNotBlank()) userBloodGroup = user.bloodGroup
                if (user.allergies.isNotBlank()) userAllergies = user.allergies
                if (user.primaryDoctorName.isNotBlank()) primaryDoctorName = user.primaryDoctorName
                if (user.primaryDoctorPhone.isNotBlank()) primaryDoctorPhone = user.primaryDoctorPhone
                if (user.emergencyContactName.isNotBlank()) emergencyContactName = user.emergencyContactName
                if (user.emergencyContactPhone.isNotBlank()) emergencyContactPhone = user.emergencyContactPhone
                
                // Automatically bypass sign-in screen if user is already logged in
                if (currentScreen == Screen.LOGIN) {
                    currentScreen = Screen.HOME
                }
            }
        }
    }

    LaunchedEffect(userEmail) {
        if (userEmail.isNotBlank()) {
            FirestoreHelper.loadUserProfile(
                context = context,
                userEmail = userEmail,
                onSuccess = { profile ->
                    if (profile.name.isNotBlank()) userName = profile.name
                    if (profile.age.isNotBlank()) userAge = profile.age
                    if (profile.gender.isNotBlank()) userGender = profile.gender
                    if (profile.bloodGroup.isNotBlank()) userBloodGroup = profile.bloodGroup
                    if (profile.phone.isNotBlank()) userPhone = profile.phone
                    if (profile.allergies.isNotBlank()) userAllergies = profile.allergies
                    if (profile.primaryDoctorName.isNotBlank()) primaryDoctorName = profile.primaryDoctorName
                    if (profile.primaryDoctorPhone.isNotBlank()) primaryDoctorPhone = profile.primaryDoctorPhone
                    if (profile.emergencyContactName.isNotBlank()) emergencyContactName = profile.emergencyContactName
                    if (profile.emergencyContactPhone.isNotBlank()) emergencyContactPhone = profile.emergencyContactPhone
                }
            )
        }
    }

    LaunchedEffect(Unit) {
        FirestoreHelper.loadAppointments(
            context = context,
            onSuccess = { firestoreAppts ->
                val allDoctors = DoctorRepository.doctors
                val newBookedDoctors = mutableListOf<Doctor>()
                val newBookedDetails = bookedAppointmentsDetails.toMutableMap()
                
                for (fa in firestoreAppts) {
                    val doc = allDoctors.find { it.id == fa.doctorId } ?: if (fa.doctorName.isNotBlank()) {
                        Doctor(
                            id = fa.doctorId,
                            name = fa.doctorName,
                            specialtyKey = if (fa.doctorSpecialtyKey.isNotBlank()) fa.doctorSpecialtyKey else "General",
                            specialtyDisplay = if (fa.doctorSpecialtyDisplay.isNotBlank()) fa.doctorSpecialtyDisplay else "Specialist",
                            rating = fa.doctorRating,
                            nextAvailable = fa.date,
                            imageUrl = if (fa.doctorImageUrl.isNotBlank()) fa.doctorImageUrl else "https://images.unsplash.com/photo-1537368910025-700350fe46c7?auto=format&fit=crop&w=300&q=80",
                            yearsExperience = fa.doctorYearsExperience
                        )
                    } else null

                    if (doc != null) {
                        if (!newBookedDoctors.any { it.id == doc.id }) {
                            newBookedDoctors.add(doc)
                        }
                        newBookedDetails[doc.id] = Triple(fa.date, fa.timeSlot, fa.notes)
                    }
                }
                
                // Add existing ones if not present
                for (doc in bookedAppointments) {
                    if (!newBookedDoctors.any { it.id == doc.id }) {
                        newBookedDoctors.add(doc)
                    }
                }
                
                bookedAppointments = newBookedDoctors
                bookedAppointmentsDetails = newBookedDetails
                Toast.makeText(context, "Loaded appointments from Firestore!".localized, Toast.LENGTH_SHORT).show()
            },
            onFailure = { e ->
                android.util.Log.e("MainAppScreen", "Failed to load appointments from Firestore", e)
            }
        )
    }
    
    // State to track AI-based symptom assessments
    var recentAssessments by remember {
        mutableStateOf(
            listOf(
                SymptomAssessment(
                    id = "assessment_1",
                    date = "04-08-2026",
                    symptoms = "Mild fever and slight headache for 2 days".localized,
                    analysis = "Your symptoms suggest a mild viral fever or dehydration. Rest, hydrate well, and keep track of your temperature. Please book an appointment if it exceeds 101°F.".localized
                ),
                SymptomAssessment(
                    id = "assessment_2",
                    date = "01-08-2026",
                    symptoms = "Dry cough and throat irritation".localized,
                    analysis = "This dry cough could be due to allergy, atmospheric dust, or minor pharyngitis. Recommendation: Warm water gargles and throat lozenges. Consult Dr. Priya Sharma if persistent.".localized
                )
            )
        )
    }
    
    // Quick SOS Dialog State
    var showSOSDialog by remember { mutableStateOf(false) }

    // Quick Health Records Dialog State
    var showRecordsDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (currentScreen != Screen.LOGIN) {
                BottomNavigationBar(
                    currentScreen = currentScreen,
                    onNavigate = { screen ->
                        currentScreen = screen
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                },
                label = "ScreenTransition"
            ) { screen ->
                when (screen) {
                    Screen.LOGIN -> LoginScreen(
                        onLoginSuccess = { name, phone, email ->
                            val finalName = if (name.isNotBlank()) name else "User"
                            userName = finalName
                            userPhone = if (phone.isNotBlank()) phone else ""
                            userEmail = if (email.isNotBlank()) email else ""
                            
                            coroutineScope.launch {
                                userRepository.saveUser(
                                    UserEntity(
                                        id = 1,
                                        name = finalName,
                                        phone = userPhone,
                                        email = userEmail,
                                        age = userAge,
                                        gender = userGender,
                                        bloodGroup = userBloodGroup,
                                        isLoggedIn = true,
                                        isEmailVerified = true,
                                        loginTimestamp = System.currentTimeMillis()
                                    )
                                )
                            }
                            currentScreen = Screen.HOME
                        },
                        onDoctorLogin = { docName ->
                            userName = docName
                            currentScreen = Screen.DOCTOR_DASHBOARD
                        },
                        onAdminLogin = { adminName ->
                            userName = adminName
                            currentScreen = Screen.ADMIN_DASHBOARD
                        }
                    )
                    Screen.HOME -> HomeScreen(
                        userName = userName,
                        bookedAppointments = bookedAppointments,
                        bookedAppointmentsDetails = bookedAppointmentsDetails,
                        onNavigate = { target -> currentScreen = target },
                        onDoctorSelected = { doc -> 
                            selectedDoctor = doc
                            currentScreen = Screen.CONFIRMATION
                        },
                        onTriggerSOS = { showSOSDialog = true },
                        onShowRecords = { showRecordsDialog = true }
                    )
                    Screen.SPECIALIST -> SpecialistScreen(
                        onBookDoctor = { doctor ->
                            selectedDoctor = doctor
                            currentScreen = Screen.BOOK_APPOINTMENT
                        }
                    )
                    Screen.BOOK_APPOINTMENT -> ConsultationBookingScreen(
                        initialDoctor = selectedDoctor,
                        onConfirmBooking = { doctor, date, time, notes ->
                            selectedDoctor = doctor
                            selectedBookingDate = date
                            selectedBookingTime = time
                            selectedBookingNotes = notes
                            
                            if (!bookedAppointments.any { it.id == doctor.id }) {
                                bookedAppointments = bookedAppointments + doctor
                            }
                            bookedAppointmentsDetails = bookedAppointmentsDetails + (doctor.id to Triple(date, time, notes))
                            
                            // Real Firestore Save with callback
                            FirestoreHelper.saveAppointment(
                                context = context,
                                doctor = doctor,
                                date = date,
                                timeSlot = time,
                                notes = notes,
                                userName = userName,
                                userPhone = userPhone,
                                userEmail = userEmail,
                                onSuccess = {
                                    Toast.makeText(context, "Appointment & Doctor details stored in Firestore!".localized, Toast.LENGTH_SHORT).show()
                                },
                                onFailure = { e ->
                                    android.util.Log.e("MainAppScreen", "Failed to store in Firestore", e)
                                    Toast.makeText(context, "Saved locally (Firestore Offline)".localized, Toast.LENGTH_SHORT).show()
                                }
                            )
                            currentScreen = Screen.CONFIRMATION
                        },
                        onBack = {
                            currentScreen = Screen.SPECIALIST
                        }
                    )
                    Screen.CONFIRMATION -> {
                        val doc = selectedDoctor ?: DoctorRepository.doctors.first { it.id == "arul_selvan" }
                        val details = bookedAppointmentsDetails[doc.id] ?: Triple("04-08-2026", "08:12 PM", "")
                        ConfirmationScreen(
                            doctor = doc,
                            bookingDate = details.first,
                            bookingTime = details.second,
                            bookingNotes = details.third,
                            onBackToDashboard = {
                                currentScreen = Screen.HOME
                            },
                            onNavigateToZeroWait = {
                                currentScreen = Screen.ZERO_WAIT_QUEUE
                            },
                            onNavigateToPrep = {
                                currentScreen = Screen.PRE_ARRIVAL_PREP
                            }
                        )
                    }
                    Screen.SYMPTOM_CHECKER -> SymptomCheckerScreen(
                        userName = userName,
                        onAddAssessment = { symptoms, analysis ->
                            val formatter = java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault())
                            val currentDateStr = formatter.format(java.util.Date())
                            val newAss = SymptomAssessment(
                                id = "assessment_${System.currentTimeMillis()}",
                                date = currentDateStr,
                                symptoms = symptoms,
                                analysis = analysis
                            )
                            recentAssessments = listOf(newAss) + recentAssessments
                        }
                    )
                    Screen.PROFILE -> ProfileScreen(
                        userName = userName,
                        userAge = userAge,
                        userGender = userGender,
                        userBloodGroup = userBloodGroup,
                        userEmail = userEmail,
                        userPhone = userPhone,
                        userAllergies = userAllergies,
                        primaryDoctorName = primaryDoctorName,
                        primaryDoctorPhone = primaryDoctorPhone,
                        emergencyContactName = emergencyContactName,
                        emergencyContactPhone = emergencyContactPhone,
                        enableDailyHealthTips = enableDailyHealthTips,
                        enableConsultationReminders = enableConsultationReminders,
                        onDailyHealthTipsChange = { enableDailyHealthTips = it },
                        onConsultationRemindersChange = { enableConsultationReminders = it },
                        onUpdateProfile = { name, age, gender, blood, email, phone, allergies, primDocName, primDocPhone, emergName, emergPhone ->
                            userName = name
                            userAge = age
                            userGender = gender
                            userBloodGroup = blood
                            userEmail = email
                            userPhone = phone
                            userAllergies = allergies
                            primaryDoctorName = primDocName
                            primaryDoctorPhone = primDocPhone
                            emergencyContactName = emergName
                            emergencyContactPhone = emergPhone

                            coroutineScope.launch {
                                userRepository.saveUser(
                                    UserEntity(
                                        id = 1,
                                        name = name,
                                        phone = phone,
                                        email = email,
                                        age = age,
                                        gender = gender,
                                        bloodGroup = blood,
                                        allergies = allergies,
                                        primaryDoctorName = primDocName,
                                        primaryDoctorPhone = primDocPhone,
                                        emergencyContactName = emergName,
                                        emergencyContactPhone = emergPhone,
                                        isLoggedIn = true,
                                        isEmailVerified = true,
                                        loginTimestamp = System.currentTimeMillis()
                                    )
                                )
                            }

                            // Sync profile & essential medical info to Firestore Cloud Storage
                            FirestoreHelper.saveUserProfile(
                                context = context,
                                profile = FirestoreUserProfile(
                                    name = name,
                                    age = age,
                                    gender = gender,
                                    bloodGroup = blood,
                                    email = email,
                                    phone = phone,
                                    allergies = allergies,
                                    primaryDoctorName = primDocName,
                                    primaryDoctorPhone = primDocPhone,
                                    emergencyContactName = emergName,
                                    emergencyContactPhone = emergPhone
                                ),
                                onSuccess = {
                                    Toast.makeText(context, "Medical Profile synced to Firestore Cloud!".localized, Toast.LENGTH_SHORT).show()
                                },
                                onFailure = { e ->
                                    android.util.Log.e("ProfileScreen", "Failed to sync profile to Firestore", e)
                                }
                            )
                        },
                        onLogout = {
                            coroutineScope.launch {
                                userRepository.logout()
                                EmailVerificationManager.reset()
                                MobileOtpManager.removeOtpAndReset()
                            }
                            currentScreen = Screen.LOGIN
                        }
                    )
                    Screen.DASHBOARD -> DashboardScreen(
                        userName = userName,
                        bookedAppointments = bookedAppointments,
                        bookedAppointmentsDetails = bookedAppointmentsDetails,
                        recentAssessments = recentAssessments,
                        onNavigate = { target -> currentScreen = target },
                        onDoctorSelected = { doc ->
                            selectedDoctor = doc
                            currentScreen = Screen.CONFIRMATION
                        }
                    )
                    Screen.ZERO_WAIT_QUEUE -> {
                        val doc = selectedDoctor ?: DoctorRepository.doctors.first()
                        ZeroWaitQueueScreen(
                            doctor = doc,
                            onNavigateToPrep = { currentScreen = Screen.PRE_ARRIVAL_PREP },
                            onBackToHome = { currentScreen = Screen.HOME }
                        )
                    }
                    Screen.PRE_ARRIVAL_PREP -> {
                        val doc = selectedDoctor ?: DoctorRepository.doctors.first()
                        PreArrivalPrepScreen(
                            doctor = doc,
                            onBackToQueue = { currentScreen = Screen.ZERO_WAIT_QUEUE }
                        )
                    }
                    Screen.DEPARTMENTS -> HospitalDepartmentsScreen(
                        onSelectDepartment = { dept ->
                            val doc = DoctorRepository.doctors.firstOrNull { it.specialtyKey.contains(dept.type.name, ignoreCase = true) }
                                ?: DoctorRepository.doctors.first()
                            selectedDoctor = doc
                            currentScreen = Screen.BOOK_APPOINTMENT
                        },
                        onBookDoctor = { doc ->
                            selectedDoctor = doc
                            currentScreen = Screen.BOOK_APPOINTMENT
                        },
                        onBack = { currentScreen = Screen.HOME }
                    )
                    Screen.DOCTOR_DASHBOARD -> HospitalDoctorDashboardScreen(
                        onBack = { currentScreen = Screen.HOME },
                        onViewPatientRecords = { currentScreen = Screen.LAB_REPORTS }
                    )
                    Screen.ADMIN_DASHBOARD -> HospitalAdminDashboardScreen(
                        onBack = { currentScreen = Screen.HOME },
                        onNavigateToBeds = { currentScreen = Screen.BED_MANAGEMENT },
                        onNavigateToEmergency = { currentScreen = Screen.EMERGENCY },
                        onNavigateToLab = { currentScreen = Screen.LAB_REPORTS },
                        onNavigateToPharmacy = { currentScreen = Screen.PHARMACY }
                    )
                    Screen.EMERGENCY -> HospitalEmergencyScreen(
                        onBack = { currentScreen = Screen.HOME }
                    )
                    Screen.LAB_REPORTS -> HospitalLabReportsScreen(
                        onBack = { currentScreen = Screen.HOME }
                    )
                    Screen.PHARMACY -> HospitalPharmacyScreen(
                        onBack = { currentScreen = Screen.HOME }
                    )
                    Screen.BILLING -> HospitalBillingScreen(
                        onBack = { currentScreen = Screen.HOME }
                    )
                    Screen.BED_MANAGEMENT -> HospitalBedManagementScreen(
                        onBack = { currentScreen = Screen.HOME }
                    )
                    Screen.NOTIFICATIONS -> HospitalNotificationsScreen(
                        onBack = { currentScreen = Screen.HOME }
                    )
                    Screen.PATIENT_JOURNEY -> HospitalPatientJourneyScreen(
                        onNavigate = { currentScreen = it },
                        onDoctorSelected = { doc ->
                            selectedDoctor = doc
                            currentScreen = Screen.BOOK_APPOINTMENT
                        },
                        onBack = { currentScreen = Screen.HOME }
                    )
                }
            }

            VoiceOverBanner(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp)
            )

            // Quick SOS Modal Dialog
            if (showSOSDialog) {
                AlertDialog(
                    onDismissRequest = { showSOSDialog = false },
                    icon = {
                        Icon(
                            Icons.Filled.Warning,
                            contentDescription = "Emergency",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(36.dp)
                        )
                    },
                    title = {
                        Text(
                            text = "Emergency SOS Mode".localized,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                    },
                    text = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Emergency contact services are prepared. Do you want to call the nearby ambulance center?".localized,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Ambulance: 108\nLife Care Center: 1800-425-LIFE",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = { showSOSDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Call Now".localized, color = Color.White)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showSOSDialog = false }) {
                            Text("Cancel".localized)
                        }
                    }
                )
            }

            // Quick Health Records Dialog
            if (showRecordsDialog) {
                AlertDialog(
                    onDismissRequest = { showRecordsDialog = false },
                    icon = {
                        Icon(
                            Icons.Filled.FolderOpen,
                            contentDescription = "Health Records",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                    },
                    title = {
                        Text(
                            text = "Health Records".localized,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.heightIn(max = 300.dp)
                        ) {
                            item {
                                RecordItem(title = "Lab Report - CBC & Lipid Profile", date = "04-08-2026", doctor = "Dr. Rajesh Kumar")
                            }
                            item {
                                RecordItem(title = "Endocrinology Summary Report", date = "28-07-2026", doctor = "Dr. Aruna Devi")
                            }
                            item {
                                RecordItem(title = "ECG Report - Normal", date = "15-07-2026", doctor = "Dr. Arul Selvan")
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = { showRecordsDialog = false }) {
                            Text("Close".localized)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun RecordItem(title: String, date: String, doctor: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(doctor, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    NavigationBar(
        containerColor = Color(0xFF211F26),
        modifier = Modifier.shadow(8.dp, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    ) {
        NavigationBarItem(
            selected = currentScreen == Screen.HOME,
            onClick = { onNavigate(Screen.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home".localized) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF21005D),
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
        NavigationBarItem(
            selected = currentScreen == Screen.DASHBOARD,
            onClick = { onNavigate(Screen.DASHBOARD) },
            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
            label = { Text("Dashboard".localized) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF21005D),
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
        NavigationBarItem(
            selected = currentScreen == Screen.SYMPTOM_CHECKER,
            onClick = { onNavigate(Screen.SYMPTOM_CHECKER) },
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Checker") },
            label = { Text("Checker".localized) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF21005D),
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
        NavigationBarItem(
            selected = currentScreen == Screen.SPECIALIST,
            onClick = { onNavigate(Screen.SPECIALIST) },
            icon = { Icon(Icons.Default.CalendarToday, contentDescription = "Appointments") },
            label = { Text("Appts".localized) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF21005D),
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
        NavigationBarItem(
            selected = currentScreen == Screen.PROFILE,
            onClick = { onNavigate(Screen.PROFILE) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile".localized) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF21005D),
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
    }
}

@Composable
fun GlobalHeader(
    showLanguageButton: Boolean = true,
    showSearch: Boolean = false,
    onNavigateToLogin: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(enabled = onNavigateToLogin != null) { onNavigateToLogin?.invoke() }
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_app_logo_1783130509082),
                contentDescription = "Life Care Logo",
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "Life Care",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Hospital Portal",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (onNavigateToLogin != null) {
                OutlinedButton(
                    onClick = onNavigateToLogin,
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                ) {
                    Icon(
                        Icons.Default.LocalHospital,
                        contentDescription = "Hospital Portal Login",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Portal", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            if (showSearch) {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
            
            if (showLanguageButton) {
                Button(
                    onClick = {
                        LanguageManager.currentLanguage = if (LanguageManager.currentLanguage == AppLanguage.ENGLISH) {
                            AppLanguage.TAMIL
                        } else {
                            AppLanguage.ENGLISH
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(
                        text = if (LanguageManager.currentLanguage == AppLanguage.ENGLISH) "தமிழ்" else "English",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun LoginScreen(
    onLoginSuccess: (name: String, phone: String, email: String) -> Unit,
    onDoctorLogin: ((doctorName: String) -> Unit)? = null,
    onAdminLogin: ((adminName: String) -> Unit)? = null
) {
    HospitalLoginScreen(
        onLoginSuccess = onLoginSuccess,
        onDoctorLogin = onDoctorLogin,
        onAdminLogin = onAdminLogin
    )
}

@Composable
fun LegacyLoginScreen(
    onLoginSuccess: (name: String, phone: String, email: String) -> Unit
) {
    var selectedTabIsPhone by remember { mutableStateOf(true) }
    var fullName by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var enteredOtpCode by remember { mutableStateOf("") }
    var emailAddress by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val dotColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
    // Dot background pattern drawing
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .drawBehind {
                val dotRadius = 1.dp.toPx()
                val spacing = 24.dp.toPx()
                for (x in 0..size.width.toInt() step spacing.toInt()) {
                    for (y in 0..size.height.toInt() step spacing.toInt()) {
                        drawCircle(
                            color = dotColor,
                            radius = dotRadius,
                            center = Offset(x.toFloat(), y.toFloat())
                        )
                    }
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            GlobalHeader()
            
            Spacer(modifier = Modifier.height(24.dp))

            // Welcome Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome Back".localized,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Your health journey continues here. Securely access your medical profile.".localized,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Card container
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .shadow(12.dp, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Full Name".localized,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        placeholder = { Text("Enter your full name".localized) },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Full Name", tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("full_name_input"),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Custom tab layout
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                            .padding(4.dp)
                    ) {
                        Button(
                            onClick = { selectedTabIsPhone = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedTabIsPhone) MaterialTheme.colorScheme.surface else Color.Transparent,
                                contentColor = if (selectedTabIsPhone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            elevation = if (selectedTabIsPhone) ButtonDefaults.buttonElevation(defaultElevation = 2.dp) else null,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("Phone Number".localized, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { selectedTabIsPhone = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (!selectedTabIsPhone) MaterialTheme.colorScheme.surface else Color.Transparent,
                                contentColor = if (!selectedTabIsPhone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            elevation = if (!selectedTabIsPhone) ButtonDefaults.buttonElevation(defaultElevation = 2.dp) else null,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("Email Address".localized, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    if (selectedTabIsPhone) {
                        // Phone Form with Mobile OTP Generation and Verification
                        Text(
                            text = "Mobile Number".localized,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        OutlinedTextField(
                            value = mobileNumber,
                            onValueChange = { if (it.length <= 10) mobileNumber = it },
                            placeholder = { Text("Enter 10-digit number".localized) },
                            leadingIcon = { 
                                Text("+91", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(start = 12.dp, end = 8.dp))
                            },
                            singleLine = true,
                            enabled = !MobileOtpManager.isOtpSent,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("mobile_number_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (MobileOtpManager.isOtpSent) {
                            // Display simulated SMS card with generated OTP
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Sms, contentDescription = "SMS", tint = MaterialTheme.colorScheme.onTertiaryContainer)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Simulated SMS Notification".localized, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onTertiaryContainer)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = MobileOtpManager.lastSmsNotification ?: "OTP code generated: ${MobileOtpManager.generatedOtpCode}",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                }
                            }

                            Text(
                                text = "Enter 6-digit OTP".localized,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = enteredOtpCode,
                                onValueChange = { if (it.length <= 6) enteredOtpCode = it },
                                placeholder = { Text("123456") },
                                leadingIcon = { Icon(Icons.Default.Pin, contentDescription = "OTP", tint = MaterialTheme.colorScheme.primary) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("otp_code_input"),
                                shape = RoundedCornerShape(8.dp)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    if (MobileOtpManager.verifyOtp(enteredOtpCode)) {
                                        Toast.makeText(context, "OTP Verified Successfully!".localized, Toast.LENGTH_SHORT).show()
                                        val formattedPhone = if (mobileNumber.startsWith("+")) mobileNumber else "+91 $mobileNumber"
                                        onLoginSuccess(
                                            if (fullName.isNotBlank()) fullName else "User",
                                            formattedPhone,
                                            emailAddress
                                        )
                                    } else {
                                        Toast.makeText(context, "Invalid OTP Code".localized, Toast.LENGTH_SHORT).show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("verify_otp_button")
                            ) {
                                Icon(Icons.Default.VerifiedUser, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Verify OTP & Access Data".localized, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedButton(
                                onClick = {
                                    MobileOtpManager.removeOtpAndReset()
                                    enteredOtpCode = ""
                                    Toast.makeText(context, "OTP removed successfully".localized, Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth().height(42.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Remove Mobile OTP / Reset Verification".localized, fontSize = 12.sp)
                            }
                        } else {
                            Button(
                                onClick = {
                                    if (fullName.isBlank()) {
                                        Toast.makeText(context, "Please enter your full name".localized, Toast.LENGTH_SHORT).show()
                                    } else if (mobileNumber.length < 10) {
                                        Toast.makeText(context, "Please enter a valid 10-digit mobile number".localized, Toast.LENGTH_SHORT).show()
                                    } else {
                                        val code = MobileOtpManager.sendOtp("+91 $mobileNumber")
                                        Toast.makeText(context, "OTP Generated: $code".localized, Toast.LENGTH_LONG).show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("get_otp_button")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Smartphone, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Generate OTP on Mobile".localized, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                                }
                            }
                        }
                    } else {
                        // Email Form
                        Text(
                            text = "Email Address".localized,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = emailAddress,
                            onValueChange = { emailAddress = it },
                            placeholder = { Text("example@email.com") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email", tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("email_input"),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Password".localized,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = { Text("••••••••") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lock", tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Toggle Password",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            singleLine = true,
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("password_input"),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        var enteredEmailVerificationCode by remember { mutableStateOf("") }

                        if (EmailVerificationManager.isCodeSent) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "📩 Email Verification Code Sent to $emailAddress: ${EmailVerificationManager.generatedCode}".localized,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            Text(
                                text = "Enter Email Verification Code".localized,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = enteredEmailVerificationCode,
                                onValueChange = { enteredEmailVerificationCode = it },
                                placeholder = { Text("e.g. ${EmailVerificationManager.generatedCode}") },
                                leadingIcon = { Icon(Icons.Default.MarkEmailRead, contentDescription = "Email Verification", tint = MaterialTheme.colorScheme.primary) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("email_code_input"),
                                shape = RoundedCornerShape(8.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    if (enteredEmailVerificationCode.isBlank()) {
                                        Toast.makeText(context, "Please enter the verification code".localized, Toast.LENGTH_SHORT).show()
                                    } else if (EmailVerificationManager.verifyCode(enteredEmailVerificationCode)) {
                                        Toast.makeText(context, "Email Verified Successfully! Saving user data...".localized, Toast.LENGTH_SHORT).show()
                                        onLoginSuccess(
                                            fullName,
                                            if (mobileNumber.isNotBlank()) mobileNumber else "",
                                            emailAddress
                                        )
                                    } else {
                                        Toast.makeText(context, "Invalid Email Verification Code".localized, Toast.LENGTH_SHORT).show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("verify_email_button")
                            ) {
                                Icon(Icons.Default.VerifiedUser, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Verify Email & Login".localized, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                            }
                        } else {
                            Button(
                                onClick = {
                                    if (fullName.isBlank()) {
                                        Toast.makeText(context, "Please enter your full name".localized, Toast.LENGTH_SHORT).show()
                                    } else if (emailAddress.isEmpty() || password.isEmpty()) {
                                        Toast.makeText(context, "Please enter your email and password".localized, Toast.LENGTH_SHORT).show()
                                    } else if (!emailAddress.contains("@")) {
                                        Toast.makeText(context, "Please enter a valid email address".localized, Toast.LENGTH_SHORT).show()
                                    } else {
                                        val code = EmailVerificationManager.sendVerificationCode(emailAddress)
                                        Toast.makeText(context, "Verification Code: $code".localized, Toast.LENGTH_LONG).show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("send_email_code_button")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.MarkEmailRead, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Send Email Verification Code".localized, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // OR Divider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant)
                        Text(
                            text = "OR".localized,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Divider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Social buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Google Login
                        OutlinedButton(
                            onClick = {
                                val finalName = if (fullName.isNotBlank()) fullName else "User"
                                onLoginSuccess(
                                    finalName,
                                    if (mobileNumber.isNotBlank()) mobileNumber else "",
                                    if (emailAddress.isNotBlank()) emailAddress else ""
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Google Icon approximation (colored star)
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .background(Color(0xFFEA4335), CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Google", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }

                        // Biometric Login
                        OutlinedButton(
                            onClick = {
                                val finalName = if (fullName.isNotBlank()) fullName else "User"
                                onLoginSuccess(
                                    finalName,
                                    if (mobileNumber.isNotBlank()) mobileNumber else "",
                                    if (emailAddress.isNotBlank()) emailAddress else ""
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Fingerprint, contentDescription = "Biometric", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Biometric".localized, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign Up Prompts
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Don't have an account? ".localized, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    "Sign up for Life Care".localized,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        Toast.makeText(context, "Sign up clicked".localized, Toast.LENGTH_SHORT).show()
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Trust Badges
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TrustBadge(icon = Icons.Default.VerifiedUser, label = "Secure Data".localized)
                TrustBadge(icon = Icons.Default.HealthAndSafety, label = "Privacy First".localized)
                TrustBadge(icon = Icons.Default.EnhancedEncryption, label = "End-to-end".localized)
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Footer
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Privacy Policy", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.clickable {})
                    Text("Terms of Service", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.clickable {})
                    Text("Help Center", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.clickable {})
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "© 2024 Life Care Health Systems. All rights reserved.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun TrustBadge(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(6.dp))
        Text(label.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, letterSpacing = 1.sp)
    }
}

@Composable
fun HomeScreen(
    userName: String,
    bookedAppointments: List<Doctor>,
    bookedAppointmentsDetails: Map<String, Triple<String, String, String>> = emptyMap(),
    onNavigate: (Screen) -> Unit,
    onDoctorSelected: (Doctor) -> Unit,
    onTriggerSOS: () -> Unit,
    onShowRecords: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            GlobalHeader()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Welcome Section
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (userName == "Karthik") "Hello, Karthik".localized else "${"Hello".localized}, $userName",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Stay updated with your health and wellness today.".localized,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Zero-Wait Hospital & Pre-Arrival Preparation Live Widget
                ZeroWaitHomeWidget(
                    onOpenQueue = { onNavigate(Screen.ZERO_WAIT_QUEUE) },
                    onOpenPrep = { onNavigate(Screen.PRE_ARRIVAL_PREP) }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Life Care Hospital Management Hub Header
                Text(
                    text = "Hospital Management Services",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Hospital Services Grid
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        HospitalServiceChip(
                            title = "Departments",
                            subtitle = "8 Specialties",
                            icon = Icons.Default.Apartment,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigate(Screen.DEPARTMENTS) }
                        )
                        HospitalServiceChip(
                            title = "Doctor Portal",
                            subtitle = "Clinical Triage",
                            icon = Icons.Default.MedicalServices,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigate(Screen.DOCTOR_DASHBOARD) }
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        HospitalServiceChip(
                            title = "Admin Telemetry",
                            subtitle = "Operations & Beds",
                            icon = Icons.Default.AdminPanelSettings,
                            color = Color(0xFF6A1B9A),
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigate(Screen.ADMIN_DASHBOARD) }
                        )
                        HospitalServiceChip(
                            title = "24x7 Emergency",
                            subtitle = "Ambulance SOS",
                            icon = Icons.Default.Emergency,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigate(Screen.EMERGENCY) }
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        HospitalServiceChip(
                            title = "Diagnostic Labs",
                            subtitle = "Verified Reports",
                            icon = Icons.Default.Science,
                            color = Color(0xFF00897B),
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigate(Screen.LAB_REPORTS) }
                        )
                        HospitalServiceChip(
                            title = "Pharmacy & Rx",
                            subtitle = "Counter Pickup",
                            icon = Icons.Default.Medication,
                            color = Color(0xFFE65100),
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigate(Screen.PHARMACY) }
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        HospitalServiceChip(
                            title = "Billing & Claims",
                            subtitle = "Transparent Invoices",
                            icon = Icons.Default.ReceiptLong,
                            color = Color(0xFF1565C0),
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigate(Screen.BILLING) }
                        )
                        HospitalServiceChip(
                            title = "Bed Availability",
                            subtitle = "ICU & Inpatient",
                            icon = Icons.Default.Bed,
                            color = Color(0xFF2E7D32),
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigate(Screen.BED_MANAGEMENT) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Spacer(modifier = Modifier.height(20.dp))

                // Hero Doctor of the Day Promotion Banner
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(176.dp)
                        .shadow(4.dp, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(0.72f)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    "Doctor of the Day".localized,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        "Dr. C. Soundara Rajan",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    val voiceOverHelper = LocalVoiceOverHelper.current
                                    val leadDoctor = remember { DoctorRepository.doctors.firstOrNull { it.id == "dr_soundara_rajan" } }
                                    if (leadDoctor != null) {
                                        VoiceOverIconButton(
                                            onSpeak = { voiceOverHelper?.readDoctorDetails(leadDoctor) },
                                            contentDescription = "Read Dr Soundara Rajan Details",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }
                                Text(
                                    "Managing Director, Emergency & Cardiology • TCR Hospital".localized,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                                    maxLines = 2
                                )
                            }
                            Button(
                                onClick = { 
                                    val targetDoc = DoctorRepository.doctors.firstOrNull { it.id == "dr_soundara_rajan" } ?: DoctorRepository.doctors.first()
                                    onDoctorSelected(targetDoc)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text("Consult Now".localized, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Medical Doctor Avatar Badge (Photo-free)
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 16.dp)
                                .size(72.dp)
                                .background(
                                    brush = Brush.linearGradient(
                                        listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)
                                    ),
                                    shape = CircleShape
                                )
                                .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.MedicalServices,
                                    contentDescription = "Doctor Icon",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "TCR",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Bento Quick Actions Grid
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Card 1: Book Appointment
                        BentoCard(
                            modifier = Modifier
                                .weight(1f)
                                .testTag("book_appointment_card"),
                            title = "Book Appointment".localized,
                            icon = Icons.Default.CalendarToday,
                            iconBgColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                            iconColor = MaterialTheme.colorScheme.primary,
                            onClick = { onNavigate(Screen.SPECIALIST) }
                        )

                        // Card 2: AI Symptom Checker
                        BentoCard(
                            modifier = Modifier
                                .weight(1f)
                                .testTag("symptom_checker_card")
                                .drawBehind {
                                    // Secondary orange left highlight border
                                    val strokeWidth = 4.dp.toPx()
                                    drawLine(
                                        color = Color(0xFFFEAE2C),
                                        start = Offset(0f, 0f),
                                        end = Offset(0f, size.height),
                                        strokeWidth = strokeWidth
                                    )
                                },
                            title = "AI Symptom Checker".localized,
                            icon = Icons.Default.SmartToy,
                            iconBgColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.1f),
                            iconColor = MaterialTheme.colorScheme.secondary,
                            onClick = { onNavigate(Screen.SYMPTOM_CHECKER) }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Card 3: Health Records
                        BentoCard(
                            modifier = Modifier
                                .weight(1f)
                                .testTag("health_records_card"),
                            title = "Health Records".localized,
                            icon = Icons.Default.FolderZip,
                            iconBgColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.1f),
                            iconColor = MaterialTheme.colorScheme.tertiary,
                            onClick = onShowRecords
                        )

                        // Card 4: Emergency SOS
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(120.dp)
                                .clickable { onTriggerSOS() }
                                .testTag("sos_card"),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(MaterialTheme.colorScheme.error, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "SOS",
                                        tint = MaterialTheme.colorScheme.onError,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Emergency SOS".localized,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Upcoming Visits Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        "Upcoming Visit".localized,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "View All".localized,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onNavigate(Screen.SPECIALIST) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (bookedAppointments.isNotEmpty()) {
                    // Show actual booked appointments
                    for (appt in bookedAppointments) {
                        val details = bookedAppointmentsDetails[appt.id] ?: Triple("04-08-2026", "08:12 PM", "")
                        UpcomingVisitCard(
                            doctor = appt,
                            bookingDate = details.first,
                            bookingTime = details.second,
                            onClick = { onDoctorSelected(appt) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                } else {
                    // Predefined Default Upcoming Visit Card (Dr. Sathish Krishnan)
                    val defaultDoctor = DoctorRepository.doctors.first { it.id == "sathish_krishnan" }
                    val details = bookedAppointmentsDetails[defaultDoctor.id] ?: Triple("04-08-2026", "08:12 PM", "")
                    UpcomingVisitCard(
                        doctor = defaultDoctor,
                        bookingDate = details.first,
                        bookingTime = details.second,
                        onClick = { onDoctorSelected(defaultDoctor) }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Quick Vitals Section
                Text(
                    "Quick Vitals".localized,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    VitalCard(icon = Icons.Default.Favorite, label = "Heart Rate".localized, value = "72", unit = "bpm", iconColor = MaterialTheme.colorScheme.error)
                    VitalCard(icon = Icons.Default.FavoriteBorder, label = "BP".localized, value = "120/80", unit = "", iconColor = MaterialTheme.colorScheme.primary)
                    VitalCard(icon = Icons.Default.DirectionsWalk, label = "Steps".localized, value = "8,432", unit = "", iconColor = MaterialTheme.colorScheme.secondary)
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun HospitalServiceChip(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(84.dp)
            .clickable { onClick() }
            .shadow(1.5.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = color.copy(alpha = 0.12f),
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = color,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun BentoCard(
    modifier: Modifier = Modifier,
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBgColor: Color,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .clickable { onClick() }
            .shadow(2.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(iconBgColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun UpcomingVisitCard(
    doctor: Doctor,
    bookingDate: String = "04-08-2026",
    bookingTime: String = "08:12 PM",
    onClick: () -> Unit
) {
    val monthAndDay = remember(bookingDate) {
        try {
            if (bookingDate.contains("-")) {
                val parts = bookingDate.split("-")
                if (parts.size == 3) {
                    val day = parts[0]
                    val monthNum = parts[1].toIntOrNull() ?: 8
                    val months = listOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC")
                    val monthStr = months.getOrElse(monthNum - 1) { "AUG" }
                    Pair(monthStr, day)
                } else {
                    Pair("AUG", "04")
                }
            } else {
                val clean = bookingDate.replace(",", " ")
                val parts = clean.split("\\s+".toRegex()).filter { it.isNotBlank() }
                val months = listOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC")
                var foundMonth = "AUG"
                var foundDay = "04"
                for (part in parts) {
                    val upper = part.uppercase()
                    val matchingMonth = months.firstOrNull { upper.startsWith(it) }
                    if (matchingMonth != null) {
                        foundMonth = matchingMonth
                    } else if (part.all { it.isDigit() } && part.toInt() in 1..31) {
                        foundDay = part
                    }
                }
                Pair(foundMonth, foundDay)
            }
        } catch (e: Exception) {
            Pair("AUG", "04")
        }
    }
    val displayTime = remember(bookingTime) {
        bookingTime.split("-").first().trim()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Calendar block
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(monthAndDay.first, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(monthAndDay.second, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${doctor.titlePrefix} ${doctor.name}".trim(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${doctor.specialtyDisplay.localized} • $displayTime",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = onClick,
                modifier = Modifier
                    .size(36.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
            ) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Details", tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun VitalCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    unit: String,
    iconColor: Color
) {
    Card(
        modifier = Modifier
            .width(136.dp)
            .height(112.dp)
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(icon, contentDescription = label, tint = iconColor, modifier = Modifier.size(24.dp))
            Column {
                Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    if (unit.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(unit, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
            .border(
                width = if (selected) 0.dp else 1.dp,
                color = if (selected) Color.Transparent else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(
            LocalContentColor provides if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        ) {
            label()
        }
    }
}

@Composable
fun SpecialistScreen(
    onBookDoctor: (Doctor) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("All Doctors") }
    var searchQuery by remember { mutableStateOf("") }
    
    // Search/filter states
    var selectedAvailability by remember { mutableStateOf("Any Time") }
    var minRating by remember { mutableStateOf(0.0) }
    var showFilters by remember { mutableStateOf(false) }

    val categories = listOf(
        "All Doctors", "Cardiology", "General", "Pediatrics", "Orthopedics", "ENT", "Dermatology", "Dentistry"
    )

    // Filtered doctor list
    val filteredDoctors = remember(selectedCategory, searchQuery, selectedAvailability, minRating) {
        DoctorRepository.doctors.filter { doc ->
            val matchesCategory = selectedCategory == "All Doctors" || doc.specialtyKey == selectedCategory
            val matchesSearch = searchQuery.isEmpty() || 
                doc.name.contains(searchQuery, ignoreCase = true) || 
                doc.specialtyDisplay.contains(searchQuery, ignoreCase = true)
            
            val matchesAvailability = when (selectedAvailability) {
                "Available Today" -> doc.nextAvailable.contains("Today", ignoreCase = true)
                "Available Tomorrow" -> doc.nextAvailable.contains("Tomorrow", ignoreCase = true) || doc.nextAvailable.contains("Today", ignoreCase = true)
                else -> true
            }
            
            val matchesRating = doc.rating >= minRating
            
            matchesCategory && matchesSearch && matchesAvailability && matchesRating
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            GlobalHeader(showSearch = true)

            // Screen Header Section
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Find Your Specialist".localized,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.testTag("specialist_screen_title")
                    )
                    val voiceOverHelper = LocalVoiceOverHelper.current
                    VoiceOverIconButton(
                        onSpeak = {
                            voiceOverHelper?.speak("Showing verified specialist doctors across CMC Vellore, Naruvi Hospitals, Sri Narayani Hospital and Dietetics, Bajaj Finserv Health Network, Thangam Hospital, GCC Hospitals, Kavan Hospital, Vijay Super Speciality, Royal Hospital, Krishnagiri Road Hospitals, and partner centers. Found ${filteredDoctors.size} verified specialist doctors available for consultation.")
                        },
                        contentDescription = "Read Specialist Directory Summary Aloud"
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "CMC Vellore, Naruvi, Sri Narayani, Bajaj Finserv, Thangam, GCC & Partner Hospitals • Verified Specialists".localized,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                // Search Input with Filter Toggle Action
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search doctor or specialty...".localized) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                        trailingIcon = {
                            IconButton(
                                onClick = { showFilters = !showFilters },
                                modifier = Modifier.testTag("toggle_filters_btn")
                            ) {
                                Box(contentAlignment = Alignment.TopEnd) {
                                    Icon(
                                        imageVector = Icons.Default.FilterList,
                                        contentDescription = "Filters".localized,
                                        tint = if (showFilters || selectedAvailability != "Any Time" || minRating > 0.0) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                        }
                                    )
                                    // Filter active dot indicator
                                    if (selectedAvailability != "Any Time" || minRating > 0.0) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .background(MaterialTheme.colorScheme.error, CircleShape)
                                        )
                                    }
                                }
                            }
                        },
                        singleLine = true,
                        modifier = Modifier.weight(1f).testTag("doctor_search_input"),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // Expandable Filters Panel
                AnimatedVisibility(
                    visible = showFilters,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .testTag("filters_panel"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Header of filter pane
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Filters".localized,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (selectedAvailability != "Any Time" || minRating > 0.0) {
                                    TextButton(
                                        onClick = {
                                            selectedAvailability = "Any Time"
                                            minRating = 0.0
                                        },
                                        modifier = Modifier.testTag("clear_filters_btn")
                                    ) {
                                        Text("Clear All".localized, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // Availability options
                            Text(
                                text = "Availability".localized,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("Any Time", "Available Today", "Available Tomorrow").forEach { option ->
                                    val isSelected = selectedAvailability == option
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedAvailability = option },
                                        label = { Text(option.localized, fontSize = 12.sp) },
                                        modifier = Modifier.testTag("filter_avail_$option")
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Rating options
                            Text(
                                text = "Minimum Rating".localized,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(
                                    Pair(0.0, "Any Rating"),
                                    Pair(4.7, "4.7+ Stars"),
                                    Pair(4.8, "4.8+ Stars"),
                                    Pair(4.9, "4.9+ Stars")
                                ).forEach { (ratingVal, label) ->
                                    val isSelected = minRating == ratingVal
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { minRating = ratingVal },
                                        label = { Text(label.localized, fontSize = 12.sp) },
                                        modifier = Modifier.testTag("filter_rating_$ratingVal")
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable category chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (cat in categories) {
                        val isSelected = selectedCategory == cat
                        Button(
                            onClick = { selectedCategory = cat },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                            modifier = Modifier.height(36.dp).testTag("category_chip_$cat")
                        ) {
                            Text(cat.localized, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Doctor List or Empty State
            if (filteredDoctors.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredDoctors) { doc ->
                        DoctorCard(doctor = doc, onBook = { onBookDoctor(doc) })
                    }
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            } else {
                // Beautiful Empty State
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .testTag("empty_doctors_view"),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "No Results",
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No doctors found matching filters.".localized,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Try adjusting your filters or search keywords.".localized,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            searchQuery = ""
                            selectedCategory = "All Doctors"
                            selectedAvailability = "Any Time"
                            minRating = 0.0
                        },
                        modifier = Modifier.testTag("reset_all_filters_btn")
                    ) {
                        Text("Reset All Filters".localized, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun DoctorAvatarBadge(
    doctor: Doctor,
    size: Dp = 52.dp,
    textSize: TextUnit = 18.sp,
    modifier: Modifier = Modifier
) {
    val initials = remember(doctor.name) {
        val parts = doctor.name.trim().split(" ", ".").filter { it.isNotBlank() }
        when {
            parts.size >= 2 -> "${parts[0].first().uppercaseChar()}${parts[1].first().uppercaseChar()}"
            parts.isNotEmpty() -> parts[0].take(2).uppercase()
            else -> "DR"
        }
    }
    
    val bgColors = remember(doctor.specialtyKey, doctor.id) {
        when (doctor.specialtyKey) {
            "Cardiology" -> listOf(Color(0xFFE53935), Color(0xFFC62828))
            "Dentistry" -> listOf(Color(0xFF00ACC1), Color(0xFF00838F))
            "Orthopedics" -> listOf(Color(0xFFFB8C00), Color(0xFFE65100))
            "ENT" -> listOf(Color(0xFF8E24AA), Color(0xFF6A1B9A))
            "Dermatology" -> listOf(Color(0xFFE91E63), Color(0xFFAD1457))
            "Pediatrics" -> listOf(Color(0xFF43A047), Color(0xFF2E7D32))
            else -> listOf(Color(0xFF1E88E5), Color(0xFF1565C0))
        }
    }

    Box(
        modifier = modifier
            .size(size)
            .background(
                brush = Brush.linearGradient(bgColors),
                shape = CircleShape
            )
            .border(1.5.dp, MaterialTheme.colorScheme.surface.copy(alpha = 0.9f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            fontSize = textSize,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun DoctorCard(
    doctor: Doctor,
    onBook: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DoctorAvatarBadge(doctor = doctor, size = 56.dp, textSize = 20.sp)
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${doctor.titlePrefix} ${doctor.name}".trim(),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified",
                            tint = Color(0xFF00897B),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = doctor.specialtyDisplay.localized,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.secondary,
                        lineHeight = 17.sp
                    )
                }

                val voiceOverHelper = LocalVoiceOverHelper.current
                VoiceOverIconButton(
                    onSpeak = {
                        voiceOverHelper?.readDoctorDetails(doctor)
                    },
                    contentDescription = "Read Doctor Details Aloud",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Badges Row: Experience, Rating, Specialty
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.WorkOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${doctor.yearsExperience}+ Yrs Exp",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFFF8E1)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFF57C00),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = doctor.rating.toString(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE65100)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MedicalServices,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = doctor.specialtyKey,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Schedule, contentDescription = "Available", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(15.dp))
                Text("Next: ".localized + doctor.nextAvailable.localized, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onBook,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
            ) {
                Icon(Icons.Default.EventAvailable, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onPrimary)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Book Appointment".localized, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
fun ConfirmationScreen(
    doctor: Doctor,
    bookingDate: String = "04-08-2026",
    bookingTime: String = "08:12 PM",
    bookingNotes: String = "",
    onBackToDashboard: () -> Unit,
    onNavigateToZeroWait: (() -> Unit)? = null,
    onNavigateToPrep: (() -> Unit)? = null
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            GlobalHeader()

            // Header Icon Success
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                        .shadow(4.dp, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = "Confirmed",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(44.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Booking Confirmed!".localized,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Your appointment has been successfully scheduled. Please show the QR code at the reception upon arrival.".localized,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                val voiceOverHelper = LocalVoiceOverHelper.current
                OutlinedButton(
                    onClick = {
                        doctor?.let { doc ->
                            voiceOverHelper?.readAppointmentConfirmation(
                                doctorName = doc.name,
                                specialty = doc.specialtyDisplay.localized,
                                date = bookingDate,
                                time = bookingTime,
                                token = doc.tokenNumber
                            )
                        }
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.testTag("listen_confirmation_btn")
                ) {
                    Icon(Icons.Default.VolumeUp, contentDescription = "Listen", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Listen to Summary".localized, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Ticket container
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    // Header inside ticket
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DoctorAvatarBadge(
                            doctor = doctor,
                            size = 54.dp,
                            textSize = 19.sp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(doctor.specialtyDisplay.localized, fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                            Text("${doctor.titlePrefix} ${doctor.name}".trim(), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Text("${doctor.yearsExperience} years exp.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Box(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(doctor.tokenNumber, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Verified, contentDescription = "Verified", tint = Color(0xFFFEAE2C), modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("Priority Pass".uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer, letterSpacing = 1.sp)
                            }
                        }
                    }

                    // Dash line cutout simulator
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .drawBehind {
                                drawLine(
                                    color = Color(0xFFC4C5D3),
                                    start = Offset(0f, 0f),
                                    end = Offset(size.width, 0f),
                                    strokeWidth = 2.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(
                                        floatArrayOf(15f, 15f), 0f
                                    )
                                )
                            }
                    )

                    // Details inside ticket
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1.2f)
                                .padding(end = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            TicketDetailRow(
                                icon = Icons.Default.CalendarToday,
                                title = "Date".localized,
                                value = bookingDate
                            )
                            TicketDetailRow(
                                icon = Icons.Default.Schedule,
                                title = "Time Slot".localized,
                                value = bookingTime
                            )
                            if (bookingNotes.isNotEmpty()) {
                                TicketDetailRow(
                                    icon = Icons.Default.Info,
                                    title = "Notes".localized,
                                    value = bookingNotes
                                )
                            }
                        }

                        // Vertical partition line
                        Divider(
                            modifier = Modifier
                                .width(1.dp)
                                .height(160.dp)
                                .align(Alignment.CenterVertically),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )

                        // QR Code container
                        Column(
                            modifier = Modifier
                                .weight(0.8f)
                                .padding(start = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                                    .padding(8.dp)
                            ) {
                                AsyncImage(
                                    model = "https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=LC-APP-2024-8891",
                                    contentDescription = "QR Code",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Check-in QR Code".localized, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text("ID: LC-APP-2024-8891", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Info Alerts & Preparation Checklist
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.1f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = "Checklist", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Preparation Checklist".localized, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    ChecklistItem(text = "Bring your previous medical records.".localized)
                    Spacer(modifier = Modifier.height(8.dp))
                    ChecklistItem(text = "Arrive 15 minutes before slot time.".localized)
                    Spacer(modifier = Modifier.height(8.dp))
                    ChecklistItem(text = "Wear a comfortable mask.".localized)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (onNavigateToZeroWait != null && onNavigateToPrep != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onNavigateToZeroWait,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).height(48.dp)
                        ) {
                            Icon(Icons.Default.ConfirmationNumber, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Zero-Wait Queue".localized, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = onNavigateToPrep,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).height(48.dp)
                        ) {
                            Icon(Icons.Default.Assignment, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Pre-Arrival Prep".localized, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Button(
                    onClick = {
                        Toast.makeText(context, "Added to Google Calendar".localized, Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Add to Calendar".localized, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondary)
                }

                OutlinedButton(
                    onClick = {
                        Toast.makeText(context, "Details shared successfully!".localized, Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.outlinedButtonColors(),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Share Details".localized, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }

                // Custom styled orange action button
                Button(
                    onClick = onBackToDashboard,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEAE2C)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Back to Dashboard".localized, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF291800))
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun TicketDetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    hasAction: Boolean = false,
    onActionClick: () -> Unit = {}
) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            if (hasAction) {
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    modifier = Modifier.clickable { onActionClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Get Directions".localized, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.OpenInNew, contentDescription = "Open", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(12.dp))
                }
            }
        }
    }
}

@Composable
fun ChecklistItem(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun SymptomCheckerScreen(
    userName: String,
    onAddAssessment: (String, String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var inputQuery by remember { mutableStateOf("") }
    
    // Chat logs modeled as a List of Pairs (Message content, isUser)
    var messages by remember {
        mutableStateOf(
            listOf(
                Pair(
                    if (userName == "Karthik") {
                        "Hello! I'm your AI health assistant. Please describe the symptoms you're experiencing today so I can help you understand them better."
                    } else {
                        "Hello, $userName! I'm your AI health assistant. Please describe the symptoms you're experiencing today so I can help you understand them better."
                    },
                    false
                )
            )
        )
    }

    var isLoading by remember { mutableStateOf(false) }

    val quickChips = listOf(
        Pair(Icons.Default.Face, "Fever"),
        Pair(Icons.Default.Info, "Cough"),
        Pair(Icons.Default.Face, "Headache"),
        Pair(Icons.Default.Warning, "Stomach Pain")
    )

    val listState = rememberLazyListState()

    // Auto-scroll on new message
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            GlobalHeader()

            // Medical Warning Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Info, contentDescription = "Warning", tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "This is not a medical diagnosis. In case of a health emergency, please contact your local emergency services immediately.".localized,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        lineHeight = 18.sp
                    )
                }
            }

            // Message Area
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(messages) { msg ->
                    ChatBubble(message = msg.first, isUser = msg.second)
                }
                
                if (isLoading) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.SmartToy, contentDescription = "AI", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp)
                            ) {
                                Row(modifier = Modifier.padding(16.dp)) {
                                    Text("LifeCare AI is analyzing...".localized, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Input Controls panel
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                // Quick Suggestion Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    items(quickChips) { chip ->
                        Button(
                            onClick = {
                                if (!isLoading) {
                                    val symptom = chip.second
                                    val userPrompt = "I am experiencing symptoms: ${symptom.localized}"
                                    messages = messages + Pair(userPrompt, true)
                                    isLoading = true
                                    coroutineScope.launch {
                                        val aiResponse = GeminiService.getSymptomAnalysis(messages, userName)
                                        messages = messages + Pair(aiResponse, false)
                                        isLoading = false
                                        onAddAssessment(symptom, aiResponse)
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(chip.first, contentDescription = chip.second, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(chip.second.localized, fontSize = 13.sp)
                            }
                        }
                    }
                }

                // Chat Input box
                val keyboardController = LocalSoftwareKeyboardController.current
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = inputQuery,
                        onValueChange = { inputQuery = it },
                        placeholder = { Text("Type your symptoms here...".localized, fontSize = 14.sp) },
                        trailingIcon = {
                            IconButton(onClick = {}) {
                                Icon(Icons.Default.Mic, contentDescription = "Voice Input", tint = MaterialTheme.colorScheme.primary)
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                                keyboardActions = KeyboardActions(onSend = {
                                    if (inputQuery.trim().isNotEmpty() && !isLoading) {
                                        val textToSend = inputQuery
                                        inputQuery = ""
                                        keyboardController?.hide()
                                        messages = messages + Pair(textToSend, true)
                                        isLoading = true
                                        coroutineScope.launch {
                                            val aiResponse = GeminiService.getSymptomAnalysis(messages, userName)
                                            messages = messages + Pair(aiResponse, false)
                                            isLoading = false
                                            onAddAssessment(textToSend, aiResponse)
                                        }
                                    }
                                }),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("symptom_text_input"),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )

                    Button(
                        onClick = {
                            if (inputQuery.trim().isNotEmpty() && !isLoading) {
                                val textToSend = inputQuery
                                inputQuery = ""
                                keyboardController?.hide()
                                messages = messages + Pair(textToSend, true)
                                isLoading = true
                                coroutineScope.launch {
                                    val aiResponse = GeminiService.getSymptomAnalysis(messages, userName)
                                    messages = messages + Pair(aiResponse, false)
                                    isLoading = false
                                    onAddAssessment(textToSend, aiResponse)
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .size(52.dp)
                            .testTag("symptom_send_button"),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: String, isUser: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.SmartToy, contentDescription = "AI Logo", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
        }

        Card(
            modifier = Modifier.widthIn(max = 280.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = if (isUser) {
                RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp)
            } else {
                RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp)
            }
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (!isUser) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("LifeCare AI", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        val voiceOverHelper = LocalVoiceOverHelper.current
                        VoiceOverIconButton(
                            onSpeak = { voiceOverHelper?.readSymptomAnalysis(message) },
                            contentDescription = "Read AI Response Aloud",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Text(
                    text = message,
                    fontSize = 14.sp,
                    color = if (isUser) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun ProfileScreen(
    userName: String,
    userAge: String,
    userGender: String,
    userBloodGroup: String,
    userEmail: String,
    userPhone: String,
    userAllergies: String = "",
    primaryDoctorName: String = "",
    primaryDoctorPhone: String = "",
    emergencyContactName: String = "",
    emergencyContactPhone: String = "",
    enableDailyHealthTips: Boolean,
    enableConsultationReminders: Boolean,
    onDailyHealthTipsChange: (Boolean) -> Unit,
    onConsultationRemindersChange: (Boolean) -> Unit,
    onUpdateProfile: (
        name: String,
        age: String,
        gender: String,
        blood: String,
        email: String,
        phone: String,
        allergies: String,
        primaryDocName: String,
        primaryDocPhone: String,
        emergencyName: String,
        emergencyPhone: String
    ) -> Unit,
    onLogout: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }

    // local editing state
    var editName by remember(userName, isEditing) { mutableStateOf(userName) }
    var editAge by remember(userAge, isEditing) { mutableStateOf(userAge) }
    var editGender by remember(userGender, isEditing) { mutableStateOf(userGender) }
    var editBloodGroup by remember(userBloodGroup, isEditing) { mutableStateOf(if (userBloodGroup.isNotBlank()) userBloodGroup else "O+") }
    var editEmail by remember(userEmail, isEditing) { mutableStateOf(userEmail) }
    var editPhone by remember(userPhone, isEditing) { mutableStateOf(userPhone) }
    var editAllergies by remember(userAllergies, isEditing) { mutableStateOf(if (userAllergies.isNotBlank()) userAllergies else "Penicillin, Peanuts (Mild)") }
    var editPrimaryDoctorName by remember(primaryDoctorName, isEditing) { mutableStateOf(if (primaryDoctorName.isNotBlank()) primaryDoctorName else "Dr. Sathish Krishnan") }
    var editPrimaryDoctorPhone by remember(primaryDoctorPhone, isEditing) { mutableStateOf(if (primaryDoctorPhone.isNotBlank()) primaryDoctorPhone else "+1 (555) 234-5678") }
    var editEmergencyContactName by remember(emergencyContactName, isEditing) { mutableStateOf(if (emergencyContactName.isNotBlank()) emergencyContactName else "Sarah Jenkins (Spouse)") }
    var editEmergencyContactPhone by remember(emergencyContactPhone, isEditing) { mutableStateOf(if (emergencyContactPhone.isNotBlank()) emergencyContactPhone else "+1 (555) 987-6543") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Upper Profile Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Firestore Sync Badge
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(Color(0xFF4CAF50), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Firestore Cloud Storage Connected".localized,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    // Avatar Image/Icon
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape)
                            .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Avatar",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(56.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = userName,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = userEmail,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Branding Logo Badge
                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_app_logo_1783130509082),
                            contentDescription = "Life Care Logo",
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Life Care Verified Member",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Essential Medical Records Card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.MedicalInformation, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Essential Medical Information".localized,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            if (!isEditing) {
                IconButton(
                    onClick = { isEditing = true },
                    modifier = Modifier.testTag("edit_profile_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        if (!isEditing) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .testTag("medical_info_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Blood Group Badge & Allergies Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Blood Group / Type".localized, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.errorContainer
                            ) {
                                Text(
                                    text = if (userBloodGroup.isNotBlank()) userBloodGroup else "O+",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Column(modifier = Modifier.weight(1f).padding(start = 16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Known Allergies".localized, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (userAllergies.isNotBlank()) userAllergies else "None reported",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 12.dp))

                    // Primary Care Contact
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocalHospital, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "Primary Care Doctor / Clinic".localized, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (primaryDoctorName.isNotBlank()) primaryDoctorName else "Dr. Sathish Krishnan",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (primaryDoctorPhone.isNotBlank()) primaryDoctorPhone else "+1 (555) 234-5678",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 12.dp))

                    // Emergency Contact Details
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PhoneInTalk, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "Emergency Contact Person".localized, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (emergencyContactName.isNotBlank()) emergencyContactName else "Sarah Jenkins (Spouse)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (emergencyContactPhone.isNotBlank()) emergencyContactPhone else "+1 (555) 987-6543",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }

        // Account Details Section
        Text(
            text = "Personal & Contact Info".localized,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        if (isEditing) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Edit Profile & Medical Records".localized,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Full Name".localized) },
                        modifier = Modifier.fillMaxWidth().testTag("edit_profile_name"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        label = { Text("Email Address".localized) },
                        modifier = Modifier.fillMaxWidth().testTag("edit_profile_email"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = editPhone,
                        onValueChange = { editPhone = it },
                        label = { Text("Phone Number".localized) },
                        modifier = Modifier.fillMaxWidth().testTag("edit_profile_phone"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = editAge,
                            onValueChange = { editAge = it },
                            label = { Text("Age".localized) },
                            modifier = Modifier.weight(1f).testTag("edit_profile_age"),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = editBloodGroup,
                            onValueChange = { editBloodGroup = it },
                            label = { Text("Blood Group".localized) },
                            modifier = Modifier.weight(1f).testTag("edit_profile_blood"),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = editAllergies,
                        onValueChange = { editAllergies = it },
                        label = { Text("Known Allergies".localized) },
                        placeholder = { Text("e.g. Penicillin, Peanuts, Pollen, None") },
                        modifier = Modifier.fillMaxWidth().testTag("edit_profile_allergies"),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = editPrimaryDoctorName,
                        onValueChange = { editPrimaryDoctorName = it },
                        label = { Text("Primary Doctor / Clinic Name".localized) },
                        modifier = Modifier.fillMaxWidth().testTag("edit_profile_prim_doc_name"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = editPrimaryDoctorPhone,
                        onValueChange = { editPrimaryDoctorPhone = it },
                        label = { Text("Primary Doctor Contact Phone".localized) },
                        modifier = Modifier.fillMaxWidth().testTag("edit_profile_prim_doc_phone"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = editEmergencyContactName,
                        onValueChange = { editEmergencyContactName = it },
                        label = { Text("Emergency Contact Person".localized) },
                        modifier = Modifier.fillMaxWidth().testTag("edit_profile_emerg_name"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = editEmergencyContactPhone,
                        onValueChange = { editEmergencyContactPhone = it },
                        label = { Text("Emergency Contact Phone".localized) },
                        modifier = Modifier.fillMaxWidth().testTag("edit_profile_emerg_phone"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Gender".localized,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Male", "Female", "Other").forEach { g ->
                            val isSelected = editGender == g
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .clickable { editGender = g }
                                    .testTag("gender_option_$g"),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    }
                                ),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outlineVariant
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text(
                                        text = g.localized,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { isEditing = false },
                            modifier = Modifier.weight(1f).height(48.dp).testTag("cancel_edit_profile"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Cancel".localized, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                if (editName.isBlank()) {
                                    Toast.makeText(context, "Name cannot be empty".localized, Toast.LENGTH_SHORT).show()
                                } else {
                                    onUpdateProfile(
                                        editName,
                                        editAge,
                                        editGender,
                                        editBloodGroup,
                                        editEmail,
                                        editPhone,
                                        editAllergies,
                                        editPrimaryDoctorName,
                                        editPrimaryDoctorPhone,
                                        editEmergencyContactName,
                                        editEmergencyContactPhone
                                    )
                                    isEditing = false
                                }
                            },
                            modifier = Modifier.weight(1.5f).height(48.dp).testTag("save_edit_profile"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save & Sync".localized, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ProfileDetailRow(label = "Full Name".localized, value = userName)
                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 12.dp))
                    ProfileDetailRow(label = "Email".localized, value = userEmail)
                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 12.dp))
                    ProfileDetailRow(label = "Phone".localized, value = userPhone)
                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 12.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Gender".localized, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (userGender.isNotBlank()) userGender.localized else "Not entered".localized,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (userGender.isNotBlank()) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Age".localized, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (userAge.isNotBlank()) userAge else "Not entered".localized,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (userAge.isNotBlank()) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Blood Group".localized, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (userBloodGroup.isNotBlank()) userBloodGroup else "Not entered".localized,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (userBloodGroup.isNotBlank()) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Mobile Phone OTP Verification & Access Security Section
        Text(
            text = "Mobile OTP Security & Member Access".localized,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Smartphone, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "Phone Number".localized, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text(text = userPhone, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Surface(
                        color = if (MobileOtpManager.isVerified) Color(0xFF2E7D32) else Color(0xFFE65100),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (MobileOtpManager.isVerified) "VERIFIED".localized else "OTP PENDING".localized,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (MobileOtpManager.isVerified) {
                    Text(
                        text = "Single Member Data Access Unlocked. Your profile data is secured by OTP verification on $userPhone.".localized,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = {
                            MobileOtpManager.removeOtpAndReset()
                            Toast.makeText(context, "OTP removed successfully".localized, Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Remove Mobile OTP / Reset Verification".localized, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Text(
                        text = "Verify mobile number via 6-digit OTP to create or modify member data.".localized,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    var profileOtpInput by remember { mutableStateOf("") }

                    if (MobileOtpManager.isOtpSent) {
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                        ) {
                            Text(
                                text = MobileOtpManager.lastSmsNotification ?: "OTP code generated: ${MobileOtpManager.generatedOtpCode}",
                                fontSize = 12.sp,
                                modifier = Modifier.padding(10.dp),
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }

                        OutlinedTextField(
                            value = profileOtpInput,
                            onValueChange = { if (it.length <= 6) profileOtpInput = it },
                            placeholder = { Text("Enter 6-digit OTP".localized) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    if (MobileOtpManager.verifyOtp(profileOtpInput)) {
                                        Toast.makeText(context, "OTP Verified Successfully!".localized, Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Invalid OTP Code".localized, Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Verify OTP & Access Data".localized, fontSize = 12.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    MobileOtpManager.removeOtpAndReset()
                                    profileOtpInput = ""
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Remove / Cancel".localized, fontSize = 12.sp)
                            }
                        }
                    } else {
                        Button(
                            onClick = {
                                val code = MobileOtpManager.sendOtp(userPhone)
                                Toast.makeText(context, "OTP Generated: $code".localized, Toast.LENGTH_LONG).show()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Generate OTP on Mobile".localized, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Preferences Section (Language Settings)
        Text(
            text = "Preferences".localized,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language Icon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "Language".localized, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                            Text(
                                text = if (LanguageManager.currentLanguage == AppLanguage.ENGLISH) "English (Active)" else "தமிழ் (Active)",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row {
                        Button(
                            onClick = { LanguageManager.currentLanguage = AppLanguage.ENGLISH },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (LanguageManager.currentLanguage == AppLanguage.ENGLISH) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (LanguageManager.currentLanguage == AppLanguage.ENGLISH) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("EN", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { LanguageManager.currentLanguage = AppLanguage.TAMIL },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (LanguageManager.currentLanguage == AppLanguage.TAMIL) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (LanguageManager.currentLanguage == AppLanguage.TAMIL) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("தமிழ்", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(
                            imageVector = Icons.Default.HealthAndSafety,
                            contentDescription = "Daily Health Tips Icon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Daily Health Tips".localized,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Enable or disable notifications for daily tips.".localized,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Switch(
                        checked = enableDailyHealthTips,
                        onCheckedChange = onDailyHealthTipsChange,
                        modifier = Modifier.testTag("daily_health_tips_switch")
                    )
                }

                Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Consultation Reminders Icon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Consultation Reminders".localized,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Receive reminders about upcoming appointments.".localized,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Switch(
                        checked = enableConsultationReminders,
                        onCheckedChange = onConsultationRemindersChange,
                        modifier = Modifier.testTag("consultation_reminders_switch")
                    )
                }

                Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 12.dp))

                Column(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CloudQueue,
                            contentDescription = "Cloud Storage Icon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Cloud Sync Integration".localized,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Appointments are stored on Firebase Firestore. To connect your personal production database, ensure 'google-services.json' is added in your app's module folder.".localized,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 14.sp,
                        modifier = Modifier.padding(start = 36.dp)
                    )
                }
            }
        }

        // App Version Info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${"App Version".localized}: 1.0.0 (Build 42)",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Logout Button
        Button(
            onClick = {
                onLogout()
                Toast.makeText(context, "Logged out successfully!".localized, Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("logout_button"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Logout".localized, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ProfileDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = if (value.isNotBlank()) value else "Not entered".localized,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (value.isNotBlank()) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun DashboardScreen(
    userName: String,
    bookedAppointments: List<Doctor>,
    bookedAppointmentsDetails: Map<String, Triple<String, String, String>> = emptyMap(),
    recentAssessments: List<SymptomAssessment>,
    onNavigate: (Screen) -> Unit,
    onDoctorSelected: (Doctor) -> Unit
) {
    var expandedAssessmentId by remember { mutableStateOf<String?>(null) }
    var selectedAppointmentTab by remember { mutableStateOf(0) } // 0 = Upcoming, 1 = Past

    // Sample Past Medical Appointments history
    val pastAppointments = remember {
        listOf(
            Triple(
                DoctorRepository.doctors.first { it.id == "gowtham_h" },
                "01-08-2026",
                "10:00 AM - Internal Medicine Follow-up. Prescribed vitamins & rest."
            ),
            Triple(
                DoctorRepository.doctors.first { it.id == "b_sreedhar" },
                "25-07-2026",
                "02:00 PM - Orthopedic Posture Consultation. Ergonomic advice provided."
            ),
            Triple(
                DoctorRepository.doctors.first { it.id == "arul_selvan" },
                "15-07-2026",
                "10:30 AM - Routine Cardiac Checkup & ECG Review. Results normal."
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // App Header
        GlobalHeader()
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Dashboard".localized,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.testTag("dashboard_title")
                )
                Text(
                    text = "Track your active care schedule and AI health screenings.".localized,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Firestore Live Sync Status Banner
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color(0xFF4CAF50), CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Live Firestore Cloud Synchronization Active".localized,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Appointment Category Tabs (Upcoming vs Past)
        TabRow(
            selectedTabIndex = selectedAppointmentTab,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            divider = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedAppointmentTab == 0,
                onClick = { selectedAppointmentTab = 0 },
                modifier = Modifier.testTag("tab_upcoming_appointments"),
                text = {
                    Text(
                        text = "Upcoming (${bookedAppointments.size.coerceAtLeast(1)})".localized,
                        fontWeight = if (selectedAppointmentTab == 0) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            )
            Tab(
                selected = selectedAppointmentTab == 1,
                onClick = { selectedAppointmentTab = 1 },
                modifier = Modifier.testTag("tab_past_appointments"),
                text = {
                    Text(
                        text = "Past Visits (${pastAppointments.size})".localized,
                        fontWeight = if (selectedAppointmentTab == 1) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Content based on selected tab
        if (selectedAppointmentTab == 0) {
            // UPCOMING APPOINTMENTS TAB
            if (bookedAppointments.isNotEmpty()) {
                bookedAppointments.forEach { doc ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .testTag("appointment_card_${doc.id}")
                            .clickable { onDoctorSelected(doc) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    DoctorAvatarBadge(
                                        doctor = doc,
                                        size = 48.dp,
                                        textSize = 16.sp
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "${doc.titlePrefix} ${doc.name}",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = doc.specialtyDisplay.localized,
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Text(
                                        text = "Confirmed".localized,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            val apptDetails = bookedAppointmentsDetails[doc.id]
                            val displayDate = apptDetails?.first ?: doc.nextAvailable
                            val displayTime = apptDetails?.second ?: "08:12 PM"
                            val displayNotes = apptDetails?.third ?: ""

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = "Schedule",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "$displayDate  •  $displayTime",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            if (displayNotes.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Notes: $displayNotes".localized,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            } else {
                // Default upcoming card plus CTA to schedule more
                val defaultDoctor = DoctorRepository.doctors.first { it.id == "sathish_krishnan" }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable { onDoctorSelected(defaultDoctor) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            DoctorAvatarBadge(
                                doctor = defaultDoctor,
                                size = 48.dp,
                                textSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${defaultDoctor.titlePrefix} ${defaultDoctor.name}",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = defaultDoctor.specialtyDisplay.localized + " (Scheduled Routine Visit)",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { onNavigate(Screen.SPECIALIST) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("dashboard_book_appointment_btn"),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Book New Appointment".localized, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            // PAST APPOINTMENTS TAB
            pastAppointments.forEach { (doc, dateStr, notesStr) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .testTag("past_appointment_card_${doc.id}")
                        .clickable { onDoctorSelected(doc) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                DoctorAvatarBadge(
                                    doctor = doc,
                                    size = 48.dp,
                                    textSize = 16.sp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "${doc.titlePrefix} ${doc.name}",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = doc.specialtyDisplay.localized,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
                            ) {
                                Text(
                                    text = "Completed".localized,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.EventAvailable,
                                contentDescription = "Completed Date",
                                tint = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Visited on: $dateStr".localized,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = notesStr.localized,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = { onDoctorSelected(doc) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Book Follow-Up Appointment".localized, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // AI Symptoms Assessment Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent AI Assessments".localized,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            IconButton(
                onClick = { onNavigate(Screen.SYMPTOM_CHECKER) },
                modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New Assessment",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (recentAssessments.isNotEmpty()) {
            recentAssessments.forEach { assessment ->
                val isExpanded = expandedAssessmentId == assessment.id
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .testTag("assessment_card_${assessment.id}")
                        .clickable {
                            expandedAssessmentId = if (isExpanded) null else assessment.id
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
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
                                        .size(36.dp)
                                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SmartToy,
                                        contentDescription = "AI",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "AI Symptom Analysis".localized,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = assessment.date,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = if (isExpanded) "Collapse" else "Expand",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Brief summary
                        Text(
                            text = "${"Symptom Details".localized}: ${assessment.symptoms}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 4.dp))
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = "Info",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "AI Recommendation".localized,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = assessment.analysis,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Button(
                                onClick = { onNavigate(Screen.SPECIALIST) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = "Consult", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Consult Doctor".localized, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Text(
                                text = "View Assessment Summary".localized,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = "AI",
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No recent assessments".localized,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Take a symptom screening to find health insights.".localized,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onNavigate(Screen.SYMPTOM_CHECKER) },
                        modifier = Modifier.testTag("dashboard_start_assessment_btn")
                    ) {
                        Text("Start New Assessment".localized, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ConsultationBookingScreen(
    initialDoctor: Doctor?,
    onConfirmBooking: (Doctor, String, String, String) -> Unit,
    onBack: () -> Unit
) {
    val doctors = remember { DoctorRepository.doctors }
    var selectedDoctor by remember { mutableStateOf(initialDoctor ?: doctors.first()) }
    
    // Modern Date Picker Cards data
    val availableDates = remember {
        DateTimeHelper.getUpcomingDates(6)
    }
    var selectedDate by remember { mutableStateOf(availableDates.firstOrNull() ?: DateTimeHelper.getCurrentDate()) }
    
    // Modern Time Slot Cards data
    val availableSlots = remember {
        listOf(
            DateTimeHelper.getCurrentTime(),
            "08:30 PM",
            "09:00 PM",
            "10:00 AM",
            "10:30 AM",
            "11:00 AM"
        )
    }
    var selectedSlot by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // App top header with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("booking_back_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Consultation Booking".localized,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.testTag("booking_screen_title")
            )
        }

        Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Section 1: Select Doctor
            Text(
                text = "Select Doctor".localized,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Horizontal Doctor Carousel
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(doctors) { doc ->
                    val isSelected = selectedDoctor.id == doc.id
                    Card(
                        modifier = Modifier
                            .width(160.dp)
                            .testTag("doctor_carousel_item_${doc.id}")
                            .clickable { selectedDoctor = doc }
                            .shadow(if (isSelected) 4.dp else 1.dp, RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.surface
                            }
                        ),
                        border = BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            }
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            DoctorAvatarBadge(
                                doctor = doc,
                                size = 52.dp,
                                textSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${doc.titlePrefix} ${doc.name}".trim(),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = doc.specialtyDisplay.localized,
                                fontSize = 11.sp,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = Color(0xFFFEAE2C),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = doc.rating.toString(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // Doctor Info Card for selected doctor
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .testTag("selected_doctor_info_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DoctorAvatarBadge(
                        doctor = selectedDoctor,
                        size = 54.dp,
                        textSize = 20.sp
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Selected Expert".localized,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${selectedDoctor.titlePrefix} ${selectedDoctor.name}".trim(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = selectedDoctor.specialtyDisplay.localized,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFEAE2C), modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(selectedDoctor.rating.toString(), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            }
                            Text(
                                text = "${selectedDoctor.yearsExperience} yrs exp.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (selectedDoctor.location.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, contentDescription = "Location", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(selectedDoctor.location.localized, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            // Section 2: Pick Date
            Text(
                text = "Select Date".localized,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(availableDates) { dateOption ->
                    val isSelected = selectedDate == dateOption
                    Card(
                        modifier = Modifier
                            .testTag("date_chip_$dateOption")
                            .clickable { selectedDate = dateOption }
                            .shadow(if (isSelected) 3.dp else 1.dp, RoundedCornerShape(10.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.surface
                            }
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (isSelected) {
                                Color.Transparent
                            } else {
                                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            }
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val parts = dateOption.split(",")
                            val label = parts[0]
                            val subLabel = if (parts.size > 1) parts[1].trim() else ""
                            
                            Text(
                                text = label.localized,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = subLabel,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Section 3: Pick Time Slot
            Text(
                text = "Select Time Slot".localized,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Grid style listing using a column of rows
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                availableSlots.chunked(2).forEach { rowSlots ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowSlots.forEach { slot ->
                            val isSelected = selectedSlot == slot
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("time_slot_$slot")
                                    .clickable { selectedSlot = slot }
                                    .shadow(if (isSelected) 3.dp else 1.dp, RoundedCornerShape(10.dp)),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) {
                                        MaterialTheme.colorScheme.primaryContainer
                                    } else {
                                        MaterialTheme.colorScheme.surface
                                    }
                                ),
                                border = BorderStroke(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                    }
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = "Slot",
                                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = slot,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                        if (rowSlots.size < 2) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            // Section 4: Notes (Optional)
            Text(
                text = "Notes (Optional)".localized,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                placeholder = { Text("Reason for visit (optional)...".localized) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .testTag("booking_notes_input"),
                shape = RoundedCornerShape(12.dp),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Main CTA Button
            Button(
                onClick = {
                    if (selectedSlot.isEmpty()) {
                        Toast.makeText(context, "Please select a time slot to continue.".localized, Toast.LENGTH_SHORT).show()
                    } else {
                        onConfirmBooking(selectedDoctor, selectedDate, selectedSlot, notes)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("booking_confirm_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Confirm",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Book Consultation".localized,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
