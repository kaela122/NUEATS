package nueats.models;

import javafx.beans.property.*;

public class User {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty password;
    private final StringProperty userType;
    private final StringProperty phone;
    private final StringProperty studentId;
    private final StringProperty hostel;
    private final StringProperty department;

    public User(int id, String name, String email, String password, String userType) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
        this.userType = new SimpleStringProperty(userType);
        this.phone = new SimpleStringProperty("");
        this.studentId = new SimpleStringProperty("");
        this.hostel = new SimpleStringProperty("");
        this.department = new SimpleStringProperty("");
    }

    // Full constructor
    public User(int id, String name, String email, String password, String userType,
               String phone, String studentId, String hostel, String department) {
        this(id, name, email, password, userType);
        this.phone.set(phone);
        this.studentId.set(studentId);
        this.hostel.set(hostel);
        this.department.set(department);
    }

    // Getters and Setters
    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }

    public String getName() { return name.get(); }
    public void setName(String value) { name.set(value); }
    public StringProperty nameProperty() { return name; }

    public String getEmail() { return email.get(); }
    public void setEmail(String value) { email.set(value); }
    public StringProperty emailProperty() { return email; }

    public String getPassword() { return password.get(); }
    public void setPassword(String value) { password.set(value); }
    public StringProperty passwordProperty() { return password; }

    public String getUserType() { return userType.get(); }
    public void setUserType(String value) { userType.set(value); }
    public StringProperty userTypeProperty() { return userType; }

    public String getPhone() { return phone.get(); }
    public void setPhone(String value) { phone.set(value); }
    public StringProperty phoneProperty() { return phone; }

    public String getStudentId() { return studentId.get(); }
    public void setStudentId(String value) { studentId.set(value); }
    public StringProperty studentIdProperty() { return studentId; }

    public String getHostel() { return hostel.get(); }
    public void setHostel(String value) { hostel.set(value); }
    public StringProperty hostelProperty() { return hostel; }

    public String getDepartment() { return department.get(); }
    public void setDepartment(String value) { department.set(value); }
    public StringProperty departmentProperty() { return department; }
}
