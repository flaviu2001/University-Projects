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
    private val paperRecommendationRepository: PaperRecommendationRepository
    private val sessionRepository: SessionRepository
    private val proposalSessionRepository: ProposalSessionRepository
    private val userSectionRepository: UserSectionRepository
    private val roomsRepository: RoomRepository

    init {
        val configs = readSettingsFile()
        userRepository = UserRepository(configs["database"]!!, configs["user"]!!, configs["password"]!!)
        conferenceRepository = ConferenceRepository(configs["database"]!!, configs["user"]!!, configs["password"]!!)
        userConferenceRepository =
            UserConferenceRepository(configs["database"]!!, configs["user"]!!, configs["password"]!!)
        proposalRepository = ProposalRepository(configs["database"]!!, configs["user"]!!, configs["password"]!!)
        pcMemberProposalRepository =
            PcMemberProposalRepository(configs["database"]!!, configs["user"]!!, configs["password"]!!)
        reviewRepository = ReviewRepository(configs["database"]!!, configs["user"]!!, configs["password"]!!)
        paperRecommendationRepository =
            PaperRecommendationRepository(configs["database"]!!, configs["user"]!!, configs["password"]!!)
        roomsRepository = RoomRepository(configs["database"]!!, configs["user"]!!, configs["password"]!!)
        sessionRepository = SessionRepository(configs["database"]!!, configs["user"]!!, configs["password"]!!)
        proposalSessionRepository =
            ProposalSessionRepository(configs["database"]!!, configs["user"]!!, configs["password"]!!)
        userSectionRepository = UserSectionRepository(configs["database"]!!, configs["user"]!!, configs["password"]!!)
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

    fun getUsers() = userRepository.getUsers()
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

    fun getConferences() = conferenceRepository.getConferences()
    fun addConference(
        name: String,
        date: Date,
        attendancePrice: Int,
        submitPaperDeadline: Date,
        reviewPaperDeadline: Date,
        biddingPhaseDeadline: Date,
        withFullPaper: Boolean
    ) {
        var id = 0
        for (conference in conferenceRepository.getConferences()) id = max(id, conference.id + 1)
        conferenceRepository.addConference(
            Conference(
                id,
                name,
                date,
                attendancePrice,
                submitPaperDeadline,
                reviewPaperDeadline,
                biddingPhaseDeadline,
                withFullPaper
            )
        )
    }

    fun getConferencesOfUser(uid: Int) = userConferenceRepository.getConferencesOfUser(uid)

    fun getUserConference(uid: Int, cid: Int) = userConferenceRepository.findUserConference(uid, cid)

    fun pay(uid: Int, cid: Int) = userConferenceRepository.pay(uid, cid)

    fun addUserToConference(uid: Int, cid: Int, role: Role, paid: Boolean) {
        if (userConferenceRepository.getAll().any {
                it.userId == uid && it.conferenceId == cid && it.role == role
            }) throw ConferenceException("User is already reviewer to that conference")
        userConferenceRepository.addPair(
            UserConference(
                (userConferenceRepository.getAll().map { userConference -> userConference.id }.maxOrNull() ?: 0) + 1,
                uid,
                cid,
                role,
                paid
            )
        )

    }

    fun addUserToSection(uid: Int, sid: Int) = userSectionRepository.addPair(UserSection(uid, sid))

    fun getSectionsOfUser(uid: Int): List<Session> {
        val sids: List<Int> = userSectionRepository.getSectionsOfUser(uid)
        val sessions = sessionRepository.getSessions()

        val sectionsOfUser = mutableListOf<Session>()

        sids.forEach {
            val sessionId = it
            sectionsOfUser.add(sessions.first { session -> session.sessionId == sessionId })
        }

        return sectionsOfUser
    }

    fun getConferenceOfProposal(proposal: Proposal) = conferenceRepository.getConferenceOfProposal(proposal)

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
        finalized: Boolean = false,
        accepted: Boolean = false,
        fullPaperLocation: String = ""
    ) =
        proposalRepository.addProposal(Proposal((proposalRepository.getProposals().map { proposal -> proposal.id }
            .maxOrNull() ?: 0) + 1,
            userConferenceId,
            abstractText,
            paperText,
            title,
            authors,
            keywords,
            finalized,
            accepted,
            fullPaperLocation
        ))

    fun updateProposal(
        id: Int, userConferenceId: Int,
        abstractText: String,
        paperText: String,
        title: String,
        authors: String,
        keywords: String,
        finalized: Boolean = false,
        accepted: Boolean = false,
        fullPaperLocation: String = ""
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
                finalized,
                accepted,
                fullPaperLocation
            )
        )
    }

    fun addPcMemberProposal(pcMemberId: Int, proposalId: Int, availability: Availability) {
        pcMemberProposalRepository.addPair(PCMemberProposal(pcMemberId, proposalId, availability, false))
    }

    fun review(pcMemberId: Int, proposalId: Int, reviewResult: ReviewResult): Pair<Int, Int> {
        if (reviewRepository.getAll().any {
                it.pcMemberId == pcMemberId && it.proposalId == proposalId
            })
            reviewRepository.updatePair(Review(pcMemberId, proposalId, reviewResult))
        else
            reviewRepository.addPair(Review(pcMemberId, proposalId, reviewResult))
        var total = 0
        var needed = 0
        for (proposal in pcMemberProposalRepository.getAll())
            if (proposal.proposalId == proposalId && proposal.availability != Availability.REFUSE)
                ++total
        for (review in reviewRepository.getAll())
            if (review.proposalId == proposalId && review.reviewResult in listOf(
                    ReviewResult.STRONGLY_ACCEPT,
                    ReviewResult.ACCEPT,
                    ReviewResult.WEAK_ACCEPT,
                    ReviewResult.BORDERLINE
                )
            )
                ++needed
        if (needed == total) {
            val proposal = proposalRepository.getProposalWithGivenId(proposalId)
            proposal.accepted = true
            proposalRepository.updateProposal(proposal)
        }
        return Pair(needed, total)
    }


    fun getNonFinalizedProposalsOfUser(uid: Int) = proposalRepository.getProposalsOfUser(uid).filter {
        !it.finalized
    }

    fun getRolesOfUser(uid: Int, cid: Int) = userConferenceRepository.getRolesOfUser(uid, cid)

    fun makeUserSpeaker(user: User, conference: Conference) {
        val userConference = userConferenceRepository.getAll().find { userConference ->
            userConference.userId == user.id &&
                    userConference.conferenceId == conference.id &&
                    userConference.role == Role.AUTHOR &&
                    proposalRepository.getProposalsOfUser(user.id).any { proposal ->
                        proposal.accepted &&
                                getConferenceOfProposal(proposal)!!.id == conference.id
                    }
        }
        when {
            userConference != null -> {
                userConferenceRepository.changeAuthorToSpeaker(userConference)
            }
            userConferenceRepository.getAll().any {
                it.userId == user.id &&
                        it.conferenceId == conference.id &&
                        it.role == Role.SPEAKER
            } -> throw ConferenceException("This user is already registered as a speaker to this conference")
            else -> throw ConferenceException("This user has no accepted papers to this conference")
        }
    }

    fun unmakeUserSpeaker(user: User, conference: Conference) {
        if (userConferenceRepository.getAll().any { userConference ->
                userConference.userId == user.id &&
                        userConference.conferenceId == conference.id &&
                        userConference.role == Role.SPEAKER
            }) {
            userConferenceRepository.changeSpeakerToAuthor(user.id, conference.id)
        } else throw ConferenceException("This user is not registered as a speaker to this conference")
    }

    fun getProposalsForPcMember(pcMemberId: Int, conferenceId: Int) =
        proposalRepository.getProposalsForPcMember(pcMemberId, conferenceId)

    fun getReviewsForPaper(proposalId: Int): List<Review> {
        return reviewRepository.getAll().filter {
            (it.proposalId == proposalId)
        }.toList()
    }

    fun getAssignedPapers(pcMemberId: Int): List<Proposal> {
        val proposalIds: List<Int> = pcMemberProposalRepository
            .getAll()
            .stream()
            .filter {
                (it.pcMemberId == pcMemberId && it.assigned)
            }
            .map {
                it.proposalId
            }
            .toList()
        return proposalIds.map { proposalRepository.getProposalWithGivenId(it) }.toList()
    }

    fun assignPaper(proposalId: Int, reviewerId: Int) {
        pcMemberProposalRepository.assignPaper(proposalId, reviewerId)
    }

    fun attachRecommendations(proposalId: Int, reviewerId: Int, recommendation: String) {
        paperRecommendationRepository.addPair(
            PaperRecommendation(
                id = (paperRecommendationRepository.getAll().map { paperRecommendation -> paperRecommendation.id }
                    .maxOrNull() ?: 0) + 1,
                proposalId = proposalId,
                reviewerId = reviewerId,
                recommendation = recommendation
            )
        )
    }

    private fun getProposalWithUserConferenceId(ucid: Int): List<Proposal> {
        return proposalRepository.getProposals()
            .filter { (it.userConferenceId == ucid) }
            .toList()
    }

    fun updateConferenceDeadlines(conference: Conference) = conferenceRepository.updateDeadlines(conference)
    fun getAcceptedPapers(cid: Int): List<Proposal> {
        val userConferenceIds: List<Int> = userConferenceRepository
            .getAll()
            .stream()
            .filter {
                (it.conferenceId == cid)
            }
            .map {
                it.id
            }
            .toList()

        val proposals: MutableList<Proposal> = ArrayList()

        userConferenceIds.forEach {
            proposals.addAll(getProposalWithUserConferenceId(it))
        }

        return proposals.stream().filter {
            (it.accepted)
        }.toList()
    }

    fun getAuthorOfProposal(proposal: Proposal): User? {
        return userRepository.findUserById(
            userConferenceRepository.getAll().first { it.id == proposal.userConferenceId }.userId
        )
    }

    fun makeAcceptedAuthorSpeaker(author: User, conferenceId: Int) {
        userConferenceRepository.findUserConference(author.id, conferenceId)
            ?.let { userConferenceRepository.changeAuthorToSpeaker(it) }
    }

    fun getSectionsOfSpeaker(userId: Int): List<Session> {
        val proposalIds = proposalRepository.getProposalsOfUser(userId).map { it.id }
        return sessionRepository.getSessions().filter { session ->
            proposalSessionRepository.getProposalSessionsOfSession(sessionId = session.sessionId)
                .find { proposalSession -> proposalIds.contains(proposalSession.proposalId) } != null
        }
    }

    fun getEmailOfAuthor(proposal: Proposal): String {
        val userConference = userConferenceRepository.getAll()
            .first {
                it.id == proposal.userConferenceId
            }

        return userRepository.getUsers()
            .first {
                it.id == userConference.userId
            }.email
    }

    fun addSession(conferenceId: Int, topic: String, participantsLimit: Int) {
        var id = 0
        for (session in sessionRepository.getSessions()) id = max(id, session.sessionId + 1)
        sessionRepository.addSession(Session(id, conferenceId, topic, participantsLimit))
    }

    fun getPaperOfSpeakerAtSession(speaker: User, conference: Conference, session: Session): Proposal {
        userConferenceRepository.findUserConference(speaker.id, conference.id)
        val ps = proposalSessionRepository.getProposalSessionsOfSession(session.sessionId)
            .find { proposalSession ->
                proposalRepository.getProposalWithGivenId(proposalSession.proposalId).userConferenceId == userConferenceRepository.findUserConference(
                    speaker.id,
                    conference.id
                )!!.id
            }
        return proposalRepository.getProposalWithGivenId(ps!!.proposalId)
    }

    fun getDateOfPresentation(proposal: Proposal, session: Session): Date {
        return proposalSessionRepository.getAllProposalSessions().find { proposalSession ->
            proposalSession.proposalId == proposal.id && proposalSession.sessionId == session.sessionId
        }!!.time
    }

    fun getSessionsOfAConference(conferenceId: Int): List<Session> {
        return sessionRepository.findSessionsByConferenceId(conferenceId)
    }

    fun getPcMemberProposalsOfConferenceNotRefused(conferenceId: Int) =
        pcMemberProposalRepository.getPcMemberProposalsOfConferenceNotRefused(conferenceId)

    fun getProposalSessionsOfSession(sessionId: Int) = proposalSessionRepository.getProposalSessionsOfSession(sessionId)

    fun addProposalSession(proposalSession: ProposalSession) {
        if (proposalSessionRepository.getAllProposalSessions().any {
                it.proposalId == proposalSession.proposalId
            }) {
            throw ConferenceException("Paper already attributed to a session")
        }
        proposalSessionRepository.addProposalSession(proposalSession)
    }

    fun getRooms(): List<Room> {
        return roomsRepository.getRooms();
    }

    fun assignRoomToSession(session: Session, roomId: Int){
        if(session.roomId != -1){
            throw ConferenceException("Room is already set for session")
        }


        val sessionsForConference = sessionRepository.findSessionsByConferenceId(session.conferenceId);

        if(sessionsForConference.any{
            it.roomId == roomId
            }){
            throw ConferenceException("Room already assigned to another session of this conference")
        }

        sessionRepository.assignRoomToSession(session.sessionId, roomId)
    }
}
