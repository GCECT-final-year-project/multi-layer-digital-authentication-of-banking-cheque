
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsoleOutput {
    public static void main(String[] args) {
        printSeparator(20);
    }
    public static void printSeparator(int length){
        System.out.println("#".repeat(length) + " "+ getCurrentTime());
    }
    private static  String getCurrentTime(){
        return (new SimpleDateFormat("HH:mm:ss.SSS")).format(new Timestamp(System.currentTimeMillis()));
    }
}
