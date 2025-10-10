package upc.edu.pe.eduspace.features.auth.presentation.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import upc.edu.pe.eduspace.R

@Composable
fun AppLogo() {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Logo de EduSpace",
        modifier = Modifier.size(120.dp),
        contentScale = ContentScale.Fit
    )
}
