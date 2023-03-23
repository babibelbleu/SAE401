package fr.weshdev.sae401.student.controllers;

import fr.weshdev.sae401.MainEtudiant;
import fr.weshdev.sae401.model.Crypter;
import fr.weshdev.sae401.model.DarkModeManager;
import fr.weshdev.sae401.model.Exercise;
import fr.weshdev.sae401.model.Option;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
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
import java.nio.charset.Charset;
import java.util.*;
import java.util.List;

public class MenuController implements Initializable {
	@FXML
	private  Text welcomeText;
	@FXML
	private Label aboutText;

	@FXML
	private BorderPane mainPain;




	private static HashMap <String, Option> options = new HashMap<>();

	private static ExerciseController exerciseController;
	DarkModeManager darkModeManager = new DarkModeManager();

	public static HashMap<String,Option> getOptions() {

		// TODO document why this method is empty
		return options;
	}


	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			MenuBar header = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/header.fxml"));
			mainPain.setTop(header);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}


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
		Scene exerciseScene = new Scene(root, MainEtudiant.getWidth(), MainEtudiant.getHeight());

		primaryStage.setScene(exerciseScene);
		primaryStage.show();

		if(darkModeManager.isDarkMode()) {
			darkModeManager.darkModeActivation(exerciseScene);
		}
		else {
			darkModeManager.darkModeDesactivation(exerciseScene);
		}
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

	@FXML
	public void loadAboutPage() throws IOException {
		Stage primaryStage = (Stage) welcomeText.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/student/about.fxml"));
		Scene scene = new Scene(root, MainEtudiant.getWidth(), MainEtudiant.getHeight() - 60);
		primaryStage.setScene(scene);

		primaryStage.setMaximized(true);
		primaryStage.setMinHeight(800);
		primaryStage.setMinWidth(1200);
		primaryStage.show();
	}

	@FXML
	public void loadMenu() throws IOException {
		Stage primaryStage = (Stage) aboutText.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/student/menu.fxml"));
		Scene scene = new Scene(root, MainEtudiant.getWidth(), MainEtudiant.getHeight());
		primaryStage.setScene(scene);


		primaryStage.setMinHeight(800);
		primaryStage.setMinWidth(1200);
		primaryStage.show();
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

		Charset utf8 = Charset.forName("UTF-8");
		String str = new String(finalBuffer, utf8);
		return str.getBytes(utf8);
	}

	public static byte[] readAllBytesFromFile(FileInputStream file) throws IOException {
		return readBytesFromFile(file, Integer.MAX_VALUE);
	}

}