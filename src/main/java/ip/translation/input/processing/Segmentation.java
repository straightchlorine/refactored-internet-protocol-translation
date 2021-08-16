import java.util.LinkedList;
/**
 * Class responsible for processing of the user input.
 *
 * @return      
 * */
public final class Segmentation {
   
    private Segmentation() {}

    public static void main(String[] args) {
        segment("192.168.1.1"); 
    }

    static LinkedList<Integer> segment(String input) {
        char[] arrayInput = input.toCharArray();
        LinkedList<Integer> segments = new LinkedList<>(); 

       
        char[] buffer = new char[3]; 
        for(int i = 0, s_index = 0; i < arrayInput.length; i++) {

            if(arrayInput[i] == '.') {

                buffer = new char[3];
                s_index = 0;

            } else {
                buffer[s_index] = arrayInput[i];
                s_index++;          
            }

        }            

       return segments; 
    }

    public static Integer charArrayToInteger(char[] array) {
        int result = 0;

        for(int i = 0; i < array.length; i++) {
            int digit = array[i] - '0';
            result *= 10;
            result += digit;
        }

        return result;
    }

}
