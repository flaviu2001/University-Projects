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
    private val user: User,
    private val service: Service,
    private val parent: View,
    private val conference: Conference
) : View(user.name + " - " + conference.name) {
    override val root: GridPane by fxml()
    private val FinalPapers: ListView<Proposal> by fxid()
    private val Sessions: ListView<Session> by fxid()
    private val PapersOfSession: ListView<ProposalSession> by fxid()
    private val topic: TextField by fxid()
    private val time: TextField by fxid()
    private val goBack: Button by fxid()
    private val addSession: Button by fxid()
    private val addToSession: Button by fxid()
    private val map = hashMapOf<Int, Any?>()

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
        Sessions.onLeftClick {
            val session = Sessions.selectionModel.selectedItem ?: return@onLeftClick
            PapersOfSession.items.clear()
            PapersOfSession.items.addAll(service.getProposalSessionsOfSession(session.sessionId).asObservable())
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
        FinalPapers.items.addAll(observable)
    }

    private fun loadSessions() {
        val observable = FXCollections.observableArrayList(service.getSessionsOfAConference(conference.id))
        Sessions.items.clear()
        Sessions.items.addAll(observable)
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
        val proposal = FinalPapers.selectionModel.selectedItem
        if (proposal == null) {
            alert(Alert.AlertType.ERROR, "Proposal not selected")
            return
        }
        val session = Sessions.selectionModel.selectedItem
        try {
            service.addProposalSession(ProposalSession(proposal.id, session.sessionId, Date.valueOf(date)))
        } catch (e: ConferenceException) {
            alert(Alert.AlertType.ERROR, e.message)
        } catch (e: Exception) {
            alert(Alert.AlertType.ERROR, "The proposal was assigned to the session already")
        }
    }
}