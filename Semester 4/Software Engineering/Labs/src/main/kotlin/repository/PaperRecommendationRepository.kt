package repository

import domain.PaperRecommendation
import java.sql.DriverManager

class PaperRecommendationRepository(
    private val url: String,
    private val db_user: String,
    private val db_password: String
) {
    init {
        val sqlCreateTableQuery = """
                CREATE TABLE IF NOT EXISTS PaperRecommendation (
                    id INT PRIMARY KEY ,
                    proposalId INT REFERENCES Proposals(id),
                    reviewerId INT REFERENCES users(id),
                    recommendation text
                )"""
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCreateTableQuery)
            preparedStatement.executeUpdate()
        }
    }

    fun getAll(): List<PaperRecommendation> {
        val recommendations = mutableListOf<PaperRecommendation>()
        val sqlCommand = "SELECT * FROM PaperRecommendation"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            val rs = preparedStatement.executeQuery()
            while (rs.next()) {
                val paperRecommendation = PaperRecommendation(
                    id = rs.getInt("id"),
                    proposalId = rs.getInt("proposalId"),
                    reviewerId = rs.getInt("reviewerId"),
                    recommendation = rs.getString("recommendation")
                )
                recommendations.add(paperRecommendation)
            }
        }
        return recommendations
    }

    fun addPair(recommendation: PaperRecommendation) {
        val sqlCommand = "INSERT INTO PaperRecommendation(id, proposalid, reviewerId, recommendation) values (?, ?, ?, ?)"
        DriverManager.getConnection(url, db_user,db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand);
            preparedStatement.setInt(1, recommendation.id)
            preparedStatement.setInt(2, recommendation.proposalId)
            preparedStatement.setInt(3, recommendation.reviewerId)
            preparedStatement.setString(4, recommendation.recommendation)
            preparedStatement.executeUpdate()
        }

    }
}
