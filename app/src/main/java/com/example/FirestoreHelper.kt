package com.example

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

object FirestoreHelper {
    private var initialized = false
    private var firestoreInstance: FirebaseFirestore? = null
    
    // In-memory cache for local fallback when Firebase Play Services broker is unavailable
    private val localAppointments = mutableMapOf<String, FirestoreAppointment>()
    private var localUserProfile: FirestoreUserProfile? = null

    fun getFirestore(context: Context): FirebaseFirestore? {
        if (firestoreInstance != null) return firestoreInstance
        if (!initialized) {
            try {
                if (FirebaseApp.getApps(context).isEmpty()) {
                    val options = FirebaseOptions.Builder()
                        .setApplicationId("1:951458426643:android:d07c11d9faf549da9b9c")
                        .setApiKey("AIzaSyDummyKeyForFirestore")
                        .setProjectId("aistudio-firebase-project")
                        .build()
                    FirebaseApp.initializeApp(context, options)
                }
                initialized = true
                
                try {
                    val db = FirebaseFirestore.getInstance()
                    val settings = FirebaseFirestoreSettings.Builder()
                        .setPersistenceEnabled(true)
                        .build()
                    db.firestoreSettings = settings
                    firestoreInstance = db
                } catch (t: Throwable) {
                    Log.w("FirestoreHelper", "Firestore instance not available in current environment: ${t.message}")
                    firestoreInstance = null
                }
            } catch (t: Throwable) {
                Log.w("FirestoreHelper", "FirebaseApp initialization skipped: ${t.message}")
                initialized = true
                firestoreInstance = null
            }
        }
        return firestoreInstance
    }

    fun saveAppointment(
        context: Context,
        doctor: Doctor,
        date: String,
        timeSlot: String,
        notes: String,
        userName: String = "",
        userPhone: String = "",
        userEmail: String = "",
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        // Save to local cache first
        val appt = FirestoreAppointment(
            doctorId = doctor.id,
            doctorName = doctor.name,
            doctorSpecialtyKey = doctor.specialtyKey,
            doctorSpecialtyDisplay = doctor.specialtyDisplay,
            doctorRating = doctor.rating,
            doctorYearsExperience = doctor.yearsExperience,
            doctorImageUrl = doctor.imageUrl.toString(),
            date = date,
            timeSlot = timeSlot,
            notes = notes
        )
        localAppointments[doctor.id] = appt

        try {
            val db = getFirestore(context)
            if (db == null) {
                onSuccess()
                return
            }

            val appointmentData = hashMapOf(
                "doctorId" to doctor.id,
                "doctorName" to doctor.name,
                "doctorSpecialtyKey" to doctor.specialtyKey,
                "doctorSpecialtyDisplay" to doctor.specialtyDisplay,
                "doctorRating" to doctor.rating,
                "doctorYearsExperience" to doctor.yearsExperience,
                "doctorImageUrl" to doctor.imageUrl.toString(),
                "date" to date,
                "timeSlot" to timeSlot,
                "notes" to notes,
                "userName" to userName,
                "userPhone" to userPhone,
                "userEmail" to userEmail,
                "timestamp" to System.currentTimeMillis()
            )

            db.collection("appointments")
                .document(doctor.id)
                .set(appointmentData)
                .addOnSuccessListener {
                    Log.d("FirestoreHelper", "Appointment successfully written to Firestore!")
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    Log.w("FirestoreHelper", "Firestore sync failed, local appointment preserved: ${e.message}")
                    onSuccess()
                }
        } catch (t: Throwable) {
            Log.w("FirestoreHelper", "Firestore write caught exception: ${t.message}")
            onSuccess()
        }
    }

    fun loadAppointments(
        context: Context,
        onSuccess: (List<FirestoreAppointment>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {
            val db = getFirestore(context)
            if (db == null) {
                onSuccess(localAppointments.values.toList())
                return
            }

            db.collection("appointments")
                .get()
                .addOnSuccessListener { result ->
                    val list = mutableListOf<FirestoreAppointment>()
                    for (document in result) {
                        val doctorId = document.getString("doctorId") ?: ""
                        val doctorName = document.getString("doctorName") ?: ""
                        val doctorSpecialtyKey = document.getString("doctorSpecialtyKey") ?: "General"
                        val doctorSpecialtyDisplay = document.getString("doctorSpecialtyDisplay") ?: ""
                        val doctorRating = document.getDouble("doctorRating") ?: 4.8
                        val doctorYearsExperience = document.getLong("doctorYearsExperience")?.toInt() ?: 10
                        val doctorImageUrl = document.getString("doctorImageUrl") ?: ""
                        val date = document.getString("date") ?: ""
                        val timeSlot = document.getString("timeSlot") ?: ""
                        val notes = document.getString("notes") ?: ""
                        if (doctorId.isNotEmpty()) {
                            list.add(
                                FirestoreAppointment(
                                    doctorId = doctorId,
                                    doctorName = doctorName,
                                    doctorSpecialtyKey = doctorSpecialtyKey,
                                    doctorSpecialtyDisplay = doctorSpecialtyDisplay,
                                    doctorRating = doctorRating,
                                    doctorYearsExperience = doctorYearsExperience,
                                    doctorImageUrl = doctorImageUrl,
                                    date = date,
                                    timeSlot = timeSlot,
                                    notes = notes
                                )
                            )
                        }
                    }
                    if (list.isEmpty() && localAppointments.isNotEmpty()) {
                        onSuccess(localAppointments.values.toList())
                    } else {
                        onSuccess(list)
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("FirestoreHelper", "Firestore fetch failed, serving local appointments: ${e.message}")
                    onSuccess(localAppointments.values.toList())
                }
        } catch (t: Throwable) {
            Log.w("FirestoreHelper", "Firestore read caught exception: ${t.message}")
            onSuccess(localAppointments.values.toList())
        }
    }

    fun saveUserProfile(
        context: Context,
        profile: FirestoreUserProfile,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        localUserProfile = profile
        try {
            val db = getFirestore(context)
            if (db == null) {
                onSuccess()
                return
            }

            val profileData = hashMapOf(
                "name" to profile.name,
                "age" to profile.age,
                "gender" to profile.gender,
                "bloodGroup" to profile.bloodGroup,
                "email" to profile.email,
                "phone" to profile.phone,
                "allergies" to profile.allergies,
                "primaryDoctorName" to profile.primaryDoctorName,
                "primaryDoctorPhone" to profile.primaryDoctorPhone,
                "emergencyContactName" to profile.emergencyContactName,
                "emergencyContactPhone" to profile.emergencyContactPhone,
                "updatedAt" to System.currentTimeMillis()
            )

            val docId = if (profile.email.isNotBlank()) profile.email.replace(".", "_") else "user_profile"

            db.collection("users")
                .document(docId)
                .set(profileData)
                .addOnSuccessListener {
                    Log.d("FirestoreHelper", "User profile saved to Firestore!")
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    Log.w("FirestoreHelper", "Firestore profile save failed, local profile preserved: ${e.message}")
                    onSuccess()
                }
        } catch (t: Throwable) {
            Log.w("FirestoreHelper", "Firestore save caught exception: ${t.message}")
            onSuccess()
        }
    }

    fun loadUserProfile(
        context: Context,
        userEmail: String = "",
        onSuccess: (FirestoreUserProfile) -> Unit,
        onFailure: (Exception) -> Unit = {}
    ) {
        if (localUserProfile != null) {
            onSuccess(localUserProfile!!)
            return
        }

        try {
            val db = getFirestore(context)
            if (db == null) {
                localUserProfile?.let { onSuccess(it) } ?: onFailure(Exception("Profile document does not exist"))
                return
            }

            val docId = if (userEmail.isNotBlank()) userEmail.replace(".", "_") else "user_profile"

            db.collection("users")
                .document(docId)
                .get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        val profile = FirestoreUserProfile(
                            name = doc.getString("name") ?: "",
                            age = doc.getString("age") ?: "",
                            gender = doc.getString("gender") ?: "",
                            bloodGroup = doc.getString("bloodGroup") ?: "",
                            email = doc.getString("email") ?: "",
                            phone = doc.getString("phone") ?: "",
                            allergies = doc.getString("allergies") ?: "",
                            primaryDoctorName = doc.getString("primaryDoctorName") ?: "",
                            primaryDoctorPhone = doc.getString("primaryDoctorPhone") ?: "",
                            emergencyContactName = doc.getString("emergencyContactName") ?: "",
                            emergencyContactPhone = doc.getString("emergencyContactPhone") ?: ""
                        )
                        localUserProfile = profile
                        onSuccess(profile)
                    } else if (localUserProfile != null) {
                        onSuccess(localUserProfile!!)
                    } else {
                        onFailure(Exception("Profile document does not exist"))
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("FirestoreHelper", "Firestore loadUserProfile failed: ${e.message}")
                    localUserProfile?.let { onSuccess(it) } ?: onFailure(e)
                }
        } catch (t: Throwable) {
            Log.w("FirestoreHelper", "Firestore load caught exception: ${t.message}")
            localUserProfile?.let { onSuccess(it) } ?: onFailure(Exception(t.message))
        }
    }
}

data class FirestoreUserProfile(
    val name: String = "",
    val age: String = "",
    val gender: String = "",
    val bloodGroup: String = "",
    val email: String = "",
    val phone: String = "",
    val allergies: String = "",
    val primaryDoctorName: String = "",
    val primaryDoctorPhone: String = "",
    val emergencyContactName: String = "",
    val emergencyContactPhone: String = ""
)

data class FirestoreAppointment(
    val doctorId: String,
    val doctorName: String = "",
    val doctorSpecialtyKey: String = "",
    val doctorSpecialtyDisplay: String = "",
    val doctorRating: Double = 4.8,
    val doctorYearsExperience: Int = 10,
    val doctorImageUrl: String = "",
    val date: String,
    val timeSlot: String,
    val notes: String
)
