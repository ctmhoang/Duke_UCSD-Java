public class MinutesFilter implements Filter
{
    private final int min, max;

    public MinutesFilter(int min, int max)
    {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean satisfies(String id)
    {
        return MovieDatabase.getMinutes(id) >= min && MovieDatabase.getMinutes(id) <= max;
    }
}
