
/**
 * Write a description of class QuakeSortInPlace here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.util.*;
import java.util.stream.Stream;

import edu.duke.*;

public class QuakeSortInPlace
{
    public QuakeSortInPlace()
    {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args)
    {
        testSort();
    }

    public static int getSmallestMagnitude(ArrayList<QuakeEntry> quakes, int from)
    {
        int minIdx = from;
        for (int i = from + 1; i < quakes.size(); i++)
        {
            if (quakes.get(i).getMagnitude() < quakes.get(minIdx).getMagnitude())
            {
                minIdx = i;
            }
        }
        return minIdx;
    }

    public static void sortByMagnitude(ArrayList<QuakeEntry> in)
    {

        for (int i = 0; i < in.size(); i++)
        {
            int minIdx = getSmallestMagnitude(in, i);
            QuakeEntry qi = in.get(i);
            QuakeEntry qmin = in.get(minIdx);
            in.set(i, qmin);
            in.set(minIdx, qi);
        }

    }

    public static void testSort()
    {
        EarthQuakeParser parser = new EarthQuakeParser();
        //String source = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.atom";
//        String source = "data/nov20quakedatasmall.atom";
        String source = "data/earthquakeDataSampleSix2.atom";
        //String source = "data/nov20quakedata.atom";
        ArrayList<QuakeEntry> list = parser.read(source);

        System.out.println("read data for " + list.size() + " quakes");
//        sortByMagnitude(list);
//        sortByLargestDepth(list);
//        sortByMagnitudeWithBubbleSort(list);
//        sortByMagnitudeWithBubbleSortWithCheck(list);
//        sortByMagnitudeWithCheck(list);
        for (QuakeEntry qe : list)
        {
            System.out.println(qe);
        }

    }

    public void createCSV()
    {
        EarthQuakeParser parser = new EarthQuakeParser();
        //String source = "data/nov20quakedata.atom";
        String source = "data/nov20quakedatasmall.atom";
        //String source = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.atom";
        ArrayList<QuakeEntry> list = parser.read(source);
        dumpCSV(list);
        System.out.println("# quakes read: " + list.size());
    }

    public void dumpCSV(ArrayList<QuakeEntry> list)
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

    public static int getLargestDepth(ArrayList<QuakeEntry> quakeData, int from)
    {
        return quakeData.indexOf(Collections.max(quakeData.subList(from, quakeData.size()), Comparator.comparingDouble(QuakeEntry::getDepth)));
    }

    public static void sortByLargestDepth(ArrayList<QuakeEntry> in)
    {
        for (int i = 0; i < in.size(); i++)
        {
            int maxIdx = getLargestDepth(in, i);
            if (i == maxIdx) continue;
            QuakeEntry tmp = in.get(maxIdx);
            in.set(maxIdx, in.get(i));
            in.set(i, tmp);
        }
    }

    public static boolean onePassBubbleSort(ArrayList<QuakeEntry> quakeData, int numSorted)
    {
        if (numSorted == quakeData.size() - 1) return true;
        boolean isSwapped = false;
        for (int i = 0, k = quakeData.size() - 1; i < k; i++)
        {
            QuakeEntry q1 = quakeData.get(i);
            QuakeEntry q2 = quakeData.get(i + 1);
            if (q1.getMagnitude() > q2.getMagnitude())
            {
                quakeData.set(i + 1, q1);
                quakeData.set(i, q2);
                isSwapped = true;
            }
        }
        return !isSwapped;
        //Return true if it's already sorted
    }

    public static void sortByMagnitudeWithBubbleSort(ArrayList<QuakeEntry> in)
    {
        Stream.iterate(0, i -> !onePassBubbleSort(in, i), i -> ++i).forEach(i -> onePassBubbleSort(in, i));
    }

    public static boolean checkInSortedOrder(ArrayList<QuakeEntry> quakes)
    {
        for (int i = 0, k = quakes.size() - 1; i < k; i++)
        {
            QuakeEntry q1 = quakes.get(i);
            QuakeEntry q2 = quakes.get(i + 1);
            if (q1.getMagnitude() > q2.getMagnitude())
                return false;
        }
        return true;
    }

    public static void sortByMagnitudeWithBubbleSortWithCheck(ArrayList<QuakeEntry> in)
    {
        int i = -1, k = in.size() - 1;
        for (; i++ < k; )
        {
            if (checkInSortedOrder(in)) break;
            onePassBubbleSort(in, i);
        }
        System.out.println("Passes: " + i);
    }

    public static void sortByMagnitudeWithCheck(ArrayList<QuakeEntry> in)
    {
        int i = 0;
        for (int k = in.size() - 1; i < k; i++)
        {
            if (checkInSortedOrder(in)) break;
            int minIdx = getSmallestMagnitude(in, i);
            QuakeEntry qi = in.get(i);
            QuakeEntry qmin = in.get(minIdx);
            in.set(i, qmin);
            in.set(minIdx, qi);
        }
        System.out.println("Passes: " + i);
    }
}

