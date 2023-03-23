package fr.weshdev.sae401.model;

import javafx.scene.image.Image;
import javafx.scene.media.Media;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an exercise.<br>
 * An exercise have:<br>
 * <ul>
 *     <li>An order</li>
 *     <li>A hint</li>
 *     <li>A transcription</li>
 *     <li>A replacing char</li>
 *     <li>A timer</li>
 *     <li>A media path</li>
 *     <li>Options</li>
 * </ul>
 *
 * Optional (if it is a mp3):
 * <ul>
 *     <li>An image path</li>
 * </ul>
 *
 * @author Weshdev
 */
public class Exercise {

    private String order;
    private String hint;
    private String transcription;
    private String replacingChar;
    private String timer;

    private String mediaPath;

    private String imagePath;

    private Media media;

    private Image image;

    private HashMap<String, Option> options;

    public Exercise() {
        this.order = "";
        this.hint = "";
        this.transcription = "";
        this.replacingChar = "";
        this.timer = "";
        this.mediaPath = "";
        this.imagePath = "";
        this.media = null;
        this.image = null;
        this.options = new HashMap<>();
    }

    public Exercise(String order, String hint, String transcription, String replacingChar, String timer, String mediaPath, String imagePath, HashMap<String, Option> options) {
        this.order = order;
        this.hint = hint;
        this.transcription = transcription;
        this.replacingChar = replacingChar;
        this.timer = timer;
        this.mediaPath = mediaPath;
        this.imagePath = imagePath;
        this.options = options;
    }

    public void build(String filePath) throws IOException {
        new ExerciseIO().build(filePath, this);
    }

    public Exercise get(String filePath) throws IOException {
        return new ExerciseIO().get(filePath);
    }

    //Getters
    public String getOrder() {
        return order;
    }

    public String getHint() {
        return hint;
    }

    public String getTranscription() {
        return transcription;
    }

    public String getReplacingChar() {
        return replacingChar;
    }

    public String getTimer() {
        return timer;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public HashMap<String, Option> getOptions() {
        return options;
    }

    public Media getMedia() {
        return media;
    }

    public Image getImage() {
        return image;
    }

    //Setters
    public void setOrder(String order) {
        this.order = order;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public void setReplacingChar(String replacingChar) {
        this.replacingChar = replacingChar;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setOptions(HashMap<String, Option> options) {
        this.options = options;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public void setImage(Image image) {
        this.image = image;
    }

}
