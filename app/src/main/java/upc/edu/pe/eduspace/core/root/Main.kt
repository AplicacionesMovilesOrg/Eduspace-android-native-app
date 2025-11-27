package upc.edu.pe.eduspace.core.root

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import upc.edu.pe.eduspace.features.teachers.presentation.teachers.TeachersRoute

@Composable
fun Main() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    Scaffold { padding ->
        Column(Modifier.padding(padding)) {
            TeachersRoute(
                drawerState = drawerState,
                scope = scope
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainPreview() {
    Main()
}