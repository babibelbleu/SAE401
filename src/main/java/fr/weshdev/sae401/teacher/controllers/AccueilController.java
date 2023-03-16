package fr.weshdev.sae401.teacher.controllers;

import fr.weshdev.sae401.MainEnseignant;
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
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AccueilController implements Initializable {

	@FXML
	private Text RecupScene;
	@FXML
	private Label recupScene;

	@FXML private CheckMenuItem dark;
	public static boolean isDark = false;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	@FXML
	public void quitter() {
		Platform.exit();
	}

	@FXML
	public void ouvrir() throws IOException {

		FileChooser fileChooser = new FileChooser();
		File selectedFile;
		fileChooser.setTitle("Ouvrez votre exercice");

		selectedFile = fileChooser.showOpenDialog(null);
		decrypte(selectedFile);

		NewExerciseController.contenuNomExo = stripExtension(selectedFile);

		NewExerciseController.contenuRepertoire = stripPath(selectedFile);

		pageNouvelExo();
	}

	public static String stripExtension(File file) {
		if (file == null) {
			return null;
		}
		String name = file.getName();

		int posPoint = name.lastIndexOf(".");

		if (posPoint == -1) {
			return name;
		}

		return name.substring(0, posPoint);
	}

	public static String stripPath(File file) {
		if (file == null) {
			return null;
		}
		String name = file.getAbsolutePath();

		int posPoint = name.lastIndexOf("\\");

		if (posPoint == -1) {
			return name;
		}

		return name.substring(0, posPoint);
	}

	public void decrypte(File file) throws IOException {

		String consigne, aide, transcription, caraOccul, nbMin;
		int nombreOctetALire, sensiCasse, mode, solution, motsDecouverts, motsIncomplets, lettre, extension;
		File tmpFile;

		FileInputStream fin = new FileInputStream(file);

		nombreOctetALire = ByteBuffer.wrap(readNBytes(fin, 4)).getInt();
		consigne = chaine(readNBytes(fin, nombreOctetALire));
		ApercuController.contenuConsigne = consigne;

		nombreOctetALire = ByteBuffer.wrap(readNBytes(fin, 4)).getInt();
		transcription = chaine(readNBytes(fin, nombreOctetALire));
		ApercuController.contenuTranscription = transcription;

		nombreOctetALire = ByteBuffer.wrap(readNBytes(fin, 4)).getInt();
		aide = chaine(readNBytes(fin, nombreOctetALire));
		ApercuController.contenuAide = aide;

		caraOccul = chaine(readNBytes(fin, 1));
		OptionsController.caraOccul = caraOccul;

		// On r�cup�re la reponse de sensiCasse 0 = false, 1 = true
		sensiCasse = ByteBuffer.wrap(readNBytes(fin, 1)).get();

		if (sensiCasse == 1) {
			OptionsController.sensiCasse = true;
		} else {
			OptionsController.sensiCasse = false;
		}

		mode = ByteBuffer.wrap(readNBytes(fin, 1)).get();

		if (mode == 1) {
			OptionsController.evaluation = true;
			OptionsController.entrainement = false;

			nombreOctetALire = ByteBuffer.wrap(readNBytes(fin, 4)).getInt();
			nbMin = chaine(readNBytes(fin, nombreOctetALire));

			OptionsController.nbMin = nbMin;

		} else {
			OptionsController.evaluation = false;
			OptionsController.entrainement = true;

			solution = ByteBuffer.wrap(readNBytes(fin, 1)).get();

			if (solution == 1) {
				OptionsController.solution = true;
			} else {
				OptionsController.solution = false;
			}

			motsDecouverts = ByteBuffer.wrap(readNBytes(fin, 1)).get();

			if (motsDecouverts == 1) {
				OptionsController.motDecouverts = true;
			} else {
				OptionsController.motDecouverts = false;
			}

			motsIncomplets = ByteBuffer.wrap(readNBytes(fin, 1)).get();

			if (motsIncomplets == 1) {
				OptionsController.motIncomplet = true;

				lettre = ByteBuffer.wrap(readNBytes(fin, 1)).get();

				if (lettre == 2) {
					OptionsController.lettres_2 = true;
					OptionsController.lettres_3 = false;
				} else {
					OptionsController.lettres_2 = false;
					OptionsController.lettres_3 = true;
				}

			} else {
				OptionsController.motIncomplet = false;
				OptionsController.lettres_2 = false;
				OptionsController.lettres_3 = false;
			}
		}

		// extension = 0 -> mp3, extension = 1 -> mp4
		extension = ByteBuffer.wrap(readNBytes(fin, 1)).get();

		if (extension == 0) {

			nombreOctetALire = ByteBuffer.wrap(readNBytes(fin, 8)).getInt();

			File tmpFileImage = File.createTempFile("data", ".png");
			FileOutputStream ecritureFileImage = new FileOutputStream(tmpFileImage);
			ecritureFileImage.write(readNBytes(fin, nombreOctetALire));
			ecritureFileImage.close();

			ImportRessourceController.contenuImage = new Image(tmpFileImage.toURI().toString());
			ImportRessourceController.cheminImg = tmpFileImage.getAbsolutePath();

			tmpFileImage.deleteOnExit();

			tmpFile = File.createTempFile("data", ".mp3");

		}
		else {
			ImportRessourceController.contenuImage = null;

			tmpFile = File.createTempFile("data", ".mp4");

		}

		FileOutputStream ecritureFile = new FileOutputStream(tmpFile);
		ecritureFile.write(readAllBytes(fin));
		ecritureFile.close();

		ImportRessourceController.contenuMedia = new Media(tmpFile.toURI().toString());
		ImportRessourceController.cheminVideo = tmpFile.getAbsolutePath();

		tmpFile.deleteOnExit();

		fin.close();
	}

	/**
	 * Méthode qui permet de convertir un tableau d'octets en chaine de caractère
	 * @param bytes
	 * @return
	 */
	public String chaine(byte[] bytes) {
		String chaine = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
		return chaine;
	}

	@FXML
	public void tuto() throws IOException{
		
		InputStream is = MainEnseignant.class.getResourceAsStream("/fr.weshdev.sae401/pdf/user_manual.pdf");

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
	public void pageNouvelExo() throws IOException {

		Stage primaryStage = (Stage) RecupScene.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/new_exercise.fxml"));
		Scene scene = new Scene(root, MainEnseignant.width, MainEnseignant.height - 60);
		primaryStage.setScene(scene);
		darkModeActivation(scene);
		primaryStage.show();
	}

	@FXML
	public void aPropos() throws IOException {
		Stage primaryStage = (Stage) RecupScene.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/about.fxml"));
		Scene scene = new Scene(root, MainEnseignant.width, MainEnseignant.height - 60);
		primaryStage.setScene(scene);
		darkModeActivation(scene);
		primaryStage.show();
	}

	@FXML
	public void retourMenu() throws IOException {
		Stage primaryStage = (Stage) recupScene.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/menu.fxml"));
		Scene scene = new Scene(root, MainEnseignant.width, MainEnseignant.height - 60);
		primaryStage.setScene(scene);
		darkModeActivation(scene);

		primaryStage.setMinHeight(800);
		primaryStage.setMinWidth(1200);
		primaryStage.show();
	}

	@FXML
	public void darkMode() {
		if(dark.isSelected()) {
			RecupScene.getScene().getStylesheets().removeAll(getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
			RecupScene.getScene().getStylesheets().addAll(getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
			isDark = true;
		} else {
			RecupScene.getScene().getStylesheets().removeAll(getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
			RecupScene.getScene().getStylesheets().addAll(getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
			isDark = false;
		}
	}
	public void darkModeActivation(Scene scene) {
		if(isDark) {
			scene.getStylesheets().removeAll(getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
			scene.getStylesheets().addAll(getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
			dark.setSelected(true);
		} else {
			scene.getStylesheets().removeAll(getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
			scene.getStylesheets().addAll(getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
			dark.setSelected(false);
		}
	}

	public void delete() {
		NewExerciseController.contenuNomExo = null;
		NewExerciseController.contenuRepertoire = null;
		ImportRessourceController.contenuMedia = null;
		ImportRessourceController.contenuImage = null;
		ApercuController.contenuAide = null;
		ApercuController.contenuConsigne = null;
		ApercuController.contenuTranscription = null;
		OptionsController.caraOccul = null;
		OptionsController.sensiCasse = false;
		OptionsController.entrainement = false;
		OptionsController.evaluation = false;
		OptionsController.lettres_2 = false;
		OptionsController.lettres_3 = false;
		OptionsController.motDecouverts = false;
		OptionsController.motIncomplet = false;
		OptionsController.solution = false;
		OptionsController.nbMin = null;
	}

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
