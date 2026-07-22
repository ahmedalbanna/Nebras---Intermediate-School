package com.example.ui.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.screens.ConceptExplainScreen
import com.example.ui.screens.EssayCorrectorScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LessonSummarizerScreen
import com.example.ui.screens.MathPhysicsSolverScreen
import com.example.ui.screens.QuizGeneratorScreen
import com.example.ui.screens.SavedLibraryScreen
import com.example.ui.viewmodel.MainViewModel

import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Science
import com.example.ui.screens.InteractiveLabScreen
import com.example.ui.screens.StudyPlanScreen

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Screen("home", "الرئيسية", Icons.Default.Home)
    object StudyPlan : Screen("study_plan", "الجدول", Icons.Default.DateRange)
    object Concept : Screen("concept", "المفاهيم", Icons.Default.Lightbulb)
    object MathSolver : Screen("math_solver", "المسائل", Icons.Default.Calculate)
    object Summarizer : Screen("summarizer", "الملخصات", Icons.Default.Book)
    object Essay : Screen("essay", "التعبير", Icons.Default.Create)
    object Quiz : Screen("quiz", "الاختبارات", Icons.Default.Quiz)
    object InteractiveLab : Screen("interactive_lab", "المختبر", Icons.Default.Science)
    object Saved : Screen("saved", "المحفوظات", Icons.Default.Bookmark)
}

@Composable
fun AppNavigation(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val messageState by viewModel.messageState.collectAsState()

    LaunchedEffect(messageState) {
        messageState?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }
    }

    val bottomNavItems = listOf(
        Screen.Home,
        Screen.StudyPlan,
        Screen.Concept,
        Screen.MathSolver,
        Screen.Summarizer,
        Screen.Saved
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { screen ->
                    val isSelected = currentRoute == screen.route ||
                            (screen == Screen.Concept && currentRoute?.startsWith("concept") == true)
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = isSelected,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToConcept = { subject ->
                        navController.navigate("concept?subject=$subject")
                    },
                    onNavigateToMathSolver = {
                        navController.navigate(Screen.MathSolver.route)
                    },
                    onNavigateToSummarizer = {
                        navController.navigate(Screen.Summarizer.route)
                    },
                    onNavigateToEssayCorrector = {
                        navController.navigate(Screen.Essay.route)
                    },
                    onNavigateToQuiz = {
                        navController.navigate(Screen.Quiz.route)
                    },
                    onNavigateToSaved = {
                        navController.navigate(Screen.Saved.route)
                    },
                    onNavigateToStudyPlan = {
                        navController.navigate(Screen.StudyPlan.route)
                    },
                    onNavigateToInteractiveLab = {
                        navController.navigate(Screen.InteractiveLab.route)
                    }
                )
            }

            composable(Screen.StudyPlan.route) {
                StudyPlanScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = "concept?subject={subject}",
                arguments = listOf(navArgument("subject") {
                    type = NavType.StringType
                    defaultValue = "علوم عامة"
                })
            ) { backStackEntry ->
                val subjectArg = backStackEntry.arguments?.getString("subject") ?: "علوم عامة"
                ConceptExplainScreen(
                    viewModel = viewModel,
                    initialSubject = subjectArg,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Concept.route) {
                ConceptExplainScreen(
                    viewModel = viewModel,
                    initialSubject = "علوم عامة",
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.MathSolver.route) {
                MathPhysicsSolverScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Summarizer.route) {
                LessonSummarizerScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Essay.route) {
                EssayCorrectorScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Quiz.route) {
                QuizGeneratorScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.InteractiveLab.route) {
                InteractiveLabScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Saved.route) {
                SavedLibraryScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
