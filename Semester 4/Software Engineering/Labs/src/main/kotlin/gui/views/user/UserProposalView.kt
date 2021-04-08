package gui.views.user

import domain.Proposal
import domain.User
import javafx.collections.FXCollections
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.*

class UserProposalView(private val user: User, private val service: Service) : View(user.name) {
    override val root: GridPane by fxml()
    private val proposalListView: ListView<Proposal> by fxid()
    private val abstractText: TextArea by fxid()
    private val paperText: TextArea by fxid()
    private val paperTitle: TextField by fxid()
    private val authors: TextField by fxid()
    private val keywords: TextField by fxid()
    private val updateProposal: Button by fxid()
    private val backButton: Button by fxid()

    init {
        backButton.apply {
            action {
                handleBackButton()
            }
        }
        updateProposal.apply {
            action {
                handleUpdateProposal()
            }
        }
        loadData()
    }

    private fun handleBackButton() {
        replaceWith(
            UserView(user, service),
            ViewTransition.Metro(1.seconds)
        )
    }

    private fun handleUpdateProposal() {
        val proposal = proposalListView.selectionModel.selectedItem
        if (proposal == null) {
            alert(Alert.AlertType.INFORMATION, "Select a proposal")
            return
        }
        service.updateProposal(
            proposal.id,
            proposal.userConferenceId,
            abstractText.text,
            paperText.text,
            paperTitle.text,
            authors.text,
            keywords.text
        )
        loadData()
    }

    private fun loadData() {
        val observable = FXCollections.observableArrayList(service.getProposalsOfUser(user.id))
        proposalListView.items.removeAll(proposalListView.items)
        proposalListView.items.addAll(observable)
    }
}