package repository

import domain.Role
import domain.UserConference
import java.sql.DriverManager

class UserConferenceRepository (private val url: String, private val db_user: String, private val db_password: String) {
    init {
        val sqlCreateTableQuery = """
                CREATE TABLE IF NOT EXISTS UserConference (
                    uid INT REFERENCES Users(id),
                    cid INT REFERENCES Conferences(id),
                    role VARCHAR(50),
                    paid BOOLEAN,
                    primary key (uid, cid)
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
                pairs.add(UserConference(rs.getInt("uid"), rs.getInt("cid"), Role.valueOf(rs.getString("role")), rs.getBoolean("paid")))
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
            while (rs.next()) {
                pairs.add(UserConference(rs.getInt("uid"), rs.getInt("cid"), Role.valueOf(rs.getString("role")), rs.getBoolean("paid")))
            }
        }
        return pairs
    }

    fun addPair(uid: Int, cid: Int, role: Role, paid: Boolean) {
        val sqlCommand = "INSERT INTO UserConference (uid, cid, role, paid) VALUES (?, ?, ?, ?)"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, uid)
            preparedStatement.setInt(2, cid)
            preparedStatement.setString(3, role.name)
            preparedStatement.setBoolean(4, paid)
            preparedStatement.executeUpdate()
        }
    }
}