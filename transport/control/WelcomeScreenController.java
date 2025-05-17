package transport.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeScreenController {

    @FXML
    private Button userManagementButton;

    @FXML
    private Button validateFareButton;

    @FXML
    private Button complaintManagementButton;

    @FXML
    private Button displaySoldFaresButton;

    @FXML
    private Button exitButton;

    @FXML
    void handleUserManagement(ActionEvent event) {
        System.out.println("User & Fare Management Clicked");
        // Load UserManagementScreen.fxml (to be created)
        // loadScene(event, "/transport/ui/UserManagementScreen.fxml");
    }

    @FXML
    void handleValidateFare(ActionEvent event) {
        System.out.println("Validate Fare Medium Clicked");
        // Load ValidateFareScreen.fxml (to be created)
        // loadScene(event, "/transport/ui/ValidateFareScreen.fxml");
    }

    @FXML
    void handleComplaintManagement(ActionEvent event) {
        System.out.println("Complaint Management Clicked");
        // Load ComplaintScreen.fxml (to be created)
        // loadScene(event, "/transport/ui/ComplaintScreen.fxml");
    }

    @FXML
    void handleDisplaySoldFares(ActionEvent event) {
        System.out.println("Display Sold Fares Clicked");
        // Load DisplaySoldFaresScreen.fxml (to be created)
        // loadScene(event, "/transport/ui/DisplaySoldFaresScreen.fxml");
    }

    @FXML
    void handleExit(ActionEvent event) {
        System.out.println("Exit Clicked");
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    // Helper method to load new scenes (optional, can be in MainApp or a utility class)
    private void loadScene(ActionEvent event, String fxmlFile) {
        try {
            Parent nextSceneRoot = FXMLLoader.load(getClass().getResource(fxmlFile));
            Scene nextScene = new Scene(nextSceneRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(nextScene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load FXML file: " + fxmlFile);
            e.printStackTrace();
        }
    }
}

