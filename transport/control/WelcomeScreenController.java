package transport.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import transport.core.DataManager;
import transport.core.ServiceReclamation;

import java.io.IOException;

public class WelcomeScreenController {

    @FXML private Button userManagementButton;
    @FXML private Button validateFareButton;
    @FXML private Button complaintManagementButton;
    @FXML private Button displaySoldFaresButton;
    @FXML private Button exitButton;

    private DataManager dataManager;
    private ServiceReclamation serviceReclamation;

    // This method is called by MainApp to inject shared services
    public void setSharedServices(DataManager dataManager, ServiceReclamation serviceReclamation) {
        this.dataManager = dataManager;
        this.serviceReclamation = serviceReclamation;
        System.out.println("WelcomeScreenController: Received DataManager. Hash: " + (this.dataManager != null ? this.dataManager.hashCode() : "null") + ". Users: " + (this.dataManager != null ? this.dataManager.getPersonnes().size() : "N/A"));
        System.out.println("WelcomeScreenController: Received ServiceReclamation. Hash: " + (this.serviceReclamation != null ? this.serviceReclamation.hashCode() : "null"));
    }

    @FXML
    void handleUserManagement(ActionEvent event) {
        System.out.println("WelcomeScreenController: User & Fare Management Clicked. DataManager Hash: " + (dataManager != null ? dataManager.hashCode() : "null") + ", ServiceReclamation Hash: " + (serviceReclamation != null ? serviceReclamation.hashCode() : "null"));
        loadScene(event, "/transport/ui/UserManagementScreen.fxml");
    }

    @FXML
    void handleValidateFare(ActionEvent event) {
        System.out.println("WelcomeScreenController: Validate Fare Medium Clicked. DataManager Hash: " + (dataManager != null ? dataManager.hashCode() : "null") + ", ServiceReclamation Hash: " + (serviceReclamation != null ? serviceReclamation.hashCode() : "null"));
        loadScene(event, "/transport/ui/ValidateFareScreen.fxml");
    }

    @FXML
    void handleComplaintManagement(ActionEvent event) {
        System.out.println("WelcomeScreenController: Complaint Management Clicked. DataManager Hash: " + (dataManager != null ? dataManager.hashCode() : "null") + ", ServiceReclamation Hash: " + (serviceReclamation != null ? serviceReclamation.hashCode() : "null"));
        loadScene(event, "/transport/ui/ComplaintScreen.fxml");
    }

    @FXML
    void handleDisplaySoldFares(ActionEvent event) {
        System.out.println("WelcomeScreenController: Display Sold Fares Clicked. DataManager Hash: " + (dataManager != null ? dataManager.hashCode() : "null") + ", ServiceReclamation Hash: " + (serviceReclamation != null ? serviceReclamation.hashCode() : "null"));
        loadScene(event, "/transport/ui/DisplaySoldFaresScreen.fxml");
    }

    @FXML
    void handleExit(ActionEvent event) {

        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close(); // This will trigger MainApp.stop() for saving data
    }

    private void loadScene(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent nextSceneRoot = loader.load();
            Object controller = loader.getController();

            System.out.println("WelcomeScreenController: Loading scene " + fxmlFile + ". Attempting to pass DataManager Hash: " + (this.dataManager != null ? this.dataManager.hashCode() : "null") + ", ServiceReclamation Hash: " + (this.serviceReclamation != null ? this.serviceReclamation.hashCode() : "null"));

            if (this.dataManager != null && this.serviceReclamation != null) {
                if (controller instanceof UserManagementScreenController) {
                    // Correctly call setSharedServices with both arguments
                    ((UserManagementScreenController) controller).setSharedServices(this.dataManager, this.serviceReclamation);
                }else if (controller instanceof DisplaySoldFaresScreenController) {
                    // Correctly call setSharedServices with both arguments
                    ((DisplaySoldFaresScreenController) controller).setSharedServices(this.dataManager, this.serviceReclamation);
                }else if (controller instanceof ValidateFareScreenController) {
                        // Correctly call setSharedServices with both arguments
                        ((ValidateFareScreenController) controller).setSharedServices(this.dataManager, this.serviceReclamation);
                    }else if (controller instanceof ComplaintScreenController) {
                    ((ComplaintScreenController) controller).setDataManager(this.dataManager, this.serviceReclamation);
                }
                // Add other controllers that might need these services
            } else {
                System.err.println("WelcomeScreenController: DataManager or ServiceReclamation is null in WelcomeScreenController, cannot pass to next controller.");
                // Optionally show an alert to the user here if this happens unexpectedly
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Service Error");
                alert.setHeaderText("Core services not available.");
                alert.setContentText("Some data services are not initialized. Please restart the application or contact support.");
                alert.showAndWait();
            }

            Scene nextScene = new Scene(nextSceneRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(nextScene);
            stage.show();
        } catch (IOException e) {
            System.err.println("WelcomeScreenController: Failed to load FXML file: " + fxmlFile);
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Could not load the requested screen.");
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
