module martin {
    requires javafx.controls;
    requires javafx.fxml;

    opens martin to javafx.fxml;
    exports martin;
}
