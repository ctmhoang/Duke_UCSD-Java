package week_3;

public class GenreFilter implements Filter
{
    private final String genre;

    public GenreFilter(String genre)
    {
        this.genre = genre.toLowerCase();
    }

    @Override
    public boolean satisfies(String id)
    {
        return MovieDatabase.getGenres(id).toLowerCase().contains(genre);
    }
}
