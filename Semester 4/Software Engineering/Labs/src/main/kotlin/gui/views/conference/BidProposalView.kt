package gui.views.conference

import domain.Availability
import domain.Conference
import domain.Proposal
import domain.User
import javafx.collections.FXCollections
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.*

class BidProposalView(private val user: User,
                      private val service: Service,
                      private val parent: View,
                      private val conference: Conference) : View(user.name + " - " + conference.name) {

    override val root: GridPane by fxml()
    private val goBackButton: Button by fxid()
    private val bidProposalButton: Button by fxid()

    private val proposalsListView: ListView<Proposal> by fxid()
    private val availabilityListView: ListView<Availability> by fxid()

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

        bidProposalButton.apply {
            action{
                bidProposalHandle()
            }
        }

        proposalsListView.onLeftClick {
            selectProposalHandle()
        }

        loadData()
    }

    private fun goBackHandle(){
        replaceWith(parent,
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
    }

    private fun bidProposalHandle(){
        val proposal = proposalsListView.selectionModel.selectedItem
        if(proposal == null) {
            alert(Alert.AlertType.INFORMATION, "Select a proposal")
            return
        }

        val availability = availabilityListView.selectionModel.selectedItem
        if(availability == null) {
            alert(Alert.AlertType.INFORMATION, "Select a availability")
            return
        }
        service.addPcMemberProposal(user.id, proposal.id, availability)

        alert(Alert.AlertType.INFORMATION, "Proposal was bid")
    }

    private fun selectProposalHandle(){
        val proposal = proposalsListView.selectionModel.selectedItem
        abstractLabel.text = proposal.abstractText
        paperLabel.text = proposal.paperText
        titleLabel.text =  proposal.title
        authorsLabel.text = proposal.authors
        keywordsLabel.text = proposal.keywords
    }

    private fun loadProposals(){
        val observable = FXCollections.observableArrayList(service.getProposalsForPcMember(user.id, conference.id))
        proposalsListView.items.removeAll(proposalsListView.items)
        proposalsListView.items.addAll(observable)
    }

    private fun loadAvailability(){
        val observable = FXCollections.observableArrayList(enumValues<Availability>().toList())
        availabilityListView.items.removeAll(availabilityListView.items)
        availabilityListView.items.addAll(observable)
    }

    private fun loadData() {
        loadProposals()
        loadAvailability()
    }
}