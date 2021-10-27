package repository

import domain.Room
import domain.Session
import java.sql.DriverManager

class SessionRepository(private val url: String, private val db_user: String, private val db_password: String) {
    init {
        val sqlCreateTableQuery = """
                CREATE TABLE IF NOT EXISTS Sessions (
                    sessionId INT PRIMARY KEY,
                    conferenceId INT REFERENCES Conferences(id),
                    topic VARCHAR(50),
                    participantsLimit INT, 
                    roomId INT REFERENCES Rooms(id)
                )"""
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCreateTableQuery)
            preparedStatement.executeUpdate()
        }

    }

    fun getSessions(): List<Session> {
        val sessions = mutableListOf<Session>()
        val sqlCommand = "SELECT * FROM Sessions"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            val rs = preparedStatement.executeQuery()
            while (rs.next()) {
                val session = Session(rs.getInt("sessionId"), rs.getInt("conferenceId"), rs.getString("topic"), rs.getInt("participantsLimit"), rs.getInt("roomId"))
                sessions.add(session)
            }
        }
        return sessions
    }

    fun findSessionsByConferenceId(conferenceId: Int): List<Session> {
        val sqlCommand = "SELECT * FROM Sessions WHERE conferenceId = ?"
        val sessions = mutableListOf<Session>()
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, conferenceId)
            val rs = preparedStatement.executeQuery()
            while (rs.next()) {
                val session = Session(rs.getInt("sessionId"), rs.getInt("conferenceId"), rs.getString("topic"), rs.getInt("participantsLimit"), rs.getInt("roomId"))
                sessions.add(session)
            }
        }
        return sessions
    }

    fun addSession(entity: Session) {
                val sqlCommand = "INSERT INTO Sessions (sessionId, conferenceId, topic, participantsLimit) VALUES (?, ?, ?, ?)"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, entity.sessionId)
            preparedStatement.setInt(2, entity.conferenceId)
            preparedStatement.setString(3, entity.topic)
            preparedStatement.setInt(4, entity.participantsLimit)
            preparedStatement.executeUpdate()
        }
    }

    fun findSessionsByConferenceIdWithNoRoomsAssigned(conferenceId: Int): List<Session> {
        val sqlCommand = """
            SELECT * 
            FROM Sessions 
            WHERE (conferenceId = ?) AND (roomid IS NULL)
        """.trimIndent()
        val sessions = mutableListOf<Session>()
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, conferenceId)
            val rs = preparedStatement.executeQuery()
            while (rs.next()) {
                val session = Session(rs.getInt("sessionId"), rs.getInt("conferenceId"), rs.getString("topic"), rs.getInt("participantsLimit"))
                sessions.add(session)
            }
        }
        return sessions
    }

    fun getRoomOfSession(sid: Int): Room?{
        var room: Room? = null;
        val sqlCommand = """
            SELECT r.* 
            FROM Sessions INNER JOIN rooms r on sessions.roomid = r.id
            WHERE sessions.sessionid = ?
        """.trimIndent()
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, sid)
            val rs = preparedStatement.executeQuery()
            if (rs.next()) {
                room = Room(rs.getInt("id"), rs.getString("name"), rs.getInt("capacity"))
            }
        }
        return room
    }

    fun assignRoomToSession(sessionId: Int, roomId: Int){
        val sqlCommand = """
            UPDATE Sessions 
            SET roomid = ?  
            WHERE sessionId = ?
        """.trimIndent()
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, roomId)
            preparedStatement.setInt(2, sessionId)
            preparedStatement.executeUpdate()
        }
    }
}
