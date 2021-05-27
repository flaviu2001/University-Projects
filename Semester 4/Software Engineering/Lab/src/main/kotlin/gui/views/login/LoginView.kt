package gui.views.login

import gui.views.user.AdminView
import gui.views.user.UserView
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.*


class LoginView : View("CMS") {
    private val service = Service()

    override val root : GridPane by fxml()
    private val usernameField : TextField by fxid()
    private val passwordField : PasswordField by fxid()
    private val loginButton: Button by fxid()
    private val createAccountButton: Button by fxid()

    init {
        loginButton.apply { action { handleLogin() }}
        createAccountButton.apply { action { handleCreateAccount()}}
    }

    private fun handleLogin() {
        val user = service.usersWithNameAndPassword(usernameField.text, passwordField.text)
        if(user.isNotEmpty()){
            if (usernameField.text == "admin") {
                replaceWith(
                    AdminView(service),
                    ViewTransition.Explode(1.seconds)
                )
            } else {
                replaceWith(
                    UserView(user[0], service),
                    ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
                )
            }
        }else{
            alert(Alert.AlertType.INFORMATION, "Bad credentials")
        }
    }

    private fun handleCreateAccount() {
        replaceWith(CreateAccountView(service), ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT))
    }
}