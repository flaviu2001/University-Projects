package gui.views.login

import exceptions.ConferenceException
import javafx.scene.control.Alert
import javafx.scene.layout.GridPane
import service.Service
import javafx.scene.control.Button
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import tornadofx.*

class CreateAccountView(private val service: Service) : View("Create account") {
    override val root : GridPane by fxml()
    private val emailField : TextField by fxid()
    private val usernameField : TextField by fxid()
    private val passwordField : PasswordField by fxid()
    private val confirmPasswordField : PasswordField by fxid()
    private val fullNameField : TextField by fxid()
    private val affiliationField : TextField by fxid()
    private val personalWebsiteField : TextField by fxid()
    private val domainOfInterestField : TextField by fxid()

    private val createAccountButton:  Button by fxid()
   private val backToLoginButton: Button by fxid()

    init {
        createAccountButton.apply{ action { handleCreateAccount() }}
        backToLoginButton.apply{ action { handleLogout() }}
    }

    private fun handleLogout(){
        replaceWith(LoginView::class, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
    }

    private fun areFieldsFilled(): Boolean{
        if(emailField.text.isEmpty() || usernameField.text.isEmpty() || passwordField.text.isEmpty()
            || confirmPasswordField.text.isEmpty() || fullNameField.text.isEmpty() || affiliationField.text.isEmpty()
            || personalWebsiteField.text.isEmpty() || domainOfInterestField.text.isEmpty())
                return false
        return true
    }

    private fun handleCreateAccount(){
        if(!areFieldsFilled()) {
            alert(Alert.AlertType.ERROR, "All fields are mandatory")
            return
        }
        if(!passwordField.text.equals(confirmPasswordField.text)){
            alert(Alert.AlertType.ERROR, "Passwords don't match")
            return
        }
        try {
            service.addUser(usernameField.text, passwordField.text, emailField.text, fullNameField.text,
            affiliationField.text, personalWebsiteField.text, domainOfInterestField.text)
            alert(Alert.AlertType.INFORMATION, "Account created successfully")
        }catch (exception: ConferenceException){
            alert(Alert.AlertType.ERROR, exception.message)
        }
    }
}
