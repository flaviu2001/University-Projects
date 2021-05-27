package gui.views.user

import domain.Conference
import domain.User
import javafx.scene.control.Button
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.View
import tornadofx.ViewTransition
import tornadofx.action
import tornadofx.seconds

class AssignRoomsView(private val user: User,
                      private val service: Service,
                      private val parent: View,
                      private val conference: Conference
) : View(user.name + " - " + conference.name)  {

    override val root: GridPane by fxml()
    private val goBackButton: Button by fxid()

    init {
        goBackButton.apply {
            action {
                goBackHandle()
            }
        }
    }

    private fun goBackHandle() {
        replaceWith(
            parent,
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.DOWN)
        )
    }
}