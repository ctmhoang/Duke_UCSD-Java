package week_3;

import java.util.Arrays;

public class DirectorsFilter implements Filter
{
    private final String[] director;

    public DirectorsFilter(String directors)
    {
        this.director = directors.split("\\s*,\\s*");
    }

    @Override
    public boolean satisfies(String id)
    {
        String movieDir = MovieDatabase.getDirector(id);
        return Arrays.stream(director).anyMatch(movieDir::contains);
    }
}
