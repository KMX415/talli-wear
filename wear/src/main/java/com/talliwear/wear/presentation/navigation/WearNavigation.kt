package com.talliwear.wear.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.talliwear.wear.presentation.screens.ActivityDetailScreen
import com.talliwear.wear.presentation.screens.ActivityListScreen
import com.talliwear.wear.presentation.screens.HomeScreen
import com.talliwear.wear.presentation.screens.QuickActionsScreen
import com.talliwear.wear.presentation.screens.TimerScreen

@Composable
fun WearNavigation() {
    val navController = rememberSwipeDismissableNavController()
    
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onNavigateToQuickActions = { navController.navigate("quick_actions") },
                onNavigateToActivityList = { navController.navigate("activity_list") },
                onNavigateToTimer = { navController.navigate("timer") }
            )
        }
        
        composable("quick_actions") {
            QuickActionsScreen(
                onActivityLogged = { navController.popBackStack() }
            )
        }
        
        composable("activity_list") {
            ActivityListScreen(
                onActivityClick = { activityId ->
                    navController.navigate("activity_detail/$activityId")
                }
            )
        }
        
        composable(
            "activity_detail/{activityId}",
            arguments = listOf(navArgument("activityId") { type = NavType.StringType })
        ) { backStackEntry ->
            val activityId = backStackEntry.arguments?.getString("activityId") ?: ""
            ActivityDetailScreen(
                activityId = activityId,
                onBack = { navController.popBackStack() }
            )
        }
        
        composable("timer") {
            TimerScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
} 