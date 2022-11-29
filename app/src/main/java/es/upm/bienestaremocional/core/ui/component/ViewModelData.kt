package es.upm.bienestaremocional.core.ui.component

import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.Record
import es.upm.bienestaremocional.core.extraction.healthconnect.ui.UiState

data class ViewModelData(
    val data: List<Record>,
    val uiState: UiState,
    val permissions: Set<HealthPermission>,
    val onPermissionsResult : () -> Unit,
    val onRequestPermissions : (Set<HealthPermission>) -> Unit,
    val onWrite : () -> Unit
)