package screens.game

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    onOpenSettings: () -> Unit,
    onStartGame: () -> Unit,
    onStopGame: () -> Unit,
    gameState: StateFlow<Boolean>
) {
    val isGame = gameState.collectAsState()
    Column(modifier) {

        IconButton(
            modifier = Modifier.align(Alignment.End),
            onClick = onOpenSettings
        ) {
            Icon(Icons.Default.Settings, "Settings")
        }


        Surface(
            modifier = Modifier.align(Alignment.CenterHorizontally).size(400.dp),
            shape = CircleShape,
            color = Color.Red,
            onClick = {
                if (!isGame.value) {
                    onStartGame()
                } else {
                    onStopGame()
                }
            }
        ) {
            Surface(
                modifier = Modifier.padding(20.dp),
                shape = CircleShape,
                color = Color.White
            ) {
                Box(Modifier.fillMaxSize().align(Alignment.CenterHorizontally)) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Black,
                        fontSize = 50.sp,
                        textAlign = TextAlign.Center,
                        text = if (!isGame.value) {
                            "Начать игру"
                        } else {
                            "Остановить игру"
                        }
                    )
                }

            }
        }


    }
}