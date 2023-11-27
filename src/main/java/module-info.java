module martin {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.martin to javafx.fxml;

    exports com.martin;
}
