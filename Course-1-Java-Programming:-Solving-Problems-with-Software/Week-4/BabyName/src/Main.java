import edu.duke.DirectoryResource;
import edu.duke.FileResource;
import edu.duke.StorageResource;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        // write your code here
//        FileResource fr = new FileResource();
//        totalBirths(fr);
//        System.out.println(getRank(2012, "Mason", "M"));
//        System.out.println(getName(2012, 1, "M"));
//        whatIsNameInYear("Isabella", 2012, 2014,"F");
//        System.out.println(yearOfHighestRank("Mason", "M"));
//        System.out.println(getAverageRank("Jacob", "M"));
//        System.out.println(getTotalBirthsRankedHigher(1990, "Drew", "M"));
    }

    public static void totalBirths(FileResource fr) {
        int numOfGirls = 0, numOfBoys = 0, tot = 0;
        for (CSVRecord rec : fr.getCSVParser(false)) {
//            int currNum = Integer.parseInt(rec.get(2));
            tot ++;
            if (rec.get(1).equals("F")) {
                numOfGirls ++;
            } else {
                numOfBoys ++;
            }
        }
        System.out.println("Number of girls names :" + numOfGirls + "\n" +
                "Number of boys names: " + numOfBoys + "\n" +
                "Total number: " + tot);
    }

    public static FileResource getFile(int year) throws IOException {
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            if (f.getName().contains(Integer.toString(year))) {
                return new FileResource(f.getCanonicalFile().toString());
            }
        }
        return null;
    }

    public static int getRank(int year, String name, String gender) throws IOException {
        int rank = 0;
        FileResource fr = getFile(year);
        if (fr == null) {
            return -1;
        }
        for (CSVRecord rc : fr.getCSVParser(false)) {
            if (gender.equals(rc.get(1))) {
                rank++;
                if (name.equalsIgnoreCase(rc.get(0))) {
                    return rank;
                }
            }
        }
        return -1;
    }

    public static String getName(int year, int rank, String gender) throws IOException {
        FileResource fr = getFile(year);
        int counter = 0;
        if (fr == null) {
            return "NO NAME";
        }
        for (CSVRecord rec : fr.getCSVParser(false)) {
            if (gender.equals(rec.get(1))) {
                counter++;
                if (counter == rank) {
                    return rec.get(0);
                }
            }
        }
        return "NO NAME";
    }

    public static void whatIsNameInYear(String name, int year, int newYear, String gender) throws IOException {
        int rank = getRank(year, name, gender);
        String newName = getName(newYear, rank, gender);
        System.out.println(name + " born in " + year + " would be " + newName + " if she/he was born in " + newYear);
    }

    public static int yearOfHighestRank(String name, String gender) {
        int rank = 0, year = -1;
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            FileResource data = new FileResource(f);
            int currRank = 0;
            for (CSVRecord rec : data.getCSVParser(false)) {
                if (rec.get(1).equals(gender)) {
                    currRank++;
                    if (rec.get(0).equalsIgnoreCase(name) && (rank == 0 || rank > currRank)) {
                        rank = currRank;
                        year = Integer.parseInt(f.getName().replaceAll("\\D*", ""));
                    }
                }
            }
        }
        return year;
    }

    public static double getAverageRank(String name, String gender) {
        int totRank = 0, year = 0;
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            FileResource data = new FileResource(f);
            int currRank = 0;
            for (CSVRecord rec : data.getCSVParser(false)) {
                if (rec.get(1).equals(gender)) {
                    currRank++;
                    if (rec.get(0).equalsIgnoreCase(name)) {
                        totRank += currRank;
                        year++;
                    }
                }
            }
        }
        return (double) totRank / year;
    }

    public static int getTotalBirthsRankedHigher(int year, String name, String gender) throws IOException {
        FileResource fr = getFile(year);
        if (fr == null) {
            return -1;
        }
        int tot = 0;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            if (rec.get(1).equals(gender)) {
                if (rec.get(0).equalsIgnoreCase(name)) {
                    return tot;
                }
                tot += Integer.parseInt(rec.get(2));
            }
        }
        return -1;
    }

}