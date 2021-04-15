package service

import domain.*
import exceptions.ConferenceException
import repository.*
import java.io.FileInputStream
import java.io.IOException
import java.lang.Integer.max
import java.sql.Date
import java.util.*
import kotlin.streams.toList

class Service {
    private val userRepository: UserRepository
    private val conferenceRepository: ConferenceRepository
    private val userConferenceRepository: UserConferenceRepository
    private val proposalRepository: ProposalRepository
    private val pcMemberProposalRepository: PcMemberProposalRepository
    private val reviewRepository: ReviewRepository

    init {
        val configs = readSettingsFile()
        userRepository = UserRepository(configs["database"]!!, configs["user"]!!, configs["password"]!!)
        conferenceRepository = ConferenceRepository(configs["database"]!!, configs["user"]!!, configs["password"]!!)
        userConferenceRepository =
            UserConferenceRepository(configs["database"]!!, configs["user"]!!, configs["password"]!!)
        proposalRepository = ProposalRepository(configs["database"]!!, configs["user"]!!, configs["password"]!!)
        pcMemberProposalRepository = PcMemberProposalRepository(configs["database"]!!, configs["user"]!!, configs["password"]!!)
        reviewRepository = ReviewRepository(configs["database"]!!, configs["user"]!!, configs["password"]!!)
    }

    private fun readSettingsFile(): HashMap<String, String> {
        val propertiesMap = HashMap<String, String>()
        val properties = Properties()
        val configFile = "data/settings.properties"
        val fileInputStream: FileInputStream = try {
            FileInputStream(configFile)
        } catch (exception: IOException) {
            println(exception.message)
            return propertiesMap
        }
        try {
            properties.load(fileInputStream)
            propertiesMap["database"] = properties.getProperty("database")
            propertiesMap["user"] = properties.getProperty("user")
            propertiesMap["password"] = properties.getProperty("password")
        } catch (ioException: IOException) {
            println("IOException: " + ioException.message)
        }
        return propertiesMap
    }

    private fun getUsers() = userRepository.getUsers()
    fun findUserById(id: Int) = userRepository.findUserById(id)
    fun addUser(
        name: String, password: String, email: String, fullName: String, affiliation: String,
        personalWebsite: String, domainOfInterest: String
    ) {
        if (isUsernameExistent(name))
            throw ConferenceException("Username already exists")

        var id = 0
        for (user in userRepository.getUsers()) id = max(id, user.id + 1)

        userRepository.addUser(
            User(
                id,
                name,
                password,
                email,
                fullName,
                affiliation,
                personalWebsite,
                domainOfInterest
            )
        )
    }

    fun findConferenceById(id: Int) = conferenceRepository.findConferenceById(id)
    fun getConferences() = conferenceRepository.getConferences()
    fun addConference(name: String, date: Date, attendancePrice: Int) {
        var id = 0
        for (conference in conferenceRepository.getConferences()) id = max(id, conference.id + 1)
        conferenceRepository.addConference(Conference(id, name, date, attendancePrice))
    }

    fun getConferencesOfUser(uid: Int) = userConferenceRepository.getConferencesOfUser(uid)
    fun getUsersOfConference(cid: Int) = userConferenceRepository.getUsersOfConference(cid)
    fun addUserToConference(uid: Int, cid: Int, role: Role, paid: Boolean) = userConferenceRepository.addPair(
        UserConference(
            (userConferenceRepository.getAll().map { userConference -> userConference.id }.maxOrNull() ?: 0) + 1,
            uid,
            cid,
            role,
            paid
        )
    )

    fun usersWithNameAndPassword(username: String, password: String): List<User> {
        return getUsers().stream().filter {
            (it.name == username) and (it.password == password)
        }.toList()
    }

    fun getEmailOfUser(username: String): String {
        return getUsers().stream().filter {
            (it.name == username)
        }.findFirst().get().email
    }

    fun isUsernameExistent(username: String): Boolean {
        return getUsers().stream().filter {
            (it.name == username)
        }.toList().isNotEmpty()
    }

    fun getIdOfUsername(username: String): Int {
        return getUsers().stream().filter {
            (it.name == username)
        }.findFirst().get().id
    }

    fun addProposal(
        userConferenceId: Int,
        abstractText: String,
        paperText: String,
        title: String,
        authors: String,
        keywords: String,
        accepted: Boolean = false
    ) =
        proposalRepository.addProposal(Proposal((proposalRepository.getProposals().map { proposal -> proposal.id }
            .maxOrNull()?: 0)+ 1, userConferenceId, abstractText, paperText, title, authors, keywords, accepted))

    fun getProposals() = proposalRepository.getProposals()

    fun updateProposal(
        id: Int, userConferenceId: Int,
        abstractText: String,
        paperText: String,
        title: String,
        authors: String,
        keywords: String,
        accepted: Boolean = false
    ) {
        proposalRepository.updateProposal(
            Proposal(
                id,
                userConferenceId,
                abstractText,
                paperText,
                title,
                authors,
                keywords,
                accepted
            )
        )
    }

    fun addPcMemberProposal(pcMemberId: Int, proposalId: Int, availability: Availability){
        pcMemberProposalRepository.addPair(PCMemberProposal(pcMemberId, proposalId, availability, false))
    }

    fun addReview(pcMemberId: Int, proposalId: Int, reviewResult: ReviewResult){
        reviewRepository.addPair(Review(pcMemberId, proposalId, reviewResult));
    }


    fun getProposalsOfUser(uid: Int) = proposalRepository.getProposalsOfUser(uid)

    fun getRolesOfUser(uid: Int, cid: Int) = userConferenceRepository.getRolesOfUser(uid, cid)

    fun getProposalsForPcMember(pcMemberId: Int, conferenceId: Int) = proposalRepository.getProposalsForPcMember(pcMemberId, conferenceId)

    fun getReviewsForPaper(proposalId: Int): List<Review> {
        return reviewRepository.getAll().filter {
            (it.proposalId == proposalId)
        }.toList()
    }

    fun getAssignedPapers(pcMemberId: Int) : List<Proposal>{
        val proposalIds :List<Int> = pcMemberProposalRepository
            .getAll()
            .stream()
            .filter{
                (it.pcMemberId == pcMemberId && it.assigned)
            }
            .map {
                it.proposalId
            }
            .toList()
        return proposalIds.map { proposalRepository.getProposalWithGivenId(it) }.toList()
    }

    fun assignPaper(proposalId: Int, reviewerId: Int){
        pcMemberProposalRepository.assignPaper(proposalId, reviewerId)
    }
}