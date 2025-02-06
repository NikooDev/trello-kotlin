package com.mydigitalschool.nicotrello.ui.components.project

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mydigitalschool.nicotrello.data.model.ProjectModel
import com.mydigitalschool.nicotrello.utils.toFormattedString

@Composable
fun ProjectCard(project: ProjectModel, modifier: Modifier = Modifier, onClick: () -> Unit) {
	Card(
		modifier = modifier
			.padding(8.dp).shadow(elevation = 8.dp, shape = RoundedCornerShape(15.dp))
			.clickable { onClick() },
		shape = RoundedCornerShape(15.dp),
		colors = CardColors(
			containerColor = Color.White,
			contentColor = Color.DarkGray,
			disabledContentColor = Color.Unspecified,
			disabledContainerColor = Color.Unspecified
		)
	) {
		Column (modifier = Modifier.padding(16.dp)) {
			Text(
				text = project.title,
				fontWeight = FontWeight.Bold,
				fontSize = 25.sp
			)
			Spacer(modifier = Modifier.height(5.dp))
			Text(
				text = project.author,
				fontSize = 18.sp
			)
			Row (
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.End
			) {
				Text(text = "${project.nbTasks} tâche${if (project.nbTasks > 1) "s" else ""}", color = Color.Gray)
				Text(" • ", color = Color.Gray)
				Text(text = "Créé le ${project.created?.toFormattedString()}", color = Color.Gray)
			}
		}
	}
}