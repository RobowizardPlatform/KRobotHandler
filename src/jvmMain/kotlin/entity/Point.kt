package entity

class Point(
    val x: String,
    val y: String,
    val z: String,
) {
    override fun toString(): String {
        return "$x,$y,$z"
    }
}