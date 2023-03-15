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
	private TextFlow solution;
	@FXML
	private Button closeSol;

	ExerciseController c = MenuController.getC();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		String[] encrypted = c.getEncryptedText().split("[ \\t\\n\\x0B\\f\\r]");
		String[] clear = c.getClearText().split("[ \\t\\n\\x0B\\f\\r]");
		int length = 0;
		for (int i = 0; i < clear.length; i++) {
			Text t = new Text(clear[i]);
			if (clear[i].equals(encrypted[i])) {
				t.setFill(Color.GREEN);
			}else {
				t.setFill(Color.RED);
			}
			solution.getChildren().add(t);
			if (length + clear[i].length() < c.getClearText().length()) {
				length += clear[i].length();
			}
			if (Character.isWhitespace(c.getClearText().charAt(length)) || Character.isSpaceChar(c.getClearText().charAt(length))) {
				solution.getChildren().add(new Text(""+c.getClearText().charAt(length)));
			}
			length++;
		}

	}

	@FXML
	public void closeSolution() {
		closeSol.getScene().getWindow().hide();
	}

}
