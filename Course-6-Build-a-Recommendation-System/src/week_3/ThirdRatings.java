package week_3;

import week_1.Rating;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.stream.Collectors;

public class ThirdRatings
{
    private final ArrayList<Rater> myRaters;

    public ThirdRatings()
    {
        // default constructor
        this( "data/ratings.csv");
    }

    public ThirdRatings(String ratingsFile)
    {
        FirstRatings firstRatings = new FirstRatings();
        myRaters = firstRatings.loadRaters(ratingsFile);
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
        return MovieDatabase.filterBy(new TrueFilter()).parallelStream().map(m -> new Rating(m, getAverageByID(m, minimalRaters))).filter(r -> r.getValue() != 0).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Rating> getAverageRatingsByFilter(int minimalRaters, Filter filterCriteria)
    {
        return MovieDatabase.filterBy(filterCriteria).parallelStream().map(mid -> new Rating(mid, getAverageByID(mid,minimalRaters))).filter(r -> r.getValue() != 0).collect(Collectors.toCollection(ArrayList::new));
    }

}
