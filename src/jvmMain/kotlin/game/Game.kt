package game

import entity.Buffer
import handDetector.HandDetector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import robot.RRobot

class Game(
    private val robot: RRobot,
    private val handDetector: HandDetector,
    private val coroutineScope: CoroutineScope
) {
    private val gameSignal = 2001
    private val _isGame = MutableStateFlow(false)
    val isGame: StateFlow<Boolean> = _isGame

    private val buffer = Buffer(10)
    fun startGame() {
        buffer.clear()
        _isGame.value = true

        coroutineScope.launch {
            robot.signal(gameSignal, true)
            delay(10L)
            robot.startProgram("moveByHand")
            delay(1500L)

            trackHandDetector()
//            trackData()
            sendBuffer()
        }
    }

    fun sendBuffer() {
        coroutineScope.launch(Dispatchers.IO) {
            while (_isGame.value) {
                val message = buffer.getMiddle()
                if (message != null) {
                    println(message)
                    robot.send("$message\n")
                }
                delay(10L)
            }
//            else {
//                    stopGame()
//                }
//            }
        }
    }

    fun trackHandDetector() {
        coroutineScope.launch {
            handDetector.startFlow {
//                if (!it.contains("START")) {
//                    val values = it.split(";").map { it.trim().toFloat() }
//                    var message = ""
//                    values.forEach {
//                        message += "${(it + 1.0) / 2.0},"
//                    }
//                    buffer.add("$message,")
//                }
                if (!it.contains("START")) {
                    buffer.add(it)
                }
            }
        }
    }

    private fun trackData() {
        coroutineScope.launch {
            for (x in 0..100) {
                for (y in 0..100) {
                    for (z in 0..100) {
                        val command =
                            "${x.toFloat() / 100.0},${y.toFloat() / 100.0},${z.toFloat() / 100.0},0,0,0,0\n"
                        buffer.add(command)

                        if (!_isGame.value) {
                            return@launch
                        }
                    }
                }
            }
        }
    }

    fun stopGame() {
        _isGame.value = false
        robot.signal(gameSignal, false)
    }
}