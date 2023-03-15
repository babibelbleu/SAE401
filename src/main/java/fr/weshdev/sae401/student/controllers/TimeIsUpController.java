package fr.weshdev.sae401.student.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class TimeIsUpController {

	@FXML private Label recupScene;
	
	@FXML private Button recupScene1;

	@FXML
	public void retourAccueil()  {
		recupScene.getScene().getWindow().hide();
	}
	
	@FXML
	public void retourAccueil1()  {
		recupScene1.getScene().getWindow().hide();
	}

}
