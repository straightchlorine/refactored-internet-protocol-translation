package ip.translation.input.control;

import ip.translation.input.exceptions.SegmentLengthException;
import ip.translation.input.exceptions.StringLengthException;
import java.lang.Exception;

/**
 * Utility class responsible for basic validation. 
 *
 * It checks the length of the string (max. 12 digits + 3 dots)
 * and the length of each segment, rejecting most of obvious invalid
 * user input.
 *
 */
public class InitialInputControl {
   
    public static void main(String[] args) {            // testing
        if (validate("192.163.111.1231"))
            System.out.println("address is valid");
        else System.out.println("addresss is invalid");
    }
  
    /**
     * Function checks both scenarios i.e.:
     *          a) string as a whole too long (greater than 15)
     *          b) segment exceeds length of 3
     * Employs try-catch and throws an adequate exception.
     *
     * @param input     user input
     */
    public static boolean validate(String input) {
        try {
            if (!validateAddress(input))
                throw new StringLengthException();
        
            if (!validateSegment(input))
                throw new SegmentLengthException();
        } catch (Exception e) {
            e.getMessage();
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
     * if and only if the current character is a dot. If counter exceeds 
     * the required value, loop breaks.
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
}
