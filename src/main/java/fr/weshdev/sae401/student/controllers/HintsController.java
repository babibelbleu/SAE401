package fr.weshdev.sae401.student.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class HintsController implements Initializable {

	// Aide
	@FXML
	private Button close;
	@FXML
	private TextArea affichageAide;
	public static String contenuAide;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		affichageAide.setWrapText(true);
		affichageAide.setText(contenuAide);
		
	}

	@FXML
	public void closeAide() {
		close.getScene().getWindow().hide();
	}

}
