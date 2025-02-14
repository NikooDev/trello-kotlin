package com.mydigitalschool.nicotrello.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.mydigitalschool.nicotrello.R

val provider = GoogleFont.Provider(
	providerAuthority = "com.google.android.gms.fonts",
	providerPackage = "com.google.android.gms",
	certificates = R.array.com_google_android_gms_fonts_certs
)

val Paytone = FontFamily(
	Font(
		googleFont = GoogleFont("Paytone One"),
		fontProvider = provider,
	)
)

val Lato = FontFamily(
	Font(
		googleFont = GoogleFont("Lato"),
		fontProvider = provider,
	)
)

val Typography = Typography(

)