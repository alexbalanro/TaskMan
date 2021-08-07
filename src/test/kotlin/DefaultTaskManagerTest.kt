import model.PidNotFoundException
import model.Priority
import model.TaskManagerFullException
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertTrue

internal class DefaultTaskManagerTest {

    @Test
    fun processesAreAddedAndListedProperly() {
        val taskManager = DefaultTaskManager(3)

        val firstPid = taskManager.addProcess(Priority.LOW)
        val secondPid = taskManager.addProcess(Priority.MEDIUM)
        val thirdPid = taskManager.addProcess(Priority.HIGH)

        val listing = taskManager.list(SortType.TIME)
        assert(listing.size == 3)
        assert(listing[0].pid == thirdPid && listing[0].priority == Priority.HIGH)
        assert(listing[1].pid == secondPid && listing[1].priority == Priority.MEDIUM)
        assert(listing[2].pid == firstPid && listing[2].priority == Priority.LOW)

        val sortedPids = mutableListOf(firstPid, secondPid, thirdPid).sorted()
        val sortedListing = taskManager.list(SortType.PID)
        assertContentEquals(sortedPids, sortedListing.map { it.pid })
    }

    @Test
    fun listingByPriorityWorksProperly() {
        val taskManager = DefaultTaskManager(6)

        val pid1 = taskManager.addProcess(Priority.LOW)
        val pid2 = taskManager.addProcess(Priority.MEDIUM)
        val pid3 = taskManager.addProcess(Priority.HIGH)
        val pid4 = taskManager.addProcess(Priority.LOW)
        val pid5 = taskManager.addProcess(Priority.MEDIUM)
        val pid6 = taskManager.addProcess(Priority.HIGH)

        val expectedOrder = mutableListOf(pid3, pid6, pid2, pid5, pid1, pid4)
        val sortedListing = taskManager.list(SortType.PRIORITY)
        assertContentEquals(expectedOrder, sortedListing.map { it.pid })
    }

    @Test(expected = TaskManagerFullException::class)
    fun errorOnLimitReached() {
        val taskManager = DefaultTaskManager(2)
        taskManager.addProcess(Priority.LOW)
        taskManager.addProcess(Priority.LOW)
        taskManager.addProcess(Priority.LOW)
    }

    @Test(expected = PidNotFoundException::class)
    fun errorOnAttemptingToKillUnknownPid() {
        val taskManager = DefaultTaskManager(2)
        taskManager.killProcess(1)
    }

    @Test
    fun killPidWorksProperly() {
        val taskManager = DefaultTaskManager(3)
        var pid1 = taskManager.addProcess(Priority.LOW)
        var pid2 = taskManager.addProcess(Priority.LOW)
        var pid3 = taskManager.addProcess(Priority.LOW)

        taskManager.killProcess(pid1)

        val expectedOrder = mutableListOf(pid3, pid2)
        val sortedListing = taskManager.list(SortType.TIME)
        assertContentEquals(expectedOrder, sortedListing.map { it.pid })
    }

    @Test
    fun killGroupWorksProperly() {
        val taskManager = DefaultTaskManager(5)
        var pid1 = taskManager.addProcess(Priority.LOW)
        var pid2 = taskManager.addProcess(Priority.LOW)
        var pid3 = taskManager.addProcess(Priority.MEDIUM)
        var pid4 = taskManager.addProcess(Priority.HIGH)
        var pid5 = taskManager.addProcess(Priority.LOW)

        taskManager.killGroup(Priority.LOW)

        val expectedOrder = mutableListOf(pid4, pid3)
        val sortedListing = taskManager.list(SortType.TIME)
        assertContentEquals(expectedOrder, sortedListing.map { it.pid })
    }

    @Test
    fun killAllWorksProperly() {
        val taskManager = DefaultTaskManager(3)
        var pid1 = taskManager.addProcess(Priority.LOW)
        var pid2 = taskManager.addProcess(Priority.MEDIUM)
        var pid3 = taskManager.addProcess(Priority.HIGH)

        taskManager.killAll()

        val sortedListing = taskManager.list(SortType.TIME)
        assertTrue(sortedListing.isEmpty())
    }
}