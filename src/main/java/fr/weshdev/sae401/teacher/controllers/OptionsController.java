package fr.weshdev.sae401.teacher.controllers;

import fr.weshdev.sae401.DeplacementFenetre;
import fr.weshdev.sae401.MainEnseignant;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class OptionsController implements Initializable {

	@FXML
	private RadioButton radioButtonEntrainement;
	@FXML
	private RadioButton radioButtonEvaluation;
	@FXML
	private RadioButton radioButton2Lettres;
	@FXML
	private RadioButton radioButton3Lettres;
	@FXML
	private TextField CaraOccul;
	@FXML
	private TextField nbMinute;
	@FXML
	private CheckBox checkBoxMotIncomplet;
	@FXML
	private CheckBox checkBoxSolution;
	@FXML
	private CheckBox checkBoxMotsDecouverts;
	@FXML
	private CheckBox sensibiliteCasse;
	@FXML
	private Button enregistrer;

	public static String caraOccul;
	public static String nbMin;
	public static boolean sensiCasse;
	public static boolean entrainement;
	public static boolean evaluation;
	public static boolean solution;
	public static boolean motDecouverts;
	public static boolean motIncomplet;
	public static boolean lettres_2;
	public static boolean lettres_3;

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

		if(caraOccul != null) {
			CaraOccul.setText(caraOccul);
		}

		if(sensiCasse == true) {
			sensibiliteCasse.setSelected(true);
		}

		if(entrainement == true) {
			radioButtonEntrainement.setSelected(true);

			nbMinute.setDisable(true);

			if(solution == true) {
				checkBoxSolution.setSelected(true);
			}

			if(motDecouverts == true) {
				checkBoxMotsDecouverts.setSelected(true);
			}

			if(motIncomplet ==  true) {
				checkBoxMotIncomplet.setSelected(true);

				if(lettres_2 == true) {
					radioButton2Lettres.setSelected(true);
				}

				if(lettres_3 == true) {
					radioButton3Lettres.setSelected(true);
				}

			}
		}

		if(evaluation == true) {
			radioButtonEvaluation.setSelected(true);

			checkBoxMotsDecouverts.setDisable(true);
			checkBoxMotIncomplet.setDisable(true);
			checkBoxSolution.setDisable(true);
			radioButton2Lettres.setDisable(true);
			radioButton3Lettres.setDisable(true);
			
			
			nbMinute.setText(nbMin);
			nbMinute.setDisable(false);
		}

		checkMode();

		if(!CaraOccul.getText().isEmpty() && (radioButtonEntrainement.isSelected() || (radioButtonEvaluation.isSelected()  && !nbMinute.getText().isEmpty()))) {
			enregistrer.setDisable(false);
		}

	}

	private void checkMode() {

		CaraOccul.textProperty().addListener((arg0, arg1, arg2) -> {
			if(!CaraOccul.getText().isEmpty() && (radioButtonEntrainement.isSelected() || (radioButtonEvaluation.isSelected()  && !nbMinute.getText().isEmpty()))) {
				enregistrer.setDisable(false);
			} else {
				enregistrer.setDisable(true);
			}

		});

		radioButtonEntrainement.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
			if (!CaraOccul.getText().isEmpty() && (radioButtonEntrainement.isSelected() || (radioButtonEvaluation.isSelected() && !nbMinute.getText().isEmpty()))) {
				enregistrer.setDisable(false);
			} else {
				enregistrer.setDisable(true);
			}
		});

		radioButtonEvaluation.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
			if (!CaraOccul.getText().isEmpty() && (radioButtonEntrainement.isSelected() || (radioButtonEvaluation.isSelected() && !nbMinute.getText().isEmpty()))) {
				enregistrer.setDisable(false);
			} else {
				enregistrer.setDisable(true);
			}
		});


		nbMinute.textProperty().addListener((arg0, arg1, arg2) -> {

			if(radioButtonEvaluation.isSelected()) {
				if(!CaraOccul.getText().isEmpty() && !nbMinute.getText().isEmpty()) {
					enregistrer.setDisable(false);
				} else {
					enregistrer.setDisable(true);
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

		if(!CaraOccul.getText().isEmpty() && CaraOccul.getText() != "") {
			caraOccul = CaraOccul.getText();
		}

		if(!nbMinute.getText().isEmpty() && nbMinute.getText() != "") {
			nbMin = nbMinute.getText();
		}

		Stage primaryStage = (Stage) nbMinute.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/apercu.fxml"));
		Scene scene = new Scene(root, MainEnseignant.width, MainEnseignant.height - 60);
		primaryStage.setScene(scene);
		darkModeActivation(scene);
	}

	public void retourMenu() throws IOException {
		Stage stage = (Stage) CaraOccul.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/menu.fxml"));
		Scene scene = new Scene(root,  MainEnseignant.width, MainEnseignant.height - 60);
		stage.setScene(scene);
		darkModeActivation(scene);
		stage.show();
	}

	@FXML
	public void pageEnregistrementFinal() throws IOException {

		caraOccul = CaraOccul.getText();
		nbMin = nbMinute.getText();

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
		Stage primaryStage = (Stage) CaraOccul.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/new_exercise.fxml"));
		Scene scene = new Scene(root, MainEnseignant.width, MainEnseignant.height - 60);
		primaryStage.setScene(scene);
		darkModeActivation(scene);
		primaryStage.show();
	}

	@FXML
	public void selectionModeEvaluation() {
		nbMinute.setDisable(false);
		evaluation = true;
		checkBoxMotIncomplet.setSelected(false);
		motIncomplet = false;
		checkBoxSolution.setSelected(false);
		solution = false;
		checkBoxMotsDecouverts.setSelected(false);
		motDecouverts = false;
		radioButton2Lettres.setSelected(false);
		lettres_2 = false;
		radioButton3Lettres.setSelected(false);
		lettres_3 = false;

		checkBoxMotsDecouverts.setDisable(true);
		checkBoxMotIncomplet.setDisable(true);
		checkBoxSolution.setDisable(true);
		radioButton2Lettres.setDisable(true);
		radioButton3Lettres.setDisable(true);

		if (radioButtonEntrainement.isSelected()) {
			radioButtonEntrainement.setSelected(false);
			entrainement = false;
		}

		if (!radioButtonEvaluation.isSelected()) {
			evaluation = false;

			nbMinute.setText(null);
			nbMinute.setDisable(true);
		}

	}

	@FXML
	public void selectionModeEntrainement() {
		checkBoxMotsDecouverts.setDisable(false);
		checkBoxMotIncomplet.setDisable(false);
		checkBoxSolution.setDisable(false);
		radioButton2Lettres.setDisable(false);
		radioButton3Lettres.setDisable(false);

		nbMinute.setDisable(true);
		entrainement = true;

		nbMinute.setText("");

		if (radioButtonEvaluation.isSelected()) {
			radioButtonEvaluation.setSelected(false);
			evaluation = false;
		}

		if (!radioButtonEntrainement.isSelected()) {

			checkBoxMotsDecouverts.setDisable(true);
			checkBoxMotIncomplet.setDisable(true);
			checkBoxSolution.setDisable(true);
			radioButton2Lettres.setDisable(true);
			radioButton3Lettres.setDisable(true);

			checkBoxMotIncomplet.setSelected(false);
			motIncomplet = false;
			checkBoxSolution.setSelected(false);
			solution = false;
			checkBoxMotsDecouverts.setSelected(false);
			motDecouverts = false;
			radioButton2Lettres.setSelected(false);
			lettres_2 = false;
			radioButton3Lettres.setSelected(false);
			lettres_3 = false;

			entrainement = false;
		}

	}

	@FXML
	public void selection2Lettres() {
		if (radioButton3Lettres.isSelected()) {
			radioButton3Lettres.setSelected(false);

			lettres_2 = true;
			lettres_3 = false;
		} else {
			lettres_2 = true;
		}
	}

	@FXML
	public void selection3Lettres() {
		if (radioButton2Lettres.isSelected()) {
			radioButton2Lettres.setSelected(false);

			lettres_3 = true;
			lettres_2 = false;
		} 
		else {
			lettres_3 = true;
		}
	}

	@FXML
	public void RestrictionOne() {
		if (CaraOccul.getText().length() > 1) {
			CaraOccul.deletePreviousChar();
		}
	}

	@FXML
	public void RestrictionChiffre() {
		if (nbMinute.getText().length() > 0) {
			if (!nbMinute.getText().matches("[0-9]*")) {
				nbMinute.deletePreviousChar();
			}
		}
	}

	@FXML
	public void motIncomplet() {

		if (checkBoxMotIncomplet.isSelected()) {

			radioButton2Lettres.setDisable(false);

			radioButton2Lettres.setSelected(true);
			lettres_2 = true;
			radioButton3Lettres.setDisable(false);

			motIncomplet = true;
		}
		if (!checkBoxMotIncomplet.isSelected()) {
			radioButton2Lettres.setDisable(true);
			radioButton3Lettres.setDisable(true);

			radioButton2Lettres.setSelected(false);
			radioButton3Lettres.setSelected(false);

			lettres_2 = false;
			lettres_3 = false;

			motIncomplet = false;
		}

	}

	@FXML
	public void sensiCasse() {
		if (sensibiliteCasse.isSelected()) {
			sensiCasse = true;
		}
		else {
			sensiCasse = false;
		}
	}

	@FXML
	public void affichageSolution() {
		if (checkBoxSolution.isSelected()) {
			solution = true;
		}
		else {
			solution = false;
		}
	}

	@FXML
	public void motDecouverts() {
		if (checkBoxMotsDecouverts.isSelected()) {
			motDecouverts = true;
		}
		else {
			motDecouverts = false;
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
		affichageToolTip(toolTipSolution, "Autoriser � ce que l'�tudiant puisse consulter la solution pendant l'exercice");
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
			CaraOccul.getScene().getStylesheets().removeAll(getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
			CaraOccul.getScene().getStylesheets().addAll(getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
			AccueilController.isDark = true;
		} else {
			CaraOccul.getScene().getStylesheets().removeAll(getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
			CaraOccul.getScene().getStylesheets().addAll(getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
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
