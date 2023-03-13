package fr.weshdev.sae401.student.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class TimeIsUpController {

	@FXML private Label recupScene;
	
	@FXML private Button recupScene1;

	@FXML
	public void retourAccueil() throws IOException {
		recupScene.getScene().getWindow().hide();
	}
	
	@FXML
	public void retourAccueil1() throws IOException {
		recupScene1.getScene().getWindow().hide();
	}

}
