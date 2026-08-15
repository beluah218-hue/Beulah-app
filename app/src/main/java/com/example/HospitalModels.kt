package com.example

import androidx.compose.ui.graphics.vector.ImageVector

enum class DepartmentType(val displayName: String, val floor: String, val opdRooms: String) {
    GENERAL_MEDICINE("General Medicine", "1st Floor, Block A", "Rooms 101 - 108"),
    CARDIOLOGY("Cardiology & Heart Center", "2nd Floor, Block B", "Rooms 201 - 206"),
    ORTHOPEDICS("Orthopedics & Joint Care", "2nd Floor, Block A", "Rooms 210 - 216"),
    NEUROLOGY("Neurology & Brain Sciences", "3rd Floor, Block B", "Rooms 301 - 306"),
    PEDIATRICS("Pediatrics & Child Care", "1st Floor, Block C", "Rooms 120 - 126"),
    ENT("ENT (Ear, Nose, Throat)", "3rd Floor, Block A", "Rooms 310 - 314"),
    DERMATOLOGY("Dermatology & Skin Care", "4th Floor, Block A", "Rooms 401 - 406"),
    DENTISTRY("Dental & Maxillofacial", "4th Floor, Block B", "Rooms 410 - 415"),
    GASTROENTEROLOGY("Gastroenterology", "3rd Floor, Block C", "Rooms 320 - 325"),
    EMERGENCY("24x7 Emergency & Trauma", "Ground Floor, Red Zone", "Trauma Bays 1 - 8"),
    RADIOLOGY("Radiology & Imaging (MRI/CT)", "Basement 1, Diagnostic Wing", "Scan Rooms 01 - 04"),
    PATHOLOGY_LAB("Central Pathology Laboratory", "Basement 1, Lab Wing", "Sample Collection 1 - 6")
}

data class HospitalDepartment(
    val id: String,
    val type: DepartmentType,
    val description: String,
    val leadDoctor: String,
    val doctorsCount: Int,
    val avgWaitTimeMins: Int,
    val commonSymptoms: List<String>,
    val imageUrl: String
)

data class DigitalToken(
    val tokenNumber: Int,
    val department: DepartmentType,
    val doctorName: String,
    val roomNumber: String,
    val currentServingToken: Int,
    val estimatedWaitMinutes: Int,
    val issueTime: String,
    val status: TokenStatus,
    val isExpressFastTrack: Boolean = false
)

enum class TokenStatus(val label: String) {
    ISSUED("Token Active"),
    PRE_ARRIVAL_VERIFIED("Pre-Arrival Verified"),
    HOSPITAL_CHECKED_IN("Checked-In at Hospital"),
    IN_TRIAGE("In Triage Station"),
    INSIDE_CONSULTATION("In Consultation"),
    COMPLETED("Consultation Finished")
}

data class LabTestParameter(
    val name: String,
    val value: String,
    val normalRange: String,
    val unit: String,
    val isAbnormal: Boolean = false
)

data class LabReport(
    val id: String,
    val patientName: String,
    val testName: String,
    val department: String,
    val prescribedBy: String,
    val sampleDate: String,
    val reportDate: String,
    val status: LabStatus,
    val parameters: List<LabTestParameter>,
    val doctorSummary: String,
    val cost: Double
)

enum class LabStatus(val label: String) {
    PENDING_COLLECTION("Sample Awaited"),
    IN_ANALYSIS("Processing in Lab"),
    READY("Report Ready"),
    REVIEWED("Reviewed by Doctor")
}

data class PrescriptionMedication(
    val name: String,
    val dosage: String,
    val frequency: String, // e.g. "1 - 0 - 1 (Morning & Night)"
    val duration: String,  // e.g. "5 Days"
    val instructions: String, // "After food with warm water"
    val price: Double,
    var dispenseStatus: PharmacyDispenseStatus = PharmacyDispenseStatus.READY_FOR_DISPENSE
)

enum class PharmacyDispenseStatus(val label: String) {
    ORDERED("Sent to Pharmacy"),
    READY_FOR_DISPENSE("Ready for Pickup"),
    DISPENSED("Dispensed & Handed Over")
}

data class HospitalPrescription(
    val id: String,
    val patientName: String,
    val doctorName: String,
    val department: String,
    val date: String,
    val diagnosis: String,
    val medications: List<PrescriptionMedication>,
    val doctorNotes: String,
    val isDispensed: Boolean = false
)

data class BillItem(
    val description: String,
    val category: String,
    val amount: Double
)

data class BillingInvoice(
    val invoiceId: String,
    val patientName: String,
    val date: String,
    val department: String,
    val items: List<BillItem>,
    val subtotal: Double,
    val insuranceCovered: Double,
    val taxAmount: Double,
    val totalPayable: Double,
    val paymentStatus: PaymentStatus,
    val paymentMethod: String = "UPI / Card",
    val transactionRef: String = "TXN-LC-89234"
)

enum class PaymentStatus(val label: String) {
    PENDING("Payment Due"),
    PAID("Settled & Cleared"),
    INSURANCE_PROCESSING("Insurance Claim Processing")
}

data class BedRoom(
    val id: String,
    val roomNumber: String,
    val floor: String,
    val category: BedCategory,
    val totalBeds: Int,
    val occupiedBeds: Int,
    val pricePerDay: Double,
    val amenities: List<String>,
    val isOxygenSupported: Boolean = true,
    val isVentilatorEquipped: Boolean = false
) {
    val availableBeds: Int get() = (totalBeds - occupiedBeds).coerceAtLeast(0)
    val occupancyPercentage: Int get() = if (totalBeds > 0) ((occupiedBeds.toFloat() / totalBeds) * 100).toInt() else 0
}

enum class BedCategory(val displayName: String) {
    ICU("Intensive Care Unit (ICU)"),
    CCU("Critical Cardiac Care (CCU)"),
    DELUXE_PRIVATE("Deluxe Private Suite"),
    SEMI_PRIVATE("Semi-Private Twin Room"),
    GENERAL_WARD("General Inpatient Ward"),
    EMERGENCY_OBSERVATION("Emergency Observation Bay")
}

data class EmergencyCase(
    val id: String,
    val patientName: String,
    val contactPhone: String,
    val emergencyType: String,
    val triageLevel: TriageSeverity,
    val ambulanceEtaMinutes: Int,
    val assignedDoctor: String,
    val assignedBed: String,
    val reportedTime: String,
    val status: String
)

enum class TriageSeverity(val label: String, val code: String) {
    CRITICAL_RED("Immediate Resuscitation (Red)", "RED-1"),
    URGENT_YELLOW("Urgent Care Required (Yellow)", "YEL-2"),
    STABLE_GREEN("Stable / Minor (Green)", "GRN-3")
}

data class HospitalNotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val type: NotificationType,
    val isRead: Boolean = false
)

enum class NotificationType {
    TOKEN_UPDATE,
    PRE_ARRIVAL,
    APPOINTMENT,
    LAB_READY,
    PHARMACY_READY,
    BILLING,
    EMERGENCY_ALERT
}

enum class PatientJourneyStep(val stepNumber: Int, val title: String, val subtitle: String) {
    REGISTER(1, "Register / Login", "Patient Profile & Medical History"),
    SYMPTOM_CHECKER(2, "AI Symptom Checker", "Smart Triage & Department Guide"),
    APPOINTMENT(3, "Book Appointment", "Choose Specialist & OPD Slot"),
    PRE_ARRIVAL(4, "Pre-Arrival Preparation", "Enter Vitals & Upload Records"),
    DIGITAL_TOKEN(5, "Digital Token", "Zero-Wait Queue Position"),
    HOSPITAL_ARRIVAL(6, "Hospital Arrival", "Geofence & Touchless Check-in"),
    DOCTOR_CONSULT(7, "Doctor Consultation", "Clinical Evaluation & Treatment"),
    LAB_PHARMACY(8, "Lab & Pharmacy", "Diagnostic Tests & E-Prescription"),
    BILLING(9, "Billing & Discharge", "Transparent Invoice & One-Tap Pay")
}
