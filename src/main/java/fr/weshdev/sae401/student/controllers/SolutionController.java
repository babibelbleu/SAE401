package fr.weshdev.sae401.student.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class SolutionController implements Initializable {

	// Solution
	@FXML
	private TextFlow solutionContainer;
	@FXML
	private Button exitPopUpButton;

	ExerciseController exerciseController = MenuController.getController();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		String[] encryptedText = exerciseController.getEncryptedText().split("[ \\t\\n\\x0B\\f\\r]");
		String[] clearText = exerciseController.getClearText().split("[ \\t\\n\\x0B\\f\\r]");
		int length = 0;
		for (int i = 0; i < clearText.length; i++) {
			Text t = new Text(clearText[i]);
			if (clearText[i].equals(encryptedText[i])) {
				t.setFill(Color.GREEN);
			}else {
				t.setFill(Color.RED);
			}
			solutionContainer.getChildren().add(t);
			if (length + clearText[i].length() < exerciseController.getClearText().length()) {
				length += clearText[i].length();
			}
			if (Character.isWhitespace(exerciseController.getClearText().charAt(length)) || Character.isSpaceChar(exerciseController.getClearText().charAt(length))) {
				solutionContainer.getChildren().add(new Text(""+ exerciseController.getClearText().charAt(length)));
			}
			length++;
		}

	}

	@FXML
	public void hideSolutionPopUp() {
		exitPopUpButton.getScene().getWindow().hide();
	}

}
