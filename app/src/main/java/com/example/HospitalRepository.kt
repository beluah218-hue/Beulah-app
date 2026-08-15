package com.example

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object HospitalRepository {

    // Hospital Name Constant
    const val HOSPITAL_NAME = "Life Care Hospital"
    const val HOSPITAL_TAGLINE = "Excellence in Compassionate, Zero-Wait Healthcare"
    const val EMERGENCY_HOTLINE = "+1 (800) 543-3227 / 911"
    const val HOSPITAL_ADDRESS = "100 Healthcare Boulevard, Life Care Medical City"

    // Patient Journey Current Progress
    var currentJourneyStep by mutableStateOf(PatientJourneyStep.REGISTER)

    // Departments Catalog
    val departments = listOf(
        HospitalDepartment(
            id = "dept_general",
            type = DepartmentType.GENERAL_MEDICINE,
            description = "Comprehensive primary healthcare, chronic condition management, viral fevers, diabetes and routine wellness checks.",
            leadDoctor = "Dr. Gowtham H",
            doctorsCount = 8,
            avgWaitTimeMins = 8,
            commonSymptoms = listOf("Fever", "Headache", "Fatigue", "Weakness", "Cough & Cold", "Body Aches"),
            imageUrl = "https://images.unsplash.com/photo-1579684385127-1ef15d508118?auto=format&fit=crop&w=400&q=80"
        ),
        HospitalDepartment(
            id = "dept_cardiology",
            type = DepartmentType.CARDIOLOGY,
            description = "Advanced cardiac care, 24/7 cath lab, ECG, Echocardiography, hypertension management, and preventative cardiology.",
            leadDoctor = "Dr. Arul Selvan",
            doctorsCount = 6,
            avgWaitTimeMins = 12,
            commonSymptoms = listOf("Chest Pain", "Palpitations", "Shortness of Breath", "High BP", "Dizziness", "Swollen Ankles"),
            imageUrl = "https://images.unsplash.com/photo-1628348068343-c6a848d2b6dd?auto=format&fit=crop&w=400&q=80"
        ),
        HospitalDepartment(
            id = "dept_ortho",
            type = DepartmentType.ORTHOPEDICS,
            description = "Bone and joint health, spine care, arthroscopy, sports injury rehabilitation, fracture care, and robotic joint replacement.",
            leadDoctor = "Dr. B Sreedhar",
            doctorsCount = 5,
            avgWaitTimeMins = 10,
            commonSymptoms = listOf("Joint Pain", "Back Pain", "Knee Stiffness", "Fracture / Sprain", "Neck Pain", "Arthritis"),
            imageUrl = "https://images.unsplash.com/photo-1584515979956-d9f6e5d09982?auto=format&fit=crop&w=400&q=80"
        ),
        HospitalDepartment(
            id = "dept_neuro",
            type = DepartmentType.NEUROLOGY,
            description = "Expert diagnosis and neuro-intervention for stroke, migraines, epilepsy, neuropathies, and memory disorders.",
            leadDoctor = "Dr. Sathish Krishnan",
            doctorsCount = 4,
            avgWaitTimeMins = 15,
            commonSymptoms = listOf("Severe Migraine", "Numbness", "Seizures", "Tremors", "Balance Issues", "Memory Loss"),
            imageUrl = "https://images.unsplash.com/photo-1559757175-5700dde675bc?auto=format&fit=crop&w=400&q=80"
        ),
        HospitalDepartment(
            id = "dept_pediatrics",
            type = DepartmentType.PEDIATRICS,
            description = "Child health, immunization schedules, pediatric growth assessment, newborn nursery, and pediatric emergencies.",
            leadDoctor = "Dr. Praveen Kumar",
            doctorsCount = 6,
            avgWaitTimeMins = 6,
            commonSymptoms = listOf("Child Fever", "Vaccination", "Stomach Ache in Kids", "Ear Infection", "Growth Milestone Delay"),
            imageUrl = "https://images.unsplash.com/photo-1584515933487-779824d29309?auto=format&fit=crop&w=400&q=80"
        ),
        HospitalDepartment(
            id = "dept_ent",
            type = DepartmentType.ENT,
            description = "Microscopic ear surgery, nasal endoscopy, sinusitis treatment, hearing tests, and tonsillectomy.",
            leadDoctor = "Dr. Guruprasanth G",
            doctorsCount = 4,
            avgWaitTimeMins = 8,
            commonSymptoms = listOf("Sore Throat", "Earache", "Hearing Loss", "Sinus Pressure", "Nosebleed", "Snoring / Sleep Apnea"),
            imageUrl = "https://images.unsplash.com/photo-1516549655169-df83a0774514?auto=format&fit=crop&w=400&q=80"
        ),
        HospitalDepartment(
            id = "dept_derma",
            type = DepartmentType.DERMATOLOGY,
            description = "Skin allergy testing, acne laser therapy, eczema, psoriasis management, mole screening, and hair restoration.",
            leadDoctor = "Dr. L T Thenmozhi",
            doctorsCount = 4,
            avgWaitTimeMins = 7,
            commonSymptoms = listOf("Skin Rash", "Severe Itching", "Acne", "Eczema Flare", "Hair Loss", "Fungal Infection"),
            imageUrl = "https://images.unsplash.com/photo-1576091160399-112ba8d25d1d?auto=format&fit=crop&w=400&q=80"
        ),
        HospitalDepartment(
            id = "dept_dental",
            type = DepartmentType.DENTISTRY,
            description = "Root canal treatment, dental implants, cosmetic alignment, pediatric dentistry, and gum care.",
            leadDoctor = "Dr. Menaka Palaniappan Venkatesh",
            doctorsCount = 6,
            avgWaitTimeMins = 10,
            commonSymptoms = listOf("Toothache", "Bleeding Gums", "Cavity", "Broken Tooth", "Jaw Pain", "Wisdom Tooth"),
            imageUrl = "https://images.unsplash.com/photo-1606811841689-23dfddce3e95?auto=format&fit=crop&w=400&q=80"
        ),
        HospitalDepartment(
            id = "dept_emergency",
            type = DepartmentType.EMERGENCY,
            description = "Level-1 Trauma Center with 24/7 ACLS-trained ER physicians, instant resuscitation, CT/MRI on-standby, and cardiac response team.",
            leadDoctor = "Dr. T Sailaja (ER Lead)",
            doctorsCount = 10,
            avgWaitTimeMins = 0,
            commonSymptoms = listOf("Chest Pain / Cardiac Arrest", "Heavy Bleeding", "Severe Trauma", "Unconsciousness", "Difficulty Breathing"),
            imageUrl = "https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d?auto=format&fit=crop&w=400&q=80"
        )
    )

    // Digital Token State
    var activeToken by mutableStateOf<DigitalToken?>(
        DigitalToken(
            tokenNumber = 42,
            department = DepartmentType.GENERAL_MEDICINE,
            doctorName = "Dr. Gowtham H",
            roomNumber = "OPD Room 104",
            currentServingToken = 39,
            estimatedWaitMinutes = 12,
            issueTime = "09:15 AM",
            status = TokenStatus.ISSUED,
            isExpressFastTrack = true
        )
    )

    // Reactive Lab Reports
    var labReports by mutableStateOf(
        listOf(
            LabReport(
                id = "LAB-2026-9810",
                patientName = "Alex Jenkins",
                testName = "Complete Blood Count (CBC) with Differential",
                department = "Central Pathology Lab",
                prescribedBy = "Dr. Gowtham H",
                sampleDate = "14-Aug-2026, 08:30 AM",
                reportDate = "14-Aug-2026, 11:15 AM",
                status = LabStatus.READY,
                parameters = listOf(
                    LabTestParameter("Hemoglobin (Hb)", "14.2", "13.0 - 17.0", "g/dL"),
                    LabTestParameter("Total WBC Count", "7,400", "4,000 - 11,000", "/mcL"),
                    LabTestParameter("Platelet Count", "240,000", "150,000 - 450,000", "/mcL"),
                    LabTestParameter("RBC Count", "4.85", "4.5 - 5.9", "mil/mcL"),
                    LabTestParameter("Neutrophils", "62", "40 - 70", "%"),
                    LabTestParameter("Lymphocytes", "30", "20 - 40", "%")
                ),
                doctorSummary = "All hematological indices within standard physiological limits. No signs of infection or anemia.",
                cost = 45.0
            ),
            LabReport(
                id = "LAB-2026-9811",
                patientName = "Alex Jenkins",
                testName = "Comprehensive Metabolic & Lipid Panel",
                department = "Central Pathology Lab",
                prescribedBy = "Dr. Gowtham H",
                sampleDate = "14-Aug-2026, 08:30 AM",
                reportDate = "14-Aug-2026, 01:45 PM",
                status = LabStatus.READY,
                parameters = listOf(
                    LabTestParameter("Fasting Blood Glucose", "98", "70 - 100", "mg/dL"),
                    LabTestParameter("HbA1c Glycated Hemoglobin", "5.4", "< 5.7", "%"),
                    LabTestParameter("Total Cholesterol", "182", "< 200", "mg/dL"),
                    LabTestParameter("HDL (Good Cholesterol)", "52", "> 40", "mg/dL"),
                    LabTestParameter("LDL (Bad Cholesterol)", "105", "< 100", "mg/dL", isAbnormal = true),
                    LabTestParameter("Serum Creatinine", "0.92", "0.7 - 1.3", "mg/dL")
                ),
                doctorSummary = "Mild borderline elevation in LDL cholesterol. Advised low saturated fat diet, 30 mins brisk walking.",
                cost = 65.0
            ),
            LabReport(
                id = "LAB-2026-9812",
                patientName = "Alex Jenkins",
                testName = "Digital 12-Lead Electrocardiogram (ECG)",
                department = "Cardiology Diagnostics",
                prescribedBy = "Dr. Arul Selvan",
                sampleDate = "14-Aug-2026, 10:00 AM",
                reportDate = "14-Aug-2026, 10:20 AM",
                status = LabStatus.READY,
                parameters = listOf(
                    LabTestParameter("Heart Rate", "72", "60 - 100", "bpm"),
                    LabTestParameter("PR Interval", "154", "120 - 200", "ms"),
                    LabTestParameter("QRS Duration", "88", "80 - 120", "ms"),
                    LabTestParameter("QTc Interval", "412", "< 440", "ms")
                ),
                doctorSummary = "Normal sinus rhythm, normal axis. No acute ST-T wave abnormalities or ischemic changes noted.",
                cost = 50.0
            )
        )
    )

    // Reactive Prescriptions
    var prescriptions by mutableStateOf(
        listOf(
            HospitalPrescription(
                id = "RX-LC-44021",
                patientName = "Alex Jenkins",
                doctorName = "Dr. Gowtham H",
                department = "General Medicine",
                date = "14-Aug-2026",
                diagnosis = "Upper Respiratory Viral Rhinitis with mild tension headache",
                medications = listOf(
                    PrescriptionMedication(
                        name = "Paracetamol 650mg",
                        dosage = "650 mg Tablet",
                        frequency = "1 - 0 - 1 (Morning & Night)",
                        duration = "3 Days",
                        instructions = "Take after food with warm water for fever/pain",
                        price = 6.50,
                        dispenseStatus = PharmacyDispenseStatus.READY_FOR_DISPENSE
                    ),
                    PrescriptionMedication(
                        name = "Cetirizine 10mg",
                        dosage = "10 mg Tablet",
                        frequency = "0 - 0 - 1 (Night only)",
                        duration = "5 Days",
                        instructions = "Take at bedtime for allergy & nasal congestion",
                        price = 4.20,
                        dispenseStatus = PharmacyDispenseStatus.READY_FOR_DISPENSE
                    ),
                    PrescriptionMedication(
                        name = "Vitamin C + Zinc Chewables",
                        dosage = "500 mg",
                        frequency = "1 - 0 - 0 (After Breakfast)",
                        duration = "10 Days",
                        instructions = "Chew thoroughly for immunity support",
                        price = 8.00,
                        dispenseStatus = PharmacyDispenseStatus.READY_FOR_DISPENSE
                    )
                ),
                doctorNotes = "Drink plenty of warm fluids, steam inhalation twice daily. Review after 3 days if symptoms persist."
            )
        )
    )

    // Reactive Billing Invoices
    var billingInvoices by mutableStateOf(
        listOf(
            BillingInvoice(
                invoiceId = "INV-LC-2026-7890",
                patientName = "Alex Jenkins",
                date = "14-Aug-2026",
                department = "General Medicine & Diagnostics",
                items = listOf(
                    BillItem("Senior Specialist OPD Consultation (Dr. Gowtham H)", "Consultation", 50.00),
                    BillItem("Pre-Arrival Zero-Wait Priority Pass Processing", "Hospital Services", 0.00),
                    BillItem("Complete Blood Count (CBC) with Differential", "Pathology Lab", 45.00),
                    BillItem("Comprehensive Metabolic & Lipid Panel", "Pathology Lab", 65.00),
                    BillItem("Prescription Medications (Pharmacy Dispense)", "Pharmacy", 18.70)
                ),
                subtotal = 178.70,
                insuranceCovered = 50.00,
                taxAmount = 6.43,
                totalPayable = 135.13,
                paymentStatus = PaymentStatus.PENDING,
                paymentMethod = "UPI / Debit / Credit Card"
            )
        )
    )

    // Bed & Room Management Inventory
    var bedRooms by mutableStateOf(
        listOf(
            BedRoom(
                id = "room_icu_01",
                roomNumber = "ICU - Bay A",
                floor = "1st Floor, Critical Care Wing",
                category = BedCategory.ICU,
                totalBeds = 12,
                occupiedBeds = 9,
                pricePerDay = 350.0,
                amenities = listOf("Continuous Multi-Para Monitor", "Mechanical Ventilator", "Dedicated ICU Nurse 1:1", "Central Oxygen Pipeline"),
                isVentilatorEquipped = true
            ),
            BedRoom(
                id = "room_ccu_01",
                roomNumber = "CCU - Suite 2",
                floor = "2nd Floor, Heart Institute",
                category = BedCategory.CCU,
                totalBeds = 8,
                occupiedBeds = 5,
                pricePerDay = 400.0,
                amenities = listOf("Intra-Aortic Balloon Pump Support", "Telemetry Monitoring", "Emergency Defibrillator", "24/7 Interventional Cardiologist"),
                isVentilatorEquipped = true
            ),
            BedRoom(
                id = "room_deluxe_301",
                roomNumber = "Deluxe Room 301",
                floor = "3rd Floor, Executive Wing",
                category = BedCategory.DELUXE_PRIVATE,
                totalBeds = 1,
                occupiedBeds = 0,
                pricePerDay = 180.0,
                amenities = listOf("Motorized Orthopedic Bed", "Attendant Couch", "Ensuite Bathroom", "Smart TV & High-Speed WiFi", "Mini Refrigerator", "Nurse Call System")
            ),
            BedRoom(
                id = "room_deluxe_302",
                roomNumber = "Deluxe Room 302",
                floor = "3rd Floor, Executive Wing",
                category = BedCategory.DELUXE_PRIVATE,
                totalBeds = 1,
                occupiedBeds = 1,
                pricePerDay = 180.0,
                amenities = listOf("Motorized Bed", "Attendant Couch", "Ensuite Bathroom", "Smart TV", "Nurse Call System")
            ),
            BedRoom(
                id = "room_semi_401",
                roomNumber = "Semi-Private 401 (Twin)",
                floor = "4th Floor, Block A",
                category = BedCategory.SEMI_PRIVATE,
                totalBeds = 2,
                occupiedBeds = 1,
                pricePerDay = 95.0,
                amenities = listOf("Curtain Privacy Partition", "Individual Wardrobe", "Nurse Call System", "Shared Attached Washroom")
            ),
            BedRoom(
                id = "room_gen_501",
                roomNumber = "General Inpatient Ward 501",
                floor = "5th Floor, Main Tower",
                category = BedCategory.GENERAL_WARD,
                totalBeds = 20,
                occupiedBeds = 14,
                pricePerDay = 45.0,
                amenities = listOf("Central Air Conditioning", "Oxygen Port", "24/7 Nursing Desk", "Visitor Hours Access")
            )
        )
    )

    // Emergency Management Cases
    var emergencyCases by mutableStateOf(
        listOf(
            EmergencyCase(
                id = "ER-CASE-01",
                patientName = "David Miller",
                contactPhone = "+1 (555) 432-1098",
                emergencyType = "Acute Chest Pain / Angina",
                triageLevel = TriageSeverity.CRITICAL_RED,
                ambulanceEtaMinutes = 3,
                assignedDoctor = "Dr. Arul Selvan (Cardiologist on call)",
                assignedBed = "Trauma Resuscitation Bay 1",
                reportedTime = "09:40 AM",
                status = "Ambulance Inbound • Cath Lab Ready"
            ),
            EmergencyCase(
                id = "ER-CASE-02",
                patientName = "Emily Watson",
                contactPhone = "+1 (555) 234-8899",
                emergencyType = "Pediatric High Grade Febrile Seizure",
                triageLevel = TriageSeverity.URGENT_YELLOW,
                ambulanceEtaMinutes = 0,
                assignedDoctor = "Dr. Praveen Kumar",
                assignedBed = "ER Pediatric Bay 3",
                reportedTime = "09:25 AM",
                status = "In ER Observation • Vitals Stabilized"
            ),
            EmergencyCase(
                id = "ER-CASE-03",
                patientName = "Robert King",
                contactPhone = "+1 (555) 876-5432",
                emergencyType = "Deep Laceration & Minor Fracture",
                triageLevel = TriageSeverity.STABLE_GREEN,
                ambulanceEtaMinutes = 0,
                assignedDoctor = "Dr. B Sreedhar",
                assignedBed = "ER Minor Procedure Room",
                reportedTime = "08:50 AM",
                status = "Suturing Completed • X-Ray Cleared"
            )
        )
    )

    // Real-Time Hospital Notifications
    var notifications by mutableStateOf(
        listOf(
            HospitalNotificationItem(
                id = "NOTIF-01",
                title = "Zero-Wait Token Fast-Track Active",
                message = "Your Pre-Arrival records have been forwarded to Dr. Gowtham H. Current queue is at Token #39.",
                timestamp = "10 mins ago",
                type = NotificationType.TOKEN_UPDATE
            ),
            HospitalNotificationItem(
                id = "NOTIF-02",
                title = "Lab Diagnostic Reports Ready",
                message = "Complete Blood Count (CBC) and Lipid Panel results have been uploaded and verified by Pathology.",
                timestamp = "35 mins ago",
                type = NotificationType.LAB_READY
            ),
            HospitalNotificationItem(
                id = "NOTIF-03",
                title = "E-Prescription Sent to Pharmacy",
                message = "Dr. Gowtham H prescribed 3 medications. Available for instant pickup at Counter 2 (Basement 1).",
                timestamp = "1 hour ago",
                type = NotificationType.PHARMACY_READY
            ),
            HospitalNotificationItem(
                id = "NOTIF-04",
                title = "Billing Summary Generated",
                message = "Invoice INV-LC-2026-7890 of $135.13 is ready with $50 insurance co-pay discount applied.",
                timestamp = "1 hour ago",
                type = NotificationType.BILLING
            )
        )
    )

    // Quick Stats for Admin / Hospital Dashboard
    val totalBedsCount get() = bedRooms.sumOf { it.totalBeds }
    val occupiedBedsCount get() = bedRooms.sumOf { it.occupiedBeds }
    val availableBedsCount get() = totalBedsCount - occupiedBedsCount
    val bedOccupancyRate get() = if (totalBedsCount > 0) ((occupiedBedsCount.toFloat() / totalBedsCount) * 100).toInt() else 0

    // Actions
    fun markBillAsPaid(invoiceId: String) {
        billingInvoices = billingInvoices.map { inv ->
            if (inv.invoiceId == invoiceId) {
                inv.copy(paymentStatus = PaymentStatus.PAID)
            } else inv
        }
        notifications = listOf(
            HospitalNotificationItem(
                id = "NOTIF-${System.currentTimeMillis()}",
                title = "Payment Cleared Successfully",
                message = "Invoice $invoiceId settled. Digital receipt and discharge summary have been generated.",
                timestamp = "Just now",
                type = NotificationType.BILLING
            )
        ) + notifications
    }

    fun markMedicationDispensed(prescriptionId: String) {
        prescriptions = prescriptions.map { rx ->
            if (rx.id == prescriptionId) {
                rx.copy(
                    isDispensed = true,
                    medications = rx.medications.map { it.copy(dispenseStatus = PharmacyDispenseStatus.DISPENSED) }
                )
            } else rx
        }
        notifications = listOf(
            HospitalNotificationItem(
                id = "NOTIF-${System.currentTimeMillis()}",
                title = "Medicines Dispensed",
                message = "Prescription $prescriptionId has been dispensed at Life Care Pharmacy Counter 2.",
                timestamp = "Just now",
                type = NotificationType.PHARMACY_READY
            )
        ) + notifications
    }

    fun triggerEmergencySOS(patientName: String, phone: String, emergencyType: String, location: String): EmergencyCase {
        val newCase = EmergencyCase(
            id = "ER-CASE-${System.currentTimeMillis().toString().takeLast(4)}",
            patientName = patientName.ifBlank { "Emergency Patient" },
            contactPhone = phone.ifBlank { "+1 (800) 543-3227" },
            emergencyType = emergencyType.ifBlank { "Severe Acute Distress" },
            triageLevel = TriageSeverity.CRITICAL_RED,
            ambulanceEtaMinutes = 4,
            assignedDoctor = "Dr. T Sailaja (ER Trauma Team)",
            assignedBed = "ER Resuscitation Bay Red",
            reportedTime = DateTimeHelper.getCurrentTime(),
            status = "Ambulance Dispatched to $location"
        )
        emergencyCases = listOf(newCase) + emergencyCases
        notifications = listOf(
            HospitalNotificationItem(
                id = "NOTIF-${System.currentTimeMillis()}",
                title = "EMERGENCY AMBULANCE DISPATCHED",
                message = "Life Care Rapid Response Ambulance unit is en route. ETA 4 minutes. ER Trauma team is on standby.",
                timestamp = "Just now",
                type = NotificationType.EMERGENCY_ALERT
            )
        ) + notifications
        return newCase
    }

    fun reserveBed(roomId: String, patientName: String): Boolean {
        var success = false
        bedRooms = bedRooms.map { room ->
            if (room.id == roomId && room.availableBeds > 0) {
                success = true
                room.copy(occupiedBeds = room.occupiedBeds + 1)
            } else room
        }
        if (success) {
            notifications = listOf(
                HospitalNotificationItem(
                    id = "NOTIF-${System.currentTimeMillis()}",
                    title = "Hospital Bed Allocated",
                    message = "Bed reserved in $roomId for $patientName. Room preparation and nursing station alert sent.",
                    timestamp = "Just now",
                    type = NotificationType.APPOINTMENT
                )
            ) + notifications
        }
        return success
    }
}
