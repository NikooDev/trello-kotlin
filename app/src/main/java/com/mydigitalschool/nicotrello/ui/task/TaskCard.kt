package com.mydigitalschool.nicotrello.ui.task

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mydigitalschool.nicotrello.R
import com.mydigitalschool.nicotrello.core.ui.components.Item
import com.mydigitalschool.nicotrello.core.utils.cap
import com.mydigitalschool.nicotrello.data.model.Priority
import com.mydigitalschool.nicotrello.data.model.ScreenUserModel
import com.mydigitalschool.nicotrello.data.model.TaskModel
import com.mydigitalschool.nicotrello.ui.theme.ColorScheme

@Composable
fun TaskCard(navController: NavHostController, task: TaskModel, modifier: Modifier, lastIndex: Int, index: Int, colors: ColorScheme) {
	val lastItem = index == lastIndex

	fun taskPriorityColor(): Color {
		return when (task.priority) {
			Priority.LOW -> colors.priorityLow
			Priority.MEDIUM -> colors.priorityMedium
			Priority.HIGH -> colors.priorityHigh
			else -> Color.DarkGray
		}
	}

	key(task.uid) {
		Item(
			modifier.fillMaxWidth()
				.padding(bottom = 6.dp, top = if(!lastItem) 6.dp else 0.dp)
				.padding(horizontal = 16.dp),
			onClick = {
				navController.navigate("${ScreenUserModel.Task.route}/${task.uid}")
			}
		) {
			Column(
				modifier = modifier.padding(horizontal = 8.dp, vertical = 3.dp).fillMaxWidth()
			) {
				Row(
					modifier = Modifier.fillMaxWidth().height(intrinsicSize = IntrinsicSize.Max).padding(vertical = 6.dp),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.Start
				) {
					Box(
						modifier = Modifier
							.width(4.dp)
							.fillMaxHeight()
							.clip(CircleShape)
							.background(taskPriorityColor())
					)
					Text(
						modifier = Modifier.weight(1f).padding(start = 8.dp),
						overflow = TextOverflow.Ellipsis,
						maxLines = 1,
						text = task.title.cap(),
						fontWeight = FontWeight.SemiBold,
						fontSize = 16.sp
					)

					if (task.picture != null) {
						Image(
							painter = painterResource(R.drawable.photo_landscape),
							contentDescription = "Edit",
							modifier = Modifier.size(24.dp),
							colorFilter = ColorFilter.tint(Color.DarkGray)
						)
						Spacer(modifier = Modifier.width(6.dp))
					}

					Image(
						painter = painterResource(R.drawable.next_ui),
						contentDescription = "Edit",
						modifier = Modifier.size(22.dp),
						colorFilter = ColorFilter.tint(Color.DarkGray)
					)
				}
			}
		}
	}
}