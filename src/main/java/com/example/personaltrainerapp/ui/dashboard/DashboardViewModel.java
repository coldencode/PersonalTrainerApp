package com.example.personaltrainerapp.ui.dashboard;

import com.example.personaltrainerapp.model.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;

public class DashboardViewModel {

    private final ObservableList<Client> clients =
            FXCollections.observableArrayList();

    private final IntegerProperty totalClients =
            new SimpleIntegerProperty();

    public DashboardViewModel() {

        loadMockData();
    }

    private void loadMockData() {

        clients.addAll(
                new Client("Sarah", 24),
                new Client("Michael", 31),
                new Client("Emma", 27)
        );

        totalClients.set(clients.size());
    }

    public ObservableList<Client> getClients() {
        return clients;
    }

    public IntegerProperty totalClientsProperty() {
        return totalClients;
    }
}