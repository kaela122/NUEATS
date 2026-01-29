package nueats.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import nueats.models.User;
import nueats.utils.DataManager;
import nueats.utils.SceneManager;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private RadioButton studentRadio;
    @FXML private RadioButton facultyRadio;
    @FXML private RadioButton staffRadio;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Button guestButton;
    @FXML private Label errorLabel;

    private ToggleGroup userTypeGroup;
    private DataManager dataManager;

    @FXML
    public void initialize() {
        dataManager = DataManager.getInstance();
        
        // Setup radio button group
        userTypeGroup = new ToggleGroup();
        studentRadio.setToggleGroup(userTypeGroup);
        facultyRadio.setToggleGroup(userTypeGroup);
        staffRadio.setToggleGroup(userTypeGroup);
        studentRadio.setSelected(true);

        // Add login button action
        loginButton.setOnAction(e -> handleLogin());
        
        // Add register button action
        registerButton.setOnAction(e -> handleRegister());
        
        // Add guest button action
        guestButton.setOnAction(e -> handleGuestLogin());

        // Enter key support
        passwordField.setOnAction(e -> handleLogin());
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }

        User user = dataManager.authenticateUser(email, password);
        
        if (user != null) {
            dataManager.setCurrentUser(user);
            showError(""); // Clear error
            
            // Navigate based on user type
            if (user.getUserType().equals("Admin")) {
                SceneManager.switchScene("/nueats/DashboardView.fxml");
            } else {
                SceneManager.switchScene("/nueats/Menu.fxml");
            }
        } else {
            showError("Invalid email or password");
        }
    }

    @FXML
    private void handleRegister() {
        SceneManager.switchScene("/nueats/RegisterView.fxml");
    }

    @FXML
    private void handleGuestLogin() {
        // Create a guest user account
        User guestUser = new User(0, "Guest User", "guest@nueats.com", "", "Guest");
        dataManager.setCurrentUser(guestUser);
        
        // Navigate to menu
        SceneManager.switchScene("/nueats/Menu.fxml");
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(!message.isEmpty());
    }
}
