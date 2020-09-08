package week_4_fp;

import week_1.Rating;
import week_3.MovieDatabase;
import week_3.YearAfterFilter;
import week_4.FourthRatings;
import week_4.RaterDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RecommendationRunner implements Recommender
{

    private final int SIML_RATER = 11;
    private final int MIN_RATER = 3;
    private final int RECOMMEND_MOVIE = 10;

    @Override
    public ArrayList<String> getItemsToRate()
    {
        List<String> data = MovieDatabase.filterBy(new YearAfterFilter(2005));
        return IntStream.range(0, ThreadLocalRandom.current().nextInt(10, 20)).mapToObj(i ->
                data.remove(ThreadLocalRandom.current().nextInt(data.size()))
        ).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void printRecommendationsFor(String webRaterID)
    {
        FourthRatings ratings = new FourthRatings();
        List<Rating> rec = ratings.getSimilarRatings(webRaterID, SIML_RATER, MIN_RATER);
        if (rec.isEmpty())
        {
            System.out.println("<h1>404</h1>");
            System.out.println("<p>This page is lost :(</p>");
            return;
        }
        System.out.println("<table align=\"center\" border=\"black solid 1px\" width=100%>");
        System.out.println("<caption>\n" +
                "        Movie recommendation\n" +
                "      </caption>\n" +
                "      <thead>\n" +
                "        <th>No</th>" +
                "        <th>Title</th>\n" +
                "        <th>Year</th>\n" +
                "        <th>Country</th>\n" +
                "        <th>Genre</th>\n" +
                "        <th>Poster</th>\n" +
                "      </thead>");
        int size = Math.min(rec.size(), RECOMMEND_MOVIE);
        for (int i = 0; i < size; i++)
        {
            try
            {
                while (RaterDatabase.getRater(webRaterID).hasRating(rec.get(i).getItem()))
                {
                    rec.remove(i);
                }
                printMovieCell(rec.get(i).getItem(), i);
            } catch (Exception e)
            {
                break;
            }
        }
        System.out.println("</table>");
    }

    private void printMovieCell(String id, int i)
    {
        System.out.println("<tr>");
        System.out.printf("<td>%s</td>", i + 1);
        System.out.printf("<td>%s</td>", MovieDatabase.getTitle(id));
        System.out.printf("<td>%s</td>", MovieDatabase.getYear(id));
        System.out.printf("<td>%s</td>", MovieDatabase.getCountry(id));
        System.out.printf("<td>%s</td>", MovieDatabase.getGenres(id));
        System.out.printf("<td><img src=\"%s\" alt=\"%s\"></td>", MovieDatabase.getPoster(id), MovieDatabase.getTitle(id));
        System.out.println("</tr>");
    }
}
