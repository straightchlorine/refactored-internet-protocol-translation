package ip.translation;

import java.util.*;
import static ip.translation.Display.*;
import static ip.translation.ToNumberSystem.*;
import static ip.translation.Confirmation.*;

/**
 *  <h1>InvalidCommandException</h1>
 *  Thrown in case the command given by user is invalid.
 *  @see java.lang.Exception
 *
 *  @author Piotr Lis
 *  @version 1.0, 2019-11-22
 *  @since 1.8.0
 */
class InvalidCommandException extends Exception {}

/**
 * <h1>InvalidCommandException</h1>
 * Thrown in case the command parameter given by user is invalid.
 * @see java.lang.Exception
 *
 * @author Piotr Lis
 * @version 1.0, 2019-11-22
 * @since 1.8.0
 */
class InvalidParameterException extends Exception{}

/**
 *  <h1>InvalidSubNetworkQuantityException</h1>
 *  Thrown in case the quantity of hosts provided by the user is invalid.
 *  @see java.lang.Exception
 *
 *  @author Piotr Lis
 *  @version 1.0, 2019-11-22
 *  @since 1.8.0
 */
class InvalidSubNetworkQuantityException extends Exception {}

/**
 *  <h1>InvalidNetworkIndexException</h1>
 *  Thrown in case the index of network provided by the user is invalid.
 *  @see java.lang.Exception
 *
 *  @author Piotr Lis
 *  @version 1.0, 2019-12-17
 *  @since 1.8.0
 */
class InvalidNetworkIndexException extends Exception {}

/**
 *  <h1>Descending comparator</h1>
 *  Comparator, sorts in descending order.
 *
 *  @author Piotr Lis
 *  @version 1.2, 2019-12-09
 *  @since 1.8.0
 */
class Descending implements Comparator<Integer> {
    @Override
    public int compare(Integer o1, Integer o2) {
        return o2.compareTo(o1);
    }
}

/**
 *  <h1>Console class</h1>
 *  The class provides interface for user, triggering appropriate calculations.
 *
 *  @author Piotr Lis
 *  @version 1.2, 2019-11-22
 *  @since 1.8.0
 */
public class Console {
    public static void main(String[] args) {
        Console console = new Console();
        console.addressesRequest();
    }

    /** Boolean variable, indicating if user wishes to see full binary interpretation at every step (-b parameter). */
    static boolean fullToBinary = false;
    /** Boolean variable indicating if the division calculations have been made. */
    private boolean divided = false;
    /** Boolean variable indicating if some networks have been added or deleted after the calculation. */
    private boolean changes = false;
    /** Default IPAddress , provided by user */
    private InternetProtocolAddress IPAddress;

    private Console() {
        /*addressesRequest();
        IPAddress = new InternetProtocolAddress(true);
        IPAddress.inputAddressAssignment("10.26.12.31", true);
        IPAddress.inputMaskAssignment("16", true);
        commands();*/
    }

    /**
     * The method requests input data from user, assigns given data to {@link InternetProtocolAddress} object and starts
     * the program.
     */
    private void addressesRequest() {
        Scanner input = new Scanner(System.in);
        IPAddress = new InternetProtocolAddress(true);
        System.out.println();

        System.out.print("<< Address input: ");
        String address = input.nextLine();
        IPAddress.inputAddressAssignment(address, true);

        System.out.print("<< Mask input: ");
        String mask = input.nextLine();
        IPAddress.inputMaskAssignment(mask, true);

        commands();
    }

    /**
     *  Verifies the parameter of the command.
     *
     *  @param parameter parameter extracted by {@code parameterExtraction(String)} method.
     *
     *  @return true if provided parameter is valid.
     */
    private boolean parameterVerification(String parameter) {
        if(parameter.equals("")) {
            fullToBinary = false;
            return true;
        } else if(parameter.equals("B")) {
            fullToBinary = true;
            return true;
        } else return false;
    }

    /**
     *  Processes the input, provided by the user and returns the parameter of the command i.e the letter after the dash
     *  (ex. "-B" - the letter B is the parameter)
     *
     *  @param input user's command
     *
     *  @return parameter of the command in the form of letter
     */
    private String parameterExtraction(String input) {
        StringBuilder parameter = new StringBuilder();
        boolean parameterNext = false;
        for(char current : input.toCharArray()) {
            if(current == '-') {
                parameterNext = true;
                continue;
            }
            if(parameterNext)
                parameter.append(current);
        }
        return parameter.toString().toUpperCase();
    }

    /**
     * Interface of the program - the function requests and verifies the commands provided by user.<p>
     * HELP        -   displays all the commands<p>
     * IP          -   displays the given ip address<p>
     * MASK        -   displays the given subnet mask<p>
     * NETADDRESS  -   calculates and displays network address<p>
     * BROADCAST   -   calculates and displays broadcast address<p>
     * SUBNETS     -   triggers the sub-program, responsible for sub network calculation<p>
     * HOSTS       -   displays number of hosts the network is able to contain<p>
     * EXIT        -   terminates the program
     */
    private void commands() {
        try {
            print();
            if(fullToBinary) fullToBinary = false;              // Resetting indicator
            Scanner userInput = new Scanner(System.in);
            String input = userInput.nextLine().toUpperCase();  // Requesting input
            String parameter = parameterExtraction(input);      // Parameter extraction

            if(input.equals("HELP")) {
                commandsHelp();
            } else if(input.equals("NETADDRESS" + " -" + parameter) || (input.equals("NETADDRESS"))) {
                if(!parameterVerification(parameter))
                    throw new InvalidParameterException();
                networkAddress();
            } else if(input.equals("BROADCAST" + " -" + parameter) || (input.equals("BROADCAST"))) {
                if(!parameterVerification(parameter))
                    throw new InvalidParameterException();
                broadcastAddress();
            } else if(input.equals("SUBNETS")) {
                subNetworks();
            } else if(input.equals("NEW")) {
                if (!confirmation())
                    System.out.println("\t\t>< Permission to exit the program not granted.");
                else addressesRequest();
            } else if(input.equals("HOSTS")) {
                println("hosts", "" + (hosts(IPAddress) - 2));
            } else if(input.equals("IP" + " -" + parameter) || (input.equals("IP"))) {     // Displays address, provided by the user
                if(!parameterVerification(parameter))
                    throw new InvalidParameterException();
                for(InternetProtocolSegment currentSegment : IPAddress.addressSegments)  // Checking if needed fields exist
                    if(currentSegment.binaryAddressSegment == null)
                        currentSegment.binaryAddressSegment = decimalToBinary(currentSegment.segmentValue);

                if(fullToBinary) {
                    printIP(IPAddress);
                    printNoIntervalFullBinaryIP((IPAddress));
                } else printIP(IPAddress);
            } else if (input.equals("MASK" + " -" + parameter) || (input.equals("MASK"))) {     // Displays mask, provided by user
                if(!parameterVerification(parameter))
                    throw new InvalidParameterException();
                for(InternetProtocolSegment currentSegment : IPAddress.addressSegments)   // Checking if needed fields exist
                    if(currentSegment.binaryMaskSegment == null)
                        currentSegment.binaryMaskSegment = decimalToBinary(currentSegment.maskValue);

                if(fullToBinary) {
                    printSubnetMask(IPAddress);
                    printFullBinarySubnetMask((IPAddress));
                } else printSubnetMask(IPAddress);
            } else if(input.equals("EXIT")) {
                if (!confirmation())
                    System.out.println("\t\t>< Permission to exit the program not granted.");
                else System.exit(0);
            }
            else throw new InvalidCommandException();
        } catch (InvalidCommandException exception) {
            System.out.println("\t>< Invalid command, enter a valid one.");
        } catch (InvalidParameterException invalidParameter) {
            System.out.println("\t>< Invalid parameter, enter a valid one.");
        }
        commands();
    }

    /** Displays all the commands in current range. */
    private void commandsHelp() {
        System.out.println();
        System.out.println("\tType \"help\" to see all the possible commands.");
        System.out.println("\tType \"ip (-b)\" to see the provided address.");
        System.out.println("\tType \"mask (-b)\" to see provided mask.");
        System.out.println("\tType \"netaddress (-b)\" to calculate the address of the network.");
        System.out.println("\tType \"broadcast (-b)\" to calculate the broadcast address.");
        System.out.println("\tType \"hosts\" to calculate and display number of hosts.");
        System.out.println("\tType \"subnets\" to enter subnetDivision subprogram.");
        System.out.println("\tType \"new\" to enter new mask and address.");
        System.out.println("\tType \"exit\" to exit the program.");
        System.out.println("\t\tParameter -b prints also binary equivalents of every piece of data.");
        System.out.println();
    }

    /**
     *  The method is triggered by command "netaddress" - it provides calculation of network address and displays
     *  the outcome of the calculations.
     */
    private void networkAddress() {
        getNetworkAddress(IPAddress);
        if(!fullToBinary)
            println("networkAddress", networkAddressToText(IPAddress));
        else {
            printBinaryResult("networkAddress");

            printFullBinaryIP(IPAddress);              // Displays a binary interpretation of IP address
            printFullBinarySubnetMask(IPAddress);      // Displays a binary interpretation of subnet mask
            printFullBinaryNetworkAddress(IPAddress);  // Displays a binary representation of network address

            System.out.println();

            printIP(IPAddress);              // Displays IP address
            printSubnetMask(IPAddress);      // Displays subnet mask
            printNetworkAddress(IPAddress);  // Displays network address

            System.out.println();
        }
    }

    /**
     *  The method is triggered by command "broadcast" - it provides calculation of broadcast address and displays the
     *  outcome of the calculations.
     */
    private void broadcastAddress() {
        getBroadcastAddress(IPAddress);
        if(!fullToBinary)
            println("broadcast", broadcastAddressToText(IPAddress));
        else {
            printBinaryResult("broadcast");

            printFullBinaryIP(IPAddress);              // Displays binary interpretation of the IP address
            printFullBinarySubnetMask(IPAddress);      // Displays binary interpretation of the subnet mask
            printFullBinaryNOTSubnetMask(IPAddress);   // Displays binary interpretation of the subnet mask after NOT operation
            printFullBinaryNetworkAddress(IPAddress);  // Displays binary interpretation of network address
            printFullBinaryBroadcast(IPAddress);       // Displays binary interpretation of broadcast address

            System.out.println();

            printIP(IPAddress);                             // Displays the IP address
            printSubnetMask(IPAddress);                     // Displays the subnet mask
            printNOTSubnetMask(IPAddress);                  // Displays the subnet mask after NOT operation
            printNetworkAddress(IPAddress);                 // Displays the network address
            printBroadcast(IPAddress);                      // Displays the broadcast address
            firstAndLastHost(IPAddress, 0, false);          // Displays the first and the last possible host
            printNumberOfHosts(IPAddress);                  // Displays the number of users the network is able to contain

            System.out.println();
        }
    }

    /**
     *  Modifies the fields of given {@link InternetProtocolAddress} object, by operating on it's internal
     *  {@link InternetProtocolSegment} object list i.e creating or changing the objects of the
     *  {@link ip.translation.InternetProtocolSegment.IPv4NetworkAddressSegment} class, wherein data is processed.
     *
     *  @param IPAddress is the {@link InternetProtocolAddress} object, which is meant to undergo the network address
     *                   generation process.
     */
    private void getNetworkAddress(InternetProtocolAddress IPAddress) {
        for(InternetProtocolSegment current : IPAddress.addressSegments) {
            if(current.networkAddressSegment == null)   // Checks if creating the object is needed
                current.getNetwork();
            else current.networkAddressSegment.networkAddressConfiguration();
        }
    }

    /**
     *  Analogically to the process of {@code getNetworkAddress(IPAddress)}, the method modifies the fields
     *  of given {@link InternetProtocolAddress} object, by operating on it's internal
     *  {@link InternetProtocolSegment} object list i.e creating or slightly changing the objects of the
     *  {@link ip.translation.InternetProtocolSegment.IPv4BroadcastAddressSegment} class, wherein data is processed.
     *
     *  @param IPAddress is the {@link InternetProtocolAddress} object, which is meant to undergo the broadcast address
     *                   generation process.
     */
    private void getBroadcastAddress(InternetProtocolAddress IPAddress) {
        for(InternetProtocolSegment current : IPAddress.addressSegments) {
            if(current.broadcastSegment == null)
                current.getBroadcast();
            else current.broadcastSegment.broadcastAddressConfiguration();
        }
    }

    /** Calls the subnetDivision subprogram. */
    private void subNetworks() {
        List<InternetProtocolAddress> subNetworks = new LinkedList<>();
        List<Integer> hostQuantity = new LinkedList<>();
        List<Integer> bitsOfMasks = new LinkedList<>();
        System.out.println("\t>>Entering the subnetDivision sub-program...");

        internalDivisionCommands(subNetworks, hostQuantity, bitsOfMasks, 0);
    }

    /**
     * Similar to {@code commands()}, although significantly more expanded - the verifies all the commands, concerning
     * the division of networks.
     * <p>
     * SUBNET CALCULATE        - calculates the networks, able to contain host quantities, provided by user<p>
     * SUBNET [quantity] /add  - rounds given quantity up to the power of 2 and verifies if the root network is able to
     *                            contain given number of hosts<p>
     * SUBNET [index] /del     - removes the quantity of given index, stored in {@code hostQuantity} (requires confirmation)<p>
     * SUBNET LIST             - prompts all the requested networks without calculating them - to show the user what has been done
     *                            and check index of the network, if the request is to delete a particular network<p>
     * NEW              - allows user to enter different address - erases all the stored data and prompts the address creation
     *                            window (requires confirmation)<p>
     * SUBNET ERASE            - allows user to erase whole division and start again, using the same address (requires confirmation)<p>
     * HOSTS                   - displays number of hosts left to manage<p>
     * HELP                    - displays all the commands<p>
     * SUBNET EXIT             - exits the subnet division mode (requires confirmation)<p>
     *
     * @param subNetworks  list of {@link InternetProtocolAddress} objects, containing the data of calculated networks.
     * @param hostQuantity list of Integers, containing quantity of hosts, the user desired, rounded up to the number
     *                     which can be expressed as a power of two.
     * @param masks        list of Integers, representing the number of bits in each mask of each network.
     * @param totalHosts   total number of hosts in each calculated network.
     */
    private void internalDivisionCommands(List<InternetProtocolAddress> subNetworks, List<Integer> hostQuantity, List<Integer> masks, int totalHosts) {
        try{
            hostQuantity.sort(new Descending());
            Collections.sort(masks);

            printSubnetDivisionData();
            Scanner command = new Scanner(System.in);
            String input = command.nextLine().toUpperCase();
            String parameter = parameterExtraction(input);  // Extracting the parameter for future calculations and display

            if(input.equals("HELP")) {   // Prompts all possible commands, along with their application
                subNetworkDivisionHelp();
            } else if(input.equals("HOSTS")) {
                printHostsLeft(IPAddress, totalHosts);
            } else if(input.equals("SUBNET " + quantityExtraction(input) + " /ADD")) {
                int hosts = quantityExtraction(input);
                hosts = currentIntegerCorrection(hosts, masks);
                totalHosts += hosts;    // Adding new quantity to the total of all hosts
                if((totalHosts <= hosts(IPAddress)) && (hosts > 0)) {    // Verification if the total sum of hosts does not exceed the limit
                    if(divided) {
                        changes = true;
                        divided = false;
                    }
                    hostQuantity.add(hosts);    // Adding corrected quantity to the list
                    System.out.println("\t>> The host quantity of " + hosts + " has been added.");
                } else {
                    totalHosts -= hosts;
                    masks.remove(masks.size() - 1);
                    throw new InvalidSubNetworkQuantityException();
                }
            } else if(input.equals("SUBNET " + quantityExtraction(input) + " /DEL")) {
                int index = quantityExtraction(input) - 1;
                if(hostQuantity.size() >= index) {
                    System.out.println(">> Do you really want to delete network of index " + (index + 1) + " of host quantity equal " + hostQuantity.get(index) + "?");
                    if(confirmation()) {      // Asking user for confirmation of the action
                        if(divided) {
                            changes = true;
                            divided = false;
                        }
                        System.out.println("\t>> Deleting network: ");
                        System.out.println("\t\t> Index: " + (index + 1));
                        System.out.println("\t\t> Host quantity: " + hostQuantity.get(index));
                        hostQuantity.remove(index);
                    } else System.out.println("\t\t>< Permission to delete network not granted.");
                } else throw new InvalidNetworkIndexException();
            } else if(input.equals("SUBNET CALCULATE"  + " -" + parameter) || input.equals("SUBNET CALCULATE")) {
                if(!parameterVerification(parameter))
                    throw new InvalidParameterException();
                if (hostQuantity.isEmpty())
                    System.out.println("\t>< Network hasn't been divided.");
                else {
                    if(changes) {
                        subNetworks = new LinkedList<>();
                        totalHosts = 0;
                        changes = false;
                    }
                    parameterVerification(parameter);
                    InternetProtocolAddress singleNetwork =     // Copying original address, so the user will be able to perform other operations on input address
                            new InternetProtocolAddress(IPAddress);

                    if(subNetworks.isEmpty()) {
                        for (int i = 0; i <= hostQuantity.size(); i++)  // Creating an object per every single network
                            if(i == 0) subNetworks.add(singleNetwork);
                            else subNetworks.add(new InternetProtocolAddress(true));

                        int index = 0;
                        for (int i = 1, j = 0; i < subNetworks.size(); i++, j++) {  // Configuration of the objects
                            subNetworks.get(i).inputMaskAssignment(masks.get(j).toString(), false);
                            if(i == 1)
                                subNetworks.get(i).inputAddressAssignment(networkAddressToText(subNetworks.get(j)), false);
                            else {
                                subNetworks.get(i).inputAddressAssignment(subNetworks.get(j).followingNetworkAddressCreation(hostQuantity.get(index)), false);
                                index++;
                            }
                        }
                    }
                    for(InternetProtocolAddress currentAddress : subNetworks)
                        currentAddress.addressConfiguration();

                    subNetworkPresentation(IPAddress, 0);
                    for (int i = 1; i < subNetworks.size(); i++)
                        subNetworkPresentation(subNetworks.get(i), i);
                    printUnreservedScope(subNetworks, hostQuantity);
                    printHostsLeft(subNetworks.get(0), totalHosts);
                    divided = true;
                }
            } else if(input.equals("SUBNET LIST")) {
                if(!hostQuantity.isEmpty()) {
                    System.out.println("\n\t>> The network has been divided into " + hostQuantity.size() + " networks so far:");
                    for(int i = 0; i < hostQuantity.size(); i++) {  // Simple display of networks divided so far
                        int index = i + 1;
                        System.out.println("\t\t> Network of index " + index + ": " + hostQuantity.get(i) + " hosts.");
                        System.out.println("\t\t> Subnet Mask assigned to network " + index + ": /" + masks.get(i));
                        System.out.println();
                    }
                    printHostsLeft(IPAddress, totalHosts);
                } else System.out.println("\t>< Network hasn't been divided");
            } else if(input.equals("NEW")) {
                if(confirmation()) {
                    subNetworks = new LinkedList<>();
                    hostQuantity = new LinkedList<>();
                    masks = new LinkedList<>();
                    totalHosts = 0;
                    divided = false;
                    changes = false;
                    addressesRequest();
                } else System.out.println("\t\t>< Permission to reset address not granted.");
            } else if(input.equals("SUBNET ERASE")) {
                if(hostQuantity.isEmpty())   // Checking if the user inserted data into the program
                    System.out.println(">< Invalid command, there is no division registered.");
                else {
                    if(confirmation()) {
                        subNetworks = new LinkedList<>();
                        hostQuantity = new LinkedList<>();
                        masks = new LinkedList<>();
                        totalHosts = 0;
                        divided = false;
                        changes = false;
                        System.out.println("\t>> Stored data erased.");
                    } else System.out.println("\t\t>< Permission to erase data not granted.");
                }
            } else if(input.equals("EXIT")) {
                if(!confirmation())
                    System.out.println("\t\t>< Permission to exit subDivision mode not granted.");
                else commands();
            } else throw new InvalidCommandException();
        } catch (InvalidCommandException command) {
            System.out.println("\t>< Invalid command, type \"help\" to check the possible tasks.");
        } catch (InvalidSubNetworkQuantityException quantity) {
            System.out.println("\t>< Provided quantity is invalid - provide valid amount.");
        } catch (InvalidNetworkIndexException index) {
            System.out.println("\t>< Provided index of network is invalid - provide valid network index.");
        } catch (InvalidParameterException parameter) {
            System.out.println("\t>< Provided parameter of the command is invalid - provide valid parameter.");
        }
        internalDivisionCommands(subNetworks, hostQuantity, masks, totalHosts);
    }

    /** List of all the commands in subnetDivision sub-program. */
    private void subNetworkDivisionHelp() {
        System.out.println();
        System.out.println("\tType \"help\" to see all the possible commands in this mode.");
        System.out.println("\tType \"subnet <number of hosts> /add\" to add the subnet.");
        System.out.println("\tType \"subnet <index of network> /del\" to delete the subnet.");
        System.out.println("\tType \"subnet calculate\" to calculate the addresses of each subnet.");
        System.out.println("\tType \"subnet new\" to terminate the sub network addition process and start it once again, upon a different address.");
        System.out.println("\tType \"subnet erase\" to terminate the sub network addition process and start it once again, upon the same address.");
        System.out.println("\tType \"hosts\" display quantity of hosts, which weren't assigned to any network.");
        System.out.println("\tType \"exit\" to exit the subDivision subprogram.");
        System.out.println();
    }

    /**
     *  Method is extracting quantities out of the commands - (ex. subnet 128 /add - returns 128).
     *
     *  @param command  command entered by user.
     *
     *  @throws InvalidSubNetworkQuantityException thrown, when the value is invalid in sense of input method
     *  (ex. 1000 expressed as 1 000 - invalid).
     *
     *  @return Integer, storing the number, extracted out of command provided by the user.
     */
    private Integer quantityExtraction(String command) throws InvalidSubNetworkQuantityException {
        List<Character> currentHostQuantity = new LinkedList<>();  // Extracted desired quantity of hosts
        char[] task = command.toCharArray();                        // Container, storing input command in form of char array
        int timesSpace = 0;                                         // Counts how many times space appears in the command - to mark section of extraction
        boolean toRead = false;                                     // True, if the iterator processes data inside the section

        for(char current : task) {
            if(timesSpace > 2) throw new InvalidSubNetworkQuantityException();   // Section needs a beginning and a finish
            if(current == ' ') {
                timesSpace++;               // Marks beginning and ending
                toRead = timesSpace < 2;    // Verifies if there is only one beginning and one ending
                continue;
            }
            if(toRead)  // In case iterator is in the section and should get data from command
                currentHostQuantity.add(current);
        }
        return charListToInteger(currentHostQuantity);
    }

    /**
     *  Method returns the corrected host quantity - in the form of the power of two, along with assigning the bits of mask
     *  to the given mask.
     *
     *  @param hostQuantity given host quantity, yet to be rounded up.
     *  @param masks        given list, which contains the masks of each sub network
     *
     *  @return Integer, storing the number, represented by the power of two, which is able to contain input given by the user
     */
    private int currentIntegerCorrection(int hostQuantity, List<Integer> masks) {
        int correctedHostQuantity;
        for(int i = 0;; i++) {
            correctedHostQuantity = toThePower(2, i);
            if(hostQuantity <= correctedHostQuantity - 2) {
                if(bitsInMask(IPAddress) <= (32 - i))
                    masks.add(32 - i);
                break;
            }
        }
        return correctedHostQuantity;
    }
}
/**
 *  <h1>Display class</h1>
 *  Class consisted of methods used to display outcomes of calculations in the {@link InternetProtocolSegment} lists,
 *  within {@link InternetProtocolAddress} objects.
 *
 *  @author Piotr Lis
 *  @version 1.2, 2019-12-09
 *  @since 1.8.0
 */
class Display {
    /** Print line before every command - to actually see that the program is working */
    static void print() {
        System.out.print("IP/translation" + ("/" + "") + "> ");
    }

     /** Print line before every display of data generated by subnetDivision program. */
    static void printSubnetDivisionData() {
        System.out.print("IP/translation/subnetDivision/> ");
    }

    /**
     * Print line before every regular result - to inform the user what has been done.
     *
     * @param  currentMode String variable, containing the name of current mode.
     * @param  data        Data, meant to be displayed in simple and short manner.
     */
    static void println(String currentMode, String data) {
        System.out.println("\tIP/translation" + ("/" + currentMode) + "/" + "> " + data);
    }

    /**
     * Print line before every full binary result - to inform the user what has been done.
     *
     * @param  currentMode String variable, containing the name of current mode.
     */
    static void printBinaryResult(String currentMode) {
        System.out.println("IP/translation" + ("/" + currentMode) + "/fullbinary/" + "> ");
        System.out.println();
    }

    /**
     * Displays quantity of hosts given address is able to contain.
     *
     * @param  IPAddress address, which will undergo calculations.
     */
    static void printNumberOfHosts(InternetProtocolAddress IPAddress) {
        System.out.println("\tHosts . . . . . . . . : " + (hosts(IPAddress) - 2));
    }

    /**
     * Provides {@code networkAddress()} method with regular, decimal representation of network address.
     *
     * @param IPAddress the address providing data.
     *
     * @return decimal representation of network address
     */
    static String networkAddressToText(InternetProtocolAddress IPAddress) {
        StringBuilder networkAddress = new StringBuilder();
        for(InternetProtocolSegment current : IPAddress.addressSegments) {
            networkAddress.append(current.networkAddressSegment.networkAddressValue);
            period(IPAddress.addressSegments, current, networkAddress);
        }
        return networkAddress.toString();
    }

    /**
     * Provides {@code broadcastAddress()} method with regular, decimal representation of broadcast address.
     *
     * @param IPAddress the address providing data.
     *
     * @return decimal representation of broadcast address.
     */
    static String broadcastAddressToText(InternetProtocolAddress IPAddress) {
        StringBuilder broadcastAddress = new StringBuilder();
        for(InternetProtocolSegment current : IPAddress.addressSegments) {
            broadcastAddress.append(current.broadcastSegment.broadcastValue);
            period(IPAddress.addressSegments, current, broadcastAddress);
        }
        return broadcastAddress.toString();
    }

    /**
     * Displays sub networks calculated in subnet division program.
     * @param IPAddress         address to display.
     * @param subNetworkIndex   index of currently processed network
     */
    static void subNetworkPresentation(InternetProtocolAddress IPAddress, int subNetworkIndex) {
        int times = 1;
        if(subNetworkIndex == 0) {
            tabulation(times);
            System.out.println(">> The network, which has been divided: ");
        } else {
            times++;
            tabulation(times);
            System.out.println(">> Network " + subNetworkIndex +  ":");
        }
        times++;

        if(!Console.fullToBinary) {
            tabulation(times);
            printIP(IPAddress);

            tabulation(times);
            printNetworkAddress(IPAddress);  // Displays the network address

            tabulation(times);
            printBroadcast(IPAddress);       // Displays the broadcast address

            tabulation(times);
            printNumberOfHosts(IPAddress);   // Displays the number of users the network is able to contain

        } else {

            tabulation(times);
            printFullBinaryNetworkAddress(IPAddress);  // Displays binary interpretation of network address

            tabulation(times);
            printFullBinarySubnetMask(IPAddress);      // Displays binary interpretation of the subnet mask

            tabulation(times);
            printFullBinaryNOTSubnetMask(IPAddress);   // Displays binary interpretation of the subnet mask after NOT operation

            tabulation(times);
            printFullBinaryBroadcast(IPAddress);       // Displays binary interpretation of broadcast address

            tabulation(times);
            printNumberOfHosts(IPAddress);              // Displays the number of users the network is able to contain

            System.out.println();

            tabulation(times);
            printIP(IPAddress);

            tabulation(times);
            printNetworkAddress(IPAddress);                   // Displays the network address

            tabulation(times);
            printSubnetMask(IPAddress);                       // Displays the subnet mask

            tabulation(times);
            printNOTSubnetMask(IPAddress);                    // Displays the subnet mask after NOT operation

            tabulation(times);
            printBroadcast(IPAddress);                        // Displays the broadcast address

            firstAndLastHost(IPAddress, times, true);     // Displays the first and the last possible host

            tabulation(times);
            printNumberOfHosts(IPAddress);                    // Displays the number of users the network is able to contain
        }
        System.out.println();
    }

    /**
     * Displays the scope of unreserved addresses.
     *
     * @param subNetworks list of already divided networks.
     * @param hostQuantities quantity of hosts each of these network consists of.
     */
    static void printUnreservedScope(List<InternetProtocolAddress> subNetworks, List<Integer> hostQuantities) {
        String beginning = subNetworks.get(subNetworks.size() - 1).followingNetworkAddressCreation(hostQuantities.get(hostQuantities.size() -1 ));
        System.out.println("\t>> Unreserved addresses: ");
        System.out.println("\t\t> Start of the scope . . . : " + beginning);
        System.out.println("\t\t> End of the scope . . . . : " + broadcastAddressToText(subNetworks.get(0)));
    }

    /**
     * Displays how many hosts are left to assign and how many hosts already have been assigned.
     *
     * @param IPAddress address to be displayed.
     * @param totalHosts total quantity of hosts all the divided networks are able to contain.
     */
    static void printHostsLeft(InternetProtocolAddress IPAddress, int totalHosts) {
        int hosts = hosts(IPAddress);
        int hostsLeft = hosts - totalHosts;
        System.out.println("\tHosts left. . . . . . : " + hostsLeft + " out of " + hosts);
        System.out.println("\tHosts assigned. . . . : " + (hosts - hostsLeft));
        System.out.println();
    }

    /**
     * Calculates number of hosts.
     *
     * @param  IPAddress   address, which contains data, that will be displayed.
     *
     * @return quantity of hosts the address consists of.
     */
    static int hosts(InternetProtocolAddress IPAddress) {
        int powerIndex = 32 - bitsInMask(IPAddress);
        return toThePower(2, powerIndex);
    }

    /**
     * Calculates and displays IPv4 addresses of first and last host of the network.
     *
     * @param  IPAddress       address, which contains data, that will be displayed - to assure versatility.
     * @param  networkIndex    index of currently processed network.
     * @param  tabulation      indicator if the tabulation should take place or not (purely visual aspect).
     */
    static void firstAndLastHost(InternetProtocolAddress IPAddress, int networkIndex, boolean tabulation) {
        StringBuilder firstHostAddress = new StringBuilder();
        StringBuilder lastHostAddress = new StringBuilder();

        for(InternetProtocolSegment currentSegment : IPAddress.addressSegments) {
            if(IPAddress.addressSegments.indexOf(currentSegment) == 3) {
                firstHostAddress.append(currentSegment.networkAddressSegment.networkAddressValue + 1);
                lastHostAddress.append(currentSegment.broadcastSegment.broadcastValue - 1);
            } else {
                firstHostAddress.append(currentSegment.networkAddressSegment.networkAddressValue);
                period(IPAddress.addressSegments, currentSegment, firstHostAddress);
                lastHostAddress.append(currentSegment.broadcastSegment.broadcastValue);
                period(IPAddress.addressSegments, currentSegment, lastHostAddress);
            }
        }
        printFirstAndLastHost(firstHostAddress.toString(), lastHostAddress.toString(), networkIndex, tabulation);
    }

    /**
     *  Displays IPv4 addresses of first and last host of the network.
     *
     * @param  firstHost    address of the first host in the network, received from {@code firstAndLastHost()}.
     * @param  lastHost     address of the last host in the network, received from {@code firstAndLastHost()}.
     * @param  networkIndex index of the currently processed network.
     * @param tabulation    indicator, deciding if tabulation should take place before the output.
     */
    private static void printFirstAndLastHost(String firstHost, String lastHost, int networkIndex, boolean tabulation) {
        if(tabulation)
            tabulation(networkIndex);
            System.out.println("\tFirst Host. . . . . . : " + firstHost);
        if(tabulation)
            tabulation(networkIndex);
            System.out.println("\tLast Host . . . . . . : " + lastHost);
    }

    /**
     * Iterates through segments and creates the string - the IP address.
     *
     * @param  IPAddress address, which contains data, that will be displayed.
     */
    static void printIP(InternetProtocolAddress IPAddress) {
        StringBuilder givenIPAddress = new StringBuilder();
        for(InternetProtocolSegment currentSegment : IPAddress.addressSegments) {
            givenIPAddress.append(currentSegment.segmentValue);
            period(IPAddress.addressSegments, currentSegment, givenIPAddress);
        }
        System.out.println("\tIPv4 Address  . . . . : " + givenIPAddress.toString() + "/" + bitsInMask(IPAddress));
    }

    /**
     * Iterates through segments and creates the string - the subnet mask address.
     *
     * @param  IPAddress address, which contains data, that will be displayed.
     */
    static void printSubnetMask(InternetProtocolAddress IPAddress) {
        StringBuilder subnetMask = new StringBuilder();
        for(InternetProtocolSegment currentSegment : IPAddress.addressSegments) {
            subnetMask.append(currentSegment.maskValue);
            period(IPAddress.addressSegments, currentSegment, subnetMask);
        }
        System.out.println("\tSubnet Mask . . . . . : " + subnetMask.toString());
    }

    /**
     * Iterates through segments and creates the string - the negated subnet mask address.
     *
     * @param  IPAddress address, which contains data, that will be displayed.
     */
    static void printNOTSubnetMask(InternetProtocolAddress IPAddress) {
        StringBuilder subnetMaskNOT = new StringBuilder();
        for(InternetProtocolSegment currentSegment : IPAddress.addressSegments) {
            subnetMaskNOT.append(currentSegment.broadcastSegment.notMaskValue);
            period(IPAddress.addressSegments, currentSegment, subnetMaskNOT);
        }
        System.out.println("\tNOT(Subnet Mask). . . : " + subnetMaskNOT.toString());
    }

    /**
     * Iterates through segments and creates the string - the network address.
     *
     * @param  IPAddress address, which contains data, that will be displayed.
     */
    static void printNetworkAddress(InternetProtocolAddress IPAddress) {
        StringBuilder networkAddress = new StringBuilder();
        for(InternetProtocolSegment currentSegment : IPAddress.addressSegments) {
            networkAddress.append(currentSegment.networkAddressSegment.networkAddressValue);
            period(IPAddress.addressSegments, currentSegment, networkAddress);
        }
        System.out.println("\tNetwork Address . . . : " + networkAddress.toString());
    }

    /**
     * Iterates through segments and creates the string - the broadcast address.
     *
     * @param  IPAddress address, which contains data, that will be displayed.
     */
    static void printBroadcast(InternetProtocolAddress IPAddress) {
        StringBuilder broadcastAddress = new StringBuilder();
        for(InternetProtocolSegment currentSegment : IPAddress.addressSegments) {
            broadcastAddress.append(currentSegment.broadcastSegment.broadcastValue);
            period(IPAddress.addressSegments, currentSegment, broadcastAddress);
        }
        System.out.println("\tBroadcast Address . . : " + broadcastAddress.toString());
    }

    /**
     * Iterates through segments and creates the string - the binary IP address.
     *
     * @param  IPAddress address, which contains data, that will be displayed.
     */
    static void printFullBinaryIP(InternetProtocolAddress IPAddress) {
        StringBuilder binaryRepresentation = new StringBuilder();
        for(InternetProtocolSegment currentSegment : IPAddress.addressSegments)
            binaryRepresentation.append(currentSegment.binaryAddressSegment);
        System.out.println("\tIPv4 Address  . . . . : " + binaryStringCreation(IPAddress, binaryRepresentation));
    }

    /**
     * Iterates through segments and creates the string - the binary IP address.
     *
     * @param  IPAddress address, which contains data, that will be displayed.
     */
    static void printNoIntervalFullBinaryIP(InternetProtocolAddress IPAddress) {
        StringBuilder binaryRepresentation = new StringBuilder();
        for(InternetProtocolSegment currentSegment : IPAddress.addressSegments) {
            binaryRepresentation.append(currentSegment.binaryAddressSegment);
            binaryRepresentation.append("  ");
        }
        System.out.println("\tIPv4 Address  . . . . : " + binaryRepresentation.toString());
    }

    /**
     * Iterates through segments and creates the string - the binary subnet mask address.
     *
     * @param  IPAddress address, which contains data, that will be displayed.
     */
    static void printFullBinarySubnetMask(InternetProtocolAddress IPAddress) {
        StringBuilder binaryRepresentation = new StringBuilder();
        for(InternetProtocolSegment currentSegment : IPAddress.addressSegments)
            binaryRepresentation.append(currentSegment.binaryMaskSegment);
        System.out.println("\tSubnet Mask . . . . . : " + binaryStringCreation(IPAddress, binaryRepresentation));
    }

    /**
     * Iterates through segments and creates the string - the binary negated subnet mask address.
     *
     * @param  IPAddress address, which contains data, that will be displayed.
     */
    static void printFullBinaryNOTSubnetMask(InternetProtocolAddress IPAddress) {
        StringBuilder binaryRepresentation = new StringBuilder();
        for(InternetProtocolSegment currentSegment : IPAddress.addressSegments)
            binaryRepresentation.append(currentSegment.broadcastSegment.binaryNOTMaskSegment);
        System.out.println("\tNOT(Subnet Mask). . . : " + binaryStringCreation(IPAddress, binaryRepresentation));
    }

    /**
     * Iterates through segments and creates the string - the binary network address.
     *
     * @param  IPAddress address, which contains data, that will be displayed.
     */
    static void printFullBinaryNetworkAddress(InternetProtocolAddress IPAddress) {
        StringBuilder binaryRepresentation = new StringBuilder();
        for(InternetProtocolSegment currentSegment : IPAddress.addressSegments)
            binaryRepresentation.append(currentSegment.networkAddressSegment.binaryNetworkAddressSegment);
        System.out.println("\tNetwork Address . . . : " + binaryStringCreation(IPAddress, binaryRepresentation));
    }

    /**
     * Iterates through segments and creates the string - the binary broadcast address.
     *
     * @param  IPAddress address, which contains data, that will be displayed.
     */
    static void printFullBinaryBroadcast(InternetProtocolAddress IPAddress) {
        StringBuilder binaryRepresentation = new StringBuilder();
        for(InternetProtocolSegment currentSegment : IPAddress.addressSegments)
            binaryRepresentation.append(currentSegment.broadcastSegment.binaryBroadcastAddressSegment);
        System.out.println("\tBroadcast Address . . : " + binaryStringCreation(IPAddress, binaryRepresentation));
    }

    /**
     * Method modifies the StringBuilder object, containing 32-bit binary mask equivalent, by dividing it into 4 octets
     * and separating address into two parts - first, indicating network and the second, indicating hosts.
     *
     * @param  IPAddress   {@link InternetProtocolAddress} object, which will undergo calculations.
     * @param  binary      {@link StringBuilder} object, containing 32-bit string of 1 and 0 - binary equivalent of IPv4 address.
     * @return String, result of {@code StringBuilder.toString()} after modifications.
     */
    private static String binaryStringCreation(InternetProtocolAddress IPAddress, StringBuilder binary) {
        int maskBits = bitsInMask(IPAddress);

        int timesAppended = 0;      // Counts how many segments have been already displayed and separated
        boolean separatorPlaced = false;    // true, if the | separator has been appended
        for(int i = 0; i < IPAddress.validBits; i++) {
            if((i + 1) == maskBits) {   // In case separator is required
                binary.insert((i + 1) + (timesAppended * 2), "| ");
                separatorPlaced = true;
                continue;
            }
            if((i + 1) % 8 == 0) {  // In case the octets need to be separated
                binary.insert((i + 1) + (timesAppended * 2) + (separatorPlaced ? 2 : 0), "  ");
                timesAppended++;
            }
        }
        return binary.toString();
    }

    /**
     * Translates the standard mask expression into short one - the number if ones in the binary version of one.
     *
     * @param IPAddress the address upon which the calculation is supposed to take place
     *
     * @return  the bits inside the mask (ex. 255.255.255.0 - 24).
     */
    static int bitsInMask(InternetProtocolAddress IPAddress) {
        int maskBits = 0;
        for(InternetProtocolSegment currentSegment : IPAddress.addressSegments) {
            if(currentSegment.binaryMaskSegment == null)
                currentSegment.binaryMaskSegment = decimalToBinary(currentSegment.maskValue);

            char[] mask = currentSegment.binaryMaskSegment.toCharArray();
            for(char current : mask)
                if(current == '1') maskBits++;
        }
        return maskBits;
    }

    /** Simple tabulation before the output. */
    private static void tabulation(int times) {
        for(int i = 1; i <= times; i++)
            System.out.print("\t");
    }

    /**
     * The method adds a period in appropriate place, in the process of address creation, thanks to checking the index
     * of the current segment - the dot belongs after every segment with exception of the fourth one.
     *
     * @param addressSegments   Full list of segments, meant to determine the index of currently processed segment.
     * @param currentSegment    InternetProtocolSegment object, which allows to get index of the currently processed segment
     *                          inside the function.
     * @param address           {@code String Builder}, which contains already stored address segments.
     */
    private static void period(List<InternetProtocolSegment> addressSegments, InternetProtocolSegment currentSegment, StringBuilder address) {
        if(addressSegments.indexOf(currentSegment) != addressSegments.size() - 1)
            address.append(".");
    }
}