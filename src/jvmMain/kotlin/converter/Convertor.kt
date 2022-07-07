package converter

import entity.Point

fun convertHandDetectorValueToRobot(handDetectorValue: String): Point? {
    return if (handDetectorValue.isNotEmpty() && !handDetectorValue.contains("START")) {
        val handDetectorSplit = handDetectorValue.split(";")
        Point(
            handDetectorSplit[0],
            handDetectorSplit[1],
            handDetectorSplit[2],
        )
    } else {
        null
    }
}