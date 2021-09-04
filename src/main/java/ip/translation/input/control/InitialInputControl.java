package ip.translation.input.control;

import ip.translation.input.exceptions.UnexpectedCharacterException;
import ip.translation.input.exceptions.StringLengthException;
import ip.translation.input.exceptions.SegmentLengthException;

import java.lang.Exception;
import java.util.regex.Pattern;

/**
 * Utility class responsible for basic validation. 
 *
 * Most importantly it checks whether given input can indeed be extracted
 * to Integers i.e does not contain any characters beside dots and digits 0-9.
 * It also checks the length of the string (max. 12 digits + 3 dots)
 * and the length of each segment, rejecting most of obvious invalid
 * user input.
 *
 */
public final class InitialInputControl {
  
    /**
     * Constructor
     */
    public static void main(String[] args) {            // testing
        validate("-92.163.111.123");
    }
  
    /**
     * Function checks both scenarios i.e.:
     *          a) string contains any characters different from digits 0-9 and dots 
     *          b) string as a whole too long (greater than 15)
     *          c) segment exceeds length of 3
     * Employs try-catch and throws an adequate exception.
     *
     * @param input     user input
     * @return          true, if the validation
     */
    public static boolean validate(String input) {
        try {
            if (!validatePattern(input))
                throw new UnexpectedCharacterException(); 

            if (!validateAddress(input))
                throw new StringLengthException();
        
            if (!validateSegment(input))
                throw new SegmentLengthException();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Convenience function to make {@code validate(String input)} more readable.
     *
     * @param input     user input
     * @return          true, if validation succeeded
     */
    private static boolean validateAddress(String input) { 
        return input.length() <= 15;
    }

    /**
     * Function counting each cell in the array, while resetting the counter
     * if and only if the current character is a dot.
     *
     * If counter exceeds the required value, loop breaks.
     *
     * @param input     user input
     * @return          true, if validation succeeded
     */
    private static boolean validateSegment(String input) {
        char[] arrayInput = input.toCharArray();

        for(int i = 0, inSegment = 0; i < arrayInput.length; i++, inSegment++) {

            if(arrayInput[i] == '.') 
                inSegment = -1;
                
            if(inSegment > 2)
                return false;
        }

        return true;
    }
    
    /**
     * Convenience method.
     *
     * @param input     user input
     * @return          true, if the input matches the pattern i.e. consists only of digits 0-9 and dots
     */
    private static boolean validatePattern(String input) {
        return Pattern.matches("(([0-9]+)\\.)+([0-9]+)", input);
    }
}
