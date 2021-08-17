package ip.translation.input.exceptions;

import java.lang.Exception;

/**
 * Exception thrown in case of invalid length of the address provided
 * by the user.
 *
 * @see ip.translation.input.control.InitialInputControl
 */
public class StringLengthException extends Exception {
    public StringLengthException() {
        super("Length of the address is not valid.");
    }
}
