import model.Priority
import kotlin.test.Test
import kotlin.test.assertContentEquals

internal class FifoTaskManagerTest {
    @Test
    fun addingProcessOverLimitRemovesOldest() {
        val taskManager = FifoTaskManager(3)
        val pid1 = taskManager.addProcess(Priority.LOW)
        val pid2 = taskManager.addProcess(Priority.LOW)
        val pid3 = taskManager.addProcess(Priority.LOW)

        var expectedPids = mutableListOf(pid3, pid2, pid1)
        var listing = taskManager.list(SortType.TIME)
        assertContentEquals(expectedPids, listing.map { it.pid })

        val pid4 = taskManager.addProcess(Priority.LOW)

        expectedPids = mutableListOf(pid4, pid3, pid2)
        listing = taskManager.list(SortType.TIME)
        assertContentEquals(expectedPids, listing.map { it.pid })

        val pid5 = taskManager.addProcess(Priority.LOW)

        expectedPids = mutableListOf(pid5, pid4, pid3)
        listing = taskManager.list(SortType.TIME)
        assertContentEquals(expectedPids, listing.map { it.pid })

        val pid6 = taskManager.addProcess(Priority.LOW)

        expectedPids = mutableListOf(pid6, pid5, pid4)
        listing = taskManager.list(SortType.TIME)
        assertContentEquals(expectedPids, listing.map { it.pid })

    }
}