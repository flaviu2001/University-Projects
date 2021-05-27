package gui.views.conference

import domain.Conference
import domain.User
import exceptions.ConferenceException
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.View
import tornadofx.action
import tornadofx.alert
import tornadofx.asObservable

class ChangeSpeakerView(private val service: Service, private val parent: View, private val conference: Conference) : View() {
    override val root: GridPane by fxml()
    private val users: ListView<User> by fxid()
    private val add: Button by fxid()
    private val delete: Button by fxid()
    private val back: Button by fxid()

    init {
        users.items.addAll(service.getUsers().asObservable())
        back.apply {
            action {
                handleBack()
            }
        }
        delete.apply {
            action {
                handleDelete()
            }
        }
        add.apply {
            action {
                handleAdd()
            }
        }
    }

    private fun handleAdd() {
        val user = users.selectionModel.selectedItem ?: return
        try {
            service.makeUserSpeaker(user, conference)
        } catch (e: ConferenceException) {
            alert(Alert.AlertType.ERROR, e.message)
        }
    }

    private fun handleDelete() {
        val user = users.selectionModel.selectedItem ?: return
        try {
            service.unmakeUserSpeaker(user, conference)
        } catch (e: ConferenceException) {
            alert(Alert.AlertType.ERROR, e.message)
        }
    }

    private fun handleBack() {
        replaceWith(parent)
    }
}