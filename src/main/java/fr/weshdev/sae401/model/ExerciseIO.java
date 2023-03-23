package fr.weshdev.sae401.model;

import javafx.scene.image.Image;
import javafx.scene.media.Media;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ExerciseIO {

    private int indexTranscription = 0;

    public void build(String filePath, Exercise exercise) throws IOException {
        List<String> lines = new LinkedList<>(Arrays.asList(
                Crypter.encrypt(exercise.getOrder()),
                Crypter.encrypt(exercise.getTranscription()),
                "transcription",
                Crypter.encrypt(exercise.getHint()),
                Crypter.encrypt(exercise.getReplacingChar()),
                Crypter.encrypt(exercise.getTimer())
        ));

        String mediaPath = exercise.getMediaPath();
        String extension = mediaPath.substring(mediaPath.lastIndexOf(".") + 1);
        String imagePath = exercise.getImagePath();

        writeFirstLines(lines, filePath);
        writeLine(exercise.getOptions().size() + "", filePath, true);
        writeOptions(exercise.getOptions(), filePath);
        writeLine(extension, filePath, true);
        writeMedia(mediaPath, filePath);
        if(imagePath != null && !imagePath.isEmpty()){
            writeMedia(imagePath, filePath);
        }
    }

    public Exercise get(String filePath) throws IOException {
        Exercise exercise = new Exercise();

        indexTranscription = getIndex(filePath, "transcription");

        exercise.setOrder(Crypter.decrypt(readLine(filePath, 0)));
        exercise.setTranscription(makeTranscription(filePath, indexTranscription));
        exercise.setHint(Crypter.decrypt(readLine(filePath, indexTranscription + 1)));
        exercise.setReplacingChar(Crypter.decrypt(readLine(filePath, indexTranscription + 2)));
        exercise.setTimer(Crypter.decrypt(readLine(filePath, indexTranscription + 3)));

        exercise.setOptions(readOptions(filePath));

        System.out.println(exercise.getTranscription());

        String extension = readLine(filePath, indexTranscription + 6 + exercise.getOptions().size());
        MediaType mediaType = switch (extension) {
            case "mp3" -> MediaType.AUDIO;
            case "png" -> MediaType.IMAGE;
            default -> MediaType.VIDEO;
        };

        exercise.setMedia((Media) readMedia(filePath, 0, mediaType).get(0));

        if(mediaType == MediaType.AUDIO){
            exercise.setImage((Image) readMedia(filePath, 1, mediaType).get(0));
        }

        return exercise;
    }

    private String makeTranscription(String filePath, int indexTranscription) {
        List<String> lines = readLines(filePath, 1, indexTranscription);
        StringBuilder transcription = new StringBuilder();
        int i = 0;
        for (String line : lines) {
            if (i == 0) {
                transcription.append(Crypter.decrypt(line));
            } else {
                transcription.append('\n').append(Crypter.decrypt(line));
            }
            i++;
        }
        return transcription.toString();
    }

    private int getIndex(String filePath, String toFind) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);
        String str;
        int index = 0;
        while ((str = reader.readLine()) != null) {
            if (str.equals(toFind)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    private void writeOptions(Map<String, Option> options, String filePath) {
        StringBuilder optionsString = new StringBuilder();
        for (Map.Entry<String, Option> entry : options.entrySet()) {
            optionsString.append(Crypter.encrypt(entry.getKey()))
                    .append(";")
                    .append(Crypter.encrypt(entry.getValue().getName()))
                    .append(";")
                    .append(Crypter.encrypt(entry.getValue().getDescription()))
                    .append(";")
                    .append(Crypter.encrypt(entry.getValue().isActive() + ""));
            writeLine(optionsString.toString(), filePath, true);
            optionsString.setLength(0);
        }
    }

    private HashMap<String, Option> readOptions(String filePath){
        HashMap<String, Option> options = new HashMap<>();
        List<String> lines;
        int nbOptions = Integer.parseInt(readLine(filePath, indexTranscription + 4));
        lines = readLines(filePath, indexTranscription + 5, indexTranscription + 5 + nbOptions);
        for(String line : lines){
            String[] split = line.split(";");
            options.put(Crypter.decrypt(split[0]), new Option(Crypter.decrypt(split[1]), Crypter.decrypt(split[2]), Boolean.parseBoolean(Crypter.decrypt(split[3]))));
        }
        return options;
    }

    private void writeFirstLines(List<String> lines, String path){
        writeLine(lines.remove(0), path, false);
        for(String line : lines){
            writeLine(line, path, true);
        }
    }

    private void writeLine(String line, String path, boolean appendMode){
        try (PrintWriter pw = new PrintWriter(new FileWriter(path, StandardCharsets.UTF_8, appendMode))) {

            pw.append(line).append("\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeMedia(String mediaPath, String fileName) throws IOException {
        FileInputStream input = new FileInputStream(mediaPath);
        byte[] data = input.readAllBytes();
        FileOutputStream output = new FileOutputStream(fileName, true);
        writeLine("<<<MEDIA", fileName, true);
        output.write(data);
        writeLine("\nMEDIA>>>", fileName, true);
        output.close();
    }

    private void writeMediaInTempFile(File fileName, byte[] data) throws IOException {
        FileOutputStream output = new FileOutputStream(fileName, true);
        output.write(data);
        output.close();
    }

    private String readLine(String filePath, int line){
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

        return "";
    }

    private List<String> readLines(String path, int startLine, int endLine){
        List<String> lines = new ArrayList<>();
        for(int i = startLine; i < endLine; i++){
            lines.add(readLine(path, i));
        }
        return lines;
    }

    private byte[] getMediaFromTextFile(String filePath, int mediaPosition) throws IOException {
        Path path = Paths.get(filePath);
        byte[] fileBytes = Files.readAllBytes(path);
        return extractBytesAfterMediaString(fileBytes, mediaPosition);
    }

    private byte[] extractBytesAfterMediaString(byte[] bytes, int mediaPosition) {
        String beginMediaString = "<<<MEDIA";
        String endMediaString = "MEDIA>>>";
        byte[] beginMediaBytes = beginMediaString.getBytes();
        byte[] endMediaBytes = endMediaString.getBytes();
        int beginMediaIndex = indexOfBytes(bytes, beginMediaBytes, mediaPosition);
        int endMediaIndex = indexOfBytes(bytes, endMediaBytes, mediaPosition);
        if (beginMediaIndex == -1) {
            throw new IllegalArgumentException("La chaîne <<<MEDIA est introuvable dans le tableau de bytes.");
        } else if (endMediaIndex == -1) {
            throw new IllegalArgumentException("La chaîne MEDIA>>> est introuvable dans le tableau de bytes.");
        }
        return Arrays.copyOfRange(bytes, beginMediaIndex + beginMediaBytes.length + 1, bytes.length - endMediaBytes.length - 1);
    }

    private int indexOfBytes(byte[] source, byte[] pattern, int mediaPosition) {
        int[] failure = computeFailure(pattern);
        int j = 0;
        int index = -1;

        for (int k = 0; k < mediaPosition; k++) {
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
                    index = i - pattern.length + 1;
                    j = 0;
                    break;
                }
            }
        }
        return index;
    }


    private int[] computeFailure(byte[] pattern) {
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
     * @param mediaType the type of the media
     *
     * @return the media as a list of Media or Image
     */
    private List<?> readMedia(String filePath, int mediaPosition, MediaType mediaType) throws IOException {
        byte[] mediaBytes = getMediaFromTextFile(filePath, mediaPosition+1);
        String extension = switch (mediaType) {
            case VIDEO -> "mp4";
            case AUDIO -> "mp3";
            case IMAGE -> "png";
        };

        File tempFile = File.createTempFile("temp", "." + extension);
        tempFile.deleteOnExit();
        writeMediaInTempFile(tempFile, mediaBytes);

        if(mediaType == MediaType.IMAGE) {
            return List.of(new Image(tempFile.toURI().toString()));
        }

        return List.of(new Media(tempFile.toURI().toString()));
    }

}
