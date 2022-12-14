package es.upm.bienestaremocional.app.data.settings

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import es.upm.bienestaremocional.R

/**
 * Contains the options of theming: [LIGHT_MODE], [DARK_MODE] and [DEFAULT_MODE]
 */
enum class ThemeMode(val labelRes: Int, val key: String)
{
    LIGHT_MODE(R.string.light_mode_label, "light"),
    DARK_MODE(R.string.dark_mode_label, "dark"),
    DEFAULT_MODE(R.string.default_mode_label,"default");

    /**
     * Retrieves boolean indicating if option is dark or not
     */
    @Composable
    fun themeIsDark(): Boolean =
        when(this)
        {
            LIGHT_MODE -> false
            DARK_MODE -> true
            DEFAULT_MODE -> isSystemInDarkTheme()
        }

    companion object
    {
        /**
         * Get all [ThemeMode] possible values
         * @return [List] of [ThemeMode] with the values
         */
        fun get(): List<ThemeMode> = values().asList()

        /**
         * Get all labels of the [ThemeMode] possible values
         * @return [List] of [String] with the labels
         */
        @Composable
        fun getLabels(): List<String> = get().map { stringResource(id = it.labelRes)  }
    }
}