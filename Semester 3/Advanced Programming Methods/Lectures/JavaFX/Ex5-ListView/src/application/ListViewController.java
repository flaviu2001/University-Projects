package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;


public class ListViewController {
	
	 @FXML
	 private ListView<Book> listView;
	 
	 @FXML
	 private Label label;
	 
	 
	 @FXML
	 public void initialize() {
		 listView.setItems(getBookList());
		// To set selection model
	      listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	 
	      // Select item at index = 1
	      listView.getSelectionModel().selectIndices(1);
	 
	      // Focus
	      listView.getFocusModel().focus(2);
	      
	      
	      listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Book>() {
	    	  
	            @Override
	            public void changed(ObservableValue<? extends Book> observable, Book oldValue, Book newValue) {
	                label.setText("OLD: " + oldValue + ",  NEW: " + newValue);
	            }
	        });
	 }
	
	 
	 private ObservableList<Book> getBookList() {
		 
		 Book book1 = new Book(1L, "J01", "Java IO Tutorial");
	     Book book2 = new Book(2L, "J02", "Java Enums Tutorial");
	     Book book3 = new Book(2L, "C01", "C# Tutorial for Beginners");
	 
	      // To Creating a Observable List
	     ObservableList<Book> books = FXCollections.observableArrayList(book1, book2, book3);
	     return books;
	  }
	 
	 
}
