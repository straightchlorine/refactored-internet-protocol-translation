package ip.translation.input.exceptions;

import java.lang.Exception;

/**
 * Exception thrown in case of invalid length of the segment provided
 * by the user.
 *
 * @see ip.translation.input.control.InitialInputControl
 */
public class SegmentLengthException extends Exception {
    public SegmentLengthException() {
        super("Values in segments are not valid.");
    }
}
