import model.Priority

enum class TaskManagerType {
    DEFAULT, FIFO, PRIORITY
}
fun main(args: Array<String>) {

    var taskManager: TaskManager? = null
    var type: TaskManagerType? = null

    while(true) {
        while (taskManager == null) {
            try {
                type = parseTaskManagerType()
                val initialSize = parseTaskManagerSize()
                taskManager = when(type) {
                    TaskManagerType.DEFAULT -> DefaultTaskManager(initialSize)
                    TaskManagerType.FIFO -> FifoTaskManager(initialSize)
                    TaskManagerType.PRIORITY -> PriorityTaskManager(initialSize)
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }

        try {
            parseTaskManagerOperation(taskManager, type)
        } catch (e: Exception) {
            println(e.message)
        }
    }
}

fun parseTaskManagerOperation(taskManager: TaskManager, type: TaskManagerType?) {
    println("Enter operation for task manager of type ${type}: ")
    print("(1) Add LOW priority process ")
    print("(2) Add MEDIUM priority process ")
    println("(3) Add HIGH priority process ")
    print("(4) List all processes sorted by creation order ")
    print("(5) List all processes sorted by pid order ")
    println("(6) List all processes sorted by priority order ")
    print("(7) Kill process by ID ")
    println("(8) Kill all processes ")
    print("(9) Kill process by priority LOW ")
    print("(10) Kill process by priority MEDIUM ")
    println("(11) Kill process by priority HIGH ")

    var value = readLine()?.split(" ")
    when(Integer.parseInt(value?.get(0))) {
        1 -> taskManager.addProcess(Priority.LOW)
        2 -> taskManager.addProcess(Priority.MEDIUM)
        3 -> taskManager.addProcess(Priority.HIGH)
        4 -> taskManager.list(SortType.TIME).forEach { println("PID:${it.pid} PRIORITY:${it.priority}") }
        5 -> taskManager.list(SortType.PID).forEach { println("PID:${it.pid} PRIORITY:${it.priority}") }
        6 -> taskManager.list(SortType.PRIORITY).forEach { println("PID:${it.pid} PRIORITY:${it.priority}") }
        7 -> taskManager.killProcess(Integer.parseInt(value?.get(1)))
        8 -> taskManager.killAll()
        9 -> taskManager.killGroup(Priority.LOW)
        10 -> taskManager.killGroup(Priority.MEDIUM)
        11 -> taskManager.killGroup(Priority.HIGH)
        else -> throw IllegalArgumentException("Unknown command")
    }
}

fun parseTaskManagerSize(): Int {
    println("Enter the task manager maximum size")
    return Integer.parseInt(readLine())
}

fun parseTaskManagerType() : TaskManagerType {
    println("Enter the task manager type\nType can be (1) Default (2) FiFo (3) Priority")
    return when (Integer.parseInt(readLine())) {
        1 -> TaskManagerType.DEFAULT
        2 -> TaskManagerType.FIFO
        3 -> TaskManagerType.PRIORITY
        else -> throw IllegalArgumentException("Unknown type")
    }
}
