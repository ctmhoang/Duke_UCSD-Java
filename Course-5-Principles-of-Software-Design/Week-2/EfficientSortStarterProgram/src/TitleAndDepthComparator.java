import java.util.Comparator;

public class TitleAndDepthComparator implements Comparator<QuakeEntry>
{
    @Override
    public int compare(QuakeEntry quakeEntry, QuakeEntry t1)
    {
         int res = quakeEntry.getInfo().compareTo(t1.getInfo());
         if(res == 0) return Double.compare(quakeEntry.getDepth(), t1.getDepth());
         return res;
    }
}
