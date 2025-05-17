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

    private DataManager dataManager; // Shared instance or passed from Welcome/MainApp

    public void initialize() {
        // This is a simplified way to get DataManager. 
        // In a real app, you might use dependency injection or pass it from the previous controller.
        // For now, we assume UserManagementScreenController might have populated it.
        // A better approach would be a singleton DataManager or passing it explicitly.
        // Let's assume for now we create a new instance and it loads data, or it's a shared static instance.
        dataManager = new DataManager(); 
        // dataManager.loadAllData(); // Ensure data is loaded if not already
        // For this screen, we primarily need access to titresVendus from DataManager
        // If UserManagementScreenController is the one adding to DataManager, we need a way to access that instance.
        // For simplicity, let's assume DataManager is a singleton or we pass the instance.
        // For now, we'll rely on the fact that UserManagementScreenController also uses a DataManager instance.
        // This part needs careful handling in a larger app for data consistency.
        // Let's assume we get the data from a new DataManager instance for now, which might not reflect live changes from other screens without saving/loading.
        // A better way: pass the DataManager instance from the WelcomeScreenController or MainApp.

        // For the purpose of this controller, let's assume we can get the data from a shared source or a new load.
        // We will use the dataManager instance created here.
        // To get live data, the DataManager instance should be shared across controllers.
        // One way is to pass it during scene loading.
        // For now, this controller will create its own DataManager and load from file if persistence is implemented.
        // If UserManagementScreenController saved data, this should load it.
        // dataManager.loadFares(); // Assuming a method to load just fares
        // For now, let's assume the list is populated by UserManagementScreenController and saved/loaded by DataManager

        refreshSoldFaresList();
    }

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
        refreshSoldFaresList(); // Refresh list once DataManager is set
    }


    @FXML
    void handleRefresh(ActionEvent event) {
        refreshSoldFaresList();
    }

    private void refreshSoldFaresList() {
        if (dataManager == null) {
            // This can happen if setDataManager was not called. Initialize a default one.
            // This is a fallback, proper injection is preferred.
            dataManager = new DataManager();
            // Potentially load data here if DataManager supports it
            // dataManager.loadAllData(); 
        }

        List<TitreTransport> titres = dataManager.getTitresVendus();
        if (titres != null) {
            // Requirement: Display the list of sold fare media in descending order of purchase date (most recent first).
            List<String> fareStrings = titres.stream()
                    .sorted(Comparator.comparing(TitreTransport::getDateEmission).reversed())
                    .map(TitreTransport::toString)
                    .collect(Collectors.toList());
            ObservableList<String> observableFares = FXCollections.observableArrayList(fareStrings);
            soldFaresListView.setItems(observableFares);
        } else {
            soldFaresListView.setItems(FXCollections.observableArrayList("No fares sold or data not loaded."));
        }
    }

    @FXML
    void handleBackToMenu(ActionEvent event) {
        loadScene(event, "/transport/ui/WelcomeScreen.fxml", null); // Pass null as DataManager for now
    }

    // Modified loadScene to potentially pass DataManager
    private void loadScene(ActionEvent event, String fxmlFile, DataManager dm) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent nextSceneRoot = loader.load();

            // If the next controller needs DataManager, set it up
            Object controller = loader.getController();
            if (dm != null) {
                if (controller instanceof UserManagementScreenController) {
                    // ((UserManagementScreenController) controller).setDataManager(dm); // Requires UserManagementScreenController to have setDataManager
                } else if (controller instanceof DisplaySoldFaresScreenController) {
                    ((DisplaySoldFaresScreenController) controller).setDataManager(dm);
                } else if (controller instanceof ValidateFareScreenController) {
                    // ((ValidateFareScreenController) controller).setDataManager(dm);
                } else if (controller instanceof ComplaintScreenController) {
                    // ((ComplaintScreenController) controller).setDataManager(dm);
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

