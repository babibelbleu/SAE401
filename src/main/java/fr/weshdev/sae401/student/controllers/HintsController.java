package fr.weshdev.sae401.student.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class HintsController implements Initializable {
	@FXML
	private Button closeButton;
	@FXML
	private TextArea hintDisplay;
	public static String contenuAide;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		hintDisplay.setWrapText(true);
		hintDisplay.setText(contenuAide);
		
	}

	@FXML
	public void closeAide() {
		closeButton.getScene().getWindow().hide();
	}

}
