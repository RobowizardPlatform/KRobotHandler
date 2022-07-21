package entity

import converter.convertHandDetectorValueToRobot
import org.jetbrains.skiko.currentNanoTime

class Buffer(private val bufferSize: Int) {
    private val buffer = mutableListOf<String>()
    private var gripState = "0"
    private var middle: String? = null

    /**
     * Time during which gripper should not change its status
     */
    private var gripTime = 100L

    fun add(line: String) {
        if (!line.contains("START")) {
            buffer.add(line)
            updateGripStatus(line)

            middle = calculateMiddle()
        }
    }

    fun calculateMiddle(): String? {
        return if (buffer.size == bufferSize) {
            val points = mutableListOf<Point>()
            val l_buffer = buffer.toMutableList()
            for (line in l_buffer) {
                if (line.isNotEmpty()) {
                    val point = convertHandDetectorValueToRobot(line)
                    if (point != null) {
                        points.add(point)
                        println("POINTS: $point")
                    }
                } else {
                    return null
                }
            }


            var fullX = 0.0
            var fullY = 0.0
            var fullZ = 0.0

            points.forEach {
                fullX += modCoordinate(it.x.toFloat())
                fullY += modCoordinate(it.y.toFloat())
                fullZ += modCoordinate(it.z.toFloat())
            }

            val middlePoint = Point(
                (fullX / buffer.size).toString(),
                (fullY / buffer.size).toString(),
                (fullZ / buffer.size).toString(),
            )

            if (middlePoint.toString().contains("NaN")) {
                null
            } else {
                "$middlePoint,$gripState"
            }
        } else {
            null
        }
    }


    fun getLast(): String? {
        return if (buffer.size > 0) {
            val first = buffer.first()
            buffer.removeFirst()

            val values = first.split(";").map { it.trim().toFloat() }
            var message = ""
            values.forEachIndexed { index, coordinate ->
                message += if (index != values.lastIndex) {
                    "${modCoordinate(coordinate)},"
                } else {
                    ""
                }
            }
            "$message,$gripState,"
        } else {
            null
        }
    }


    private var lastTimeUpdate = currentNanoTime()
    private fun updateGripStatus(message: String) {
        currentNanoTime()
        val oldGripState = gripState == "1"
        val newGripState = message.split(";").last().trim() == "1"
        if (newGripState != oldGripState) {
            val timeDif = currentNanoTime() - lastTimeUpdate
            if (timeDif > gripTime) {
                lastTimeUpdate = currentNanoTime()
                gripState = if (newGripState) "1" else "0"
            }
        }
    }

    /**
     * Since the handDetector comes value from -1 to 1 and the robot
     * accepts values from 0 to 1, you need to pass this conversion.
     *
     * @param oldPos - value from hand detector (-1 to 1)
     *
     * @return value from robot (0 to 1)
     */
    private fun modCoordinate(oldPos: Float): Float {
        return (oldPos + 1f) / 2f
    }

    fun clear() {
        buffer.clear()
    }

    fun getMiddle(): String? {
        return middle
    }
}