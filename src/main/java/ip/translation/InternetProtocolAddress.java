package ip.translation;
import java.util.*;
import static ip.translation.ToNumberSystem.*;
/**
 *  <h1>InternetProtocolAddressNotValidException</h1>
 *  Exception, in case the address is not valid, inherited after Exception class.
 *  @see java.lang.Exception
 *
 *  @author Piotr Lis
 *  @version 1.0, 2019-11-15
 *  @since 1.8.0
 */
class InternetProtocolAddressNotValidException extends Exception {}

/**
 *  <h1>InternetProtocolValid</h1>
 *  Exception, indicates if the problem has been dealt with, inherited after Exception class.
 *  @see java.lang.Exception
 *
 *  @author Piotr Lis
 *  @version 1.0, 2019-11-15
 *  @since 1.8.0
 */
class InternetProtocolValid extends Exception {}

/**
 * <h1>InternetProtocolAddress Class</h1>
 * The class is receiving the IP address and divides it into {@link InternetProtocolSegment} class objects.
 *
 * @author Piotr Lis
 * @version 1.0, 2019-11-15
 * @since 1.8.0
 */
class InternetProtocolAddress {
    /** IP address divided into four addressSegments */
    List<InternetProtocolSegment> addressSegments = new LinkedList<>();
    /** Internet Protocol Address provided by user */
    private String internalInputAddress;
    /** Internet Protocol Address provided by user */
    private String internalInputSubnetMask;
    /** Symbol separating the addressSegments */
    private char separator;
    /** Number of segments of which address is consisted of */
    private int validSegmentPerAddressRatio;
    /** Number of digits of which a single segment is consisted of */
    private int validSegmentSize;
    /** Number of bits each address has to be consisted of */
    int validBits;

    /**
     * Primary constructor, which simply assigns values to each field of the class.
     *
     * @param ifIPv4 boolean value, which determines version of IP.
     */
    InternetProtocolAddress(boolean ifIPv4) {
        if(ifIPv4) {
            separator = '.';
            validSegmentPerAddressRatio = 4;
            validSegmentSize = 3;
            validBits = 32;
            for(int i = 0; i < validSegmentPerAddressRatio; i++)
                addressSegments.add(new InternetProtocolSegment());
        } else {
            separator = ':';
            validSegmentPerAddressRatio = 8;
            validSegmentSize = 4;
            validBits = 128;
            for(int i = 0; i < validSegmentPerAddressRatio; i++)
                addressSegments.add(new InternetProtocolSegment());
        }
    }

    /**
     * Secondary constructor, copying fields of given {@link InternetProtocolAddress} object to the new object of the same type.
     *
     * @param IPAddress given address, which data is meant to be duplicated.
     */
    InternetProtocolAddress(InternetProtocolAddress IPAddress) {
        this.internalInputAddress = IPAddress.internalInputAddress;
        this.internalInputSubnetMask = IPAddress.internalInputSubnetMask;
        this.separator = IPAddress.separator;
        this.validSegmentPerAddressRatio = IPAddress.validSegmentPerAddressRatio;
        this.validSegmentSize = IPAddress.validSegmentSize;
        this.validBits = IPAddress.validBits;

        this.addressSegments = new LinkedList<>();
        for(InternetProtocolSegment currentSegment : IPAddress.addressSegments) {
            this.addressSegments.add(new InternetProtocolSegment(currentSegment));
        }
    }

    void addressConfiguration() {
        for (InternetProtocolSegment currentSegment : addressSegments) {
            currentSegment.getNetwork();
            currentSegment.getBroadcast();
        }
    }

    /**
     * Calls the {@code validationAndSegmentingAddressIPv4} method upon given input address.
     *
     * @param inputAddress  input address, provided by the user.
     * @param prompt        boolean value, which determines if the prompt about successful assignment will appear.
     */
    void inputAddressAssignment(String inputAddress, boolean prompt) {
        this.internalInputAddress = inputAddress;
        this.validationAndSegmentingAddressIPv4(prompt);
    }

    /**
     * The method validates and assigns values to every {@code segmentValue} field of {@link InternetProtocolSegment} object in
     * {@code addressSegments} list.
     * <p>
     * First step of verification is generation of {@code List<Integer> addressSegmentLengths} using {@code segmentScope()}
     * method, which divides given address into segments, separated by periods.
     * <p>
     * After that comes verification of said List - first is checking how many segments actually are in the Collection,
     * the size of list has to be equal to field {@code validSegmentPerAddressRatio}, else the exception is thrown.
     * Second is checking how many digits each segment consists - the size has to be within [1 - validSegmentSize] scope,
     * else the exception is thrown.
     * <p>
     * Afterwards the values are assigned to {@code segmentValue} fields in the {@link InternetProtocolSegment} objects
     * in the List, using {@code toSegments()} method.
     * <p>
     * Last part depends on processing data inside of each {@link InternetProtocolSegment} object. After reconstructing
     * {@code List<Character>} collections to integers it verifies each {@code maskValue}, stored by each segment.
     *
     * @param prompt boolean value, deciding if prompt confirming that address is valid should be displayed
     *
     * @see InternetProtocolSegment
     * @see InternetProtocolValid
     * @see InternetProtocolAddressNotValidException
     */
    private void validationAndSegmentingAddressIPv4(boolean prompt) {
        List<Integer> addressSegmentLengths;
        try {
            addressSegmentLengths = segmentScope(internalInputAddress.toCharArray());   // Generating lengths of segments
            if(addressSegmentLengths.size() != validSegmentPerAddressRatio) // Checking if the user provided valid number of segments
                throw new InternetProtocolAddressNotValidException();
            for(Integer current : addressSegmentLengths)    // Checking if the length of every segment is valid
                if ((current > validSegmentSize) || (current == 0))
                    throw new InternetProtocolAddressNotValidException();

            toSegments(internalInputAddress, true, addressSegmentLengths);  // Configuring InternetProtocolSegment objects

            for(InternetProtocolSegment segment : addressSegments)      // Creating integers out of Lists of Character class objects
                segment.segmentReconstructionIPv4(segment.rawSegment, true);
            for(InternetProtocolSegment segment : addressSegments)      // Verifying values of each segments
                segment.segmentVerificationIPv4(segment.segmentValue);
            throw new InternetProtocolValid();
        } catch (InternetProtocolAddressNotValidException exception) {
            System.out.print("\t>< Given address is invalid, enter a valid one: ");
            Scanner address = new Scanner(System.in);
            internalInputAddress = address.nextLine();
            validationAndSegmentingAddressIPv4(prompt);
        } catch (InternetProtocolValid internetProtocolValid) {
            if(prompt) {
                System.out.println("\t\t>> Address is valid");
                System.out.println();
            }
        }
    }

    /**
     * Calls the {@code validationAndSegmentingMaskIPv4} method upon given input mask.
     *
     * @param inputMask input mask, provided by the user.
     * @param prompt    boolean value, which determines if the prompt about successful assignment will appear.
     */
    void inputMaskAssignment(String inputMask, boolean prompt) {
        this.internalInputSubnetMask = inputMask;
        this.validationAndSegmentingMaskIPv4(prompt);
    }

    /**
     * Verifies which way of validation the input should undergo - masks expressed in CIDR format and masks expressed
     * in regular, decimal way need different course of verification.
     *
     * @param prompt boolean value, indicating if the prompts informing that given mask is valid should appear.
     */
    private void validationAndSegmentingMaskIPv4(boolean prompt) {
        if(ifCIDR())
            shortMaskValidationAndSegmentation(prompt);
        else
            standardMaskValidationAndSegmentation(prompt);
    }

    /**
     * Method determines the way of mask validation.
     *
     * @return  true - indicating the need of short format validation
     *          false - indicating the need of long format validation
     */
    private boolean ifCIDR() {
        if((!internalInputSubnetMask.contains(".")) && (internalInputSubnetMask.toCharArray()[0] == '/'))   // Verifying if the next input is another
            return true;
        else if ((internalInputSubnetMask.length() <= 5) && (!internalInputSubnetMask.contains("/") && (!internalInputSubnetMask.contains(".")))) {
            internalInputSubnetMask = "/" + internalInputSubnetMask;
            return true;
        }
        return false;
    }

    /**
     * The method validates and assigns values to every {@code maskValue} field of {@link InternetProtocolSegment} object in
     * {@code addressSegments} list.
     * <p>
     * First step of verification is generation of {@code List<Integer> addressSegmentLengths} using {@code segmentScope()}
     * method, which divides given address into segments, separated by periods.
     * <p>
     * After that comes verification of said List - first is checking how many segments actually are in the Collection,
     * the size of list has to be equal to field {@code validSegmentPerAddressRatio}, else the exception is thrown.
     * Second is checking how many digits each segment consists - the size has to be within [1 - validSegmentSize] scope,
     * else the exception is thrown.
     * <p>
     * Afterwards the values are assigned to {@code maskValue} fields in the {@link InternetProtocolSegment} objects
     * in the List, using {@code toSegments()} method.
     * <p>
     * Last part depends on processing data inside of each {@link InternetProtocolSegment} object. After reconstructing
     * {@code List<Character>} collections to integers it verifies each {@code segmentValue}, stored by each segment.
     *
     * @param prompt boolean value, deciding if prompt confirming that address is valid should be displayed
     *
     * @see InternetProtocolSegment
     * @see InternetProtocolValid
     * @see InternetProtocolAddressNotValidException
     */
    private void standardMaskValidationAndSegmentation(boolean prompt) {
        List<Integer> maskSegmentLengths;
        try {
            maskSegmentLengths = segmentScope(internalInputSubnetMask.toCharArray());   // Generating lengths of segments
            if (maskSegmentLengths.size() != validSegmentPerAddressRatio)   // Checking if the user provided valid number of segments
                throw new InternetProtocolAddressNotValidException();
            for(Integer current : maskSegmentLengths)   // Checking if the length of every segment is valid
                if ((current > validSegmentSize) || (current == 0))
                    throw new InternetProtocolAddressNotValidException();

            toSegments(internalInputSubnetMask, false, maskSegmentLengths);     // Configuring InternetProtocolSegment objects

            for (InternetProtocolSegment segment : addressSegments) // Creating integers out of Lists of Character class objects
                segment.segmentReconstructionIPv4(segment.rawMask, false);
            for (InternetProtocolSegment segment : addressSegments) // Verifying values of each segment
                segment.segmentVerificationIPv4(segment.maskValue);
            throw new InternetProtocolValid();
        } catch (InternetProtocolAddressNotValidException exception) {
            System.out.print("\t>< Given mask is invalid, enter a valid one: ");
            Scanner subnetMask = new Scanner(System.in);
            internalInputSubnetMask = subnetMask.nextLine();
            validationAndSegmentingMaskIPv4(prompt);
        } catch (InternetProtocolValid internetProtocolValid) {
            if(prompt) {
                System.out.println("\t\t>> Subnet mask is valid");
                System.out.println();
            }
        }
    }

    /**
     *  Calculates the length of each segment.
     *
     *  @param digits   all the characters from the {@code internalInputAddress.toCharArray()}
     *
     *  @return List of integers, containing the number of digits in each segment.
     */
    private List<Integer> segmentScope(char[] digits) {
        List<Integer> addressSegmentLengths = new LinkedList<>();
        int processed = 0;  // Number of digits, which have been already iterated through
        for (char digit : digits) {
            if (digit != separator)    // If the processed character isn't a separator, the Integer is incremented, counting the spaces before one
                processed++;
            if (digit == separator) {  // In case iterator has stumbled upon a separator, the processed variable is added to the list and reset - to measure length to another separator
                addressSegmentLengths.add(processed);
                processed = 0;
            }
        }
        addressSegmentLengths.add(processed);  // Adds the last segment, which is skipped - there is no separator at the end of the address
        return addressSegmentLengths;
    }

    /**
     * The function is called, when the CIDR input is detected - it validates given quantity and then calls
     * {@code toDecimalEquivalent} method to provide regular, decimal input, needed in further calculations.
     * <p>
     * If the requirements weren't met, the exception is thrown, repeating the whole process one more time.
     *
     * @param prompt boolean variable, indicating if prompt informing that mask is valid should appear.
     *
     * @see ToNumberSystem
     * @see InternetProtocolAddressNotValidException
     * @see InternetProtocolValid
     */
    private void shortMaskValidationAndSegmentation(boolean prompt) {
        List<Character> CIDR = new LinkedList<>();
        for(int i = 1; i < internalInputSubnetMask.length(); i++)   // Skipping the slash to get only the quantity of bits
            CIDR.add(internalInputSubnetMask.charAt(i));
        int maskBits = 0;
        try {
            maskBits = charListToInteger(CIDR);                // Conversion from List<Character> to integer, to simplify the validation
            if((maskBits < 0) || (maskBits > 30))
                throw new InternetProtocolAddressNotValidException();
            toDecimalEquivalent(maskBits);

            throw new InternetProtocolValid();
        } catch (InternetProtocolAddressNotValidException exception) {
            System.out.print("\t>< Given mask is " + (maskBits <= 16 ? "too short" : "too long") + ", enter valid mask: ");
            Scanner mask = new Scanner(System.in);
            internalInputSubnetMask = mask.nextLine();
            validationAndSegmentingMaskIPv4(prompt);
        } catch (InternetProtocolValid internetProtocolValid) {
            if(prompt) {
                System.out.println("\t\t>> Subnet mask is valid");
                System.out.println();
            }
        }
    }

    /**
     * Function is called after validation and creation of given integer - if converts said variable, the quantity of
     * bits in the mask to char array of ones and zeroes - the number of ones is equal to given integer variable and
     * the zeroes fill every remaining cell in the array.
     * <p>
     * After that, using splitToOctets method, returned values are assigned to {@code binaryMaskSegment} fields in every
     * {@link InternetProtocolSegment} object of addressSegments array. Binary representations of each segments are then
     * converted into decimal equivalent and assigned to {@code maskValue} field.
     *
     * @param maskBits verified quantity of bits
     */
    private void toDecimalEquivalent(int maskBits) {
        char[] binaryMask = new char[validBits];
        for(int i = 0; i < binaryMask.length; i++)
            if(i < maskBits) binaryMask[i] = '1';
            else binaryMask[i] = '0';

        int startingPoint = 0;
        for (InternetProtocolSegment addressSegment : addressSegments) {
            addressSegment.binaryMaskSegment = splitToOctets(binaryMask, startingPoint);
            addressSegment.maskValue = binaryToDecimal(addressSegment.binaryMaskSegment);
            startingPoint += 8;
        }
    }

    /**
     * Iterates through the [startingPoint - finish] scope of 8 elements, inside the array of char variables, while assigning
     * them to the buffer. After the iteration the buffer is iterated through and using {@link StringBuilder} class
     * created into the String.
     *
     * @param octets        full binary representation of given address.
     * @param startingPoint marks the beginning of the scope
     *
     * @return String variable, storing part of the full binary representation.
     */
    private String splitToOctets(char[] octets, int startingPoint) {
        char[] tempSegment = new char[8];
        int finish = startingPoint + 8;
        for(int i = startingPoint, index = 0; i < finish; i++, index++)
            tempSegment[index] = octets[i];

        StringBuilder octet = new StringBuilder();
        for(char current : tempSegment)    // Creating the octet
            octet.append(current);
        return octet.toString();
    }

    /**
     *  Divides the {@code char[] digits} into independent arrays, stored in {@code List<Character> rawSegment} or
     *  {@code List<Character> rawMask} parameters in the {@link InternetProtocolSegment} class' objects.
     *  <p>
     *  The algorithm generates the starting point of each independent array by using lengths of every segment, contained
     *  in {@code List<Integer> segmentLengths} and calculates the starting point of a segment by adding every already
     *  iterated length from the list and the finish by adding to the sum of said elements the next one.
     *  @see InternetProtocolSegment
     *
     *  @param input            given address IP or subnet mask to ensure versatility of the method
     *  @param ifAddress        true - IP Address
     *                          false - subnet mask
     *  @param segmentLengths   lengths of segments of IP address
     *                          lengths of segments of subnet mask
     */
    private void toSegments(String input, boolean ifAddress, List<Integer> segmentLengths) {
        char[] digits = input.toCharArray();
        ListIterator<Integer> lengthIterator = segmentLengths.listIterator();
        for(int i = 0; i < validSegmentPerAddressRatio; i++) {
            int startingPoint = totalValueOfPreviousValues(segmentLengths, i);
            if(ifAddress) {     // In case the function is supposed to split IP address
                if(i == 0)
                    addressSegments.get(i).rawSegment = digitsToSegments(digits, i, lengthIterator.next());
                else {
                    if(lengthIterator.hasNext())
                        addressSegments.get(i).rawSegment = digitsToSegments(digits, startingPoint, startingPoint + lengthIterator.next());
                    else
                        addressSegments.get(i).rawSegment = digitsToSegments(digits, lengthIterator.previous(), digits.length);
                }
            } else {    // In case the function is supposed to split subnet mask
                if(i == 0)
                    addressSegments.get(i).rawMask = digitsToSegments(digits, i, lengthIterator.next());
                else {
                    if(lengthIterator.hasNext())
                        addressSegments.get(i).rawMask = digitsToSegments(digits, startingPoint, startingPoint + lengthIterator.next());
                    else
                        addressSegments.get(i).rawMask = digitsToSegments(digits, lengthIterator.previous(), digits.length);
                }
            }
        }
    }

    /**
     * Sums up every previous length, in order to indicate the next splitting point of iterated list.
     *
     * @param lengths   lengths of each segment, calculated, using {@code segmentScope} method.
     * @param index     currently processed segment.
     *
     * @return Integer - the sum of previous values
     */
    private int totalValueOfPreviousValues(List<Integer> lengths, int index) {
        int finalResult = 0;
        for(int i = 0; i < index; i++)
            finalResult += lengths.get(i) + 1;  // Incrementation, to skip the cell, in which the separator lies
        return finalResult;
    }

    /**
     *  Splits the digits, according to given scope - [iterator - finish], simply iterates through the scope
     *  and adds the variables to the local list, which is then returned and assigned to proper
     *  {@link InternetProtocolSegment} raw value.
     *
     *  @param digits           all the characters from the {@code internalInputAddress.toCharArray()}
     *  @param startingPoint    integer, which indicates at which point of {@code digits} the iteration should start
     *  @param finish           integer, which indicates at which point of {@code digits} the iteration should finish
     *
     *  @return LinkedList of Characters, containing values added during the iteration.
     */
    private List<Character> digitsToSegments(char[] digits, int startingPoint, int finish) {
        List<Character> part = new LinkedList<>();
        for(int i = startingPoint; i < finish; i++)
            part.add(digits[i]);
        return part;
    }

    /**
     *  Based on data stored in the object, function generates next network address for following network, by adding
     *  quantity of hosts to the last segment of the address.
     *
     * @param hostsInNetwork    number of hosts in the network
     *
     * @return decimal equivalent of following network address
     */
    String followingNetworkAddressCreation(int hostsInNetwork) {
        int processedSegments[] = new int[validSegmentPerAddressRatio];
        int addition[] = new int[validSegmentPerAddressRatio];
        addition[validSegmentPerAddressRatio - 1] = hostsInNetwork;

        int buffer = hostsInNetwork;
        for(int i = addition.length - 1; i >= 0; i--) {
            if(buffer >= 255) {
                while(buffer >= 255)
                    buffer -= 255;
                addition[i] = hostsInNetwork % 256;
                addition[i - 1] = buffer;
            }
            if((addressSegments.get(i).segmentValue + addition[i]) >= 255) {
                addition[i - 1] += 1;
                int sum = addressSegments.get(i).segmentValue + addition[i] - 256;
                processedSegments[i] = sum;
            }
            else processedSegments[i] = addressSegments.get(i).segmentValue + addition[i];
        }

        StringBuilder followingNetworkAddress = new StringBuilder();
        for(int i = 0; i < processedSegments.length; i++) {     // Creating String variable fit for inputAddressAssignment function
            followingNetworkAddress.append(processedSegments[i]);
            if(i != 3) followingNetworkAddress.append(".");
        }
        return followingNetworkAddress.toString();
    }
}