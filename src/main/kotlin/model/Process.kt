package model

enum class Priority(val priorityValue: Int) {
    LOW(0), MEDIUM(1), HIGH(2)
}
data class Process(val pid: Int, val priority: Priority)