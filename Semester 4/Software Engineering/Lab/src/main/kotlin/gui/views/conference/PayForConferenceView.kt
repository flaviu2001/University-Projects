package gui.views.conference

import domain.Conference
import domain.User
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.*

class PayForConferenceView(private val user: User,
                           private val service: Service,
                           private val parent: View,
                           private val conference: Conference) : View(user.name + " - " + conference.name) {

    override val root: GridPane by fxml()
    private val goBackButton: Button by fxid()
    private val payForConferenceButton: Button by fxid()

    private val cardNumberTextField: TextField by fxid()
    private val cardOwnerFullNameTextField: TextField by fxid()
    private val cardCVVTextField: TextField by fxid()

    init {
        goBackButton.apply{
            action {
                goBackHandle()
            }
        }

        payForConferenceButton.apply {
            action {
                payForConference()
            }
        }

        val userConference = service.getUserConference(user.id, conference.id)
        if (userConference != null) {
            if(userConference.paid){
                payForConferenceButton.isDisable = true
            }
        }
    }

    private fun payForConference() {
        if(cardNumberTextField.text.length != 16){
            alert(Alert.AlertType.INFORMATION, "Please enter a valid card number (has 16 digits)")
            return
        }
        if(cardOwnerFullNameTextField.text.isEmpty()){
            alert(Alert.AlertType.INFORMATION, "Please enter the full name")
            return
        }
        if(cardCVVTextField.text.length != 3){
            alert(Alert.AlertType.INFORMATION, "Please enter a valid CVV code (has 3 digits)")
            return
        }

        val userConference = service.getUserConference(user.id, conference.id)
        if (userConference != null) {
            userConference.paid = true
            alert(Alert.AlertType.INFORMATION, "Successfully paid") //No need for confirmation, information is enough
            payForConferenceButton.isDisable = true
            service.pay(user.id, conference.id)
        }
    }

    private fun goBackHandle(){
        replaceWith(parent,
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
    }

}