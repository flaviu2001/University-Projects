package gui.views.user

import domain.Conference
import domain.Message
import domain.Proposal
import domain.User
import exceptions.ConferenceException
import javafx.collections.FXCollections
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.*

class ChatView (
    private val user: User,
    private val service : Service,
    private val parent: View,
    conference: Conference
) : View(user.name + " - " + conference.name){
    override val root: GridPane by fxml()
    private val goBackButton: Button by fxid()
    private val sendMessageButton: Button by fxid()
    private val messageTextField: TextField by fxid()

    private val conversationsListView: ListView<Proposal> by fxid()

    private val messagesListView: ListView<Message> by fxid()

    init {
        sendMessageButton.apply {
            action {
                sendMessageHandle()
            }
        }
        goBackButton.apply {
            action {
                goBackHandle()
            }
        }

        conversationsListView.onLeftClick {
            loadMessages()
        }
        loadData()
    }

    private fun goBackHandle(){
        replaceWith(
            parent,
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
        )
    }

    private fun loadData(){
        conversationsListView.items.setAll(FXCollections.observableArrayList(service.getChatRoomsOfUser(user.id)))
    }

    private fun loadMessages(){
        if (conversationsListView.selectionModel.selectedItem == null)
            return
        messagesListView.items.setAll(
            FXCollections.observableArrayList(
                service.getMessagesOfChatRoom(
                    conversationsListView.selectionModel.selectedItem.id
        )))
    }

    private fun sendMessageHandle(){
        val proposal = conversationsListView.selectionModel.selectedItem
        if (proposal == null){
            alert(Alert.AlertType.INFORMATION, "Select conversation")
            return
        }
        val text = messageTextField.text
        if(text == null || text.equals("")){
            alert(Alert.AlertType.INFORMATION, "Text is empty")
            return
        }
        try{
            service.postMessage(text, proposal.id, user.id)
            loadMessages()
        }catch (exc: ConferenceException){
            alert(Alert.AlertType.ERROR, "ERROR: $exc")
        }
    }
}
