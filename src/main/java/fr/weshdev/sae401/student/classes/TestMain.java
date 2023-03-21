package fr.weshdev.sae401.student.classes;

import javafx.scene.media.Media;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestMain {

    static String exerciseOrder = "éééééééçççççççç";
    static String exerciseTranscription = "ààààààààààñññ";
    String exerciseHint = "éééééééççççççççà";
    String hiddenChar = "{";
    boolean isCaseSensitive = false;
    boolean isEvaluation = true;
    int time = 10;
    boolean isVideo = true;
    static String videoPath = "C:\\Users\\basti\\Downloads\\modamahugepulse - swim in a sea full of quarks.mp3";
    String imagePath = "C:\\Users\\basti\\Pictures\\4dmwc_roster_reveal-00;00;10;25.png";

    static String fileName = "test";
    static String directoryPath = "C:\\Users\\basti\\Downloads\\";
    static String newExercisePath = directoryPath + fileName + ".rct";

    public static void main(String[] args) throws IOException {
        System.out.println("Création du fichier...");
        ArrayList<String> lines = new ArrayList<>();
        lines.add(exerciseOrder);
        lines.add(exerciseTranscription);

        writeFirstLines(lines, newExercisePath);
        writeMedia(videoPath, newExercisePath);

        System.out.println("Fichier créé !");

        String s = readLine(newExercisePath, 1);
        Media m = readMedia(newExercisePath, 0);

        System.out.println(s);
        System.out.println(m.getMetadata().toString());
    }

    public static void writeLines(List<String> lines, String path){
        for(String line : lines){
            writeLine(line, path, true);
        }
    }

    public static void writeFirstLines(List<String> lines, String path){
        writeLine(lines.remove(0), path, false);
        for(String line : lines){
            writeLine(line, path, true);
        }
    }

    public static void writeLine(String line, String path, boolean appendMode){
        try (PrintWriter pw = new PrintWriter(new FileWriter(path, StandardCharsets.UTF_8, appendMode))) {

            pw.append(line).append("\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeMedia(String mediaPath, String fileName) throws IOException {
        FileInputStream input = new FileInputStream(mediaPath);
        byte[] data = input.readAllBytes();
        FileOutputStream output = new FileOutputStream(fileName, true);
        writeLine("<<<MEDIA", fileName, true);
        output.write(data);
//        writeLine("\nMEDIA>>>", fileName, true);
        output.close();
    }

    public static void writeMediaInTempFile(File fileName, byte[] data) throws IOException {
        FileOutputStream output = new FileOutputStream(fileName, true);
        output.write(data);
        output.close();
    }

    public static String readLine(String filePath, int line){
        try (FileInputStream fis = new FileInputStream(filePath);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)
        ) {

            String str;
            while ((str = reader.readLine()) != null && line > 0) {
                line--;
            }

            return str;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String readLines(String path, int startLine, int endLine){
        StringBuilder lines = new StringBuilder();
        for(int i = startLine; i < endLine; i++){
            lines.append(readLine(path, i));
        }
        return lines.toString();
    }

    public static byte[] getMP3FromTextFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] fileBytes = Files.readAllBytes(path);
        return extractBytesAfterMediaString(fileBytes);
    }

    public static byte[] extractBytesAfterMediaString(byte[] bytes) {
        String mediaString = "<<<MEDIA";
        byte[] mediaBytes = mediaString.getBytes();
        int index = indexOfBytes(bytes, mediaBytes);
        if (index == -1) {
            throw new IllegalArgumentException("La chaîne <<<MEDIA est introuvable dans le tableau de bytes.");
        }
        return Arrays.copyOfRange(bytes, index + mediaBytes.length + 1, bytes.length);
    }

    private static int indexOfBytes(byte[] source, byte[] pattern) {
        int[] failure = computeFailure(pattern);

        int j = 0;
        if (source.length == 0) {
            return -1;
        }

        for (int i = 0; i < source.length; i++) {
            while (j > 0 && pattern[j] != source[i]) {
                j = failure[j - 1];
            }
            if (pattern[j] == source[i]) {
                j++;
            }
            if (j == pattern.length) {
                return i - pattern.length + 1;
            }
        }
        return -1;
    }

    private static int[] computeFailure(byte[] pattern) {
        int[] failure = new int[pattern.length];

        int j = 0;
        for (int i = 1; i < pattern.length; i++) {
            while (j > 0 && pattern[j] != pattern[i]) {
                j = failure[j - 1];
            }
            if (pattern[j] == pattern[i]) {
                j++;
            }
            failure[i] = j;
        }

        return failure;
    }

        /**
         * Read the media from the file
         * @param filePath
         * @param mediaPosition the position of the media in the file. 0 = first media, 1 = second media, etc...
         */
    public static Media readMedia(String filePath, int mediaPosition) throws IOException {
        byte[] mp3Bytes = getMP3FromTextFile(filePath);
        File tempFile = File.createTempFile("temp", ".mp3");
        tempFile.deleteOnExit();
        writeMediaInTempFile(tempFile, mp3Bytes);

        System.out.println(tempFile.toURI());

        return new Media(tempFile.toURI().toString());
    }
}
