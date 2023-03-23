package fr.weshdev.sae401.model;

public abstract class Crypter {
    private static final int ENCRYPT_OFFSET = 3;

    public static String encrypt(String text){
        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            encrypted.append((char) (text.charAt(i) + ENCRYPT_OFFSET));
        }
        return encrypted.toString();
    }

    public static String decrypt(String text){
        StringBuilder decrypted = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            decrypted.append((char) (text.charAt(i) - ENCRYPT_OFFSET));
        }
        return decrypted.toString();
    }
}
