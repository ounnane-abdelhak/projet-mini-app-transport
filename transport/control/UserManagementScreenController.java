package transport.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import transport.core.*; // Import all core classes

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserManagementScreenController {

    // --- FXML Fields for Add User Tab ---
    @FXML private ChoiceBox<String> userTypeChoiceBox;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private DatePicker dateNaissancePicker;
    @FXML private CheckBox handicapCheckBox;
    @FXML private Label matriculeLabel;
    @FXML private TextField matriculeField;
    @FXML private Label fonctionLabel;
    @FXML private ChoiceBox<TypeFonction> fonctionChoiceBox;
    @FXML private Button addUserButton;
    @FXML private Label userStatusLabel;

    // --- FXML Fields for Purchase Fare Tab ---
    @FXML private ComboBox<Personne> userComboBoxPurchase; // To select existing user
    @FXML private ChoiceBox<String> fareTypeChoiceBox; // "Ticket" or "Personal Card"
    @FXML private Label cardTypeLabel;
    @FXML private ChoiceBox<CarteType> cardTypeChoiceBox; // For Personal Card type
    @FXML private Button purchaseFareButton;
    @FXML private Label purchaseStatusLabel;

    @FXML private Button backToMenuButton;

    // In-memory data stores (to be replaced or supplemented by DataManager)
    private List<Personne> personnes = new ArrayList<>();
    private List<TitreTransport> titresVendus = new ArrayList<>();
    private DataManager dataManager; // Instance of DataManager

    public void initialize() {
        dataManager = new DataManager(); // Initialize DataManager
        // Load existing data if any - for now, DataManager handles its own internal lists
        // personnes.addAll(dataManager.getPersonnes());
        // titresVendus.addAll(dataManager.getTitresVendus());

        // Populate ChoiceBoxes
        userTypeChoiceBox.setItems(FXCollections.observableArrayList("Usager", "Employe"));
        userTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean isEmploye = "Employe".equals(newVal);
            matriculeLabel.setVisible(isEmploye);
            matriculeField.setVisible(isEmploye);
            fonctionLabel.setVisible(isEmploye);
            fonctionChoiceBox.setVisible(isEmploye);
        });
        userTypeChoiceBox.getSelectionModel().selectFirst(); // Default to Usager

        fonctionChoiceBox.setItems(FXCollections.observableArrayList(TypeFonction.values()));
        fonctionChoiceBox.getSelectionModel().selectFirst();

        fareTypeChoiceBox.setItems(FXCollections.observableArrayList("Ticket", "Personal Navigation Card"));
        fareTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean isCard = "Personal Navigation Card".equals(newVal);
            cardTypeLabel.setVisible(isCard);
            cardTypeChoiceBox.setVisible(isCard);
        });
        fareTypeChoiceBox.getSelectionModel().selectFirst();

        cardTypeChoiceBox.setItems(FXCollections.observableArrayList(CarteType.values()));
        cardTypeChoiceBox.getSelectionModel().selectFirst();

        updateUserComboBox();
    }

    private void updateUserComboBox() {
        // Filter for Usagers only, as per typical fare purchase scenarios for personal cards/tickets
        // Or allow selection of any Personne if Employes can also purchase these specific fares
        List<Personne> usagers = personnes.stream().filter(p -> p instanceof Usager).collect(Collectors.toList());
        userComboBoxPurchase.setItems(FXCollections.observableArrayList(usagers));
        userComboBoxPurchase.setConverter(new PersonneStringConverter());
    }

    @FXML
    void handleAddUser(ActionEvent event) {
        try {
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            LocalDate dateNaissance = dateNaissancePicker.getValue();
            boolean handicap = handicapCheckBox.isSelected();

            if (nom.isEmpty() || prenom.isEmpty() || dateNaissance == null) {
                userStatusLabel.setText("Error: Name, surname, and birth date are required.");
                return;
            }

            Personne newUser;
            if ("Employe".equals(userTypeChoiceBox.getValue())) {
                String matricule = matriculeField.getText();
                TypeFonction fonction = fonctionChoiceBox.getValue();
                if (matricule.isEmpty() || fonction == null) {
                    userStatusLabel.setText("Error: Matricule and fonction are required for an employee.");
                    return;
                }
                newUser = new Employe(nom, prenom, dateNaissance, handicap, matricule, fonction);
            } else {
                newUser = new Usager(nom, prenom, dateNaissance, handicap);
            }
            personnes.add(newUser);
            dataManager.addPersonne(newUser); // Add to DataManager as well
            userStatusLabel.setText("User added: " + newUser.toString());
            clearUserFields();
            updateUserComboBox(); // Refresh the user list for purchase tab
            // dataManager.saveUsers(); // Optionally save immediately
        } catch (Exception e) {
            userStatusLabel.setText("Error adding user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearUserFields() {
        nomField.clear();
        prenomField.clear();
        dateNaissancePicker.setValue(null);
        handicapCheckBox.setSelected(false);
        matriculeField.clear();
        fonctionChoiceBox.getSelectionModel().selectFirst();
        userTypeChoiceBox.getSelectionModel().selectFirst();
    }

    @FXML
    void handlePurchaseFare(ActionEvent event) {
        try {
            Personne selectedUser = userComboBoxPurchase.getValue();
            String fareType = fareTypeChoiceBox.getValue();

            if (selectedUser == null || fareType == null) {
                purchaseStatusLabel.setText("Error: Please select a user and a fare type.");
                return;
            }

            TitreTransport newTitre;
            if ("Ticket".equals(fareType)) {
                newTitre = new Ticket(LocalDate.now());
            } else if ("Personal Navigation Card".equals(fareType)) {
                CarteType cardType = cardTypeChoiceBox.getValue();
                if (cardType == null) {
                    purchaseStatusLabel.setText("Error: Please select a card type.");
                    return;
                }
                newTitre = new CartePersonnelle(LocalDate.now(), selectedUser, cardType);
            } else {
                purchaseStatusLabel.setText("Error: Invalid fare type selected.");
                return;
            }

            titresVendus.add(newTitre);
            dataManager.addTitreVendu(newTitre); // Add to DataManager
            purchaseStatusLabel.setText("Fare purchased: " + newTitre.toString());
            // dataManager.saveFares(); // Optionally save immediately
        } catch (Exception e) {
            purchaseStatusLabel.setText("Error purchasing fare: " + e.getMessage());
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
            Scene nextScene = new Scene(nextSceneRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(nextScene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load FXML file: " + fxmlFile);
            e.printStackTrace();
            // Show an alert to the user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Could not load the requested screen.");
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait();
        }
    }

    // Helper class for ComboBox display
    private static class PersonneStringConverter extends javafx.util.StringConverter<Personne> {
        @Override
        public String toString(Personne personne) {
            if (personne == null) {
                return null;
            }
            return personne.getPrenom() + " " + personne.getNom() + (personne instanceof Employe ? " (Employe)" : " (Usager)");
        }

        @Override
        public Personne fromString(String string) {
            // Not needed for a non-editable ComboBox
            return null;
        }
    }
}

