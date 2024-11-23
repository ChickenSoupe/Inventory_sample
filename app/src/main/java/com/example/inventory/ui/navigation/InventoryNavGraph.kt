// InventoryNavHost.kt
package com.example.inventory.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.inventory.AuthViewModel
import com.example.inventory.ui.*
import com.example.inventory.ui.home.HomeDestination
import com.example.inventory.ui.home.HomeScreen
import com.example.inventory.ui.item.*
import com.example.inventory.ui.login.LoginDestination
import com.example.inventory.ui.login.LoginScreen
import com.example.inventory.ui.signup.SignUpDestination
import com.example.inventory.ui.signup.SignUpScreen

@Composable
fun InventoryNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel()
) {
    val startDestination = if (authViewModel.isLoggedIn.value) {
        HomeDestination.route
    } else {
        LoginDestination.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Login Screen
        composable(route = LoginDestination.route) {
            LoginScreen(
                onLoginSuccess = {
                    authViewModel.loginUser()
                    navController.navigate(HomeDestination.route) {
                        popUpTo(LoginDestination.route) { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate(SignUpDestination.route)
                }
            )
        }

        composable(route = SignUpDestination.route) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(LoginDestination.route) {
                        popUpTo(SignUpDestination.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = HomeDestination.route) {
            if (authViewModel.isLoggedIn.value) {
                HomeScreen(
                    navigateToItemEntry = { navController.navigate(ItemEntryDestination.route) },
                    navigateToItemUpdate = { itemId ->
                        navController.navigate("${ItemDetailsDestination.route}/$itemId")
                    },
//                    onLogout = {
//                        authViewModel.logoutUser()
//                        navController.navigate(LoginDestination.route) {
//                            popUpTo(HomeDestination.route) { inclusive = true }
//                        }
//                    }
                )
            } else {
                navController.navigate(LoginDestination.route) {
                    popUpTo(HomeDestination.route) { inclusive = true }
                }
            }
        }

        // Item Entry Screen
        composable(route = ItemEntryDestination.route) {
            if (authViewModel.isLoggedIn.value) {
                ItemEntryScreen(
                    navigateBack = { navController.popBackStack() },
                    onNavigateUp = { navController.navigateUp() }
                )
            } else {
                navController.navigate(LoginDestination.route) {
                    popUpTo(ItemEntryDestination.route) { inclusive = true }
                }
            }
        }

        composable(
            route = ItemDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemDetailsDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            if (authViewModel.isLoggedIn.value) {
                ItemDetailsScreen(
                    navigateToEditItem = { itemId ->
                        navController.navigate("${ItemEditDestination.route}/$itemId")
                    },
                    navigateBack = { navController.navigateUp() }
                )
            } else {
                navController.navigate(LoginDestination.route) {
                    popUpTo(ItemDetailsDestination.route) { inclusive = true }
                }
            }
        }

        composable(
            route = ItemEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemEditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            if (authViewModel.isLoggedIn.value) {
                ItemEditScreen(
                    navigateBack = { navController.popBackStack() },
                    onNavigateUp = { navController.navigateUp() }
                )
            } else {
                navController.navigate(LoginDestination.route) {
                    popUpTo(ItemEditDestination.route) { inclusive = true }
                }
            }
        }
    }
}
