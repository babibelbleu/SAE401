package fr.weshdev.sae401.student.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SaveValidationController {
    @FXML private Button okButton;
    @FXML
    public void backToHomePageExerciceDone()  {
        okButton.getScene().getWindow().hide();
    }

}
