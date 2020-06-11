import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LargestQuakes
{
    public static void main(String[] args)
    {
        findLargestQuakes();
    }

    public static void findLargestQuakes()
    {
        EarthQuakeParser parser = new EarthQuakeParser();
        String source = "data/nov20quakedatasmall.atom";
        ArrayList<QuakeEntry> list = parser.read(source);
//        list.forEach(System.out::println);
        System.out.println("read data for " + list.size() + " quakes");
//        System.out.println("Largest Magnitude quakes in is at index " + indexOfLargest(list) + " has magnitude " + list.get(indexOfLargest(list)).getMagnitude());
        getLargest(list, 5).forEach(System.out::println);

    }

    public static int indexOfLargest(ArrayList<QuakeEntry> data)
    {
        return data.indexOf(Collections.max(data, Comparator.comparingDouble(QuakeEntry::getMagnitude)));
    }

    public static ArrayList<QuakeEntry> getLargest(ArrayList<QuakeEntry> quakeData, int howMany)
    {
        ArrayList<QuakeEntry> clone = new ArrayList<>(quakeData);
        if (howMany >= quakeData.size())
        {
            clone.sort(Comparator.comparingDouble(k -> k.getMagnitude()));
            return clone;
        }
        return IntStream.range(0, howMany).mapToObj(k ->
                clone.remove(indexOfLargest(clone))
        ).collect(Collectors.toCollection(ArrayList::new));
    }
}
