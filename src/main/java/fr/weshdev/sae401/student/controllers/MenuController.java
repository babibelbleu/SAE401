package fr.weshdev.sae401.student.controllers;

import fr.weshdev.sae401.MainEtudiant;
import fr.weshdev.sae401.model.Option;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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

public class MenuController implements Initializable {
	@FXML
	private Text welcomeText;
	@FXML
	private Label aboutText;
	@FXML
	private CheckMenuItem darkModeMenuSelection;

	public static boolean isInDarkMode = false;

	private static HashMap <String, Option> options = new HashMap<>();

	private static ExerciseController exerciseController;

	public static HashMap<String,Option> getOptions() {

		// TODO document why this method is empty
		return options;
	}


	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		// TODO document why this method is empty
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


	}


	@FXML
	public void closeApp() {
		Platform.exit();
	}


	@FXML
	public void loadUserManual() throws IOException, URISyntaxException {
		File userManual = new File(MainEtudiant.class.getResource("/fr.weshdev.sae401/pdf/user_manual.pdf").toURI());
        Desktop.getDesktop().open(userManual);
	}

	@FXML
	public void openExercise() throws IOException {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Ouvrez votre exercice");

		File selectedFile = fileChooser.showOpenDialog(null);

		SaveAfterOpenController.exerciceName = getFileName(selectedFile);
		setExercise(selectedFile);

		loadExercise();
	}

	public String getFileName(File file) {
        if (file == null) {
            return null;
        }
        String fileName = file.getName();

		return fileName.lastIndexOf(".") > 0 ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
    }

	/**
	 * Fonction qui va charger l'exercice
	 * @throws IOException
	 */
	public void loadExercise() throws IOException {
		Stage primaryStage = (Stage) welcomeText.getScene().getWindow();
		FXMLLoader exercisePageLoader = new FXMLLoader(getClass().getResource("/fr.weshdev.sae401/templates/student/exercise.fxml"));
		Parent root = exercisePageLoader.load();
		exerciseController = exercisePageLoader.getController();
		primaryStage.setMaximized(true);
		Scene exerciseScene = new Scene(root, MainEtudiant.width, MainEtudiant.height);

		darkModeActivation(exerciseScene);

		primaryStage.setScene(exerciseScene);
		primaryStage.show();
	}

	public static ExerciseController getController() {
		return exerciseController;
	}

	/**
	 * Transform bytes into a String <br>
	 * encoding -> UTF-8
	 * @param bytes les octets à transformer
	 * @return
	 */
	public String convertBytesToString(byte[] bytes) {
		return new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
	}

	/**
	 *Fonction qui va load les informations du fichier s�lectionn� dans les
	 * diff�rents TextField...
	 *
	 * @param file fichier s�lectionn�
	 */
	public void setExercise(File file) throws IOException {

		String exerciseOrder,
				exerciseHint,
				exerciseTranscription,
				replacingChar,
				time;

		int byteLength,
				isCaseSensitive,
				isEvaluation,
				exereciseHaveSolution,
				exerciseHaveProgressBar,
				isIncompletedWordOptionSelected,
				numberOfMinimalLettersIncompletedWord,
				isVideo;

		File tempFile;

		FileInputStream encodedExerciseFile = new FileInputStream(file);

		byteLength = ByteBuffer.wrap(readBytesFromFile(encodedExerciseFile, 4)).getInt();
		exerciseOrder = convertBytesToString(readBytesFromFile(encodedExerciseFile, byteLength));
		ExerciseController.instructionContent = exerciseOrder;

		byteLength = ByteBuffer.wrap(readBytesFromFile(encodedExerciseFile, 4)).getInt();
		exerciseTranscription = convertBytesToString(readBytesFromFile(encodedExerciseFile, byteLength));
		ExerciseController.transcriptionContent = exerciseTranscription;

		byteLength = ByteBuffer.wrap(readBytesFromFile(encodedExerciseFile, 4)).getInt();
		exerciseHint = convertBytesToString(readBytesFromFile(encodedExerciseFile, byteLength));
		HintsController.contenuAide = exerciseHint;

		replacingChar = convertBytesToString(readBytesFromFile(encodedExerciseFile, 1));
		ExerciseController.hidddenChar = replacingChar;

		isCaseSensitive = ByteBuffer.wrap(readBytesFromFile(encodedExerciseFile, 1)).get();

		if(isCaseSensitive == 1) {
			options.get("caseSensitiveOption").setActive(true);


		} else {
			options.get("caseSensitiveOption").setActive(false);

		}

		isEvaluation = ByteBuffer.wrap(readBytesFromFile(encodedExerciseFile, 1)).get();

		if (isEvaluation == 1) {
			options.get("evaluationOption").setActive(true);

			options.get("trainingOption").setActive(false);


			byteLength = ByteBuffer.wrap(readBytesFromFile(encodedExerciseFile, 4)).getInt();
			time = convertBytesToString(readBytesFromFile(encodedExerciseFile, byteLength));

			ExerciseController.nbMin = time;

		} else {
			options.get("evaluationOption").setActive(false);
			options.get("trainingOption").setActive(true);

			exereciseHaveSolution = ByteBuffer.wrap(readBytesFromFile(encodedExerciseFile, 1)).get();

			if (exereciseHaveSolution == 1) {
				options.get("solutionShowOption").setActive(true);

			} else {
				options.get("solutionShowOption").setActive(false);
			}

			exerciseHaveProgressBar = ByteBuffer.wrap(readBytesFromFile(encodedExerciseFile, 1)).get();

			if (exerciseHaveProgressBar == 1) {
				options.get("discoveredWordRateProgressBarOption").setActive(true);

			} else {
				options.get("discoveredWordRateProgressBarOption").setActive(false);
			}

			isIncompletedWordOptionSelected = ByteBuffer.wrap(readBytesFromFile(encodedExerciseFile, 1)).get();

			if (isIncompletedWordOptionSelected == 1) {
				options.get("incompletedWordOption").setActive(true);


				numberOfMinimalLettersIncompletedWord = ByteBuffer.wrap(readBytesFromFile(encodedExerciseFile, 1)).get();

				if (numberOfMinimalLettersIncompletedWord == 2) {
					options.get("incompletedWordWithTwoLettersOption").setActive(true);
					options.get("incompletedWordWithThreeLettersOption").setActive(false);
				} else {
					options.get("incompletedWordWithTwoLettersOption").setActive(false);
					options.get("incompletedWordWithThreeLettersOption").setActive(true);
				}

			} else {
				options.get("incompletedWordOption").setActive(false);
				options.get("incompletedWordWithTwoLettersOption").setActive(false);
				options.get("incompletedWordWithThreeLettersOption").setActive(false);
			}
		}

		isVideo = ByteBuffer.wrap(readBytesFromFile(encodedExerciseFile, 1)).get();

		if (isVideo == 0) {

			byteLength = ByteBuffer.wrap(readBytesFromFile(encodedExerciseFile, 8)).getInt();

			File tmpFileImage = File.createTempFile("data", ".png");
			FileOutputStream ecritureFileImage = new FileOutputStream(tmpFileImage);
			ecritureFileImage.write(readBytesFromFile(encodedExerciseFile, byteLength));
			ecritureFileImage.close();

			ExerciseController.imageContent = new Image(tmpFileImage.toURI().toString());

			tmpFileImage.deleteOnExit();

			tempFile = File.createTempFile("data", ".mp3");

		}
		else {
			tempFile = File.createTempFile("data", ".mp4");
		}

		FileOutputStream clearExerciseFile = new FileOutputStream(tempFile);
		clearExerciseFile.write(readAllBytesFromFile(encodedExerciseFile));
		clearExerciseFile.close();

		ExerciseController.mediaContent = new Media(tempFile.toURI().toString());

		tempFile.deleteOnExit();

		encodedExerciseFile.close();
	}

	@FXML
	public void loadAboutPage() throws IOException {
		Stage primaryStage = (Stage) welcomeText.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/student/about.fxml"));
		Scene scene = new Scene(root, MainEtudiant.width, MainEtudiant.height - 60);
		primaryStage.setScene(scene);

		darkModeActivation(scene);

		primaryStage.setMaximized(true);
		primaryStage.setMinHeight(800);
		primaryStage.setMinWidth(1200);
		primaryStage.show();
	}

	@FXML
	public void retourMenu(ActionEvent event) throws IOException {
		Stage primaryStage = (Stage) aboutText.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/student/menu.fxml"));
		Scene scene = new Scene(root, MainEtudiant.width, MainEtudiant.height);
		primaryStage.setScene(scene);

		darkModeActivation(scene);
		primaryStage.setMinHeight(800);
		primaryStage.setMinWidth(1200);
		primaryStage.show();
	}

	@FXML
	public void darkMode() {

		if (darkModeMenuSelection.isSelected()) {
			welcomeText.getScene().getStylesheets().removeAll(
					getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
			welcomeText.getScene().getStylesheets()
					.addAll(getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
			isInDarkMode = true;
		} else {
			welcomeText.getScene().getStylesheets().removeAll(
					getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
			welcomeText.getScene().getStylesheets().addAll(
					getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
			isInDarkMode = false;
		}
	}

	public void darkModeActivation(Scene scene) {
		if (isInDarkMode) {
			scene.getStylesheets().removeAll(
					getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
			scene.getStylesheets()
					.addAll(getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
			darkModeMenuSelection.setSelected(true);
		} else {
			scene.getStylesheets().removeAll(
					getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
			scene.getStylesheets().addAll(
					getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
			darkModeMenuSelection.setSelected(false);
		}
	}

	private static final int DEFAULT_BUFFER_SIZE = 8192;
	private static final int MAX_BUFFER_SIZE = Integer.MAX_VALUE - 8;

	public static byte[] readBytesFromFile(FileInputStream file, int len) throws IOException {
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

	public static byte[] readAllBytesFromFile(FileInputStream file) throws IOException {
		return readBytesFromFile(file, Integer.MAX_VALUE);
	}

}
