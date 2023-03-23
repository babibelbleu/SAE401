package fr.weshdev.sae401.model;

import fr.weshdev.sae401.DeplacementFenetre;
import fr.weshdev.sae401.MainEnseignant;
import fr.weshdev.sae401.MainEtudiant;
import fr.weshdev.sae401.student.controllers.MenuController;
import fr.weshdev.sae401.teacher.controllers.AccueilController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class HeaderController {


    DarkModeManager darkModeManager = new DarkModeManager();

    @FXML
    private MenuBar recupScene;
    @FXML
    private CheckMenuItem darkModeMenuSelection;
    @FXML private TextField repertoire;
    @FXML
    public void exitAPK(ActionEvent event) throws IOException {

        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/confirm_quit.fxml"));
        Scene scene = new Scene(root, 400, 200);
        //On bloque sur cette fenêtre
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
        primaryStage.show();
    }
    public void backHomePage() throws IOException {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        if (MainEtudiant.root!=null) {
            Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/student/menu.fxml"));

            Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight()-60);
            Stage stage = (Stage)recupScene.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else if(MainEnseignant.root!=null){
            Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/menu.fxml"));
            Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight()-60);
            Stage stage = (Stage) recupScene.getScene().getWindow();

            stage.setScene(scene);

            stage.show();

        }
    }

    @FXML
    public void tuto() throws MalformedURLException, IOException, URISyntaxException {

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
    public void loadAboutPage() throws IOException {
        System.out.println(MainEtudiant.root.isVisible());
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/teacher/about.fxml"));
        Scene scene = new Scene(root,1200,800);
        primaryStage.setScene(scene);
        primaryStage.show();
        if(darkModeManager.isDarkMode()) {
            darkModeManager.darkModeActivation(scene);
        }
        else {
            darkModeManager.darkModeDesactivation(scene);
        }
    }
    @FXML
    public void darkMode() {
        if (darkModeMenuSelection.isSelected()) {
            darkModeManager.darkModeActivation(recupScene.getScene());
        } else {
            darkModeManager.darkModeDesactivation(recupScene.getScene());
        }
    }





}
