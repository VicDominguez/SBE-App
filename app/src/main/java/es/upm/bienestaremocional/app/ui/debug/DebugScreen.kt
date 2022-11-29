package es.upm.bienestaremocional.app.ui.debug

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.records.HeartRateRecord
import androidx.lifecycle.viewmodel.compose.viewModel
import es.upm.bienestaremocional.R
import es.upm.bienestaremocional.app.data.healthconnect.types.SleepSessionData
import es.upm.bienestaremocional.app.ui.debug.component.Display
import es.upm.bienestaremocional.core.ui.component.DrawHealthConnectScreen
import es.upm.bienestaremocional.core.ui.component.ViewModelData

@Suppress("UNCHECKED_CAST")
@Composable
private fun DrawDebugScreen(sleepViewModelData: ViewModelData,
                            heartRateViewModelData: ViewModelData,
                            onError: (Throwable?) -> Unit = {})
{
    var sleepExpanded by remember { mutableStateOf(false) }
    var heartRateExpanded by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize())
    {
        Column(modifier = Modifier.fillMaxWidth())
        {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { sleepExpanded = !sleepExpanded },
                text = stringResource(id = R.string.sleep),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary)
            if (sleepExpanded)
            {
                DrawHealthConnectScreen(viewModelData = sleepViewModelData,
                    onEmptyData = { Text(text=stringResource(id = R.string.empty_data)) },
                    onDisplayData = {
                        val data = sleepViewModelData.data as List<SleepSessionData>
                        data.forEach {
                            item {
                                it.Display()
                                Spacer(Modifier.height(16.dp))
                            }
                        }
                    },
                    onError = onError)
            }
            Text(modifier = Modifier
                .fillMaxWidth()
                .clickable { heartRateExpanded = !heartRateExpanded },
                text = stringResource(id = R.string.heart_rate),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary)
            if (heartRateExpanded)
            {
                DrawHealthConnectScreen(viewModelData = heartRateViewModelData,
                    onEmptyData = { Text(text = stringResource(id = R.string.empty_data)) },
                    onDisplayData = {
                        val data = heartRateViewModelData.data as List<HeartRateRecord>
                        data.forEach {
                            item {
                                it.Display()
                                Spacer(Modifier.height(16.dp))
                            }
                        }
                    },
                    onError = onError)
            }
        }
    }
}

@Composable
fun DebugScreen(onError: (Throwable?) -> Unit = {})
{
    val debugViewModel : DebugViewModel = viewModel(factory = DebugViewModel.Factory)

    DrawDebugScreen(
        sleepViewModelData = debugViewModel.sleepSessionViewModel.getViewModelData(),
        heartRateViewModelData = debugViewModel.heartRateViewModel.getViewModelData(),
        onError = onError)
}