package robot

import client.Client
import client.ClientsContext
import entity.Point
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect

/**
 * Robot control class
 */
class RRobot(
    private val coroutineScope: CoroutineScope,
    private val robotsContext: RobotsContext,
    private val clientsContext: ClientsContext
) {
    private lateinit var robot: Robot
    private lateinit var client: Client
    private val _connectStatus = MutableStateFlow(false)
    val connectStatus: StateFlow<Boolean> = _connectStatus

    private fun connectStatusCollect() {
        coroutineScope.launch {
            if (::robot.isInitialized) {
                robot.isConnect.collect {
                    if (_connectStatus.value && !it) {
                        _connectStatus.value = false
                        this.cancel()
                    }
                    _connectStatus.value = it
                }
            }
        }
    }

    fun connect(ip: String) {
        robotsContext.addRobot("test", ip, 9105)
        robot = robotsContext.getRobot("test")
        robot.connect()

        clientsContext.addClient("test", ip, 49152)
        client = clientsContext.getClient("test")
        client.connect()

        connectStatusCollect()
    }

    fun disconnect() {
        robot.disconnect()
        client.disconnect()
    }

    fun sendCommand(point: Point, gripState: Boolean) {
        client.send("$point,$gripState")
    }

    fun send(text: String) {
        client.send(text)
    }

    fun rememberPoint(pointName: String) {
        coroutineScope.launch(Dispatchers.IO) {
            robot.send("HERE $pointName")
            delay(10L)
            robot.send("\n")
            delay(10L)
            robot.send("MC EXECUTE calcOriginPoint")
            robot.send("MC EXECUTE calcScale")
        }
    }

    fun signal(signalNumber: Int, status: Boolean) {
        robot.send("SIGNAL ${if (!status) "-" else ""}$signalNumber")
    }

    fun startProgram(programName: String) {
        robot.send("EXECUTE $programName")
    }

    fun hold() {
        robot.send("HOLD")
    }
}