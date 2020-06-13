public class DistanceFilter implements Filter
{
    private final Location currLoc;
    private final double maxDist;

    public DistanceFilter(Location loc, double maxDistance)
    {
        currLoc = loc;
        maxDist = maxDistance;
    }
    @Override
    public boolean satisfies(QuakeEntry qe)
    {
        return qe.getLocation().distanceTo(currLoc) < maxDist;
    }
}
