package fr.weshdev.sae401.student.controllers;

import fr.weshdev.sae401.DeplacementFenetre;
import fr.weshdev.sae401.model.DarkModeManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class RegisterController {
        DarkModeManager darkModeManager = new DarkModeManager();

    public void popUpUserRegister() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fr.weshdev.sae401/templates/student/save_after_open.fxml"));
        Stage stage = new Stage();
        Rectangle rect = new Rectangle(900,500);
        rect.setArcHeight(20.0);
        rect.setArcWidth(20.0);
        root.setClip(rect);


        //On bloque sur cette fen�tre
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root, 900, 500);
        scene.setFill(Color.TRANSPARENT);



        //On bloque le resize
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        DeplacementFenetre.deplacementFenetre((Pane) root, stage);
        if(darkModeManager.isDarkMode()){
            darkModeManager.darkModeActivation(scene);
        }
        else {
            darkModeManager.darkModeDesactivation(scene);
        }
    }
}
