package domain

import java.sql.Date


data class Conference (val id: Int, val name: String, val date: Date, val attendancePrice: Int)