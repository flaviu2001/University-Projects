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

    fun getUsersOfConference(cid: Int) : List<UserConference> {
        val pairs = mutableListOf<UserConference>()
        val sqlCommand = "SELECT * FROM UserConference WHERE cid=?"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, cid)
            val rs = preparedStatement.executeQuery()
            while (rs.next())
                pairs.add(UserConference(rs.getInt("ucid"), rs.getInt("uid"), rs.getInt("cid"), Role.valueOf(rs.getString("role")), rs.getBoolean("paid")))
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
}