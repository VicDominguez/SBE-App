package es.upm.bienestaremocional.app.data.healthconnect.sources

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.health.connect.client.units.Length
import es.upm.bienestaremocional.app.utils.generateInterval
import es.upm.bienestaremocional.core.extraction.healthconnect.data.HealthConnectManagerInterface
import es.upm.bienestaremocional.core.extraction.healthconnect.data.HealthConnectSource
import es.upm.bienestaremocional.core.extraction.healthconnect.data.HealthConnectSourceInterface
import java.time.Instant
import kotlin.random.Random

/**
 * Implementation of Distance datasource implementing [HealthConnectSourceInterface]
 * @param healthConnectClient: proportionate HealthConnect's read and write primitives
 * @param healthConnectManager: proportionate HealthConnect's permission primitives
 */


class Distance(private val healthConnectClient: HealthConnectClient,
               private val healthConnectManager: HealthConnectManagerInterface):
    HealthConnectSource<DistanceRecord>(healthConnectClient,healthConnectManager)
{
    companion object
    {
        /**
         * Make demo data
         */
        fun generateDummyData() : List<DistanceRecord>
        {
            return List(5)
            { index ->
                val (init, end) = generateInterval(offsetDays = index.toLong())

                val distance = Length.kilometers(Random.nextDouble(0.0,10.0))

                DistanceRecord(
                    startTime = init.toInstant(),
                    startZoneOffset = init.offset,
                    endTime = end.toInstant(),
                    endZoneOffset = end.offset,
                    distance = distance
                )
            }
        }
    }

    override val readPermissions = setOf(
        HealthPermission.createReadPermission(DistanceRecord::class))

    override val writePermissions = setOf(
        HealthPermission.createWritePermission(DistanceRecord::class))

    override suspend fun readSource(startTime: Instant, endTime: Instant): List<DistanceRecord>
    {
        val request = ReadRecordsRequest(
            recordType = DistanceRecord::class,
            timeRangeFilter = TimeRangeFilter.between(startTime, endTime),
            ascendingOrder = false
        )
        val items = healthConnectClient.readRecords(request)
        return items.records
    }
}