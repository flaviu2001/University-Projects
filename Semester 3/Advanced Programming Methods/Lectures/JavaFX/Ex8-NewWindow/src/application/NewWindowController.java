package application;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class NewWindowController {
	@FXML
	private Button button1;
	@FXML
	private Button button2;
	
	private Stage mainStage;

	// Event Listener on Button[#button1].onAction
	@FXML
	public void onButton1(ActionEvent event) {
		 Label secondLabel = new Label("I'm a Label on new NO-MODAL Window");
		 
         StackPane secondaryLayout = new StackPane();
         secondaryLayout.getChildren().add(secondLabel);

         Scene secondScene = new Scene(secondaryLayout, 230, 100);

         // New window (Stage)
         Stage newWindow = new Stage();
         newWindow.setTitle("Second Stage");
         newWindow.setScene(secondScene);

         // Set position of second window, related to primary window.
         newWindow.setX(mainStage.getX() + 200);
         newWindow.setY(mainStage.getY() + 100);

         newWindow.show();
	}
	// Event Listener on Button[#button2].onAction
	@FXML
	public void onButton2(ActionEvent event) {
		Label secondLabel = new Label("I'm a Label on new MODAL Window");
		 
        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(secondLabel);

        Scene secondScene = new Scene(secondaryLayout, 230, 100);

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Second Stage");
        newWindow.setScene(secondScene);

        // Specifies the modality for new window.
        newWindow.initModality(Modality.WINDOW_MODAL);

        // Specifies the owner Window (parent) for new window
        newWindow.initOwner(mainStage);

        // Set position of second window, related to primary window.
        newWindow.setX(mainStage.getX() + 200);
        newWindow.setY(mainStage.getY() + 100);

        newWindow.show();
	}
	public void setMainStage(Stage primaryStage) {
		// TODO Auto-generated method stub
		mainStage = primaryStage;
	}
}
