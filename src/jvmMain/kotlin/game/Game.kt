package game

import entity.Buffer
import handDetector.HandDetector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import robot.RRobot
import utils.printInfo
import utils.printWarning

class Game(
    private val robot: RRobot,
    private val handDetector: HandDetector,
    private val coroutineScope: CoroutineScope
) {
    private val gameSignal = 2001
    private val _isGame = MutableStateFlow(false)
    val isGame: StateFlow<Boolean> = _isGame

    private val buffer = Buffer(1)
    private var isTrack = false

    fun startGame() {
        if (robot.isInit() && handDetector.isInit()) {
            if (!isTrack) {
                isTrack = true
                coroutineScope.launch {
                    trackHandDetector()
                }
            }
            buffer.clear()

            _isGame.value = true

            coroutineScope.launch {
                robot.signal(gameSignal, true)
                delay(10L)
                robot.startProgram("moveByHand")
                delay(1500L)

                sendBuffer()
            }
        } else {
            printWarning("Robot or hand detector is not init")
        }
    }

    suspend fun sendBuffer() {
        while (_isGame.value) {
            val message = buffer.getLast()
            if (message != null) {
                printInfo(message)
                robot.send("$message\n")
            }
            delay(1L)
        }
    }

    suspend fun trackHandDetector() {
        if (handDetector.isInit()) {
            handDetector.client.dataHandler.collect {
                if (!it.contains("START")) {
                    buffer.add(it)
                }

                if (!isGame.value) {
                    isTrack = false
                    return@collect
                }
            }
        }
    }

    fun stopGame() {
        _isGame.value = false
        robot.signal(gameSignal, false)
    }
}