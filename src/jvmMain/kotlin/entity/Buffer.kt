package entity

import converter.convertHandDetectorValueToRobot

class Buffer(private val bufferSize: Int) {
    private val buffer = mutableListOf<String>()
    private var gripState = "0"
    private var middle: String? = null

    fun add(line: String) {
        buffer.add(line)
        gripState = line.split(";").last().trim()
        if (buffer.size > bufferSize) {
            buffer.removeFirst()
        }
        middle = calculateMiddle()
    }

    fun calculateMiddle(): String? {
        return if (buffer.size == bufferSize) {
            val points = mutableListOf<Point>()
            val l_buffer = buffer.toMutableList()
            for (line in l_buffer) {
                if (!line.isNullOrEmpty()) {
                    val pointStr = line
                    val point = convertHandDetectorValueToRobot(pointStr)
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
                fullX += (it.x.toDouble() + 1) / 2
                fullY += (it.y.toDouble() + 1) / 2
                fullZ += (it.z.toDouble() + 1) / 2
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
        val first = buffer.first()
        buffer.removeFirst()
        return first
    }

    fun clear() {
        buffer.clear()
    }

    fun getMiddle(): String? {
        return middle
    }
}