package repository

import domain.User
import java.sql.DriverManager

class UserRepository (private val url: String, private val db_user: String, private val db_password: String) {
    init {
        val sqlCreateTableQuery = """
                CREATE TABLE IF NOT EXISTS Users (
                    id INT PRIMARY KEY,
                    name VARCHAR(50),
                    password VARCHAR(50),
                    email VARCHAR(50),
                    fullName VARCHAR(50),
                    affiliation VARCHAR(50),
                    personalWebsite VARCHAR(50),
                    domainOfInterest VARCHAR(50)
                )"""
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCreateTableQuery)
            preparedStatement.executeUpdate()
        }
    }

    fun findUserById(id: Int): User? {
        val sqlCommand = "SELECT * FROM Users WHERE id = $id"
        var user: User? = null
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            val rs = preparedStatement.executeQuery()
            if (rs.next())
                user = User(rs.getInt("id"), rs.getString("name"),
                    rs.getString("password"), rs.getString("email"),
                    rs.getString("fullName"), rs.getString("affiliation"),
                    rs.getString("personalWebsite"), rs.getString("domainOfInterest"))
        }
        return user
    }

    fun getUsers(): List<User> {
        val users = mutableListOf<User>()
        val sqlCommand = "SELECT * FROM Users"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            val rs = preparedStatement.executeQuery()
            while (rs.next()) {
                val user = User(rs.getInt("id"), rs.getString("name"),
                    rs.getString("password"), rs.getString("email"),
                    rs.getString("fullName"), rs.getString("affiliation"),
                    rs.getString("personalWebsite"), rs.getString("domainOfInterest"))
                users.add(user)
            }
        }
        return users
    }

    fun addUser(user: User) {
        val sqlCommand = "INSERT INTO Users (id, name, password, email, fullName, affiliation, personalWebsite, domainOfInterest) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, user.id)
            preparedStatement.setString(2, user.name)
            preparedStatement.setString(3, user.password)
            preparedStatement.setString(4, user.email)
            preparedStatement.setString(5, user.fullName)
            preparedStatement.setString(6, user.affiliation)
            preparedStatement.setString(7, user.personalWebsite)
            preparedStatement.setString(8, user.domainOfInterest)
            preparedStatement.executeUpdate()
        }
    }
}
