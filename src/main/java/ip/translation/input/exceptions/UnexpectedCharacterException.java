package ip.translation.input.exceptions;

import java.lang.Exception;

/**
 * Exception thrown in case pattern ensuring that input is just numbers
 * and dots fails.
 *
 * @see ip.translation.input.control.InitialInputControl
 */
public class UnexpectedCharacterException extends Exception {
    public UnexpectedCharacterException() {
        super("Address contains invalid characters");
    }
}
