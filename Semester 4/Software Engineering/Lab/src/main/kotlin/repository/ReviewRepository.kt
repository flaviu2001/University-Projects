package repository

import domain.Review
import domain.ReviewResult
import java.sql.DriverManager

class ReviewRepository(private val url: String, private val db_user: String, private val db_password: String) {
    init {
        val sqlCreateTableQuery = """
                CREATE TABLE IF NOT EXISTS Reviews (
                    pcMemberId INT REFERENCES  Users(id),
                    proposalId INT REFERENCES Proposals(id),
                    result VARCHAR(50),
                    PRIMARY KEY(pcMemberId, proposalId)
                )"""
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCreateTableQuery)
            preparedStatement.executeUpdate()
        }
    }

    fun getAll(): List<Review> {
        val pairs = mutableListOf<Review>()
        val sqlCommand = "SELECT * FROM Reviews"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            val rs = preparedStatement.executeQuery()
            while (rs.next())
                pairs.add(
                    Review(
                        rs.getInt("pcMemberId"),
                        rs.getInt("proposalId"),
                        ReviewResult.valueOf(rs.getString("result"))
                    )
                )
        }
        return pairs
    }

    fun addPair(review: Review) {
        val sqlCommand = "INSERT INTO Reviews (pcMemberId, proposalId, result) VALUES (?, ?, ?)"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, review.pcMemberId)
            preparedStatement.setInt(2, review.proposalId)
            preparedStatement.setString(3, review.reviewResult.name)
            preparedStatement.executeUpdate()
        }
    }

    fun updatePair(review: Review) {
        val sqlCommand = "UPDATE Reviews SET result = ? WHERE pcMemberId = ? AND proposalId = ?"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setString(1, review.reviewResult.name)
            preparedStatement.setInt(2, review.pcMemberId)
            preparedStatement.setInt(3, review.proposalId)
            preparedStatement.executeUpdate()
        }
    }

    fun dropReview(pcMemberId: Int, proposalId: Int){
        val sqlCommand = """
           DELETE FROM reviews
            WHERE pcmemberid=? AND proposalid=?
        """
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, pcMemberId)
            preparedStatement.setInt(2, proposalId)
            preparedStatement.executeUpdate()
        }
    }
}
