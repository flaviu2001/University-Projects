package repository

import domain.Role
import domain.UserConference
import java.sql.DriverManager

class UserConferenceRepository (private val url: String, private val db_user: String, private val db_password: String) {
    init {
        val sqlCreateTableQuery = """
                CREATE TABLE IF NOT EXISTS UserConference (
                    ucid INT PRIMARY KEY,
                    uid INT REFERENCES Users(id),
                    cid INT REFERENCES Conferences(id),
                    role VARCHAR(50),
                    paid BOOLEAN
                )"""
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCreateTableQuery)
            preparedStatement.executeUpdate()
        }
    }

    fun getRolesOfUser(uid: Int, cid: Int): List<String>{
        val roles = mutableListOf<String>()
        val sqlCommand = "SELECT role FROM UserConference WHERE uid=? AND cid=?"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, uid)
            preparedStatement.setInt(2, cid)
            val rs = preparedStatement.executeQuery()
            while (rs.next()) {
                roles.add(rs.getString("role"))
            }
        }
        return roles
    }

    fun findUserConference(uid: Int, cid: Int): UserConference?{
        val sqlCommand = "SELECT * FROM UserConference WHERE uid=? AND cid=?"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, uid)
            preparedStatement.setInt(2, cid)
            val rs = preparedStatement.executeQuery()
            if (rs.next()) {
                return UserConference(rs.getInt("ucid"), rs.getInt("uid"), rs.getInt("cid"), Role.valueOf(rs.getString("role")), rs.getBoolean("paid"))
            }
        }
        return null
    }

    fun pay(uid: Int, cid: Int) {
        val sqlCommand = "UPDATE UserConference SET paid = true WHERE uid=? AND cid=?"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, uid)
            preparedStatement.setInt(2, cid)
            preparedStatement.executeUpdate()
        }
    }

    fun getConferencesOfUser(uid: Int) : List<UserConference> {
        val pairs = mutableListOf<UserConference>()
        val sqlCommand = "SELECT * FROM UserConference WHERE uid=?"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, uid)
            val rs = preparedStatement.executeQuery()
            while (rs.next()) {
                pairs.add(UserConference(rs.getInt("ucid"), rs.getInt("uid"), rs.getInt("cid"), Role.valueOf(rs.getString("role")), rs.getBoolean("paid")))
            }
        }
        return pairs
    }

    fun getAll(): List<UserConference> {
        val pairs = mutableListOf<UserConference>()
        val sqlCommand = "SELECT * FROM UserConference"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            val rs = preparedStatement.executeQuery()
            while (rs.next())
                pairs.add(UserConference(rs.getInt("ucid"), rs.getInt("uid"), rs.getInt("cid"), Role.valueOf(rs.getString("role")), rs.getBoolean("paid")))
        }
        return pairs
    }

    fun changeAuthorToSpeaker(userConference: UserConference) {
        val sqlCommand = "UPDATE UserConference SET role = ? WHERE ucid = ?"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setString(1, Role.SPEAKER.name)
            preparedStatement.setInt(2, userConference.id)
            preparedStatement.executeUpdate()
        }
    }
    fun addPair(userConference: UserConference) {
        val sqlCommand = "INSERT INTO UserConference (ucid, uid, cid, role, paid) VALUES (?, ?, ?, ?, ?)"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, userConference.id)
            preparedStatement.setInt(2, userConference.userId)
            preparedStatement.setInt(3, userConference.conferenceId)
            preparedStatement.setString(4, userConference.role.name)
            preparedStatement.setBoolean(5, userConference.paid)
            preparedStatement.executeUpdate()
        }
    }

    fun changeSpeakerToAuthor(uid: Int, cid: Int) {
        val sqlCommand = "UPDATE UserConference SET role=? WHERE uid=? AND cid=? AND role=?"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setString(1, Role.AUTHOR.name)
            preparedStatement.setInt(2, uid)
            preparedStatement.setInt(3, cid)
            preparedStatement.setString(4, Role.SPEAKER.name)
            preparedStatement.executeUpdate()
        }
    }
}
