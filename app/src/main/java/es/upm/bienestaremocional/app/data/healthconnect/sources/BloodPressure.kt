package es.upm.bienestaremocional.app.data.healthconnect.sources

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.health.connect.client.units.Pressure
import es.upm.bienestaremocional.app.utils.generateTime
import es.upm.bienestaremocional.core.extraction.healthconnect.data.HealthConnectManagerInterface
import es.upm.bienestaremocional.core.extraction.healthconnect.data.HealthConnectSource
import es.upm.bienestaremocional.core.extraction.healthconnect.data.HealthConnectSourceInterface
import java.time.Instant
import kotlin.random.Random

/**
 * Implementation of BloodPressure datasource implementing [HealthConnectSourceInterface]
 * @param healthConnectClient: proportionate HealthConnect's read and write primitives
 * @param healthConnectManager: proportionate HealthConnect's permission primitives
 */


class BloodPressure(private val healthConnectClient: HealthConnectClient,
                    private val healthConnectManager: HealthConnectManagerInterface):
    HealthConnectSource<BloodPressureRecord>(healthConnectClient,healthConnectManager)
{
    companion object
    {
        /**
         * Make demo data
         */
        fun generateDummyData() : List<BloodPressureRecord>
        {

            return List(5)
            { index ->
                val measureTime = generateTime(offsetDays = index.toLong())

                val systolic: Pressure = Pressure.millimetersOfMercury(
                    Random.nextDouble(20.1,199.9))
                val diastolic: Pressure = Pressure.millimetersOfMercury(
                    Random.nextDouble(10.1,179.9))

                val bodyPosition = index % 5
                val measurementLocation = index % 5

                BloodPressureRecord(
                    time = measureTime.toInstant(),
                    zoneOffset = measureTime.offset,
                    systolic = systolic,
                    diastolic = diastolic,
                    bodyPosition = bodyPosition,
                    measurementLocation = measurementLocation,
                )
            }
        }
    }

    override val readPermissions = setOf(
        HealthPermission.createReadPermission(BloodPressureRecord::class))

    override val writePermissions = setOf(
        HealthPermission.createWritePermission(BloodPressureRecord::class))

    override suspend fun readSource(startTime: Instant, endTime: Instant): List<BloodPressureRecord>
    {
        val request = ReadRecordsRequest(
            recordType = BloodPressureRecord::class,
            timeRangeFilter = TimeRangeFilter.between(startTime, endTime),
            ascendingOrder = false
        )
        val items = healthConnectClient.readRecords(request)
        return items.records
    }
}