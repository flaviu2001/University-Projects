package repository

import domain.Conference
import domain.PCMemberProposal
import domain.ProposalSession
import java.sql.DriverManager

class ProposalSessionRepository(private val url: String, private val db_user: String, private val db_password: String) {
    init {
        val sqlCreateTableQuery = """
                CREATE TABLE IF NOT EXISTS ProposalSessions (
                    proposalId INT REFERENCES proposals(id),
                    sessionId INT REFERENCES sessions(sessionid),
                    time DATE,
                    PRIMARY KEY (proposalId, sessionId)
                )"""
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCreateTableQuery)
            preparedStatement.executeUpdate()
        }
    }

    fun getAllProposalSessions(): List<ProposalSession> {
        val proposalSessions = mutableListOf<ProposalSession>()
        val sqlCommand = "SELECT * FROM ProposalSessions"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            val rs = preparedStatement.executeQuery()
            while (rs.next()) {
                val proposalSession =
                    ProposalSession(rs.getInt("proposalId"), rs.getInt("sessionId"), rs.getDate("time"))
                proposalSessions.add(proposalSession)
            }
        }
        return proposalSessions
    }

    fun getProposalSessionsOfSession(sessionId: Int): List<ProposalSession> {
        val proposalSessions = mutableListOf<ProposalSession>()
        val sqlCommand = "SELECT * FROM ProposalSessions WHERE sessionId = ?"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, sessionId)
            val rs = preparedStatement.executeQuery()
            while (rs.next()) {
                val proposalSession =
                    ProposalSession(rs.getInt("proposalId"), rs.getInt("proposalId"), rs.getDate("time"))
                proposalSessions.add(proposalSession)
            }
        }
        return proposalSessions
    }

    fun addProposalSession(proposalSession: ProposalSession) {
        val sqlCommand = "INSERT INTO ProposalSessions (proposalid, sessionid, time) VALUES (?, ?, ?)"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, proposalSession.proposalId)
            preparedStatement.setInt(2, proposalSession.sessionId)
            preparedStatement.setDate(3, proposalSession.time)
            preparedStatement.executeUpdate()
        }
    }

}