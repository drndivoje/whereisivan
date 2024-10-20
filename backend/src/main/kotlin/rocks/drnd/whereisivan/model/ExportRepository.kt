package rocks.drnd.whereisivan.model

import java.time.Instant

interface ExportRepository {
    fun export(activity: Activity):ExportActivity
}

data class ExportActivity(val activityId: String, val createdAt:Instant)