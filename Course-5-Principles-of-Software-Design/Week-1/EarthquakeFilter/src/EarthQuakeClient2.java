import java.util.*;

import edu.duke.*;

public class EarthQuakeClient2
{

    public static void main(String[] args)
    {
        quakesWithFilter();
    }

    public EarthQuakeClient2()
    {
        // TODO Auto-generated constructor stub
    }

    public static ArrayList<QuakeEntry> filter(ArrayList<QuakeEntry> quakeData, Filter f)
    {
        ArrayList<QuakeEntry> answer = new ArrayList<QuakeEntry>();
        for (QuakeEntry qe : quakeData)
        {
            if (f.satisfies(qe))
            {
                answer.add(qe);
            }
        }

        return answer;
    }

    public static void quakesWithFilter()
    {
        EarthQuakeParser parser = new EarthQuakeParser();
        //String source = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.atom";
        String source = "data/nov20quakedatasmall.atom";
        ArrayList<QuakeEntry> list = parser.read(source);
        System.out.println("read data for " + list.size() + " quakes");

//        Filter f = new MinMagFilter(4.0);
//        ArrayList<QuakeEntry> m7  = filter(list, f);
//        for (QuakeEntry qe: m7) {
//            System.out.println(qe);
//        }

//        Filter magF = new MagnitudeFilter(4.0,5.0);
//        Filter depthF = new DepthFilter(-35_000,-12_000);
//        filter(filter(list,magF),depthF).parallelStream().forEach(System.out::println);

//        Location tokyo = new Location(35.42, 139.43);
//        Filter disF = new DistanceFilter(tokyo, 10_000_000);
//        Filter closeToJap = new PhraseFilter("Japan", "end");
//        filter(filter(list, closeToJap), disF).parallelStream().forEach(System.out::println);

    }

    public static void createCSV()
    {
        EarthQuakeParser parser = new EarthQuakeParser();
        //String source = "../data/nov20quakedata.atom";
        String source = "data/nov20quakedatasmall.atom";
        //String source = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.atom";
        ArrayList<QuakeEntry> list = parser.read(source);
        dumpCSV(list);
        System.out.println("# quakes read: " + list.size());
    }

    public static void dumpCSV(ArrayList<QuakeEntry> list)
    {
        System.out.println("Latitude,Longitude,Magnitude,Info");
        for (QuakeEntry qe : list)
        {
            System.out.printf("%4.2f,%4.2f,%4.2f,%s\n",
                    qe.getLocation().getLatitude(),
                    qe.getLocation().getLongitude(),
                    qe.getMagnitude(),
                    qe.getInfo());
        }
    }

}
