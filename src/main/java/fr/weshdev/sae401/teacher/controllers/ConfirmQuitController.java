package fr.weshdev.sae401.teacher.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ConfirmQuitController {

	@FXML private Button recupScene;
	
	@FXML
	public void annuler() {
		recupScene.getScene().getWindow().hide();
	}
	
	@FXML
	public void quitter() {
		Platform.exit();
	}

}
