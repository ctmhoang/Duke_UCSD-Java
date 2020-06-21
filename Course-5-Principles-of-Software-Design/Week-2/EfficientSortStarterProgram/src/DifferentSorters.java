
/**
 * Write a description of class DifferentSorters here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.util.*;

public class DifferentSorters
{

    public static void main(String[] args)
    {
//        sortWithCompareTo();
        sortByLastWordInTitleThenByMagnitude();
    }

    public static void sortWithCompareTo()
    {
        EarthQuakeParser parser = new EarthQuakeParser();
        String source = "data/nov20quakedata.atom";
        //String source = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.atom";
        ArrayList<QuakeEntry> list = parser.read(source);
        Collections.sort(list);
        for (QuakeEntry qe : list)
        {
            System.out.println(qe);
        }

        int quakeNumber = 10;
        System.out.println("Print quake entry in position " + quakeNumber);
        System.out.println(list.get(quakeNumber));
    }

    public static void sortByMagnitude()
    {
        EarthQuakeParser parser = new EarthQuakeParser();
        String source = "data/nov20quakedata.atom";
        //String source = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.atom";
        ArrayList<QuakeEntry> list = parser.read(source);
        Collections.sort(list, new MagnitudeComparator());
        for (QuakeEntry qe : list)
        {
            System.out.println(qe);
        }

    }

    public static void sortByDistance()
    {
        EarthQuakeParser parser = new EarthQuakeParser();
        String source = "data/nov20quakedata.atom";
        //String source = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.atom";
        ArrayList<QuakeEntry> list = parser.read(source);
        // Location is Durham, NC
        Location where = new Location(35.9886, -78.9072);
        Collections.sort(list, new DistanceComparator(where));
        for (QuakeEntry qe : list)
        {
            System.out.println(qe);
        }
    }

    public static void sortByLastWordInTitleThenByMagnitude()
    {
        EarthQuakeParser parser = new EarthQuakeParser();
        ArrayList<QuakeEntry> quakeData = parser.read("data/nov20quakedata.atom");
        Collections.sort(quakeData, new TitleLastAndMagnitudeComparator());
        for (QuakeEntry qe : quakeData)
        {
            System.out.println(qe);
        }

        int quakeNumber = 10;
        System.out.println("Print quake entry in position " + quakeNumber);
        System.out.println(quakeData.get(quakeNumber));
    }
}
