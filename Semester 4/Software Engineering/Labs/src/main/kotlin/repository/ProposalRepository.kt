package repository

import domain.Proposal
import exceptions.ConferenceException
import java.sql.DriverManager

class ProposalRepository(private val url: String, private val db_user: String, private val db_password: String) {
    init {
        val sqlCreateTableQuery = """
                CREATE TABLE IF NOT EXISTS Proposals (
                    id INT PRIMARY KEY,
                    ucid INT REFERENCES userconference(ucid),
                    abstractText VARCHAR(1000),
                    paperText varchar(1000),
                    title varchar(50),
                    authors varchar(100),
                    keywords varchar(50),
                    finalized BOOLEAN,
                    accepted BOOLEAN
                )"""
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCreateTableQuery)
            preparedStatement.executeUpdate()
        }
    }

    fun addProposal(proposal: Proposal) {
        val sqlCommand =
            "INSERT INTO Proposals (id, ucid, abstractText, paperText, title, authors, keywords, finalized, accepted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, proposal.id)
            preparedStatement.setInt(2, proposal.userConferenceId)
            preparedStatement.setString(3, proposal.abstractText)
            preparedStatement.setString(4, proposal.paperText)
            preparedStatement.setString(5, proposal.title)
            preparedStatement.setString(6, proposal.authors)
            preparedStatement.setString(7, proposal.keywords)
            preparedStatement.setBoolean(8, proposal.finalized)
            preparedStatement.setBoolean(9, proposal.accepted)
            preparedStatement.executeUpdate()
        }
    }

    fun updateProposal(proposal: Proposal) {
        val sqlCommand =
            "UPDATE Proposals SET ucid=?, abstractText=?, paperText=?, title=?, authors=?, keywords=?, finalized=?, accepted=? WHERE id=?"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, proposal.userConferenceId)
            preparedStatement.setString(2, proposal.abstractText)
            preparedStatement.setString(3, proposal.paperText)
            preparedStatement.setString(4, proposal.title)
            preparedStatement.setString(5, proposal.authors)
            preparedStatement.setString(6, proposal.keywords)
            preparedStatement.setBoolean(7, proposal.finalized)
            preparedStatement.setBoolean(8, proposal.accepted)
            preparedStatement.setInt(9, proposal.id)
            preparedStatement.executeUpdate()
        }
    }

    fun getProposals(): List<Proposal> {
        val proposals = mutableListOf<Proposal>()
        val sqlCommand = "SELECT * FROM Proposals"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            val rs = preparedStatement.executeQuery()
            while (rs.next()) {
                val proposal = Proposal(
                    rs.getInt("id"),
                    rs.getInt("ucid"),
                    rs.getString("abstractText"),
                    rs.getString("paperText"),
                    rs.getString("title"),
                    rs.getString("authors"),
                    rs.getString("keywords"),
                    rs.getBoolean("finalized"),
                    rs.getBoolean("accepted")
                )
                proposals.add(proposal)
            }
        }
        return proposals
    }

    fun getProposalsForPcMember(pcMemberId: Int, conferenceId: Int): List<Proposal> {
        val proposals = mutableListOf<Proposal>()
        val sqlCommand = "SELECT P.* FROM Proposals P JOIN userconference UC on P.ucid = UC.ucid WHERE UC.cid = ? AND P.ucid NOT IN (SELECT ucid FROM UserConference WHERE uid=? AND cid=?)"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, conferenceId)
            preparedStatement.setInt(2, pcMemberId)
            preparedStatement.setInt(3, conferenceId)

            val rs = preparedStatement.executeQuery()
            while (rs.next()) {
                val proposal = Proposal(
                    rs.getInt("id"),
                    rs.getInt("ucid"),
                    rs.getString("abstractText"),
                    rs.getString("paperText"),
                    rs.getString("title"),
                    rs.getString("authors"),
                    rs.getString("keywords"),
                    rs.getBoolean("finalized"),
                    rs.getBoolean("accepted")
                )
                proposals.add(proposal)
            }
        }
        return proposals
    }

    fun getProposalsOfConference(conferenceId: Int): List<Proposal> {
        val proposals = mutableListOf<Proposal>()
        val sqlCommand = "SELECT P.* FROM Proposals P JOIN userconference UC on P.ucid = UC.ucid WHERE UC.cid = ?"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, conferenceId)

            val rs = preparedStatement.executeQuery()
            while (rs.next()) {
                val proposal = Proposal(
                    rs.getInt("id"),
                    rs.getInt("ucid"),
                    rs.getString("abstractText"),
                    rs.getString("paperText"),
                    rs.getString("title"),
                    rs.getString("authors"),
                    rs.getString("keywords"),
                    rs.getBoolean("finalized"),
                    rs.getBoolean("accepted")
                )
                proposals.add(proposal)
            }
        }
        return proposals
    }

    fun getProposalsOfUser(userId: Int): List<Proposal> {
        val proposals = mutableListOf<Proposal>()
        val sqlCommand =
            "SELECT * FROM Proposals WHERE ucid IN (SELECT userconference.ucid FROM UserConference WHERE UserConference.uid = ?)"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, userId)
            val rs = preparedStatement.executeQuery()
            while (rs.next()) {
                val proposal = Proposal(
                    rs.getInt("id"),
                    rs.getInt("ucid"),
                    rs.getString("abstractText"),
                    rs.getString("paperText"),
                    rs.getString("title"),
                    rs.getString("authors"),
                    rs.getString("keywords"),
                    rs.getBoolean("finalized"),
                    rs.getBoolean("accepted")
                )
                proposals.add(proposal)
            }
        }
        return proposals
    }

    fun getProposalWithGivenId(id : Int): Proposal {
        var sqlCommand = "SELECT * FROM Proposals WHERE id= ?";
        DriverManager.getConnection(url, db_user, db_password).use{connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, id)
            val resultSet = preparedStatement.executeQuery()
            if(resultSet.next()){
                val proposal = Proposal(
                    resultSet.getInt("id"),
                    resultSet.getInt("ucid"),
                    resultSet.getString("abstractText"),
                    resultSet.getString("paperText"),
                    resultSet.getString("title"),
                    resultSet.getString("authors"),
                    resultSet.getString("keywords"),
                    resultSet.getBoolean("finalized"),
                    resultSet.getBoolean("accepted")
                )

                return proposal
            }
            throw ConferenceException("Proposal with given id does not exist")
        }

    }
}