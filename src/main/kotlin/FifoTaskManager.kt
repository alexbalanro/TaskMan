import model.Priority

class FifoTaskManager(maxSize: Int) : DefaultTaskManager(maxSize) {
    override fun addProcess(priority: Priority): Int {
        if (processes().size == maxSize()) {
            killProcess(processes().keys.first())
        }
        return super.addProcessInternal(priority)
    }
}