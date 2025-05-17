package transport.control;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import transport.core.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ComplaintScreenController {

    @FXML private ComboBox<Personne> reportingPersonComboBox;
    @FXML private ChoiceBox<TypeReclamation> complaintTypeChoiceBox;
    @FXML private ChoiceBox<String> entityTypeChoiceBox;
    @FXML private Label concernedEntityLabel;
    @FXML private ComboBox<Suspendable> concernedEntityComboBox;
    @FXML private TextArea descriptionTextArea;
    @FXML private Button registerComplaintButton;
    @FXML private Label complaintStatusLabel;
    @FXML private ListView<String> complaintsListView;
    @FXML private Button refreshComplaintsButton;
    @FXML private Button backToMenuButton;

    private DataManager dataManager;
    private ServiceReclamation serviceReclamation;

    private List<Station> stations = new ArrayList<>();
    private List<MoyenTransport> moyensTransport = new ArrayList<>();

    public void initialize() {
        complaintTypeChoiceBox.setItems(FXCollections.observableArrayList(TypeReclamation.values()));
        complaintTypeChoiceBox.getSelectionModel().selectFirst();

        entityTypeChoiceBox.setItems(FXCollections.observableArrayList("Station", "Moyen de Transport", "Aucun"));
        entityTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            updateConcernedEntityComboBox(newVal);
        });
        entityTypeChoiceBox.getSelectionModel().select("Aucun");
        System.out.println("ComplaintScreenController: Initialized.");
    }

    public void setDataManager(DataManager dataManager, ServiceReclamation serviceReclamation) {
        this.dataManager = dataManager;
        this.serviceReclamation = serviceReclamation;
        System.out.println("ComplaintScreenController: Received DataManager. Hash: " + (this.dataManager != null ? this.dataManager.hashCode() : "null") + ". Users in DM: " + (this.dataManager != null ? this.dataManager.getPersonnes().size() : "N/A"));
        System.out.println("ComplaintScreenController: Received ServiceReclamation. Hash: " + (this.serviceReclamation != null ? this.serviceReclamation.hashCode() : "null"));

        if (this.dataManager != null) {
            List<Personne> currentPersonnes = this.dataManager.getPersonnes();
            System.out.println("ComplaintScreenController: Populating reportingPersonComboBox with " + currentPersonnes.size() + " users.");
            reportingPersonComboBox.setItems(FXCollections.observableArrayList(currentPersonnes));
            reportingPersonComboBox.setConverter(new PersonneStringConverter());
            if (!currentPersonnes.isEmpty()) {
                reportingPersonComboBox.getSelectionModel().selectFirst();
            }
        }
        // Populate stations and moyensTransport (dummy data for now)
        if (stations.isEmpty()) {
            stations.add(new Station("Central Station", "123 Main St"));
            stations.add(new Station("North Station", "456 North Ave"));
        }
        if (moyensTransport.isEmpty()) {
            moyensTransport.add(new MoyenTransport("Bus-001"));
            moyensTransport.add(new MoyenTransport("Tram-002"));
        }
        refreshComplaintsList();
    }

    private void updateConcernedEntityComboBox(String entityType) {
        if ("Station".equals(entityType)) {
            concernedEntityLabel.setText("Concerned Station:");
            concernedEntityComboBox.setItems(FXCollections.observableArrayList(stations));
            concernedEntityComboBox.setConverter(new SuspendableStringConverter());
            concernedEntityComboBox.setDisable(false);
        } else if ("Moyen de Transport".equals(entityType)) {
            concernedEntityLabel.setText("Concerned Transport:");
            concernedEntityComboBox.setItems(FXCollections.observableArrayList(moyensTransport));
            concernedEntityComboBox.setConverter(new SuspendableStringConverter());
            concernedEntityComboBox.setDisable(false);
        } else {
            concernedEntityLabel.setText("Concerned Entity:");
            concernedEntityComboBox.setItems(FXCollections.emptyObservableList());
            concernedEntityComboBox.setDisable(true);
        }
        concernedEntityComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    void handleRegisterComplaint(ActionEvent event) {
        if (dataManager == null || serviceReclamation == null) {
            complaintStatusLabel.setText("Error: Services not initialized.");
            return;
        }
        System.out.println("ComplaintScreenController: handleRegisterComplaint called. DataManager Hash: " + dataManager.hashCode());
        try {
            Personne reporter = reportingPersonComboBox.getValue();
            TypeReclamation type = complaintTypeChoiceBox.getValue();
            String description = descriptionTextArea.getText();
            Suspendable concernedEntity = null;
            String entityType = entityTypeChoiceBox.getValue();

            if (reporter == null || type == null || description.isEmpty()) {
                complaintStatusLabel.setText("Error: Reporter, type, and description are required.");
                return;
            }
            if (!"Aucun".equals(entityType)) {
                concernedEntity = concernedEntityComboBox.getValue();
                if (concernedEntity == null) {
                    complaintStatusLabel.setText("Error: Please select the concerned entity or choose 'Aucun'.");
                    return;
                }
            }
            Reclamation newComplaint = new Reclamation(LocalDate.now(), type, description, reporter, concernedEntity);
            serviceReclamation.addReclamation(newComplaint);
            // serviceReclamation.saveReclamations(); // Or rely on MainApp.stop()
            complaintStatusLabel.setText("Complaint registered: N°" + newComplaint.getNumero());
            clearComplaintFields();
            refreshComplaintsList();
        } catch (Exception e) {
            complaintStatusLabel.setText("Error registering complaint: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearComplaintFields() {
        // reportingPersonComboBox.getSelectionModel().clearSelection(); // Keep selected user?
        descriptionTextArea.clear();
    }

    @FXML
    void handleRefreshComplaints(ActionEvent event) {
        refreshComplaintsList();
    }

    private void refreshComplaintsList() {
        if (serviceReclamation != null) {
            List<Reclamation> allReclamations = serviceReclamation.getAllReclamations();
            List<String> complaintStrings = allReclamations.stream()
                    .map(Reclamation::toString)
                    .collect(Collectors.toList());
            complaintsListView.setItems(FXCollections.observableArrayList(complaintStrings));
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
            Object controller = loader.getController();
            if (controller instanceof WelcomeScreenController && this.dataManager != null && this.serviceReclamation != null) {
                ((WelcomeScreenController) controller).setSharedServices(this.dataManager, this.serviceReclamation);
            }
            Scene nextScene = new Scene(nextSceneRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(nextScene);
            stage.show();
        } catch (IOException e) {
            System.err.println("ComplaintScreenController: Failed to load FXML file: " + fxmlFile);
            e.printStackTrace();
        }
    }

    private static class PersonneStringConverter extends javafx.util.StringConverter<Personne> {
        @Override
        public String toString(Personne personne) {
            if (personne == null) return null;
            return personne.getPrenom() + " " + personne.getNom() + (personne instanceof Employe ? " (Employe)" : " (Usager)");
        }
        @Override
        public Personne fromString(String string) { return null; }
    }

    private static class SuspendableStringConverter extends javafx.util.StringConverter<Suspendable> {
        @Override
        public String toString(Suspendable entity) {
            if (entity == null) return null;
            if (entity instanceof Station) return "Station: " + ((Station) entity).getNom();
            if (entity instanceof MoyenTransport) return "Transport: " + ((MoyenTransport) entity).getIdentifiant();
            return entity.toString();
        }
        @Override
        public Suspendable fromString(String string) { return null; }
    }
}
