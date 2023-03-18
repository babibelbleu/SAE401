package fr.weshdev.sae401.student.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class TimeIsUpController {

	@FXML private Label tempEcoulePopUp;


	@FXML
	public void backToHomePageTimeIsUp()  {
		tempEcoulePopUp.getScene().getWindow().hide();
	}

}
