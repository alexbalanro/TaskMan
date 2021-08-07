import model.Priority
import model.TaskManagerFullException

class PriorityTaskManager(maxSize: Int) : DefaultTaskManager(maxSize) {
    override fun addProcess(priority: Priority): Int {
        if (processes().size == maxSize()) {
            var minPriority = priority
            var pid = -1
            processes().values.forEach {
                if (it.priority.priorityValue < minPriority.priorityValue) {
                    minPriority = it.priority
                    pid = it.pid
                }
            }
            //no process with lower priority was found, throw exception
            //in order to skip it I would rather throw exception over returning a dummy pid
            if(pid == -1) throw TaskManagerFullException()
            killProcess(pid)
        }
        return addProcessInternal(priority)
    }
}