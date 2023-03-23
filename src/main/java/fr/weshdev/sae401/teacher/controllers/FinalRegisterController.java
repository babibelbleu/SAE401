package fr.weshdev.sae401.teacher.controllers;

import fr.weshdev.sae401.model.Exercise;
import fr.weshdev.sae401.model.Option;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FinalRegisterController implements Initializable {

	@FXML
	private Text recupScene;

	// M�thode d'initialisation de la page
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		Exercise exercise = new Exercise();
		exercise.setOrder(ApercuController.instructionContent);
		exercise.setTranscription(ApercuController.transcriptionContent);
		exercise.setHint(ApercuController.helpContent);
		exercise.setReplacingChar(OptionsController.hiddenChar);
		exercise.setTimer(OptionsController.timer);

		exercise.setOptions(setupOptions());

		exercise.setMediaPath(ImportRessourceController.getCheminVideo());

		if(ImportRessourceController.getContenuImage() != null) {
			exercise.setImagePath(ImportRessourceController.getCheminImg());
		}

		try {
			exercise.build(NewExerciseController.getDirectoryPath() + "\\" + NewExerciseController.getExerciseName() + ".rct");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		NewExerciseController.delete();
		ImportRessourceController.reset();
		ApercuController.reset();
		OptionsController.reset();
	}

	private HashMap<String, Option> setupOptions(){
		HashMap<String, Option> options = new HashMap<>();
		Option caseSensitiveOption = new Option("Case sensitive", "Permet de rendre sensible à la casse", OptionsController.isCaseSensitive);
		Option evaluationOption = new Option("Evaluation", "Permet de faire une évaluation", OptionsController.isInAssessmentMode);
		Option trainingOption = new Option("Training", "Permet de faire de l'entrainement", OptionsController.isInTrainingMode);
		Option solutionShowOption = new Option("SolutionShow", "Permet d'afficher la solution", OptionsController.hasSolution);
		Option discoveredWordRateProgressBarOption = new Option("Progress bar", "Permet d'afficher la barre de progression", OptionsController.hasDiscoveredWordsOption);
		Option incompletedWordOption = new Option("Incompleted word", "Permet de faire des mots incomplets", OptionsController.hasIncompleteWordOption);
		Option incompletedWordWithTwoLettersOption = new Option("Incompleted word with two letters", "Permet de faire des mots incomplets avec deux lettres", OptionsController.hasTwoLettersOption);
		Option incompleteWordWithThreeLettersOption = new Option("Incompleted word with three letters", "Permet de faire des mots incomplets avec trois lettres", OptionsController.hasThreeLettersOption);


		options.put("caseSensitiveOption", caseSensitiveOption);
		options.put("evaluationOption", evaluationOption);
		options.put("solutionShowOption", solutionShowOption);
		options.put("discoveredWordRateProgressBarOption", discoveredWordRateProgressBarOption);
		options.put("incompletedWordOption", incompletedWordOption);
		options.put("incompletedWordWithTwoLettersOption", incompletedWordWithTwoLettersOption);
		options.put("incompletedWordWithThreeLettersOption", incompleteWordWithThreeLettersOption);
		options.put("trainingOption", trainingOption);

		return options;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////       METHODES GENERALES         /////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 

	// Bouton Quitter qui permet � l'enseignant de loadQuittingPage l'application (disponible
	// sur toutes les pages)
	@FXML
	public void quitter(ActionEvent event) {
		Platform.exit();
	}

	@FXML
	public void okay() {
		recupScene.getScene().getWindow().hide();
	}

	//M�thode qui r�cup�re la lonngueur en byte d'une convertBytesToString de caract�re
	private byte[] getLongueur(String chaine) {

		int nbCara = 0;

		for (int i = 0; i < chaine.length(); i++) {
			nbCara++;
		}
		return ByteBuffer.allocate(4).putInt(nbCara).array();
	}

	private String getExtension(String filePath) {
		if (filePath == null) {
			return null;
		}
		int posPoint = filePath.lastIndexOf(".");

		if (posPoint == -1) {
			return null;
		}

		return filePath.substring(posPoint);
	}

	// M�thode qui va lire n bytes (ne marche pas sous java 1.8 donc on la remet ici
	// telle quel
	private static final int DEFAULT_BUFFER_SIZE = 8192;
	private static final int MAX_BUFFER_SIZE = Integer.MAX_VALUE - 8;

	public static byte[] readNBytes(FileInputStream fin, int len) throws IOException {
		if (len < 0) {
			throw new IllegalArgumentException("len < 0");
		}

		List<byte[]> bufs = null;
		byte[] result = null;
		int total = 0;
		int remaining = len;
		int n;
		do {
			byte[] buf = new byte[Math.min(remaining, DEFAULT_BUFFER_SIZE)];
			int nread = 0;

			// read to EOF which may read more or less than buffer size
			while ((n = fin.read(buf, nread, Math.min(buf.length - nread, remaining))) > 0) {
				nread += n;
				remaining -= n;
			}

			if (nread > 0) {
				if (MAX_BUFFER_SIZE - total < nread) {
					throw new OutOfMemoryError("Required array size too large");
				}
				total += nread;
				if (result == null) {
					result = buf;
				} else {
					if (bufs == null) {
						bufs = new ArrayList<>();
						bufs.add(result);
					}
					bufs.add(buf);
				}
			}
			// if the last call to read returned -1 or the number of bytes
			// requested have been read then break
		} while (n >= 0 && remaining > 0);

		if (bufs == null) {
			if (result == null) {
				return new byte[0];
			}
			return result.length == total ? result : Arrays.copyOf(result, total);
		}

		result = new byte[total];
		int offset = 0;
		remaining = total;
		for (byte[] b : bufs) {
			int count = Math.min(b.length, remaining);
			System.arraycopy(b, 0, result, offset, count);
			offset += count;
			remaining -= count;
		}

		return result;
	}

	public static byte[] readAllBytes(FileInputStream fin) throws IOException {
		return readNBytes(fin, Integer.MAX_VALUE);
	}

}