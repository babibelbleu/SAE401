package fr.weshdev.sae401.teacher.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class AboutController {
    @FXML
    private Label recupScene;
    @FXML
    public void retourMenu(ActionEvent event) throws IOException {
        recupScene.getScene().getWindow().hide();
    }
}
