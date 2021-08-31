package ip.translation.input;

import ip.translation.input.processing.Segmentation;
import ip.translation.input.control.PreciseInputControl;
import ip.translation.input.control.InitialInputControl;

import java.util.LinkedList;
import java.util.Scanner;

/**
 * Utility class, which reads input from user, verfifies it and returns 
 * processed input, that is LinkedList of segments.
 */
public final class InputModule {
    
    /**
     * Constructor
     */
    private InputModule() {}

    public static void main(String[] args) {}

    /**
     * Method receives input from the user, and verifies if
     * given output is valid.
     *
     * @return Integer list of segments
     */
    public static LinkedList<Integer> read() {
        LinkedList<Integer> segments = null;

        while (true) {

            String input = readInput();

            if(InitialInputControl.validate(input))
                segments = Segmentation.segment(input);
                if (PreciseInputControl.validate(segments))
                    break;
        }
        return segments;
    }

    /**
     * Convenience method, which gets user input via scanner.
     *
     * @return user input
     */
    private static String readInput() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }
}
