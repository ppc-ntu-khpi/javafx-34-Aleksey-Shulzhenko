package org.example.demofx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

// Вказані завдання:
// Читає інформацію про клієнтів та показує після вибору у ComboBox
// В кнопці Report показує звіт по ВСІХ клієнтах

public class HelloApplication extends Application {

    // Модель данних
    static class Customer {
        private String firstName;
        private String lastName;
        private Account[] accounts;

        Customer(String firstName, String lastName, Account[] accounts) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.accounts = accounts;
        }

        String getFullName(){
            return firstName + " " + lastName;
        }

        Account[] getAccounts(){
            return accounts;
        }
    }

    static abstract class Account {
        protected double balance;

        Account(double balance) {
            this.balance = balance;
        }

        abstract String getType();

        double getBalance(){
            return balance;
        }
    }

    static class Savings extends Account {
        private double interestRate;

        Savings(double balance, double interestRate) {
            super(balance);
            this.interestRate = interestRate;
        }

        @Override
        String getType(){
            return "Savings (%" + (interestRate*100) + ")";
        }
    }

    static class Checking extends Account {
        private double overdraft;

        Checking(double balance, double overdraft) {
            super(balance);
            this.overdraft = overdraft;
        }

        @Override
        String getType(){
            return "Checking (overdraft " + overdraft + ")";
        }
    }

    private Label details = new Label();
    private Label report = new Label();

    private ComboBox<String> clients = new ComboBox<>();
    private Customer[] customers;

    @Override
    public void start(Stage primaryStage) {

        // Вказані дані про клієнтів та рахунки
        customers = new Customer[] {
                new Customer("Jane","Simms", new Account[] {
                        new Savings(500.0, 0.05),
                        new Checking(200.0, 400.0)
                }),
                new Customer("Owen","Bryant", new Account[] {
                        new Checking(200.0, 0.0)
                }),
                new Customer("Tim","Soley", new Account[] {
                        new Savings(1500.0, 0.05),
                        new Checking(200.0, 0.0)
                }),
                new Customer("Maria","Soley", new Account[] {
                        new Savings(150.0, 0.05)
                })
        };


        // Заповнення ComboBox
        ObservableList<String> items = FXCollections.observableArrayList();

        for (Customer customer : customers) {
            items.add(customer.getFullName());
        }

        clients.setItems(items);
        clients.setPromptText("Виберіть клієнта");

        Button showBtn = new Button("Show");

        showBtn.setOnAction((ActionEvent event) -> {
            int idx = clients.getSelectionModel().getSelectedIndex();

            if (idx == -1) {
                showAlert("Не вибрано клієнта.");
                return;
            }

            Customer customer = customers[idx];
            StringBuilder info = new StringBuilder();

            info.append("Клієнт: ").append(customer.getFullName()).append('\n');
            for (Account account : customer.getAccounts()) {
                info.append(account.getType()).append(", баланс = ").append(account.getBalance()).append('\n');
            }

            details.setText(info.toString());
        });

        Button reportBtn = new Button("Report");

        reportBtn.setOnAction((ActionEvent event) -> {
            StringBuilder info = new StringBuilder();

            info.append("Звіт по клієнтах:\n");

            for (Customer customer : customers) {
                info.append("________________________________\n");

                info.append("Клієнт: ").append(customer.getFullName()).append('\n');
                for (Account account : customer.getAccounts()) {
                    info.append(account.getType()).append(", баланс = ").append(account.getBalance()).append('\n');
                }
            }
            report.setText(info.toString());
        });


        // Вказуємо стилі для UI
        details.setStyle("-fx-font-size: 16px; -fx-padding: 20px; -fx-background-color: #edf5ff; -fx-border-radius: 10px; -fx-background-radius: 10px;");
        report.setStyle("-fx-font-size: 16px; -fx-padding: 20px; -fx-background-color: #fff5cc; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        clients.setStyle("-fx-font-size: 16px;");
        showBtn.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-color: #4caf50; -fx-text-fill: #ffffff; -fx-background-radius: 20px;");
        reportBtn.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-color: #ff9800; -fx-text-fill: #ffffff; -fx-background-radius: 20px;");

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));

        root.getChildren().addAll(
                new Label("Банк — інформація про клієнтів"),
                clients,
                showBtn,
                reportBtn,
                details,
                report
        );

        root.setStyle("-fx-background-color: #dff0ff;");
        root.setAlignment(Pos.TOP_CENTER);

        primaryStage.setScene(new Scene(root, 500, 700));

        primaryStage.setTitle("Банк");

        primaryStage.show();

    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
