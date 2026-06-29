package rocks.drnd.whereisivan.impl

import junit.framework.TestCase.assertEquals
import rocks.drnd.whereisivan.model.Activity
import java.time.Instant
import kotlin.test.Test

class InMemoryActivityRepositoryTest {

    @Test
    fun shouldSaveActivity() {
        val inMemoryActivityRepository = InMemoryActivityRepository()
        val activity = Activity(Instant.now())
        val savedActivity = inMemoryActivityRepository.save(activity)

        val retrievedActivity = inMemoryActivityRepository.get(savedActivity.activityId)
        assertEquals(retrievedActivity, savedActivity)
    }

    @Test
    fun shouldRemoveStoppedActivity() {
        val inMemoryActivityRepository = InMemoryActivityRepository()
        val activity = Activity(Instant.now())
        val savedActivity = inMemoryActivityRepository.save(activity)

        savedActivity.stop()
        inMemoryActivityRepository.save(savedActivity)

        val retrievedActivity = inMemoryActivityRepository.get(savedActivity.activityId)
        assertEquals(retrievedActivity, null)
    }
}