package nueats.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import nueats.models.User;
import nueats.utils.DataManager;
import nueats.utils.SceneManager;

public class RegisterController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private RadioButton studentRadio;
    @FXML private RadioButton facultyRadio;
    @FXML private RadioButton staffRadio;
    @FXML private TextField studentIdField;
    @FXML private TextField hostelField;
    @FXML private TextField departmentField;
    @FXML private Button registerButton1;
    @FXML private Button loginButton;
    @FXML private Button backButton;
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

        // Add button actions
        registerButton1.setOnAction(e -> handleRegister());
        loginButton.setOnAction(e -> handleBackToLogin());
        backButton.setOnAction(e -> handleBackToLogin());
    }

    @FXML
    private void handleRegister() {
        // Validate input
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String phone = phoneField.getText().trim();
        String studentId = studentIdField.getText().trim();
        String hostel = hostelField.getText().trim();
        String department = departmentField.getText().trim();

        // Basic validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError("Please fill in all required fields (*)");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }

        if (password.length() < 6) {
            showError("Password must be at least 6 characters");
            return;
        }

        if (dataManager.userExists(email)) {
            showError("An account with this email already exists");
            return;
        }

        // Get selected user type
        RadioButton selectedRadio = (RadioButton) userTypeGroup.getSelectedToggle();
        String userType = selectedRadio.getUserData().toString();

        // Create new user
        User newUser = new User(0, name, email, password, userType, 
                               phone, studentId, hostel, department);
        
        dataManager.addUser(newUser);
        dataManager.setCurrentUser(newUser);

        // Show success and navigate
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Account Created Successfully");
        alert.setContentText("Welcome to NUEats, " + name + "!");
        alert.showAndWait();

        // Navigate to menu
        SceneManager.switchScene("/nueats/Menu.fxml");
    }

    @FXML
    private void handleBackToLogin() {
        SceneManager.switchScene("/nueats/LoginView.fxml");
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(!message.isEmpty());
    }
}
