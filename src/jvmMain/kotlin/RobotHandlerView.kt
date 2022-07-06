import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import client.ClientsContext
import game.Game
import handDetector.HandDetector
import robot.RRobot
import robot.RobotsContext
import ui.settings.SettingScreen

@Composable
fun RobotHandlerView(robotsContext: RobotsContext, clientsContext: ClientsContext) {
    val coroutineScope = rememberCoroutineScope()
    val client = remember {
        clientsContext.addClient("test", "localhost", 49152)
        clientsContext.getClient("test")
    }
    val handDetecor = remember {
        clientsContext.addClient("test2", "localhost", 9105)
        clientsContext.getClient("test2")
    }
    val robot = remember {
        robotsContext.addRobot("test", "localhost", 9105)
        robotsContext.getRobot("test")
    }
    val handDetector = remember { HandDetector(coroutineScope, handDetecor) }
    val RRobot = remember { RRobot(coroutineScope, robot) }
    val game = remember { Game(RRobot, handDetector, coroutineScope) }

    MaterialTheme {
        Column {
            SettingScreen(handDetector, RRobot, client)

            Button(onClick = {
                if (!game.isGame.value) game.startGame(client) else game.stopGame()
            }) {
                Text(if (!game.isGame.value) "Start game" else "Stop game")
            }

        }
    }
}