import java.util.*;
import java.util.stream.Collectors;

import edu.duke.*;

public class EarthQuakeClient {
    public EarthQuakeClient() {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args)
    {
//        createCSV();
//        bigQuakes();
        closeToMe();
    }

    public static ArrayList<QuakeEntry> filterByMagnitude(ArrayList<QuakeEntry> quakeData,
    double magMin) {
        ArrayList<QuakeEntry> answer = new ArrayList<QuakeEntry>();
        quakeData.stream().filter(q -> q.getMagnitude() > magMin).forEach(answer::add);
        return answer;
    }

    public static ArrayList<QuakeEntry> filterByDistanceFrom(ArrayList<QuakeEntry> quakeData,
    double distMax,
    Location from) {
        ArrayList<QuakeEntry> answer = new ArrayList<QuakeEntry>();
        quakeData.stream().filter(l -> l.getLocation().distanceTo(from) < distMax).forEach(answer::add);
        return answer;
    }

    public static void dumpCSV(ArrayList<QuakeEntry> list){
        System.out.println("Latitude,Longitude,Magnitude,Info");
        for(QuakeEntry qe : list){
            System.out.printf("%4.2f,%4.2f,%4.2f,%s\n",
                qe.getLocation().getLatitude(),
                qe.getLocation().getLongitude(),
                qe.getMagnitude(),
                qe.getInfo());
        }

    }

    public static void bigQuakes() {
        EarthQuakeParser parser = new EarthQuakeParser();
        //String source = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.atom";
        String source = "data/nov20quakedata.atom";
        ArrayList<QuakeEntry> list  = parser.read(source);
        System.out.println("read data for "+list.size()+" quakes");

        List<QuakeEntry> resList = filterByMagnitude(list,5);
        resList.forEach(System.out::println);
        System.out.printf("Found %d quakes that match that criteria",resList.size());
    }

    public static void closeToMe(){
        EarthQuakeParser parser = new EarthQuakeParser();
//        String source = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.atom";
        String source = "data/nov20quakedatasmall.atom";
        ArrayList<QuakeEntry> list  = parser.read(source);
        System.out.println("read data for "+list.size()+" quakes");

//        // This location is Durham, NC
//        Location city = new Location(35.988, -78.907);

//         This location is Bridgeport, CA
         Location city =  new Location(38.17, -118.82);

        List<QuakeEntry> resList = filterByDistanceFrom(list,1_000_000,city);
        resList.forEach(q -> System.out.println(q.getLocation().distanceTo(city) + " " + q.getInfo()));
        System.out.printf("Found %d quakes that match that criteria",resList.size());

    }

    public static void createCSV(){
        EarthQuakeParser parser = new EarthQuakeParser();
        String source = "data/nov20quakedatasmall.atom";
        //String source = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.atom";
        ArrayList<QuakeEntry> list  = parser.read(source);
        dumpCSV(list);
        System.out.println("# quakes read: " + list.size());
        for (QuakeEntry qe : list) {
            System.out.println(qe);
        }
    }
    
}
