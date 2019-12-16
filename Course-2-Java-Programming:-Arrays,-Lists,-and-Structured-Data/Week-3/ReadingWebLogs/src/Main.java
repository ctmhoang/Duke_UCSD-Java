import java.util.Date;

//Tester class
public class Main {
    public static void main(String[] args) {
        testLogAnalyzer();
    }
    public static void testLogEntry() {
        LogEntry le = new LogEntry("1.2.3.4", new Date(), "example request", 200, 500);
        System.out.println(le);
        LogEntry le2 = new LogEntry("1.2.100.4", new Date(), "example request 2", 300, 400);
        System.out.println(le2);
    }

    public static void testLogAnalyzer() {
        // complete method
        LogAnalyzer analyzer = new LogAnalyzer();
        analyzer.readFile("test/short-test_log");
        analyzer.printAll();
    }
}
