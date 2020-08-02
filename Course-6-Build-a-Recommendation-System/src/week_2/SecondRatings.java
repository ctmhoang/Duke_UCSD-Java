package week_2;
/**
 * Write a description of week_2.SecondRatings here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import week_1.FirstRatings;
import week_1.Movie;
import week_1.Rater;
import week_1.Rating;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.stream.Collectors;

public class SecondRatings
{
    private final ArrayList<Movie> myMovies;
    private final ArrayList<Rater> myRaters;

    public SecondRatings()
    {
        // default constructor
        this("data/ratedmoviesfull.csv", "data/ratings.csv");
    }

    public SecondRatings(String movieFile, String ratingsFile)
    {
        FirstRatings firstRatings = new FirstRatings();
        myMovies = firstRatings.loadMovies(movieFile);
        myRaters = firstRatings.loadRaters(ratingsFile);
    }

    public int getMovieSize()
    {
        return myMovies.size();
    }

    public int getRaterSize()
    {
        return myRaters.size();
    }

    private double getAverageByID(String id, int minimalRaters)
    {
        DoubleSummaryStatistics ratings = myRaters.parallelStream().mapToDouble(rater -> rater.getRating(id)).filter(point -> point != -1).summaryStatistics();
        if (ratings.getCount() < minimalRaters) return 0;
        return ratings.getAverage();
    }

    public ArrayList<Rating> getAverageRatings(int minimalRaters)
    {
        return myMovies.parallelStream().map(m -> new Rating(m.getID(), getAverageByID(m.getID(), minimalRaters))).filter(r -> r.getValue() != 0).collect(Collectors.toCollection(ArrayList::new));
    }

    public String getTitle(String id)
    {
        return myMovies.parallelStream().filter(m -> m.getID().equals(id)).findAny().map(Movie::getTitle).orElse(null);
    }

    public String getID(String title)
    {
        return myMovies.stream().filter(m -> m.getTitle().equals(title)).findAny().map(Movie::getID).orElse("NO SUCH TITLE.");
    }
}
