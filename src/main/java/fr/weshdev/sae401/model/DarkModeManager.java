package fr.weshdev.sae401.model;

import fr.weshdev.sae401.student.controllers.MenuController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;

import java.awt.*;

public class DarkModeManager {

    private static boolean darkMode = false;

    public static boolean isDarkMode() {
        return darkMode;
    }

    public static void setDarkMode(boolean darkMode) {
        DarkModeManager.darkMode = darkMode;
    }
    public void darkModeActivation(Scene scene){

        scene.getStylesheets().removeAll(
                getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
        scene.getStylesheets()
                .addAll(getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
        setDarkMode(true);

    }
    public void darkModeDesactivation(Scene scene) {


        scene.getStylesheets().removeAll(
                getClass().getResource("/fr.weshdev.sae401/css/darkMode.css").toExternalForm());
        scene.getStylesheets().addAll(
                getClass().getResource("/fr.weshdev.sae401/css/menu_and_button.css").toExternalForm());
        setDarkMode(false);
    }
}
