import java.util.Comparator;

public class TitleLastAndMagnitudeComparator implements Comparator<QuakeEntry>
{
    @Override
    public int compare(QuakeEntry quakeEntry, QuakeEntry t1)
    {
        String q1 = quakeEntry.getInfo();
        String q2 = t1.getInfo();
        int res = q1.substring(q1.lastIndexOf(" ")).compareTo(q2.substring(q2.lastIndexOf(" ")));
        if (res == 0) return Double.compare(quakeEntry.getMagnitude(), t1.getMagnitude());
        return res;
    }
}
