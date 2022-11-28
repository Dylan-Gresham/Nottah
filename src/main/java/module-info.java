module dylan.gresham {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens dylan.gresham to javafx.fxml;
    exports dylan.gresham;
}
