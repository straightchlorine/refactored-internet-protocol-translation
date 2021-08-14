package ip.translation;
import java.util.Scanner;

/**
 * <h1>InvalidConfirmationException</h1>
 * Thrown in case the confirmation provided by user is invalid.
 * @see java.lang.Exception
 *
 * @author Piotr Lis
 * @version 1.2, 2019-12-17
 * @since 1.8.0
 */
class InvalidConfirmationException extends Exception {}

/**
 * <h1>Confirmation class</h1>
 * Verifies if the user really wishes to continue operation.
 *
 * @author Piotr Lis
 * @version 1.2, 2019-12-17
 * @since 1.8.0
 */
class Confirmation {
    /** Buffer */
    private static boolean agreement;

    /**
     *  Method requesting user's confirmation.
     *
     *  @return boolean true, if the task
     */
    static boolean confirmation() {
        System.out.print("\t\t>>Do you wish to proceed(Y/n): ");
        agreement = false;
        return proceed();
    }

    /**
     *  Try catch instruction, verifying the input of the user, by manipulating the {@code boolean agreement} field.
     *
     *  @return true, if the permission has been granted
     */
    private static boolean proceed() {
        try {
            Scanner confirmation  = new Scanner(System.in);
            String answer = confirmation.nextLine().toUpperCase();
            agreement = permission(answer);
        } catch (InvalidConfirmationException confirmation) {
            System.out.print("\t\t\t>< Invalid input (Y/n): ");
            proceed();
        }
        return agreement;
    }

    /**
     * Analyses the input and determines if the user agrees to proceed.
     *
     * @param answer input provided by user.
     *
     * @throws InvalidConfirmationException in case given input is invalid.
     *
     * @return true if the user agreed to continue.
     */
    private static boolean permission(String answer) throws InvalidConfirmationException {
        if((answer.equals("Y")) || (answer.equals("YES")))
            return true;
        else if((answer.equals("N")) || (answer.equals("NO")))
            return false;
        else
            throw new InvalidConfirmationException();
    }
}