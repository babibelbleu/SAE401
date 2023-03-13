module fr.weshdev.sae401 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens fr.weshdev.sae401 to javafx.fxml;
    exports fr.weshdev.sae401;
}