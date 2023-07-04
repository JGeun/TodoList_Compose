package jgeun.study.todolist.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jgeun.study.todolist.model.Task
import jgeun.study.todolist.model.TaskBgColor

class TaskViewModel : ViewModel() {

	private var generatedId by mutableStateOf(0)
	private var deleteCnt by mutableStateOf(0)
	private var finishCnt by mutableStateOf(0)

	private val taskList = mutableListOf<Task>()
	private val _taskLiveData = MutableLiveData<List<Task>>()
	val taskLiveData: LiveData<List<Task>>
		get() = _taskLiveData

	init {
		_taskLiveData.value = taskList.toList()
	}

	fun getTotalTaskCnt() = generatedId - deleteCnt
	fun getTaskFinishCnt() = finishCnt

	fun addTask(title: String, content: String, taskBgColor: TaskBgColor) {
		taskList.add(createTask(title, content, taskBgColor))
		_taskLiveData.value = taskList.toList()
	}

	fun deleteTask(task: Task) {
		deleteCnt = deleteCnt.plus(1)
		taskList.remove(task)
		_taskLiveData.value = taskList.toList()
	}

	fun finishTask(task: Task) {
		finishCnt = finishCnt.plus(1)
		taskList.remove(task)
		_taskLiveData.value = taskList.toList()
	}

	private fun createTask(title: String, content: String, taskBgColor: TaskBgColor): Task {
		generatedId = generatedId.plus(1)
		return Task(id = generatedId, title = title, content = content, bgColor = taskBgColor)
	}
}