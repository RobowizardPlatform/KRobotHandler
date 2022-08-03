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
    private val clientsContext: ClientsContext,
    private var ipDefault: String = "localhost",
    private var portDefault: Int = 9105,
    private var clientPortDefault: Int = 49152
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

    init {
        val _robot = robotsContext.isConnected(ipDefault, portDefault)
        if (_robot != null) {
            robot = _robot
            connectStatusCollect()
        }

        val _client = clientsContext.isConnected(ipDefault, clientPortDefault)
        if (_client != null) {
            client = _client
        }
    }

    fun connect(ip: String, robotPort: Int?) {
        ipDefault = ip
        portDefault = robotPort ?: 9105

        robot = robotsContext.connect(ipDefault, portDefault)
        client = clientsContext.connect(ipDefault, clientPortDefault)

        connectStatusCollect()
    }

    fun disconnect() {
        clientsContext.disconnect(ipDefault, clientPortDefault)
        robotsContext.disconnect(ipDefault, portDefault)
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

    fun isInit(): Boolean {
        return ::robot.isInitialized
    }
}