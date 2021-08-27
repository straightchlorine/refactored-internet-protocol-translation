package ip.translation.input.exceptions;

import java.lang.Exception;

/**
 * Exception thrown in case of detecting a segment of an address with a value 
 * that exceeds 255.
 *
 * @see ip.translation.input.control.PreciseInputControl
 */
public class SegmentValueException extends Exception {
    public SegmentValueException() {
        super("At least one segment contains a value greater than 255.");
    }
}
