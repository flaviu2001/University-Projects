package ro.ubb.flaviu.models

enum class Color {
    WHITE,
    BLACK;
    fun otherColor(): Color {
        return if (this == WHITE) BLACK else WHITE
    }
}
