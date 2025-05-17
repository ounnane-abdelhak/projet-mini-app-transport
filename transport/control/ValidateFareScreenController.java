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
import transport.core.ServiceReclamation; // Added import
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
    private ServiceReclamation serviceReclamation; // Added field for ServiceReclamation

    public void initialize() {
        // DO NOT initialize DataManager here. It will be injected.
        System.out.println("ValidateFareScreenController: Initialized.");
    }

    // Renamed and modified to accept both shared services
    public void setSharedServices(DataManager dataManager, ServiceReclamation serviceReclamation) {
        this.dataManager = dataManager;
        this.serviceReclamation = serviceReclamation; // Store it
        System.out.println("ValidateFareScreenController: Received DataManager. Hash: " + (this.dataManager != null ? this.dataManager.hashCode() : "null"));
        System.out.println("ValidateFareScreenController: Received ServiceReclamation. Hash: " + (this.serviceReclamation != null ? this.serviceReclamation.hashCode() : "null"));
    }

    @FXML
    void handleValidate(ActionEvent event) {
        if (dataManager == null) {
            validationStatusLabel.setText("Error: DataManager not initialized.");
            validationStatusLabel.setStyle("-fx-text-fill: red;");
            return;
        }
        System.out.println("ValidateFareScreenController: handleValidate called. DataManager Hash: " + dataManager.hashCode());

        String idText = passIdField.getText();
        if (idText.isEmpty()) {
            validationStatusLabel.setText("Please enter a Pass ID.");
            validationStatusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            int passId = Integer.parseInt(idText);
            List<TitreTransport> allTitres = dataManager.getTitresVendus();

            Optional<TitreTransport> titreOpt = allTitres.stream()
                    .filter(t -> t.getIdTitre() == passId)
                    .findFirst();

            if (titreOpt.isPresent()) {
                TitreTransport titreToValidate = titreOpt.get();
                try {
                    boolean isValid = titreToValidate.valider();
                    if (isValid) {
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
        System.out.println("ValidateFareScreenController: Navigating back to Welcome Screen. Passing DM Hash: " + (dataManager != null ? dataManager.hashCode() : "null") + ", SR Hash: " + (serviceReclamation != null ? serviceReclamation.hashCode() : "null"));
        loadScene(event, "/transport/ui/WelcomeScreen.fxml");
    }

    private void loadScene(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent nextSceneRoot = loader.load();
            Object controller = loader.getController();

            // When going back to WelcomeScreen, pass both shared services
            if (controller instanceof WelcomeScreenController) {
                if (this.dataManager != null && this.serviceReclamation != null) {
                    System.out.println("ValidateFareScreenController: Passing services to WelcomeScreenController. DM Hash: " + this.dataManager.hashCode() + ", SR Hash: " + this.serviceReclamation.hashCode());
                    ((WelcomeScreenController) controller).setSharedServices(this.dataManager, this.serviceReclamation);
                } else {
                    System.err.println("ValidateFareScreenController: DataManager or ServiceReclamation is null here, cannot pass to WelcomeScreen.");
                }
            }
            // Add similar blocks if this controller can navigate to other controllers that need shared services

            Scene nextScene = new Scene(nextSceneRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(nextScene);
            stage.show();
        } catch (IOException e) {
            System.err.println("ValidateFareScreenController: Failed to load FXML file: " + fxmlFile);
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Could not load the requested screen.");
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait();
        }
    }
}

