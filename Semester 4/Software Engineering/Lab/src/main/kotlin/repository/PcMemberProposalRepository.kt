package repository

import domain.Availability
import domain.PCMemberProposal
import exceptions.ConferenceException
import java.sql.DriverManager

class PcMemberProposalRepository (private val url: String, private val db_user: String, private val db_password: String){
    init {
        val sqlCreateTableQuery = """
                CREATE TABLE IF NOT EXISTS PcMemberProposal (
                    pcMemberId INT REFERENCES  Users(id),
                    proposalId INT REFERENCES Proposals(id),
                    availability VARCHAR(50),
                    assigned BOOLEAN,
                    PRIMARY KEY(pcMemberId, proposalId)
                )"""
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCreateTableQuery)
            preparedStatement.executeUpdate()
        }
    }

    fun getAll(): List<PCMemberProposal> {
        val pairs = mutableListOf<PCMemberProposal>()
        val sqlCommand = "SELECT * FROM PcMemberProposal"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            val rs = preparedStatement.executeQuery()
            while (rs.next())
                pairs.add(PCMemberProposal(
                    rs.getInt("pcMemberId"),
                    rs.getInt("proposalId"),
                    Availability.valueOf(rs.getString("availability")),
                    rs.getBoolean("assigned")
                ))
        }
        return pairs
    }

    fun addPair(pcMemberProposal: PCMemberProposal) {
        val sqlCommand = "INSERT INTO PcMemberProposal (pcMemberId, proposalId, availability, assigned) VALUES (?, ?, ?, ?)"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, pcMemberProposal.pcMemberId)
            preparedStatement.setInt(2, pcMemberProposal.proposalId)
            preparedStatement.setString(3, pcMemberProposal.availability.name)
            preparedStatement.setBoolean(4, pcMemberProposal.assigned)
            preparedStatement.executeUpdate()
        }
    }

    fun assignPaper(proposal: Int, memberId: Int){
        print(proposal)
        print("\n")
        print(memberId)
        val sqlCommand = "UPDATE  PcMemberProposal SET assigned = true WHERE pcmemberid = ? AND proposalid = ? AND AVAILABILITY != 'REFUSE'"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, memberId)
            preparedStatement.setInt(2, proposal)
            if(preparedStatement.executeUpdate().equals(0))
                throw ConferenceException("Paper was not assigned!")
        }
    }

    fun getPcMemberProposalsOfConferenceNotRefused(conferenceId: Int): List<PCMemberProposal> {
        val pairs = mutableListOf<PCMemberProposal>()
        //val sqlCommand = "SELECT PMP.*, p.title FROM PcMemberProposal PMP JOIN proposals p on p.id = PMP.proposalid JOIN userconference u on p.ucid = u.ucid WHERE u.cid = ? AND PMP.availability != 'REFUSE'"
        val sqlCommand = "SELECT PMP.*, p.title, usr.name " +
                "FROM PcMemberProposal PMP " +
                "    JOIN proposals p on p.id = PMP.proposalid " +
                "    JOIN userconference u on p.ucid = u.ucid " +
                "    JOIN users usr on usr.id = PMP.pcmemberid " +
                "WHERE u.cid = ? AND PMP.availability != 'REFUSE' ORDER BY usr.name"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, conferenceId)
            val rs = preparedStatement.executeQuery()
            while (rs.next())
                pairs.add(PCMemberProposal(
                    rs.getInt("pcMemberId"),
                    rs.getInt("proposalId"),
                    Availability.valueOf(rs.getString("availability")),
                    rs.getBoolean("assigned"),
                    rs.getString("name"),
                    rs.getString("title")
                ))
        }
        return pairs
    }

    fun refusePaperForUser(pcMemberId: Int, proposalId: Int){
        val sqlCommand = """
           UPDATE pcmemberproposal
            SET availability='REFUSE'
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
