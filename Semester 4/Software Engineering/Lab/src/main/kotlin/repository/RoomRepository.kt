package repository

import domain.Room
import domain.Session
import java.sql.DriverManager

class RoomRepository(private val url: String, private val db_user: String, private val db_password: String) {
    init {
        val sqlCreateTableQuery = """
                CREATE TABLE IF NOT EXISTS Rooms (
                    id INT PRIMARY KEY,
                    name VARCHAR(50),
                    capacity INT
                )"""
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCreateTableQuery)
            preparedStatement.executeUpdate()
        }
    }

    fun getRooms(): List<Room> {
        val rooms = mutableListOf<Room>()
        val sqlCommand = "SELECT * FROM Rooms"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            val rs = preparedStatement.executeQuery()
            while (rs.next()) {
                val room = Room(rs.getInt("id"), rs.getString("name"), rs.getInt("capacity"))
                rooms.add(room)
            }
        }
        return rooms
    }

    fun getAvailableRooms(): List<Room> {
        val rooms = mutableListOf<Room>()
        val sqlCommand = """
            SELECT * 
            FROM Rooms 
            WHERE id NOT IN (
                SELECT roomid 
                FROM sessions 
                WHERE roomid IS NOT NULL
            )
        """.trimIndent()
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            val rs = preparedStatement.executeQuery()
            while (rs.next()) {
                val room = Room(rs.getInt("id"), rs.getString("name"), rs.getInt("capacity"))
                rooms.add(room)
            }
        }
        return rooms
    }
}
