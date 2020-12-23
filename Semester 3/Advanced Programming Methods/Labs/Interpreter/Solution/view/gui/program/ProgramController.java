package view.gui.program;

import controller.Controller;
import exceptions.InterpreterError;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.ProgramState;
import model.adt.Heap;
import model.adt.IHeap;
import model.adt.IList;
import model.statements.Statement;
import model.values.Value;

import java.util.*;
import java.util.stream.Collectors;

public class ProgramController {
    private Controller controller;

    @FXML
    private TableView<Map.Entry<Integer, String>> heapTable;

    @FXML
    private TableColumn<Map.Entry<Integer, String>, Integer> addressColumn;

    @FXML
    private TableColumn<Map.Entry<Integer, String>, String> valueColumn;

    @FXML
    private ListView<String> outputList;

    @FXML
    private ListView<String> fileList;

    @FXML
    private ListView<Integer> programStateList;

    @FXML
    private ListView<String> executionStackList;

    @FXML
    private TableView<Map.Entry<String, String>> symbolTable;

    @FXML
    private TableColumn<Map.Entry<String, String>, String> symVariableColumn;

    @FXML
    private TableColumn<Map.Entry<String, String>, String> symValueColumn;

    @FXML
    private TextField numberOfProgramStates;

    @FXML
    private Button oneStep;

    @FXML
    public void initialize() {
        addressColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        valueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue()));
        symVariableColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        symValueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue()));
        oneStep.setOnAction(actionEvent -> {
            if(controller == null){
                Alert alert = new Alert(Alert.AlertType.ERROR, "The program was not selected", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            boolean programStateLeft = Objects.requireNonNull(getCurrentProgramState()).getExecutionStack().isEmpty();
            if(programStateLeft){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Nothing left to execute", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            try {
                controller.oneStepAll();
                populate();
            } catch (InterpreterError interpreterError) {
                Alert alert = new Alert(Alert.AlertType.ERROR, interpreterError.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        });
        programStateList.setOnMouseClicked(mouseEvent -> populate());
    }

    private ProgramState getCurrentProgramState(){
        if (controller.getProgramStates().size() == 0)
            return null;
        int currentId = programStateList.getSelectionModel().getSelectedIndex();
        if (currentId == -1)
            currentId = controller.getProgramStates().get(0).id;
        else currentId = controller.getProgramStates().get(currentId).id;
        ProgramState stateToReturn = null;
        for (ProgramState state: controller.getProgramStates())
            if (state.id == currentId)
                stateToReturn = state;
        return stateToReturn;
    }

    public void setController(Controller controller) {
        this.controller = controller;
        populate();
    }

    private void populate() {
        populateHeap();
        populateProgramStateIdentifiers();
        populateFileTable();
        populateOutput();
        populateSymbolTable();
        populateExecutionStack();
    }

    private void populateHeap() {
        IHeap heap;
        if (controller.getProgramStates().size() > 0)
            heap = controller.getProgramStates().get(0).getHeap();
        else heap = new Heap();
        Map<Integer, String> heapMap = new HashMap<>();
        for (Map.Entry<Integer, Value> entry : heap.getContent().entrySet())
            heapMap.put(entry.getKey(), entry.getValue().toString());
        List<Map.Entry<Integer, String>> heapTableList = new ArrayList<>(heapMap.entrySet());
        heapTable.setItems(FXCollections.observableList(heapTableList));
        heapTable.refresh();
    }

    private void populateProgramStateIdentifiers() {
        List<ProgramState> programStates = controller.getProgramStates();
        var idList = programStates.stream().map(ps -> ps.id).collect(Collectors.toList());
        programStateList.setItems(FXCollections.observableList(idList));
        numberOfProgramStates.setText("" + programStates.size());
    }

    private void populateFileTable() {
        ArrayList<String> files;
        if (controller.getProgramStates().size() > 0)
            files = new ArrayList<>(controller.getProgramStates().get(0).getFileTable().keySet());
        else files = new ArrayList<>();
        fileList.setItems(FXCollections.observableArrayList(files));
    }

    private void populateOutput() {
        IList<String> output;
        if (controller.getProgramStates().size() > 0)
            output = controller.getProgramStates().get(0).getOut();
        else output = new model.adt.List<>();
        outputList.setItems(FXCollections.observableList(output.getList()));
        outputList.refresh();
    }

    private void populateSymbolTable() {
        ProgramState state = getCurrentProgramState();
        Map<String, String> symTableMap = new HashMap<>();
        if (state != null)
            for (Map.Entry<String, Value> entry : state.getSymTable().getContent().entrySet())
                symTableMap.put(entry.getKey(), entry.getValue().toString());
        List<Map.Entry<String, String>> symbolTableList = new ArrayList<>(symTableMap.entrySet());
        symbolTable.setItems(FXCollections.observableList(symbolTableList));
        symbolTable.refresh();
    }

    private void populateExecutionStack() {
        ProgramState state = getCurrentProgramState();
        List<String> executionStackListAsString = new ArrayList<>();
        if (state != null)
            for(Statement s : state.getExecutionStack().getStack()){
                executionStackListAsString.add(s.toString());
            }
        executionStackList.setItems(FXCollections.observableList(executionStackListAsString));
        executionStackList.refresh();
    }
}
