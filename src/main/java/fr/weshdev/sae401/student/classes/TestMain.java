package fr.weshdev.sae401.student.classes;

import fr.weshdev.sae401.model.Exercise;
import fr.weshdev.sae401.model.Option;

import java.io.IOException;
import java.util.HashMap;

public class TestMain {
    public static void main(String[] args) throws IOException {
        Exercise exercise = new Exercise();
        exercise.setOrder("***$$$§§§");
        exercise.setTranscription("éééçççààà");
        exercise.setHint("££££$$$¤¤");
        exercise.setReplacingChar("€€€");
        exercise.setTimer("timer");
        exercise.setMediaPath("C:\\Users\\basti\\Downloads\\modamahugepulse - swim in a sea full of quarks.mp4");

        HashMap<String, Option> options = generateOptions();
        exercise.setOptions(options);

        exercise.build("C:\\Users\\basti\\Desktop\\test.rct");

        Exercise newExercise = new Exercise().get("C:\\Users\\basti\\Desktop\\test.rct");
        System.out.println(newExercise.getOrder());
        System.out.println(newExercise.getTranscription());
        System.out.println(newExercise.getHint());
        System.out.println(newExercise.getReplacingChar());
        System.out.println(newExercise.getTimer());
        System.out.println(newExercise.getMediaPath());
        System.out.println(newExercise.getMedia().getSource());
        System.out.println(newExercise.getOptions().get("caseSensitiveOption").getName());
    }

    public static HashMap<String, Option> generateOptions(){
        HashMap<String, Option> options = new HashMap<>();

        Option caseSensitiveOption = new Option("Case sensitive", "Permet de rendre sensible à la casse", false);
        Option evaluationOption = new Option("Evaluation", "Permet de faire une évaluation", false);
        Option trainingOption = new Option("Training", "Permet de faire de l'entrainement", false);
        Option solutionShowOption = new Option("SolutionShow", "Permet d'afficher la solution", false);
        Option discoveredWordRateProgressBarOption = new Option("Progress bar", "Permet d'afficher la barre de progression", false);
        Option incompletedWordOption = new Option("Incompleted word", "Permet de faire des mots incomplets", false);
        Option incompletedWordWithTwoLettersOption = new Option("Incompleted word with two letters", "Permet de faire des mots incomplets avec deux lettres", false);
        Option incompleteWordWithThreeLettersOption = new Option("Incompleted word with three letters", "Permet de faire des mots incomplets avec trois lettres", false);

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

}
