package jgeun.study.todolist.model

import androidx.compose.ui.graphics.Color
import jgeun.study.todolist.ui.theme.Blue
import jgeun.study.todolist.ui.theme.Emerald
import jgeun.study.todolist.ui.theme.Green
import jgeun.study.todolist.ui.theme.Purple
import jgeun.study.todolist.ui.theme.Sky

enum class TaskBgColor(val rgbColor: Color) {
	PURPLE(Purple),
	GREEN(Green),
	BLUE(Blue),
	SKY(Sky),
	EMERALD(Emerald);
}