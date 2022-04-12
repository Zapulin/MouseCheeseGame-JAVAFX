module com.example.mousegamefx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.mousegamefx to javafx.fxml;
    exports com.example.mousegamefx;
    exports com.example.mousegamefx.controller;
    opens com.example.mousegamefx.controller to javafx.fxml;
}