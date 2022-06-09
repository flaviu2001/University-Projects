package ro.ubb.flaviu.models

// a rank is a horizontal strip, a file is a vertical strip
data class Position(val rank: Byte, val file: Byte) {
    fun valid(): Boolean {
        return rank in 0..7 && file in 0..7
    }
    operator fun plus(increment: Direction): Position {
        return Position((rank + increment.rankModifier).toByte(), (file + increment.fileModifier).toByte())
    }

    override fun toString(): String = "${('a'.code + file).toChar()}${rank+1}"
}
