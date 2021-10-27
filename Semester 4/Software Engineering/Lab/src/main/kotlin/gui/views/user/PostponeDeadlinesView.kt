package gui.views.user

import domain.Conference
import domain.User
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.DatePicker
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.View
import tornadofx.ViewTransition
import tornadofx.action
import tornadofx.seconds
import tornadofx.*
import java.sql.Date
import java.util.*

class PostponeDeadlinesView(
    user: User,
    private val service: Service,
    private val parent: View,
    private var conference: Conference
) : View(user.name + " - " + conference.name) {
    override val root: GridPane by fxml()
    private val goBackButton: Button by fxid()
    private val submitProposalButton: Button by fxid()
    private val reviewPaperButton: Button by fxid()
    private val biddingPhaseButton: Button by fxid()
    private val submitProposalDatePicker: DatePicker by fxid()
    private val reviewPaperDatePicker: DatePicker by fxid()
    private val biddingPhaseDatePicker: DatePicker by fxid()
    private val submitProposalLabel: Label by fxid()
    private val reviewPaperLabel: Label by fxid()
    private val biddingPhaseLabel: Label by fxid()

    init{
        goBackButton.apply {
            action{
                goBackHandle()
            }
        }

        submitProposalButton.apply {
            action {
                postponeSubmitProposalHandle()
            }
        }

        reviewPaperButton.apply {
            action {
                postponeReviewHandle()
            }
        }

        biddingPhaseButton.apply {
            action {
                postponeBiddingPhaseHandle()
            }
        }
        setLabels()
    }

    private fun postponeSubmitProposalHandle(){
        if(submitProposalDatePicker.value == null){
            alert(Alert.AlertType.ERROR, "Pick a date!")
            return
        }
        if (Date.valueOf(submitProposalDatePicker.value) < Date()) {
            alert(Alert.AlertType.ERROR,"Cannot choose date in the past!")
            return
        }
        conference.submitPaperDeadline = Date.valueOf(submitProposalDatePicker.value)
        service.updateConferenceDeadlines(conference)
        setLabels()
    }

    private fun postponeReviewHandle(){
        if(reviewPaperDatePicker.value == null){
            alert(Alert.AlertType.ERROR, "Pick a date!")
            return
        }
        if (Date.valueOf(reviewPaperDatePicker.value) < Date()) {
            alert(Alert.AlertType.ERROR,"Cannot choose date in the past!")
            return
        }
        conference.reviewPaperDeadline = Date.valueOf(reviewPaperDatePicker.value)
        service.updateConferenceDeadlines(conference)
        setLabels()
    }

    private fun postponeBiddingPhaseHandle(){
        if(biddingPhaseDatePicker.value == null){
            alert(Alert.AlertType.ERROR, "Pick a date!")
            return
        }
        if (Date.valueOf(biddingPhaseDatePicker.value) < Date()) {
            alert(Alert.AlertType.ERROR,"Cannot choose date in the past!")
            return
        }
        conference.biddingPhaseDeadline = Date.valueOf(biddingPhaseDatePicker.value)
        service.updateConferenceDeadlines(conference)
        setLabels()
    }

    private fun goBackHandle(){
        replaceWith(parent,
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT))
    }

    private fun setLabels(){
        submitProposalLabel.text = "Current deadline: " + conference.submitPaperDeadline.toString()
        reviewPaperLabel.text = "Current deadline: " + conference.reviewPaperDeadline.toString()
        biddingPhaseLabel.text = "Current deadline: " + conference.biddingPhaseDeadline.toString()
    }
}
