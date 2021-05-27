package gui.views.user

import domain.Conference
import domain.Role
import domain.User
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.stage.FileChooser
import service.Service
import tornadofx.*
import java.io.File
import java.io.IOException
import java.util.*

class AuthorView(
    private val user: User,
    private val service: Service,
    private val conference: Conference,
    private val parent: View
) : View(user.name) {
    override val root: GridPane by fxml()
    private val goBackButton: Button by fxid()
    private val abstractText: TextArea by fxid()
    private val paperText: TextArea by fxid()
    private val paperTitle: TextField by fxid()
    private val authors: TextField by fxid()
    private val keywords: TextField by fxid()
    private val submitProposal: Button by fxid()
    private val viewProposals: Button by fxid()
    private val addFullPaperButton: Button by fxid()

    private var destinationFile: File? = null
    private var file: File? = null
    init {
        goBackButton.apply {
            action {
                goBackHandle()
            }
        }
        submitProposal.apply {
            action {
                handleSubmitProposal()
            }
        }
        viewProposals.apply {
            action {
                handleViewProposals()
            }
        }
        if (!conference.withFullPaper) {
            addFullPaperButton.isDisable = true
        }
        addFullPaperButton.apply {
            action {
                handleAddFullPaper()
            }
        }
    }

    private fun handleAddFullPaper() {
        val fileChooser = FileChooser()
        fileChooser.title = "Upload presentation"
        fileChooser.extensionFilters.addAll(
            FileChooser.ExtensionFilter("PDF", "*.pdf"),
            FileChooser.ExtensionFilter("PPT", "*.ppt"),
            FileChooser.ExtensionFilter("PPTX", "*.pptx")
        )
        file = fileChooser.showOpenDialog(null)

        destinationFile = File("src/main/resources/full-papers/" + user.name + "." + file!!.extension)
        Alert(Alert.AlertType.INFORMATION, "File was selected")
    }
    private fun atLeastNCharacters(field: String, n: Int): Boolean {
        return field.length >= n
    }

    private fun goBackHandle() {
        replaceWith(
            parent,
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
        )
    }

    private fun handleSubmitProposal() {
        print(Calendar.getInstance().time)
        if (Calendar.getInstance().time.after(conference.submitPaperDeadline)) {
            alert(Alert.AlertType.ERROR, "Deadline for this conference has passed. Cannot send new submissions")
            return
        }
        if (!atLeastNCharacters(paperTitle.text, 3)) {
            alert(Alert.AlertType.ERROR, "The paper title should have at least 3 characters")
            return
        }
        if (!atLeastNCharacters(abstractText.text, 20)) {
            alert(Alert.AlertType.ERROR, "The abstract should have at least 20 characters")
            return
        }
        val conferencesOfUser = service.getConferencesOfUser(user.id)
        val userConference =
            conferencesOfUser.find { userConference -> userConference.conferenceId == conference.id && userConference.role == Role.AUTHOR }
        if (userConference == null) {
            alert(Alert.AlertType.INFORMATION, "User is not registered as author")
            return
        }
        var location: String = ""
        if (conference.withFullPaper) {
            if (file == null || destinationFile == null) {
                alert(Alert.AlertType.ERROR, "Paper not uploaded")
            } else {
                try {
                    file!!.copyTo(destinationFile!!)
                    location = file!!.path
                } catch (exception: IOException) {
                    exception.printStackTrace()
                    return
                }
            }
        }
        service.addProposal(
            userConference.id,
            abstractText.text,
            paperText.text,
            paperTitle.text,
            authors.text,
            keywords.text,
            fullPaperLocation = location
        )
    }

    private fun handleViewProposals() {
        replaceWith(
            UserProposalView(user, service, this),
            ViewTransition.Dissolve(0.5.seconds)
        )
    }
}
