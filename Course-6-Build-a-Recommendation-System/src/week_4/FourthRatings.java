package week_4;

import week_1.Rating;
import week_3.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

public class FourthRatings
{

    private double getAverageByID(String id, int minimalRaters)
    {
        DoubleSummaryStatistics ratings = RaterDatabase.getRaters().parallelStream().mapToDouble(rater -> rater.getRating(id)).filter(point -> point != -1).summaryStatistics();
        if (ratings.getCount() < minimalRaters) return 0;
        return ratings.getAverage();
    }

    public ArrayList<Rating> getAverageRatings(int minimalRaters)
    {
        return MovieDatabase.filterBy(new TrueFilter()).parallelStream().map(m -> new Rating(m, getAverageByID(m, minimalRaters))).filter(r -> r.getValue() != 0).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Rating> getAverageRatingsByFilter(int minimalRaters, Filter filterCriteria)
    {
        return MovieDatabase.filterBy(filterCriteria).parallelStream().map(mid -> new Rating(mid, getAverageByID(mid, minimalRaters))).filter(r -> r.getValue() != 0).collect(Collectors.toCollection(ArrayList::new));
    }

    private double dotProduct(Rater me, Rater r)
    {
        ArrayList<String> res = me.getItemsRated();
        res.retainAll(r.getItemsRated());
        List<Rater> zip = List.of(me, r);
        return res.parallelStream().mapToDouble(mid -> zip.parallelStream().mapToDouble(rater -> rater.getRating(mid) - 5).reduce(1, (rs, next) -> rs * next)).sum();
    }

    private ArrayList<Rating> getSimilarities(String id)
    {
        Rater me = RaterDatabase.getRater(id);
        List<Rater> tmp = RaterDatabase.getRaters();
        tmp.remove(me);
        return tmp.parallelStream().map(rater -> new Rating(rater.getID(), dotProduct(me, rater))).filter(rating -> rating.getValue() < 0).sorted(Collections.reverseOrder()).collect(Collectors.toCollection(ArrayList::new));
    }
}
