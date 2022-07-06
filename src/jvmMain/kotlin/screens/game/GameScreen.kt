package screens.game

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    onOpenSettings: () -> Unit,
    onStartGame: () -> Unit,
    onStopGame: () -> Unit
) {
    val isGame = remember { mutableStateOf(false) }
    Column(modifier) {
        Button(
            onClick = onOpenSettings
        ) {
            Text("Settings")
        }

        Button(
            onClick = {
                if (!isGame.value) {
                    onStartGame()
                } else {
                    onStopGame()
                }
            }
        ) {
            Text(
                if (!isGame.value) {
                    "Start Game"
                } else {
                    "Stop Game"
                }
            )
        }
    }
}