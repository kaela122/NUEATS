package nueats.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage primaryStage;
    
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }
    
    public static void switchScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            
            // Try to load stylesheet
            try {
                scene.getStylesheets().add(
                    SceneManager.class.getResource("/nueats/values/style.css").toExternalForm()
                );
            } catch (Exception e) {
                // Stylesheet not found, continue without it
            }
            
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading scene: " + fxmlPath);
        }
    }
    
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
