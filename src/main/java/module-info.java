module fr.weshdev.sae401 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires javafx.media;
    // requires java.datatransfer;
    requires java.desktop;

    opens fr.weshdev.sae401 to javafx.fxml;
    opens fr.weshdev.sae401.teacher.controllers to javafx.fxml;
    exports fr.weshdev.sae401;
}