
/**
 * Write a description of class LogAnalyzer here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import edu.duke.FileResource;

import java.util.ArrayList;
import java.util.HashSet;


public class LogAnalyzer {
    private ArrayList<LogEntry> records;

    public LogAnalyzer() {
        // complete constructor
        records = new ArrayList<>();
    }

    public void readFile(String filename) {
        // complete method
        FileResource resource = new FileResource(filename);
        for (String line : resource.lines()) {
            records.add(WebLogParser.parseEntry(line));
        }
    }

    public void printAll() {
        for (LogEntry le : records) {
            System.out.println(le);
        }
    }

    public int countUniqueIPs() {
        HashSet<String> uniqueIPs = new HashSet<>();
        for (LogEntry logEntry : records) {
            uniqueIPs.add(logEntry.getIpAddress());
        }
        return uniqueIPs.size();
    }

    public void printAllHigherThanNum(int num) {
        for (LogEntry logEntry : records) {
            if (logEntry.getStatusCode() > num) {
                System.out.println(logEntry);
            }
        }
    }

    public ArrayList<String> uniqueIPVisitOnDay(String someday) {
        ArrayList<String> uniqueIPInDay = new ArrayList<>();
        for (LogEntry logEntry : records) {
            if (logEntry.getAccessTime().toString().toUpperCase().contains(someday.toUpperCase()) && !uniqueIPInDay.contains(logEntry.getIpAddress())) {
                uniqueIPInDay.add(logEntry.getIpAddress());
            }
        }
        return uniqueIPInDay;
    }

    public int countUniqueIPsInRange(int low, int high) {
        ArrayList<String> ipsList = new ArrayList<>();
        for (LogEntry logEntry : records) {
            int statusCode = logEntry.getStatusCode();
            String ip = logEntry.getIpAddress();
            if (!ipsList.contains(ip) && statusCode <= high && statusCode >= low) {
                ipsList.add(ip);
            }
        }
        return ipsList.size();
    }
}
