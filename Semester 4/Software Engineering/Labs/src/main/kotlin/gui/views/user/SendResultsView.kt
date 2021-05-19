package gui.views.user

import domain.Conference
import domain.Proposal
import domain.User
import javafx.collections.FXCollections
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.*
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class SendResultsView (private val user: User,
                       private val service: Service,
                       private val parent: View,
                       private var conference: Conference
) : View(user.name + " - " + conference.name) {

    override val root: GridPane by fxml()
    private val goBackButton: Button by fxid()
    private val acceptedProposals: ListView<Proposal> by fxid()
    private val abstractTextField: TextArea by fxid()
    private val sendMailButton: Button by fxid()
    private lateinit var selectedProposal: Proposal


    init {
        goBackButton.apply {
            action {
                goBack()
            }
        }

        acceptedProposals.onLeftClick {
            selectProposalHandle()
        }

        sendMailButton.apply {
            action {
                makeAuthorBeSpeaker()
                sendResultViaEmail()
            }
        }

        loadData()
    }

    private fun makeAuthorBeSpeaker() {
        service.getAuthorOfProposal(selectedProposal)?.let { service.makeAcceptedAuthorSpeaker(it, conference.id) }
    }

    private fun sendResultViaEmail() {
        val from = user.email
        val host = "localhost"
        val properties = System.getProperties()
        properties.setProperty("mail.smtp.host", host)
        val session = Session.getDefaultInstance(properties)
        val email = service.getEmailOfAuthor(selectedProposal)
        try {
            val message = MimeMessage(session)
            message.setFrom(InternetAddress(from))
            message.addRecipient(Message.RecipientType.TO, InternetAddress(email))
            message.subject = "Proposal response"
            var text: String =
                "Congratulations! Your proposal with title " + selectedProposal.title + " was accepted in the "
            text += conference.name
            text += " conference."
            text += " We are looking forward to seeing you at the conference!"
            text += "\nHave a nice day!\n"
            text += user.fullName
            message.setText(text)
            Transport.send(message)
            alert(Alert.AlertType.CONFIRMATION, "Email sent successfully")
        } catch (exception: MessagingException) {
            alert(Alert.AlertType.ERROR, "Email could not be sent: ${exception.message}")
            return
        }
    }

    private fun selectProposalHandle() {
        val proposal = acceptedProposals.selectionModel.selectedItem

        if (proposal == null){
            alert(Alert.AlertType.INFORMATION, "Please select a proposal")
            return
        }

        abstractTextField.text = proposal.abstractText
        selectedProposal = proposal
    }

    private fun loadData() {
        val observable = FXCollections.observableArrayList(service.getAcceptedPapers(conference.id))
        acceptedProposals.items.removeAll()
        acceptedProposals.items.addAll(observable)
    }

    private fun goBack() {
        replaceWith(parent,
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.DOWN))
    }

}
