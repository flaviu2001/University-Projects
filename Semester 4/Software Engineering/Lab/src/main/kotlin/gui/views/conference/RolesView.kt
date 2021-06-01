package gui.views.conference

import domain.Conference
import domain.Role
import domain.User
import gui.views.user.*
import javafx.collections.FXCollections
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.*

class RolesView(
    private val user: User,
    private val service: Service,
    private val parent: View,
    private var conference: Conference
) : View(user.name + " - " + conference.name) {

    override val root: GridPane by fxml()
    private val goBackButton: Button by fxid()
    private val rolesListView: ListView<Role> by fxid()

    init {
        goBackButton.apply {
            action {
                goBackHandle()
            }
        }

        rolesListView.onDoubleClick {
            selectRoleHandle()
        }
        loadData()
    }

    private fun selectRoleHandle() {
        when (rolesListView.selectionModel.selectedItem) {
            Role.CHAIR, Role.CO_CHAIR -> replaceWith(
                ChairView(user, service, this, conference),
                ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT)
            )
            Role.REVIEWER -> replaceWith(
                ReviewerView(user, service, this, conference),
                ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT)
            )
            Role.LISTENER -> replaceWith(
                ListenerView(user, service, this, conference),
                ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT)
            )
            Role.SPEAKER -> replaceWith(
                SpeakerView(user, service, this, conference),
                ViewTransition.Slide(
                    0.3.seconds, ViewTransition.Direction.RIGHT
                )
            )
            Role.AUTHOR -> replaceWith(
                AuthorView(user, service, conference, this),
                ViewTransition.Slide(
                    0.3.seconds, ViewTransition.Direction.RIGHT
                )
            )
            Role.SESSION_CHAIR -> replaceWith(
                ChangeSpeakerView(service, this, conference, user),
                ViewTransition.Slide(
                    0.3.seconds, ViewTransition.Direction.RIGHT
                )
            )
            null
            -> {

            }
            else -> {
                alert(Alert.AlertType.INFORMATION, "Not implemented")
            }
        }
    }

    private fun goBackHandle() {
        replaceWith(
            parent,
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
        )
    }

    private fun loadData() {
        val observable = FXCollections.observableArrayList(
            service.getRolesOfUser(user.id, conference.id).map { str -> Role.valueOf(str) })
        rolesListView.items.removeAll(rolesListView.items)
        rolesListView.items.addAll(observable)
    }
}
