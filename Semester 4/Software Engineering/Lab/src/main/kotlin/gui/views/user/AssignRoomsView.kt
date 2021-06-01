package gui.views.user

import domain.Conference
import domain.Room
import domain.Session
import domain.User
import exceptions.ConferenceException
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.*

class AssignRoomsView(private val user: User,
                      private val service: Service,
                      private val parent: View,
                      private val conference: Conference
) : View(user.name + " - " + conference.name)  {

    override val root: GridPane by fxml()
    private val goBackButton: Button by fxid()
    private val assignRoomButton: Button by fxid()
    private val roomsListView: ListView<Room> by fxid()
    private val sessionsListView: ListView<Session> by fxid()
    private val numberOfParticipants: Label by fxid()
    private var selectedRoom: Room? = null
    private var selectedSession: Session? = null

    init {
        goBackButton.apply {
            action {
                goBackHandle()
            }
        }
        roomsListView.onLeftClick {
            val room = roomsListView.selectionModel.selectedItem ?: return@onLeftClick
            selectedRoom = room
        }
        sessionsListView.onLeftClick {
            val session = sessionsListView.selectionModel.selectedItem ?: return@onLeftClick
            selectedSession = session
            val numberOfListeners = service.getNumberOfUsersForSession(session.sessionId)
            numberOfParticipants.text = "Session ${session.topic} has $numberOfListeners listeners and " +
                    "limit of ${session.participantsLimit} participants"
        }

        assignRoomButton.apply {
            action {
                assignRoomHandler()
            }
        }

        loadData()
    }

    private fun assignRoomHandler() {
        if(selectedRoom == null){
            alert(Alert.AlertType.INFORMATION, "No room selected", "Please select a room in order to continue")
            return
        }

        if(selectedSession == null){
            alert(Alert.AlertType.INFORMATION, "No session selected", "Please select a session in order to continue")
            return
        }

        try {
            service.assignRoomToSession(selectedSession!!, selectedRoom!!.id)
            alert(Alert.AlertType.CONFIRMATION, "Success", "Room was successfully assigned to session")
            loadData()
        }catch (exception: ConferenceException){
            alert(Alert.AlertType.ERROR, "An error occurred", exception.message)
        }
    }


    private fun goBackHandle() {
        replaceWith(
            parent,
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.DOWN)
        )

    }

    private fun loadData() {
        numberOfParticipants.text =""
        loadRooms()
        loadSessions()
    }

    private fun loadSessions() {
        sessionsListView.items.clear()
        val sessions = service.getSessionsOfAConferenceWithNoRoomsAssigned(conference.id)
        sessionsListView.items.addAll(sessions)
    }

    private fun loadRooms() {
        roomsListView.items.clear()
        val rooms = service.getRooms()
        roomsListView.items.addAll(rooms)
    }
}