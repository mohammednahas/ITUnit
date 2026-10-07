package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.model.Student;
import sample.Service.ExcelFileService;
import sample.Service.ExcelService;
import sample.view.StudentsView;

import java.io.File;
import java.util.List;

public class Main extends Application {

    // =========================
    // Services
    // =========================

    private final ExcelService excelService = new ExcelService();

    private final ExcelFileService excelFileService =
            new ExcelFileService();

    // Students loaded from Excel
    private List<Student> students = List.of();

    // Dashboard labels
    private Label fileNameLabel;
    private Label studentCountLabel;

    // Main root
    private BorderPane root;

    @Override
    public void start(Stage stage) {

        // =========================
        // Sidebar
        // =========================

        Label logo = new Label("IT");
        logo.getStyleClass().add("logo");

        Label brand = new Label("Student IT");
        brand.getStyleClass().add("brand");

        VBox brandBox = new VBox(2, logo, brand);
        brandBox.setAlignment(Pos.CENTER);

        Button dashboardButton =
                createNavButton("Dashboard", true);

        Button studentsButton =
                createNavButton("Students", false);

        Region sidebarSpacer = new Region();

        VBox.setVgrow(
                sidebarSpacer,
                Priority.ALWAYS
        );

        Label version = new Label("v1.0.0");

        version.getStyleClass().add(
                "version"
        );

        VBox sidebar = new VBox(
                12,
                brandBox,
                new Separator(),
                dashboardButton,
                studentsButton,
                sidebarSpacer,
                version
        );

        sidebar.setPadding(
                new Insets(25, 15, 20, 15)
        );

        sidebar.setPrefWidth(220);

        sidebar.getStyleClass().add(
                "sidebar"
        );

        // =========================
        // Dashboard Content
        // =========================

        VBox content =
                createDashboard(stage);

        // =========================
        // Root
        // =========================

        root = new BorderPane();

        root.setLeft(sidebar);

        root.setCenter(content);

        root.getStyleClass().add(
                "root"
        );

        // =========================
        // Navigation
        // =========================

        // Dashboard
        dashboardButton.setOnAction(event -> {

            setActiveButton(
                    dashboardButton,
                    studentsButton
            );

            root.setCenter(
                    createDashboard(stage)
            );
        });

        // Students
        studentsButton.setOnAction(event -> {

            setActiveButton(
                    studentsButton,
                    dashboardButton
            );

            StudentsView studentsView =
                    new StudentsView(students);

            root.setCenter(
                    studentsView.createView()
            );
        });

        // =========================
        // Scene
        // =========================

        Scene scene =
                new Scene(
                        root,
                        1200,
                        750
                );

        scene.getStylesheets().add(
                getClass()
                        .getResource("/style.css")
                        .toExternalForm()
        );

        stage.setTitle(
                "Student IT Center"
        );

        stage.setMinWidth(1000);

        stage.setMinHeight(650);

        stage.setScene(scene);

        stage.show();

        // =========================
        // Load Last Excel File
        // =========================

        loadLastExcelFile();
    }

    // ============================================================
    // Dashboard
    // ============================================================

    private VBox createDashboard(Stage stage) {

        // =========================
        // Top Bar
        // =========================

        Label pageTitle =
                new Label(
                        "Student Credential Management"
                );

        pageTitle.getStyleClass().add(
                "page-title"
        );

        Label subtitle =
                new Label(
                        "Manage student accounts and IT credentials"
                );

        subtitle.getStyleClass().add(
                "subtitle"
        );

        VBox titleBox =
                new VBox(
                        4,
                        pageTitle,
                        subtitle
                );

        Region topSpacer =
                new Region();

        HBox.setHgrow(
                topSpacer,
                Priority.ALWAYS
        );

        Label statusDot =
                new Label("●");

        statusDot.getStyleClass().add(
                "online-dot"
        );

        Label status =
                new Label("System Online");

        status.getStyleClass().add(
                "system-status"
        );

        HBox statusBox =
                new HBox(
                        8,
                        statusDot,
                        status
                );

        statusBox.setAlignment(
                Pos.CENTER
        );

        HBox topBar =
                new HBox(
                        titleBox,
                        topSpacer,
                        statusBox
                );

        topBar.setAlignment(
                Pos.CENTER_LEFT
        );

        topBar.setPadding(
                new Insets(
                        25,
                        30,
                        20,
                        30
                )
        );

        // =========================
        // Search
        // =========================

        Label searchLabel =
                new Label("Find Student");

        searchLabel.getStyleClass().add(
                "section-title"
        );

        TextField searchField =
                new TextField();

        searchField.setPromptText(
                "Search by name, SSN or university email..."
        );

        searchField.getStyleClass().add(
                "search-field"
        );

        Button searchButton =
                new Button("SEARCH");

        searchButton.getStyleClass().add(
                "primary-button"
        );

        searchButton.setOnAction(event ->
                searchStudent(
                        searchField.getText()
                )
        );

        searchField.setOnAction(event ->
                searchStudent(
                        searchField.getText()
                )
        );

        HBox searchBox =
                new HBox(
                        12,
                        searchField,
                        searchButton
                );

        HBox.setHgrow(
                searchField,
                Priority.ALWAYS
        );

        VBox searchCard =
                new VBox(
                        12,
                        searchLabel,
                        searchBox
                );

        searchCard.getStyleClass().add(
                "card"
        );

        searchCard.setPadding(
                new Insets(22)
        );

        // =========================
        // Current Excel File
        // =========================

        Label fileTitle =
                new Label("Current Excel File");

        fileTitle.getStyleClass().add(
                "card-title"
        );

        fileNameLabel =
                new Label(
                        "No Excel file selected"
                );

        fileNameLabel.getStyleClass().add(
                "file-name"
        );

        Button changeFileButton =
                new Button("CHANGE FILE");

        changeFileButton.getStyleClass().add(
                "secondary-button"
        );

        changeFileButton.setOnAction(event ->
                chooseExcelFile(stage)
        );

        Region fileSpacer =
                new Region();

        HBox.setHgrow(
                fileSpacer,
                Priority.ALWAYS
        );

        HBox fileRow =
                new HBox(
                        15,
                        fileNameLabel,
                        fileSpacer,
                        changeFileButton
                );

        fileRow.setAlignment(
                Pos.CENTER_LEFT
        );

        VBox fileCard =
                new VBox(
                        10,
                        fileTitle,
                        fileRow
                );

        fileCard.getStyleClass().add(
                "card"
        );

        fileCard.setPadding(
                new Insets(22)
        );

        // =========================
        // Statistics
        // =========================

        VBox studentsCard =
                createStatCard(
                        "0",
                        "TOTAL STUDENTS"
                );

        studentCountLabel =
                (Label) studentsCard
                        .getChildren()
                        .get(0);

        VBox accountsCard =
                createStatCard(
                        "0",
                        "ACTIVE ACCOUNTS"
                );

        VBox qrCard =
                createStatCard(
                        "0",
                        "QR CODES"
                );

        HBox statistics =
                new HBox(
                        15,
                        studentsCard,
                        accountsCard,
                        qrCard
                );

        HBox.setHgrow(
                studentsCard,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                accountsCard,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                qrCard,
                Priority.ALWAYS
        );

        // =========================
        // Main Content
        // =========================

        VBox content =
                new VBox(
                        20,
                        topBar,
                        searchCard,
                        fileCard,
                        statistics
                );

        content.setPadding(
                new Insets(
                        0,
                        30,
                        30,
                        30
                )
        );

        VBox.setVgrow(
                content,
                Priority.ALWAYS
        );

        return content;
    }

    // ============================================================
    // Search Student
    // ============================================================

    private void searchStudent(
            String query
    ) {

        query = query.trim();

        if (query.isEmpty()) {

            showInformation(
                    "Search Student",
                    "Please enter a name, SSN or university email."
            );

            return;
        }

        if (students.isEmpty()) {

            showInformation(
                    "No Excel File",
                    "Please select an Excel file first."
            );

            return;
        }

        String searchValue =
                query.toLowerCase();

        List<Student> results =
                students.stream()
                        .filter(student ->
                                contains(
                                        student.getName(),
                                        searchValue
                                )
                                        ||
                                        contains(
                                                student.getSsn(),
                                                searchValue
                                        )
                                        ||
                                        contains(
                                                student.getMail(),
                                                searchValue
                                        )
                        )
                        .collect(
                                java.util.stream.Collectors.toList()
                        );

        if (results.isEmpty()) {

            showInformation(
                    "Student Not Found",
                    "No student matches:\n\n"
                            + query
            );

            return;
        }

        if (results.size() == 1) {

            showStudentDetails(
                    results.get(0)
            );

            return;
        }

        showMultipleResults(
                results
        );
    }

    // ============================================================
    // Contains Helper
    // ============================================================

    private boolean contains(
            String value,
            String query
    ) {

        if (value == null) {
            return false;
        }

        return value
                .toLowerCase()
                .contains(query);
    }

    // ============================================================
    // Multiple Search Results
    // ============================================================

    private void showMultipleResults(
            List<Student> results
    ) {

        Stage resultsStage =
                new Stage();

        resultsStage.setTitle(
                "Student Search Results"
        );

        Label title =
                new Label(
                        "Search Results"
                );

        title.getStyleClass().add(
                "page-title"
        );

        Label subtitle =
                new Label(
                        results.size()
                                + " students found"
                );

        subtitle.getStyleClass().add(
                "subtitle"
        );

        VBox header =
                new VBox(
                        5,
                        title,
                        subtitle
                );

        // =========================
        // Table
        // =========================

        TableView<Student> table =
                new TableView<>();

        table.setItems(
                FXCollections.observableArrayList(
                        results
                )
        );

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        TableColumn<Student, String> nameColumn =
                new TableColumn<>(
                        "Name"
                );

        nameColumn.setCellValueFactory(
                cellData ->
                        new javafx.beans.property.SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getName()
                        )
        );

        TableColumn<Student, String> ssnColumn =
                new TableColumn<>(
                        "SSN"
                );

        ssnColumn.setCellValueFactory(
                cellData ->
                        new javafx.beans.property.SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getSsn()
                        )
        );

        TableColumn<Student, String> emailColumn =
                new TableColumn<>(
                        "University Email"
                );

        emailColumn.setCellValueFactory(
                cellData ->
                        new javafx.beans.property.SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getMail()
                        )
        );

        table.getColumns().addAll(
                nameColumn,
                ssnColumn,
                emailColumn
        );

        // Double click -> details
        table.setOnMouseClicked(event -> {

            if (event.getClickCount() == 2) {

                Student selected =
                        table.getSelectionModel()
                                .getSelectedItem();

                if (selected != null) {

                    showStudentDetails(
                            selected
                    );
                }
            }
        });

        // =========================
        // Close
        // =========================

        Button closeButton =
                new Button(
                        "CLOSE"
                );

        closeButton.getStyleClass().add(
                "secondary-button"
        );

        closeButton.setOnAction(
                event ->
                        resultsStage.close()
        );

        HBox bottom =
                new HBox(
                        closeButton
                );

        bottom.setAlignment(
                Pos.CENTER_RIGHT
        );

        VBox root =
                new VBox(
                        20,
                        header,
                        table,
                        bottom
                );

        root.setPadding(
                new Insets(25)
        );

        VBox.setVgrow(
                table,
                Priority.ALWAYS
        );

        root.getStyleClass().add(
                "root"
        );

        Scene scene =
                new Scene(
                        root,
                        900,
                        600
                );

        scene.getStylesheets().add(
                getClass()
                        .getResource("/style.css")
                        .toExternalForm()
        );

        resultsStage.setScene(
                scene
        );

        resultsStage.setMinWidth(700);

        resultsStage.setMinHeight(450);

        resultsStage.show();
    }

    // ============================================================
    // Student Details
    // ============================================================

    private void showStudentDetails(
            Student student
    ) {

        Stage detailsStage =
                new Stage();

        detailsStage.setTitle(
                "Student Details"
        );

        Label title =
                new Label(
                        "Student Details"
                );

        title.getStyleClass().add(
                "page-title"
        );

        Label name =
                new Label(
                        "Name\n"
                                + student.getName()
                );

        name.getStyleClass().add(
                "card-title"
        );

        Label ssn =
                new Label(
                        "SSN\n"
                                + student.getSsn()
                );

        ssn.getStyleClass().add(
                "card-title"
        );

        Label email =
                new Label(
                        "University Email\n"
                                + student.getMail()
                );

        email.getStyleClass().add(
                "card-title"
        );

        Label password =
                new Label(
                        "Password\n"
                                + student.getPassword()
                );

        password.getStyleClass().add(
                "card-title"
        );

        VBox details =
                new VBox(
                        18,
                        title,
                        name,
                        ssn,
                        email,
                        password
                );

        details.setPadding(
                new Insets(30)
        );

        details.getStyleClass().add(
                "root"
        );

        Button closeButton =
                new Button(
                        "CLOSE"
                );

        closeButton.getStyleClass().add(
                "secondary-button"
        );

        closeButton.setOnAction(
                event ->
                        detailsStage.close()
        );

        HBox buttonBox =
                new HBox(
                        closeButton
                );

        buttonBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        details.getChildren().add(
                buttonBox
        );

        Scene scene =
                new Scene(
                        details,
                        550,
                        500
                );

        scene.getStylesheets().add(
                getClass()
                        .getResource("/style.css")
                        .toExternalForm()
        );

        detailsStage.setScene(
                scene
        );

        detailsStage.setMinWidth(500);

        detailsStage.setMinHeight(450);

        detailsStage.show();
    }

    // ============================================================
    // Choose Excel File
    // ============================================================

    private void chooseExcelFile(
            Stage stage
    ) {

        FileChooser fileChooser =
                new FileChooser();

        fileChooser.setTitle(
                "Select Student Excel File"
        );

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Excel Files",
                        "*.xlsx",
                        "*.xls"
                )
        );

        File selectedFile =
                fileChooser.showOpenDialog(
                        stage
                );

        if (selectedFile == null) {
            return;
        }

        loadExcelFile(
                selectedFile
        );
    }

    // ============================================================
    // Load Excel
    // ============================================================

    private void loadExcelFile(
            File file
    ) {

        try {

            /*
             * Excel structure:
             *
             * Column A -> name
             * Column B -> ssn
             * Column C -> mail
             * Column D -> password
             */

            students =
                    excelService.readStudents(
                            file
                    );

            excelFileService.saveCurrentFile(
                    file
            );

            fileNameLabel.setText(
                    file.getName()
            );

            studentCountLabel.setText(
                    String.valueOf(
                            students.size()
                    )
            );

            showInformation(
                    "Excel Loaded",
                    "The Excel file was loaded successfully.\n\n"
                            + "File: "
                            + file.getName()
                            + "\n"
                            + "Students loaded: "
                            + students.size()
            );

        } catch (Exception e) {

            showError(
                    "Could not load Excel file",
                    e.getMessage()
            );
        }
    }

    // ============================================================
    // Load Previously Selected File
    // ============================================================

    private void loadLastExcelFile() {

        File lastFile =
                excelFileService.getLastFile();

        if (lastFile == null) {
            return;
        }

        try {

            students =
                    excelService.readStudents(
                            lastFile
                    );

            fileNameLabel.setText(
                    lastFile.getName()
            );

            studentCountLabel.setText(
                    String.valueOf(
                            students.size()
                    )
            );

        } catch (Exception e) {

            fileNameLabel.setText(
                    "Previous file could not be loaded"
            );

            studentCountLabel.setText(
                    "0"
            );

            students = List.of();
        }
    }

    // ============================================================
    // Active Navigation Button
    // ============================================================

    private void setActiveButton(
            Button active,
            Button... others
    ) {

        for (Button button : others) {

            button.getStyleClass().remove(
                    "nav-button-active"
            );
        }

        if (!active.getStyleClass().contains(
                "nav-button-active"
        )) {

            active.getStyleClass().add(
                    "nav-button-active"
            );
        }
    }

    // ============================================================
    // Information Alert
    // ============================================================

    private void showInformation(
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }

    // ============================================================
    // Error Alert
    // ============================================================

    private void showError(
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(title);

        alert.setHeaderText(title);

        alert.setContentText(
                message == null
                        ? "Unknown error."
                        : message
        );

        alert.showAndWait();
    }

    // ============================================================
    // Navigation Button
    // ============================================================

    private Button createNavButton(
            String text,
            boolean active
    ) {

        Button button =
                new Button(text);

        button.setMaxWidth(
                Double.MAX_VALUE
        );

        button.getStyleClass().add(
                "nav-button"
        );

        if (active) {

            button.getStyleClass().add(
                    "nav-button-active"
            );
        }

        return button;
    }

    // ============================================================
    // Statistics Card
    // ============================================================

    private VBox createStatCard(
            String number,
            String title
    ) {

        Label numberLabel =
                new Label(number);

        numberLabel.getStyleClass().add(
                "stat-number"
        );

        Label titleLabel =
                new Label(title);

        titleLabel.getStyleClass().add(
                "stat-title"
        );

        VBox card =
                new VBox(
                        8,
                        numberLabel,
                        titleLabel
                );

        card.getStyleClass().add(
                "stat-card"
        );

        card.setPadding(
                new Insets(22)
        );

        return card;
    }

    // ============================================================
    // Main
    // ============================================================

    public static void main(
            String[] args
    ) {

        launch();
    }
}