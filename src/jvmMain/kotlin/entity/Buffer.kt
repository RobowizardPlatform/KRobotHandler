package entity

import converter.convertHandDetectorValueToRobot
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class Buffer(private val bufferSize: Int) {
    private val buffer: Queue<String> = ConcurrentLinkedQueue();

    fun add(line: String) {
        buffer.poll()
        buffer.add(line)
    }

    fun getMiddle(): Point {
        val points = mutableListOf<Point>()
        for (pointStr in buffer) {
            val point = convertHandDetectorValueToRobot(pointStr)
            if (point != null) {
                points.add(point)
            }
        }
        var fullX = 0.0
        var fullY = 0.0
        var fullZ = 0.0

        points.forEach {
            fullX += it.x.toDouble()
            fullY += it.y.toDouble()
            fullZ += it.z.toDouble()
        }

        return Point(
            (fullX / bufferSize).toString(),
            (fullY / bufferSize).toString(),
            (fullZ / bufferSize).toString(),
        )
    }


}