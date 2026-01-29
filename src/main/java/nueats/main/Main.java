package nueats.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nueats.utils.SceneManager;

public class Main extends Application {

    public static final String CURRENCY = "₹";

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Set the primary stage for SceneManager
        SceneManager.setPrimaryStage(primaryStage);
        
        // Load the login view
        Parent root = FXMLLoader.load(
                Main.class.getResource("/nueats/LoginView.fxml")
        );
        
        primaryStage.setTitle("NUEats - Campus Cafeteria System");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(false);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
