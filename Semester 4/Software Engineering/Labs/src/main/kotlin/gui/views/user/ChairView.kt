package gui.views.user

import domain.*
import exceptions.ConferenceException
import gui.views.conference.ChangeSpeakerView
import gui.views.conference.PayForConferenceView
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.*
import java.util.*


class ChairView(
    private val user: User,
    private val service: Service,
    private val parent: View,
    private val conference: Conference
) : View(user.name + " - " + conference.name) {
    override val root: GridPane by fxml()
    private val goBackButton: Button by fxid()
    private val sendPaperButton: Button by fxid()
    private val postponeDeadlinesButton: Button by fxid()
    private val sendResultsButton: Button by fxid()
    private val payButton: Button by fxid()
    private val viewSessions: Button by fxid()
    private val modifySpeakers: Button by fxid()
    private val listOfPCMembers: ListView<PCMemberProposal> by fxid()

    init {
        goBackButton.apply {
            action {
                goBackHandle()
            }
        }

        postponeDeadlinesButton.apply {
            action {
                postponeDeadlines()
            }
        }

        sendPaperButton.apply {
            action {
                sendPaperToPCMemberHandle()
            }
        }

        sendResultsButton.apply {
            action {
                sendResultsHandle()
            }
        }
        viewSessions.apply {
            action {
                showSessions()
            }
        }

        payButton.apply{
            action {
                pay()
            }
        }

        modifySpeakers.apply{
            action {
                handleModifySpeakers()
            }
        }

        loadPCMembers()
    }

    private fun handleModifySpeakers() {
        replaceWith(
            ChangeSpeakerView(service, this, conference),
            ViewTransition.Explode(0.3.seconds)
        )
    }

    private fun pay() {
        replaceWith(
            PayForConferenceView(user, service, this, conference),
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT)
        )
    }

    private fun sendResultsHandle() {
        conference.reviewPaperDeadline

        if (Calendar.getInstance().time.before(conference.reviewPaperDeadline)){
            alert(Alert.AlertType.INFORMATION, "Review deadline has not passed")
            return
        }

        replaceWith(
            SendResultsView(user, service, this, conference),
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.UP)
        )
    }

    private fun postponeDeadlines() {
        replaceWith(
            PostponeDeadlinesView(user, service, this, conference),
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
        )
    }

    private fun goBackHandle() {
        replaceWith(
            parent,
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
        )
    }

    private fun showSessions() {
        replaceWith(
            SessionsView(user, service, this, conference),
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT)
        )
    }

    private fun sendPaperToPCMemberHandle() {
        val reviewer = listOfPCMembers.selectionModel.selectedItem
        if (reviewer == null) {
            alert(Alert.AlertType.INFORMATION, "Select a reviewer")
            return
        }
        try {
            service.assignPaper(reviewer.proposalId, reviewer.pcMemberId)
            alert(Alert.AlertType.INFORMATION, "Assigned paper")
            loadPCMembers()
        } catch (exception: ConferenceException) {
            alert(Alert.AlertType.ERROR, exception.message)
            return
        }
    }

    private fun loadPCMembers() {
        listOfPCMembers.items.clear()
        val users = service.getPcMemberProposalsOfConferenceNotRefused(conference.id)
        listOfPCMembers.items.addAll(users)
    }


}
