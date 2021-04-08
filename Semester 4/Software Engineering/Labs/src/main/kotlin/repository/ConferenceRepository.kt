package repository

import domain.Conference
import java.sql.DriverManager

class ConferenceRepository (private val url: String, private val db_user: String, private val db_password: String){
    init {
        val sqlCreateTableQuery = """
                CREATE TABLE IF NOT EXISTS Conferences (
                    id INT PRIMARY KEY,
                    name VARCHAR(50),
                    date DATE,
                    attendancePrice INT
                )"""
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCreateTableQuery)
            preparedStatement.executeUpdate()
        }
    }

    fun findConferenceById(id: Int): Conference? {
        val sqlCommand = "SELECT * FROM Conferences WHERE id = ?"
        var conference: Conference? = null
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, id)
            val rs = preparedStatement.executeQuery()
            if (rs.next())
                conference = Conference(rs.getInt("id"), rs.getString("name"), rs.getDate("date"), rs.getInt("attendancePrice"))
        }
        return conference
    }

    fun getConferences(): List<Conference> {
        val conferences = mutableListOf<Conference>()
        val sqlCommand = "SELECT * FROM Conferences"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            val rs = preparedStatement.executeQuery()
            while (rs.next()) {
                val conference = Conference(rs.getInt("id"), rs.getString("name"), rs.getDate("date"), rs.getInt("attendancePrice"))
                conferences.add(conference)
            }
        }
        return conferences
    }

    fun addConference(entity: Conference) {
        val sqlCommand = "INSERT INTO Conferences (id, name, date, attendancePrice) VALUES (?, ?, ?, ?)"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, entity.id)
            preparedStatement.setString(2, entity.name)
            preparedStatement.setDate(3, entity.date)
            preparedStatement.setInt(4, entity.attendancePrice)
            preparedStatement.executeUpdate()
        }
    }
}