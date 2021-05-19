package gui.views.user

import domain.*
import exceptions.ConferenceException
import javafx.collections.FXCollections
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.*
import java.sql.Date
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class SessionsView(
    user: User,
    private val service: Service,
    private val parent: View,
    private val conference: Conference
) : View(user.name + " - " + conference.name) {
    override val root: GridPane by fxml()
    private val finalPapers: ListView<Proposal> by fxid()
    private val sessions: ListView<Session> by fxid()
    private val papersOfSession: ListView<ProposalSession> by fxid()
    private val topic: TextField by fxid()
    private val time: TextField by fxid()
    private val goBack: Button by fxid()
    private val addSession: Button by fxid()
    private val addToSession: Button by fxid()

    init {
        goBack.apply {
            action {
                goBackHandle()
            }
        }
        addSession.apply {
            action {
                addSession()
            }
        }
        addToSession.apply {
            action {
                addToSessionHandler()
            }
        }
        sessions.onLeftClick {
            val session = sessions.selectionModel.selectedItem ?: return@onLeftClick
            papersOfSession.items.clear()
            papersOfSession.items.addAll(service.getProposalSessionsOfSession(session.sessionId).asObservable())
        }
        loadSessions()
        loadPapers()
    }

    private fun goBackHandle() {
        replaceWith(
            parent,
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
        )
    }

    private fun loadPapers() {
        val observable = FXCollections.observableArrayList(service.getAcceptedPapers(conference.id))
        finalPapers.items.addAll(observable)
    }

    private fun loadSessions() {
        val observable = FXCollections.observableArrayList(service.getSessionsOfAConference(conference.id))
        sessions.items.clear()
        sessions.items.addAll(observable)
    }

    private fun addSession() {
        val t = topic.text
        service.addSession(conference.id, t)
        loadSessions()
    }

    private fun addToSessionHandler() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.ENGLISH)
        lateinit var date: LocalDate
        try {
            date = LocalDate.parse(time.text, formatter)
        } catch (e: Exception) {
            alert(Alert.AlertType.ERROR, "Invalid date")
            return
        }
        val proposal = finalPapers.selectionModel.selectedItem
        if (proposal == null) {
            alert(Alert.AlertType.ERROR, "Proposal not selected")
            return
        }
        val session = sessions.selectionModel.selectedItem
        try {
            service.addProposalSession(ProposalSession(proposal.id, session.sessionId, Date.valueOf(date)))
        } catch (e: ConferenceException) {
            alert(Alert.AlertType.ERROR, e.message)
        } catch (e: Exception) {
            alert(Alert.AlertType.ERROR, "The proposal was assigned to the session already")
        }
    }
}