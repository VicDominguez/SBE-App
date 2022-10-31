package es.upm.sistemabienestaremocional.navigation

/**
 * Contains all Screens in the app
 * @param route: The route string used for Compose navigation
 */

enum class Screen (val route: String)
{
    MainScreen(route = "main_screen"),
    ErrorScreen(route = "error_screen"),
    HomeScreen(route = "home_screen"),
    PrivacyPolicyScreen(route = "privacy_policy_screen"),
    SleepScreen(route = "sleep_screen")
}