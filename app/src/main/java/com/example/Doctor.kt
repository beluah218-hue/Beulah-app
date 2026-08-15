package com.example

data class Doctor(
    val id: String,
    val name: String,
    val specialtyKey: String, // e.g. "Cardiology", "Pediatrics", "Dermatology", "Orthopedics", "General", "ENT", "Dentistry"
    val specialtyDisplay: String,
    val rating: Double,
    val nextAvailable: String,
    val location: String = "",
    val imageUrl: Any = "",
    val yearsExperience: Int,
    val tokenNumber: String = "#42",
    val titlePrefix: String = "Dr."
)

object DoctorRepository {
    val doctors = listOf(
        // ==========================================
        // 1. Vijay Super Speciality Hospital Doctors (vijaysuperspecialityhospital.in/doctors/)
        // ==========================================
        Doctor(
            id = "vssh_dr_p_prasanth",
            name = "P. Prasanth",
            specialtyKey = "General",
            specialtyDisplay = "Consultant General Physician & Critical Care (M.B.B.S., M.D. RML Delhi)",
            rating = 4.9,
            nextAvailable = "Today, 10:00 AM",
            yearsExperience = 15,
            tokenNumber = "#01",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "vssh_dr_k_shanmugam",
            name = "K. Shanmugam",
            specialtyKey = "Cardiology",
            specialtyDisplay = "Senior Consultant Interventional Cardiologist (M.B.B.S., M.D., D.M. AIIMS)",
            rating = 5.0,
            nextAvailable = "Today, 11:00 AM",
            yearsExperience = 22,
            tokenNumber = "#03",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "vssh_dr_g_sarath_babu",
            name = "G. Sarath Babu",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Nephrologist & Transplant Physician (M.D. JIPMER, D.M. PGI)",
            rating = 4.9,
            nextAvailable = "Today, 02:00 PM",
            yearsExperience = 16,
            tokenNumber = "#06",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "vssh_dr_m_srinivasan",
            name = "M. Srinivasan",
            specialtyKey = "General",
            specialtyDisplay = "Surgical Gastroenterologist & HPB Surgeon (M.S. PGIMER, M.Ch, FALS)",
            rating = 4.9,
            nextAvailable = "Tomorrow, 10:30 AM",
            yearsExperience = 17,
            tokenNumber = "#09",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "vssh_dr_vijayakumar_s",
            name = "Vijayakumar S.",
            specialtyKey = "General",
            specialtyDisplay = "Director & Senior Medical Oncologist (M.B.B.S., M.D., D.M. Medical Oncology)",
            rating = 5.0,
            nextAvailable = "Tomorrow, 11:30 AM",
            yearsExperience = 24,
            tokenNumber = "#11",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "vssh_dr_n_vijayamurugan",
            name = "N. Vijayamurugan",
            specialtyKey = "Pediatrics",
            specialtyDisplay = "Pediatric & Medical Oncologist (M.D. Pediatrics AIIMS, D.M. AIIMS)",
            rating = 4.9,
            nextAvailable = "Tomorrow, 03:00 PM",
            yearsExperience = 16,
            tokenNumber = "#13",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "vssh_dr_c_gokul",
            name = "C. Gokul",
            specialtyKey = "Orthopedics",
            specialtyDisplay = "Orthopedic Trauma & Joint Replacement Surgeon (M.B.B.S., M.S. Ortho)",
            rating = 4.8,
            nextAvailable = "Today, 04:00 PM",
            yearsExperience = 13,
            tokenNumber = "#16",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "vssh_dr_vinitha_vairam",
            name = "Vinitha Vairam",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Gynaecologist & Obstetrician (M.B.B.S., M.S. OBG)",
            rating = 4.9,
            nextAvailable = "Wednesday, 10:00 AM",
            yearsExperience = 12,
            tokenNumber = "#18",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "vssh_dr_g_udhayabharathi",
            name = "G. Udhayabharathi",
            specialtyKey = "General",
            specialtyDisplay = "Senior Neurosurgeon & Spine Specialist (M.B.B.S., M.Ch Neurosurgery)",
            rating = 4.9,
            nextAvailable = "Wednesday, 02:30 PM",
            yearsExperience = 18,
            tokenNumber = "#21",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "vssh_dr_m_pragatheeswarane",
            name = "M. Pragatheeswarane",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Urologist & Andrologist (M.B.B.S., M.S., M.Ch PGIMER)",
            rating = 4.8,
            nextAvailable = "Thursday, 11:00 AM",
            yearsExperience = 14,
            tokenNumber = "#24",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "vssh_dr_a_naveeth_shukkur",
            name = "A. Naveeth Shukkur",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Surgical Oncologist (M.B.B.S., M.S., M.Ch JIPMER)",
            rating = 4.9,
            nextAvailable = "Friday, 10:00 AM",
            yearsExperience = 15,
            tokenNumber = "#27",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "vssh_dr_dinesh_sundararajan",
            name = "Dinesh Sundararajan",
            specialtyKey = "General",
            specialtyDisplay = "Radiation Oncologist (M.B.B.S., M.D., FRCR Precision Radiation)",
            rating = 4.8,
            nextAvailable = "Friday, 02:00 PM",
            yearsExperience = 14,
            tokenNumber = "#29",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "vssh_dr_e_siyam_sundar",
            name = "E. Siyam Sundar",
            specialtyKey = "General",
            specialtyDisplay = "Chief Intensivist & Critical Care Specialist (M.D., DNB, IDCCM)",
            rating = 4.9,
            nextAvailable = "Today, 05:00 PM",
            yearsExperience = 16,
            tokenNumber = "#31",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "vssh_dr_g_vijay",
            name = "G. Vijay",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Plastic & Reconstructive Surgeon (M.B.B.S., M.S., M.Ch)",
            rating = 4.9,
            nextAvailable = "Saturday, 11:30 AM",
            yearsExperience = 17,
            tokenNumber = "#34",
            titlePrefix = "Dr."
        ),

        // ==========================================
        // 2. Royal Hospital Doctors (royalhospital.net.in/doctors.html)
        // ==========================================
        Doctor(
            id = "dr_v_arun_kumar",
            name = "V. Arun Kumar",
            specialtyKey = "General",
            specialtyDisplay = "General Physician, Diabetologist & Intensivist (M.B.B.S., M.D.)",
            rating = 4.9,
            nextAvailable = "Today, 10:30 AM",
            yearsExperience = 16,
            tokenNumber = "#05",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "dr_c_s_kailash",
            name = "C.S. Kailash",
            specialtyKey = "Orthopedics",
            specialtyDisplay = "Trauma, Spine & Joint Replacement Surgeon (M.S. Ortho, DNB)",
            rating = 4.9,
            nextAvailable = "Today, 11:30 AM",
            yearsExperience = 18,
            tokenNumber = "#07",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "dr_a_kalaiarasan",
            name = "A. Kalaiarasan",
            specialtyKey = "ENT",
            specialtyDisplay = "Endoscopic ENT, Head & Neck Surgeon (M.S. ENT, DNB)",
            rating = 4.8,
            nextAvailable = "Today, 03:00 PM",
            yearsExperience = 14,
            tokenNumber = "#11",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "dr_s_sakthivel",
            name = "S. Sakthivel",
            specialtyKey = "General",
            specialtyDisplay = "General, Laparoscopic & Endoscopic Surgeon (M.S. Gen Surg, FIAGES)",
            rating = 4.9,
            nextAvailable = "Tomorrow, 10:00 AM",
            yearsExperience = 19,
            tokenNumber = "#15",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "dr_ragapriya",
            name = "Ragapriya",
            specialtyKey = "General",
            specialtyDisplay = "Clinical & Radiation Oncologist (M.D. Radiation Oncology)",
            rating = 4.8,
            nextAvailable = "Tomorrow, 11:30 AM",
            yearsExperience = 11,
            tokenNumber = "#18",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "dr_yadhu_lokanath",
            name = "Yadhu Lokanath",
            specialtyKey = "General",
            specialtyDisplay = "Neuro Surgeon & Spine Surgeon (M.S., M.Ch Neurosurgery)",
            rating = 4.9,
            nextAvailable = "Wednesday, 10:00 AM",
            yearsExperience = 15,
            tokenNumber = "#21",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "dr_sushruth_shetty",
            name = "Sushruth Shetty",
            specialtyKey = "General",
            specialtyDisplay = "Surgical Gastroenterologist & HPB Surgeon (M.S., M.Ch GI Surg)",
            rating = 4.8,
            nextAvailable = "Wednesday, 02:30 PM",
            yearsExperience = 13,
            tokenNumber = "#24",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "dr_prasenjit_sutradhar",
            name = "Prasenjit Sutradhar",
            specialtyKey = "General",
            specialtyDisplay = "General and Vascular Surgeon (M.S. Gen Surg, Fellow Vascular)",
            rating = 4.7,
            nextAvailable = "Thursday, 11:00 AM",
            yearsExperience = 12,
            tokenNumber = "#26",
            titlePrefix = "Dr."
        ),

        // ==========================================
        // 3. Kavan Multispeciality Hospital Doctors (kavanhospital.com/doctors.php)
        // ==========================================
        Doctor(
            id = "kavan_dr_v_arun",
            name = "V. Arun",
            specialtyKey = "Orthopedics",
            specialtyDisplay = "Chief Orthopedic & Joint Replacement Surgeon (M.S. Ortho)",
            rating = 4.9,
            nextAvailable = "Today, 10:00 AM",
            yearsExperience = 20,
            tokenNumber = "#02",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "kavan_dr_v_prabhuraja",
            name = "V. Prabhuraja",
            specialtyKey = "Orthopedics",
            specialtyDisplay = "Orthopedic & Arthroscopy Surgeon (M.S. Ortho, Fellowship Arthroscopy)",
            rating = 4.8,
            nextAvailable = "Today, 11:30 AM",
            yearsExperience = 14,
            tokenNumber = "#08",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "kavan_dr_m_saravanan",
            name = "M. Saravanan",
            specialtyKey = "General",
            specialtyDisplay = "Consultant General Physician & Diabetologist (M.B.B.S., M.D.)",
            rating = 4.9,
            nextAvailable = "Today, 03:30 PM",
            yearsExperience = 16,
            tokenNumber = "#12",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "kavan_dr_kirthika",
            name = "Kirthika",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Obstetrician & Gynaecologist (M.B.B.S., D.G.O., DNB)",
            rating = 4.9,
            nextAvailable = "Tomorrow, 09:30 AM",
            yearsExperience = 13,
            tokenNumber = "#14",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "kavan_dr_t_gokul",
            name = "T. Gokul",
            specialtyKey = "General",
            specialtyDisplay = "General & Laparoscopic Surgeon (M.S. General Surgery)",
            rating = 4.8,
            nextAvailable = "Tomorrow, 11:00 AM",
            yearsExperience = 12,
            tokenNumber = "#17",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "kavan_dr_a_venniladevi",
            name = "A. Venniladevi",
            specialtyKey = "General",
            specialtyDisplay = "Senior Ophthalmologist & Eye Surgeon (M.S. Ophthalmology, DO)",
            rating = 4.9,
            nextAvailable = "Tomorrow, 02:30 PM",
            yearsExperience = 15,
            tokenNumber = "#19",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "kavan_dr_thennarasu",
            name = "Thennarasu",
            specialtyKey = "General",
            specialtyDisplay = "Cleft & Craniofacial Plastic Surgeon (M.D.S., Fellowship Maxillofacial)",
            rating = 4.9,
            nextAvailable = "Wednesday, 10:30 AM",
            yearsExperience = 17,
            tokenNumber = "#22",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "kavan_dr_m_deepanand",
            name = "M. Deepanand",
            specialtyKey = "ENT",
            specialtyDisplay = "Consultant ENT, Head & Neck Surgeon (M.S. ENT)",
            rating = 4.8,
            nextAvailable = "Wednesday, 03:00 PM",
            yearsExperience = 11,
            tokenNumber = "#25",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "kavan_dr_k_indhumathi",
            name = "K. Indhumathi",
            specialtyKey = "Dermatology",
            specialtyDisplay = "Consultant Dermatologist & Cosmetologist (M.D. DVL)",
            rating = 4.9,
            nextAvailable = "Thursday, 10:00 AM",
            yearsExperience = 10,
            tokenNumber = "#28",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "kavan_dr_pradeep_kumar",
            name = "Pradeep Kumar",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Neurosurgeon & Spine Care (M.Ch Neurosurgery)",
            rating = 4.9,
            nextAvailable = "Thursday, 02:30 PM",
            yearsExperience = 16,
            tokenNumber = "#30",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "kavan_dr_m_balaji",
            name = "M. Balaji",
            specialtyKey = "Dentistry",
            specialtyDisplay = "Dental Surgeon & Implant Specialist (B.D.S., M.D.S.)",
            rating = 4.8,
            nextAvailable = "Friday, 11:00 AM",
            yearsExperience = 14,
            tokenNumber = "#32",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "kavan_dr_n_iniya",
            name = "N. Iniya",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Psychiatrist & Behavioral Therapist (M.D. Psychiatry)",
            rating = 4.8,
            nextAvailable = "Friday, 04:00 PM",
            yearsExperience = 9,
            tokenNumber = "#36",
            titlePrefix = "Dr."
        ),

        // ==========================================
        // 4. TCR Multi Speciality Hospital Doctors
        // ==========================================
        Doctor(
            id = "dr_soundara_rajan",
            name = "C. Soundara Rajan",
            specialtyKey = "Cardiology",
            specialtyDisplay = "Managing Director, Emergency & ICU, Cardiology (M.D., FCC)",
            rating = 5.0,
            nextAvailable = "Today, 10:00 AM",
            yearsExperience = 25,
            tokenNumber = "#10",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "dr_gm_monika",
            name = "G.M. Monika",
            specialtyKey = "General",
            specialtyDisplay = "Consultant General Medicine & Diabetology (M.B.B.S., M.D.)",
            rating = 4.9,
            nextAvailable = "Today, 11:30 AM",
            yearsExperience = 11,
            tokenNumber = "#14",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "dr_ranjana_t",
            name = "Ranjana T",
            specialtyKey = "General",
            specialtyDisplay = "Consultant General Medicine & Critical Care (M.B.B.S., DNB)",
            rating = 4.8,
            nextAvailable = "Today, 03:30 PM",
            yearsExperience = 9,
            tokenNumber = "#19",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "dr_udhayachandrika",
            name = "Udhayachandrika",
            specialtyKey = "Dentistry",
            specialtyDisplay = "Dental Expert & Oral Maxillofacial Surgeon (B.D.S., M.D.S.)",
            rating = 4.9,
            nextAvailable = "Tomorrow, 10:00 AM",
            yearsExperience = 12,
            tokenNumber = "#08",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "dr_radhakrishnan_a",
            name = "Radhakrishnan A",
            specialtyKey = "General",
            specialtyDisplay = "Senior General Physician & Internal Medicine (M.B.B.S., M.D.)",
            rating = 4.9,
            nextAvailable = "Today, 05:00 PM",
            yearsExperience = 21,
            tokenNumber = "#22",
            titlePrefix = "Dr."
        ),

        // ==========================================
        // 5. Gunam Super Speciality Hospital Doctors (gunamhospital.com/doctors/krishnagiri)
        // ==========================================
        Doctor(
            id = "dr_karthik_pandian",
            name = "Karthik Pandian",
            specialtyKey = "General",
            specialtyDisplay = "Head of Medical ICU & Critical Care (M.D., IDCCM)",
            rating = 4.9,
            nextAvailable = "Today, 11:30 AM",
            yearsExperience = 15,
            tokenNumber = "#04",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "dr_pradeep_kumar",
            name = "K Pradeep Kumar",
            specialtyKey = "ENT",
            specialtyDisplay = "Consultant ENT, Head & Neck Surgeon (D.Co, DNB)",
            rating = 4.9,
            nextAvailable = "Today, 04:00 PM",
            yearsExperience = 14,
            tokenNumber = "#09",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "dr_m_senthil",
            name = "M. Senthil",
            specialtyKey = "General",
            specialtyDisplay = "Senior Laparoscopic & General Surgeon (M.S., D.MAS)",
            rating = 4.9,
            nextAvailable = "Tomorrow, 10:00 AM",
            yearsExperience = 22,
            tokenNumber = "#12",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "dr_kavitha_senthil",
            name = "Kavitha Senthil",
            specialtyKey = "General",
            specialtyDisplay = "Senior Consultant Gynaecologist & Obstetrician (M.B.B.S., D.G.O., DNB)",
            rating = 4.9,
            nextAvailable = "Today, 04:30 PM",
            yearsExperience = 18,
            tokenNumber = "#16",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "dr_rajesh_kumar",
            name = "J Rajesh Kumar",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Physician & Diabetologist (M.D.)",
            rating = 4.8,
            nextAvailable = "Today, 05:30 PM",
            yearsExperience = 16,
            tokenNumber = "#20",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "dr_subramanian",
            name = "Subramanian",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Gastroenterologist & Hepatologist (MD, DM)",
            rating = 4.9,
            nextAvailable = "Wednesday, 10:30 AM",
            yearsExperience = 18,
            tokenNumber = "#23",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "dr_prabhu_dev",
            name = "Prabhu Dev K B",
            specialtyKey = "Orthopedics",
            specialtyDisplay = "Consultant Orthopaedic & Joint Replacement Surgeon (MS Ortho)",
            rating = 4.9,
            nextAvailable = "Tomorrow, 03:00 PM",
            yearsExperience = 17,
            tokenNumber = "#25",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "dr_sai_sudha",
            name = "Sai Sudha",
            specialtyKey = "General",
            specialtyDisplay = "Consultant General & Laparoscopic Surgeon (MBBS, MD)",
            rating = 4.8,
            nextAvailable = "Today, 02:30 PM",
            yearsExperience = 12,
            tokenNumber = "#27",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "dr_santhosh_kumar",
            name = "Santhosh Kumar",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Neurologist & Stroke Specialist (MD, DM)",
            rating = 4.9,
            nextAvailable = "Thursday, 11:00 AM",
            yearsExperience = 14,
            tokenNumber = "#29",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "dr_rajesh_iyer",
            name = "Rajesh Iyer B",
            specialtyKey = "General",
            specialtyDisplay = "Senior Consultant Neurologist & Spine Specialist (M.D., D.M. Neuro)",
            rating = 4.9,
            nextAvailable = "Wednesday, 03:00 PM",
            yearsExperience = 20,
            tokenNumber = "#31",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "dr_gowrishankar",
            name = "Gowrishankar",
            specialtyKey = "Orthopedics",
            specialtyDisplay = "Senior Consultant Orthopaedic & Trauma Specialist (M.S. Ortho)",
            rating = 4.8,
            nextAvailable = "Tomorrow, 05:00 PM",
            yearsExperience = 16,
            tokenNumber = "#33",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "dr_sudha_sagar",
            name = "Sudha Sagar",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Pulmonologist & Chest Physician (MD)",
            rating = 4.8,
            nextAvailable = "Tomorrow, 11:30 AM",
            yearsExperience = 13,
            tokenNumber = "#35",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "dr_dayananda",
            name = "Dayananda S",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Surgical Oncologist (MS, MCh)",
            rating = 4.9,
            nextAvailable = "Friday, 09:30 AM",
            yearsExperience = 19,
            tokenNumber = "#37",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "dr_shanmuga_priya",
            name = "Shanmuga Priya",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Clinical Nutritionist & Dietitian (M.Sc., PGD Clinical Dietetics)",
            rating = 4.8,
            nextAvailable = "Today, 02:00 PM",
            yearsExperience = 11,
            tokenNumber = "#39",
            titlePrefix = "Dr."
        ),

        // ==========================================
        // 6. Life Care Senior Specialist Doctors
        // ==========================================
        Doctor(
            id = "sathish_krishnan",
            name = "Sathish Krishnan",
            specialtyKey = "General",
            specialtyDisplay = "Physiatrist & Rehab Specialist",
            rating = 4.8,
            nextAvailable = "Today, 04:30 PM",
            yearsExperience = 12,
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "gowtham_h",
            name = "Gowtham H",
            specialtyKey = "General",
            specialtyDisplay = "Internal Medicine Specialist",
            rating = 4.8,
            nextAvailable = "Today, 10:00 AM",
            yearsExperience = 10,
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "t_sailaja",
            name = "T Sailaja",
            specialtyKey = "General",
            specialtyDisplay = "Diabetologist & General Physician",
            rating = 4.7,
            nextAvailable = "Tomorrow, 09:30 AM",
            yearsExperience = 10,
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "b_sreedhar",
            name = "B Sreedhar",
            specialtyKey = "Orthopedics",
            specialtyDisplay = "Senior Orthopaedician",
            rating = 4.9,
            nextAvailable = "Tomorrow, 02:00 PM",
            yearsExperience = 23,
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "menaka_venkatesh",
            name = "Menaka Palaniappan Venkatesh",
            specialtyKey = "Dentistry",
            specialtyDisplay = "Dentist & Dental Surgeon",
            rating = 4.9,
            nextAvailable = "${DateTimeHelper.getCurrentDate()}, 11:00 AM",
            yearsExperience = 16,
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "l_t_thenmozhi",
            name = "L T Thenmozhi",
            specialtyKey = "General",
            specialtyDisplay = "Family Medicine Practitioner",
            rating = 4.7,
            nextAvailable = "Today, 06:00 PM",
            yearsExperience = 13,
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "guruprasanth_g",
            name = "Guruprasanth G",
            specialtyKey = "ENT",
            specialtyDisplay = "Surgical Gastroenterologist & ENT",
            rating = 4.8,
            nextAvailable = "Monday, 08:30 AM",
            yearsExperience = 11,
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "arul_selvan",
            name = "Arul Selvan",
            specialtyKey = "Cardiology",
            specialtyDisplay = "Senior Cardiologist",
            rating = 4.9,
            nextAvailable = "${DateTimeHelper.getCurrentDate()}, 10:30 AM",
            yearsExperience = 12,
            tokenNumber = "#42",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "praveen_kumar",
            name = "Praveen Kumar",
            specialtyKey = "Pediatrics",
            specialtyDisplay = "Consultant General Physician & Pediatrician",
            rating = 4.7,
            nextAvailable = "Today, 08:12 PM",
            yearsExperience = 15,
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "harish_jain",
            name = "Harish Jain",
            specialtyKey = "Dentistry",
            specialtyDisplay = "Dentist & Orthodontist",
            rating = 4.8,
            nextAvailable = "Tomorrow, 10:00 AM",
            yearsExperience = 17,
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "thanga_mani",
            name = "Thanga Mani",
            specialtyKey = "Dentistry",
            specialtyDisplay = "Dentist & Dental Surgeon",
            rating = 4.9,
            nextAvailable = "Today, 03:00 PM",
            yearsExperience = 20,
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "ganesh_b_n",
            name = "Ganesh B N Narasimmiah",
            specialtyKey = "Dentistry",
            specialtyDisplay = "Prosthodontist & Dental Surgeon",
            rating = 4.8,
            nextAvailable = "Wednesday, 11:30 AM",
            yearsExperience = 13,
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "dhanasekar_balasundaram",
            name = "Dhanasekar Balasundaram",
            specialtyKey = "Dentistry",
            specialtyDisplay = "Dentist & Implantologist",
            rating = 4.9,
            nextAvailable = "${DateTimeHelper.getCurrentDate()}, 09:00 AM",
            yearsExperience = 24,
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "hareekrishnan",
            name = "Hareekrishnan",
            specialtyKey = "General",
            specialtyDisplay = "General Practitioner",
            rating = 4.5,
            nextAvailable = "Today, 05:00 PM",
            yearsExperience = 1,
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "sugantha_meenskshi",
            name = "Sugantha Meenskshi E",
            specialtyKey = "Dentistry",
            specialtyDisplay = "Dentist & Endodontist",
            rating = 4.8,
            nextAvailable = "Tomorrow, 04:00 PM",
            yearsExperience = 12,
            titlePrefix = "Dr."
        ),

        // ==========================================
        // 7. Krishnagiri Road Hospitals Doctors, Tirupattur (Justdial Directory nct-10253670)
        // ==========================================
        Doctor(
            id = "jd_dr_m_kalyanasundaram",
            name = "M. Kalyanasundaram",
            specialtyKey = "General",
            specialtyDisplay = "Senior Consultant General Physician & Diabetologist (M.B.B.S., M.D.)",
            rating = 4.9,
            nextAvailable = "Today, 10:00 AM",
            yearsExperience = 22,
            tokenNumber = "#05",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "jd_dr_r_chellappan",
            name = "R. Chellappan",
            specialtyKey = "General",
            specialtyDisplay = "Consultant General Physician & Critical Care (M.B.B.S., M.D. Gen Med)",
            rating = 4.8,
            nextAvailable = "Today, 11:30 AM",
            yearsExperience = 18,
            tokenNumber = "#09",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "jd_dr_s_elamparithi",
            name = "S. Elamparithi",
            specialtyKey = "Pediatrics",
            specialtyDisplay = "Senior Consultant Pediatrician & Neonatologist (M.D. Pediatrics, DCH)",
            rating = 5.0,
            nextAvailable = "Today, 04:00 PM",
            yearsExperience = 19,
            tokenNumber = "#13",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "jd_dr_john_m_c",
            name = "John M.C.",
            specialtyKey = "Pediatrics",
            specialtyDisplay = "Consultant Pediatrician & Child Specialist (M.B.B.S., DCH)",
            rating = 4.8,
            nextAvailable = "Tomorrow, 10:00 AM",
            yearsExperience = 15,
            tokenNumber = "#17",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "jd_dr_t_p_prabakar",
            name = "T.P. Prabakar",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Gastroenterologist & Endoscopist (M.D., D.M. Gastro)",
            rating = 4.9,
            nextAvailable = "Tomorrow, 11:30 AM",
            yearsExperience = 17,
            tokenNumber = "#21",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "jd_dr_k_sadayandi",
            name = "K. Sadayandi",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Ophthalmologist & Eye Surgeon (M.S. Ophthalmology, DO)",
            rating = 4.9,
            nextAvailable = "Wednesday, 10:30 AM",
            yearsExperience = 24,
            tokenNumber = "#26",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "jd_dr_kalyani_gajendran",
            name = "Kalyani Gajendran",
            specialtyKey = "General",
            specialtyDisplay = "Senior Obstetrician & Gynaecologist (M.B.B.S., D.G.O.)",
            rating = 4.9,
            nextAvailable = "Wednesday, 03:00 PM",
            yearsExperience = 20,
            tokenNumber = "#28",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "jd_dr_dharun",
            name = "Dharun Kumar",
            specialtyKey = "Cardiology",
            specialtyDisplay = "Chief Interventional Cardiologist (M.D., D.M. Cardiology, FCC)",
            rating = 5.0,
            nextAvailable = "Thursday, 10:00 AM",
            yearsExperience = 16,
            tokenNumber = "#31",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "jd_dr_arulmozhi",
            name = "Arulmozhi S.",
            specialtyKey = "General",
            specialtyDisplay = "Medical Director & Laparoscopic Surgeon (M.S. General Surgery, FAIS)",
            rating = 4.9,
            nextAvailable = "Thursday, 02:30 PM",
            yearsExperience = 25,
            tokenNumber = "#35",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "jd_dr_vignesh_r",
            name = "R. Vignesh",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Laparoscopic & Emergency Surgeon (M.S. Gen Surg, FIAGES)",
            rating = 4.8,
            nextAvailable = "Friday, 11:00 AM",
            yearsExperience = 13,
            tokenNumber = "#38",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "jd_dr_j_jeyalakshmi",
            name = "J. Jeyalakshmi",
            specialtyKey = "General",
            specialtyDisplay = "Senior Eye Specialist & Glaucoma Surgeon (M.S. Ophthalmology, FICO)",
            rating = 4.9,
            nextAvailable = "Friday, 03:30 PM",
            yearsExperience = 16,
            tokenNumber = "#41",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "jd_dr_g_balaji",
            name = "G. Balaji",
            specialtyKey = "Orthopedics",
            specialtyDisplay = "Consultant Orthopedic & Trauma Surgeon (M.B.B.S., M.S. Ortho)",
            rating = 4.8,
            nextAvailable = "Saturday, 10:30 AM",
            yearsExperience = 14,
            tokenNumber = "#44",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "jd_dr_j_malarvannan",
            name = "J. Malarvannan",
            specialtyKey = "General",
            specialtyDisplay = "Senior Physician & Emergency Care Specialist (M.B.B.S., M.D. Gen Med)",
            rating = 4.9,
            nextAvailable = "Saturday, 02:00 PM",
            yearsExperience = 19,
            tokenNumber = "#47",
            titlePrefix = "Dr."
        ),

        // ==========================================
        // 8. Thangam Hospital Doctors (thangamhospital.in)
        // ==========================================
        Doctor(
            id = "thangam_dr_v_thangam",
            name = "V. Thangam",
            specialtyKey = "General",
            specialtyDisplay = "Senior Consultant Obstetrician & Gynaecologist (M.B.B.S., D.G.O.)",
            rating = 5.0,
            nextAvailable = "Today, 10:00 AM",
            yearsExperience = 25,
            tokenNumber = "#02",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "thangam_dr_r_senthil",
            name = "R. Senthil",
            specialtyKey = "General",
            specialtyDisplay = "Senior Consultant Urologist & Andrologist (M.S., M.Ch Urology)",
            rating = 4.9,
            nextAvailable = "Today, 11:30 AM",
            yearsExperience = 22,
            tokenNumber = "#06",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "thangam_dr_t_muthu",
            name = "T. Muthu",
            specialtyKey = "General",
            specialtyDisplay = "Senior General & Laparoscopic Surgeon (M.S. Gen Surgery, FAIS)",
            rating = 4.9,
            nextAvailable = "Today, 03:00 PM",
            yearsExperience = 20,
            tokenNumber = "#10",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "thangam_dr_n_govindaraj",
            name = "N. Govindaraj",
            specialtyKey = "ENT",
            specialtyDisplay = "Consultant ENT, Head & Neck Surgeon (M.S. ENT)",
            rating = 4.8,
            nextAvailable = "Tomorrow, 10:30 AM",
            yearsExperience = 16,
            tokenNumber = "#15",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "thangam_dr_balaji_viswanath",
            name = "Balaji Viswanath",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Radiologist & Imaging Specialist (M.D. Radio-Diagnosis)",
            rating = 4.9,
            nextAvailable = "Tomorrow, 11:30 AM",
            yearsExperience = 14,
            tokenNumber = "#19",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "thangam_dr_k_velavan",
            name = "K. Velavan",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Surgical Oncologist & Cancer Specialist (M.S., M.Ch)",
            rating = 4.9,
            nextAvailable = "Wednesday, 10:00 AM",
            yearsExperience = 18,
            tokenNumber = "#23",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "thangam_dr_jaganathan",
            name = "Jaganathan",
            specialtyKey = "Pediatrics",
            specialtyDisplay = "Senior Consultant Paediatric Surgeon (M.S., M.Ch Paed Surg)",
            rating = 4.9,
            nextAvailable = "Wednesday, 02:30 PM",
            yearsExperience = 17,
            tokenNumber = "#27",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "thangam_dr_vivek_praveen",
            name = "Vivek Praveen",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Nephrologist & Kidney Specialist (M.D., D.M. Nephrology)",
            rating = 4.8,
            nextAvailable = "Thursday, 11:00 AM",
            yearsExperience = 13,
            tokenNumber = "#30",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "thangam_dr_karpagam",
            name = "Karpagam",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Psychiatrist & Behavioral Health (M.D. Psychiatry)",
            rating = 4.8,
            nextAvailable = "Thursday, 03:30 PM",
            yearsExperience = 12,
            tokenNumber = "#34",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "thangam_dr_ashok_birla",
            name = "Ashok Birla",
            specialtyKey = "General",
            specialtyDisplay = "Senior Consultant Anesthesiologist & Critical Care (M.D. Anaesthesia)",
            rating = 4.9,
            nextAvailable = "Friday, 10:00 AM",
            yearsExperience = 19,
            tokenNumber = "#37",
            titlePrefix = "Dr."
        ),

        // ==========================================
        // 9. GCC Hospitals Doctors (gcchospitals.com/specialist.php)
        // ==========================================
        Doctor(
            id = "gcc_dr_ramu_gunasekar",
            name = "Ramu Gunasekar",
            specialtyKey = "Cardiology",
            specialtyDisplay = "Senior Consultant Interventional Cardiologist (M.D., D.M. Cardiology)",
            rating = 5.0,
            nextAvailable = "Today, 10:30 AM",
            yearsExperience = 21,
            tokenNumber = "#03",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "gcc_dr_karthik",
            name = "Karthik",
            specialtyKey = "Cardiology",
            specialtyDisplay = "Consultant Cardiologist & Heart Specialist (M.D., D.M. Cardiology)",
            rating = 4.8,
            nextAvailable = "Today, 02:00 PM",
            yearsExperience = 14,
            tokenNumber = "#07",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "gcc_dr_ponghuzali",
            name = "Ponghuzali",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Obstetrician & Gynaecologist (M.B.B.S., M.S. OBG)",
            rating = 4.9,
            nextAvailable = "Today, 04:30 PM",
            yearsExperience = 16,
            tokenNumber = "#11",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "gcc_dr_shanmuga_shanthini",
            name = "Shanmuga Shanthini",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Obstetrician, Gynaecologist & Infertility (M.B.B.S., DGO)",
            rating = 4.9,
            nextAvailable = "Tomorrow, 10:00 AM",
            yearsExperience = 13,
            tokenNumber = "#14",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "gcc_dr_dhatchinamurthi",
            name = "Dhatchinamurthi",
            specialtyKey = "Pediatrics",
            specialtyDisplay = "Senior Consultant Paediatric Surgeon (M.S., M.Ch Paed Surg)",
            rating = 4.9,
            nextAvailable = "Tomorrow, 11:30 AM",
            yearsExperience = 18,
            tokenNumber = "#18",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "gcc_dr_sivashankar",
            name = "Sivashankar",
            specialtyKey = "General",
            specialtyDisplay = "Senior Consultant General & Laparoscopic Surgeon (M.S. General Surgery)",
            rating = 4.9,
            nextAvailable = "Tomorrow, 03:00 PM",
            yearsExperience = 19,
            tokenNumber = "#20",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "gcc_dr_sadhasivam",
            name = "Sadhasivam",
            specialtyKey = "General",
            specialtyDisplay = "Consultant General & GI Surgeon (M.S. General Surgery, FMAS)",
            rating = 4.8,
            nextAvailable = "Wednesday, 10:00 AM",
            yearsExperience = 15,
            tokenNumber = "#24",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "gcc_dr_pradeep",
            name = "Pradeep",
            specialtyKey = "General",
            specialtyDisplay = "Consultant General Physician & Intensivist (M.B.B.S., M.D. Gen Med)",
            rating = 4.8,
            nextAvailable = "Wednesday, 02:00 PM",
            yearsExperience = 12,
            tokenNumber = "#26",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "gcc_dr_sivakumar",
            name = "Sivakumar",
            specialtyKey = "Orthopedics",
            specialtyDisplay = "Chief Orthopedic & Spine Surgeon (M.S. Ortho, Fellowship Spine)",
            rating = 5.0,
            nextAvailable = "Wednesday, 04:00 PM",
            yearsExperience = 22,
            tokenNumber = "#29",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "gcc_dr_moulidharan",
            name = "Moulidharan",
            specialtyKey = "Orthopedics",
            specialtyDisplay = "Consultant Orthopedic Surgeon (M.S. Ortho)",
            rating = 4.8,
            nextAvailable = "Thursday, 10:00 AM",
            yearsExperience = 14,
            tokenNumber = "#32",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "gcc_dr_vishnuprasath",
            name = "Vishnuprasath",
            specialtyKey = "Orthopedics",
            specialtyDisplay = "Consultant Orthopedic & Trauma Surgeon (M.B.B.S., D.Ortho, DNB)",
            rating = 4.8,
            nextAvailable = "Thursday, 11:30 AM",
            yearsExperience = 11,
            tokenNumber = "#35",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "gcc_dr_anand",
            name = "Anand",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Surgical Oncologist (M.S., M.Ch Surgical Oncology)",
            rating = 4.9,
            nextAvailable = "Friday, 10:00 AM",
            yearsExperience = 16,
            tokenNumber = "#38",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "gcc_dr_balamurugan",
            name = "Balamurugan",
            specialtyKey = "General",
            specialtyDisplay = "Senior Consultant Neurologist & Stroke Specialist (M.D., D.M. Neuro)",
            rating = 4.9,
            nextAvailable = "Friday, 02:30 PM",
            yearsExperience = 20,
            tokenNumber = "#40",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "gcc_dr_rammohan",
            name = "Rammohan",
            specialtyKey = "Dentistry",
            specialtyDisplay = "Oro-Faciomaxillary & Craniofacial Surgeon (M.D.S. Maxillofacial)",
            rating = 4.9,
            nextAvailable = "Friday, 04:00 PM",
            yearsExperience = 17,
            tokenNumber = "#43",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "gcc_dr_ganesh_kumar",
            name = "Ganesh Kumar",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Plastic & Cosmetic Surgeon (M.S., M.Ch Plastic Surgery)",
            rating = 4.9,
            nextAvailable = "Saturday, 10:00 AM",
            yearsExperience = 15,
            tokenNumber = "#46",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "gcc_dr_arun",
            name = "Arun",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Pulmonologist & Respiratory Care (M.D. Chest & TB)",
            rating = 4.8,
            nextAvailable = "Saturday, 11:30 AM",
            yearsExperience = 13,
            tokenNumber = "#48",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "gcc_dr_vinoth",
            name = "Vinoth",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Rheumatologist & Clinical Immunologist (M.D., D.M.)",
            rating = 4.9,
            nextAvailable = "Saturday, 02:30 PM",
            yearsExperience = 14,
            tokenNumber = "#50",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "gcc_dr_sathyan",
            name = "Sathyan",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Urologist, Andrologist & Renal Transplant (M.S., M.Ch)",
            rating = 4.9,
            nextAvailable = "Saturday, 04:00 PM",
            yearsExperience = 17,
            tokenNumber = "#52",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "gcc_dr_ramanathan",
            name = "Ramanathan",
            specialtyKey = "General",
            specialtyDisplay = "Senior Consultant Anesthesiologist & Critical Care (M.D. Anaesthesia)",
            rating = 4.9,
            nextAvailable = "Today, 06:00 PM",
            yearsExperience = 23,
            tokenNumber = "#55",
            titlePrefix = "Dr."
        ),

        // ==========================================
        // 10. Bajaj Finserv Health Dharmapuri Directory (bajajfinservhealth.in/hospitals/dharmapuri)
        // ==========================================
        Doctor(
            id = "bfh_dr_i_senthilkumar",
            name = "I. Senthilkumar",
            specialtyKey = "General",
            specialtyDisplay = "Senior Consultant General & Laparoscopic Surgeon (M.S. General Surgery)",
            rating = 4.9,
            nextAvailable = "Today, 10:00 AM",
            yearsExperience = 21,
            tokenNumber = "#04",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "bfh_dr_i_nithya",
            name = "I. Nithya",
            specialtyKey = "General",
            specialtyDisplay = "Senior Consultant Obstetrician & Gynaecologist (M.D., D.G.O.)",
            rating = 5.0,
            nextAvailable = "Today, 11:30 AM",
            yearsExperience = 19,
            tokenNumber = "#08",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "bfh_dr_k_nithya",
            name = "K. Nithya",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Obstetrician, Gynaecologist & Fertility Specialist (M.S. OBG)",
            rating = 4.9,
            nextAvailable = "Today, 03:00 PM",
            yearsExperience = 15,
            tokenNumber = "#12",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "bfh_dr_r_siva",
            name = "R. Siva",
            specialtyKey = "ENT",
            specialtyDisplay = "Consultant ENT & Head Neck Surgeon (M.S. ENT)",
            rating = 4.8,
            nextAvailable = "Today, 05:00 PM",
            yearsExperience = 14,
            tokenNumber = "#17",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "bfh_dr_b_gopalakrishnan",
            name = "B. Gopalakrishnan",
            specialtyKey = "General",
            specialtyDisplay = "Senior Consultant Physician & Diabetologist (M.B.B.S., M.D. Gen Med)",
            rating = 4.9,
            nextAvailable = "Tomorrow, 10:00 AM",
            yearsExperience = 23,
            tokenNumber = "#22",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "bfh_dr_r_geetha",
            name = "R. Geetha",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Obstetrician & Infertility Specialist (M.B.B.S., DGO, DRM)",
            rating = 4.9,
            nextAvailable = "Tomorrow, 11:30 AM",
            yearsExperience = 16,
            tokenNumber = "#25",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "bfh_dr_n_balaji",
            name = "N. Balaji",
            specialtyKey = "Pediatrics",
            specialtyDisplay = "Consultant Pediatrician & Neonatologist (M.D. Paediatrics)",
            rating = 4.9,
            nextAvailable = "Tomorrow, 03:30 PM",
            yearsExperience = 17,
            tokenNumber = "#28",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "bfh_dr_k_v_jayachandran",
            name = "K.V. Jayachandran",
            specialtyKey = "General",
            specialtyDisplay = "Senior Consultant Physician & Cardiologist (M.D. Gen Med, PGDCC)",
            rating = 4.9,
            nextAvailable = "Wednesday, 10:00 AM",
            yearsExperience = 24,
            tokenNumber = "#31",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "bfh_dr_s_ragavendran",
            name = "S. Ragavendran",
            specialtyKey = "General",
            specialtyDisplay = "Consultant General & GI Surgeon (M.S. General Surgery, FIAGES)",
            rating = 4.8,
            nextAvailable = "Wednesday, 11:30 AM",
            yearsExperience = 18,
            tokenNumber = "#36",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "bfh_dr_k_ravindran",
            name = "K. Ravindran",
            specialtyKey = "General",
            specialtyDisplay = "Senior General Physician & Critical Care (M.B.B.S., M.D. Gen Med)",
            rating = 4.9,
            nextAvailable = "Wednesday, 03:00 PM",
            yearsExperience = 20,
            tokenNumber = "#39",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "bfh_dr_m_jayanthi",
            name = "M. Jayanthi",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Gynaecologist & Fetal Medicine (M.S. OBG)",
            rating = 4.9,
            nextAvailable = "Thursday, 10:30 AM",
            yearsExperience = 14,
            tokenNumber = "#42",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "bfh_dr_p_rajendran",
            name = "P. Rajendran",
            specialtyKey = "General",
            specialtyDisplay = "Senior Consultant Physician & Diabetologist (M.D. Gen Med)",
            rating = 5.0,
            nextAvailable = "Thursday, 11:30 AM",
            yearsExperience = 26,
            tokenNumber = "#45",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "bfh_dr_r_malathi",
            name = "R. Malathi",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Obstetrician & Women's Health (M.B.B.S., D.G.O.)",
            rating = 4.8,
            nextAvailable = "Thursday, 03:00 PM",
            yearsExperience = 18,
            tokenNumber = "#49",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "bfh_dr_d_nethaji_velan",
            name = "D. Nethaji Velan",
            specialtyKey = "Orthopedics",
            specialtyDisplay = "Senior Orthopedic & Joint Replacement Surgeon (M.S. Ortho, Fellowship Arthroplasty)",
            rating = 5.0,
            nextAvailable = "Friday, 10:00 AM",
            yearsExperience = 19,
            tokenNumber = "#51",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "bfh_dr_n_vimal",
            name = "N. Vimal",
            specialtyKey = "Orthopedics",
            specialtyDisplay = "Consultant Arthroscopy & Sports Injury Specialist (M.B.B.S., D.Ortho, DNB)",
            rating = 4.8,
            nextAvailable = "Friday, 11:30 AM",
            yearsExperience = 13,
            tokenNumber = "#54",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "bfh_dr_s_sundaramoorthy",
            name = "S. Sundaramoorthy",
            specialtyKey = "General",
            specialtyDisplay = "Chief Eye Surgeon & Phaco Cataract Specialist (M.S. Ophthalmology)",
            rating = 4.9,
            nextAvailable = "Friday, 02:30 PM",
            yearsExperience = 22,
            tokenNumber = "#57",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "bfh_dr_s_anitha",
            name = "S. Anitha",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Cornea & Refractive Surgeon (M.S. Ophthalmology, FICO)",
            rating = 4.9,
            nextAvailable = "Saturday, 10:00 AM",
            yearsExperience = 15,
            tokenNumber = "#60",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "bfh_dr_k_senthilvel",
            name = "K. Senthilvel",
            specialtyKey = "General",
            specialtyDisplay = "Senior Consultant Physician & Pulmonologist (M.D. Gen Med, DTCD)",
            rating = 4.9,
            nextAvailable = "Saturday, 11:30 AM",
            yearsExperience = 20,
            tokenNumber = "#63",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "bfh_dr_s_jayasree",
            name = "S. Jayasree",
            specialtyKey = "Dermatology",
            specialtyDisplay = "Consultant Dermatologist & Cosmetologist (M.D. Dermatology, DNB)",
            rating = 4.9,
            nextAvailable = "Saturday, 03:00 PM",
            yearsExperience = 14,
            tokenNumber = "#66",
            titlePrefix = "Dr."
        ),

        // ==========================================
        // 11. CMC Vellore Doctors (cmch-vellore.edu)
        // ==========================================
        Doctor(
            id = "cmc_dr_vikram_mathews",
            name = "Vikram Mathews",
            specialtyKey = "General",
            specialtyDisplay = "Director & Senior Professor, Department of Haematology (M.D., D.M. Clinical Haematology)",
            rating = 5.0,
            nextAvailable = "Today, 09:30 AM",
            yearsExperience = 28,
            tokenNumber = "#01",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "cmc_dr_anand_zachariah",
            name = "Anand Zachariah",
            specialtyKey = "General",
            specialtyDisplay = "Professor & Head, Department of Internal Medicine (M.D. Gen Med, FRCP)",
            rating = 5.0,
            nextAvailable = "Today, 11:00 AM",
            yearsExperience = 26,
            tokenNumber = "#05",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "cmc_dr_nihal_thomas",
            name = "Nihal Thomas",
            specialtyKey = "General",
            specialtyDisplay = "Senior Professor & Head, Endocrinology, Diabetes & Metabolism (M.D., MNAMS, DNB, FRACP, FRCP)",
            rating = 5.0,
            nextAvailable = "Today, 02:30 PM",
            yearsExperience = 27,
            tokenNumber = "#09",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "cmc_dr_vinoi_george_david",
            name = "Vinoi George David",
            specialtyKey = "General",
            specialtyDisplay = "Professor & Head, Department of Nephrology & Renal Care (M.D., D.M. Nephrology)",
            rating = 4.9,
            nextAvailable = "Tomorrow, 10:00 AM",
            yearsExperience = 21,
            tokenNumber = "#14",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "cmc_dr_oommen_k_george",
            name = "Oommen K. George",
            specialtyKey = "Cardiology",
            specialtyDisplay = "Professor & Head, Department of Cardiology & Cardiac Sciences (M.D., D.M. Cardiology)",
            rating = 5.0,
            nextAvailable = "Tomorrow, 11:30 AM",
            yearsExperience = 25,
            tokenNumber = "#18",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "cmc_dr_pradeep_mathew",
            name = "Pradeep Mathew Poonnoose",
            specialtyKey = "Orthopedics",
            specialtyDisplay = "Professor & Head, Department of Orthopaedics & Joint Reconstruction (M.S. Ortho, MCh Orth)",
            rating = 4.9,
            nextAvailable = "Wednesday, 10:30 AM",
            yearsExperience = 24,
            tokenNumber = "#23",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "cmc_dr_ashish_singh",
            name = "Ashish Singh",
            specialtyKey = "General",
            specialtyDisplay = "Professor & Head, Department of Medical Oncology (M.D., D.M. Medical Oncology)",
            rating = 4.9,
            nextAvailable = "Wednesday, 03:00 PM",
            yearsExperience = 20,
            tokenNumber = "#27",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "cmc_dr_joseph_a_j",
            name = "Joseph A. J.",
            specialtyKey = "General",
            specialtyDisplay = "Professor & Head, Department of Medical Gastroenterology & Hepatology (M.D., D.M. Gastro)",
            rating = 4.9,
            nextAvailable = "Thursday, 11:00 AM",
            yearsExperience = 22,
            tokenNumber = "#31",
            titlePrefix = "Dr."
        ),

        // ==========================================
        // 12. Naruvi Hospitals Doctors (naruvihospitals.com)
        // ==========================================
        Doctor(
            id = "naruvi_dr_nitin_kekre",
            name = "Nitin Kekre",
            specialtyKey = "General",
            specialtyDisplay = "Senior Urology Surgeon & Medical Director (M.S., M.Ch Urology, DNB)",
            rating = 5.0,
            nextAvailable = "Today, 10:00 AM",
            yearsExperience = 29,
            tokenNumber = "#03",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "naruvi_dr_ashok_chacko",
            name = "Ashok Chacko",
            specialtyKey = "General",
            specialtyDisplay = "Chief Gastroenterologist & Liver Specialist (M.D., D.M. Gastroenterology, FRCP)",
            rating = 5.0,
            nextAvailable = "Today, 11:30 AM",
            yearsExperience = 32,
            tokenNumber = "#07",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "naruvi_dr_aravindan_nair",
            name = "Aravindan Nair",
            specialtyKey = "General",
            specialtyDisplay = "Senior Consultant General, Breast & Endocrine Surgeon (M.S. General Surgery, FRCS)",
            rating = 4.9,
            nextAvailable = "Today, 03:30 PM",
            yearsExperience = 26,
            tokenNumber = "#11",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "naruvi_dr_jacob_jose",
            name = "Jacob Jose",
            specialtyKey = "Cardiology",
            specialtyDisplay = "Chief Interventional Cardiologist (M.D., D.M. Cardiology, FACC)",
            rating = 5.0,
            nextAvailable = "Tomorrow, 10:30 AM",
            yearsExperience = 25,
            tokenNumber = "#16",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "naruvi_dr_vernon_neville_lee",
            name = "Vernon Neville Lee",
            specialtyKey = "Orthopedics",
            specialtyDisplay = "Senior Consultant Orthopaedic & Joint Replacement Surgeon (M.S. Ortho, DNB)",
            rating = 4.9,
            nextAvailable = "Tomorrow, 02:00 PM",
            yearsExperience = 23,
            tokenNumber = "#20",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "naruvi_dr_mathew_j_chandy",
            name = "Mathew J. Chandy",
            specialtyKey = "General",
            specialtyDisplay = "Senior Consultant Neurosurgeon & Spine Care Specialist (M.S., M.Ch Neuro Surgery)",
            rating = 5.0,
            nextAvailable = "Wednesday, 10:00 AM",
            yearsExperience = 30,
            tokenNumber = "#24",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "naruvi_dr_aruna_kekre",
            name = "Aruna Kekre",
            specialtyKey = "General",
            specialtyDisplay = "Senior Consultant Obstetrician & Gynaecologist (M.D., D.G.O., FRCOG)",
            rating = 4.9,
            nextAvailable = "Wednesday, 02:30 PM",
            yearsExperience = 27,
            tokenNumber = "#28",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "naruvi_dr_vinayak_shukla",
            name = "Vinayak Shukla",
            specialtyKey = "Cardiology",
            specialtyDisplay = "Chief Cardiothoracic & Vascular Surgeon (M.S., M.Ch CTVS, FIACS)",
            rating = 4.9,
            nextAvailable = "Thursday, 10:00 AM",
            yearsExperience = 21,
            tokenNumber = "#33",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "naruvi_dr_sai_krishna_chaitanya",
            name = "Sai Krishna Chaitanya P.",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Endocrinologist & Thyroid Specialist (M.D., D.M. Endocrinology)",
            rating = 4.9,
            nextAvailable = "Thursday, 03:00 PM",
            yearsExperience = 15,
            tokenNumber = "#37",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "naruvi_dr_d_backiaraj",
            name = "D. Backiaraj",
            specialtyKey = "Orthopedics",
            specialtyDisplay = "Senior Consultant Spine & Joint Replacement Surgeon (M.S. Ortho, Fellowship Spine)",
            rating = 4.9,
            nextAvailable = "Friday, 11:30 AM",
            yearsExperience = 18,
            tokenNumber = "#41",
            titlePrefix = "Dr."
        ),

        // ==========================================
        // 13. Sri Narayani Hospital & Research Centre / Department of Dietetics (narayanihospital.com)
        // ==========================================
        Doctor(
            id = "snhrc_dr_r_nandhini",
            name = "R. Nandhini",
            specialtyKey = "General",
            specialtyDisplay = "Chief Clinical Dietitian & Therapeutic Nutrition Specialist (M.Sc. Clinical Nutrition, RD)",
            rating = 5.0,
            nextAvailable = "Today, 10:00 AM",
            yearsExperience = 16,
            tokenNumber = "#02",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "snhrc_dr_ravali_hickson",
            name = "Ravali Hickson Lankapalli",
            specialtyKey = "General",
            specialtyDisplay = "Consultant General Physician & Critical Care (M.B.B.S., M.D. Gen Med)",
            rating = 4.9,
            nextAvailable = "Today, 11:30 AM",
            yearsExperience = 14,
            tokenNumber = "#06",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "snhrc_dr_s_bala_murugan",
            name = "S. Bala Murugan",
            specialtyKey = "General",
            specialtyDisplay = "Consultant General Medicine & Diabetologist (M.B.B.S., M.D. Internal Medicine)",
            rating = 4.8,
            nextAvailable = "Today, 03:00 PM",
            yearsExperience = 17,
            tokenNumber = "#10",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "snhrc_dr_d_sakthivelan",
            name = "D. Sakthivelan",
            specialtyKey = "General",
            specialtyDisplay = "Senior General & Laparoscopic Surgeon (M.S. General Surgery, FMAS)",
            rating = 4.9,
            nextAvailable = "Tomorrow, 10:00 AM",
            yearsExperience = 20,
            tokenNumber = "#15",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "snhrc_dr_nisha_kalaiarasan",
            name = "Nisha Kalaiarasan",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Obstetrician & Gynaecologist (M.S. OBG, DNB)",
            rating = 4.9,
            nextAvailable = "Tomorrow, 11:30 AM",
            yearsExperience = 15,
            tokenNumber = "#19",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "snhrc_dr_k_angappan",
            name = "K. Angappan",
            specialtyKey = "Pediatrics",
            specialtyDisplay = "Senior Consultant Paediatrician & Neonatologist (M.D. Paediatrics, DCH)",
            rating = 5.0,
            nextAvailable = "Tomorrow, 03:00 PM",
            yearsExperience = 22,
            tokenNumber = "#22",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "snhrc_dr_e_r_srinivas",
            name = "E. R. Srinivas",
            specialtyKey = "General",
            specialtyDisplay = "Senior Consultant Plastic, Cosmetic & Reconstructive Surgeon (M.S., M.Ch Plastic Surgery)",
            rating = 4.9,
            nextAvailable = "Wednesday, 10:30 AM",
            yearsExperience = 23,
            tokenNumber = "#26",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "snhrc_dr_k_sidharthan",
            name = "K. Sidharthan",
            specialtyKey = "Cardiology",
            specialtyDisplay = "Consultant Interventional Cardiologist (M.D., D.M. Cardiology)",
            rating = 4.9,
            nextAvailable = "Wednesday, 02:00 PM",
            yearsExperience = 18,
            tokenNumber = "#30",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "snhrc_dr_balasubramanian",
            name = "Balasubramanian",
            specialtyKey = "ENT",
            specialtyDisplay = "Consultant ENT, Head & Neck Surgeon (M.S. ENT, DLO)",
            rating = 4.8,
            nextAvailable = "Thursday, 10:30 AM",
            yearsExperience = 16,
            tokenNumber = "#34",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "snhrc_dr_srinivasa_prasad",
            name = "Srinivasa Prasad",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Nephrologist & Renal Transplant Physician (M.D., D.M. Nephrology)",
            rating = 4.9,
            nextAvailable = "Thursday, 02:30 PM",
            yearsExperience = 17,
            tokenNumber = "#38",
            titlePrefix = "Dr."
        ),
        Doctor(
            id = "snhrc_dr_v_s_ajay_chandrasekar",
            name = "V. S. Ajay Chandrasekar",
            specialtyKey = "General",
            specialtyDisplay = "Consultant Surgical Oncologist & Laparoscopic Cancer Surgeon (M.S., M.Ch Surgical Oncology)",
            rating = 4.9,
            nextAvailable = "Friday, 10:00 AM",
            yearsExperience = 15,
            tokenNumber = "#43",
            titlePrefix = "Dr."
        )
    )
}

data class SymptomAssessment(
    val id: String,
    val date: String,
    val symptoms: String,
    val analysis: String
)

data class Appointment(
    val id: String,
    val doctor: Doctor,
    val date: String,
    val monthShort: String,
    val dayOfMonth: String,
    val timeSlot: String,
    val notes: String = ""
)
