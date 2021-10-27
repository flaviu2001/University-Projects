package repository

import domain.UserSection
import java.sql.DriverManager

class UserSectionRepository(private val url: String, private val db_user: String, private val db_password: String) {
    init {
        val sqlCreateTableQuery = """
                CREATE TABLE IF NOT EXISTS UserSection (
                    uid INT REFERENCES Users(id),
                    sid INT REFERENCES Sessions(sessionId),
                    primary key (uid, sid)
                )"""
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCreateTableQuery)
            preparedStatement.executeUpdate()
        }
    }

    fun getSectionsOfUser(uid: Int): List<Int> {
        val pairs = mutableListOf<Int>()
        val sqlCommand = "SELECT sid FROM UserSection where uid = ?"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, uid)
            val rs = preparedStatement.executeQuery()

            while (rs.next())
                pairs.add(rs.getInt("sid"))
        }
        return pairs
    }

    fun getUsersOfSession(sid: Int): List<Int>{
        val pairs = mutableListOf<Int>()
        val sqlCommand = "SELECT uid FROM UserSection where sid = ?"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, sid)
            val rs = preparedStatement.executeQuery()

            while (rs.next())
                pairs.add(rs.getInt("uid"))
        }
        return pairs
    }

    fun addPair(userSection: UserSection) {
        val sqlCommand = "INSERT INTO UserSection (uid, sid) VALUES (?, ?)"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, userSection.userId)
            preparedStatement.setInt(2, userSection.sectionId)
            preparedStatement.executeUpdate()
        }
    }
}