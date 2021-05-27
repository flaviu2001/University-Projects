package domain

class Room (val id: Int, val name: String, val capacity: Int) {
    override fun toString(): String {
        return "Room $name has $capacity available seats";
    }
}