package ip.translation;

import java.util.*;
import static ip.translation.ToNumberSystem.*;

/**
 *  <h1>InternetProtocolAddress Class</h1>
 *  The class processes data provided by {@link InternetProtocolAddress} class - reconstructs the char arrays
 *  as integers and verifies the address - if every addressSegments's value is lower than 256.
 *
 *  @author Piotr Lis
 *  @version 2.0, 2019-11-16
 *  @since 1.8.0
 */
class InternetProtocolSegment {
    /** List, holding the digits of a IP address segment in the form of chars in according order */
    List<Character> rawSegment;
    /** List, holding the digits of a subnet mask segment in the form of chars in according order */
    List<Character> rawMask;

    /** Converted {@code rawSegment} into the Integer value. Provided automatically. */
    Integer segmentValue;
    /** Converted {@code rawMask} into the Integer value. Provided automatically. */
    Integer maskValue;

    /** {@code segmentValue} turned into binary */
    String binaryAddressSegment;
    /** {@code maskValue} turned into binary */
    String binaryMaskSegment;

    /** List of objects, containing the segments of network address */
    IPv4NetworkAddressSegment networkAddressSegment;
    /** List of objects, containing the segments of broadcast address */
    IPv4BroadcastAddressSegment broadcastSegment;

    /** Basic class constructor, assigning values */
    InternetProtocolSegment() {
        rawSegment = new LinkedList<>();
        rawMask = new LinkedList<>();

        binaryAddressSegment = null;
        binaryMaskSegment = null;

        networkAddressSegment = null;
        broadcastSegment = null;
    }

    /**
     * Constructor allowing copying the fields of the object .
     *
     * @param IPSegment the segment, which data is going to be copied into the object.
     */
    InternetProtocolSegment(InternetProtocolSegment IPSegment) {
        this.segmentValue = IPSegment.segmentValue;
        this.maskValue = IPSegment.maskValue;

        if(Console.fullToBinary) {
            if(IPSegment.binaryAddressSegment == null)
                IPSegment.binaryAddressSegment = decimalToBinary(IPSegment.segmentValue);
            this.binaryAddressSegment = IPSegment.binaryAddressSegment;
        }

        this.binaryMaskSegment = IPSegment.binaryAddressSegment;

        if(IPSegment.networkAddressSegment == null) IPSegment.getNetwork();
        else IPSegment.networkAddressSegment.networkAddressConfiguration();

        if(IPSegment.broadcastSegment == null) IPSegment.getBroadcast();
        else IPSegment.broadcastSegment.broadcastAddressConfiguration();

        if(networkAddressSegment == null)
            this.networkAddressSegment = new IPv4NetworkAddressSegment();
        this.networkAddressSegment.binaryNetworkAddressSegment =
                IPSegment.networkAddressSegment.binaryNetworkAddressSegment;
        this.networkAddressSegment.networkAddressValue =
                IPSegment.networkAddressSegment.networkAddressValue;

        if(broadcastSegment == null)
            this.broadcastSegment = new IPv4BroadcastAddressSegment();
        this.broadcastSegment.binaryBroadcastAddressSegment =
                IPSegment.broadcastSegment.binaryBroadcastAddressSegment;
        this.broadcastSegment.broadcastValue =
                IPSegment.broadcastSegment.broadcastValue;
    }

    /** Creates the IPv4NetworkAddressSegment object, triggering the appropriate calculation process */
    void getNetwork() {
        if(networkAddressSegment == null)
            networkAddressSegment = new IPv4NetworkAddressSegment();
        else networkAddressSegment.networkAddressConfiguration();
    }

    /** Creates the IPv4BroadcastAddressSegment object, triggering the appropriate calculation process */
    void getBroadcast() {
        if(networkAddressSegment == null)
            networkAddressSegment = new IPv4NetworkAddressSegment();
        if(broadcastSegment == null)
            broadcastSegment = new IPv4BroadcastAddressSegment();
        else broadcastSegment.broadcastAddressConfiguration();
    }

    /**
     * Reconstructs the segment into {@code Integer} value and assigns it to {@code Integer segment} class parameter.
     *
     * @param rawData       list of digits in the segment.
     * @param ifAddress     true if the given list belongs to the address.
     */
    void segmentReconstructionIPv4(List<Character> rawData, boolean ifAddress) {
        if(ifAddress)
            segmentValue = charListToInteger(rawData);  // Assigning the converted integer to the class parameter
        else
            maskValue = charListToInteger(rawData);
    }

    /**
     * Verifies the value of every segment.
     *
     * @param segment value of the segment.
     *
     * @throws InternetProtocolAddressNotValidException in case the value is greater than the 255
     */
    void segmentVerificationIPv4(Integer segment) throws InternetProtocolAddressNotValidException {
        if(segment > 255)
            throw new InternetProtocolAddressNotValidException();
    }

    /**
     *  <h1>IPv4NetworkAddressSegment Class</h1>
     *  The subclass of {@link InternetProtocolSegment}, contains calculated segment of network address, depends on
     *  {@link InternetProtocolSegment} basic data - {@code segmentValue} and {@code maskValue}, along with their binary
     *  equivalents.
     *
     *  @author Piotr Lis
     *  @version 1.0, 2019-12-12
     *  @since 1.8.0
     */
    class IPv4NetworkAddressSegment {
        /**
         *  Calculated out of {@code binaryAddressSegment} and {@code binaryMaskSegment},
         *  using AND operation or simply equal {@code binaryAddressSegment}.
         */
        String binaryNetworkAddressSegment;

        /** Decimal equivalent of {@code binaryNetworkAddressSegment} or equal {@code segmentValue}. */
        Integer networkAddressValue;

        /** Triggers {@code networkAddressConfiguration()} method, which provides class fields with needed data. */
        IPv4NetworkAddressSegment() {
            networkAddressConfiguration();
        }

        /** Decides if the calculations are needed and assigns the class fields appropriate data. */
        void networkAddressConfiguration() {
            if(maskValue == 255) {
                if(Console.fullToBinary)
                    fullCalculation();
                else this.networkAddressValue = segmentValue;   // fullCalculation() isn't needed in case -b parameter hasn't appeared
            } else fullCalculation();
        }

        /**
         *  Checks if the fields exist and if the calculations are needed, if so calculates network address
         *  and assigns given data to appropriate fields
         */
        private void fullCalculation() {
            if(binaryAddressSegment == null)
                binaryAddressSegment = decimalToBinary(segmentValue);

            if(binaryMaskSegment == null)
                binaryMaskSegment = decimalToBinary(maskValue);

            if(binaryNetworkAddressSegment == null)                                         // Calculating the network address in binary system
                this.binaryNetworkAddressSegment = IPv4SegmentBinaryAND(binaryAddressSegment, binaryMaskSegment);
            if(networkAddressValue == null)
                this.networkAddressValue = binaryToDecimal(binaryNetworkAddressSegment);    // Decimal equivalent of networkAddressValue
        }
    }

    /**
     *  <h1>IPv4BroadcastAddressSegment Class</h1>
     *  The subclass of {@link InternetProtocolSegment}, contains calculated segment of broadcast address, depends on
     *  {@link InternetProtocolSegment} basic data - {@code segmentValue} and {@code maskValue}, along with their binary
     *  equivalents.
     *
     *  @author Piotr Lis
     *  @version 1.0, 2019-12-12
     *  @since 1.8.0
     */
    class IPv4BroadcastAddressSegment {
        /**
         * Calculated out of {@code networkAddressSegment} and {@code binaryNOTMaskSegment},
         * using OR operation or simply equal {@code segmentValue}.
         */
        String binaryBroadcastAddressSegment;

        /** Calculated out of {@code binaryMaskSegment}, using NOT operation. */
        String binaryNOTMaskSegment;

        /** Decimal equivalent of {@code binaryNOTMaskSegment}. */
        Integer notMaskValue;

        /** Decimal equivalent of {@code binaryBroadcastAddressSegment} or equal {@code segmentValue}. */
        Integer broadcastValue;

        /** Triggers {@code broadcastAddressConfiguration()} method, which provides class fields with needed data. */
        IPv4BroadcastAddressSegment() {
            broadcastAddressConfiguration();
        }

        /** Decides if the calculations are needed and assigns the class fields appropriate data */
        void broadcastAddressConfiguration() {
            if(maskValue == 255) {
                if(Console.fullToBinary)
                    broadcastFullCalculation();
                else this.broadcastValue = segmentValue;
            } else broadcastCalculationShift();
        }

        /** Depending on the value of {@code fullToBinary} field of {@link Console} class provides appropriate way of calculation. */
        private void broadcastCalculationShift() {
             if(Console.fullToBinary)
                broadcastFullCalculation();
             else
                broadcastMinimalCalculation();
        }

        /**
         *  Checks if the fields exist and if the calculations are needed, if so calculates broadcast address
         *  and assigns given data to appropriate fields
         */
        private void broadcastFullCalculation() {
            if(binaryAddressSegment == null)
                binaryAddressSegment = decimalToBinary(segmentValue);
            if(binaryMaskSegment == null)
                binaryMaskSegment = decimalToBinary(maskValue);

            if(networkAddressSegment == null) {
                networkAddressSegment = new IPv4NetworkAddressSegment();
            } else networkAddressSegment.networkAddressConfiguration();
            if(binaryNOTMaskSegment == null)
                this.binaryNOTMaskSegment = IPv4SegmentBinaryNOT(binaryMaskSegment);    // Negation of the mask
            if(notMaskValue == null)
                this.notMaskValue = binaryToDecimal(binaryNOTMaskSegment);              // Turning the negation into value
            if(binaryBroadcastAddressSegment == null)
                this.binaryBroadcastAddressSegment =                                    // Calculating broadcast address in binary system,
                        IPv4SegmentBinaryOR(networkAddressSegment.binaryNetworkAddressSegment, binaryNOTMaskSegment);
            if(broadcastValue == null)
                this.broadcastValue = binaryToDecimal(binaryBroadcastAddressSegment);   // Calculating decimal value of previously calculated binary address
        }

        /**
         *  Calculates and assigns proper values to only to obligatory fields, needed for the segment processing and checks if
         *  they haven't been made before.
         */
        private void broadcastMinimalCalculation() {
            if(binaryAddressSegment == null)
                binaryAddressSegment = decimalToBinary(segmentValue);
            if(binaryMaskSegment == null)
                binaryMaskSegment = decimalToBinary(maskValue);

            if(networkAddressSegment == null) {
                networkAddressSegment = new IPv4NetworkAddressSegment();
            } else networkAddressSegment.networkAddressConfiguration();
            if(binaryNOTMaskSegment == null)
                this.binaryNOTMaskSegment = IPv4SegmentBinaryNOT(binaryMaskSegment);    // Negation of the mask
            if(binaryBroadcastAddressSegment == null)
                this.binaryBroadcastAddressSegment =                                    // Calculating broadcast address in binary system,
                        IPv4SegmentBinaryOR(networkAddressSegment.binaryNetworkAddressSegment, binaryNOTMaskSegment);
            if(broadcastValue == null)
                this.broadcastValue = binaryToDecimal(binaryBroadcastAddressSegment);   // Calculating decimal value of previously calculated binary address
        }
    }
}