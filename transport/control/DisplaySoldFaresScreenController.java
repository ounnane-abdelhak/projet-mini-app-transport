package transport.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import transport.core.DataManager;
import transport.core.ServiceReclamation; // Added import
import transport.core.TitreTransport;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DisplaySoldFaresScreenController {

    @FXML
    private ListView<String> soldFaresListView;

    @FXML
    private Button refreshButton;

    @FXML
    private Button backToMenuButton;

    private DataManager dataManager;
    private ServiceReclamation serviceReclamation; // Added field for ServiceReclamation

    public void initialize() {
        // DO NOT initialize DataManager here. It will be injected.
        System.out.println("DisplaySoldFaresScreenController: Initialized.");
        // The list will be refreshed once dataManager is set via setSharedServices.
    }

    // Renamed and modified to accept both shared services
    public void setSharedServices(DataManager dataManager, ServiceReclamation serviceReclamation) {
        this.dataManager = dataManager;
        this.serviceReclamation = serviceReclamation; // Store it
        System.out.println("DisplaySoldFaresScreenController: Received DataManager. Hash: " + (this.dataManager != null ? this.dataManager.hashCode() : "null"));
        System.out.println("DisplaySoldFaresScreenController: Received ServiceReclamation. Hash: " + (this.serviceReclamation != null ? this.serviceReclamation.hashCode() : "null"));
        refreshSoldFaresList(); // Refresh list once DataManager is set
    }

    @FXML
    void handleRefresh(ActionEvent event) {
        System.out.println("DisplaySoldFaresScreenController: handleRefresh called.");
        refreshSoldFaresList();
    }

    private void refreshSoldFaresList() {
        if (dataManager == null) {
            System.err.println("DisplaySoldFaresScreenController: DataManager is null in refreshSoldFaresList. Cannot refresh.");
            soldFaresListView.setItems(FXCollections.observableArrayList("Error: Data service not available."));
            return;
        }
        System.out.println("DisplaySoldFaresScreenController: Refreshing list. DataManager Hash: " + dataManager.hashCode());

        List<TitreTransport> titres = dataManager.getTitresVendus();
        if (titres != null) {
            List<String> fareStrings = titres.stream()
                    .sorted(Comparator.comparing(TitreTransport::getDateEmission).reversed())
                    .map(TitreTransport::toString)
                    .collect(Collectors.toList());
            ObservableList<String> observableFares = FXCollections.observableArrayList(fareStrings);
            soldFaresListView.setItems(observableFares);
            System.out.println("DisplaySoldFaresScreenController: List refreshed with " + fareStrings.size() + " items.");
        } else {
            soldFaresListView.setItems(FXCollections.observableArrayList("No fares sold or data not loaded."));
            System.out.println("DisplaySoldFaresScreenController: TitresVendus list is null.");
        }
    }

    @FXML
    void handleBackToMenu(ActionEvent event) {
        System.out.println("DisplaySoldFaresScreenController: Navigating back to Welcome Screen. Passing DM Hash: " + (dataManager != null ? dataManager.hashCode() : "null") + ", SR Hash: " + (serviceReclamation != null ? serviceReclamation.hashCode() : "null"));
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
                    System.out.println("DisplaySoldFaresScreenController: Passing services to WelcomeScreenController. DM Hash: " + this.dataManager.hashCode() + ", SR Hash: " + this.serviceReclamation.hashCode());
                    ((WelcomeScreenController) controller).setSharedServices(this.dataManager, this.serviceReclamation);
                } else {
                    System.err.println("DisplaySoldFaresScreenController: DataManager or ServiceReclamation is null here, cannot pass to WelcomeScreen.");
                }
            }
            // Add similar blocks if this controller can navigate to other controllers that need shared services

            Scene nextScene = new Scene(nextSceneRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(nextScene);
            stage.show();
        } catch (IOException e) {
            System.err.println("DisplaySoldFaresScreenController: Failed to load FXML file: " + fxmlFile);
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Could not load the requested screen.");
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
