package upc.edu.pe.eduspace.core.root

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import upc.edu.pe.eduspace.features.teachers.presentation.teachers.TeachersRoute

@Composable
fun Main() {
    Scaffold { padding ->
        Column(Modifier.padding(padding)) {
            TeachersRoute()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainPreview() {
    Main()
}