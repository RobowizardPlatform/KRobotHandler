import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import client.ClientsContext
import game.Game
import handDetector.HandDetector
import robot.RRobot
import robot.RobotsContext
import screens.Screens
import screens.game.GameScreen
import ui.settings.SettingScreen

@Composable
fun RobotHandlerView(robotsContext: RobotsContext, clientsContext: ClientsContext) {
    val coroutineScope = rememberCoroutineScope()

    val handDetector = remember { HandDetector(coroutineScope, clientsContext) }
    val RRobot = remember { RRobot(coroutineScope, robotsContext, clientsContext) }
    val game = remember { Game(RRobot, handDetector, coroutineScope) }

    val screen = remember { mutableStateOf(Screens.GAME) }
    MaterialTheme {
        Box(Modifier.fillMaxSize()) {
            when (screen.value) {
                Screens.GAME -> {
                    GameScreen(
                        modifier = Modifier.fillMaxSize(),
                        onOpenSettings = {
                            screen.value = Screens.SETTINGS
                        },
                        onStartGame = {
                            game.startGame()
                        },
                        onStopGame = {
                            game.stopGame()
                        },
                        gameState = game.isGame
                    )
                }
                Screens.SETTINGS -> {
                    SettingScreen(
                        onBack = {
                            screen.value = Screens.GAME
                        },
                        handDetector = handDetector,
                        RRobot = RRobot,
                    )
                }
            }
        }
    }
}