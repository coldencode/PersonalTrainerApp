module com.example.personaltrainerapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.personaltrainerapp to javafx.fxml;
    exports com.example.personaltrainerapp;
}