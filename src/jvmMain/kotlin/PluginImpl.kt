import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import client.ClientsContext
import robot.RobotsContext

class PluginImpl : KRPlugin {
    var robotsContext: RobotsContext? = null
        private set
    var clientsContext: ClientsContext? = null
        private set

    override fun setRobotsContext(robotsContext: RobotsContext) {
        this.robotsContext = robotsContext
    }

    override fun setClientsContext(clientsContext: ClientsContext) {
        this.clientsContext = clientsContext
    }

    @Composable
    override fun content() {
        if (robotsContext != null && clientsContext != null) {
            RobotHandlerView(
                robotsContext!!,
                clientsContext!!
            )
        } else {
            Text("Plugin loading")
        }
    }
}