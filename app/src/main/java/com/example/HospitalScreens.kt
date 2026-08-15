package com.example

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// ==========================================
// 1. PATIENT JOURNEY FLOW COMPONENT
// ==========================================
@Composable
fun PatientJourneyStepper(
    currentStep: PatientJourneyStep,
    onStepSelected: (PatientJourneyStep) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("patient_journey_stepper"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Timeline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Life Care Zero-Wait Patient Journey",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "Step ${currentStep.stepNumber} of 9",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Horizontal Step Scroll
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                items(PatientJourneyStep.values()) { step ->
                    val isCurrent = step == currentStep
                    val isPast = step.stepNumber < currentStep.stepNumber

                    Surface(
                        onClick = { onStepSelected(step) },
                        shape = RoundedCornerShape(12.dp),
                        color = when {
                            isCurrent -> MaterialTheme.colorScheme.primary
                            isPast -> MaterialTheme.colorScheme.primaryContainer
                            else -> MaterialTheme.colorScheme.surface
                        },
                        border = if (isCurrent) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                        modifier = Modifier.testTag("journey_step_${step.stepNumber}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(
                                        when {
                                            isCurrent -> MaterialTheme.colorScheme.onPrimary
                                            isPast -> MaterialTheme.colorScheme.primary
                                            else -> MaterialTheme.colorScheme.surfaceVariant
                                        },
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isPast) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(12.dp)
                                    )
                                } else {
                                    Text(
                                        text = "${step.stepNumber}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = step.title,
                                fontSize = 11.sp,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                color = when {
                                    isCurrent -> MaterialTheme.colorScheme.onPrimary
                                    isPast -> MaterialTheme.colorScheme.onPrimaryContainer
                                    else -> MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 2. DOCTOR & DEPARTMENT MANAGEMENT SCREEN
// ==========================================
@Composable
fun HospitalDepartmentsScreen(
    onSelectDepartment: (HospitalDepartment) -> Unit,
    onBookDoctor: (Doctor) -> Unit,
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }

    val departments = HospitalRepository.departments
    val filteredDepts = departments.filter {
        (selectedFilter == "All" || it.type.displayName.contains(selectedFilter, ignoreCase = true)) &&
        (searchQuery.isBlank() || it.type.displayName.contains(searchQuery, ignoreCase = true) || it.leadDoctor.contains(searchQuery, ignoreCase = true))
    }

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
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "Life Care Departments",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Centers of Clinical Excellence & OPD Wings",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search departments, specialists, symptoms...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dept_search_bar"),
                singleLine = true
            )
        }

        // Quick Category Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val chips = listOf("All", "Cardiology", "Orthopedics", "General", "Neurology", "Pediatrics", "Emergency")
                items(chips) { chip ->
                    FilterChip(
                        selected = selectedFilter == chip,
                        onClick = { selectedFilter = chip },
                        label = { Text(chip, fontSize = 12.sp) },
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }
        }

        // Department Cards
        items(filteredDepts) { dept ->
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectDepartment(dept) }
                    .testTag("dept_card_${dept.id}")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        when (dept.type) {
                                            DepartmentType.CARDIOLOGY -> Icons.Default.Favorite
                                            DepartmentType.ORTHOPEDICS -> Icons.Default.Accessibility
                                            DepartmentType.NEUROLOGY -> Icons.Default.Psychology
                                            DepartmentType.PEDIATRICS -> Icons.Default.ChildCare
                                            DepartmentType.ENT -> Icons.Default.Hearing
                                            DepartmentType.DERMATOLOGY -> Icons.Default.Face
                                            DepartmentType.DENTISTRY -> Icons.Default.CleanHands
                                            DepartmentType.EMERGENCY -> Icons.Default.LocalHospital
                                            else -> Icons.Default.MedicalServices
                                        },
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = dept.type.displayName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = dept.type.floor,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFE8F5E9)
                        ) {
                            Text(
                                text = "~${dept.avgWaitTimeMins}m Wait",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = dept.description,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Department Lead", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = dept.leadDoctor, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }

                        Button(
                            onClick = {
                                val matchDoc = DoctorRepository.doctors.firstOrNull { it.specialtyKey.contains(dept.type.name, ignoreCase = true) }
                                    ?: DoctorRepository.doctors.first()
                                onBookDoctor(matchDoc)
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Book OPD", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. DOCTOR DASHBOARD (CLINICAL PORTAL)
// ==========================================
@Composable
fun HospitalDoctorDashboardScreen(
    onBack: () -> Unit,
    onViewPatientRecords: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }
    val prepState = PreArrivalManager.state

    var rxDiagnosis by remember { mutableStateOf("Upper Respiratory Viral Infection") }
    var rxMedicineName by remember { mutableStateOf("Amoxicillin 500mg") }
    var rxDosage by remember { mutableStateOf("1 Tab thrice daily after food") }
    var isRxCreated by remember { mutableStateOf(false) }

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
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "Doctor Clinical Portal",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Dr. Gowtham H • OPD Room 104",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF2E7D32)
                ) {
                    Text(
                        text = "ON DUTY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Live OPD Status Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Active OPD Queue", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text("Token #39", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                        Text("Next In Line: Token #42 (Alex Jenkins)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }

                    Button(
                        onClick = {
                            PreArrivalManager.currentServingToken++
                            Toast.makeText(context, "Called next token: #${PreArrivalManager.currentServingToken}", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Call Next")
                    }
                }
            }
        }

        // Patient in Consultation Vitals Summary (Pre-Arrival Data Integration)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Alex Jenkins (Male, 32y)", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Text("Token #42 • Pre-Arrival Fast-Track Verified", fontSize = 12.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.SemiBold)
                            }
                        }

                        IconButton(onClick = onViewPatientRecords) {
                            Icon(Icons.Default.HistoryEdu, contentDescription = "History", tint = MaterialTheme.colorScheme.primary)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Pre-Arrival Vitals Submitted:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        VitalBox("BP", if (prepState.bloodPressure.isNotBlank()) prepState.bloodPressure else "120/80 mmHg")
                        VitalBox("Sugar", if (prepState.bloodSugar.isNotBlank()) prepState.bloodSugar else "98 mg/dL")
                        VitalBox("Temp", if (prepState.temperature.isNotBlank()) "${prepState.temperature}°F" else "98.6°F")
                        VitalBox("Fasting", if (prepState.isFastingDone) "Done (8h)" else "Pending")
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Known Allergies: ${if (prepState.allergyNotes.isNotBlank()) prepState.allergyNotes else "Penicillin, Peanuts (Mild)"}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        // Doctor Action: Quick E-Prescription & Order Lab
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Medication, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Create Clinical E-Prescription", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = rxDiagnosis,
                        onValueChange = { rxDiagnosis = it },
                        label = { Text("Clinical Diagnosis") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = rxMedicineName,
                        onValueChange = { rxMedicineName = it },
                        label = { Text("Prescribed Medication & Strength") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = rxDosage,
                        onValueChange = { rxDosage = it },
                        label = { Text("Dosage & Instructions") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = {
                                Toast.makeText(context, "CBC & Lipid Panel Ordered in Pathology Lab!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Science, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Order Lab Test", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                isRxCreated = true
                                Toast.makeText(context, "E-Prescription saved & dispatched to Pharmacy!", Toast.LENGTH_LONG).show()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Send to Pharmacy", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VitalBox(title: String, value: String) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        modifier = Modifier.padding(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
    }
}

// ==========================================
// 4. HOSPITAL ADMIN DASHBOARD
// ==========================================
@Composable
fun HospitalAdminDashboardScreen(
    onBack: () -> Unit,
    onNavigateToBeds: () -> Unit,
    onNavigateToEmergency: () -> Unit,
    onNavigateToLab: () -> Unit,
    onNavigateToPharmacy: () -> Unit
) {
    val totalBeds = HospitalRepository.totalBedsCount
    val occupiedBeds = HospitalRepository.occupiedBedsCount
    val availableBeds = HospitalRepository.availableBedsCount
    val occupancyRate = HospitalRepository.bedOccupancyRate

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
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "Life Care Admin Center",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Hospital Operations & Resource Telemetry",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "LIVE SYSTEM",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Top Metrics Grid
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AdminMetricCard(
                    title = "Total OPD Today",
                    value = "148 Patients",
                    subtitle = "Zero-Wait: 82%",
                    icon = Icons.Default.People,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                AdminMetricCard(
                    title = "Active Doctors",
                    value = "34 On Duty",
                    subtitle = "12 Departments",
                    icon = Icons.Default.MedicalServices,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AdminMetricCard(
                    title = "Bed Occupancy",
                    value = "$occupancyRate%",
                    subtitle = "$availableBeds Beds Available",
                    icon = Icons.Default.Bed,
                    color = if (occupancyRate > 80) MaterialTheme.colorScheme.error else Color(0xFF2E7D32),
                    modifier = Modifier.weight(1f)
                )
                AdminMetricCard(
                    title = "Emergency Bay",
                    value = "3 Cases",
                    subtitle = "1 Inbound Unit",
                    icon = Icons.Default.LocalHospital,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Quick Administrative Navigation Panels
        item {
            Text("Departmental Operations", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }

        item {
            AdminActionRow(
                title = "Inpatient Bed & ICU Allocation",
                subtitle = "Manage ICU, Deluxe suites & general ward capacity",
                icon = Icons.Default.SingleBed,
                onClick = onNavigateToBeds
            )
        }

        item {
            AdminActionRow(
                title = "Emergency & Trauma Triage Room",
                subtitle = "Active resuscitation bays, ambulance tracker",
                icon = Icons.Default.Emergency,
                onClick = onNavigateToEmergency
            )
        }

        item {
            AdminActionRow(
                title = "Pathology & Radiology Diagnostics",
                subtitle = "Pending tests, sample collection, verification queue",
                icon = Icons.Default.Science,
                onClick = onNavigateToLab
            )
        }

        item {
            AdminActionRow(
                title = "Central Pharmacy & Drug Dispense",
                subtitle = "E-Prescriptions queue, stock inventory, pickup status",
                icon = Icons.Default.Medication,
                onClick = onNavigateToPharmacy
            )
        }
    }
}

@Composable
fun AdminMetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(subtitle, fontSize = 11.sp, color = color, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun AdminActionRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// ==========================================
// 5. 24x7 EMERGENCY & TRAUMA MANAGEMENT
// ==========================================
@Composable
fun HospitalEmergencyScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var isSosTriggered by remember { mutableStateOf(false) }
    var sosName by remember { mutableStateOf("") }
    var sosPhone by remember { mutableStateOf("") }
    var sosReason by remember { mutableStateOf("Severe Chest Pain & Breathing Difficulty") }

    val emergencyCases = HospitalRepository.emergencyCases

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
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "24x7 Emergency & Trauma",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = "Level-1 Rapid Response Center",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.error
                ) {
                    Text(
                        text = "HOTLINE: 911",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Rapid Emergency SOS Trigger
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f)),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Emergency,
                        contentDescription = "SOS",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Instant Emergency Ambulance SOS",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Text(
                        text = "Dispatches Life Care Advanced Life Support Ambulance with GPS Tracking",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            isSosTriggered = true
                            HospitalRepository.triggerEmergencySOS(
                                patientName = sosName.ifBlank { "Alex Jenkins" },
                                phone = sosPhone.ifBlank { "+1 (555) 987-6543" },
                                emergencyType = sosReason,
                                location = "User Live GPS Location"
                            )
                            Toast.makeText(context, "🚨 EMERGENCY DISPATCHED! Ambulance ETA: 4 Minutes", Toast.LENGTH_LONG).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("emergency_sos_btn")
                    ) {
                        Icon(Icons.Default.NotificationsActive, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("TAP FOR IMMEDIATE AMBULANCE (SOS)", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }

        // Active Emergency Cases
        item {
            Text("Active ER Resuscitation & Triage Bays", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }

        items(emergencyCases) { erCase ->
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(erCase.patientName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when (erCase.triageLevel) {
                                TriageSeverity.CRITICAL_RED -> MaterialTheme.colorScheme.error
                                TriageSeverity.URGENT_YELLOW -> Color(0xFFE65100)
                                TriageSeverity.STABLE_GREEN -> Color(0xFF2E7D32)
                            }
                        ) {
                            Text(
                                text = erCase.triageLevel.code,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(erCase.emergencyType, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Doctor: ${erCase.assignedDoctor}", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                        Text(erCase.status, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

// ==========================================
// 6. LABORATORY & DIAGNOSTIC REPORTS SCREEN
// ==========================================
@Composable
fun HospitalLabReportsScreen(
    onBack: () -> Unit
) {
    val reports = HospitalRepository.labReports
    var selectedReport by remember { mutableStateOf<LabReport?>(reports.firstOrNull()) }

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
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "Central Pathology & Lab",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Verified Diagnostic Investigation Reports",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        items(reports) { report ->
            val isExpanded = selectedReport?.id == report.id
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedReport = if (isExpanded) null else report }
                    .testTag("lab_report_${report.id}")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Science, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(report.testName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Text("Prescribed: ${report.prescribedBy}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFE8F5E9)
                        ) {
                            Text(
                                text = report.status.label,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Report Date: ${report.reportDate}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    if (isExpanded) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Test Parameters & Findings:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Table of parameters
                        report.parameters.forEach { param ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(param.name, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1.5f))
                                Text(
                                    "${param.value} ${param.unit}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (param.isAbnormal) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.End
                                )
                                Text("Ref: ${param.normalRange}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Pathologist Summary: ${report.doctorSummary}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 7. PHARMACY & PRESCRIPTIONS SCREEN
// ==========================================
@Composable
fun HospitalPharmacyScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val prescriptions = HospitalRepository.prescriptions

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
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "Life Care Central Pharmacy",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Prescription Refills & Instant Counter Pickup",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        items(prescriptions) { rx ->
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(rx.id, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Text("Prescribed by ${rx.doctorName}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (rx.isDispensed) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = if (rx.isDispensed) "Dispensed" else "Ready at Counter 2",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (rx.isDispensed) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Diagnosis: ${rx.diagnosis}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Medication Schedule:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(6.dp))

                    rx.medications.forEach { med ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(med.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Text("${med.frequency} • ${med.duration}", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                                Text(med.instructions, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Text("$${med.price}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            HospitalRepository.markMedicationDispensed(rx.id)
                            Toast.makeText(context, "Medicines dispensed at Counter 2!", Toast.LENGTH_SHORT).show()
                        },
                        enabled = !rx.isDispensed,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (rx.isDispensed) "Dispensed & Completed" else "Collect at Pharmacy Counter 2")
                    }
                }
            }
        }
    }
}

// ==========================================
// 8. BILLING & PAYMENTS SCREEN
// ==========================================
@Composable
fun HospitalBillingScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val invoices = HospitalRepository.billingInvoices

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
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "Billing & Payments",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Transparent Invoices & Insurance Claims",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        items(invoices) { invoice ->
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(invoice.invoiceId, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Text(invoice.department, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (invoice.paymentStatus == PaymentStatus.PAID) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.errorContainer
                        ) {
                            Text(
                                text = invoice.paymentStatus.label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (invoice.paymentStatus == PaymentStatus.PAID) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Itemized Hospital Charges:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(6.dp))

                    invoice.items.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                                Text(item.category, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Text("$${String.format("%.2f", item.amount)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("$${String.format("%.2f", invoice.subtotal)}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Insurance Co-Pay Discount", fontSize = 12.sp, color = Color(0xFF2E7D32))
                        Text("-$${String.format("%.2f", invoice.insuranceCovered)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total Amount Payable", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Text("$${String.format("%.2f", invoice.totalPayable)}", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (invoice.paymentStatus == PaymentStatus.PENDING) {
                        Button(
                            onClick = {
                                HospitalRepository.markBillAsPaid(invoice.invoiceId)
                                Toast.makeText(context, "Payment of $${invoice.totalPayable} Successful! Receipt generated.", Toast.LENGTH_LONG).show()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("pay_bill_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Payment, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Pay Now ($${String.format("%.2f", invoice.totalPayable)}) via UPI / Card", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        OutlinedButton(
                            onClick = {
                                Toast.makeText(context, "Digital Tax Invoice downloaded.", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.ReceiptLong, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Download Paid Receipt (PDF)")
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 9. BED & ROOM MANAGEMENT SCREEN
// ==========================================
@Composable
fun HospitalBedManagementScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val rooms = HospitalRepository.bedRooms

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
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "Bed & Room Management",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Real-Time Inpatient & ICU Bed Availability",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        items(rooms) { room ->
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(room.roomNumber, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text(room.floor, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (room.availableBeds > 0) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.errorContainer
                        ) {
                            Text(
                                text = if (room.availableBeds > 0) "${room.availableBeds} Available" else "Fully Occupied",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (room.availableBeds > 0) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(room.category.displayName, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)

                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { room.occupancyPercentage / 100f },
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                        color = if (room.occupancyPercentage > 85) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Occupancy: ${room.occupiedBeds}/${room.totalBeds} Beds (${room.occupancyPercentage}%)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Amenities: ${room.amenities.joinToString(" • ")}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("$${room.pricePerDay.toInt()} / Day", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

                        Button(
                            onClick = {
                                val ok = HospitalRepository.reserveBed(room.id, "Alex Jenkins")
                                if (ok) {
                                    Toast.makeText(context, "Bed reserved in ${room.roomNumber}!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Room is currently full.", Toast.LENGTH_SHORT).show()
                                }
                            },
                            enabled = room.availableBeds > 0,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Request Admission Bed", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 11. END-TO-END PATIENT JOURNEY SCREEN
// ==========================================
@Composable
fun HospitalPatientJourneyScreen(
    onNavigate: (Screen) -> Unit,
    onDoctorSelected: (Doctor) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var currentStep by remember { mutableStateOf(HospitalRepository.currentJourneyStep) }

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
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "Zero-Wait Patient Journey",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Seamless Life Care Hospital Flow",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Stepper Bar
        item {
            PatientJourneyStepper(
                currentStep = currentStep,
                onStepSelected = { selected ->
                    currentStep = selected
                    HospitalRepository.currentJourneyStep = selected
                }
            )
        }

        // Active Step Detailed Card
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${currentStep.stepNumber}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }

                        Text(
                            text = "Phase ${currentStep.stepNumber} of 9",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = currentStep.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = currentStep.subtitle,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(14.dp))

                    when (currentStep) {
                        PatientJourneyStep.REGISTER -> {
                            Text("Your verified patient account stores medical allergies, blood group, and emergency contacts for swift check-ins.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = { onNavigate(Screen.PROFILE) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Open Medical Profile")
                            }
                        }
                        PatientJourneyStep.SYMPTOM_CHECKER -> {
                            Text("Use Life Care AI to describe health symptoms, get intelligent department triage guidance, and instant specialist match.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = { onNavigate(Screen.SYMPTOM_CHECKER) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(Icons.Default.SmartToy, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Launch AI Symptoms Checker")
                            }
                        }
                        PatientJourneyStep.APPOINTMENT -> {
                            Text("Select specialty department, doctor, and convenient OPD time slot. Syncs with Zero-Wait queue.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = { onNavigate(Screen.SPECIALIST) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.CalendarToday, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Select Doctor & Book Slot")
                            }
                        }
                        PatientJourneyStep.PRE_ARRIVAL -> {
                            Text("Enter pre-visit vitals (BP, Glucose, Temp, SpO2) and upload previous lab records from home to save 20 minutes at hospital.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = { onNavigate(Screen.PRE_ARRIVAL_PREP) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                            ) {
                                Icon(Icons.Default.UploadFile, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Complete Pre-Arrival Prep")
                            }
                        }
                        PatientJourneyStep.DIGITAL_TOKEN -> {
                            Text("Your Fast-Track Digital Token is active! Live estimated waiting time: 12 mins. Track queue status in real-time.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = { onNavigate(Screen.ZERO_WAIT_QUEUE) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.ConfirmationNumber, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("View Live Digital Token #42")
                            }
                        }
                        PatientJourneyStep.HOSPITAL_ARRIVAL -> {
                            Text("Touchless check-in verified at Life Care Main Reception Gate A. Fast-track routing to OPD Room 104 enabled.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = {
                                    Toast.makeText(context, "Touchless Hospital Arrival Check-in Confirmed!", Toast.LENGTH_SHORT).show()
                                    currentStep = PatientJourneyStep.DOCTOR_CONSULT
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                            ) {
                                Icon(Icons.Default.GpsFixed, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Confirm Touchless Arrival")
                            }
                        }
                        PatientJourneyStep.DOCTOR_CONSULT -> {
                            Text("Consult with Dr. Gowtham H in OPD Room 104. Doctor has pre-reviewed your submitted vitals and medical history.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = { onNavigate(Screen.DOCTOR_DASHBOARD) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.MedicalServices, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Open Doctor Clinical Portal")
                            }
                        }
                        PatientJourneyStep.LAB_PHARMACY -> {
                            Text("Diagnostic lab investigations ordered and E-Prescription dispatched to Life Care Central Pharmacy Counter 2.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Button(
                                    onClick = { onNavigate(Screen.LAB_REPORTS) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("View Lab Reports", fontSize = 12.sp)
                                }
                                Button(
                                    onClick = { onNavigate(Screen.PHARMACY) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                                ) {
                                    Text("Pick Up Rx", fontSize = 12.sp)
                                }
                            }
                        }
                        PatientJourneyStep.BILLING -> {
                            Text("Review your itemized hospital summary, insurance discount, and clear invoice seamlessly with one tap.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = { onNavigate(Screen.BILLING) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(Icons.Default.Payment, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Review & Pay Invoice")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Next / Previous Navigation Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = {
                                val prev = PatientJourneyStep.values().firstOrNull { it.stepNumber == currentStep.stepNumber - 1 }
                                if (prev != null) currentStep = prev
                            },
                            enabled = currentStep.stepNumber > 1,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Previous Step")
                        }

                        Button(
                            onClick = {
                                val next = PatientJourneyStep.values().firstOrNull { it.stepNumber == currentStep.stepNumber + 1 }
                                if (next != null) currentStep = next
                            },
                            enabled = currentStep.stepNumber < 9,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Next Step")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HospitalNotificationsScreen(
    onBack: () -> Unit
) {
    val notifs = HospitalRepository.notifications

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "Life Care Alerts",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Real-time updates on tokens, labs and prescriptions",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        items(notifs) { item ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = when (item.type) {
                            NotificationType.EMERGENCY_ALERT -> MaterialTheme.colorScheme.errorContainer
                            NotificationType.TOKEN_UPDATE -> MaterialTheme.colorScheme.primaryContainer
                            NotificationType.LAB_READY -> MaterialTheme.colorScheme.secondaryContainer
                            NotificationType.PHARMACY_READY -> MaterialTheme.colorScheme.tertiaryContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                when (item.type) {
                                    NotificationType.EMERGENCY_ALERT -> Icons.Default.Emergency
                                    NotificationType.TOKEN_UPDATE -> Icons.Default.ConfirmationNumber
                                    NotificationType.LAB_READY -> Icons.Default.Science
                                    NotificationType.PHARMACY_READY -> Icons.Default.Medication
                                    NotificationType.BILLING -> Icons.Default.Receipt
                                    else -> Icons.Default.Notifications
                                },
                                contentDescription = null,
                                tint = when (item.type) {
                                    NotificationType.EMERGENCY_ALERT -> MaterialTheme.colorScheme.error
                                    else -> MaterialTheme.colorScheme.primary
                                },
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(item.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text(item.timestamp, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(item.message, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}
