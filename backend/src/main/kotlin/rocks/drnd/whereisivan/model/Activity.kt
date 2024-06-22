package rocks.drnd.whereisivan.model

import java.time.Instant

class Activity constructor(startTime: Instant) {
    val activityId = startTime.hashCode()
    private var status = ActivityStatus.INITIATED

    fun start() {
        status = ActivityStatus.STARTED
    }

    fun stop() {
        status = ActivityStatus.STOPPED
    }
}