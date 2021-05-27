package gui.views.user

import domain.*
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.*

class SpeakerView(private val user: User, private val service: Service, private val parent: View, private val conference: Conference): View(user.name) {
    override val root: GridPane by fxml()
    private val goBackButton: Button by fxid()
    private val goToSessionButton: Button by fxid()
    private val sessionsListView: ListView<Session> by fxid()

    init {
        goBackButton.apply {
            action {
                goBackHandle()
            }
        }

        goToSessionButton.apply {
            action {
                goToSession()
            }
        }

        loadData()

    }

    private fun goToSession() {
        val userConference = sessionsListView.selectedItem
        if (userConference == null) {
            Alert(Alert.AlertType.ERROR, "Please select a section")
            return
        }
        replaceWith(
            SpeakerSessionView(user, service, this, conference, sessionsListView.selectedItem!!),
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
        )
    }
    private fun loadData() {
        val sections = service.getSectionsOfSpeaker(user.id)
        print(sections)
        sessionsListView.items.addAll(sections)
    }

    private fun goBackHandle() {
        replaceWith(
            parent,
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
        )
    }
}
