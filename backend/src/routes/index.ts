import { Router, Request, Response } from 'express';

const router = Router();

// Profile Routes
router.post('/profiles', (req: Request, res: Response) => {
  const { name, age, bloodGroup, allergies, medicalConditions } = req.body;
  const profileId = 'profile-' + Date.now();
  res.status(201).json({
    success: true,
    data: {
      profileId,
      name: name || 'Shalini',
      age: age || '28',
      bloodGroup: bloodGroup || 'O+',
      allergies: allergies || 'None',
      medicalConditions: medicalConditions || 'None'
    }
  });
});

router.get('/profiles/:id', (req: Request, res: Response) => {
  res.status(200).json({
    success: true,
    data: {
      profileId: req.params.id,
      name: 'Shalini',
      age: '28',
      bloodGroup: 'O+',
      allergies: 'None',
      medicalConditions: 'None'
    }
  });
});

// Pregnancy Routes
router.post('/pregnancy', (req: Request, res: Response) => {
  const { profileId, lastMenstrualPeriod, estimatedDueDate, currentWeek, firstPregnancy } = req.body;
  const week = currentWeek || 24;
  const trimester = week <= 13 ? 1 : week <= 27 ? 2 : 3;
  res.status(201).json({
    success: true,
    data: {
      profileId,
      lastMenstrualPeriod: lastMenstrualPeriod || '2026-04-15',
      estimatedDueDate: estimatedDueDate || '2026-11-20',
      currentWeek: week,
      trimester,
      firstPregnancy: firstPregnancy ?? true
    }
  });
});

router.get('/pregnancy/:profileId', (req: Request, res: Response) => {
  res.status(200).json({
    success: true,
    data: {
      profileId: req.params.profileId,
      lastMenstrualPeriod: '2026-04-15',
      estimatedDueDate: '2026-11-20',
      currentWeek: 24,
      trimester: 2,
      firstPregnancy: true
    }
  });
});

// Maya Chat Route (Safety-First with Emergency Detection)
router.post('/maya/chat', (req: Request, res: Response) => {
  const { message, currentWeek } = req.body;
  if (!message || typeof message !== 'string') {
    return res.status(400).json({
      success: false,
      error: { code: 'INVALID_INPUT', message: 'Message field is required.' }
    });
  }

  const lower = message.toLowerCase();
  const emergencyKeywords = [
    'heavy bleeding', 'severe abdominal pain', 'cannot breathe', 'difficulty breathing',
    'chest pain', 'seizure', 'loss of consciousness', 'baby not moving', 'no movement'
  ];

  if (emergencyKeywords.some(k => lower.includes(k))) {
    return res.status(200).json({
      success: true,
      data: {
        safetyLevel: 'EMERGENCY',
        response: 'CRITICAL: The symptoms you described require immediate emergency evaluation. Please call emergency services (911/112) or go to the nearest maternity emergency department now.',
        seekImmediateCare: true
      }
    });
  }

  const week = currentWeek || 24;
  let reply = `Thank you for asking! During week ${week} of pregnancy, resting on your left side, staying hydrated, and keeping scheduled prenatal appointments are essential.`;

  if (lower.includes('iron') || lower.includes('hemoglobin')) {
    reply = 'Iron is essential during pregnancy because maternal blood volume increases by 45%. Include spinach, lentils, and fortified grains paired with Vitamin C to improve absorption. Always discuss lab values with your doctor.';
  } else if (lower.includes('kick') || lower.includes('movement')) {
    reply = 'Regular kick counts are usually tracked starting at 28 weeks. A healthy milestone is feeling at least 10 movements over 2 hours of quiet rest. If movement notably decreases, contact your obstetrician right away.';
  }

  res.status(200).json({
    success: true,
    data: {
      safetyLevel: 'NORMAL',
      response: reply,
      seekImmediateCare: false
    }
  });
});

// Nearby Hospitals Route
router.get('/hospitals/nearby', (req: Request, res: Response) => {
  const lat = parseFloat(req.query.latitude as string) || 37.7749;
  const lng = parseFloat(req.query.longitude as string) || -122.4194;

  res.status(200).json({
    success: true,
    data: [
      {
        name: "City Maternity & Women's Hospital",
        address: "742 Healthcare Blvd, Maternal Care Wing",
        distance: "1.2 km away",
        phone: "555-019-4821",
        latitude: lat + 0.005,
        longitude: lng + 0.005,
        openNow: true
      },
      {
        name: "St. Jude Women & Children's Medical Center",
        address: "120 Emergency Drive, Building B",
        distance: "2.8 km away",
        phone: "555-014-9382",
        latitude: lat - 0.008,
        longitude: lng + 0.004,
        openNow: true
      },
      {
        name: "University General Hospital (Obstetric Triage)",
        address: "500 Medical Center Parkway",
        distance: "4.1 km away",
        phone: "555-017-2930",
        latitude: lat + 0.012,
        longitude: lng - 0.006,
        openNow: true
      }
    ]
  });
});

// Appointments Route
router.post('/appointments', (req: Request, res: Response) => {
  const { profileId, doctorName, hospitalName, date, time, notes } = req.body;
  res.status(201).json({
    success: true,
    data: {
      id: 'appt-' + Date.now(),
      profileId: profileId || 'local-1',
      doctorName: doctorName || 'Dr. Ananya Sharma',
      hospitalName: hospitalName || 'City Maternity Hospital',
      date: date || '2026-10-15',
      time: time || '10:30 AM',
      notes: notes || ''
    }
  });
});

export default router;
