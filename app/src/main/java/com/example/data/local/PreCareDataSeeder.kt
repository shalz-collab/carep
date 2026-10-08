package com.example.data.local

import com.example.data.local.dao.PreCareDao
import com.example.data.local.entity.FirstAidItem
import com.example.data.local.entity.PregnancyWeekInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object PreCareDataSeeder {

    suspend fun seedIfNeeded(dao: PreCareDao) {
        withContext(Dispatchers.IO) {
            if (dao.getFirstAidCount() == 0) {
                dao.insertFirstAidItems(firstAidData)
            }
            if (dao.getPregnancyWeeksCount() == 0) {
                dao.insertPregnancyWeeks(pregnancyWeeksData)
            }
        }
    }

    private val firstAidData = listOf(
        FirstAidItem(
            id = "bleeding",
            title = "Vaginal Bleeding",
            category = "Pregnancy First Aid",
            summary = "Any spotting or vaginal bleeding during pregnancy requires prompt medical evaluation.",
            whatMayBeHappening = "Can range from harmless implantation spotting in early weeks to cervical changes, placenta previa, or subchorionic hematoma. In late pregnancy, it can signal placental abruption or labor.",
            immediateSteps = "1. Sit or lie down on your left side immediately.\n2. Put on a clean sanitary pad (do not use tampons) to monitor amount and color.\n3. Note whether the blood is bright red, pink, or brownish.\n4. Stay calm and rest quietly while contacting your doctor.",
            thingsToAvoid = "• Do not insert anything into the vagina (no tampons or douches).\n• Do not engage in sexual intercourse.\n• Do not take aspirin, ibuprofen, or unprescribed home medications.\n• Do not perform heavy physical exertion.",
            whenToContactDoctor = "Contact your obstetrician or midwife immediately for any spotting, bleeding with cramping, clots, or fever.",
            emergencyWarning = "CRITICAL: If bleeding is heavy (soaking a pad in an hour), accompanied by severe abdominal pain, dizziness, fainting, or chills, go to the nearest emergency room immediately.",
            isEmergency = true
        ),
        FirstAidItem(
            id = "dizziness",
            title = "Dizziness & Lightheadedness",
            category = "Pregnancy First Aid",
            summary = "Feeling faint or woozy caused by expanding circulatory system, blood pressure shifts, or low blood sugar.",
            whatMayBeHappening = "Progesterone relaxes blood vessel walls, causing blood pressure to drop. Lying flat on your back can compress the inferior vena cava, decreasing blood return to the heart.",
            immediateSteps = "1. Sit down immediately or lie down on your left side to maximize blood flow to the heart and baby.\n2. Lower your head between your knees if sitting.\n3. Take slow, deep breaths.\n4. Sip cool water and eat a light snack with complex carbohydrates.\n5. Loosen tight clothing around your chest and waist.",
            thingsToAvoid = "• Do not stand up abruptly.\n• Do not lie flat on your back.\n• Do not skip meals or let yourself get dehydrated.\n• Do not stay in hot, crowded, or unventilated spaces.",
            whenToContactDoctor = "Inform your doctor if dizziness happens frequently, occurs with blurred vision, or is accompanied by heart palpitations.",
            emergencyWarning = "Seek emergency care if dizziness leads to fainting, sudden numbness, chest tightness, or severe headache.",
            isEmergency = false
        ),
        FirstAidItem(
            id = "fainting",
            title = "Fainting (Syncope)",
            category = "Pregnancy First Aid",
            summary = "Temporary loss of consciousness due to reduced cerebral perfusion.",
            whatMayBeHappening = "Sudden drop in blood pressure, dehydration, prolonged standing, or supine hypotensive syndrome (lying on back).",
            immediateSteps = "1. If you feel faint, lie down on your left side immediately.\n2. If someone has fainted, position them safely on their left side with airway clear.\n3. Check breathing and responsiveness.\n4. Cool the forehead with a damp cloth.\n5. Once alert, give small sips of water.",
            thingsToAvoid = "• Do not attempt to stand up quickly after recovering.\n• Do not give fluids while the person is unconscious.\n• Do not leave the person unattended.",
            whenToContactDoctor = "Always report any episode of fainting to your obstetrician, even if you feel completely fine afterward.",
            emergencyWarning = "CALL EMERGENCY SERVICES if consciousness is not regained within 1 minute, if there was a head strike, or if accompanied by seizures, bleeding, or difficulty breathing.",
            isEmergency = true
        ),
        FirstAidItem(
            id = "headache",
            title = "Severe Headache",
            category = "Pregnancy First Aid",
            summary = "While tension headaches are common, persistent severe headaches can indicate gestational hypertension or preeclampsia.",
            whatMayBeHappening = "Hormonal fluctuations, fatigue, or dehydration in early pregnancy. After 20 weeks, persistent throbbing headaches can be a warning sign of preeclampsia.",
            immediateSteps = "1. Rest in a dark, quiet, well-ventilated room.\n2. Place a cool damp cloth or ice pack over your forehead or back of your neck.\n3. Drink 1-2 glasses of water.\n4. Check your blood pressure if you have a home monitor.",
            thingsToAvoid = "• Do not take NSAIDs like ibuprofen, naproxen, or aspirin without doctor approval.\n• Do not ignore headaches accompanied by visual changes.",
            whenToContactDoctor = "Call your doctor if headache lasts more than 2 hours or does not respond to resting and hydration.",
            emergencyWarning = "EMERGENCY: Sudden severe headache with visual disturbances (flashing lights, blurriness), sudden face/hand swelling, or upper right abdominal pain requires immediate emergency evaluation for preeclampsia.",
            isEmergency = true
        ),
        FirstAidItem(
            id = "vomiting",
            title = "Severe Vomiting & Hyperemesis",
            category = "Pregnancy First Aid",
            summary = "Excessive vomiting beyond regular morning sickness that prevents keeping fluids down.",
            whatMayBeHappening = "Elevated hCG levels can trigger hyperemesis gravidarum, putting both mother and fetus at risk of dehydration and electrolyte imbalance.",
            immediateSteps = "1. Rest in an upright or slightly reclined position.\n2. Take tiny sips of electrolyte solution, coconut water, or ice chips every 10 minutes.\n3. Avoid strong smells and food aromas.\n4. Try bland dry crackers in small nibbles.",
            thingsToAvoid = "• Do not gulp large amounts of water at once (triggers gag reflex).\n• Do not consume greasy, spicy, or strongly scented foods.\n• Do not take over-the-counter antiemetics without prescription.",
            whenToContactDoctor = "Contact doctor if unable to keep liquids down for more than 12 hours, dark urine, or weight loss.",
            emergencyWarning = "Go to emergency care if vomiting blood or dark coffee-ground material, severe abdominal pain, or confusion.",
            isEmergency = false
        ),
        FirstAidItem(
            id = "dehydration",
            title = "Dehydration",
            category = "Pregnancy First Aid",
            summary = "Insufficient fluid balance which can cause uterine irritability, contractions, and low amniotic fluid.",
            whatMayBeHappening = "Increased fluid needs in pregnancy (blood volume expands by 45%). Dehydration easily occurs from hot weather, vomiting, or low intake.",
            immediateSteps = "1. Move to a cool, shaded place.\n2. Slowly drink an oral rehydration salt (ORS) solution or cool water.\n3. Rest with legs elevated.\n4. Monitor urine color—aim for pale straw color.",
            thingsToAvoid = "• Do not drink caffeinated beverages, energy drinks, or high-sugar sodas.\n• Do not engage in strenuous physical activity.",
            whenToContactDoctor = "Contact doctor if urine is very dark, infrequent (less than 3 times a day), or accompanied by uterine tightening.",
            emergencyWarning = "Seek immediate care if accompanied by rapid heartbeat, lethargy, sunken eyes, or preterm contractions.",
            isEmergency = false
        ),
        FirstAidItem(
            id = "abdominal_pain",
            title = "Abdominal Pain & Cramping",
            category = "Pregnancy First Aid",
            summary = "Discomfort in the pelvic or stomach region.",
            whatMayBeHappening = "Mild stretching of round ligaments is normal. Severe or rhythmic cramping may indicate ectopic pregnancy, miscarriage, placenta issues, or preterm labor.",
            immediateSteps = "1. Lie down on your left side to take pressure off internal organs.\n2. Place a warm (not hot) water bottle wrapped in a towel on lower abdomen.\n3. Drink a tall glass of water.\n4. Time any rhythmic cramping or tightening.",
            thingsToAvoid = "• Do not use hot heating pads directly on the pregnant belly.\n• Do not take pain killers without medical advice.\n• Do not massage or press hard on the abdomen.",
            whenToContactDoctor = "Contact doctor if cramping is rhythmic, sharper than menstrual cramps, or accompanied by lower back ache.",
            emergencyWarning = "EMERGENCY: Sharp, one-sided pain in early pregnancy, shoulder tip pain, severe continuous pain, or bleeding requires immediate hospital care.",
            isEmergency = true
        ),
        FirstAidItem(
            id = "minor_burns",
            title = "Minor Burns",
            category = "General First Aid",
            summary = "First-degree superficial burns from kitchen cooking or hot liquids.",
            whatMayBeHappening = "Heat damages superficial skin layers causing redness, stinging, and localized inflammation.",
            immediateSteps = "1. Hold burned area under gentle cool running water for 15-20 minutes.\n2. Remove rings or tight accessories near the area before swelling starts.\n3. Cover loosely with a sterile, non-adherent dressing or clean cling wrap.\n4. Take pregnancy-safe paracetamol only if approved by your doctor.",
            thingsToAvoid = "• NEVER apply ice, butter, toothpaste, or oil to a burn.\n• Do not pop or puncture blisters.\n• Do not apply adhesive bandages directly on the burned skin.",
            whenToContactDoctor = "Seek medical check if the burn covers an area larger than 3 inches, is on the face, hands, or abdomen, or shows signs of infection.",
            emergencyWarning = "Seek emergency care for charred skin, electrical burns, or chemical exposure.",
            isEmergency = false
        ),
        FirstAidItem(
            id = "minor_cuts",
            title = "Minor Cuts & Scrapes",
            category = "General First Aid",
            summary = "Small lacerations with minor bleeding.",
            whatMayBeHappening = "Breach of skin barrier. Blood clotting happens normally, but cleanliness is paramount during pregnancy to avoid infection.",
            immediateSteps = "1. Wash hands thoroughly with soap.\n2. Apply gentle, direct pressure using a clean gauze or cloth for 5 minutes.\n3. Rinse the wound under clean running tap water.\n4. Apply mild antiseptic and cover with a sterile bandage.",
            thingsToAvoid = "• Do not touch open wound with dirty hands.\n• Do not blow on the wound.\n• Do not apply aggressive chemical solvents.",
            whenToContactDoctor = "Contact doctor if tetanus immunization is out of date or wound edges gape apart.",
            emergencyWarning = "Go to emergency room if bleeding spurts or does not stop after 10 minutes of continuous direct pressure.",
            isEmergency = false
        ),
        FirstAidItem(
            id = "nosebleed",
            title = "Nosebleed (Epistaxis)",
            category = "Women's Health",
            summary = "Bleeding from nasal passages, very common in pregnancy due to increased blood flow and fragile mucous membranes.",
            whatMayBeHappening = "Estrogen and progesterone swell mucous membranes in the nose, making tiny capillaries more prone to bleeding.",
            immediateSteps = "1. Sit upright and lean slightly FORWARD (never lean back).\n2. Firmly pinch the soft part of your nose just below the nasal bridge.\n3. Hold pressure continuously for 10-15 minutes without letting go.\n4. Breathe through your mouth.\n5. Apply a cold compress to the bridge of your nose.",
            thingsToAvoid = "• Do not lean your head back (this causes blood to flow down the throat and trigger vomiting).\n• Do not blow your nose or pick at it for several hours after bleeding stops.\n• Do not pack the nose with dry cotton swabs.",
            whenToContactDoctor = "Report recurrent nosebleeds to your doctor during routine prenatal checkups.",
            emergencyWarning = "Seek emergency care if nosebleed continues for more than 20 minutes despite firm pressure, or causes difficulty breathing.",
            isEmergency = false
        ),
        FirstAidItem(
            id = "minor_falls",
            title = "Minor Falls & Trauma",
            category = "Pregnancy First Aid",
            summary = "Slipping or tripping due to shifted center of gravity and relaxed pelvic ligaments.",
            whatMayBeHappening = "Amniotic fluid and muscular uterine wall provide great cushioning, but any direct impact or sudden deceleration needs careful monitoring.",
            immediateSteps = "1. Stay calm and sit or lie on your left side.\n2. Check for injury to head, arms, legs, and belly.\n3. Pay close attention to baby's movements.\n4. Check underwear for fluid leaks or bleeding.\n5. Call your prenatal care provider for guidance.",
            thingsToAvoid = "• Do not jump up quickly.\n• Do not disregard a fall even if it seemed minor, especially in the 2nd or 3rd trimester.",
            whenToContactDoctor = "Always contact your obstetric care provider after any fall, even if you feel completely fine, to verify fetal wellbeing.",
            emergencyWarning = "EMERGENCY: Seek immediate hospital care if you experienced direct impact to the abdomen, vaginal bleeding, leaking fluid, uterine pain, contractions, or decreased fetal movements.",
            isEmergency = true
        ),
        FirstAidItem(
            id = "allergic_reaction",
            title = "Allergic Reaction & Hives",
            category = "Emergency Warning Signs",
            summary = "Immune reaction causing rash, itching, or swelling.",
            whatMayBeHappening = "Exposure to food allergens, medications, or insect stings triggering systemic histamine release.",
            immediateSteps = "1. Identify and remove any known allergic trigger.\n2. Wash skin with cool water to soothe itching.\n3. Sit in an upright, relaxed position.\n4. Check breathing closely.",
            thingsToAvoid = "• Do not take over-the-counter antihistamines without checking pregnancy safety with a physician.\n• Do not scratch irritated skin intensely.",
            whenToContactDoctor = "Consult doctor for widespread itchy rash, pregnancy hives (PUPPP), or suspicious skin lesions.",
            emergencyWarning = "CRITICAL EMERGENCY: Swelling of lips, tongue, or throat, wheezing, difficulty swallowing or breathing, or feeling faint indicates anaphylaxis. USE AUTO-INJECTOR (EpiPen) IF PRESCRIBED AND CALL EMERGENCY SERVICES IMMEDIATELY.",
            isEmergency = true
        ),
        FirstAidItem(
            id = "breathing_difficulty",
            title = "Breathing Difficulty & Shortness of Breath",
            category = "Emergency Warning Signs",
            summary = "Sudden or severe difficulty taking in enough air.",
            whatMayBeHappening = "Mild breathlessness when climbing stairs is normal in pregnancy as the growing uterus pushes upward on the diaphragm. Sudden shortness of breath can signal pulmonary embolism, asthma flare, or cardiac issue.",
            immediateSteps = "1. Stop all activity and sit completely upright.\n2. Loosen tight bras and waistbands.\n3. Take slow, pursed-lip breaths.\n4. Ensure fresh air circulation.",
            thingsToAvoid = "• Do not lie flat on your back.\n• Do not exert yourself.",
            whenToContactDoctor = "Discuss any new or progressively worsening shortness of breath with your doctor.",
            emergencyWarning = "EMERGENCY: Sudden onset, chest pain, coughing up blood, bluish lips/fingertips, or inability to speak full sentences requires calling emergency services immediately.",
            isEmergency = true
        ),
        FirstAidItem(
            id = "chest_pain",
            title = "Chest Pain",
            category = "Emergency Warning Signs",
            summary = "Pressure, tightness, or pain located in the chest area.",
            whatMayBeHappening = "Could be heartburn (indigestion), muscle strain, or a serious cardiovascular condition like peripartum cardiomyopathy or pulmonary embolism.",
            immediateSteps = "1. Sit in an upright, supported position immediately.\n2. Loosen any constrictive clothing.\n3. Take slow gentle breaths.\n4. Keep calm and seek help.",
            thingsToAvoid = "• Do not assume chest pain in pregnancy is simply heartburn without medical confirmation.\n• Do not exert yourself physically.",
            whenToContactDoctor = "Any unexplained chest pain requires immediate medical review.",
            emergencyWarning = "CALL EMERGENCY SERVICES IMMEDIATELY if pain radiates to left arm, jaw, neck, or back, or is accompanied by cold sweat, nausea, or shortness of breath.",
            isEmergency = true
        ),
        FirstAidItem(
            id = "seizure",
            title = "Seizure / Convulsions",
            category = "Emergency Warning Signs",
            summary = "Involuntary shaking, twitching, or loss of responsiveness.",
            whatMayBeHappening = "In pregnancy, sudden seizures can indicate eclampsia—a life-threatening complication of severe preeclampsia.",
            immediateSteps = "1. Call Emergency Services (911/112) immediately.\n2. Gently position the mother on her LEFT side to keep airway open and protect blood flow to fetus.\n3. Place something soft under her head.\n4. Clear away hard or sharp objects.\n5. Note the duration of the seizure.",
            thingsToAvoid = "• NEVER hold down or restrain the person.\n• NEVER put anything in their mouth.\n• Do not offer food or drink.",
            whenToContactDoctor = "This is a direct medical emergency requiring immediate hospitalization.",
            emergencyWarning = "CRITICAL EMERGENCY: Any seizure during pregnancy is an obstetric emergency. Call emergency services immediately without delay.",
            isEmergency = true
        )
    )

    private val pregnancyWeeksData = (1..40).map { week ->
        val trimester = when {
            week <= 13 -> 1
            week <= 27 -> 2
            else -> 3
        }
        val sizeComparison = when (week) {
            1, 2 -> "A tiny spark of potential"
            3 -> "A microscopic poppy seed"
            4 -> "A tiny poppy seed (1 mm)"
            5 -> "An apple seed (2 mm)"
            6 -> "A sweet pea (5 mm)"
            7 -> "A blueberry (1 cm)"
            8 -> "A kidney bean (1.6 cm)"
            9 -> "A green olive (2.3 cm)"
            10 -> "A prune (3.1 cm)"
            11 -> "A lime (4.1 cm)"
            12 -> "A plum (5.4 cm)"
            13 -> "A peach (7.4 cm)"
            14 -> "A lemon (8.7 cm)"
            15 -> "An apple (10 cm)"
            16 -> "An avocado (11.6 cm)"
            17 -> "A pomegranate (13 cm)"
            18 -> "A bell pepper (14.2 cm)"
            19 -> "A mango (15.3 cm)"
            20 -> "A banana (16.4 cm - Halfway!)"
            21 -> "A carrot (26.7 cm)"
            22 -> "A papaya (27.8 cm)"
            23 -> "An eggplant (28.9 cm)"
            24 -> "An ear of corn (30 cm)"
            25 -> "An acorn squash (34.6 cm)"
            26 -> "A zucchini (35.6 cm)"
            27 -> "A head of cauliflower (36.6 cm)"
            28 -> "A large eggplant (37.6 cm)"
            29 -> "A butternut squash (38.6 cm)"
            30 -> "A head of cabbage (39.9 cm)"
            31 -> "A coconut (41.1 cm)"
            32 -> "A jicama (42.4 cm)"
            33 -> "A pineapple (43.7 cm)"
            34 -> "A cantaloupe (45 cm)"
            35 -> "A honeydew melon (46.2 cm)"
            36 -> "A bunch of kale (47.4 cm)"
            37 -> "A winter melon (48.6 cm)"
            38 -> "A stalk of rhubarb (49.8 cm)"
            39 -> "A small watermelon (50.7 cm)"
            else -> "A full pumpkin (51.2 cm - Ready for arrival!)"
        }

        PregnancyWeekInfo(
            weekNumber = week,
            trimester = trimester,
            babyDevelopment = when {
                week <= 4 -> "Cell division proceeds rapidly as the blastocyst implants into the uterine lining and embryonic disc forms."
                week <= 8 -> "Major organs begin developing. Neural tube closes and tiny heart begins beating rhythmically at 100-160 bpm."
                week <= 12 -> "Facial features, fingers, toes, and vocal cords develop. Reflexes appear as baby curls toes and clenches fists."
                week <= 16 -> "Baby can make sucking motions. Skeleton begins hardening from flexible cartilage into bone."
                week <= 20 -> "Fine hair (lanugo) covers baby's skin. Vernix caseosa forms to protect skin in amniotic fluid. First gentle kicks ('quickening') felt."
                week <= 24 -> "Inner ear structures are complete; baby can hear mother's voice and heartbeat! Lungs develop surfactant branches."
                week <= 28 -> "Baby's eyes open and close, with active REM sleep cycles. Brain tissue continues rapid folding."
                week <= 32 -> "Baby practices breathing motions. Layer of protective subcutaneous fat accumulates to regulate temperature after birth."
                week <= 36 -> "Baby gains about 200-250 grams per week. Most babies settle into a head-down (vertex) position."
                else -> "Full term! Baby's organs and lungs are completely ready for outside world respiration and feeding."
            },
            maternalChanges = when {
                week <= 13 -> "Hormonal shifts with rising hCG and progesterone. You may feel fatigue, mild breast tenderness, and heightened smell."
                week <= 27 -> "Often called the golden trimester. Energy frequently rebounds, baby bump becomes visible, and appetite normalizes."
                else -> "Growing bump exerts pressure on ribs and bladder. You may experience pelvic pressure, backache, and occasional Braxton Hicks tightenings."
            },
            careGuidance = "Drink 8-10 glasses of water daily, maintain gentle walking or prenatal yoga, sleep on your left side to optimize placental circulation, and take daily prenatal vitamins with folic acid.",
            nutritionGuidance = "Focus on iron-rich foods (lentils, spinach, lean meats), calcium (yogurt, fortified milks), omega-3 DHA (chia seeds, walnuts, low-mercury fish), and colorful antioxidant-rich fruits.",
            commonSymptoms = when {
                week <= 13 -> "Mild nausea, fatigue, frequent urination, tender breasts, food aversions."
                week <= 27 -> "Mild backache, skin glowing, growing belly, occasional leg cramps, increased appetite."
                else -> "Shortness of breath on exertion, frequent trips to bathroom, pelvic pressure, heartburn, mild feet swelling."
            },
            warningSigns = "Report immediately: heavy bleeding, severe cramping, sudden fluid gush, persistent severe headache with vision changes, fever over 100.4°F (38°C), or concerning decrease in fetal movement.",
            babySizeComparison = sizeComparison
        )
    }
}
