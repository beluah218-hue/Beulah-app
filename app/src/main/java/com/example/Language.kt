package com.example

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class AppLanguage {
    ENGLISH,
    TAMIL
}

object LanguageManager {
    var currentLanguage by mutableStateOf(AppLanguage.ENGLISH)

    private val translations = mapOf(
        "Welcome Back" to mapOf(
            AppLanguage.ENGLISH to "Welcome Back",
            AppLanguage.TAMIL to "மீண்டும் வருக"
        ),
        "Your health journey continues here. Securely access your medical profile." to mapOf(
            AppLanguage.ENGLISH to "Your health journey continues here. Securely access your medical profile.",
            AppLanguage.TAMIL to "உங்கள் ஆரோக்கியப் பயணம் இங்கே தொடர்கிறது. உங்கள் மருத்துவ விவரங்களை பாதுகாப்பாக அணுகவும்."
        ),
        "Phone Number" to mapOf(
            AppLanguage.ENGLISH to "Phone Number",
            AppLanguage.TAMIL to "தொலைபேசி எண்"
        ),
        "Email Address" to mapOf(
            AppLanguage.ENGLISH to "Email Address",
            AppLanguage.TAMIL to "மின்னஞ்சல் முகவரி"
        ),
        "Mobile Number" to mapOf(
            AppLanguage.ENGLISH to "Mobile Number",
            AppLanguage.TAMIL to "கைபேசி எண்"
        ),
        "Enter 10-digit number" to mapOf(
            AppLanguage.ENGLISH to "Enter 10-digit number",
            AppLanguage.TAMIL to "10-இலக்க எண்ணை உள்ளிடவும்"
        ),
        "Get OTP" to mapOf(
            AppLanguage.ENGLISH to "Get OTP",
            AppLanguage.TAMIL to "OTP பெறுக"
        ),
        "OR" to mapOf(
            AppLanguage.ENGLISH to "OR",
            AppLanguage.TAMIL to "அல்லது"
        ),
        "Don't have an account? " to mapOf(
            AppLanguage.ENGLISH to "Don't have an account? ",
            AppLanguage.TAMIL to "கணக்கு இல்லையா? "
        ),
        "Sign up for Life Care" to mapOf(
            AppLanguage.ENGLISH to "Sign up for Life Care",
            AppLanguage.TAMIL to "Life Care-க்கு பதிவு செய்க"
        ),
        "Secure Data" to mapOf(
            AppLanguage.ENGLISH to "Secure Data",
            AppLanguage.TAMIL to "பாதுகாப்பான தரவு"
        ),
        "Privacy First" to mapOf(
            AppLanguage.ENGLISH to "Privacy First",
            AppLanguage.TAMIL to "தனியுரிமைக்கு முன்னுரிமை"
        ),
        "End-to-end" to mapOf(
            AppLanguage.ENGLISH to "End-to-end",
            AppLanguage.TAMIL to "முழுமையான பாதுகாப்பு"
        ),
        "Hello, Karthik" to mapOf(
            AppLanguage.ENGLISH to "Hello, Karthik",
            AppLanguage.TAMIL to "வணக்கம், கார்த்திக்"
        ),
        "Hello" to mapOf(
            AppLanguage.ENGLISH to "Hello",
            AppLanguage.TAMIL to "வணக்கம்"
        ),
        "Full Name" to mapOf(
            AppLanguage.ENGLISH to "Full Name",
            AppLanguage.TAMIL to "முழு பெயர்"
        ),
        "Enter your full name" to mapOf(
            AppLanguage.ENGLISH to "Enter your full name",
            AppLanguage.TAMIL to "உங்கள் முழு பெயரை உள்ளிடவும்"
        ),
        "Stay updated with your health and wellness today." to mapOf(
            AppLanguage.ENGLISH to "Stay updated with your health and wellness today.",
            AppLanguage.TAMIL to "இன்று உங்கள் ஆரோக்கியம் மற்றும் நல்வாழ்வைப் பற்றி உடனுக்குடன் தெரிந்துகொள்ளுங்கள்."
        ),
        "Doctor of the Day" to mapOf(
            AppLanguage.ENGLISH to "Doctor of the Day",
            AppLanguage.TAMIL to "இன்றைய சிறந்த மருத்துவர்"
        ),
        "Consult Now" to mapOf(
            AppLanguage.ENGLISH to "Consult Now",
            AppLanguage.TAMIL to "இப்போது ஆலோசனை செய்க"
        ),
        "Book Appointment" to mapOf(
            AppLanguage.ENGLISH to "Book Appointment",
            AppLanguage.TAMIL to "முன்பதிவு செய்க"
        ),
        "AI Symptom Checker" to mapOf(
            AppLanguage.ENGLISH to "AI Symptom Checker",
            AppLanguage.TAMIL to "AI அறிகுறி கண்டறிதல்"
        ),
        "Health Records" to mapOf(
            AppLanguage.ENGLISH to "Health Records",
            AppLanguage.TAMIL to "சுகாதார பதிவுகள்"
        ),
        "Emergency SOS" to mapOf(
            AppLanguage.ENGLISH to "Emergency SOS",
            AppLanguage.TAMIL to "அவசரகால உதவி"
        ),
        "Upcoming Visit" to mapOf(
            AppLanguage.ENGLISH to "Upcoming Visit",
            AppLanguage.TAMIL to "வரவிருக்கும் சந்திப்பு"
        ),
        "View All" to mapOf(
            AppLanguage.ENGLISH to "View All",
            AppLanguage.TAMIL to "அனைத்தையும் காட்டு"
        ),
        "Quick Vitals" to mapOf(
            AppLanguage.ENGLISH to "Quick Vitals",
            AppLanguage.TAMIL to "உடல் நிலை அளவுகள்"
        ),
        "Heart Rate" to mapOf(
            AppLanguage.ENGLISH to "Heart Rate",
            AppLanguage.TAMIL to "இதய துடிப்பு விகிதம்"
        ),
        "BP" to mapOf(
            AppLanguage.ENGLISH to "BP",
            AppLanguage.TAMIL to "இரத்த அழுத்தம்"
        ),
        "Steps" to mapOf(
            AppLanguage.ENGLISH to "Steps",
            AppLanguage.TAMIL to "நடந்த படிகள்"
        ),
        "Home" to mapOf(
            AppLanguage.ENGLISH to "Home",
            AppLanguage.TAMIL to "முகப்பு"
        ),
        "Appointments" to mapOf(
            AppLanguage.ENGLISH to "Appointments",
            AppLanguage.TAMIL to "சந்திப்புகள்"
        ),
        "Records" to mapOf(
            AppLanguage.ENGLISH to "Records",
            AppLanguage.TAMIL to "பதிவுகள்"
        ),
        "Profile" to mapOf(
            AppLanguage.ENGLISH to "Profile",
            AppLanguage.TAMIL to "சுயவிவரம்"
        ),
        "Dashboard" to mapOf(
            AppLanguage.ENGLISH to "Dashboard",
            AppLanguage.TAMIL to "டேஷ்போர்டு"
        ),
        "Recent AI Assessments" to mapOf(
            AppLanguage.ENGLISH to "Recent AI Assessments",
            AppLanguage.TAMIL to "சமீபத்திய AI சோதனைகள்"
        ),
        "Upcoming Appointments" to mapOf(
            AppLanguage.ENGLISH to "Upcoming Appointments",
            AppLanguage.TAMIL to "வரவிருக்கும் சந்திப்புகள்"
        ),
        "View Assessment Summary" to mapOf(
            AppLanguage.ENGLISH to "View Assessment Summary",
            AppLanguage.TAMIL to "மதிப்பீட்டு சுருக்கத்தைக் காண்க"
        ),
        "No upcoming appointments" to mapOf(
            AppLanguage.ENGLISH to "No upcoming appointments",
            AppLanguage.TAMIL to "வரவிருக்கும் சந்திப்புகள் எதுவும் இல்லை"
        ),
        "No recent assessments" to mapOf(
            AppLanguage.ENGLISH to "No recent assessments",
            AppLanguage.TAMIL to "சமீபத்திய மதிப்பீடுகள் எதுவும் இல்லை"
        ),
        "Assessment Date" to mapOf(
            AppLanguage.ENGLISH to "Assessment Date",
            AppLanguage.TAMIL to "பரிசோதனை தேதி"
        ),
        "Symptom Details" to mapOf(
            AppLanguage.ENGLISH to "Symptom Details",
            AppLanguage.TAMIL to "அறிகுறி விவரங்கள்"
        ),
        "AI Recommendation" to mapOf(
            AppLanguage.ENGLISH to "AI Recommendation",
            AppLanguage.TAMIL to "AI பரிந்துரை"
        ),
        "Consult Doctor" to mapOf(
            AppLanguage.ENGLISH to "Consult Doctor",
            AppLanguage.TAMIL to "மருத்துவரை அணுகவும்"
        ),
        "Start New Assessment" to mapOf(
            AppLanguage.ENGLISH to "Start New Assessment",
            AppLanguage.TAMIL to "புதிய சோதனையைத் தொடங்கு"
        ),
        "Checker" to mapOf(
            AppLanguage.ENGLISH to "Checker",
            AppLanguage.TAMIL to "கண்டறிவி"
        ),
        "Logout" to mapOf(
            AppLanguage.ENGLISH to "Logout",
            AppLanguage.TAMIL to "வெளியேறு"
        ),
        "Account Info" to mapOf(
            AppLanguage.ENGLISH to "Account Info",
            AppLanguage.TAMIL to "கணக்கு தகவல்"
        ),
        "Preferences" to mapOf(
            AppLanguage.ENGLISH to "Preferences",
            AppLanguage.TAMIL to "விருப்பத்தேர்வுகள்"
        ),
        "Language" to mapOf(
            AppLanguage.ENGLISH to "Language",
            AppLanguage.TAMIL to "மொழி"
        ),
        "App Version" to mapOf(
            AppLanguage.ENGLISH to "App Version",
            AppLanguage.TAMIL to "பயன்பாட்டு பதிப்பு"
        ),
        "Email" to mapOf(
            AppLanguage.ENGLISH to "Email",
            AppLanguage.TAMIL to "மின்னஞ்சல்"
        ),
        "Phone" to mapOf(
            AppLanguage.ENGLISH to "Phone",
            AppLanguage.TAMIL to "தொலைபேசி"
        ),
        "Gender" to mapOf(
            AppLanguage.ENGLISH to "Gender",
            AppLanguage.TAMIL to "பாலினம்"
        ),
        "Age" to mapOf(
            AppLanguage.ENGLISH to "Age",
            AppLanguage.TAMIL to "வயது"
        ),
        "Blood Group" to mapOf(
            AppLanguage.ENGLISH to "Blood Group",
            AppLanguage.TAMIL to "இரத்த வகை"
        ),
        "Find Your Specialist" to mapOf(
            AppLanguage.ENGLISH to "Find Your Specialist",
            AppLanguage.TAMIL to "உங்கள் நிபுணரைக் கண்டறியவும்"
        ),
        "Filters" to mapOf(
            AppLanguage.ENGLISH to "Filters",
            AppLanguage.TAMIL to "வடிகட்டிகள்"
        ),
        "Hide Filters" to mapOf(
            AppLanguage.ENGLISH to "Hide Filters",
            AppLanguage.TAMIL to "வடிகட்டிகளை மறை"
        ),
        "Show Filters" to mapOf(
            AppLanguage.ENGLISH to "Show Filters",
            AppLanguage.TAMIL to "வடிகட்டிகளைக் காட்டு"
        ),
        "Availability" to mapOf(
            AppLanguage.ENGLISH to "Availability",
            AppLanguage.TAMIL to "கிடைக்கும் தன்மை"
        ),
        "Any Time" to mapOf(
            AppLanguage.ENGLISH to "Any Time",
            AppLanguage.TAMIL to "எந்த நேரமும்"
        ),
        "Available Today" to mapOf(
            AppLanguage.ENGLISH to "Available Today",
            AppLanguage.TAMIL to "இன்று கிடைக்கும்"
        ),
        "Available Tomorrow" to mapOf(
            AppLanguage.ENGLISH to "Available Tomorrow",
            AppLanguage.TAMIL to "நாளை கிடைக்கும்"
        ),
        "Minimum Rating" to mapOf(
            AppLanguage.ENGLISH to "Minimum Rating",
            AppLanguage.TAMIL to "குறைந்தபட்ச மதிப்பீடு"
        ),
        "Any Rating" to mapOf(
            AppLanguage.ENGLISH to "Any Rating",
            AppLanguage.TAMIL to "எந்த மதிப்பீடும்"
        ),
        "4.7+ Stars" to mapOf(
            AppLanguage.ENGLISH to "4.7+ Stars",
            AppLanguage.TAMIL to "4.7+ நட்சத்திரங்கள்"
        ),
        "4.8+ Stars" to mapOf(
            AppLanguage.ENGLISH to "4.8+ Stars",
            AppLanguage.TAMIL to "4.8+ நட்சத்திரங்கள்"
        ),
        "4.9+ Stars" to mapOf(
            AppLanguage.ENGLISH to "4.9+ Stars",
            AppLanguage.TAMIL to "4.9+ நட்சத்திரங்கள்"
        ),
        "No doctors found matching filters." to mapOf(
            AppLanguage.ENGLISH to "No doctors found matching filters.",
            AppLanguage.TAMIL to "வடிகட்டல்களுக்குப் பொருந்தும் மருத்துவர்கள் யாரும் இல்லை."
        ),
        "Search doctor or specialty..." to mapOf(
            AppLanguage.ENGLISH to "Search doctor or specialty...",
            AppLanguage.TAMIL to "மருத்துவர் அல்லது சிறப்பைக் கண்டறியவும்..."
        ),
        "Consultation Booking" to mapOf(
            AppLanguage.ENGLISH to "Consultation Booking",
            AppLanguage.TAMIL to "ஆலோசனை முன்பதிவு"
        ),
        "Select Date" to mapOf(
            AppLanguage.ENGLISH to "Select Date",
            AppLanguage.TAMIL to "தேதியைத் தேர்ந்தெடுக்கவும்"
        ),
        "Select Time Slot" to mapOf(
            AppLanguage.ENGLISH to "Select Time Slot",
            AppLanguage.TAMIL to "நேரத்தைத் தேர்ந்தெடுக்கவும்"
        ),
        "Notes (Optional)" to mapOf(
            AppLanguage.ENGLISH to "Notes (Optional)",
            AppLanguage.TAMIL to "குறிப்புகள் (விருப்பத்தேர்வு)"
        ),
        "Reason for visit (optional)..." to mapOf(
            AppLanguage.ENGLISH to "Reason for visit (optional)...",
            AppLanguage.TAMIL to "வருகைக்கான காரணம் (விருப்பத்தேர்வு)..."
        ),
        "Book Consultation" to mapOf(
            AppLanguage.ENGLISH to "Book Consultation",
            AppLanguage.TAMIL to "ஆலோசனையை முன்பதிவு செய்"
        ),
        "Please select a time slot to continue." to mapOf(
            AppLanguage.ENGLISH to "Please select a time slot to continue.",
            AppLanguage.TAMIL to "தொடர, நேரத்தைத் தேர்ந்தெடுக்கவும்."
        ),
        "Consultation Details" to mapOf(
            AppLanguage.ENGLISH to "Consultation Details",
            AppLanguage.TAMIL to "ஆலோசனை விவரங்கள்"
        ),
        "Expert care across 20+ specialized departments." to mapOf(
            AppLanguage.ENGLISH to "Expert care across 20+ specialized departments.",
            AppLanguage.TAMIL to "20-க்கும் மேற்பட்ட சிறப்புத் துறைகளில் நிபுணத்துவ சிகிச்சை."
        ),
        "All Doctors" to mapOf(
            AppLanguage.ENGLISH to "All Doctors",
            AppLanguage.TAMIL to "அனைத்து மருத்துவர்கள்"
        ),
        "Cardiology" to mapOf(
            AppLanguage.ENGLISH to "Cardiology",
            AppLanguage.TAMIL to "இதயவியல்"
        ),
        "General" to mapOf(
            AppLanguage.ENGLISH to "General",
            AppLanguage.TAMIL to "பொது மருத்துவம்"
        ),
        "Pediatrics" to mapOf(
            AppLanguage.ENGLISH to "Pediatrics",
            AppLanguage.TAMIL to "குழந்தை மருத்துவம்"
        ),
        "Orthopedics" to mapOf(
            AppLanguage.ENGLISH to "Orthopedics",
            AppLanguage.TAMIL to "எலும்பியல்"
        ),
        "ENT" to mapOf(
            AppLanguage.ENGLISH to "ENT",
            AppLanguage.TAMIL to "காது, மூக்கு, தொண்டை"
        ),
        "Dermatology" to mapOf(
            AppLanguage.ENGLISH to "Dermatology",
            AppLanguage.TAMIL to "தோல் மருத்துவம்"
        ),
        "Dentistry" to mapOf(
            AppLanguage.ENGLISH to "Dentistry",
            AppLanguage.TAMIL to "பல் மருத்துவம்"
        ),
        "Next: " to mapOf(
            AppLanguage.ENGLISH to "Next: ",
            AppLanguage.TAMIL to "அடுத்த சந்திப்பு: "
        ),
        "Booking Confirmed!" to mapOf(
            AppLanguage.ENGLISH to "Booking Confirmed!",
            AppLanguage.TAMIL to "முன்பதிவு உறுதிசெய்யப்பட்டது!"
        ),
        "Your appointment has been successfully scheduled. Please show the QR code at the reception upon arrival." to mapOf(
            AppLanguage.ENGLISH to "Your appointment has been successfully scheduled. Please show the QR code at the reception upon arrival.",
            AppLanguage.TAMIL to "உங்கள் சந்திப்பு வெற்றிகரமாக திட்டமிடப்பட்டுள்ளது. வருகையின் போது வரவேற்பறையில் QR குறியீட்டைக் காட்டவும்."
        ),
        "Date" to mapOf(
            AppLanguage.ENGLISH to "Date",
            AppLanguage.TAMIL to "தேதி"
        ),
        "Time Slot" to mapOf(
            AppLanguage.ENGLISH to "Time Slot",
            AppLanguage.TAMIL to "நேர ஒதுக்கீடு"
        ),
        "Location" to mapOf(
            AppLanguage.ENGLISH to "Location",
            AppLanguage.TAMIL to "இடம்"
        ),
        "Get Directions" to mapOf(
            AppLanguage.ENGLISH to "Get Directions",
            AppLanguage.TAMIL to "வழிசெலுத்தல் பெறுக"
        ),
        "Check-in QR Code" to mapOf(
            AppLanguage.ENGLISH to "Check-in QR Code",
            AppLanguage.TAMIL to "வருகைப்பதிவு QR குறியீடு"
        ),
        "Add to Calendar" to mapOf(
            AppLanguage.ENGLISH to "Add to Calendar",
            AppLanguage.TAMIL to "நாட்காட்டியில் சேர்க்க"
        ),
        "Share Details" to mapOf(
            AppLanguage.ENGLISH to "Share Details",
            AppLanguage.TAMIL to "விவரங்களைப் பகிர்க"
        ),
        "Back to Dashboard" to mapOf(
            AppLanguage.ENGLISH to "Back to Dashboard",
            AppLanguage.TAMIL to "முகப்புப் பலகைக்குத் திரும்புக"
        ),
        "Preparation Checklist" to mapOf(
            AppLanguage.ENGLISH to "Preparation Checklist",
            AppLanguage.TAMIL to "ஆயத்தப் பட்டியல்"
        ),
        "Bring your previous medical records." to mapOf(
            AppLanguage.ENGLISH to "Bring your previous medical records.",
            AppLanguage.TAMIL to "உங்கள் முந்தைய மருத்துவப் பதிவுகளைக் கொண்டு வரவும்."
        ),
        "Arrive 15 minutes before slot time." to mapOf(
            AppLanguage.ENGLISH to "Arrive 15 minutes before slot time.",
            AppLanguage.TAMIL to "நேர ஒதுக்கீட்டிற்கு 15 நிமிடங்களுக்கு முன் வரவும்."
        ),
        "Wear a comfortable mask." to mapOf(
            AppLanguage.ENGLISH to "Wear a comfortable mask.",
            AppLanguage.TAMIL to "வசதியான முகமூடியை அணியுங்கள்."
        ),
        "This is not a medical diagnosis. In case of a health emergency, please contact your local emergency services immediately." to mapOf(
            AppLanguage.ENGLISH to "This is not a medical diagnosis. In case of a health emergency, please contact your local emergency services immediately.",
            AppLanguage.TAMIL to "இது மருத்துவக் கண்டறிதல் அல்ல. அவசர மருத்துவ உதவிக்கு, உடனடியாக உங்கள் உள்ளூர் அவசரச் சேவைகளைத் தொடர்பு கொள்ளவும்."
        ),
        "Type your symptoms here..." to mapOf(
            AppLanguage.ENGLISH to "Type your symptoms here...",
            AppLanguage.TAMIL to "உங்கள் அறிகுறிகளை இங்கே தட்டச்சு செய்யவும்..."
        ),
        "Checker" to mapOf(
            AppLanguage.ENGLISH to "Checker",
            AppLanguage.TAMIL to "பரிசோதகர்"
        ),
        "Appts" to mapOf(
            AppLanguage.ENGLISH to "Appts",
            AppLanguage.TAMIL to "சந்திப்புகள்"
        ),
        "General Medicine" to mapOf(
            AppLanguage.ENGLISH to "General Medicine",
            AppLanguage.TAMIL to "பொது மருத்துவம்"
        ),
        "Senior Cardiologist" to mapOf(
            AppLanguage.ENGLISH to "Senior Cardiologist",
            AppLanguage.TAMIL to "மூத்த இதயவியல் நிபுணர்"
        ),
        "Senior Endocrinologist" to mapOf(
            AppLanguage.ENGLISH to "Senior Endocrinologist",
            AppLanguage.TAMIL to "மூத்த நாளமில்லாச் சுரப்பியியல் நிபுணர்"
        ),
        "General Cardiology" to mapOf(
            AppLanguage.ENGLISH to "General Cardiology",
            AppLanguage.TAMIL to "பொது இதயவியல்"
        ),
        "Children's Wing, 2nd Floor" to mapOf(
            AppLanguage.ENGLISH to "Children's Wing, 2nd Floor",
            AppLanguage.TAMIL to "குழந்தைகள் பிரிவு, 2வது தளம்"
        ),
        "Main Clinic, Block B" to mapOf(
            AppLanguage.ENGLISH to "Main Clinic, Block B",
            AppLanguage.TAMIL to "முதன்மை கிளினிக், பிளாக் பி"
        ),
        "Wellness Center, Room 102" to mapOf(
            AppLanguage.ENGLISH to "Wellness Center, Room 102",
            AppLanguage.TAMIL to "ஆரோக்கிய மையம், அறை 102"
        ),
        "Surgical Block, Unit 4" to mapOf(
            AppLanguage.ENGLISH to "Surgical Block, Unit 4",
            AppLanguage.TAMIL to "அறுவை சிகிச்சை பிரிவு, அலகு 4"
        ),
        "Outpatient Wing, Ground Floor" to mapOf(
            AppLanguage.ENGLISH to "Outpatient Wing, Ground Floor",
            AppLanguage.TAMIL to "வெளிநோயாளிகள் பிரிவு, தரைத்தளம்"
        ),
        "Specialty Block, Room 305" to mapOf(
            AppLanguage.ENGLISH to "Specialty Block, Room 305",
            AppLanguage.TAMIL to "சிறப்பு பிரிவு, அறை 305"
        ),
        "City Health Hub, Block B, Floor 4" to mapOf(
            AppLanguage.ENGLISH to "City Health Hub, Block B, Floor 4",
            AppLanguage.TAMIL to "நகர சுகாதார மையம், பிளாக் பி, தளம் 4"
        ),
        "Apollo Clinic, Tirupattur H.O, Vellore" to mapOf(
            AppLanguage.ENGLISH to "Apollo Clinic, Tirupattur H.O, Vellore",
            AppLanguage.TAMIL to "அப்போலோ கிளினிக், திருப்பத்தூர் ஹெச்.ஓ, வேலூர்"
        ),
        "Fever" to mapOf(
            AppLanguage.ENGLISH to "Fever",
            AppLanguage.TAMIL to "காய்ச்சல்"
        ),
        "Cough" to mapOf(
            AppLanguage.ENGLISH to "Cough",
            AppLanguage.TAMIL to "இருமல்"
        ),
        "Headache" to mapOf(
            AppLanguage.ENGLISH to "Headache",
            AppLanguage.TAMIL to "தலைவலி"
        ),
        "Stomach Pain" to mapOf(
            AppLanguage.ENGLISH to "Stomach Pain",
            AppLanguage.TAMIL to "வயிற்று வலி"
        ),
        "Daily Health Tips" to mapOf(
            AppLanguage.ENGLISH to "Daily Health Tips",
            AppLanguage.TAMIL to "தினசரி சுகாதார குறிப்புகள்"
        ),
        "Enable or disable notifications for daily tips." to mapOf(
            AppLanguage.ENGLISH to "Enable or disable notifications for daily tips.",
            AppLanguage.TAMIL to "தினசரி குறிப்புகளுக்கான அறிவிப்புகளை இயக்கவும் அல்லது முடக்கவும்."
        ),
        "Consultation Reminders" to mapOf(
            AppLanguage.ENGLISH to "Consultation Reminders",
            AppLanguage.TAMIL to "ஆலோசனை நினைவூட்டல்கள்"
        ),
        "Receive reminders about upcoming appointments." to mapOf(
            AppLanguage.ENGLISH to "Receive reminders about upcoming appointments.",
            AppLanguage.TAMIL to "வரவிருக்கும் சந்திப்புகள் பற்றிய நினைவூட்டல்களைப் பெறுங்கள்."
        ),
        "Loaded appointments from Firestore!" to mapOf(
            AppLanguage.ENGLISH to "Loaded appointments from Firestore!",
            AppLanguage.TAMIL to "ஃபோர்ஸ்டோரிலிருந்து சந்திப்புகள் ஏற்றப்பட்டன!"
        ),
        "Appointment stored in Firestore!" to mapOf(
            AppLanguage.ENGLISH to "Appointment stored in Firestore!",
            AppLanguage.TAMIL to "சந்திப்பு பயர்ஸ்டோரில் சேமிக்கப்பட்டது!"
        ),
        "Saved locally (Firestore Offline)" to mapOf(
            AppLanguage.ENGLISH to "Saved locally (Firestore Offline)",
            AppLanguage.TAMIL to "உள்ளூரில் சேமிக்கப்பட்டது (பயர்ஸ்டோர் ஆஃப்லைன்)"
        ),
        "Cloud Sync Integration" to mapOf(
            AppLanguage.ENGLISH to "Cloud Sync Integration",
            AppLanguage.TAMIL to "கிளவுட் ஒத்திசைவு ஒருங்கிணைப்பு"
        ),
        "Appointments are stored on Firebase Firestore. To connect your personal production database, ensure 'google-services.json' is added in your app's module folder." to mapOf(
            AppLanguage.ENGLISH to "Appointments are stored on Firebase Firestore. To connect your personal production database, ensure 'google-services.json' is added in your app's module folder.",
            AppLanguage.TAMIL to "சந்திப்புகள் பயர்ஸ்டோரில் சேமிக்கப்படுகின்றன. உங்கள் தனிப்பட்ட தரவுத்தளத்தை இணைக்க, 'google-services.json' கோப்பைச் சேர்க்கவும்."
        ),
        "Generate OTP on Mobile" to mapOf(
            AppLanguage.ENGLISH to "Generate OTP on Mobile",
            AppLanguage.TAMIL to "மொபைலுக்கு OTP அனுப்புக"
        ),
        "Enter 6-digit OTP" to mapOf(
            AppLanguage.ENGLISH to "Enter 6-digit OTP",
            AppLanguage.TAMIL to "6-இலக்க OTP உள்ளிடவும்"
        ),
        "Verify OTP & Access Data" to mapOf(
            AppLanguage.ENGLISH to "Verify OTP & Access Data",
            AppLanguage.TAMIL to "OTP சரிபார்த்து தரவை அணுகவும்"
        ),
        "Mobile OTP Security & Member Access" to mapOf(
            AppLanguage.ENGLISH to "Mobile OTP Security & Member Access",
            AppLanguage.TAMIL to "மொபைல் OTP பாதுகாப்பு மற்றும் உறுப்பினர் அணுகல்"
        ),
        "Phone Verified via OTP" to mapOf(
            AppLanguage.ENGLISH to "Phone Verified via OTP",
            AppLanguage.TAMIL to "தொலைபேசி எண் OTP மூலம் சரிபார்க்கப்பட்டது"
        ),
        "Remove Mobile OTP / Reset Verification" to mapOf(
            AppLanguage.ENGLISH to "Remove Mobile OTP / Reset Verification",
            AppLanguage.TAMIL to "மொபைல் OTP நீக்கு / சரிபார்ப்பை மீட்டமை"
        ),
        "Single Member Data Access Unlocked" to mapOf(
            AppLanguage.ENGLISH to "Single Member Data Access Unlocked",
            AppLanguage.TAMIL to "ஒரு நபர் தரவு அணுகல் திறக்கப்பட்டது"
        ),
        "OTP Verified Successfully!" to mapOf(
            AppLanguage.ENGLISH to "OTP Verified Successfully!",
            AppLanguage.TAMIL to "OTP வெற்றிகரமாக சரிபார்க்கப்பட்டது!"
        ),
        "Invalid OTP Code" to mapOf(
            AppLanguage.ENGLISH to "Invalid OTP Code",
            AppLanguage.TAMIL to "தவறான OTP குறியீடு"
        ),
        "OTP removed successfully" to mapOf(
            AppLanguage.ENGLISH to "OTP removed successfully",
            AppLanguage.TAMIL to "OTP வெற்றிகரமாக நீக்கப்பட்டது"
        ),
        "Voice Over Active..." to mapOf(
            AppLanguage.ENGLISH to "Voice Over Active...",
            AppLanguage.TAMIL to "குரல் சேவை செயல்படுகிறது..."
        ),
        "Read Aloud with Voice Over" to mapOf(
            AppLanguage.ENGLISH to "Read Aloud with Voice Over",
            AppLanguage.TAMIL to "குரல் வழிகாட்டல் மூலம் கேட்கவும்"
        ),
        "Not entered" to mapOf(
            AppLanguage.ENGLISH to "Not entered",
            AppLanguage.TAMIL to "சேர்க்கப்படவில்லை"
        )
    )

    fun getString(key: String): String {
        val trans = translations[key] ?: return key
        return trans[currentLanguage] ?: trans[AppLanguage.ENGLISH] ?: key
    }
}

// Extension to easily localize any string
val String.localized: String
    get() = LanguageManager.getString(this)
