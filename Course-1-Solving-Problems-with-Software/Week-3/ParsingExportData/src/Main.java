import edu.duke.FileResource;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Main {

    public static void main(String[] args) {
        // Use each command one by one by comment others out.
        //If wanna run all at once. Need to reset CSVParser at each run ;)
        CSVParser p = tester();
//        countryInfo(p, "Germany");
//        listExportersTwoProducts(p, "gold","diamonds");
//        System.out.println(numberOfExporters(p, "gold"));
//        bigExporters(p, "$999,999,999");
    }

    public static CSVParser tester() {
        FileResource fr = new FileResource();
        return fr.getCSVParser();
    }

    public static void countryInfo(CSVParser parser, String country) {
        boolean found = false;
        for (CSVRecord record : parser) {
            if (record.get("Country").equals(country)) {
                System.out.println(record.get("Country") + " : " + record.get("Exports") + " : "
                        + record.get("Value (dollars)"));
                found = true;
            }
        }
        if (!found) {
            System.out.println("NOT FOUND");
        }
    }

    public static void listExportersTwoProducts(CSVParser parser, String exportItem1, String exportItem2) {
        for (CSVRecord record : parser) {
            String exportItems = record.get("Exports");
            if (exportItems.contains(exportItem1) && exportItems.contains(exportItem2)) {
                System.out.println(record.get("Country"));
            }
        }
    }

    public static int numberOfExporters(CSVParser parser, String exportItem) {
        int count = 0;
        for (CSVRecord record : parser) {
            if (record.get("Exports").contains(exportItem)) {
                count++;
            }
        }
        return count;
    }

    public static void bigExporters(CSVParser parser, String amount) {
        if (amount.charAt(0) != '$') {
            System.out.println("INVALID INPUT. DID YOU FORGET TO PUT '$' ?");
        } else {
            int aLen = amount.length();
            for (CSVRecord record : parser) {
                int eRLen = record.get("Value (dollars)").length();
                if (eRLen > aLen) {
                    System.out.println(record.get("Country") + " " + record.get("Value (dollars)"));
                } else if (eRLen == aLen) {
                    String value = record.get("Value (dollars)");
                    for (int i = 1; i < aLen; i++) {
                        if (value.charAt(i) > amount.charAt(i)) {
                            System.out.println(record.get("Country") + " " + record.get("Value (dollars)"));
                            break;
                        }
                    }
                }
            }
        }
    }

}
