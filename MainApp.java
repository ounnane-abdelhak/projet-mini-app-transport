package transport;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import transport.control.WelcomeScreenController;
import transport.core.DataManager;
import transport.core.ServiceReclamation;

import java.awt.*;
import java.io.IOException;

public class MainApp extends Application {

    private DataManager dataManager;
    private ServiceReclamation serviceReclamation;

    @Override
    public void init() throws Exception {

        dataManager = new DataManager();
        serviceReclamation = new ServiceReclamation();

        dataManager.loadAllData(); // Loads users and fares
        serviceReclamation.loadReclamations();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/transport/ui/WelcomeScreen.fxml"));
        Parent root = loader.load();

        WelcomeScreenController welcomeController = loader.getController();
        welcomeController.setSharedServices(dataManager, serviceReclamation);

        primaryStage.setTitle("ESI-RUN Transport Management");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        if (dataManager != null) {
            dataManager.saveAllData(); // Saves users and fares
        }
        if (serviceReclamation != null) {
            serviceReclamation.saveReclamations();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

