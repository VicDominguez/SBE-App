package es.upm.bienestaremocional.app.ui.healthconnect.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.records.SleepStageRecord
import androidx.health.connect.client.records.SleepStageRecord.Companion.STAGE_TYPE_AWAKE
import androidx.health.connect.client.records.SleepStageRecord.Companion.STAGE_TYPE_DEEP
import androidx.health.connect.client.records.SleepStageRecord.Companion.STAGE_TYPE_LIGHT
import androidx.health.connect.client.records.SleepStageRecord.Companion.STAGE_TYPE_OUT_OF_BED
import androidx.health.connect.client.records.SleepStageRecord.Companion.STAGE_TYPE_REM
import androidx.health.connect.client.records.SleepStageRecord.Companion.STAGE_TYPE_SLEEPING
import androidx.health.connect.client.records.SleepStageRecord.Companion.STAGE_TYPE_UNKNOWN
import es.upm.bienestaremocional.R
import es.upm.bienestaremocional.app.data.healthconnect.sources.Sleep
import es.upm.bienestaremocional.app.data.healthconnect.types.SleepSessionData
import es.upm.bienestaremocional.app.ui.component.SeriesDateTimeHeading
import es.upm.bienestaremocional.core.extraction.healthconnect.data.formatDisplayTimeStartEnd
import es.upm.bienestaremocional.core.extraction.healthconnect.data.formatHoursMinutes
import es.upm.bienestaremocional.core.ui.component.BasicCard
import es.upm.bienestaremocional.core.ui.component.DrawPair
import es.upm.bienestaremocional.core.ui.responsive.WindowSize
import es.upm.bienestaremocional.core.ui.theme.BienestarEmocionalTheme

@Composable
fun SleepSessionData.Display(windowSize: WindowSize)
{
    BasicCard {
        SeriesDateTimeHeading(
            start = startTime,
            startZoneOffset = startZoneOffset,
            end = endTime,
            endZoneOffset = endZoneOffset
        )
        title?.let {
            DrawPair(key = stringResource(R.string.title), value = it)
        }
        notes?.let {
            DrawPair(key = stringResource(R.string.notes), value = it)
        }

        duration?.let {
            val formattedDuration = duration.formatHoursMinutes()
            DrawPair(key = stringResource(R.string.duration), value = formattedDuration)
        }

        Text(text = stringResource(R.string.sleep_stages),color = MaterialTheme.colorScheme.onSurface)
        stages.forEach{it.Display()}

        metadata.Display(windowSize)
    }
}

@Composable
fun SleepStageRecord.Display()
{
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp))
    {
        val intervalLabel = formatDisplayTimeStartEnd(
            startTime, startZoneOffset, endTime, endZoneOffset)

        Text(modifier = Modifier.weight(1f),
            text = intervalLabel,
            color = MaterialTheme.colorScheme.primary)
        Text(modifier = Modifier.weight(1f),
            text = decode(),
            color = MaterialTheme.colorScheme.tertiary)
    }
}

@Composable
fun SleepStageRecord.decode(): String =
    when(stage)
    {
        STAGE_TYPE_UNKNOWN -> stringResource(R.string.unknown)
        STAGE_TYPE_AWAKE -> stringResource(R.string.awake)
        STAGE_TYPE_SLEEPING -> stringResource(R.string.sleeping)
        STAGE_TYPE_OUT_OF_BED -> stringResource(R.string.out_of_bed)
        STAGE_TYPE_LIGHT -> stringResource(R.string.light)
        STAGE_TYPE_DEEP -> stringResource(R.string.deep)
        STAGE_TYPE_REM -> stringResource(R.string.rem)
        else -> stringResource(R.string.unknown)
    }

@Preview(group = "Light Theme")
@Composable
fun SleepSessionDataDisplayPreview()
{
    val sleepSessionData = Sleep.generateDummyData()[0]
    BienestarEmocionalTheme {
        sleepSessionData.Display(windowSize = WindowSize.COMPACT)
    }
}

@Preview(group = "Light Theme")
@Composable
fun SleepSessionDataDisplayPreviewDarkTheme()
{
    val sleepSessionData = Sleep.generateDummyData()[0]
    BienestarEmocionalTheme(darkTheme = true) {
        sleepSessionData.Display(windowSize = WindowSize.COMPACT)
    }
}

@Preview(group = "Light Theme")
@Composable
fun SleepSessionDataDisplayLargeScreenPreview()
{
    val sleepSessionData = Sleep.generateDummyData()[0]
    BienestarEmocionalTheme {
        sleepSessionData.Display(windowSize = WindowSize.MEDIUM)
    }
}

@Preview(group = "Dark Theme")
@Composable
fun SleepSessionDataDisplayLargeScreenPreviewDarkTheme()
{
    val sleepSessionData = Sleep.generateDummyData()[0]
    BienestarEmocionalTheme(darkTheme = true) {
        sleepSessionData.Display(windowSize = WindowSize.MEDIUM)
    }
}

@Preview(group = "Light Theme")
@Composable
fun SleepStageDisplayPreview()
{
    val stage = Sleep.generateDummyData()[0].stages[0]
    BienestarEmocionalTheme {
        Surface {
            stage.Display()
        }
    }
}

@Preview(group = "Dark Theme")
@Composable
fun SleepStageDisplayPreviewDarkTheme()
{
    val stage = Sleep.generateDummyData()[0].stages[0]
    BienestarEmocionalTheme(darkTheme = true) {
        Surface {
            stage.Display()
        }
    }
}