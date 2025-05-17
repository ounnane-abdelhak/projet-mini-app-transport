package transport.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import transport.core.DataManager;
import transport.core.TitreNonValideException;
import transport.core.TitreTransport;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ValidateFareScreenController {

    @FXML
    private TextField passIdField;

    @FXML
    private Button validateButton;

    @FXML
    private Label validationStatusLabel;

    @FXML
    private Button backToMenuButton;

    private DataManager dataManager;

    public void initialize() {
        // Initialize DataManager - ideally passed or injected
        dataManager = new DataManager();
        // dataManager.loadAllData(); // Load data if not already loaded by another part of the app
    }
    
    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @FXML
    void handleValidate(ActionEvent event) {
        String idText = passIdField.getText();
        if (idText.isEmpty()) {
            validationStatusLabel.setText("Please enter a Pass ID.");
            validationStatusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            int passId = Integer.parseInt(idText);
            // Assuming DataManager holds the list of all sold TitreTransport
            // If not, this logic needs to access the correct list (e.g., from UserManagementScreenController or a shared service)
            List<TitreTransport> allTitres = dataManager.getTitresVendus(); 
            // If DataManager is not shared, this list will be empty unless loaded from file.
            // This highlights the need for a shared DataManager instance.

            Optional<TitreTransport> titreOpt = allTitres.stream()
                    .filter(t -> t.getIdTitre() == passId)
                    .findFirst();

            if (titreOpt.isPresent()) {
                TitreTransport titreToValidate = titreOpt.get();
                try {
                    boolean isValid = titreToValidate.valider(); // This method now throws TitreNonValideException
                    if (isValid) { // valider() will throw exception if not valid
                        validationStatusLabel.setText("Pass ID: " + passId + " is VALID. Details: " + titreToValidate.toString());
                        validationStatusLabel.setStyle("-fx-text-fill: green;");
                    }
                } catch (TitreNonValideException e) {
                    validationStatusLabel.setText("Pass ID: " + passId + " is INVALID. Reason: " + e.getMessage());
                    validationStatusLabel.setStyle("-fx-text-fill: red;");
                }
            } else {
                validationStatusLabel.setText("Pass ID: " + passId + " not found.");
                validationStatusLabel.setStyle("-fx-text-fill: red;");
            }

        } catch (NumberFormatException e) {
            validationStatusLabel.setText("Invalid Pass ID format. Please enter a number.");
            validationStatusLabel.setStyle("-fx-text-fill: red;");
        } catch (Exception e) {
            validationStatusLabel.setText("Error during validation: " + e.getMessage());
            validationStatusLabel.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }
    }

    @FXML
    void handleBackToMenu(ActionEvent event) {
        loadScene(event, "/transport/ui/WelcomeScreen.fxml");
    }

    private void loadScene(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent nextSceneRoot = loader.load();
            
            // Pass DataManager if needed by the next controller
            Object controller = loader.getController();
            if (dataManager != null) {
                 if (controller instanceof UserManagementScreenController) {
                    // ((UserManagementScreenController) controller).setDataManager(dataManager);
                } else if (controller instanceof DisplaySoldFaresScreenController) {
                    ((DisplaySoldFaresScreenController) controller).setDataManager(dataManager);
                } else if (controller instanceof ValidateFareScreenController) {
                    ((ValidateFareScreenController) controller).setDataManager(dataManager);
                } else if (controller instanceof ComplaintScreenController) {
                    // ((ComplaintScreenController) controller).setDataManager(dataManager);
                }
            }

            Scene nextScene = new Scene(nextSceneRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(nextScene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load FXML file: " + fxmlFile);
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Could not load the requested screen.");
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait();
        }
    }
}

