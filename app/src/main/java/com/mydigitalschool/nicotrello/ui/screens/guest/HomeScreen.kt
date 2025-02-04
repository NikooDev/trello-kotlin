package com.mydigitalschool.nicotrello.ui.screens.guest

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mydigitalschool.nicotrello.data.model.ScreenModel
import com.mydigitalschool.nicotrello.R

@Composable
fun HomeScreen(navController: NavController) {
	Column(
		modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text("Trello", style = MaterialTheme.typography.displayLarge, color = MaterialTheme.colorScheme.onPrimary)
		Text("Trello rassemble vos tâches, vos coéquipiers et vos outils", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onPrimary)
		Image(
			painter = painterResource(id = R.drawable.bg), // Nom de ton fichier WebP dans le dossier drawable
			contentDescription = "Background",
			modifier = Modifier.fillMaxWidth().padding(top = 50.dp),
			contentScale = ContentScale.Fit
		)
		Button(
			onClick = {
				navController.navigate(ScreenModel.Login.route)
			},
			modifier = Modifier.fillMaxWidth().padding(top = 16.dp).height(50.dp),
			colors = ButtonDefaults.buttonColors(
				containerColor = MaterialTheme.colorScheme.onPrimary
			)
		) {
			Text(
				"Connexion",
				color = MaterialTheme.colorScheme.primary,
				style = MaterialTheme.typography.bodyLarge,
				fontSize = 20.sp,
				fontWeight = FontWeight.SemiBold
			)
		}

		Spacer(modifier = Modifier.height(20.dp))

		Button(
			onClick = {
				navController.navigate(ScreenModel.Signup.route)
			},
			modifier = Modifier.fillMaxWidth().height(50.dp).border(2.dp, color = Color(0xFFFFFFFF), shape = RoundedCornerShape(30.dp)),
			colors = ButtonDefaults.buttonColors(
				containerColor = Color.Transparent
			)
		) {
			Text(
				"Inscrivez-vous",
				fontSize = 20.sp,
				fontWeight = FontWeight.SemiBold
			)
		}
	}
}