package view.gui.list;

import controller.Controller;
import exceptions.InterpreterError;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import model.ProgramState;
import model.statements.Statement;
import repository.IRepository;
import repository.Repository;
import view.Examples;
import view.gui.program.ProgramController;

public class ListController {
    private ProgramController programController;

    public void setProgramController(ProgramController programController) {
        this.programController = programController;
    }

    @FXML
    private ListView<Statement> statements;

    @FXML
    private Button displayButton;

    @FXML
    public void initialize() {
        statements.setItems(FXCollections.observableArrayList(Examples.exampleList()));
        displayButton.setOnAction(actionEvent -> {
            int index = statements.getSelectionModel().getSelectedIndex();
            if (index < 0)
                return;
            ProgramState state = new ProgramState(Examples.exampleList()[index]);
            IRepository repository = new Repository("log.txt");
            Controller controller = new Controller(repository);
            controller.addProgram(state);
            try{
                controller.runTypeChecker();
                programController.setController(controller);
            } catch (InterpreterError interpreterError) {
                Alert alert = new Alert(Alert.AlertType.ERROR, interpreterError.getMessage(), ButtonType.OK);
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.showAndWait();
            }
        });
    }
}
