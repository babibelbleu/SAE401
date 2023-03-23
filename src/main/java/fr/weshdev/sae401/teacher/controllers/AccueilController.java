package fr.weshdev.sae401.teacher.controllers;

import fr.weshdev.sae401.MainEnseignant;
import fr.weshdev.sae401.MainEtudiant;
import fr.weshdev.sae401.model.DarkModeManager;
import fr.weshdev.sae401.model.Exercise;
import fr.weshdev.sae401.model.Option;
import fr.weshdev.sae401.student.controllers.ExerciseController;
import fr.weshdev.sae401.student.controllers.HintsController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.List;

public class AccueilController implements Initializable {

	@FXML
	private Text welcomeText;
	@FXML
	private BorderPane mainPain;

	@FXML private CheckMenuItem darkModeMenuSelection;

	DarkModeManager darkModeManager = new DarkModeManager();

	private static HashMap<String, Option> options = new HashMap<>();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Option caseSensitiveOption = new Option("Case sensitive", "Permet de rendre sensible à la casse", false);
		Option evaluationOption = new Option("Evaluation", "Permet de faire une évaluation", false);
		Option trainingOption = new Option("Training", "Permet de faire de l'entrainement", false);
		Option solutionShowOption = new Option("SolutionShow", "Permet d'afficher la solution", false);
		Option discoveredWordRateProgressBarOption = new Option("Progress bar", "Permet d'afficher la barre de progression", false);
		Option incompletedWordOption = new Option("Incompleted word", "Permet de faire des mots incomplets", false);
		Option incompletedWordWithTwoLettersOption = new Option("Incompleted word with two letters", "Permet de faire des mots incomplets avec deux lettres", false);
		Option incompleteWordWithThreeLettersOption = new Option("Incompleted word with three letters", "Permet de faire des mots incomplets avec trois lettres", false);


		options.put("caseSensitiveOption", caseSensitiveOption);
		options.put("evaluationOption", evaluationOption);
		options.put("solutionShowOption", solutionShowOption);
		options.put("discoveredWordRateProgressBarOption", discoveredWordRateProgressBarOption);
		options.put("incompletedWordOption", incompletedWordOption);
		options.put("incompletedWordWithTwoLettersOption", incompletedWordWithTwoLettersOption);
		options.put("incompletedWordWithThreeLettersOption", incompleteWordWithThreeLettersOption);
		options.put("trainingOption", trainingOption);

		try {
			MenuBar header = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/header.fxml"));
			mainPain.setTop(header);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}



	@FXML
	public void openExercise() throws IOException {

		FileChooser fileChooser = new FileChooser();
		File selectedFile;
		fileChooser.setTitle("Ouvrez votre exercice");

		selectedFile = fileChooser.showOpenDialog(null);
		setExercise(selectedFile);

		NewExerciseController.setExerciseName(getFileName(selectedFile));

		NewExerciseController.setDirectoryPath(getDirectoryPath(selectedFile));

		loadExercise();
	}

	public String getFileName(File file) {
		if (file == null) {
			return null;
		}

		String fileName = file.getName();

		return fileName.lastIndexOf(".") > 0 ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
	}

	public String getDirectoryPath(File file) {
		if (file == null) {
			return null;
		}

		String fileName = file.getAbsolutePath();

		return fileName.lastIndexOf("\\") > 0 ? fileName.substring(0, fileName.lastIndexOf("\\")) : fileName;
	}

	public void setExercise(File file) throws IOException {

		Exercise exercise = new Exercise().get(file.getAbsolutePath());

		ExerciseController.instructionContent = exercise.getOrder();
		ExerciseController.transcriptionContent = exercise.getTranscription();
		HintsController.contenuAide = exercise.getHint();
		ExerciseController.hidddenChar = exercise.getReplacingChar();
		options = exercise.getOptions();

		ExerciseController.nbMin = exercise.getTimer();
		ExerciseController.imageContent = exercise.getImage();
		ExerciseController.mediaContent = exercise.getMedia();
	}

	/**
	 * Méthode qui permet de convertir un tableau d'octets en convertBytesToString de caractère
	 * @param bytes
	 * @return
	 */
	public String convertBytesToString(byte[] bytes) {
		return new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
	}



	@FXML
	public void loadExercise() throws IOException {

		Stage primaryStage = (Stage) welcomeText.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/new_exercise.fxml"));
		Scene newExerciseScene = new Scene(root, MainEnseignant.width, MainEnseignant.height - 60);
		primaryStage.setScene(newExerciseScene);

		primaryStage.show();
		if(darkModeManager.isDarkMode()){
			darkModeManager.darkModeActivation(newExerciseScene);
		}
		else {
			darkModeManager.darkModeDesactivation(newExerciseScene);
		}
	}

	public void delete() {
		for(Option option: options.values()){
			option.reset();
		}
		NewExerciseController.delete();
		ImportRessourceController.reset();
		ApercuController.reset();
		OptionsController.reset();
	}

	private static final int DEFAULT_BUFFER_SIZE = 8192;
	private static final int MAX_BUFFER_SIZE = Integer.MAX_VALUE - 8;

	public byte[] readBytesFromFile(FileInputStream file, int len) throws IOException {
		if (len < 0) {
			throw new IllegalArgumentException("len < 0, cannot read negative bytes");
		}

		List<byte[]> buffers = null;
		byte[] firstBuffer = null;
		int total = 0;
		int remainingBytes = len;
		int bytesRead;
		do {
			byte[] buffer = new byte[Math.min(remainingBytes, DEFAULT_BUFFER_SIZE)];
			int offset = 0;

			while ((bytesRead = file.read(buffer, offset, Math.min(buffer.length - offset, remainingBytes))) > 0) {
				offset += bytesRead;
				remainingBytes -= bytesRead;
			}

			if (offset > 0) {
				if (MAX_BUFFER_SIZE - total < offset) {
					throw new OutOfMemoryError("Required array size too large");
				}
				total += offset;
				if (firstBuffer == null) {
					firstBuffer = buffer;
				} else {
					if (buffers == null) {
						buffers = new ArrayList<>();
						buffers.add(firstBuffer);
					}
					buffers.add(buffer);
				}
			}
		} while (bytesRead >= 0 && remainingBytes > 0);

		if (buffers == null) {
			if (firstBuffer == null) {
				return new byte[0];
			}
			return firstBuffer.length == total ? firstBuffer : Arrays.copyOf(firstBuffer, total);
		}

		byte[] finalBuffer = new byte[total];
		int finalBufferPosition = 0;
		remainingBytes = total;
		for (byte[] buffer : buffers) {
			int count = Math.min(buffer.length, remainingBytes);
			System.arraycopy(buffer, 0, finalBuffer, finalBufferPosition, count);
			finalBufferPosition += count;
			remainingBytes -= count;
		}

		return finalBuffer;
	}

	public byte[] readAllBytesFromFile(FileInputStream file) throws IOException {
		return readBytesFromFile(file, Integer.MAX_VALUE);
	}

}
