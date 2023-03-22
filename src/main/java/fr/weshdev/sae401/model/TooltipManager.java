package fr.weshdev.sae401.model;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

import javax.swing.text.html.ImageView;

public class TooltipManager {

    public void tips(Node noeud,String text) {
        Tooltip tooltip = new Tooltip();
        tooltip.setText(text);
        tooltip.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white; -fx-font-size: 15px;");
        tooltip.setShowDelay(Duration.millis(0));
        tooltip.setShowDuration(Duration.millis(5000));
        Tooltip.install(noeud, tooltip);
    }
}
