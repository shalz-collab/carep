package com.example.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.emergency.EmergencyScreen
import com.example.ui.firstaid.FirstAidDetailScreen
import com.example.ui.firstaid.FirstAidScreen
import com.example.ui.maya.MayaChatScreen
import com.example.ui.nutrition.NutritionScreen
import com.example.ui.onboarding.BasicInfoScreen
import com.example.ui.onboarding.EmergencyContactScreen
import com.example.ui.onboarding.PregnancySetupScreen
import com.example.ui.onboarding.SplashScreen
import com.example.ui.onboarding.WelcomeScreen
import com.example.ui.pregnancy.PregnancyJourneyScreen
import com.example.ui.pregnancy.WeekDetailScreen
import com.example.ui.profile.ProfileScreen
import com.example.ui.reports.ReportAnalyzerScreen
import com.example.ui.reports.ReportFirstAidPlanScreen
import com.example.ui.reports.ReportResultScreen
import com.example.ui.reports.ReportsScreen
import com.example.ui.theme.PreCareBackground
import com.example.ui.theme.PreCarePrimary
import com.example.ui.theme.PreCarePrimarySoft
import com.example.ui.theme.PreCareSecondaryText
import com.example.ui.util.PreCareStrings
import com.example.ui.util.SupportedLanguage
import com.example.ui.viewmodel.PreCareViewModel

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Welcome : Screen("welcome")
    object BasicInfo : Screen("basic_info")
    object PregnancySetup : Screen("pregnancy_setup")
    object EmergencyContact : Screen("emergency_contact")

    // Main 5 Navigation Tabs
    object MainScan : Screen("main_scan")
    object SavedReports : Screen("saved_reports")
    object FirstAid : Screen("first_aid")
    object Maya : Screen("maya")
    object Settings : Screen("settings")

    // Additional Screen destinations
    object Pregnancy : Screen("pregnancy")
    object Emergency : Screen("emergency")
    object WeekDetail : Screen("week_detail/{weekNumber}") {
        fun createRoute(week: Int) = "week_detail/$week"
    }
    object FirstAidDetail : Screen("first_aid_detail/{id}") {
        fun createRoute(id: String) = "first_aid_detail/$id"
    }
    object ReportResult : Screen("report_result")
    object ReportFirstAidPlan : Screen("report_first_aid_plan")
    object Nutrition : Screen("nutrition")
}

data class BottomNavItem(
    val title: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun PreCareNavGraph(
    viewModel: PreCareViewModel,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val appLanguageName by viewModel.appLanguage.collectAsState()
    val appLanguage = SupportedLanguage.fromDisplayName(appLanguageName)

    // Primary 5-tab Navigation: 1. Scan & Analyze, 2. Saved, 3. First Aid, 4. Ask Maya, 5. Settings
    val bottomNavItems = listOf(
        BottomNavItem(PreCareStrings.scanAndOcr(appLanguage), Screen.MainScan.route, Icons.Default.DocumentScanner),
        BottomNavItem(PreCareStrings.saved(appLanguage), Screen.SavedReports.route, Icons.Default.Assignment),
        BottomNavItem(PreCareStrings.firstAid(appLanguage), Screen.FirstAid.route, Icons.Default.Medication),
        BottomNavItem(PreCareStrings.askMaya(appLanguage), Screen.Maya.route, Icons.Default.Chat),
        BottomNavItem(PreCareStrings.settings(appLanguage), Screen.Settings.route, Icons.Default.Person)
    )

    val showBottomBar = currentRoute in listOf(
        Screen.MainScan.route,
        Screen.SavedReports.route,
        Screen.FirstAid.route,
        Screen.Maya.route,
        Screen.Settings.route
    )

    val profile by viewModel.userProfile.collectAsState()

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 4.dp,
                    windowInsets = WindowInsets.navigationBars,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("main_bottom_nav_bar")
                ) {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentRoute == item.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(Screen.MainScan.route) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    modifier = Modifier.size(24.dp),
                                    tint = if (isSelected) PreCarePrimary else PreCareSecondaryText
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) PreCarePrimary else PreCareSecondaryText,
                                    maxLines = 1
                                )
                            },
                            alwaysShowLabel = true,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = PreCarePrimary,
                                unselectedIconColor = PreCareSecondaryText,
                                selectedTextColor = PreCarePrimary,
                                unselectedTextColor = PreCareSecondaryText,
                                indicatorColor = PreCarePrimarySoft
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    onSplashComplete = {
                        val isSetupCompleted = profile?.isSetupCompleted == true
                        if (isSetupCompleted) {
                            navController.navigate(Screen.MainScan.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true }
                            }
                        } else {
                            navController.navigate(Screen.Welcome.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true }
                            }
                        }
                    }
                )
            }

            composable(Screen.Welcome.route) {
                WelcomeScreen(
                    onGetStarted = { navController.navigate(Screen.BasicInfo.route) },
                    onEmergencyHelp = { navController.navigate(Screen.Emergency.route) }
                )
            }

            composable(Screen.BasicInfo.route) {
                BasicInfoScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onContinue = { navController.navigate(Screen.PregnancySetup.route) }
                )
            }

            composable(Screen.PregnancySetup.route) {
                PregnancySetupScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onContinue = { navController.navigate(Screen.EmergencyContact.route) },
                    onSkip = { navController.navigate(Screen.EmergencyContact.route) }
                )
            }

            composable(Screen.EmergencyContact.route) {
                EmergencyContactScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onFinish = {
                        navController.navigate(Screen.MainScan.route) {
                            popUpTo(Screen.Welcome.route) { inclusive = true }
                        }
                    }
                )
            }

            // PRIMARY MAIN SCREEN (Tab 1): Scan & Analyze Report
            composable(Screen.MainScan.route) {
                ReportAnalyzerScreen(
                    viewModel = viewModel,
                    onBack = null,
                    onAnalysisSuccess = {
                        navController.navigate(Screen.ReportResult.route)
                    },
                    onNavigateToFirstAid = { navController.navigate(Screen.FirstAid.route) },
                    onNavigateToMaya = { navController.navigate(Screen.Maya.route) },
                    onNavigateToEmergency = { navController.navigate(Screen.Emergency.route) },
                    onNavigateToSavedReports = { navController.navigate(Screen.SavedReports.route) }
                )
            }

            // TAB 2: Saved Medical Reports (Local Room Storage)
            composable(Screen.SavedReports.route) {
                ReportsScreen(
                    viewModel = viewModel,
                    onBack = null,
                    onNavigateToAnalyze = {
                        navController.navigate(Screen.MainScan.route)
                    },
                    onViewReport = { reportId ->
                        viewModel.loadMedicalReportById(reportId)
                        navController.navigate(Screen.ReportResult.route)
                    }
                )
            }

            // TAB 3: First Aid Center
            composable(Screen.FirstAid.route) {
                FirstAidScreen(
                    viewModel = viewModel,
                    onNavigateToDetail = { id ->
                        navController.navigate(Screen.FirstAidDetail.createRoute(id))
                    },
                    onNavigateToEmergency = { navController.navigate(Screen.Emergency.route) }
                )
            }

            // TAB 4: Ask Maya AI Companion
            composable(Screen.Maya.route) {
                MayaChatScreen(
                    viewModel = viewModel,
                    onBack = null,
                    onNavigateToEmergency = { navController.navigate(Screen.Emergency.route) }
                )
            }

            // TAB 5: Complete Settings & Profile
            composable(Screen.Settings.route) {
                ProfileScreen(
                    viewModel = viewModel,
                    onResetToOnboarding = {
                        navController.navigate(Screen.Welcome.route) {
                            popUpTo(Screen.MainScan.route) { inclusive = true }
                        }
                    },
                    onNavigateToMaya = { navController.navigate(Screen.Maya.route) },
                    onNavigateToEmergency = { navController.navigate(Screen.Emergency.route) }
                )
            }

            // Sub-destinations: Pregnancy Journey
            composable(Screen.Pregnancy.route) {
                PregnancyJourneyScreen(
                    viewModel = viewModel,
                    onSelectWeek = { week ->
                        navController.navigate(Screen.WeekDetail.createRoute(week))
                    }
                )
            }

            // Sub-destinations: Emergency Screen
            composable(Screen.Emergency.route) {
                EmergencyScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            // Sub-destinations: Week Detail
            composable(
                route = Screen.WeekDetail.route,
                arguments = listOf(navArgument("weekNumber") { type = NavType.IntType })
            ) { backStack ->
                val week = backStack.arguments?.getInt("weekNumber") ?: 24
                WeekDetailScreen(
                    weekNumber = week,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            // Sub-destinations: First Aid Detail
            composable(
                route = Screen.FirstAidDetail.route,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStack ->
                val id = backStack.arguments?.getString("id") ?: "bleeding"
                FirstAidDetailScreen(
                    itemId = id,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToEmergency = { navController.navigate(Screen.Emergency.route) }
                )
            }

            // Sub-destinations: Report Result
            composable(Screen.ReportResult.route) {
                ReportResultScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onViewFirstAidPlan = {
                        navController.navigate(Screen.ReportFirstAidPlan.route)
                    },
                    onNavigateToEmergency = { navController.navigate(Screen.Emergency.route) },
                    onNavigateToMaya = { navController.navigate(Screen.Maya.route) },
                    onBookAppointment = { navController.navigate(Screen.Settings.route) }
                )
            }

            // Sub-destinations: Report First-Aid Action Plan
            composable(Screen.ReportFirstAidPlan.route) {
                ReportFirstAidPlanScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToFirstAidTopic = { topicId ->
                        navController.navigate(Screen.FirstAidDetail.createRoute(topicId))
                    },
                    onNavigateToEmergency = { navController.navigate(Screen.Emergency.route) },
                    onNavigateToMaya = { navController.navigate(Screen.Maya.route) }
                )
            }

            // Sub-destinations: Nutrition
            composable(Screen.Nutrition.route) {
                NutritionScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
