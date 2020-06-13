import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

public class MatchAllFilter implements Filter
{
    private List<Filter> filters;

    public MatchAllFilter()
    {
        filters = new LinkedList<>();
    }

    public void addFilter(Filter f)
    {
        filters.add(f);
    }

    @Override
    public boolean satisfies(QuakeEntry qe)
    {
        return filters.parallelStream().allMatch(f -> f.satisfies(qe));
    }

    @Override
    public String getName()
    {
        StringJoiner res = new StringJoiner(" ");
        filters.stream().map(Filter::getName).forEach(res::add);
        return res.toString();
    }
}
