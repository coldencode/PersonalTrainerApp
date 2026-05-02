module com.example.personaltrainerapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.net.http;
    requires org.json;


    opens com.example.personaltrainerapp to javafx.fxml;
    opens com.example.personaltrainerapp.ui.dashboard to javafx.fxml;
    opens com.example.personaltrainerapp.ui.onboarding to javafx.fxml;
    opens com.example.personaltrainerapp.ui.workout to javafx.fxml;
    opens com.example.personaltrainerapp.ui.pushup to javafx.fxml;
    opens com.example.personaltrainerapp.ui.fruits to javafx.fxml;
    exports com.example.personaltrainerapp;
}