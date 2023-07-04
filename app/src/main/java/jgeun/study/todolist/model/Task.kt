package jgeun.study.todolist.model

data class Task(
	// Task 고유 ID
	val id: Int,
	val title: String,
	val content: String,
	// 선택한 배경색 번호
	val bgColor: TaskBgColor,
	val isFinish: Boolean = false
)
