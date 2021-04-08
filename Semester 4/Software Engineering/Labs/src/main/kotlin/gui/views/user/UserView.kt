package gui.views.user

import domain.Conference
import domain.Role
import domain.User
import gui.views.login.LoginView
import javafx.collections.FXCollections
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.*

class UserView(private val user: User, private val service: Service) : View(user.name) {
    override val root: GridPane by fxml()
    private val logoutButton: Button by fxid()
    private val conferenceListView: ListView<Conference> by fxid()
    private val registerAuthor: Button by fxid()
    private val abstractText: TextArea by fxid()
    private val paperText: TextArea by fxid()
    private val paperTitle: TextField by fxid()
    private val authors: TextField by fxid()
    private val keywords: TextField by fxid()
    private val submitProposal: Button by fxid()
    private val viewProposals: Button by fxid()

    init {
        logoutButton.apply {
            action {
                handleLogout()
            }
        }
        registerAuthor.apply {
            action {
                handleRegisterAuthor()
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
        loadData()
    }

    private fun handleLogout() {
        replaceWith(LoginView::class, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT))
    }

    private fun handleRegisterAuthor() {
        val conference = conferenceListView.selectionModel.selectedItem
        if (conference == null) {
            alert(Alert.AlertType.INFORMATION, "Select a conference")
            return
        }
        val conferencesOfUser = service.getConferencesOfUser(user.id)
        if (!conferencesOfUser.any { userConference -> userConference.conferenceId == conference.id })
            service.addUserToConference(user.id, conference.id, Role.AUTHOR, false)
        else {
            alert(Alert.AlertType.INFORMATION, "User already registered to conference")
        }
    }

    private fun handleSubmitProposal() {
        val conference = conferenceListView.selectionModel.selectedItem
        if (conference == null) {
            alert(Alert.AlertType.INFORMATION, "Select a conference")
            return
        }
        val conferencesOfUser = service.getConferencesOfUser(user.id)
        val userConference =
            conferencesOfUser.find { userConference -> userConference.conferenceId == conference.id && userConference.role == Role.AUTHOR }
        if (userConference == null) {
            alert(Alert.AlertType.INFORMATION, "User is not registered as author")
            return
        }
        if (abstractText.text.isEmpty()) {
            alert(Alert.AlertType.INFORMATION, "The abstract text cannot be empty")
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
            UserProposalView(user, service),
            ViewTransition.Dissolve(0.5.seconds)
        )
    }

    private fun loadData() {
        val observable = FXCollections.observableArrayList(service.getConferences())
        conferenceListView.items.addAll(observable)
    }
}