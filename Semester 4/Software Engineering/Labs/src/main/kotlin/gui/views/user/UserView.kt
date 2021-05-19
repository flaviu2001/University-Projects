package gui.views.user

import domain.Conference
import domain.Role
import domain.User
import gui.views.conference.RolesView
import gui.views.login.LoginView
import javafx.collections.FXCollections
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.*

class UserView(private val user: User, private val service: Service) : View(user.name) {
    override val root: GridPane by fxml()
    private val logoutButton: Button by fxid()
    private val conferenceListView: ListView<Conference> by fxid()
    private val registerAuthor: Button by fxid()
    private val registerAsListener: Button by fxid()

    init {
        conferenceListView.onDoubleClick {
            handleChooseConference()
        }

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
        registerAsListener.apply {
            action {
                handleRegisterAsListener()
            }
        }
        loadData()
    }

    private fun handleRegisterAsListener() {
        val conference = conferenceListView.selectionModel.selectedItem
        if (conference == null) {
            alert(Alert.AlertType.INFORMATION, "Select a conference")
            return
        }
        val conferencesOfUser = service.getConferencesOfUser(user.id)
        if (!conferencesOfUser.any { userConference -> userConference.conferenceId == conference.id })
            service.addUserToConference(user.id, conference.id, Role.LISTENER, false)
        else {
            alert(Alert.AlertType.INFORMATION, "User already registered to conference")
        }
    }

    private fun handleChooseConference(){
        val conference = conferenceListView.selectionModel.selectedItem ?: return
        val conferencesOfUser = service.getConferencesOfUser(user.id)
        if (!conferencesOfUser.any { userConference -> userConference.conferenceId == conference.id })
            alert(Alert.AlertType.INFORMATION, "User must participate in conference")
        else {
            replaceWith(RolesView(user, service, this, conference),
                ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT))
        }
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
        if (!conferencesOfUser.any { userConference -> userConference.conferenceId == conference.id && userConference.role == Role.AUTHOR || userConference.role == Role.SPEAKER })
            service.addUserToConference(user.id, conference.id, Role.AUTHOR, false)
        else {
            alert(Alert.AlertType.INFORMATION, "User already registered to conference")
        }
    }

    private fun loadData() {
        val observable = FXCollections.observableArrayList(service.getConferences())
        conferenceListView.items.addAll(observable)
    }
}
