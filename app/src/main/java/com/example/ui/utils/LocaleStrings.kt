package com.example.ui.utils

object LocaleStrings {

    fun get(key: String, language: String): String {
        val langMap = when (language) {
            "Hindi (हिन्दी)", "Hindi" -> hindi
            "Tamil (தமிழ்)", "Tamil" -> tamil
            "Telugu (తెలుగు)", "Telugu" -> telugu
            else -> english
        }
        return langMap[key] ?: english[key] ?: key
    }

    private val english = mapOf(
        "nav_scan" to "Scan & OCR",
        "nav_first_aid" to "First Aid",
        "nav_maya" to "Ask Maya",
        "nav_pregnancy" to "Pregnancy",
        "nav_settings" to "Settings",

        "scan_title" to "Scan & Analyze Report",
        "scan_engine_title" to "Report-to-First-Aid Engine",
        "scan_engine_desc" to "Upload your maternal lab report or scan. Our OCR identifies key values and provides an immediate first-aid home care plan.",
        "tap_to_choose" to "Tap here to choose report image or document",
        "supported_formats" to "Supported: PDF, JPG, PNG • ML Kit OCR",
        "report_ready" to "Report Ready for Analysis",
        "btn_start_analysis" to "Start OCR & First-Aid Analysis",
        "btn_upload_first" to "Upload Report to Start Analysis",
        "first_aid_center" to "First Aid Center",
        "all_topics" to "All Topics & Steps",
        "ask_maya" to "Ask Maya",
        "maternal_qa" to "Maternal Q&A",
        "emergency_help" to "Emergency Help",
        "emergency_desc" to "One-tap emergency call & critical warning signs",

        "report_first_aid_plan" to "Report First-Aid Plan",
        "immediate_actions" to "Immediate Safe Actions (Right Now)",
        "things_to_avoid" to "Things to Avoid Right Now",
        "when_emergency" to "When This Becomes an Emergency",
        "open_first_aid_topic" to "Open Dedicated First-Aid Topic",
        "ask_maya_about_report" to "Ask Maya About This Report",

        "settings_title" to "Settings & Profile",
        "my_info" to "My Information",
        "pregnancy_profile" to "Pregnancy Profile",
        "emergency_contact" to "Emergency Contact",
        "notifications" to "Notifications & Reminders",
        "app_theme" to "Theme & Display",
        "language_region" to "Language & Region",
        "clinical_units" to "Clinical Units",
        "data_privacy" to "Data & Privacy",
        "about_precare" to "About PreCare"
    )

    private val hindi = mapOf(
        "nav_scan" to "स्कैन और ओसीआर",
        "nav_first_aid" to "प्राथमिक चिकित्सा",
        "nav_maya" to "माया से पूछें",
        "nav_pregnancy" to "गर्भावस्था",
        "nav_settings" to "सेटिंग्स",

        "scan_title" to "रिपोर्ट स्कैन और विश्लेषण",
        "scan_engine_title" to "रिपोर्ट-टू-फर्स्ट-एड इंजन",
        "scan_engine_desc" to "अपनी मातृत्व लैब रिपोर्ट अपलोड करें। ओसीआर मूल्यों की पहचान करता है और तत्काल प्राथमिक चिकित्सा योजना प्रदान करता है।",
        "tap_to_choose" to "रिपोर्ट छवि या दस्तावेज़ चुनने के लिए यहां टैप करें",
        "supported_formats" to "समर्थित: PDF, JPG, PNG • ML Kit OCR",
        "report_ready" to "रिपोर्ट विश्लेषण के लिए तैयार है",
        "btn_start_analysis" to "ओसीआर और प्राथमिक चिकित्सा विश्लेषण शुरू करें",
        "btn_upload_first" to "विश्लेषण शुरू करने के लिए रिपोर्ट अपलोड करें",
        "first_aid_center" to "प्राथमिक चिकित्सा केंद्र",
        "all_topics" to "सभी विषय और कदम",
        "ask_maya" to "माया से पूछें",
        "maternal_qa" to "मातृ प्रश्नोत्तर",
        "emergency_help" to "आपातकालीन सहायता",
        "emergency_desc" to "एक-टैप आपातकालीन कॉल और चेतावनी संकेत",

        "report_first_aid_plan" to "रिपोर्ट प्राथमिक चिकित्सा योजना",
        "immediate_actions" to "तुरंत करने योग्य सुरक्षित कार्य",
        "things_to_avoid" to "इन बातों से तुरंत बचें",
        "when_emergency" to "जब यह आपातकालीन स्थिति बन जाए",
        "open_first_aid_topic" to "विस्तृत प्राथमिक चिकित्सा विषय खोलें",
        "ask_maya_about_report" to "इस रिपोर्ट के बारे में माया से पूछें",

        "settings_title" to "सेटिंग्स और प्रोफ़ाइल",
        "my_info" to "मेरी जानकारी",
        "pregnancy_profile" to "गर्भावस्था प्रोफ़ाइल",
        "emergency_contact" to "आपातकालीन संपर्क",
        "notifications" to "सूचनाएं और अनुस्मारक",
        "app_theme" to "थीम और डिस्प्ले",
        "language_region" to "भाषा और क्षेत्र",
        "clinical_units" to "चिकित्सीय इकाइयाँ",
        "data_privacy" to "डेटा और गोपनीयता",
        "about_precare" to "PreCare के बारे में"
    )

    private val tamil = mapOf(
        "nav_scan" to "ஸ்கேன் & OCR",
        "nav_first_aid" to "முதலுதவி",
        "nav_maya" to "மாயாவிடம் கேளுங்கள்",
        "nav_pregnancy" to "கர்ப்பம்",
        "nav_settings" to "அமைப்புகள்",

        "scan_title" to "அறிக்கை ஸ்கேன் & பகுப்பாய்வு",
        "scan_engine_title" to "அறிக்கை-முதலுதவி அமைப்பு",
        "scan_engine_desc" to "உங்கள் மருத்துவ அறிக்கையை பதிவேற்றவும். உடனடி முதலுதவி வழிகாட்டுதலைப் பெறுங்கள்.",
        "tap_to_choose" to "அறிக்கையை தேர்வு செய்ய இங்கே தட்டவும்",
        "supported_formats" to "ஆதரிக்கப்படும்: PDF, JPG, PNG • ML Kit OCR",
        "report_ready" to "அறிக்கை தயாராக உள்ளது",
        "btn_start_analysis" to "பகுப்பாய்வைத் தொடங்கு",
        "btn_upload_first" to "முதலில் அறிக்கையை பதிவேற்றவும்",
        "first_aid_center" to "முதலுதவி மையம்",
        "all_topics" to "அனைத்து தலைப்புகள்",
        "ask_maya" to "மாயாவிடம் கேளுங்கள்",
        "maternal_qa" to "தாய்மை கேள்வி பதில்",
        "emergency_help" to "அவசர உதவி",
        "emergency_desc" to "அவசர அழைப்பு & முக்கிய அறிகுறிகள்",

        "report_first_aid_plan" to "அறிக்கை முதலுதவி திட்டம்",
        "immediate_actions" to "உடனடி பாதுகாப்பான செயல்கள்",
        "things_to_avoid" to "தவிர்க்க வேண்டியவை",
        "when_emergency" to "அவசர சூழ்நிலைகள்",
        "open_first_aid_topic" to "முதலுதவி வழிகாட்டியைத் திறக்கவும்",
        "ask_maya_about_report" to "மாயாவிடம் விளக்கம் கேளுங்கள்",

        "settings_title" to "அமைப்புகள் & சுயவிவரம்",
        "my_info" to "என் தகவல்",
        "pregnancy_profile" to "கர்ப்ப சுயவிவரம்",
        "emergency_contact" to "அவசர தொடர்பு",
        "notifications" to "அறிவிப்புகள்",
        "app_theme" to "தீம் & காட்சி",
        "language_region" to "மொழி & பிராந்தியம்",
        "clinical_units" to "அளவீட்டு அலகுகள்",
        "data_privacy" to "தரவு & தனியுரிமை",
        "about_precare" to "PreCare பற்றி"
    )

    private val telugu = mapOf(
        "nav_scan" to "స్కాన్ & OCR",
        "nav_first_aid" to "ప్రథమ చికిత్స",
        "nav_maya" to "మాయాను అడగండి",
        "nav_pregnancy" to "గర్భధారణ",
        "nav_settings" to "సెట్టింగ్‌లు",

        "scan_title" to "రిపోర్ట్ స్కాన్ & విశ్లేషణ",
        "scan_engine_title" to "రిపోర్ట్-ప్రథమ చికిత్స ఇంజిన్",
        "scan_engine_desc" to "మీ ప్రసూతి ల్యాబ్ నివేదికను అప్‌లోడ్ చేయండి. తక్షణ ప్రథమ చికిత్స కార్యాచరణ ప్రణాళికను పొందండి.",
        "tap_to_choose" to "రిపోర్ట్ ఎంచుకోవడానికి ఇక్కడ నొక్కండి",
        "supported_formats" to "మద్దతు: PDF, JPG, PNG • ML Kit OCR",
        "report_ready" to "రిపోర్ట్ విశ్లేషణకు సిద్ధంగా ఉంది",
        "btn_start_analysis" to "OCR & ప్రథమ చికిత్స విశ్లేషణను ప్రారంభించండి",
        "btn_upload_first" to "విశ్లేషణ ప్రారంభించడానికి రిపోర్ట్ అప్‌లోడ్ చేయండి",
        "first_aid_center" to "ప్రథమ చికిత్స కేంద్రం",
        "all_topics" to "అన్ని అంశాలు మరియు దశలు",
        "ask_maya" to "మాయాను అడగండి",
        "maternal_qa" to "ప్రసూతి ప్రశ్నలు & సమాధానాలు",
        "emergency_help" to "అత్యవసర సహాయం",
        "emergency_desc" to "వన్-ట్యాప్ అత్యవసర కాల్ & హెచ్చరిక సంకేతాలు",

        "report_first_aid_plan" to "రిపోర్ట్ ప్రథమ చికిత్స ప్రణాళిక",
        "immediate_actions" to "తక్షణ సురక్షిత చర్యలు",
        "things_to_avoid" to "వెంటనే నివారించాల్సినవి",
        "when_emergency" to "అత్యవసర పరిస్థితి సంభవించినప్పుడు",
        "open_first_aid_topic" to "ప్రథమ చికిత్స మార్గదర్శినిని తెరవండి",
        "ask_maya_about_report" to "ఈ రిపోర్ట్ గురించి మాయాను అడగండి",

        "settings_title" to "సెట్టింగ్‌లు & ప్రొఫైల్",
        "my_info" to "నా సమాచారం",
        "pregnancy_profile" to "గర్భధారణ ప్రొఫైల్",
        "emergency_contact" to "అత్యవసర పరిచయం",
        "notifications" to "నోటిఫికేషన్‌లు",
        "app_theme" to "థీమ్ & ప్రదర్శన",
        "language_region" to "భాష & ప్రాంతం",
        "clinical_units" to "క్లినికల్ కొలతలు",
        "data_privacy" to "డేటా & గోప్యత",
        "about_precare" to "PreCare గురించి"
    )
}
