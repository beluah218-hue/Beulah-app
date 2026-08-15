package com.example

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiService {
    private const val TAG = "GeminiService"
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val mediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun getSymptomAnalysis(chatHistory: List<Pair<String, Boolean>>, userName: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        val fallbackUser = if (userName.isNotBlank()) userName else "Patient"

        val promptGuidelines = """
You are the AI Symptom Checker & Clinical Triage Assistant for "Life Care Hospital".
When analyzing the patient's reported symptoms:
1. Address the patient warmly as $fallbackUser.
2. Carefully evaluate the symptoms described.
3. Suggest the most appropriate Life Care Hospital department (e.g., General Medicine, Cardiology, Orthopedics, Neurology, Pediatrics, ENT, Dermatology, Dental, or 24x7 Emergency & Trauma).
4. Recommend a relevant specialist doctor from Life Care Hospital if applicable (e.g., Dr. Gowtham H for General Medicine, Dr. Arul Selvan for Cardiology, Dr. B Sreedhar for Orthopedics, Dr. Sathish Krishnan for Neurology, Dr. Praveen Kumar for Pediatrics, Dr. Menaka for Dental, Dr. Guruprasanth for ENT).
5. Provide actionable, supportive general health guidance (hydration, rest, avoiding self-medication, warning signs).
6. MANDATORY CLINICAL DISCLAIMER: You MUST prominently include the following exact statement:
"⚠️ MEDICAL DISCLAIMER: This AI symptom analysis is for informational and triage guidance only. It is NOT a clinical medical diagnosis or a substitute for professional healthcare. Please consult a qualified Life Care Hospital doctor for proper medical evaluation."
7. If symptoms suggest a severe condition (such as crushing chest pain, difficulty breathing, acute weakness/facial drooping, heavy bleeding), urge them to immediately use the "Life Care Hospital 24x7 Emergency SOS" or call emergency services.
Keep your output structured, clear, empathetic, and professional.
""".trimIndent()

        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            val lastUserQuery = chatHistory.lastOrNull { it.second }?.first?.lowercase() ?: ""
            val (dept, doc, advice) = when {
                lastUserQuery.contains("chest") || lastUserQuery.contains("heart") || lastUserQuery.contains("breath") -> 
                    Triple("Cardiology & Heart Center", "Dr. Arul Selvan", "Chest discomfort and breathing difficulty require prompt cardiac evaluation. Avoid strenuous exertion, rest in an upright position, and proceed to the Cardiology OPD or Emergency immediately.")
                lastUserQuery.contains("bone") || lastUserQuery.contains("joint") || lastUserQuery.contains("knee") || lastUserQuery.contains("back") || lastUserQuery.contains("fracture") ->
                    Triple("Orthopedics & Joint Care", "Dr. B Sreedhar", "Joint or musculoskeletal pain benefits from rest, ice packs if recently injured, and avoiding heavy weight-bearing until evaluated.")
                lastUserQuery.contains("headache") || lastUserQuery.contains("migraine") || lastUserQuery.contains("dizzy") || lastUserQuery.contains("numb") ->
                    Triple("Neurology & Brain Sciences", "Dr. Sathish Krishnan", "Persistent headaches or dizziness can stem from tension, migraines, or vascular causes. Maintain hydration and rest in a dark, quiet room.")
                lastUserQuery.contains("throat") || lastUserQuery.contains("ear") || lastUserQuery.contains("sinus") || lastUserQuery.contains("nose") ->
                    Triple("ENT (Ear, Nose, Throat)", "Dr. Guruprasanth G", "Warm saline gargles, steam inhalation, and hydration will soothe throat and sinus irritation while you await consultation.")
                lastUserQuery.contains("skin") || lastUserQuery.contains("rash") || lastUserQuery.contains("itch") ->
                    Triple("Dermatology & Skin Care", "Dr. L T Thenmozhi", "Avoid scratching the area, use gentle fragrance-free cleansers, and avoid applying unprescribed steroid creams.")
                lastUserQuery.contains("tooth") || lastUserQuery.contains("teeth") || lastUserQuery.contains("gum") ->
                    Triple("Dental & Maxillofacial", "Dr. Menaka Palaniappan", "Rinse with lukewarm salt water, avoid extremely hot or cold beverages, and maintain oral hygiene.")
                else ->
                    Triple("General Medicine", "Dr. Gowtham H", "Ensure good hydration, balanced nutrition, adequate rest, and monitor your temperature periodically.")
            }

            return@withContext """
Hello $fallbackUser! Thank you for sharing your symptoms with Life Care Hospital AI Symptom Checker.

🏥 Recommended Department: $dept
👨‍⚕️ Suggested Specialist: $doc

📋 General Health Guidance:
$advice

⚡ Next Step: You can book a direct OPD appointment or use our Pre-Arrival Fast-Track system to get a Zero-Wait Digital Token immediately.

⚠️ MEDICAL DISCLAIMER: This AI symptom analysis is for informational and triage guidance only. It is NOT a clinical medical diagnosis or a substitute for professional healthcare. Please consult a qualified Life Care Hospital doctor for proper medical evaluation.
""".trimIndent()
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"

        try {
            val contentsArray = JSONArray()

            for (chat in chatHistory) {
                val role = if (chat.second) "user" else "model"
                val contentObj = JSONObject()
                contentObj.put("role", role)
                
                val partsArray = JSONArray()
                val partObj = JSONObject()
                partObj.put("text", chat.first)
                partsArray.put(partObj)
                
                contentObj.put("parts", partsArray)
                contentsArray.put(contentObj)
            }

            val payload = JSONObject()
            payload.put("contents", contentsArray)

            val systemInstructionObj = JSONObject()
            val systemPartsArray = JSONArray()
            val systemPartObj = JSONObject()
            systemPartObj.put("text", promptGuidelines)
            systemPartsArray.put(systemPartObj)
            systemInstructionObj.put("parts", systemPartsArray)
            payload.put("systemInstruction", systemInstructionObj)

            val generationConfig = JSONObject()
            generationConfig.put("temperature", 0.6)
            generationConfig.put("maxOutputTokens", 800)
            payload.put("generationConfig", generationConfig)

            val requestBody = payload.toString().toRequestBody(mediaType)
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errBody = response.body?.string() ?: ""
                    Log.e(TAG, "API call failed with code ${response.code}: $errBody")
                    return@withContext "Hello $fallbackUser. Based on your symptoms, we recommend consulting our General Medicine department (Dr. Gowtham H). Rest, stay hydrated, and book an appointment.\n\n⚠️ MEDICAL DISCLAIMER: This AI symptom analysis is for informational and triage guidance only. It is NOT a clinical medical diagnosis."
                }

                val responseBody = response.body?.string()
                if (responseBody.isNullOrEmpty()) {
                    return@withContext "Error: Received empty response from Life Care AI."
                }

                val jsonResponse = JSONObject(responseBody)
                val candidates = jsonResponse.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val content = candidates.getJSONObject(0).optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text", "No response generated.")
                    }
                }
                return@withContext "Error: Could not parse response text."
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching analysis from Gemini", e)
            return@withContext "Hello $fallbackUser. Based on your symptoms, we suggest scheduling a consultation with Life Care Hospital General Medicine or the appropriate specialist.\n\n⚠️ MEDICAL DISCLAIMER: This AI symptom analysis is for informational and triage guidance only. It is NOT a clinical medical diagnosis."
        }
    }
}

