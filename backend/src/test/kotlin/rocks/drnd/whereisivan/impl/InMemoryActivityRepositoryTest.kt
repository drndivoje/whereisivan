package rocks.drnd.whereisivan.impl

import junit.framework.TestCase.assertEquals
import rocks.drnd.whereisivan.data.InMemoryActivityRepository
import rocks.drnd.whereisivan.model.Activity
import kotlin.test.Test
import kotlin.time.Clock

class InMemoryActivityRepositoryTest {

    @Test
    fun shouldSaveActivity() {
        val inMemoryActivityRepository = InMemoryActivityRepository()
        val activity = Activity(Clock.System.now())
        val savedActivity = inMemoryActivityRepository.save(activity)

        val retrievedActivity = inMemoryActivityRepository.get(savedActivity.activityId)
        assertEquals(retrievedActivity, savedActivity)
    }

    @Test
    fun shouldRemoveStoppedActivity() {
        val inMemoryActivityRepository = InMemoryActivityRepository()
        val activity = Activity(Clock.System.now())
        val savedActivity = inMemoryActivityRepository.save(activity)

        savedActivity.stop()
        inMemoryActivityRepository.save(savedActivity)

        val retrievedActivity = inMemoryActivityRepository.get(savedActivity.activityId)
        assertEquals(retrievedActivity, null)
    }
}