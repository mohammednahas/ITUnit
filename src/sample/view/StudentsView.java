package sample.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import sample.model.Student;

import java.util.List;
import java.util.stream.Collectors;

public class StudentsView {

    private final List<Student> students;

    public StudentsView(List<Student> students) {
        this.students = students;
    }

    public VBox createView() {

        VBox container = new VBox(20);
        container.setPadding(new Insets(30));
        container.setStyle("-fx-background-color: #f6f8fc;");

        // =========================
        // Header
        // =========================

        Label title = new Label("Students");
        title.setStyle(
                "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #172033;"
        );

        Label subtitle = new Label(
                "Search and manage student university credentials"
        );

        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #7b8497;"
        );

        VBox header = new VBox(5, title, subtitle);

        // =========================
        // Search
        // =========================

        TextField searchField = new TextField();
        searchField.setPromptText("Search by name, SSN or university email...");
        searchField.setPrefHeight(42);

        Button searchButton = new Button("Search");
        searchButton.setPrefHeight(42);
        searchButton.setStyle(
                "-fx-background-color: #2563eb;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 0 25px;"
        );

        HBox searchBox = new HBox(10, searchField, searchButton);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        // =========================
        // Table
        // =========================

        TableView<Student> table = new TableView<>();

        TableColumn<Student, String> nameColumn =
                new TableColumn<>("Student Name");

        nameColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getName())
        );

        TableColumn<Student, String> ssnColumn =
                new TableColumn<>("SSN");

        ssnColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getSsn())
        );

        TableColumn<Student, String> emailColumn =
                new TableColumn<>("University Email");

        emailColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getMail())
        );

        table.getColumns().addAll(
                nameColumn,
                ssnColumn,
                emailColumn
        );

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        table.setPlaceholder(
                new Label("No students found")
        );

        table.setPrefHeight(500);

        // Load all students initially
        table.getItems().setAll(students);

        // =========================
        // Search Action
        // =========================

        Runnable performSearch = () -> {

            String query = searchField.getText()
                    .trim()
                    .toLowerCase();

            if (query.isBlank()) {
                table.getItems().setAll(students);
                return;
            }

            List<Student> filtered = students.stream()
                    .filter(student ->
                            student.getName()
                                    .toLowerCase()
                                    .contains(query)
                                    ||
                                    student.getSsn()
                                            .toLowerCase()
                                            .contains(query)
                                    ||
                                    student.getMail()
                                            .toLowerCase()
                                            .contains(query)
                    )
                    .collect(Collectors.toList());

            table.getItems().setAll(filtered);
        };

        searchButton.setOnAction(event -> performSearch.run());

        searchField.setOnAction(event -> performSearch.run());

        // =========================
        // Student Details
        // =========================

        table.setOnMouseClicked(event -> {

            if (event.getClickCount() == 2) {

                Student selected =
                        table.getSelectionModel().getSelectedItem();

                if (selected != null) {
                    showStudentDetails(selected);
                }
            }
        });

        // =========================
        // Bottom info
        // =========================

        Label countLabel = new Label(
                "Total Students: " + students.size()
        );

        countLabel.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #7b8497;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox bottomBar = new HBox(
                countLabel,
                spacer
        );

        bottomBar.setAlignment(Pos.CENTER_LEFT);

        // =========================
        // Assemble
        // =========================

        container.getChildren().addAll(
                header,
                searchBox,
                table,
                bottomBar
        );

        VBox.setVgrow(table, Priority.ALWAYS);

        return container;
    }

    // =========================================================
    // Student Details Window
    // =========================================================

    private void showStudentDetails(Student student) {

        Stage stage = new Stage();

        stage.setTitle("Student Details");

        VBox root = new VBox(18);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #ffffff;");

        Label title = new Label("Student Details");

        title.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #172033;"
        );

        VBox nameBox = createField(
                "Student Name",
                student.getName()
        );

        VBox ssnBox = createField(
                "SSN",
                student.getSsn()
        );

        VBox emailBox = createField(
                "University Email",
                student.getMail()
        );

        VBox passwordBox = createField(
                "Password",
                student.getPassword()
        );

        Button closeButton = new Button("Close");

        closeButton.setPrefHeight(40);

        closeButton.setStyle(
                "-fx-background-color: #eef2f7;" +
                        "-fx-text-fill: #172033;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 0 25px;"
        );

        closeButton.setOnAction(event -> stage.close());

        root.getChildren().addAll(
                title,
                nameBox,
                ssnBox,
                emailBox,
                passwordBox,
                closeButton
        );

        Scene scene = new Scene(root, 550, 500);

        stage.setScene(scene);
        stage.show();
    }

    private VBox createField(String labelText, String value) {

        Label label = new Label(labelText);

        label.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #7b8497;"
        );

        Label valueLabel = new Label(
                value == null || value.isBlank()
                        ? "-"
                        : value
        );

        valueLabel.setWrapText(true);

        valueLabel.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #172033;" +
                        "-fx-background-color: #f6f8fc;" +
                        "-fx-padding: 12px;" +
                        "-fx-background-radius: 8px;"
        );

        VBox box = new VBox(5, label, valueLabel);

        return box;
    }
}