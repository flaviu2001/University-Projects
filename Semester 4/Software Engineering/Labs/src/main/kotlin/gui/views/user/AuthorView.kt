package gui.views.user

import domain.Conference
import domain.Role
import domain.User
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.*
import java.util.*

class AuthorView(
    private val user: User,
    private val service: Service,
    private val conference: Conference,
    private val parent: View
) : View(user.name) {
    override val root: GridPane by fxml()
    private val goBackButton: Button by fxid()
    private val abstractText: TextArea by fxid()
    private val paperText: TextArea by fxid()
    private val paperTitle: TextField by fxid()
    private val authors: TextField by fxid()
    private val keywords: TextField by fxid()
    private val submitProposal: Button by fxid()
    private val viewProposals: Button by fxid()

    init {
        goBackButton.apply {
            action {
                goBackHandle()
            }
        }
        submitProposal.apply {
            action {
                handleSubmitProposal()
            }
        }
        viewProposals.apply {
            action {
                handleViewProposals()
            }
        }
    }

    private fun atLeastNCharacters(field: String, n: Int): Boolean {
        return field.length >= n
    }

    private fun goBackHandle() {
        replaceWith(
            parent,
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
        )
    }

    private fun handleSubmitProposal() {
        if (Calendar.getInstance().time.after(conference.submitPaperDeadline)) {
            alert(Alert.AlertType.ERROR, "Deadline for this conference has passed. Cannot send new submissions")
            return
        }
        if (!atLeastNCharacters(paperTitle.text, 3)) {
            alert(Alert.AlertType.ERROR, "The paper title should have at least 3 characters")
            return
        }
        if (!atLeastNCharacters(abstractText.text, 20)) {
            alert(Alert.AlertType.ERROR, "The abstract should have at least 20 characters")
            return
        }
        val conferencesOfUser = service.getConferencesOfUser(user.id)
        val userConference =
            conferencesOfUser.find { userConference -> userConference.conferenceId == conference.id && userConference.role == Role.AUTHOR }
        if (userConference == null) {
            alert(Alert.AlertType.INFORMATION, "User is not registered as author")
            return
        }
        service.addProposal(
            userConference.id,
            abstractText.text,
            paperText.text,
            paperTitle.text,
            authors.text,
            keywords.text
        )
    }

    private fun handleViewProposals() {
        replaceWith(
            UserProposalView(user, service, this),
            ViewTransition.Dissolve(0.5.seconds)
        )
    }
}
