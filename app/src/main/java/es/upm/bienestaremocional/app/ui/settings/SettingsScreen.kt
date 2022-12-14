package es.upm.bienestaremocional.app.ui.settings

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.alorma.compose.settings.storage.base.SettingValueState
import com.alorma.compose.settings.storage.base.rememberBooleanSettingState
import com.alorma.compose.settings.storage.base.rememberIntSettingState
import com.alorma.compose.settings.ui.SettingsList
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.SettingsSwitch
import es.upm.bienestaremocional.R
import es.upm.bienestaremocional.app.MainApplication
import es.upm.bienestaremocional.app.data.settings.ThemeMode
import es.upm.bienestaremocional.app.ui.navigation.MenuEntry
import es.upm.bienestaremocional.app.ui.navigation.Screen
import es.upm.bienestaremocional.app.utils.dynamicColorsSupported
import es.upm.bienestaremocional.app.utils.openForeignActivity
import es.upm.bienestaremocional.app.utils.restartApp
import es.upm.bienestaremocional.core.ui.component.AppBasicScreen
import es.upm.bienestaremocional.core.ui.theme.BienestarEmocionalTheme

@Composable
private fun GroupText(textRes : Int)
{
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.Start)
    {
        Text(text = stringResource(textRes),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary)
    }

}

private fun Modifier.defaultIconModifier() = this.then(padding(all = 2.dp).size(size = 28.dp))


private suspend fun showRestartInfo(snackbarHostState: SnackbarHostState,
                                    message : String,
                                    actionLabel : String,
                                    context: Context)
{
    val result = snackbarHostState.showSnackbar(message = message, actionLabel = actionLabel,
        withDismissAction = true, duration = SnackbarDuration.Long)
    if (result === SnackbarResult.ActionPerformed)
        restartApp(activity = context as Activity)
}

private const val HEALTH_CONNECT_ACTION = "androidx.health.ACTION_HEALTH_CONNECT_SETTINGS"

/**
 * Renders settings menu
 * @param navController: needed for render menu
 * @param language: var that stores the language of the app
 * @param themeMode: var that stores theme setting value
 * @param dynamicColor: var that stores dynamic setting value
 * @param shouldDisplayDynamicOption: boolean to control rendering (or not) dynamic option
 * (option available in Android 12+)
 * @param languageSupportedLabels: labels to show on language setting
 * @param onLanguageChange: callback to react language setting changes
 * @param onThemeChange: callback to react theme setting changes
 * @param onDynamicChange: callback to react dynamic setting changes
 */
@Composable
private fun DrawSettingsScreen(navController: NavController,
                               language: SettingValueState<Int>,
                               themeMode: SettingValueState<Int>,
                               dynamicColor : SettingValueState<Boolean>,
                               shouldDisplayDynamicOption : Boolean,
                               languageSupportedLabels : List<String>,
                               onLanguageChange : @Composable (SettingValueState<Int>) -> Unit,
                               onThemeChange : suspend (SettingValueState<Int>) -> Unit,
                               onDynamicChange : suspend (SettingValueState<Boolean>) -> Unit)
{
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    //avoid undesired launch
    val defaultThemeValue : Int = remember { themeMode.value }
    val defaultDynamicValue : Boolean = remember { dynamicColor.value }
    val defaultLanguage : Int = remember { language.value }

    val restartToApplyChanges = stringResource(id = R.string.restart_apply_changes)
    val restartApplyAllChanges = stringResource(id = R.string.restart_apply_all_changes)
    val actionLabel = stringResource(id = R.string.restart)

    if (themeMode.value != defaultThemeValue)
    {
        LaunchedEffect(themeMode.value)
        {
            onThemeChange(themeMode)
            showRestartInfo(snackbarHostState = snackbarHostState,
                message = restartToApplyChanges,
                actionLabel = actionLabel,
                context = context)
        }
    }

    if (dynamicColor.value != defaultDynamicValue)
    {
        LaunchedEffect(dynamicColor.value)
        {
            onDynamicChange(dynamicColor)
            showRestartInfo(snackbarHostState = snackbarHostState,
                message = restartToApplyChanges,
                actionLabel = actionLabel,
                context = context)
        }
    }

    if (language.value != defaultLanguage)
    {
        onLanguageChange(language)
        LaunchedEffect(language.value)
        {
            showRestartInfo(snackbarHostState = snackbarHostState,
                message = restartApplyAllChanges,
                actionLabel = actionLabel,
                context = context)
        }
    }


    AppBasicScreen(navController = navController,
        entrySelected = MenuEntry.SettingsScreen,
        label = MenuEntry.SettingsScreen.labelId,
        scope = scope,
        snackbarHostState = snackbarHostState
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            GroupText(textRes = R.string.privacy_group)

            SettingsMenuLink(
                icon = { Icon(painter = painterResource(R.drawable.storage),
                    contentDescription = null,
                    modifier = Modifier.defaultIconModifier()) },
                title = { Text(text = stringResource(id = R.string.my_data_label),
                    color = MaterialTheme.colorScheme.secondary) },
                subtitle = { Text(stringResource(id = R.string.my_data_description)) },
                onClick = { navController.navigate(Screen.MyDataScreen.route) },
            )


            SettingsMenuLink(
                icon = { Icon(painter = painterResource(R.drawable.security),
                    contentDescription = null,
                    modifier = Modifier.defaultIconModifier()) },
                title = { Text(text = stringResource(id = R.string.privacy_policy_screen_label),
                    color = MaterialTheme.colorScheme.secondary) },
                subtitle = { Text(stringResource(id = R.string.privacy_policy_screen_description)) },
                onClick = { navController.navigate(Screen.PrivacyPolicyScreen.route) },
            )

            SettingsMenuLink(
                    icon = { Icon(painter = painterResource(R.drawable.health_connect_logo),
                        contentDescription = null,
                        modifier = Modifier.defaultIconModifier(),
                        tint = Color.Unspecified) },
            title = { Text(text = stringResource(id = R.string.health_connect_settings_label),
                color = MaterialTheme.colorScheme.secondary) },
            subtitle = { Text(stringResource(id = R.string.health_connect_settings_description)) },
            onClick = { openForeignActivity(context = context, action = HEALTH_CONNECT_ACTION) },
            )

            Divider(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp))

            GroupText(textRes = R.string.ui_group)

            SettingsList(
                icon = { Icon(painter = painterResource(R.drawable.language),
                    contentDescription = null,
                    modifier = Modifier.defaultIconModifier()) },
                title = { Text(stringResource(R.string.language),
                    color = MaterialTheme.colorScheme.secondary) },
                state = language,
                items = languageSupportedLabels
            )

            if (shouldDisplayDynamicOption)
            {
                SettingsSwitch(
                    icon = { Icon(painter = painterResource(R.drawable.palette),
                        contentDescription = null,
                        modifier = Modifier.defaultIconModifier()) },
                    title = { Text(text = stringResource(R.string.dynamic_colors_label),
                        color = MaterialTheme.colorScheme.secondary) },
                    subtitle = { Text(text = stringResource(R.string.dynamic_colors_description)) },
                    state = dynamicColor
                )
            }

            SettingsList(
                icon = { Icon(painter = painterResource(R.drawable.dark_mode),
                    contentDescription = null,
                    modifier = Modifier.defaultIconModifier()) },
                title = { Text(stringResource(R.string.dark_mode),
                    color = MaterialTheme.colorScheme.secondary) },
                state = themeMode,
                items = ThemeMode.getLabels()
            )

            Divider(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp))

            GroupText(textRes = R.string.misc_group)

            SettingsMenuLink(
                icon = { Icon(painter = painterResource(R.drawable.help_outline),
                    contentDescription = null,
                    modifier = Modifier.defaultIconModifier()) },
                title = { Text(text = stringResource(id = R.string.about_screen_label),
                    color = MaterialTheme.colorScheme.secondary) },
                subtitle = { Text(stringResource(id = R.string.about_screen_description)) },
                onClick = { navController.navigate(Screen.AboutScreen.route) },
            )

            SettingsMenuLink(
                icon = { Icon(painter = painterResource(R.drawable.start),
                    contentDescription = null,
                    modifier = Modifier.defaultIconModifier()) },
                title = { Text(text = stringResource(id = R.string.onboarding_screen_label),
                    color = MaterialTheme.colorScheme.secondary) },
                subtitle = { Text(stringResource(id = R.string.onboarding_screen_description)) },
                onClick = { navController.navigate(Screen.OnboardingScreen.route) },
            )

            SettingsMenuLink(
                icon = { Icon(painter = painterResource(R.drawable.people_alt),
                    contentDescription = null,
                    modifier = Modifier.defaultIconModifier()) },
                title = { Text(text = stringResource(id = R.string.credits_screen_label),
                    color = MaterialTheme.colorScheme.secondary) },
                subtitle = { Text(stringResource(id = R.string.credits_screen_description)) },
                onClick = { navController.navigate(Screen.CreditsScreen.route) },
            )
        }
    }
}

/**
 * Public function to read SettingsScreen using [SettingsViewModel]
 */
@Composable
fun SettingsScreen(navController: NavController)
{
    val viewModel : SettingsViewModel = viewModel(factory = SettingsViewModel.Factory)
    val language = viewModel.loadLanguage()
    val themeMode = viewModel.loadDarkMode()
    val dynamicColor = viewModel.loadDynamicColors()
    val languageManager = MainApplication.languageManager

    DrawSettingsScreen(
        navController = navController,
        language = language,
        themeMode = themeMode,
        dynamicColor = dynamicColor,
        shouldDisplayDynamicOption = dynamicColorsSupported(),
        languageSupportedLabels = languageManager.getSupportedLocalesLabel(),
        onThemeChange = {theme -> viewModel.changeDarkMode(theme)},
        onDynamicChange = {dynamic -> viewModel.changeDynamicColors(dynamic)},
        onLanguageChange = { viewModel.changeLanguage(LocalContext.current,it)}
    )
}

@Preview(
    showBackground = true,
    group = "Light Theme"
)
@Composable
fun SettingsScreenNoDynamicPreview()
{
    val navController = rememberNavController()

    BienestarEmocionalTheme()
    {
        DrawSettingsScreen(
            navController = navController,
            language = rememberIntSettingState(-1),
            themeMode = rememberIntSettingState(-1),
            dynamicColor = rememberBooleanSettingState(true),
            shouldDisplayDynamicOption = false,
            languageSupportedLabels = listOf("Espa??ol","English"),
            onThemeChange = {},
            onDynamicChange = {},
            onLanguageChange = {}
        )
    }
}

@Preview(
    showBackground = true,
    group = "Dark Theme"
)
@Composable
fun SettingsScreenNoDynamicPreviewDarkTheme()
{
    val navController = rememberNavController()

    BienestarEmocionalTheme(darkTheme = true)
    {
        DrawSettingsScreen(
            navController = navController,
            language = rememberIntSettingState(-1),
            themeMode = rememberIntSettingState(-1),
            dynamicColor = rememberBooleanSettingState(true),
            shouldDisplayDynamicOption = false,
            languageSupportedLabels = listOf("Espa??ol","English"),
            onThemeChange = {},
            onDynamicChange = {},
            onLanguageChange = {}
        )
    }
}

@Preview(
    showBackground = true,
    group = "Light Theme"
)
@Composable
fun SettingsScreenPreview()
{
    val navController = rememberNavController()

    BienestarEmocionalTheme()
    {
        DrawSettingsScreen(
            navController = navController,
            language = rememberIntSettingState(-1),
            themeMode = rememberIntSettingState(-1),
            dynamicColor = rememberBooleanSettingState(true),
            shouldDisplayDynamicOption = true,
            languageSupportedLabels = listOf("Espa??ol","English"),
            onThemeChange = {},
            onDynamicChange = {},
            onLanguageChange = {}
        )
    }
}

@Preview(
    showBackground = true,
    group = "Dark Theme"
)
@Composable
fun SettingsScreenPreviewDarkTheme()
{
    val navController = rememberNavController()

    BienestarEmocionalTheme(darkTheme = true)
    {
        DrawSettingsScreen(
            navController = navController,
            language = rememberIntSettingState(-1),
            themeMode = rememberIntSettingState(-1),
            dynamicColor = rememberBooleanSettingState(true),
            shouldDisplayDynamicOption = true,
            languageSupportedLabels = listOf("Espa??ol","English"),
            onThemeChange = {},
            onDynamicChange = {},
            onLanguageChange = {}
        )
    }
}