package gui.views.user

import domain.Conference
import domain.User
import gui.views.login.LoginView
import javafx.collections.FXCollections
import javafx.scene.layout.GridPane
import service.Service
import javafx.geometry.Pos;
import javafx.scene.control.*
import tornadofx.*

class UserView(user: User, private val service: Service): View(user.name) {
    override val root : GridPane by fxml()
    private val logoutButton: Button by fxid()
    private val menuBar: MenuBar by fxid()
    private val conferenceListView: ListView<Conference> by fxid()
    init {
        logoutButton.apply { action { handleLogout() }}
        loadData()
    }

    private fun handleLogout(){
        replaceWith(LoginView::class, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT))
    }

    private fun loadData(){
        val observable = FXCollections.observableArrayList<Conference>(service.getConferences())
        conferenceListView.items.addAll(observable)
    }
}