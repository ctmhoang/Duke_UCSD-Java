import java.util.ArrayList;
import java.util.Comparator;

public class MovieRunnerAverage
{
    public static void main(String[] args)
    {
//        printAverageRatings();
//        getAverageRatingOneMovie();
    }

    public static void printAverageRatings()
    {
//        SecondRatings ratings = new SecondRatings();
        SecondRatings ratings = new SecondRatings("data/ratedmovies_short.csv", "data/ratings_short.csv");
        System.out.println("Number of movies is " + ratings.getMovieSize());
        System.out.println("Number of raters is " + ratings.getRaterSize());

        ArrayList<Rating> tmp = ratings.getAverageRatings(3);
        tmp.sort(Comparator.comparingDouble(Rating::getValue));
        tmp.stream().map(r -> r.getValue() + " " + ratings.getTitle(r.getItem())).forEach(System.out::println);

    }

    public static void getAverageRatingOneMovie(){
        SecondRatings ratings = new SecondRatings("data/ratedmovies_short.csv", "data/ratings_short.csv");

        String title  = "The Godfather";
        ratings.getAverageRatings(0).stream().filter(r -> r.getItem().equals(ratings.getID(title))).findAny().map(Rating::getValue).ifPresentOrElse(System.out::println, () -> System.out.println("Cannot find"));
    }
}
