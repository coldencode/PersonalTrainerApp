package com.example.personaltrainerapp.ui.dashboard;

import com.example.personaltrainerapp.model.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class DashboardController {

    @FXML
    private Label totalClientsLabel;

    @FXML
    private ListView<String> clientListView;

    private final DashboardViewModel vm =
            new DashboardViewModel();

    @FXML
    public void initialize() {

        totalClientsLabel.textProperty().bind(
                vm.totalClientsProperty().asString()
        );

        for (Client client : vm.getClients()) {
            clientListView.getItems().add(
                    client.getName()
            );
        }
    }
}
