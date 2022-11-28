module dylan.gresham {
    requires javafx.controls;
    requires javafx.fxml;

    opens dylan.gresham to javafx.fxml;
    exports dylan.gresham;
}
