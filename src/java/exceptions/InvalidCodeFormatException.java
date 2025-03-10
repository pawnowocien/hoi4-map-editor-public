package src.java.exceptions;

import java.io.File;

public class InvalidCodeFormatException extends Exception {
    public InvalidCodeFormatException(String message, File file) {
        super(String.format("%s (%s)", message, file.getAbsolutePath()));
    }
}
