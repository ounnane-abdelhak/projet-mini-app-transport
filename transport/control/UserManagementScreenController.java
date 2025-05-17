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
import java.time.format.DateTimeParseException;

public class UserManagementScreenController {
    @FXML private ChoiceBox<Methodpay>   MethodeChoiceBox;
    @FXML private ChoiceBox<String> userTypeChoiceBox;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private DatePicker dateNaissancePicker;
    @FXML private CheckBox handicapCheckBox;
    @FXML private TextField matriculeField; // For Employe
    @FXML private ChoiceBox<TypeFonction> fonctionChoiceBox; // For Employe
    @FXML private Label userStatusLabel; // Renamed from purchaseStatusLabel to match FXML for user add status
    @FXML private Button addUserButton;
    @FXML private Button backToMenuButton;

    // Fields for fare purchase
    @FXML private Tab farePurchaseTab;
    @FXML private ComboBox<Personne> userComboBoxPurchase;
    @FXML private ChoiceBox<String> fareTypeChoiceBox; // "Ticket" or "CartePersonnelle"
    @FXML private ChoiceBox<CarteType> cardTypeChoiceBox; // For CartePersonnelle
    @FXML private Button purchaseFareButton;
    @FXML private Label fareStatusLabel; // This is for fare purchase status

    private DataManager dataManager;
    private ServiceReclamation serviceReclamation; // Added to hold and pass this service

    public void initialize() {
        MethodeChoiceBox.setItems(FXCollections.observableArrayList(Methodpay.values()));
        MethodeChoiceBox.getSelectionModel().selectFirst();

        userTypeChoiceBox.setItems(FXCollections.observableArrayList("Usager", "Employe"));
        userTypeChoiceBox.getSelectionModel().selectFirst();
        userTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean isEmploye = "Employe".equals(newVal);
            matriculeField.setVisible(isEmploye);
            matriculeField.setManaged(isEmploye);
            fonctionChoiceBox.setVisible(isEmploye);
            fonctionChoiceBox.setManaged(isEmploye);
        });
        matriculeField.setVisible(false);
        matriculeField.setManaged(false);
        fonctionChoiceBox.setVisible(false);
        fonctionChoiceBox.setManaged(false);
        fonctionChoiceBox.setItems(FXCollections.observableArrayList(TypeFonction.values()));

        // Fare purchase tab initialization
        fareTypeChoiceBox.setItems(FXCollections.observableArrayList("Ticket", "Carte Personnelle"));
        fareTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            cardTypeChoiceBox.setVisible("Carte Personnelle".equals(newVal));
            cardTypeChoiceBox.setManaged("Carte Personnelle".equals(newVal));
        });
        cardTypeChoiceBox.setItems(FXCollections.observableArrayList(CarteType.values()));
        cardTypeChoiceBox.setVisible(false);
        cardTypeChoiceBox.setManaged(false);
    }

    // Updated to accept both services
    public void setSharedServices(DataManager dataManager, ServiceReclamation serviceReclamation) {
        this.dataManager = dataManager;
        this.serviceReclamation = serviceReclamation;
        System.out.println("UserManagementScreenController: Received DataManager. Hash: " + (this.dataManager != null ? this.dataManager.hashCode() : "null") + ". Users in DM: " + (this.dataManager != null ? this.dataManager.getPersonnes().size() : "N/A"));
        System.out.println("UserManagementScreenController: Received ServiceReclamation. Hash: " + (this.serviceReclamation != null ? this.serviceReclamation.hashCode() : "null"));
        if (this.dataManager != null && userComboBoxPurchase != null) {
            userComboBoxPurchase.setItems(FXCollections.observableArrayList(this.dataManager.getPersonnes()));
            userComboBoxPurchase.setConverter(new PersonneStringConverter());
        }
    }

    @FXML
    void handleAddUser(ActionEvent event) {
        if (dataManager == null) {
            if (userStatusLabel != null) userStatusLabel.setText("Error: DataManager not initialized.");
            else System.err.println("UserManagementScreenController Error: userStatusLabel is null in handleAddUser");
            return;
        }
        System.out.println("UserManagementScreenController: handleAddUser called. DataManager Hash: " + dataManager.hashCode());
        try {
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            LocalDate dateNaissance = dateNaissancePicker.getValue();
            boolean handicap = handicapCheckBox.isSelected();
            String userType = userTypeChoiceBox.getValue();

            if (nom.isEmpty() || prenom.isEmpty() || dateNaissance == null) {
                if (userStatusLabel != null) userStatusLabel.setText("Nom, prénom et date de naissance sont requis.");
                return;
            }

            Personne newUser;
            if ("Employe".equals(userType)) {
                String matricule = matriculeField.getText();
                TypeFonction fonction = fonctionChoiceBox.getValue();
                if (matricule.isEmpty() || fonction == null) {
                    if (userStatusLabel != null) userStatusLabel.setText("Matricule et fonction sont requis pour un employé.");
                    return;
                }
                newUser = new Employe(nom, prenom, dateNaissance, handicap, matricule, fonction);
            } else {
                newUser = new Usager(nom, prenom, dateNaissance, handicap);
            }

            dataManager.addPersonne(newUser);
            dataManager.saveUsers(); // Save immediately after adding
            if (userStatusLabel != null) userStatusLabel.setText("Utilisateur ajouté: " + newUser.getPrenom() + " " + newUser.getNom());
            System.out.println("UserManagementScreenController: User added. Total users in DM: " + dataManager.getPersonnes().size());
            clearUserFields();
            // Refresh ComboBox for fare purchase
            if (this.dataManager != null && userComboBoxPurchase != null) {
                userComboBoxPurchase.setItems(FXCollections.observableArrayList(this.dataManager.getPersonnes()));
            }

        } catch (DateTimeParseException e) {
            if (userStatusLabel != null) userStatusLabel.setText("Format de date invalide.");
        } catch (Exception e) {
            if (userStatusLabel != null) userStatusLabel.setText("Erreur lors de l'ajout de l'utilisateur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handlePurchaseFare(ActionEvent event) {
        if (dataManager == null) {
            if (fareStatusLabel != null) fareStatusLabel.setText("Error: DataManager not initialized.");
            else System.err.println("UserManagementScreenController Error: fareStatusLabel is null in handlePurchaseFare");
            return;
        }
        System.out.println("UserManagementScreenController: handlePurchaseFare called. DataManager Hash: " + dataManager.hashCode());
        try {
            Personne selectedUser = userComboBoxPurchase.getValue();
            String fareType = fareTypeChoiceBox.getValue();

            if (selectedUser == null || fareType == null) {
                if (fareStatusLabel != null) fareStatusLabel.setText("Veuillez sélectionner un utilisateur et un type de titre.");
                return;
            }

            TitreTransport newTitre;
            if ("Ticket".equals(fareType)) {
                newTitre = new Ticket(LocalDate.now());
            } else if ("Carte Personnelle".equals(fareType)) {
                CarteType selectedCarteType = cardTypeChoiceBox.getValue();
                Methodpay selectedmethode = MethodeChoiceBox.getValue();
                if (selectedCarteType == null) {
                    if (fareStatusLabel != null) fareStatusLabel.setText("Veuillez sélectionner un type de carte.");
                    return;
                }
                newTitre = new CartePersonnelle(LocalDate.now(), selectedUser, selectedCarteType,selectedmethode);
            } else {
                if (fareStatusLabel != null) fareStatusLabel.setText("Type de titre inconnu.");
                return;
            }

            dataManager.addTitreVendu(newTitre);
            dataManager.saveFares(); // Save fares immediately
            if (fareStatusLabel != null) fareStatusLabel.setText("Titre acheté: " + newTitre.getClass().getSimpleName() + " N°" + newTitre.getIdTitre() + " pour " + selectedUser.getPrenom());
            System.out.println("UserManagementScreenController: Fare purchased. Total fares in DM: " + dataManager.getTitresVendus().size());

        } catch (ReductionImpossibleException e) {
            if (fareStatusLabel != null) fareStatusLabel.setText("Achat impossible: " + e.getMessage());
        } catch (Exception e) {
            if (fareStatusLabel != null) fareStatusLabel.setText("Erreur lors de l'achat du titre: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearUserFields() {
        nomField.clear();
        prenomField.clear();
        dateNaissancePicker.setValue(null);
        handicapCheckBox.setSelected(false);
        matriculeField.clear();
        fonctionChoiceBox.getSelectionModel().clearSelection();
        userTypeChoiceBox.getSelectionModel().selectFirst();
        MethodeChoiceBox.getSelectionModel().selectFirst();
    }

    @FXML
    void handleBackToMenu(ActionEvent event) {
        System.out.println("UserManagementScreenController: Navigating back to Welcome Screen. Passing DM Hash: " + (dataManager != null ? dataManager.hashCode() : "null") + ", SR Hash: " + (serviceReclamation != null ? serviceReclamation.hashCode() : "null"));
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
                    System.out.println("UserManagementScreenController: Passing services to WelcomeScreenController. DM Hash: " + this.dataManager.hashCode() + ", SR Hash: " + this.serviceReclamation.hashCode());
                    ((WelcomeScreenController) controller).setSharedServices(this.dataManager, this.serviceReclamation);
                } else {
                    System.err.println("UserManagementScreenController: DataManager or ServiceReclamation is null here, cannot pass to WelcomeScreen.");
                }
            }
            // Add similar blocks if this controller can navigate to other controllers that need shared services

            Scene nextScene = new Scene(nextSceneRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(nextScene);
            stage.show();
        } catch (IOException e) {
            System.err.println("UserManagementScreenController: Failed to load FXML file: " + fxmlFile);
            e.printStackTrace();
        }
    }


    // Helper class for ComboBox display
    private static class PersonneStringConverter extends javafx.util.StringConverter<Personne> {
        @Override
        public String toString(Personne personne) {
            if (personne == null) return null;
            return personne.getPrenom() + " " + personne.getNom() + (personne instanceof Employe ? " (Employe)" : " (Usager)");
        }
        @Override
        public Personne fromString(String string) { return null; }
    }
}