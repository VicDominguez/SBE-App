package es.upm.sistemabienestaremocional.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.upm.sistemabienestaremocional.R
import es.upm.sistemabienestaremocional.theme.SistemaBienestarEmocionalTheme

/**
 * Renders settings menu
 */

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsScreen(onBackClick : () -> Unit)
{
    DetailScreen(
        idTitle = R.string.settings_screen_label,
        onBackClick = onBackClick)
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = 10.dp
            )
            {
                Column(
                    modifier = Modifier.padding(15.dp)
                )
                {
                    Text("Settings placeholder")
                }
            }
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview()
{
    SistemaBienestarEmocionalTheme {
        SettingsScreen(onBackClick = {})
    }
}