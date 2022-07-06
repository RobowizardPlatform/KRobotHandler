package converter

import entity.Point

fun convertHandDetectorValueToRobot(handDetectorValue: String): Point? {
    val handDetectorSplit = handDetectorValue.split(" ")
    return if (handDetectorValue.isNotEmpty() && !handDetectorValue.contains("START")) {
        Point(
            handDetectorSplit[0],
            handDetectorSplit[1],
            handDetectorSplit[2],
        )
    } else {
        null
    }
}