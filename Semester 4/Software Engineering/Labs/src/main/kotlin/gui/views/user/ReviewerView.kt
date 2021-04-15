package gui.views.user

import domain.*
import gui.views.conference.BidProposalView
import javafx.collections.FXCollections
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.*

class ReviewerView(private val user: User,
                    private val service: Service,
                    private val parent: View,
                    private val conference: Conference
                    ) : View(user.name + " - " + conference.name){
    override val root: GridPane by fxml()
    private val goBackButton: Button by fxid()
    private val submitResultButton: Button by fxid()
    private val bidProposalButton: Button by fxid()

    private val assignedProposalsListView: ListView<Proposal> by fxid()
    private val reviewResultOptions: ComboBox<ReviewResult> by fxid()

    private val abstractLabel: Label by fxid()
    private val paperLabel: Label by fxid()
    private val titleLabel: Label by fxid()
    private val authorsLabel: Label by fxid()
    private val keywordsLabel: Label by fxid()

    init {
        goBackButton.apply {
            action{
                goBackHandle()
            }
        }

        submitResultButton.apply {
            action{
                submitResultHandle()
            }
        }

        bidProposalButton.apply{
            action {
               bidProposalHandle()
            }
        }

        assignedProposalsListView.onLeftClick {
            selectProposalHandle()
        }

        loadData()
    }

    private fun goBackHandle(){
        replaceWith(parent,
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
    }

    private fun bidProposalHandle(){
        replaceWith(
            BidProposalView(user, service,this, conference),
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
    }

    private fun selectProposalHandle(){
        val proposal = assignedProposalsListView.selectionModel.selectedItem
        abstractLabel.text = proposal.abstractText
        paperLabel.text = proposal.paperText
        titleLabel.text =  proposal.title
        authorsLabel.text = proposal.authors
        keywordsLabel.text = proposal.keywords
    }

    private fun populateComboBox(){
        val observable = FXCollections.observableArrayList(enumValues<ReviewResult>().toList())
        reviewResultOptions.items.addAll(observable)
    }

    private fun loadAssignedProposals(){
        val observable = FXCollections.observableArrayList(service.getAssignedPapers(user.id))
        assignedProposalsListView.items.removeAll()
        assignedProposalsListView.items.addAll(observable)
    }

    private fun loadData(){
        populateComboBox()
        loadAssignedProposals()
    }

    private fun submitResultHandle() {
        val result = reviewResultOptions.selectedItem
        if (result == null) {
            alert(Alert.AlertType.INFORMATION, "Please select a result")
            return
        }
        val paper = assignedProposalsListView.selectedItem
        if (paper == null) {
            alert(Alert.AlertType.INFORMATION, "Please select a paper")
            return
        }
        service.addReview(user.id, paper.id, result)
        alert(Alert.AlertType.INFORMATION, "Paper was reviewed");
    }
}
