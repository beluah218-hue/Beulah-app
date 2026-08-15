package com.example

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HospitalLoginScreen(
    onLoginSuccess: (name: String, phone: String, email: String) -> Unit,
    onDoctorLogin: ((doctorName: String) -> Unit)? = null,
    onAdminLogin: ((adminName: String) -> Unit)? = null
) {
    val hospitals = listOf(
        "CMC Vellore",
        "Naruvi Hospitals",
        "Sri Narayani Hospital",
        "Bajaj Finserv Network",
        "Thangam Hospital",
        "GCC Hospitals",
        "Kavan Hospital",
        "Vijay Super Speciality",
        "Royal Hospital"
    )
    var selectedHospital by remember { mutableStateOf("CMC Vellore") }
    var selectedRole by remember { mutableStateOf("Patient") } // "Patient", "Doctor", "Staff"
    var authMode by remember { mutableStateOf("Mobile OTP") } // "Mobile OTP", "Email & Password", "Hospital UHID"
    
    var fullName by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var enteredOtpCode by remember { mutableStateOf("") }
    var emailAddress by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var uhidOrStaffId by remember { mutableStateOf("") }

    val context = LocalContext.current
    val voiceOverHelper = LocalVoiceOverHelper.current

    val dotColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
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
            
            Spacer(modifier = Modifier.height(16.dp))

            // Hospital Portal Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f))
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
                                    .size(40.dp)
                                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.LocalHospital,
                                    contentDescription = "Hospital Portal",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Hospital Portal Login",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "NABH & ABDM Connected Network",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            }
                        }

                        VoiceOverIconButton(
                            onSpeak = {
                                voiceOverHelper?.speak(
                                    "Hospital Portal Login. Selected hospital is $selectedHospital for $selectedRole login. Please choose your authentication method: Mobile OTP, Email, or UHID."
                                )
                            },
                            contentDescription = "Read Hospital Login Aloud"
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Select Hospital / Health Network:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Hospital Horizontal Scrollable Chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(hospitals.size) { index ->
                            val hosp = hospitals[index]
                            val isSelected = hosp == selectedHospital
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    selectedHospital = hosp
                                    Toast.makeText(context, "Selected: $hosp", Toast.LENGTH_SHORT).show()
                                },
                                label = {
                                    Text(
                                        text = hosp,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Apartment,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                    selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Role Selector Tabs (Patient, Doctor, Staff)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                    .padding(4.dp)
            ) {
                listOf(
                    Triple("Patient", Icons.Default.Person, "Patient"),
                    Triple("Doctor", Icons.Default.MedicalServices, "Doctor"),
                    Triple("Staff", Icons.Default.AdminPanelSettings, "Staff")
                ).forEach { (role, icon, label) ->
                    val isSelected = selectedRole == role
                    Button(
                        onClick = { selectedRole = role },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        elevation = if (isSelected) ButtonDefaults.buttonElevation(defaultElevation = 2.dp) else null,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                    ) {
                        Icon(icon, contentDescription = label, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(label, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Login Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(8.dp, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "$selectedHospital • $selectedRole Login",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = when (selectedRole) {
                            "Doctor" -> "Access Clinical OPD, Patient Teleconsult & Prescription Hub"
                            "Staff" -> "Hospital Administration, Bed Allocation & Billing Services"
                            else -> "Access your Electronic Health Records, Appointments & Tokens"
                        },
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Auth Method Sub-Tabs
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                            .padding(4.dp)
                    ) {
                        listOf("Mobile OTP", "Email & Password", "Hospital UHID").forEach { mode ->
                            val isSelected = authMode == mode
                            Button(
                                onClick = { authMode = mode },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent,
                                    contentColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                elevation = if (isSelected) ButtonDefaults.buttonElevation(defaultElevation = 2.dp) else null,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp),
                                contentPadding = PaddingValues(horizontal = 2.dp, vertical = 0.dp)
                            ) {
                                Text(
                                    text = mode,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Full Name
                    Text(
                        text = if (selectedRole == "Doctor") "Doctor Full Name".localized else if (selectedRole == "Staff") "Staff Member Name".localized else "Patient Full Name".localized,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        placeholder = { 
                            Text(if (selectedRole == "Doctor") "e.g. Dr. Vikram Mathews" else if (selectedRole == "Staff") "e.g. Hospital Admin" else "e.g. Karthik Raja") 
                        },
                        leadingIcon = { 
                            Icon(
                                if (selectedRole == "Doctor") Icons.Default.MedicalServices else Icons.Default.Person,
                                contentDescription = "Full Name",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            ) 
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("full_name_input"),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    when (authMode) {
                        "Mobile OTP" -> {
                            Text(
                                text = "Registered Mobile Number".localized,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            
                            OutlinedTextField(
                                value = mobileNumber,
                                onValueChange = { if (it.length <= 10) mobileNumber = it },
                                placeholder = { Text("Enter 10-digit mobile number".localized) },
                                leadingIcon = { 
                                    Text("+91", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(start = 12.dp, end = 8.dp))
                                },
                                singleLine = true,
                                enabled = !MobileOtpManager.isOtpSent,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("mobile_number_input"),
                                shape = RoundedCornerShape(8.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            if (MobileOtpManager.isOtpSent) {
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Sms, contentDescription = "SMS", tint = MaterialTheme.colorScheme.onTertiaryContainer)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Hospital SMS OTP Gateway", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onTertiaryContainer)
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = MobileOtpManager.lastSmsNotification ?: "OTP code generated: ${MobileOtpManager.generatedOtpCode}",
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onTertiaryContainer
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "Tap to auto-fill OTP: ${MobileOtpManager.generatedOtpCode}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.clickable {
                                                enteredOtpCode = MobileOtpManager.generatedOtpCode
                                            }
                                        )
                                    }
                                }

                                Text(
                                    text = "Enter 6-digit OTP".localized,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(6.dp))

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
                                            Toast.makeText(context, "OTP Verified for $selectedHospital!".localized, Toast.LENGTH_SHORT).show()
                                            val formattedPhone = if (mobileNumber.startsWith("+")) mobileNumber else "+91 $mobileNumber"
                                            val finalName = if (fullName.isNotBlank()) fullName else if (selectedRole == "Doctor") "Dr. Vikram Mathews" else "Karthik Raja"
                                            
                                            if (selectedRole == "Doctor" && onDoctorLogin != null) {
                                                onDoctorLogin(finalName)
                                            } else if (selectedRole == "Staff" && onAdminLogin != null) {
                                                onAdminLogin(finalName)
                                            } else {
                                                onLoginSuccess(finalName, formattedPhone, emailAddress)
                                            }
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
                                    Text("Verify & Enter $selectedHospital".localized, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedButton(
                                    onClick = {
                                        MobileOtpManager.removeOtpAndReset()
                                        enteredOtpCode = ""
                                        Toast.makeText(context, "OTP Reset", Toast.LENGTH_SHORT).show()
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth().height(40.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Reset OTP".localized, fontSize = 12.sp)
                                }
                            } else {
                                Button(
                                    onClick = {
                                        if (fullName.isBlank()) {
                                            Toast.makeText(context, "Please enter your name".localized, Toast.LENGTH_SHORT).show()
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
                                    Icon(Icons.Default.Smartphone, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Generate Mobile OTP".localized, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        "Email & Password" -> {
                            Text(
                                text = "Hospital / Personal Email".localized,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = emailAddress,
                                onValueChange = { emailAddress = it },
                                placeholder = { Text(if (selectedRole == "Doctor") "doctor@${selectedHospital.lowercase().replace(" ", "")}.edu" else "patient@email.com") },
                                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email", tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("email_input"),
                                shape = RoundedCornerShape(8.dp)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Secure Password".localized,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                placeholder = { Text("••••••••") },
                                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lock", tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
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
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "📩 Verification code: ${EmailVerificationManager.generatedCode}".localized,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }

                                OutlinedTextField(
                                    value = enteredEmailVerificationCode,
                                    onValueChange = { enteredEmailVerificationCode = it },
                                    placeholder = { Text("Enter verification code") },
                                    leadingIcon = { Icon(Icons.Default.MarkEmailRead, contentDescription = "Email Verification", tint = MaterialTheme.colorScheme.primary) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.fillMaxWidth().testTag("email_code_input"),
                                    shape = RoundedCornerShape(8.dp)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        if (EmailVerificationManager.verifyCode(enteredEmailVerificationCode)) {
                                            Toast.makeText(context, "Email Verified Successfully!".localized, Toast.LENGTH_SHORT).show()
                                            val finalName = if (fullName.isNotBlank()) fullName else "User"
                                            if (selectedRole == "Doctor" && onDoctorLogin != null) {
                                                onDoctorLogin(finalName)
                                            } else if (selectedRole == "Staff" && onAdminLogin != null) {
                                                onAdminLogin(finalName)
                                            } else {
                                                onLoginSuccess(finalName, if (mobileNumber.isNotBlank()) mobileNumber else "", emailAddress)
                                            }
                                        } else {
                                            Toast.makeText(context, "Invalid Verification Code".localized, Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("verify_email_button")
                                ) {
                                    Icon(Icons.Default.VerifiedUser, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Verify & Access Hospital Portal".localized, fontSize = 14.sp, fontWeight = FontWeight.Bold)
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
                                    modifier = Modifier.fillMaxWidth().height(48.dp)
                                ) {
                                    Icon(Icons.Default.Send, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Send Verification Code".localized, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        "Hospital UHID" -> {
                            Text(
                                text = if (selectedRole == "Staff") "Staff Employee ID".localized else if (selectedRole == "Doctor") "Doctor Medical License ID".localized else "Patient UHID / MRN Number".localized,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = uhidOrStaffId,
                                onValueChange = { uhidOrStaffId = it },
                                placeholder = { 
                                    Text(
                                        when (selectedHospital) {
                                            "CMC Vellore" -> "e.g. CMC-99482"
                                            "Naruvi Hospitals" -> "e.g. NAR-4821"
                                            "Sri Narayani Hospital" -> "e.g. SNH-7731"
                                            else -> "e.g. LC-55401"
                                        }
                                    ) 
                                },
                                leadingIcon = { Icon(Icons.Default.Badge, contentDescription = "ID", tint = MaterialTheme.colorScheme.primary) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    val finalId = if (uhidOrStaffId.isNotBlank()) uhidOrStaffId else "UHID-${(10000..99999).random()}"
                                    val finalName = if (fullName.isNotBlank()) fullName else if (selectedRole == "Doctor") "Dr. Vikram Mathews" else "Karthik Raja"
                                    Toast.makeText(context, "$selectedHospital ID $finalId verified!".localized, Toast.LENGTH_SHORT).show()
                                    
                                    if (selectedRole == "Doctor" && onDoctorLogin != null) {
                                        onDoctorLogin(finalName)
                                    } else if (selectedRole == "Staff" && onAdminLogin != null) {
                                        onAdminLogin(finalName)
                                    } else {
                                        onLoginSuccess(finalName, "+91 98401 23456", "patient@lifecare.org")
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth().height(48.dp)
                            ) {
                                Icon(Icons.Default.Login, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Sign In with Hospital ID".localized, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quick 1-Tap Access Demo Logins Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "⚡ Instant 1-Tap Fast Logins:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "Logged in as Patient", Toast.LENGTH_SHORT).show()
                            onLoginSuccess("Karthik Raja", "+91 98401 23456", "karthik.raja@email.com")
                        },
                        modifier = Modifier.weight(1f).height(42.dp),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Patient", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "Logged in as Specialist Doctor", Toast.LENGTH_SHORT).show()
                            if (onDoctorLogin != null) {
                                onDoctorLogin("Dr. Vikram Mathews")
                            } else {
                                onLoginSuccess("Dr. Vikram Mathews", "+91 94432 10001", "vmathews@cmcvellore.edu")
                            }
                        },
                        modifier = Modifier.weight(1f).height(42.dp),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                    ) {
                        Icon(Icons.Default.MedicalServices, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Doctor", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "Logged in as Hospital Admin", Toast.LENGTH_SHORT).show()
                            if (onAdminLogin != null) {
                                onAdminLogin("Hospital Administrator")
                            } else {
                                onLoginSuccess("Hospital Admin", "+91 98400 99999", "admin@lifecare.org")
                            }
                        },
                        modifier = Modifier.weight(1f).height(42.dp),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                    ) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Staff / Admin", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Trust & Security Badges
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TrustBadge(icon = Icons.Default.VerifiedUser, label = "NABH Certified".localized)
                TrustBadge(icon = Icons.Default.HealthAndSafety, label = "ABDM Linked".localized)
                TrustBadge(icon = Icons.Default.EnhancedEncryption, label = "256-Bit SSL".localized)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 24x7 Emergency Contact Footer
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Call,
                        contentDescription = "Helpline",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "24x7 Hospital Emergency Helpline: 108 • 1800-425-LIFE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Christian Medical College Vellore • Naruvi Hospitals • Sri Narayani Hospital & Research Centre",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "© 2026 Life Care Hospital Management Systems. All rights reserved.",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}
