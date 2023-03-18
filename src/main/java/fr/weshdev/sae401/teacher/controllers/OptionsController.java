package fr.weshdev.sae401.teacher.controllers;

import fr.weshdev.sae401.DeplacementFenetre;
import fr.weshdev.sae401.MainEnseignant;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class OptionsController implements Initializable {

	@FXML
	private RadioButton trainingModeRadioButtonOption;
	@FXML
	private RadioButton assessmentModeRadioButtonOption;
	@FXML
	private RadioButton twoLettersRadioButtonOption;
	@FXML
	private RadioButton threeLettersRadioButtonOption;
	@FXML
	private TextField hiddenCharOption;
	@FXML
	private TextField timerOption;
	@FXML
	private CheckBox incompleteWordCheckBoxOption;
	@FXML
	private CheckBox solutionCheckBoxOption;
	@FXML
	private CheckBox discoveredWordsCheckBoxOption;
	@FXML
	private CheckBox caseSensitiveCheckBoxOption;
	@FXML
	private Button saveButton;

	public static String hiddenChar;
	public static String timer;
	public static boolean isCaseSensitive;
	public static boolean isInTrainingMode;
	public static boolean isInAssessmentMode;
	public static boolean hasSolution;
	public static boolean hasDiscoveredWordsOption;
	public static boolean hasIncompleteWordOption;
	public static boolean hasTwoLettersOption;
	public static boolean hasThreeLettersOption;

	@FXML private ImageView toolTipOccul;
	@FXML private ImageView toolTipSensi;
	@FXML private ImageView toolTipEntr;
	@FXML private ImageView toolTipEval;
	@FXML private ImageView toolTipNbMin;
	@FXML private ImageView toolTipMotDecouvert;
	@FXML private ImageView toolTipSolution;
	@FXML private ImageView toolTipMotIncomplet;

	@FXML private CheckMenuItem dark;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		if(hiddenChar != null) {
			hiddenCharOption.setText(hiddenChar);
		}

		if(isCaseSensitive) {
			caseSensitiveCheckBoxOption.setSelected(true);
		}

		if(isInTrainingMode) {
			trainingModeRadioButtonOption.setSelected(true);

			timerOption.setDisable(true);

			if(hasSolution) {
				solutionCheckBoxOption.setSelected(true);
			}

			if(hasDiscoveredWordsOption) {
				discoveredWordsCheckBoxOption.setSelected(true);
			}

			if(hasIncompleteWordOption) {
				incompleteWordCheckBoxOption.setSelected(true);

				if(hasTwoLettersOption) {
					twoLettersRadioButtonOption.setSelected(true);
				}

				if(hasThreeLettersOption) {
					threeLettersRadioButtonOption.setSelected(true);
				}

			}
		}

		if(isInAssessmentMode) {
			assessmentModeRadioButtonOption.setSelected(true);

			discoveredWordsCheckBoxOption.setDisable(true);
			incompleteWordCheckBoxOption.setDisable(true);
			solutionCheckBoxOption.setDisable(true);
			twoLettersRadioButtonOption.setDisable(true);
			threeLettersRadioButtonOption.setDisable(true);
			
			
			timerOption.setText(timer);
			timerOption.setDisable(false);
		}

		checkMode();

		if(!hiddenCharOption.getText().isEmpty() && (trainingModeRadioButtonOption.isSelected() || (assessmentModeRadioButtonOption.isSelected()  && !timerOption.getText().isEmpty()))) {
			saveButton.setDisable(false);
		}

	}

	private void checkMode() {

		hiddenCharOption.textProperty().addListener((arg0, arg1, arg2) -> {
			if(!hiddenCharOption.getText().isEmpty() && (trainingModeRadioButtonOption.isSelected() || (assessmentModeRadioButtonOption.isSelected()  && !timerOption.getText().isEmpty()))) {
				saveButton.setDisable(false);
			} else {
				saveButton.setDisable(true);
			}

		});

		trainingModeRadioButtonOption.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
			if (!hiddenCharOption.getText().isEmpty() && (trainingModeRadioButtonOption.isSelected() || (assessmentModeRadioButtonOption.isSelected() && !timerOption.getText().isEmpty()))) {
				saveButton.setDisable(false);
			} else {
				saveButton.setDisable(true);
			}
		});

		assessmentModeRadioButtonOption.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
			if (!hiddenCharOption.getText().isEmpty() && (trainingModeRadioButtonOption.isSelected() || (assessmentModeRadioButtonOption.isSelected() && !timerOption.getText().isEmpty()))) {
				saveButton.setDisable(false);
			} else {
				saveButton.setDisable(true);
			}
		});


		timerOption.textProperty().addListener((arg0, arg1, arg2) -> {

			if(assessmentModeRadioButtonOption.isSelected()) {
				if(!hiddenCharOption.getText().isEmpty() && !timerOption.getText().isEmpty()) {
					saveButton.setDisable(false);
				} else {
					saveButton.setDisable(true);
				}
			}

		});


	}

	@FXML
	public void tuto() throws IOException, URISyntaxException {
		
		InputStream is = MainEnseignant.class.getResourceAsStream("fr.weshdev.sae401/pdf/user_manual.pdf");

		File pdf = File.createTempFile("Manuel Utilisateur", ".pdf");
		pdf.deleteOnExit();
        OutputStream out = new FileOutputStream(pdf);

        byte[] buffer = new byte[4096];
        int bytesRead = 0;

        while (is.available() != 0) {
            bytesRead = is.read(buffer);
            out.write(buffer, 0, bytesRead);
        }
        
        out.close();
        is.close();
        
        Desktop.getDesktop().open(pdf);

	}

	@FXML
	public void quitter() throws IOException {

		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/confirm_quit.fxml"));
		Scene scene = new Scene(root, 400, 200);
		//On bloque sur cette fen�tre
		primaryStage.initModality(Modality.APPLICATION_MODAL);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		scene.setFill(Color.TRANSPARENT);

		//Bordure
		Rectangle rect = new Rectangle(400,200); 
		rect.setArcHeight(20.0); 
		rect.setArcWidth(20.0);  
		root.setClip(rect);

		DeplacementFenetre.deplacementFenetre((Pane) root, primaryStage);
		primaryStage.setScene(scene);
		darkModeActivation(scene);
		primaryStage.show();
	}

	@FXML
	public void pageApercu() throws IOException {

		if(!hiddenCharOption.getText().isEmpty() && hiddenCharOption.getText() != "") {
			hiddenChar = hiddenCharOption.getText();
		}

		if(!timerOption.getText().isEmpty() && timerOption.getText() != "") {
			timer = timerOption.getText();
		}

		Stage primaryStage = (Stage) timerOption.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/apercu.fxml"));
		Scene scene = new Scene(root, MainEnseignant.width, MainEnseignant.height - 60);
		primaryStage.setScene(scene);
		darkModeActivation(scene);
	}

	public void retourMenu() throws IOException {
		Stage stage = (Stage) hiddenCharOption.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/menu.fxml"));
		Scene scene = new Scene(root,  MainEnseignant.width, MainEnseignant.height - 60);
		stage.setScene(scene);
		darkModeActivation(scene);
		stage.show();
	}

	@FXML
	public void pageEnregistrementFinal() throws IOException {

		hiddenChar = hiddenCharOption.getText();
		timer = timerOption.getText();

		retourMenu();

		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/register_validation.fxml"));
		Scene scene = new Scene(root, 320, 150);
		DeplacementFenetre.deplacementFenetre((Pane) root, primaryStage);
		//On bloque sur cette fen�tre
		primaryStage.initModality(Modality.APPLICATION_MODAL);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setScene(scene);
		darkModeActivation(scene);
		primaryStage.show();
	}

	@FXML
	public void pageNouvelExo() throws IOException {
		//R�initialisation des variables
		AccueilController c = new AccueilController();
		c.delete();
		Stage primaryStage = (Stage) hiddenCharOption.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/new_exercise.fxml"));
		Scene scene = new Scene(root, MainEnseignant.width, MainEnseignant.height - 60);
		primaryStage.setScene(scene);
		darkModeActivation(scene);
		primaryStage.show();
	}

	@FXML
	public void selectionModeEvaluation() {
		timerOption.setDisable(false);
		isInAssessmentMode = true;
		incompleteWordCheckBoxOption.setSelected(false);
		hasIncompleteWordOption = false;
		solutionCheckBoxOption.setSelected(false);
		hasSolution = false;
		discoveredWordsCheckBoxOption.setSelected(false);
		hasDiscoveredWordsOption = false;
		twoLettersRadioButtonOption.setSelected(false);
		hasTwoLettersOption = false;
		threeLettersRadioButtonOption.setSelected(false);
		hasThreeLettersOption = false;

		discoveredWordsCheckBoxOption.setDisable(true);
		incompleteWordCheckBoxOption.setDisable(true);
		solutionCheckBoxOption.setDisable(true);
		twoLettersRadioButtonOption.setDisable(true);
		threeLettersRadioButtonOption.setDisable(true);

		if (trainingModeRadioButtonOption.isSelected()) {
			trainingModeRadioButtonOption.setSelected(false);
			isInTrainingMode = false;
		}

		if (!assessmentModeRadioButtonOption.isSelected()) {
			isInAssessmentMode = false;

			timerOption.setText(null);
			timerOption.setDisable(true);
		}

	}

	@FXML
	public void selectionModeEntrainement() {
		discoveredWordsCheckBoxOption.setDisable(false);
		incompleteWordCheckBoxOption.setDisable(false);
		solutionCheckBoxOption.setDisable(false);
		twoLettersRadioButtonOption.setDisable(false);
		threeLettersRadioButtonOption.setDisable(false);

		timerOption.setDisable(true);
		isInTrainingMode = true;

		timerOption.setText("");

		if (assessmentModeRadioButtonOption.isSelected()) {
			assessmentModeRadioButtonOption.setSelected(false);
			isInAssessmentMode = false;
		}

		if (!trainingModeRadioButtonOption.isSelected()) {

			discoveredWordsCheckBoxOption.setDisable(true);
			incompleteWordCheckBoxOption.setDisable(true);
			solutionCheckBoxOption.setDisable(true);
			twoLettersRadioButtonOption.setDisable(true);
			threeLettersRadioButtonOption.setDisable(true);

			incompleteWordCheckBoxOption.setSelected(false);
			hasIncompleteWordOption = false;
			solutionCheckBoxOption.setSelected(false);
			hasSolution = false;
			discoveredWordsCheckBoxOption.setSelected(false);
			hasDiscoveredWordsOption = false;
			twoLettersRadioButtonOption.setSelected(false);
			hasTwoLettersOption = false;
			threeLettersRadioButtonOption.setSelected(false);
			hasThreeLettersOption = false;

			isInTrainingMode = false;
		}

	}

	@FXML
	public void selection2Lettres() {
		if (threeLettersRadioButtonOption.isSelected()) {
			threeLettersRadioButtonOption.setSelected(false);

			hasTwoLettersOption = true;
			hasThreeLettersOption = false;
		} else {
			hasTwoLettersOption = true;
		}
	}

	@FXML
	public void selection3Lettres() {
		if (twoLettersRadioButtonOption.isSelected()) {
			twoLettersRadioButtonOption.setSelected(false);

			hasThreeLettersOption = true;
			hasTwoLettersOption = false;
		} 
		else {
			hasThreeLettersOption = true;
		}
	}

	@FXML
	public void RestrictionOne() {
		if (hiddenCharOption.getText().length() > 1) {
			hiddenCharOption.deletePreviousChar();
		}
	}

	@FXML
	public void RestrictionChiffre() {
		if (timerOption.getText().length() > 0) {
			if (!timerOption.getText().matches("[0-9]*")) {
				timerOption.deletePreviousChar();
			}
		}
	}

	@FXML
	public void motIncomplet() {

		if (incompleteWordCheckBoxOption.isSelected()) {

			twoLettersRadioButtonOption.setDisable(false);

			twoLettersRadioButtonOption.setSelected(true);
			hasTwoLettersOption = true;
			threeLettersRadioButtonOption.setDisable(false);

			hasIncompleteWordOption = true;
		}
		if (!incompleteWordCheckBoxOption.isSelected()) {
			twoLettersRadioButtonOption.setDisable(true);
			threeLettersRadioButtonOption.setDisable(true);

			twoLettersRadioButtonOption.setSelected(false);
			threeLettersRadioButtonOption.setSelected(false);

			hasTwoLettersOption = false;
			hasThreeLettersOption = false;

			hasIncompleteWordOption = false;
		}

	}

	@FXML
	public void sensiCasse() {
		if (caseSensitiveCheckBoxOption.isSelected()) {
			isCaseSensitive = true;
		}
		else {
			isCaseSensitive = false;
		}
	}

	@FXML
	public void affichageSolution() {
		if (solutionCheckBoxOption.isSelected()) {
			hasSolution = true;
		}
		else {
			hasSolution = false;
		}
	}

	@FXML
	public void motDecouverts() {
		if (discoveredWordsCheckBoxOption.isSelected()) {
			hasDiscoveredWordsOption = true;
		}
		else {
			hasDiscoveredWordsOption = false;
		}
	}

	public void affichageToolTip(ImageView image, String description) {
		Tooltip t = new Tooltip(description);
		image.setFitWidth(image.getFitWidth() + 2);
		image.setFitHeight(image.getFitHeight() + 2);
		Tooltip.install(image, t);
	}

	public void adaptationImage(ImageView image) {
		image.setFitWidth(image.getFitWidth() - 2);
		image.setFitHeight(image.getFitHeight() - 2);
	}

	@FXML
	public void tipOcculEnter() {
		affichageToolTip(toolTipOccul, "Ce caract�re servira � crypter le script de votre document");
	}

	@FXML
	public void tipOcculExit() {
		adaptationImage(toolTipOccul);
	}

	@FXML
	public void tipSensiEnter() {
		affichageToolTip(toolTipSensi, "Activer la sensibilit� � la casse signifie prendre en compte la diff�rence entre minuscule et majuscule");
	}

	@FXML
	public void tipSensiExit() {
		adaptationImage(toolTipSensi);
	}

	@FXML
	public void tipEvalEnter() {
		affichageToolTip(toolTipEval, "Le mode Evaluation n'autorise aucune aide pour l'�tudiant");
	}

	@FXML
	public void tipEvalExit() {
		adaptationImage(toolTipEval);
	}

	@FXML
	public void tipMinEnter() {
		affichageToolTip(toolTipNbMin, "Le nombre de minutes dont l'�l�ve disposera pour faire l'exercice");
	}

	@FXML
	public void tipMinExit() {
		adaptationImage(toolTipNbMin);
	}

	@FXML
	public void tipEntrEnter() {
		affichageToolTip(toolTipEntr, "Le mode Entra�nement autorise ou non ceratiens options (list�es ci-dessous)");
	}

	@FXML
	public void tipEntrExit() {
		adaptationImage(toolTipEntr);
	}

	@FXML
	public void tipMotDecouvertEnter() {
		affichageToolTip(toolTipMotDecouvert, "Cette option permet � l'�tudiant de voir en temps r�el le nombre de mots qu'il a trouv�");
	}

	@FXML
	public void tipMotDecouvertExit() {
		adaptationImage(toolTipMotDecouvert);
	}

	@FXML
	public void tipSolutionEnter() {
		affichageToolTip(toolTipSolution, "Autoriser � ce que l'�tudiant puisse consulter la hasSolution pendant l'exercice");
	}

	@FXML
	public void tipSolutionExit() {
		adaptationImage(toolTipSolution);
	}

	@FXML
	public void tipMotIncompletEnter() {
		affichageToolTip(toolTipMotIncomplet, "Autoriser le remplacement partiel des mots � partir d'un nombre minimum de lettres");
	}

	@FXML
	public void tipMotIncompletExit() {
		adaptationImage(toolTipMotIncomplet);
	}

	@FXML
	public void darkMode() {

		if(dark.isSelected()) {
			hiddenCharOption.getScene().getStylesheets().removeAll(getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
			hiddenCharOption.getScene().getStylesheets().addAll(getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
			AccueilController.isDark = true;
		} else {
			hiddenCharOption.getScene().getStylesheets().removeAll(getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
			hiddenCharOption.getScene().getStylesheets().addAll(getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
			AccueilController.isDark = false;
		}

	}

	public void darkModeActivation(Scene scene) {
		if(AccueilController.isDark) {
			scene.getStylesheets().removeAll(getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
			scene.getStylesheets().addAll(getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
			dark.setSelected(true);
		} else {
			scene.getStylesheets().removeAll(getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
			scene.getStylesheets().addAll(getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
			dark.setSelected(false);
		}
	}
}
