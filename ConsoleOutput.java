
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * 
 * The ConsoleOutput class contains methods for printing formatted output to the
 * console.
 */
public class ConsoleOutput {
    public static void main(String[] args) {
        printSeparator(20);
    }

    /**
     * 
     * Prints a separator line with the current time stamp to the console.
     * 
     * @param length the length of the separator line
     */
    public static void printSeparator(int length) {
        System.out.println("#".repeat(length) + " " + getCurrentTime());
    }

    public static void pressEnter(){
        System.out.print("\nPress Enter to continue...");
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
    }

    /**
     * 
     * Returns the current time in the format "HH:mm:ss.SSS".
     * 
     * @return the current time as a string
     */
    private static String getCurrentTime() {
        return (new SimpleDateFormat("HH:mm:ss.SSS")).format(new Timestamp(System.currentTimeMillis()));
    }
}
