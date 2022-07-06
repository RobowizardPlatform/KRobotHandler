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

    private val buffer = Buffer(5)
    fun startGame() {
        coroutineScope.launch {
            _isGame.value = true
            robot.signal(gameSignal, true)
            delay(10L)
            robot.startProgram("moveByHand")
            delay(1500L)

//            trackHandDetector()
            sendBuffer()
        }
    }

    fun sendBuffer() {
        coroutineScope.launch(Dispatchers.IO) {
            while (_isGame.value) {
                if (robot.connectStatus.value) {
                    for (x in 0..100) {
                        for (y in 0..100) {
                            for (z in 0..100) {
                                val command =
                                    "${x.toFloat() / 100.0},${y.toFloat() / 100.0},${z.toFloat() / 100.0},0,0,0,0\n"
                                robot.send(command)
                                println("${robot.connectStatus.value}: $command")
                                delay(30L)
                                if (!robot.connectStatus.value) {
                                    return@launch
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun trackHandDetector() {
        coroutineScope.launch {
            handDetector.startFlow {
                buffer.add(it)
            }
        }
    }

    fun stopGame() {
        _isGame.value = false
        robot.signal(gameSignal, false)
    }
}