import java.util.Date;

//Tester class
public class Main {
    public static void main(String[] args) {
//        testLogAnalyzer();
        testUniqueIP();
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

    public static void testUniqueIP() {
        LogAnalyzer analyzer = new LogAnalyzer();
        analyzer.readFile("test/weblog3-short_log");
//        System.out.println(analyzer.countUniqueIPs());
//        analyzer.printAllHigherThanNum(400);
//        System.out.println(analyzer.uniqueIPVisitOnDay("Sep 30").size());
//        System.out.println(analyzer.countUniqueIPsInRange(300,399));
//        System.out.println(analyzer.countVisitsPerIP().toString());
//        System.out.println(analyzer.mostNumberVisitsByIP(analyzer.countVisitsPerIP()));
//        System.out.println(analyzer.iPsMostVisits(analyzer.countVisitsPerIP()));
//        System.out.println(analyzer.iPsForDays());
//        System.out.println(analyzer.dayWithMostIPVisits(analyzer.iPsForDays()));
//        System.out.println(analyzer.iPsWithMostVisitsOnDay(analyzer.iPsForDays(),"Sep 30"));
    }
}