package domain

import java.sql.Date


data class Conference (val id: Int, val name: String, val date: Date, val attendancePrice: Int, var submitPaperDeadline: Date, var reviewPaperDeadline: Date, var biddingPhaseDeadline: Date, var withFullPaper: Boolean) {
    override fun toString(): String {
        return "$name at $date costs $attendancePrice"
    }
}
