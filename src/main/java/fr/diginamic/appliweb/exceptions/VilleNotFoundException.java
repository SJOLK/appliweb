package fr.diginamic.appliweb.exceptions;

public class VilleNotFoundException extends RuntimeException {

    public VilleNotFoundException() {
        super();
    }

    public VilleNotFoundException(String message) {
        super(message);
    }

    public VilleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public VilleNotFoundException(Throwable cause) {
        super(cause);
    }
}
