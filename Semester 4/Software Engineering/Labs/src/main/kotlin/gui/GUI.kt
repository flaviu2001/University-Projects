package gui

import gui.views.login.LoginView
import tornadofx.*


class GUI : App(LoginView::class) {
    fun run(args: Array<String>) {
        launch<GUI>(args)
    }
}
