import model.Priority
import model.TaskManagerFullException
import java.util.*
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class PriorityTaskManagerTest {
    @Test
    fun addingProcessOverLimitRemovesBasedOnPriority() {
        val taskManager = PriorityTaskManager(6)
        val pid1 = taskManager.addProcess(Priority.MEDIUM)
        val pid2 = taskManager.addProcess(Priority.MEDIUM)
        val pid3 = taskManager.addProcess(Priority.HIGH)
        val pid4 = taskManager.addProcess(Priority.HIGH)
        val pid5 = taskManager.addProcess(Priority.LOW)
        val pid6 = taskManager.addProcess(Priority.LOW)

        var expectedPids = mutableListOf(pid6, pid5, pid4, pid3, pid2, pid1)
        var listing = taskManager.list(SortType.TIME)
        assertContentEquals(expectedPids, listing.map { it.pid })

        val pid7 = taskManager.addProcess(Priority.HIGH)

        expectedPids = mutableListOf(pid7, pid6, pid4, pid3, pid2, pid1)
        listing = taskManager.list(SortType.TIME)
        assertContentEquals(expectedPids, listing.map { it.pid })

        val pid8 = taskManager.addProcess(Priority.MEDIUM)

        expectedPids = mutableListOf(pid8, pid7, pid4, pid3, pid2, pid1)
        listing = taskManager.list(SortType.TIME)
        assertContentEquals(expectedPids, listing.map { it.pid })
        assertEquals(3, Collections.frequency(listing.map { it.priority }, Priority.HIGH))
        assertEquals(3, Collections.frequency(listing.map { it.priority }, Priority.MEDIUM))

        val pid9 = taskManager.addProcess(Priority.HIGH)

        expectedPids = mutableListOf(pid9, pid8, pid7, pid4, pid3, pid2)
        listing = taskManager.list(SortType.TIME)
        assertContentEquals(expectedPids, listing.map { it.pid })
        assertEquals(4, Collections.frequency(listing.map { it.priority }, Priority.HIGH))
        assertEquals(2, Collections.frequency(listing.map { it.priority }, Priority.MEDIUM))
    }

    @Test(expected = TaskManagerFullException::class)
    fun errorOnNoLowerPriorityFound() {
        val taskManager = PriorityTaskManager(2)
        taskManager.addProcess(Priority.HIGH)
        taskManager.addProcess(Priority.HIGH)
        taskManager.addProcess(Priority.HIGH)
    }
}