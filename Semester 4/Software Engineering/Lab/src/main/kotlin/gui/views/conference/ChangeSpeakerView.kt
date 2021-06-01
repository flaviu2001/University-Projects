package gui.views.conference

import domain.Conference
import domain.User
import exceptions.ConferenceException
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.*

class ChangeSpeakerView(
    private val service: Service,
    private val parent: View,
    private val conference: Conference,
    private val user: User
) : View() {
    override val root: GridPane by fxml()
    private val users: ListView<User> by fxid()
    private val add: Button by fxid()
    private val delete: Button by fxid()
    private val back: Button by fxid()
    private val pay: Button by fxid()

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
        pay.apply {
            action {
                handlePay()
            }
        }
    }

    private fun handlePay() {
        replaceWith(
            PayForConferenceView(user, service, this, conference),
            ViewTransition.Explode(0.5.seconds)
        )
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
        replaceWith(parent, ViewTransition.NewsFlash(0.5.seconds))
    }
}