package ip.translation.input.control;

import ip.translation.input.processing.Segmentation;
import ip.translation.input.exceptions.SegmentValueException;

import java.util.List;

/**
 * Utility class responsible for validating segment value after 
 * the segmentation process.
 *
 * Simply checks whether each value does not exceed 255.
 */
public final class PreciseInputControl {

    /**
     * Contructor
     */
    private Segmentation() {}

    public static void main(String[] args) {
        String testIP = "192.1688.1.1";
        validate(Segmentation.segment(testIP));
    }

    /**
     * Method iterates over the list of segments and checks if each of 
     * them does not exceed the value of 255.
     * 
     * If it does, the SegmentValueException is thrown.
     *
     * @see                     ip.translation.input.exceptions.SegmentValueException
     * @param segments          result of segmenting the address
     */
    public static boolean validate(List<Integer> segments) {
        try {
            for (Integer current : segments)
                if (!(current >= 0 && current <= 255))
                    throw new SegmentValueException();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}
