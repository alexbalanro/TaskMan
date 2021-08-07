import model.PidNotFoundException
import model.Priority
import model.Process
import model.TaskManagerFullException
import java.lang.IllegalArgumentException
import java.util.*

open class DefaultTaskManager(private val maxSize: Int) : TaskManager {
    init {
        if(maxSize <= 0) {
            throw IllegalArgumentException("Max size has to be a positive non zero integer")
        }
    }

    private val processes = Collections.synchronizedMap(LinkedHashMap<Int, Process>())
    private var nextPid = 1

    fun maxSize() = maxSize
    fun processes() = processes.toMap();

    protected fun getProcess(pid: Int) = processes[pid] ?: throw PidNotFoundException()

    @Synchronized protected fun addProcessInternal(priority: Priority): Int {
        if(processes.size >= maxSize) {
            throw TaskManagerFullException()
        }
        while (true) {
            if (processes.containsKey(nextPid)) {
                nextPid = (nextPid  % maxSize) + 1
            } else {
                processes[nextPid] = Process(nextPid, priority)
                return nextPid
            }
        }
    }

    override fun killProcess(pid: Int) {
        processes.remove(pid) ?: throw PidNotFoundException()
    }

    override fun killAll() {
        processes.clear()
    }

    override fun killGroup(priority: Priority) {
        processes.filterValues { it.priority == priority }.map { it.key }.forEach { killProcess(it) }
    }

    override fun list(sortType: SortType) : List<Process> {
        synchronized(processes) {
            return when (sortType) {
                //earliest added processes will be at the beginning, latest at the end
                SortType.TIME -> processes.map { it.value }.reversed()
                SortType.PID -> processes.keys.sorted().map { getProcess(it) }
                SortType.PRIORITY -> {
                    val groupByPrio = processes.values.groupBy { it.priority }
                    val result = mutableListOf<Process>()
                    groupByPrio.keys.sortedDescending().forEach {  result.addAll(groupByPrio[it]!!) }
                    return result
                }
            }
        }
    }

    override fun addProcess(priority: Priority) = addProcessInternal(priority)
}