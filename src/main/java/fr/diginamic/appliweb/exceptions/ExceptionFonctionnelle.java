package fr.diginamic.appliweb.exceptions;

public class ExceptionFonctionnelle extends Exception {
    public ExceptionFonctionnelle(String message) {
        super(message);
    }

    public ExceptionFonctionnelle(String message, Throwable cause) {
        super(message, cause);
    }
}
