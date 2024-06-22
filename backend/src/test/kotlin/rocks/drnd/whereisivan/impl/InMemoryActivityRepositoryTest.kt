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
        val savedActivity = inMemoryActivityRepository.saveActivity(activity)

        val retrievedActivity = inMemoryActivityRepository.getActivity(savedActivity.activityId)
        assertEquals(retrievedActivity, savedActivity)
    }
}