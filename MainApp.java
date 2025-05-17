package transport;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the FXML file for the welcome screen
        // Ensure the path to WelcomeScreen.fxml is correct relative to the resources folder or classpath
        // For now, assuming it will be in transport/ui/
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/transport/ui/WelcomeScreen.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("ESI-RUN Transport Management");
        primaryStage.setScene(new Scene(root, 800, 600)); // Adjust size as needed
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

