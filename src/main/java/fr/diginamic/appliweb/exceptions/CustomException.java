package fr.diginamic.appliweb.exceptions;

public class CustomException extends Exception {

    // Constructeur avec message
    public CustomException(String message) {
        super(message);
    }

    // Optionnel : constructeur avec message et cause
    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }
}