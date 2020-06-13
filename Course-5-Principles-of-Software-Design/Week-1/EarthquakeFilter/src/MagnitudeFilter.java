public class MagnitudeFilter implements Filter
{
    private final double minMag;
    private final double maxMag;

    public MagnitudeFilter(double min, double max)
    {
        if(min > max) throw new IllegalArgumentException();
        minMag = min;
        maxMag = max;
    }

    @Override
    public boolean satisfies(QuakeEntry qe)
    {
        double mag = qe.getMagnitude();
        return mag >= minMag && mag <= maxMag;
    }
}
