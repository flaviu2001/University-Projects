package gui.views.user

import domain.Conference
import domain.Proposal
import domain.ReviewResult
import domain.User
import gui.views.conference.BidProposalView
import gui.views.conference.PayForConferenceView
import javafx.collections.FXCollections
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.*
import java.util.*

class ReviewerView(
    private val user: User,
    private val service: Service,
    private val parent: View,
    private val conference: Conference
) : View(user.name + " - " + conference.name) {
    override val root: GridPane by fxml()
    private val goBackButton: Button by fxid()
    private val submitResultButton: Button by fxid()
    private val bidProposalButton: Button by fxid()
    private val payForConference: Button by fxid()
    private val attachRecommendations: Button by fxid()

    private val recommendationField: TextField by fxid()
    private val assignedProposalsListView: ListView<Proposal> by fxid()
    private val reviewResultOptions: ComboBox<ReviewResult> by fxid()

    private val abstractLabel: Label by fxid()
    private val paperLabel: Label by fxid()
    private val titleLabel: Label by fxid()
    private val authorsLabel: Label by fxid()
    private val keywordsLabel: Label by fxid()

    private val chatButton: Button by fxid()

    init {
        chatButton.apply {
            action {
                chatButtonHandle()
            }
        }

        goBackButton.apply {
            action {
                goBackHandle()
            }
        }

        submitResultButton.apply {
            action {
                submitResultHandle()
            }
        }

        bidProposalButton.apply {
            action {
                bidProposalHandle()
            }
        }

        assignedProposalsListView.onLeftClick {
            selectProposalHandle()
        }

        attachRecommendations.apply {
            action {
                attachRecommendationsHandle()

            }
        }

        payForConference.apply {
            action {
                pay()
            }
        }

        if (Calendar.getInstance().time.before(conference.reviewPaperDeadline)) {
            loadData()
        } else {
            alert(Alert.AlertType.ERROR, "Review deadline has passed. You can no longer review papers.")
        }
    }

    private fun chatButtonHandle(){
        replaceWith(
            ChatView(user, service, this, conference),
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
        )
    }

    private fun pay() {
        replaceWith(
            PayForConferenceView(user, service, this, conference),
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT)
        )
    }

    private fun goBackHandle() {
        replaceWith(
            parent,
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
        )
    }

    private fun bidProposalHandle() {
        replaceWith(
            BidProposalView(user, service, this, conference),
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
        )
    }

    private fun selectProposalHandle() {
        val proposal = assignedProposalsListView.selectionModel.selectedItem ?: return
        abstractLabel.text = proposal.abstractText
        paperLabel.text = proposal.paperText
        titleLabel.text = proposal.title
        authorsLabel.text = proposal.authors
        keywordsLabel.text = proposal.keywords
    }

    private fun populateComboBox() {
        val observable = FXCollections.observableArrayList(enumValues<ReviewResult>().toList())
        reviewResultOptions.items.addAll(observable)
    }

    private fun loadAssignedProposals() {
        val observable = FXCollections.observableArrayList(service.getAssignedPapers(user.id))
        assignedProposalsListView.items.removeAll()
        assignedProposalsListView.items.addAll(observable)
    }

    private fun loadData() {
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
        try {
            val pair = service.review(user.id, paper.id, result)
            if (pair.first != pair.second)
                alert(Alert.AlertType.INFORMATION, "Paper was reviewed, ${pair.first} accepts out of ${pair.second} needed")
            else alert(Alert.AlertType.INFORMATION, "Paper has been accepted! The necessary ${pair.second} accepts have been reached")
        } catch (e: Exception) {
            e.message?.let { alert(Alert.AlertType.ERROR, it) }
        }
    }

    private fun attachRecommendationsHandle() {
        val recommendation = recommendationField.text
        if (recommendation == null) {
            alert(Alert.AlertType.INFORMATION, "Please select a recommendation")
            return
        }
        val paper = assignedProposalsListView.selectedItem
        if (paper == null) {
            alert(Alert.AlertType.INFORMATION, "Please select a paper")
            return
        }

        if (service.getReviewsForPaper(paper.id).find { review -> review.pcMemberId == user.id } != null) {
            alert(Alert.AlertType.INFORMATION, "Cannot attach recommendations to already reviewed paper")
            return
        }

        service.attachRecommendations(paper.id, user.id, recommendation)
        alert(Alert.AlertType.CONFIRMATION, "Recommendations attached")
    }

}
