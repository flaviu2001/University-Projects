package repository

import domain.Conference
import domain.Message
import exceptions.ConferenceException
import java.sql.DriverManager

class ChatRepository(private val url: String, private val db_user: String, private val db_password: String) {
    init {
        val sqlCreateChatRoomsTableQuery = """
                CREATE TABLE IF NOT EXISTS ChatRoomUser (
                    proposalId INT REFERENCES proposals(id),
                    userId INT REFERENCES users(id),
                    PRIMARY KEY (proposalId, userId)
                )"""

        val sqlCreateMessageTableQuery = """
                CREATE TABLE IF NOT EXISTS Messages (
                    messageId INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                    text VARCHAR(100),
                    proposalId INT REFERENCES proposals(id), 
                    userId INT REFERENCES users(id)
                )"""
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            var preparedStatement = connection.prepareStatement(sqlCreateChatRoomsTableQuery)
            preparedStatement.executeUpdate()

            preparedStatement = connection.prepareStatement(sqlCreateMessageTableQuery)
            preparedStatement.executeUpdate()
        }

    }

    fun addUserToChatRoom(userId: Int, proposalId: Int){
        val sqlCommand = "INSERT INTO ChatRoomUser (userId, proposalId) VALUES (?, ?)"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, userId)
            preparedStatement.setInt(2, proposalId)
            preparedStatement.executeUpdate()
        }
    }

    fun getChatRoomsOfUser(userId: Int): List<Int>{
        val proposalIds = mutableListOf<Int>()
        val sqlCommand = "SELECT proposalid FROM chatroomuser WHERE userid = ?"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, userId)
            val rs = preparedStatement.executeQuery()
            while (rs.next()) {
                proposalIds.add(rs.getInt("proposalid"))
            }
        }
        return proposalIds
    }

    fun getMessagesOfChatRoom(proposalId: Int): List<Message>{
        val messages = mutableListOf<Message>()
        val sqlCommand = """
            SELECT m.*, u.name as username FROM messages m 
            INNER JOIN users u ON m.userid = u.id
            WHERE proposalid = ?
            ORDER BY messageid
        """
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, proposalId)
            val rs = preparedStatement.executeQuery()
            while (rs.next()) {
                messages.add(
                    Message(
                        rs.getString("text"),
                        rs.getInt("proposalid"),
                        rs.getInt("userid"),
                        rs.getString("username")
                )
                )
            }
        }
        return messages
    }

    fun postMessage(text: String, proposalId: Int, userId: Int){
        val sqlCommand = """
            INSERT INTO messages (text, proposalid, userid)
            VALUES (?, ?, ?)
        """
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setString(1, text)
            preparedStatement.setInt(2, proposalId)
            preparedStatement.setInt(3, userId)

            if(preparedStatement.executeUpdate().equals(0))
                throw ConferenceException("Message was not sent")
        }
    }
}
