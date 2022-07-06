package robot

import entity.Point
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Robot control class
 */
class RRobot(
    private val coroutineScope: CoroutineScope,
    private val robot: Robot
) {
    val isConnect = robot.isConnect

    fun connect() {
        robot.connect()
    }

    fun disconnect() {
        robot.disconnect()
    }

    fun setPoint(point: Point) {
        coroutineScope.launch(Dispatchers.IO) {
            println("POINT shift_point = TRANS($point)")
            robot.send("POINT shift_point = TRANS($point)")
            delay(10L)
            robot.send("\n${0x17.toChar()}")
        }
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