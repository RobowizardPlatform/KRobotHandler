package ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SettingSurface(
    header: String,
    modifier: Modifier = Modifier,
    settingScreen: @Composable ColumnScope.() -> Unit
) {
    Card(modifier.fillMaxWidth().padding(10.dp)) {
        Column(Modifier.padding(5.dp)) {
            Text(header, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(10.dp))
            settingScreen()
        }
    }
}