package ip.translation;
import java.util.List;

/**
 *  <h1>ToNumberSystem class</h1>
 *  Class, storing methods providing calculations and conversions.
 *
 *  @author  Piotr Lis
 *  @version 1.0, 2019-11-18
 *  @since 1.8.0
 */
abstract class ToNumberSystem {
    /**
     * The method converts the number expressed in decimal system to binary system.
     *
     * @param toBeConverted    the Integer holding value, which is meant to be converted by the method
     * @return String, holding the {@code toBeConverted} variable converted to binary
     */
    static String decimalToBinary(Integer toBeConverted) {
        if(toBeConverted == 0) return ifLowerThanOctet(toBeConverted.toString());
        String binaryForm = "";
        int currentQuotient = toBeConverted;

        StringBuilder builder = new StringBuilder();
        while(currentQuotient > 0) {                // Conversion to binary in reversed order
            int remainder = currentQuotient % 2;
            builder.append(remainder);
            binaryForm = builder.toString();
            currentQuotient /= 2;
        }

        char[] newOrder = new char[binaryForm.length()];    // Array, holding the chars in the right order
        char[] buffer = binaryForm.toCharArray();
        for(int i = 0, j = binaryForm.length() - 1; i < binaryForm.length(); i++, j--) {
            newOrder[i] = buffer[j];            // Reversing the order
        }

        builder.setLength(0);   // Resetting the StringBuilder object

        for (char current : newOrder)   // Assigning the right order from the array to class parameter
            builder.append(current);
        binaryForm = builder.toString();

        return ifLowerThanOctet(binaryForm);    // Verifying if the binary data form an octet
    }

    /**
     * The method converts given String variable, holding binary value into decimal value.
     *
     * @param binaryForm    binary value, meant to be converted
     *
     * @return Integer, the converted to decimal {@code String binaryForm}
     */
    static Integer binaryToDecimal(String binaryForm) {
        char[] binary = binaryForm.toCharArray();
        int decimalFinalResult = 0;
        int index = 7;
        for(int i = 0; i < binary.length; i++, index--) {
            if(binary[i] == '1')
                decimalFinalResult += toThePower(2, index);
        }
        return decimalFinalResult;
    }

    /**
     * The method carries out AND operation on given binary segments and.
     *
     * @param addressSegment    binary segment of the address
     * @param maskSegment       binary segment of currently used mask
     *
     * @return String, single binary number after the calculation
     */
    static String IPv4SegmentBinaryAND(String addressSegment, String maskSegment) {
        StringBuilder binaryAND = new StringBuilder();
        char[] digitsFirstFactor = addressSegment.toCharArray();
        char[] digitsSecondFactor = maskSegment.toCharArray();
        for(int i = 0; i < 8; i++) {
            Integer numericFirstFactor = Character.getNumericValue(digitsFirstFactor[i]);
            Integer numericSecondFactor = Character.getNumericValue(digitsSecondFactor[i]);
            Integer numericProduct = numericFirstFactor * numericSecondFactor;
            binaryAND.append(numericProduct);
        }
        return ifLowerThanOctet(binaryAND.toString());
    }

    /**
     * The method carries out the OR operation on given binary segment and verifies if the result is an octet
     *
     * @param addressSegment    binary segment of the address
     * @param maskSegment       binary segment of currently used mask
     * @return String, single binary number after the calculation
     */
    static String IPv4SegmentBinaryOR(String addressSegment, String maskSegment) {
        StringBuilder binaryOR = new StringBuilder();
        char[] addressDigits = addressSegment.toCharArray();
        char[] maskDigits = maskSegment.toCharArray();
        for(int i = 0; i < 8; i++) {
            if((addressDigits[i] == '0') && (maskDigits[i] == '0'))
                binaryOR.append("0");
            else binaryOR.append("1");
        }
        return ifLowerThanOctet(binaryOR.toString());
    }

    /**
     * The method carries out the NOT operation on given binary segment and verifies if the result is an octet
     *
     * @param segment    binary segment meant to be negated
     * @return String, single binary number after the calculation
     */
    static String IPv4SegmentBinaryNOT(String segment) {
        StringBuilder binaryNOT = new StringBuilder();
        segment = ifLowerThanOctet(segment);
        char[] digits = segment.toCharArray();
        for(int i = 0; i < 8; i++) {
            if(digits[i] == '1') binaryNOT.append("0");
            if(digits[i] == '0') binaryNOT.append("1");
        }
        return ifLowerThanOctet(binaryNOT.toString());
    }

    /**
     * Method receives List of {@link Character} class objects and combines them into {@link Integer} class object.
     *
     * @param rawData    list of Characters, which are going to be converted
     *
     * @return Integer - the number out of given list of Characters
     */
    static Integer charListToInteger(List<Character> rawData) {
        int finalResult = 0;
        int factor = toThePower(10, rawData.size() - 1);
        for(Character current : rawData) {
            finalResult += Character.getNumericValue(current) * factor;
            factor /= 10;
        }
        return finalResult;
    }

    /**
     * The method uses a simple algorithm, decreasing the number of calculations by half.
     *
     * @param number base of the power.
     * @param index  index of the power.
     *
     * @return given number to the power of index
     */
    static int toThePower(int number, int index) {
        if(index == 0) return 1;
        if(index == 1) return number;
        Integer currentPower = toThePower(number, index / 2);
        if(index % 2 == 0)
            return currentPower * currentPower;
        else
            return number * currentPower * currentPower;
    }

    /**
     * The method checks if the given value is an octet - if so it returns it without any changes, although if it is not
     * it adds 0's before the number. It changes the length of the expression, but does not change the value.
     *
     * @param binaryValue    binary value, meant to verified
     *
     * @return String, the binary value ofter verification
     */
    private static String ifLowerThanOctet(String binaryValue) {
        StringBuilder builder = new StringBuilder();
        if(binaryValue.length() <= 8) {
            for(int i = 0; i < 8 - binaryValue.length(); i++)
                builder.append("0");
            builder.append(binaryValue);
        }
        return builder.toString();
    }
}