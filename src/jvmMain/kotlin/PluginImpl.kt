import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import client.ClientsContext
import parameters.ParametersContext
import robot.RobotsContext

class PluginImpl : KRPlugin {
    var robotsContext: RobotsContext? = null
        private set
    var clientsContext: ClientsContext? = null
        private set

    var parametersContext: ParametersContext? = null
        private set

    override fun setRobotsContext(robotsContext: RobotsContext) {
        this.robotsContext = robotsContext
    }

    override fun setClientsContext(clientsContext: ClientsContext) {
        this.clientsContext = clientsContext
    }

    override fun setParameterContext(parametersContext: ParametersContext) {
        this.parametersContext = parametersContext
        super.setParameterContext(parametersContext)
    }

    @Composable
    override fun content() {
        if (robotsContext != null && clientsContext != null && parametersContext != null) {
            RobotHandlerView(
                robotsContext!!,
                clientsContext!!,
                parametersContext!!
            )
        } else {
            Text("Plugin loading")
        }
    }
}