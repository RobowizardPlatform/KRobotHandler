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
import parameters.ParametersContext
import robot.RRobot
import robot.RobotsContext
import screens.Screens
import screens.game.GameScreen
import ui.settings.SettingScreen

@Composable
fun RobotHandlerView(
    robotsContext: RobotsContext,
    clientsContext: ClientsContext,
    parametersContext: ParametersContext
) {
    val coroutineScope = rememberCoroutineScope()

    var handDetectorIpDefault = remember { "localhost" }
    var robotIpDefault = remember { "localhost" }
    var robotPortDefault = remember { "23" }
    var rClientPortDefault = remember { "49152" }
    val hDIpDefault: String = parametersContext.load("handDetectorIpDefault")
    if (hDIpDefault != "") {
        handDetectorIpDefault = hDIpDefault
    }

    val rIpDefault: String = parametersContext.load("robotIpDefault")
    if (rIpDefault != "") {
        robotIpDefault = rIpDefault
    }

    val rPortDefault: String = parametersContext.load("robotPortDefault")
    if (rPortDefault != "") {
        robotPortDefault = rPortDefault
    }

    val rcPortDefault: String = parametersContext.load("rClientPortDefault")
    if (rcPortDefault != "") {
        rClientPortDefault = rcPortDefault
    }
    val handDetector = remember {
        HandDetector(
            coroutineScope,
            clientsContext,
            ipDefault = handDetectorIpDefault
        )
    }
    val RRobot = remember {
        RRobot(
            coroutineScope,
            robotsContext,
            clientsContext,
            ipDefault = robotIpDefault,
            portDefault = robotPortDefault.toIntOrNull() ?: 9105,
            clientPortDefault = rClientPortDefault.toIntOrNull() ?: 49152
        )
    }
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

                            parametersContext.save("handDetectorIpDefault", handDetector.ipDefault)
                            parametersContext.save("robotIpDefault", RRobot.ipDefault)
                            parametersContext.save("robotPortDefault", RRobot.portDefault.toString())
                            parametersContext.save("rClientPortDefault", RRobot.clientPortDefault.toString())
                        },
                        handDetector = handDetector,
                        RRobot = RRobot,
                    )
                }
            }
        }
    }
}