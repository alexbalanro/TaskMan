import model.Priority
import model.Process

enum class SortType {
    TIME, PRIORITY, PID
}

interface TaskManager {
    fun addProcess(priority: Priority): Int

    fun killProcess(pid: Int)
    fun killAll()
    fun killGroup(priority: Priority)

    fun list(sortType: SortType) : List<Process>
}