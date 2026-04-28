module com.example.personaltrainerapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.personaltrainerapp to javafx.fxml;
    exports com.example.personaltrainerapp;
}