package com.mydigitalschool.nicotrello.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mydigitalschool.nicotrello.R
import com.mydigitalschool.nicotrello.core.ui.components.Btn
import com.mydigitalschool.nicotrello.core.ui.components.Title
import com.mydigitalschool.nicotrello.data.model.ScreenModel
import com.mydigitalschool.nicotrello.ui.theme.LocalColors

@Composable
fun HomeScreen(navController: NavHostController) {
	val colors = LocalColors.current

	Column(
		modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Title("Trello", 60)
		Title("Trello rassemble vos tâches, vos coéquipiers et vos outils", 23)
		Image(
			painter = painterResource(id = R.drawable.bg),
			contentDescription = "Background",
			modifier = Modifier.fillMaxWidth().padding(top = 50.dp),
			contentScale = ContentScale.Fit
		)
		Spacer(modifier = Modifier.height(10.dp))
		Btn(
			onClick = {
				navController.navigate(ScreenModel.Login.route)
			},
			modifier = Modifier.fillMaxWidth(),
			backgroundColor = Color.White,
			textColor = colors.primary,
			textSize = 18.sp,
			paddingVertical = 12,
			text = "CONNEXION"
		)
		Spacer(modifier = Modifier.height(15.dp))
		Btn(
			onClick = {
				navController.navigate(ScreenModel.Signup.route)
			},
			modifier = Modifier.fillMaxWidth().border(2.dp, color = Color.White, shape = RoundedCornerShape(30.dp)),
			backgroundColor = Color.Transparent,
			textColor = Color.White,
			textSize = 18.sp,
			paddingVertical = 0,
			text = "INSCRIVEZ-VOUS"
		)
	}
}