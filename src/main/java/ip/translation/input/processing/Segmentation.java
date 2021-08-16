import java.util.LinkedList;
import java.util.ArrayList;
/**
 * Utility class responsible for the processing of the user input.
 *
 */
public final class Segmentation {
    
    /**
     * Constructor
     */
    private Segmentation() {}

    public static void main(String[] args) {
        segment("192.168.1.1"); 
    }

    /**
     * Divides user input into four segments, removes the dots and 
     * populates integer LinkedList
     * 
     * @param input     user's input
     * @return          list of segments, as integers
     */
    static LinkedList<Integer> segment(String input) {
        char[] arrayInput = input.toCharArray();
        LinkedList<Integer> segments = new LinkedList<>();              // final list of segments

        ArrayList<Character> buffer = new ArrayList<>();                // data buffer for each segment 
        for(int i = 0; i < arrayInput.length; i++) {
            boolean lastSegment = (i == (arrayInput.length - 1));

            if(arrayInput[i] == '.' || lastSegment ) {

                if(lastSegment) {                                       // case for the last segment
                   buffer.add(arrayInput[i]);
                }       

                Character[] arr = new Character[buffer.size()];         // cast an ArrayLink to regular array
                arr = buffer.toArray(arr);

                segments.add(charArrayToInteger(arr));
                buffer = new ArrayList<>();                             // clear the buffer

            } else {
                buffer.add(arrayInput[i]);
            }

        }            
        return segments; 
    }

    /**
     * Converts an array of chars into int
     *
     * @ param array    array, which is to be converted
     * @return          integer, derived from the array of chars
     */
    public static Integer charArrayToInteger(Character[] array) {
        int result = 0;

        for(int i = 0; i < array.length; i++) {
            int digit = array[i] - '0';
            result *= 10;
            result += digit;
        }

        return result;
    }

}
