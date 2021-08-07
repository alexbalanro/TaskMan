package model

class PidNotFoundException: Exception("PID was not found")
class TaskManagerFullException: Exception("The task manager is full and cannot accept any more items")