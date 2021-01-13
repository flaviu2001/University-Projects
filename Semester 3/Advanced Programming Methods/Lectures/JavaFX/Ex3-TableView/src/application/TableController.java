package application;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;

public class TableController {
	@FXML
	private TableView<Person> tableView;
	@FXML
	private TableColumn<Person,String> firstName;
	@FXML
	private TableColumn<Person,String> lastName;
	@FXML
	private TableColumn<Person,String> emailAddress;
	@FXML
	private TableColumn<Person,Gender> genderColumn;
	@FXML
	private TextField firstNameField;
	@FXML
	private TextField lastNameField;
	@FXML
	private TextField emailField;

	// Event Listener on Button.onAction
	@FXML
	public void addPerson(ActionEvent event) {
		ObservableList<Person> data = tableView.getItems();
        data.add(new Person(firstNameField.getText(),
            lastNameField.getText(),
            emailField.getText(), 
            Gender.MALE.getCode()
        ));
 //       tableView.getSortOrder();          
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
	}
	
	
	@FXML
	public void changeSorting(ActionEvent event) {
		
   
        		ObservableList<TableColumn<Person,?>> sl= tableView.getSortOrder();
        		sl.clear();
        		sl.add(firstName);  
  
        
       
	}
	
	@FXML
	public void initialize() {
		firstName.setCellValueFactory(new PropertyValueFactory<Person,String>("firstName")); 
		//for editing
		firstName.setCellFactory(TextFieldTableCell.<Person>forTableColumn());
		firstName.setOnEditCommit(
			    (CellEditEvent<Person, String> t) -> {
			    	 TablePosition<Person, String> pos = t.getTablePosition();
			    	 int row = pos.getRow();
			    	 Person person = t.getTableView().getItems().get(row);
			    	 String newFirstName = t.getNewValue();
			    	 person.setFirstName(newFirstName);
			    	 
//			        ((Person) t.getTableView().getItems().get(
//			            t.getTablePosition().getRow())
//			            ).setFirstName(t.getNewValue());
			});
		
		lastName.setCellValueFactory(new PropertyValueFactory<Person,String>("lastName")); 
		emailAddress.setCellValueFactory(new PropertyValueFactory<Person,String>("email")); 
		
		//settings combo box column
		 genderColumn.setCellValueFactory(
				 new Callback<CellDataFeatures<Person, Gender>, ObservableValue<Gender>>() {
			 
	            @Override
	            public ObservableValue<Gender> call(CellDataFeatures<Person, Gender> param) {
	                Person person = param.getValue();
	                // F,M
	                String genderCode = person.getGender();
	                Gender gender = Gender.getByCode(genderCode);
	                return new SimpleObjectProperty<Gender>(gender);
	            }
	        });
		ObservableList<Gender> genderList = FXCollections.observableArrayList(Gender.values());
		genderColumn.setCellFactory(ComboBoxTableCell.forTableColumn(genderList));
		//edit combo box
		genderColumn.setOnEditCommit(
				(CellEditEvent<Person, Gender> event) -> {
					TablePosition<Person, Gender> pos = event.getTablePosition();
					Gender newGender = event.getNewValue();
					int row = pos.getRow();
					Person person = event.getTableView().getItems().get(row);
					person.setGender(newGender.getCode());
        });
		
		
		tableView.setItems(getPersonList());
	}
	
	 private ObservableList<Person> getPersonList() {
		 
	      Person p1 = new Person("Michael","Brown","m@example.com", Gender.MALE.getCode());
	      Person p2 = new Person("AAa","BB","a@example.com", Gender.FEMALE.getCode());
	 
	      ObservableList<Person> list = FXCollections.observableArrayList(p1,p2);
	      return list;
	  }
	 
//	 @FXML
//		public void editfirstNameColumn(){
//	 }
	 }
	 


