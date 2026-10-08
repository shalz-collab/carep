package com.example.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.ui.theme.AppTheme

enum class SupportedLanguage(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    TAMIL("ta", "Tamil", "தமிழ்"),
    TELUGU("te", "Telugu", "తెలుగు"),
    HINDI("hi", "Hindi", "हिंदी"),
    SPANISH("es", "Spanish", "Español");

    companion object {
        fun fromDisplayName(name: String): SupportedLanguage {
            return when {
                name.contains("Tamil", ignoreCase = true) || name.contains("தமிழ்") -> TAMIL
                name.contains("Telugu", ignoreCase = true) || name.contains("తెలుగు") -> TELUGU
                name.contains("Hindi", ignoreCase = true) || name.contains("हिंदी") -> HINDI
                name.contains("Spanish", ignoreCase = true) || name.contains("Español", ignoreCase = true) -> SPANISH
                else -> ENGLISH
            }
        }
    }
}

val LocalAppLanguage = staticCompositionLocalOf { SupportedLanguage.ENGLISH }

@Composable
fun ProvideAppLanguage(
    language: SupportedLanguage,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalAppLanguage provides language) {
        content()
    }
}

object PreCareStrings {
    // Navigation Tabs
    fun scanAndOcr(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "அறிக்கை ஸ்கேன்"
        SupportedLanguage.TELUGU -> "నివేదిక స్కాన్"
        SupportedLanguage.HINDI -> "रिपोर्ट स्कैन"
        SupportedLanguage.SPANISH -> "Escanear y Analizar"
        SupportedLanguage.ENGLISH -> "Scan & Analyze"
    }

    fun saved(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "சேமிக்கப்பட்டவை"
        SupportedLanguage.TELUGU -> "సేవ్ చేసినవి"
        SupportedLanguage.HINDI -> "सहेजे गए"
        SupportedLanguage.SPANISH -> "Guardados"
        SupportedLanguage.ENGLISH -> "Saved"
    }

    fun firstAid(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "முதலுதவி"
        SupportedLanguage.TELUGU -> "ప్రథమ చికిత్స"
        SupportedLanguage.HINDI -> "प्राथमिक चिकित्सा"
        SupportedLanguage.SPANISH -> "Primeros Auxilios"
        SupportedLanguage.ENGLISH -> "First Aid"
    }

    fun askMaya(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "மாயா"
        SupportedLanguage.TELUGU -> "మాయా"
        SupportedLanguage.HINDI -> "माया"
        SupportedLanguage.SPANISH -> "Maya"
        SupportedLanguage.ENGLISH -> "Ask Maya"
    }

    fun reportsHistory(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "சேமித்த அறிக்கைகள்"
        SupportedLanguage.TELUGU -> "సేవ్ చేసిన నివేదికలు"
        SupportedLanguage.HINDI -> "सहेजी गई रिपोर्टें"
        SupportedLanguage.SPANISH -> "Reportes Guardados"
        SupportedLanguage.ENGLISH -> "Saved Reports"
    }

    fun pregnancyJourney(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "கர்ப்ப பயணம்"
        SupportedLanguage.TELUGU -> "గర్భధారణ ప్రయాణం"
        SupportedLanguage.HINDI -> "गर्भावस्था यात्रा"
        SupportedLanguage.SPANISH -> "Viaje de Embarazo"
        SupportedLanguage.ENGLISH -> "Pregnancy"
    }

    fun settings(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "அமைப்புகள்"
        SupportedLanguage.TELUGU -> "సెట్టింగ్‌లు"
        SupportedLanguage.HINDI -> "सेटिंग्स"
        SupportedLanguage.SPANISH -> "Ajustes"
        SupportedLanguage.ENGLISH -> "Settings"
    }

    // Top Bar & Headers
    fun scanAndAnalyzeTitle(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "கர்ப்ப அறிக்கை ஸ்கேன் & OCR"
        SupportedLanguage.TELUGU -> "గర్భధారణ నివేదిక స్కాన్ & OCR"
        SupportedLanguage.HINDI -> "गर्भावस्था रिपोर्ट स्कैन और OCR"
        SupportedLanguage.SPANISH -> "Escanear Reporte de Embarazo"
        SupportedLanguage.ENGLISH -> "Scan & Analyze Report"
    }

    fun reportResultTitle(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "மருத்துவ பகுப்பாய்வு முடிவு"
        SupportedLanguage.TELUGU -> "వైద్య విశ్లేషణ ఫలితం"
        SupportedLanguage.HINDI -> "चिकित्सा विश्लेषण परिणाम"
        SupportedLanguage.SPANISH -> "Resultado del Análisis Médico"
        SupportedLanguage.ENGLISH -> "Medical Analysis Result"
    }

    // Upload & Dropzone
    fun uploadDropzoneTitle(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "கர்ப்ப அறிக்கையை பதிவேற்றவும் (PDF/படம்)"
        SupportedLanguage.TELUGU -> "గర్భధారణ నివేదికను అప్‌లోడ్ చేయండి (PDF/చిత్రం)"
        SupportedLanguage.HINDI -> "गर्भावस्था रिपोर्ट अपलोड करें (PDF/फोटो)"
        SupportedLanguage.SPANISH -> "Subir Reporte de Embarazo (PDF/Imagen)"
        SupportedLanguage.ENGLISH -> "Upload Pregnancy Report (PDF/Image)"
    }

    fun uploadDropzoneDesc(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "உண்மையான கர்ப்ப மருத்துவ அறிக்கைகள் மட்டுமே ஏற்கப்படும் (இரத்த பரிசோதனை, அல்ட்ராசவுண்ட் ஸ்கேன், சிறுநீர் வழக்கமான சோதனை)."
        SupportedLanguage.TELUGU -> "నిజమైన గర్భధారణ వైద్య నివేదికలు మాత్రమే ఆమోదించబడతాయి (రక్త పరీక్ష, అల్ట్రాసౌండ్ స్కాన్, మూత్ర పరీక్ష)."
        SupportedLanguage.HINDI -> "केवल वास्तविक गर्भावस्था रिपोर्ट ही स्वीकार की जाती हैं (रक्त परीक्षण, अल्ट्रासाउंड, यूरिन टेस्ट)।"
        SupportedLanguage.SPANISH -> "Solo se aceptan reportes médicos genuinos de embarazo (Sangre, Ultrasonido, Orina)."
        SupportedLanguage.ENGLISH -> "Accepts genuine pregnancy medical reports only (Blood tests, Ultrasound scans, Urinalysis)."
    }

    fun startAnalysisBtn(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "உடனடி மருத்துவ பகுப்பாய்வை தொடங்கு"
        SupportedLanguage.TELUGU -> "తక్షణ వైద్య విశ్లేషణను ప్రారంభించండి"
        SupportedLanguage.HINDI -> "तत्काल चिकित्सा विश्लेषण शुरू करें"
        SupportedLanguage.SPANISH -> "Iniciar Análisis Médico en Tiempo Real"
        SupportedLanguage.ENGLISH -> "Start Real-Time Medical Analysis"
    }

    fun uploadFirstPrompt(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "முதலில் கர்ப்ப அறிக்கையை பதிவேற்றவும்"
        SupportedLanguage.TELUGU -> "ముందుగా గర్భధారణ నివేదికను అప్‌లోడ్ చేయండి"
        SupportedLanguage.HINDI -> "कृपया पहले गर्भावस्था रिपोर्ट चुनें या अपलोड करें"
        SupportedLanguage.SPANISH -> "Seleccione o suba un reporte médico primero"
        SupportedLanguage.ENGLISH -> "Please select or upload a report first"
    }

    // Statuses
    fun normal(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "இயல்பானது (NORMAL)"
        SupportedLanguage.TELUGU -> "సాధారణం (NORMAL)"
        SupportedLanguage.HINDI -> "सामान्य (NORMAL)"
        SupportedLanguage.SPANISH -> "NORMAL"
        SupportedLanguage.ENGLISH -> "NORMAL"
    }

    fun abnormal(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "கவனம் தேவை (ABNORMAL)"
        SupportedLanguage.TELUGU -> "శ్రద్ధ అవసరం (ABNORMAL)"
        SupportedLanguage.HINDI -> "ध्यान देने योग्य (ABNORMAL)"
        SupportedLanguage.SPANISH -> "ATENCIÓN REQUERIDA"
        SupportedLanguage.ENGLISH -> "ABNORMAL / ATTENTION REQUIRED"
    }

    fun urgent(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "அவசர கவனிப்பு தேவை (URGENT)"
        SupportedLanguage.TELUGU -> "అత్యవసరం (URGENT)"
        SupportedLanguage.HINDI -> "तत्काल ध्यान दें (URGENT)"
        SupportedLanguage.SPANISH -> "URGENTE"
        SupportedLanguage.ENGLISH -> "URGENT"
    }

    fun allNormalBanner(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "பகுப்பாய்வு செய்யப்பட்ட அனைத்து அளவீடுகளும் அறிக்கையில் குறிப்பிடப்பட்டுள்ள குறிப்பு வரம்பிற்குள் உள்ளன."
        SupportedLanguage.TELUGU -> "విశ్లేషించబడిన అన్ని కొలతలు నివేదికలో చూపబడిన సాధారణ పరిమితుల్లోనే ఉన్నాయి."
        SupportedLanguage.HINDI -> "विश्लेषण किए गए सभी पैरामीटर रिपोर्ट में दर्शाई गई सामान्य सीमा के भीतर हैं।"
        SupportedLanguage.SPANISH -> "Sus parámetros analizados están dentro de los rangos de referencia aplicables mostrados en el reporte."
        SupportedLanguage.ENGLISH -> "Your analyzed parameters are within the applicable reference ranges shown on the report."
    }

    // Parameters section
    fun whatShouldIDoNext(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "அடுத்து நான் என்ன செய்ய வேண்டும்?"
        SupportedLanguage.TELUGU -> "తర్వాత నేను ఏమి చేయాలి?"
        SupportedLanguage.HINDI -> "मुझे आगे क्या करना चाहिए?"
        SupportedLanguage.SPANISH -> "¿Qué Debería Hacer Ahora?"
        SupportedLanguage.ENGLISH -> "What Should I Do Next?"
    }

    fun contactDoctor(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "மருத்துவரை அழைக்கவும்"
        SupportedLanguage.TELUGU -> "వైద్యుడిని సంప్రదించండి"
        SupportedLanguage.HINDI -> "डॉक्टर से संपर्क करें"
        SupportedLanguage.SPANISH -> "Contactar Doctor"
        SupportedLanguage.ENGLISH -> "Contact Doctor"
    }

    fun bookAppointment(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "முன்பதிவு செய்ய"
        SupportedLanguage.TELUGU -> "అపాయింట్‌మెంట్ బుక్ చేయండి"
        SupportedLanguage.HINDI -> "अपॉइंटमेंट बुक करें"
        SupportedLanguage.SPANISH -> "Agendar Cita"
        SupportedLanguage.ENGLISH -> "Book Appointment"
    }

    fun askMayaBtn(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "மாயாவிடம் கேளுங்கள்"
        SupportedLanguage.TELUGU -> "మాయాను అడగండి"
        SupportedLanguage.HINDI -> "माया से पूछें"
        SupportedLanguage.SPANISH -> "Preguntar a Maya"
        SupportedLanguage.ENGLISH -> "Ask Maya"
    }

    fun viewFullReport(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "முழு அறிக்கை பார்க்க"
        SupportedLanguage.TELUGU -> "పూర్తి నివేదికను చూడండి"
        SupportedLanguage.HINDI -> "पूरी रिपोर्ट देखें"
        SupportedLanguage.SPANISH -> "Ver Reporte Completo"
        SupportedLanguage.ENGLISH -> "View Full Report"
    }

    fun viewFirstAidPlan(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "உடனடி முதலுதவி & வீட்டுப் பராமரிப்பு"
        SupportedLanguage.TELUGU -> "తక్షణ గృహ సంరక్షణ & ప్రథమ చికిత్స"
        SupportedLanguage.HINDI -> "तत्काल प्राथमिक उपचार व देखभाल"
        SupportedLanguage.SPANISH -> "Atención Inmediata y Primeros Auxilios"
        SupportedLanguage.ENGLISH -> "Immediate Home Care & First Aid"
    }

    fun emergencyHelp(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "அவசர உதவி"
        SupportedLanguage.TELUGU -> "అత్యవసర సహాయం"
        SupportedLanguage.HINDI -> "आपातकालीन सहायता"
        SupportedLanguage.SPANISH -> "Ayuda de Emergencia"
        SupportedLanguage.ENGLISH -> "Emergency Help"
    }

    fun medicalDisclaimer(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "PreCare கல்வி மற்றும் முதலுதவி வழிகாட்டுதலை வழங்குகிறது மற்றும் தொழில்முறை மருத்துவ சிகிச்சைக்கு மாற்றாகாது. அவசரநிலை ஏற்பட்டால், உடனடியாக மருத்துவ அவசர சேவைகளை அழைக்கவும்."
        SupportedLanguage.TELUGU -> "PreCare విద్యా మరియు ప్రథమ చికిత్స మార్గదర్శకత్వాన్ని అందిస్తుంది మరియు వృత్తిపరమైన వైద్య చికిత్సకు ప్రత్యామ్నాయం కాదు. అత్యవసర పరిస్థితుల్లో వెంటనే అత్యవసర సేవలకు కాల్ చేయండి."
        SupportedLanguage.HINDI -> "PreCare केवल शैक्षिक और प्राथमिक चिकित्सा मार्गदर्शन प्रदान करता है और पेशेवर चिकित्सीय परामर्श का विकल्प नहीं है। आपातकाल में तुरंत आपातकालीन सेवाओं को कॉल करें।"
        SupportedLanguage.SPANISH -> "PreCare proporciona orientación educativa y de primeros auxilios y no reemplaza la atención médica profesional."
        SupportedLanguage.ENGLISH -> "PreCare provides educational and first-aid guidance and does not replace professional medical care. In an emergency, call emergency services immediately."
    }

    fun whatItMeans(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "இதன் பொருள்:"
        SupportedLanguage.TELUGU -> "దీని అర్థం:"
        SupportedLanguage.HINDI -> "इसका क्या अर्थ है:"
        SupportedLanguage.SPANISH -> "Lo que significa:"
        SupportedLanguage.ENGLISH -> "What it means:"
    }

    fun whatToDo(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.TAMIL -> "செய்ய வேண்டியவை:"
        SupportedLanguage.TELUGU -> "చేయవలసినది:"
        SupportedLanguage.HINDI -> "क्या करें:"
        SupportedLanguage.SPANISH -> "Qué hacer:"
        SupportedLanguage.ENGLISH -> "What to do:"
    }
}
