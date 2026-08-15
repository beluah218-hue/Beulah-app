package com.example

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

val LocalVoiceOverHelper = staticCompositionLocalOf<VoiceOverHelper?> { null }

class VoiceOverHelper(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            updateLanguage()
            isInitialized = true
            tts?.setOnUtteranceProgressListener(object : android.speech.tts.UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                }

                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                }

                override fun onError(utteranceId: String?, errorCode: Int) {
                    _isSpeaking.value = false
                }
            })
        } else {
            Log.e("VoiceOverHelper", "TextToSpeech Initialization Failed!")
        }
    }

    fun updateLanguage() {
        if (tts == null) return
        val isTamil = LanguageManager.currentLanguage == AppLanguage.TAMIL
        val targetLocale = if (isTamil) Locale("ta", "IN") else Locale.US
        val result = tts?.setLanguage(targetLocale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            if (isTamil) {
                // Fallback to general Tamil locale or US
                val altResult = tts?.setLanguage(Locale("ta"))
                if (altResult == TextToSpeech.LANG_MISSING_DATA || altResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                    tts?.setLanguage(Locale.US)
                }
            } else {
                tts?.setLanguage(Locale.US)
            }
        }
    }

    fun speak(text: String, rate: Float = 1.0f) {
        if (!isInitialized || tts == null) return
        stop()
        updateLanguage()
        tts?.setSpeechRate(rate)
        val params = android.os.Bundle()
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "VoiceOverUtterance")
        _isSpeaking.value = true
        // Translate text if in Tamil mode and localized version exists
        val localizedText = text.localized
        tts?.speak(localizedText, TextToSpeech.QUEUE_FLUSH, params, "VoiceOverUtterance")
    }

    fun stop() {
        if (tts != null && tts?.isSpeaking == true) {
            tts?.stop()
        }
        _isSpeaking.value = false
    }

    fun readDoctorDetails(doctor: Doctor) {
        val text = if (LanguageManager.currentLanguage == AppLanguage.TAMIL) {
            "மருத்துவர் ${doctor.name}, ${doctor.specialtyDisplay.localized}. அனுபவம் ${doctor.yearsExperience} ஆண்டுகள். மதிப்பீடு ${doctor.rating} நட்சத்திரங்கள். அடுத்த நேரம் ${doctor.nextAvailable.localized}."
        } else {
            "Doctor ${doctor.name}, ${doctor.specialtyDisplay}. Experienced for ${doctor.yearsExperience} years. Rating ${doctor.rating} stars. Next available time slot: ${doctor.nextAvailable}."
        }
        speak(text)
    }

    fun readAppointmentConfirmation(doctorName: String, specialty: String, location: String = "", date: String, time: String, token: String) {
        val text = if (LanguageManager.currentLanguage == AppLanguage.TAMIL) {
            "மருத்துவர் $doctorName, ${specialty.localized} அவர்களுடனான உங்கள் சந்திப்பு $date அன்று $time மணிக்கு வெற்றிகரமாக முன்பதிவு செய்யப்பட்டது. உங்கள் டோக்கன் எண் $token. வருகையின் போது இந்த டோக்கனைக் காட்டவும்."
        } else {
            "Your appointment with Doctor $doctorName, $specialty, is successfully booked for $date at $time. Your token number is $token. Please present this token upon arrival."
        }
        speak(text)
    }

    fun readSymptomAnalysis(analysisText: String) {
        // Speak cleaned text without markdown stars
        val cleaned = analysisText.replace("*", "").take(300)
        val text = if (LanguageManager.currentLanguage == AppLanguage.TAMIL) {
            "அறிகுறி பகுப்பாய்வு: $cleaned"
        } else {
            "Symptom Analysis: $cleaned"
        }
        speak(text)
    }

    fun shutdown() {
        stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}

@Composable
fun rememberVoiceOverHelper(): VoiceOverHelper {
    val context = LocalContext.current
    val voiceOverHelper = remember(context) { VoiceOverHelper(context) }

    DisposableEffect(voiceOverHelper) {
        onDispose {
            voiceOverHelper.shutdown()
        }
    }

    return voiceOverHelper
}

@Composable
fun VoiceOverIconButton(
    textToSpeak: String? = null,
    onSpeak: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    contentDescription: String = "Read Aloud with Voice Over",
    tint: Color = MaterialTheme.colorScheme.primary
) {
    val voiceOverHelper = LocalVoiceOverHelper.current
    val isSpeaking by voiceOverHelper?.isSpeaking?.collectAsState() ?: remember { mutableStateOf(false) }

    IconButton(
        onClick = {
            if (isSpeaking) {
                voiceOverHelper?.stop()
            } else {
                if (onSpeak != null) {
                    onSpeak()
                } else if (textToSpeak != null) {
                    voiceOverHelper?.speak(textToSpeak)
                }
            }
        },
        modifier = modifier.testTag("voice_over_button")
    ) {
        Icon(
            imageVector = if (isSpeaking) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
            contentDescription = contentDescription,
            tint = if (isSpeaking) MaterialTheme.colorScheme.error else tint
        )
    }
}

@Composable
fun VoiceOverBanner(
    modifier: Modifier = Modifier
) {
    val voiceOverHelper = LocalVoiceOverHelper.current
    val isSpeaking by voiceOverHelper?.isSpeaking?.collectAsState() ?: remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = isSpeaking,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(20.dp),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.RecordVoiceOver,
                        contentDescription = "Voice Assistant Speaking",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Voice Over Active...".localized,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                
                Button(
                    onClick = { voiceOverHelper?.stop() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(30.dp),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Icon(Icons.Default.Stop, contentDescription = "Stop Speech", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Stop".localized, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

