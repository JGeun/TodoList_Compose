package jgeun.study.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import jgeun.study.todolist.model.Task
import jgeun.study.todolist.model.TaskBgColor
import jgeun.study.todolist.ui.TaskViewModel
import jgeun.study.todolist.ui.theme.AddBtnBgColor
import jgeun.study.todolist.ui.theme.Blue
import jgeun.study.todolist.ui.theme.DialogBtnStrokeColor
import jgeun.study.todolist.ui.theme.Emerald
import jgeun.study.todolist.ui.theme.Green
import jgeun.study.todolist.ui.theme.MsgColor
import jgeun.study.todolist.ui.theme.ProgressAreaStrokeColor
import jgeun.study.todolist.ui.theme.ProgressBarCompleted
import jgeun.study.todolist.ui.theme.ProgressBarDefault
import jgeun.study.todolist.ui.theme.Purple
import jgeun.study.todolist.ui.theme.Sky
import jgeun.study.todolist.ui.theme.TextMainColor
import jgeun.study.todolist.ui.theme.TodoListTheme

class MainActivity : ComponentActivity() {

	private val viewModel by viewModels<TaskViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			TodoListTheme {
				TodoList(viewModel)
			}
		}
	}
}

@Composable
fun TodoList(
	viewModel: TaskViewModel
) {
	Column(
		modifier = Modifier.padding(
			horizontal = 19.dp,
			vertical = 20.dp
		)
	) {
		Header(viewModel)
		Spacer(modifier = Modifier.height(34.dp))
		ProgressArea(viewModel)
		Spacer(modifier = Modifier.height(36.dp))
		TaskArea(viewModel)
	}
}

@Composable
fun Header(
	viewModel: TaskViewModel
) {
	var showDialog by remember { mutableStateOf(false) }
	var title by rememberSaveable { mutableStateOf("") }
	var content by rememberSaveable { mutableStateOf("") }
	var checkedColor by rememberSaveable { mutableStateOf(TaskBgColor.PURPLE) }

	Row {
		Text(
			modifier = Modifier.weight(1f),
			text = stringResource(R.string.title),
			color = TextMainColor,
			style = TextStyle.Default,
			fontSize = 24.sp,
			fontWeight = FontWeight.Bold
		)

		Button(
			modifier = Modifier
				.width(36.dp)
				.height(36.dp),
			colors = ButtonDefaults.buttonColors(
				containerColor = AddBtnBgColor,
			),
			shape = RoundedCornerShape(10.dp),
			contentPadding = PaddingValues(0.dp),
			onClick = { showDialog = true }
		) {
			Image(
				painter = painterResource(id = R.drawable.ic_add_white),
				contentDescription = ""
			)
		}
	}

	if (showDialog) {
		TaskDialog(
			title = title,
			onTitleChanged = { title = it },
			content = content,
			onContentChanged = { content = it },
			taskBgColor = checkedColor,
			onTaskBgColorChanged = { checkedColor = it },
			onDismiss = { showDialog = false }
		) {
			viewModel.addTask(title, content, checkedColor)
			showDialog = false
		}
	}
}

@Composable
fun TaskDialog(
	title: String,
	onTitleChanged: (String) -> Unit,
	content: String,
	onContentChanged: (String) -> Unit,
	taskBgColor: TaskBgColor,
	onTaskBgColorChanged: (TaskBgColor) -> Unit,
	onDismiss: () -> Unit,
	onSaved: () -> Unit
) {
	Dialog(onDismissRequest = onDismiss) {
		Box(
			modifier = Modifier
				.fillMaxSize()
		) {
			TaskDialogContent(
				modifier = Modifier.align(Alignment.Center),
				title = title,
				onTitleChanged = onTitleChanged,
				content = content,
				onContentChanged = onContentChanged,
				taskBgColor = taskBgColor,
				onTaskBgColorChanged = onTaskBgColorChanged,
				onSaved = onSaved,
				onDismiss = { onDismiss() }
			)
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDialogContent(
	modifier: Modifier = Modifier,
	title: String,
	onTitleChanged: (String) -> Unit,
	content: String,
	onContentChanged: (String) -> Unit,
	taskBgColor: TaskBgColor,
	onTaskBgColorChanged: (TaskBgColor) -> Unit,
	onSaved: () -> Unit,
	onDismiss: () -> Unit,
) {
	Column(
		modifier = modifier
			.fillMaxWidth()
			.clip(shape = RoundedCornerShape(10.dp))
			.background(color = Color.White)
			.padding(vertical = 28.dp, horizontal = 24.dp),
	) {
		Text("제목")
		Spacer(modifier = Modifier.height(7.dp))
		OutlinedTextField(
			modifier = Modifier.padding(0.dp),
			value = title,
			onValueChange = { onTitleChanged(it) },
			colors = TextFieldDefaults.textFieldColors(
				textColor = TextMainColor,
				containerColor = Color.White
			),
		)
		Spacer(modifier = Modifier.height(15.dp))
		Text("내용")
		Spacer(modifier = Modifier.height(7.dp))
		OutlinedTextField(
			modifier = Modifier.padding(0.dp),
			value = content,
			onValueChange = { onContentChanged(it) },
			colors = TextFieldDefaults.textFieldColors(
				textColor = TextMainColor,
				containerColor = Color.White
			),
		)
		Spacer(modifier = Modifier.height(21.dp))

		ConstraintLayout(
			modifier = Modifier
				.fillMaxWidth()
		) {
			val (purple, green, blue, sky, emerald) = createRefs()
			createHorizontalChain(purple, green, blue, sky, emerald, chainStyle = ChainStyle.SpreadInside)

			Box(
				modifier = Modifier
					.defaultMinSize(minWidth = 35.dp, minHeight = 35.dp)
					.constrainAs(purple) {
						top.linkTo(parent.top)
					}
					.clickable(
						onClick = { onTaskBgColorChanged(TaskBgColor.PURPLE) }
					)
					.background(color = Purple, shape = RoundedCornerShape(5.dp))
			) {
				if (taskBgColor == TaskBgColor.PURPLE) {
					Image(
						modifier = Modifier.align(Alignment.Center),
						painter = painterResource(id = R.drawable.ic_box_check_white),
						contentDescription = ""
					)
				}
			}
			Box(
				modifier = Modifier
					.defaultMinSize(minWidth = 35.dp, minHeight = 35.dp)
					.constrainAs(green) {
						top.linkTo(parent.top)
					}
					.clickable(
						onClick = {
							onTaskBgColorChanged(TaskBgColor.GREEN)
						}
					)
					.background(color = Green, shape = RoundedCornerShape(5.dp))
			) {
				if (taskBgColor == TaskBgColor.GREEN) {
					Image(
						modifier = Modifier.align(Alignment.Center),
						painter = painterResource(id = R.drawable.ic_box_check_white),
						contentDescription = ""
					)
				}
			}
			Box(
				modifier = Modifier
					.defaultMinSize(minWidth = 35.dp, minHeight = 35.dp)
					.constrainAs(blue) {
						top.linkTo(parent.top)
					}
					.clickable(
						onClick = { onTaskBgColorChanged(TaskBgColor.BLUE) }
					)
					.background(color = Blue, shape = RoundedCornerShape(5.dp))
			) {
				if (taskBgColor == TaskBgColor.BLUE) {
					Image(
						modifier = Modifier.align(Alignment.Center),
						painter = painterResource(id = R.drawable.ic_box_check_white),
						contentDescription = ""
					)
				}
			}
			Box(
				modifier = Modifier
					.defaultMinSize(minWidth = 35.dp, minHeight = 35.dp)
					.constrainAs(sky) {
						top.linkTo(parent.top)
					}
					.clickable(
						onClick = { onTaskBgColorChanged(TaskBgColor.SKY) }
					)
					.background(color = Sky, shape = RoundedCornerShape(5.dp))
			) {
				if (taskBgColor == TaskBgColor.SKY) {
					Image(
						modifier = Modifier.align(Alignment.Center),
						painter = painterResource(id = R.drawable.ic_box_check_white),
						contentDescription = ""
					)
				}
			}
			Box(
				modifier = Modifier
					.defaultMinSize(minWidth = 35.dp, minHeight = 35.dp)
					.constrainAs(emerald) {
						top.linkTo(parent.top)
					}
					.clickable(
						onClick = { onTaskBgColorChanged(TaskBgColor.EMERALD) }
					)
					.background(color = Emerald, shape = RoundedCornerShape(5.dp))
			) {
				if (taskBgColor == TaskBgColor.EMERALD) {
					Image(
						modifier = Modifier.align(Alignment.Center),
						painter = painterResource(id = R.drawable.ic_box_check_white),
						contentDescription = ""
					)
				}
			}
		}

		Spacer(modifier = Modifier.height(36.dp))

		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.End
		) {
			Text(
				modifier = Modifier
					.border(
						width = 2.dp,
						color = DialogBtnStrokeColor,
						shape = RoundedCornerShape(10.dp)
					)
					.padding(
						vertical = 12.dp,
						horizontal = 25.dp
					)
					.clickable(
						onClick = { onSaved() }
					),
				text = "저장",
				color = TextMainColor
			)

			Spacer(modifier = Modifier.width(12.dp))

			Text(
				modifier = Modifier
					.border(
						width = 2.dp,
						color = DialogBtnStrokeColor,
						shape = RoundedCornerShape(10.dp)
					)
					.padding(
						vertical = 12.dp,
						horizontal = 25.dp
					)
					.clickable(
						onClick = { onDismiss() }
					),
				text = "취소",
				color = TextMainColor
			)
		}
	}
}

@Composable
fun ProgressArea(
	viewModel: TaskViewModel
) {
	val taskFinishCnt = viewModel.getTaskFinishCnt()
	val totalFinishCnt = viewModel.getTotalTaskCnt()
	val percent = if (totalFinishCnt == 0) 0 else taskFinishCnt * 100 / totalFinishCnt
	val progress = if (totalFinishCnt == 0) 0f else taskFinishCnt / totalFinishCnt.toFloat()

	val progressTitle = getProgressTitleByPercent(percent)

	Box() {
		Column(
			modifier = Modifier
				.border(
					width = 1.dp,
					shape = RoundedCornerShape(10.dp),
					color = ProgressAreaStrokeColor
				)
				.padding(
					horizontal = 16.dp,
					vertical = 20.dp
				),
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically
			) {
				Text(
					modifier = Modifier.weight(1f),
					text = progressTitle,
					style = TextStyle.Default,
					fontSize = 16.sp,
					fontWeight = FontWeight.Bold,
					color = TextMainColor
				)

				Text(
					text = stringResource(id = R.string.progress_percent, percent),
					style = TextStyle.Default,
					fontSize = 16.sp,
					fontWeight = FontWeight.Bold,
					color = TextMainColor
				)
			}
			Spacer(modifier = Modifier.height(10.dp))
			Text(
				text = stringResource(id = R.string.progress_task_rate, taskFinishCnt, totalFinishCnt),
				color = TextMainColor
			)
			Spacer(modifier = Modifier.height(10.dp))
			LinearProgressIndicator(
				modifier = Modifier
					.fillMaxWidth()
					.height(15.dp)
					.clip(RoundedCornerShape(10.dp)),
				progress = progress,
				color = ProgressBarCompleted,
				trackColor = ProgressBarDefault
			)
		}
	}
}

@Composable
private fun getProgressTitleByPercent(percent: Int) = if (percent == 100)
	stringResource(id = R.string.progress_title_finish)
else if (percent >= 30)
	stringResource(id = R.string.progress_title_ing)
else
	stringResource(id = R.string.progress_title_start)

@Composable
fun TaskArea(
	viewModel: TaskViewModel
) {
	val taskList by viewModel.taskLiveData.observeAsState(emptyList())
	val finishCnt = viewModel.getTaskFinishCnt()

	Text(
		text = stringResource(id = R.string.task_title, taskList.size),
		color = TextMainColor
	)
	Spacer(modifier = Modifier.height(21.dp))
	Box(
		modifier = Modifier
			.fillMaxSize()
	) {

		val taskMsg = getTaskMsg(finishCnt)

		if (taskList.isEmpty()) {
			Text(
				modifier = Modifier
					.align(Alignment.Center),
				text = taskMsg,
				textAlign = TextAlign.Center,
				color = MsgColor,
				style = TextStyle.Default,
				fontSize = 16.sp,
				fontWeight = FontWeight.Bold
			)
		}

		LazyColumn {
			items(taskList.size) { index ->
				val task = taskList[index]

				if (index != 0) {
					Spacer(modifier = Modifier.height(20.dp))
				}

				key(task.id) {
					TaskBox(
						task = task,
						onClear = { viewModel.finishTask(it) },
						onDelete = { viewModel.deleteTask(it) }
					)
				}
			}
		}
	}
}

@Composable
private fun getTaskMsg(finishCnt: Int) = if (finishCnt == 0)
	stringResource(id = R.string.task_empty_msg)
else
	stringResource(id = R.string.task_done_msg)

@Composable
fun TaskBox(
	task: Task,
	onClear: (Task) -> Unit,
	onDelete: (Task) -> Unit
) {
	ConstraintLayout(
		modifier = Modifier
			.fillMaxWidth()
			.background(
				color = task.bgColor.rgbColor,
				shape = RoundedCornerShape(20.dp),
			)
			.padding(
				horizontal = 15.dp,
				vertical = 14.dp
			)
	) {
		val (title, content, delete, clear) = createRefs()

		Text(
			modifier = Modifier.constrainAs(title) {
				top.linkTo(parent.top)
				start.linkTo(parent.start)
			},
			text = task.title,
			style = TextStyle.Default,
			fontWeight = FontWeight.Bold,
			color = Color.White,
		)
		Text(
			modifier = Modifier.constrainAs(content) {
				bottom.linkTo(parent.bottom)
				start.linkTo(parent.start)
				top.linkTo(title.bottom, margin = 10.dp)
			},
			text = task.title,
			style = TextStyle.Default,
			fontWeight = FontWeight.Bold,
			color = Color.White
		)

		Image(
			modifier = Modifier
				.constrainAs(delete) {
					end.linkTo(parent.end)
					top.linkTo(parent.top)
					bottom.linkTo(parent.bottom)
				}
				.clickable(
					onClick = { onDelete(task) }
				),
			painter = painterResource(id = R.drawable.ic_delete_white),
			contentDescription = ""
		)

		Image(
			modifier = Modifier
				.constrainAs(clear) {
					bottom.linkTo(parent.bottom)
					top.linkTo(parent.top)
					end.linkTo(delete.start, margin = 20.dp)
				}
				.clickable(
					onClick = { onClear(task) }
				),
			painter = painterResource(id = R.drawable.ic_check_white),
			contentDescription = ""
		)
	}
}