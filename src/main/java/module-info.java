module dylan.gresham {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.fxmisc.richtext;
    requires transitive javafx.graphics;

    opens dylan.gresham to javafx.fxml;
    exports dylan.gresham;
}
