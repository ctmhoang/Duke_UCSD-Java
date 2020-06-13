public class DepthFilter implements Filter
{
    private final double minDepth;
    private final double maxDepth;

    public DepthFilter(double min, double max)
    {
        if (min > max) throw new IllegalArgumentException();
        maxDepth = max;
        minDepth = min;
    }
    @Override
    public boolean satisfies(QuakeEntry qe)
    {
        double tmp = qe.getDepth();
        return tmp >= minDepth && tmp <= maxDepth;
    }
}
