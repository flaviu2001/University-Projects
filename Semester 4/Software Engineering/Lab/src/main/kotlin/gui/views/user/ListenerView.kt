package gui.views.user

import domain.Conference
import domain.Room
import domain.Session
import domain.User
import gui.views.conference.PayForConferenceView
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import org.postgresql.util.PSQLException
import service.Service
import tornadofx.*

class ListenerView(private val user: User,
                   private val service: Service,
                   private val parent: View,
                   private val conference: Conference
) : View(user.name + " - " + conference.name) {
    override val root: GridPane by fxml()
    private val goBackButton: Button by fxid()
    private val payButton: Button by fxid()
    private var selectedSection: Session? = null
    private val sectionsListView: ListView<Session> by fxid()
    private val userSectionsListView: ListView<Session> by fxid()
    private val sectionNameTextField: TextField by fxid()
    private val chooseSectionButton: Button by fxid()
    private val sessionInfoLabel: Label by fxid()

    init {
        goBackButton.apply {
            action {
                handleGoBack()
            }
        }

        payButton.apply{
            action {
                pay()
            }
        }

        sectionsListView.onLeftClick {
            val session = sectionsListView.selectionModel.selectedItem ?: return@onLeftClick
            sectionNameTextField.text = session.topic
            selectedSection = session
        }

        userSectionsListView.onLeftClick {
            sessionInfoLabel.text=""
            val session = userSectionsListView.selectionModel.selectedItem ?: return@onLeftClick
            val room: Room? = service.getRoomOfSession(session.sessionId)

            var sessionInfo = "No information for selected conference!"

            if(room != null){
                sessionInfo = "Session with topic ${session.topic} will be held in the ${room.name} room"
            }

            sessionInfoLabel.text = sessionInfo
        }

        chooseSectionButton.apply {
            action {
                handleChooseSection()
            }
        }

        loadSessions()
        loadUserSections()
    }

    private fun loadUserSections() {
        sessionInfoLabel.text=""
        userSectionsListView.items.clear()
        userSectionsListView.items.addAll(service.getSectionsOfUser(user.id).asObservable())
    }

    private fun handleChooseSection() {
        if(selectedSection == null){
            alert(Alert.AlertType.ERROR, "No section selected")
            return
        }
        try {
            val numberOfListeners = service.getNumberOfUsersForSession(selectedSection!!.sessionId)
            if(numberOfListeners == selectedSection!!.participantsLimit){
                alert(Alert.AlertType.INFORMATION, "Section full", "Sorry! You cannot attend this session as the maximum number of participants was reached")
                return
            }

            service.addUserToSection(user.id, selectedSection!!.sessionId)
            loadUserSections()
            alert(Alert.AlertType.INFORMATION, "Section was chosen")
        } catch (e: PSQLException) {
            alert(Alert.AlertType.ERROR, "The section has already been chosen")
        }
    }

    private fun loadSessions() {
        sessionInfoLabel.text=""
        sectionsListView.items.clear()
        sectionsListView.items.addAll(service.getSessionsOfAConference(conference.id).asObservable())
    }

    private fun pay() {
        replaceWith(
            PayForConferenceView(user, service, this, conference),
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT)
        )
    }

    private fun handleGoBack() {
        replaceWith(
            parent,
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
        )
    }
}