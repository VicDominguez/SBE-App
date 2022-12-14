package es.upm.bienestaremocional.app.data.healthconnect.sources

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.health.connect.client.units.Energy
import es.upm.bienestaremocional.app.utils.generateInterval
import es.upm.bienestaremocional.core.extraction.healthconnect.data.HealthConnectManagerInterface
import es.upm.bienestaremocional.core.extraction.healthconnect.data.HealthConnectSource
import es.upm.bienestaremocional.core.extraction.healthconnect.data.HealthConnectSourceInterface
import java.time.Instant
import kotlin.random.Random

/**
 * Implementation of ActiveCaloriesBurned datasource implementing [HealthConnectSourceInterface]
 * @param healthConnectClient: proportionate HealthConnect's read and write primitives
 * @param healthConnectManager: proportionate HealthConnect's permission primitives
 */


class ActiveCaloriesBurned(private val healthConnectClient: HealthConnectClient,
                           private val healthConnectManager: HealthConnectManagerInterface):
    HealthConnectSource<ActiveCaloriesBurnedRecord>(healthConnectClient,healthConnectManager)
{
    companion object
    {
        /**
         * Make demo data
         */
        fun generateDummyData() : List<ActiveCaloriesBurnedRecord>
        {
            return List(5)
            { index ->
                val (init,end) = generateInterval(offsetDays = index.toLong())
                val energy = Energy.kilocalories(Random.nextDouble(1000.0,5000.0))
                ActiveCaloriesBurnedRecord(
                    startTime = init.toInstant(),
                    startZoneOffset = init.offset,
                    endTime = end.toInstant(),
                    endZoneOffset = end.offset,
                    energy = energy
                )
            }
        }
    }

    override val readPermissions = setOf(
        HealthPermission.createReadPermission(ActiveCaloriesBurnedRecord::class))

    override val writePermissions = setOf(
        HealthPermission.createWritePermission(ActiveCaloriesBurnedRecord::class))

    override suspend fun readSource(startTime: Instant, endTime: Instant):
            List<ActiveCaloriesBurnedRecord>
    {
        val request = ReadRecordsRequest(
            recordType = ActiveCaloriesBurnedRecord::class,
            timeRangeFilter = TimeRangeFilter.between(startTime, endTime),
            ascendingOrder = false
        )
        val items = healthConnectClient.readRecords(request)
        return items.records
    }
}