import edu.duke.DirectoryResource;
import edu.duke.FileResource;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        // write your code here
//        testColdestInFile();
//        testFileWithColdestTemperature();
        testLowestHumidityInFile();
    }

    public static CSVRecord coldestHourInFile(CSVParser parser) {
        CSVRecord coldestRec = null;
        for (CSVRecord currentRec : parser) {
            coldestRec = getColdestTemp(coldestRec, currentRec);
        }
        return coldestRec;
    }

    public static CSVRecord getColdestTemp(CSVRecord coldestRec, CSVRecord currentRec) {
        double currentTemp = Double.parseDouble(currentRec.get("TemperatureF"));
        if (currentTemp != -9999) {
            if (coldestRec == null) {
                coldestRec = currentRec;
            } else {
                double lowestTemp = Double.parseDouble(coldestRec.get("TemperatureF"));
                if (lowestTemp > currentTemp) {
                    coldestRec = currentRec;
                }
            }
        }
        return coldestRec;
    }

    public static void testColdestInFile() {
        FileResource f = new FileResource();
        CSVRecord coldestRow = coldestHourInFile(f.getCSVParser());
        System.out.println("Coldest temperature was " + coldestRow.get("TemperatureF") + " at " + coldestRow.get("DateUTC"));
    }

    public static String fileWithColdestTemperature() throws IOException {
        DirectoryResource dr = new DirectoryResource();
        CSVRecord coldestRec = null;
        File coldestFile = null;
        for (File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            CSVRecord currentRec = coldestHourInFile(fr.getCSVParser());
            if (coldestRec == null) {
                coldestRec = currentRec;
                coldestFile = f;
            } else {
                coldestRec = getColdestTemp(coldestRec, currentRec);
                coldestFile = f;
            }
        }
        assert coldestFile != null;
        return coldestFile.getCanonicalPath();
    }

    public static void testFileWithColdestTemperature() throws IOException {
        String abPath = fileWithColdestTemperature();
        File coldestFile = new File(abPath);
        FileResource coldestFr = new FileResource(coldestFile);
        String fileName = coldestFile.getName();
        Double coldestTemp = Double.parseDouble(
                coldestHourInFile(
                        coldestFr.getCSVParser()).get("TemperatureF")
        );
        System.out.println("Coldest day was in file " + fileName + "\n" +
                "Coldest temperature on that day was " + coldestTemp + " \n" +
                "All the Temperatures on the coldest day were: \n");
        for (CSVRecord rec : coldestFr.getCSVParser()) {
            double temp = Double.parseDouble(rec.get("TemperatureF"));
            String dateUTC = rec.get("DateUTC");
            System.out.println(temp + " " + dateUTC);
        }
    }

    public static CSVRecord lowestHumidityInFile(CSVParser parser) {
//        same as coldestHourInFile but add it as assignment requirement
        CSVRecord muggyRec = null;
        for (CSVRecord currentRec : parser) {
            muggyRec = getLeastMuggyRec(muggyRec, currentRec);
        }
        return muggyRec;
    }

    public static CSVRecord getLeastMuggyRec(CSVRecord muggyRec, CSVRecord currentRec) {
        String isCurrentHumidValid = currentRec.get("Humidity");
        if (isCurrentHumidValid.charAt(0) != 'N') {
            if (muggyRec == null) {
                muggyRec = currentRec;
            } else {
                double leastHumid = Double.parseDouble(muggyRec.get("Humidity"));
                double currHumid = Double.parseDouble(isCurrentHumidValid);
                if (leastHumid > currHumid) {
                    muggyRec = currentRec;
                }
            }
        }
        return muggyRec;
    }

    public static void testLowestHumidityInFile() {
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        CSVRecord csv = lowestHumidityInFile(parser);
        System.out.println("Lowest Humidity was " + csv.get("Humidity") + " at " + csv.get("DateUTC"));
    }

}
