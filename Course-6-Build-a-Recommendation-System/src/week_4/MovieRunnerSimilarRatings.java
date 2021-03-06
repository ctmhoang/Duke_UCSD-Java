package week_4;

import week_1.Rating;
import week_3.*;

import java.util.ArrayList;
import java.util.Comparator;


public class MovieRunnerSimilarRatings
{

    public static void main(String[] args)
    {
//        printAverageRatings();
//        printAverageRatingsByYearAfterAndGenre();
//        printSimilarRatings();
//        printSimilarRatingsByGenre();
//        printSimilarRatingsByDirector();
//        printSimilarRatingsByGenreAndMinutes();
//        printSimilarRatingsByYearAfterAndMinutes();
    }

    private static FourthRatings init()
    {
        FourthRatings ratings = new FourthRatings();
        RaterDatabase.initialize("ratings.csv");
        System.out.println("Number of raters is " + RaterDatabase.size());

        MovieDatabase.initialize("ratedmoviesfull.csv");
        System.out.println("Number of movies is " + MovieDatabase.size() + "\n");
        return ratings;
    }

    public static void printAverageRatings()
    {
        FourthRatings ratings = init();

        ArrayList<Rating> tmp = ratings.getAverageRatings(1);
        System.out.println("Found: " + tmp.size());
        tmp.sort(Comparator.comparingDouble(Rating::getValue));
        tmp.stream().map(r -> r.getValue() + " " + MovieDatabase.getTitle(r.getItem())).forEach(System.out::println);
    }

    public static void printAverageRatingsByYearAfterAndGenre()
    {
        FourthRatings ratings = init();

        AllFilters al = new AllFilters();
        al.addFilter(new YearAfterFilter(1980));
        al.addFilter(new GenreFilter("Romance"));
        ArrayList<Rating> tmp = ratings.getAverageRatingsByFilter(1, al);
        System.out.println("Found: " + tmp.size());
        tmp.sort(Comparator.comparingDouble(Rating::getValue));
        tmp.stream().map(r -> r.getValue() + " " + MovieDatabase.getTitle(r.getItem()) + "\n\t" + MovieDatabase.getYear(r.getItem()) + "\t" + MovieDatabase.getGenres(r.getItem())).forEach(System.out::println);
    }

    public static void printSimilarRatings()
    {
        FourthRatings ratings = init();

        ratings.getSimilarRatings("65", 20, 5).forEach(m -> System.out.println(MovieDatabase.getTitle(m.getItem()) + "\t" + m.getValue()));
    }

    public static void printSimilarRatingsByGenre()
    {
        FourthRatings ratings = init();

        ratings.getSimilarRatingsByFilter("65", 20, 5, new GenreFilter("Action")).forEach(m -> System.out.println(MovieDatabase.getTitle(m.getItem()) + "\t" + m.getValue() + "\n" + MovieDatabase.getGenres(m.getItem()) + "\n"));
    }

    public static void printSimilarRatingsByDirector()
    {
        FourthRatings ratings = init();

        ratings.getSimilarRatingsByFilter("1034", 10, 3, new DirectorsFilter("Clint Eastwood,Sydney Pollack,David Cronenberg,Oliver Stone")).forEach(m -> System.out.println(MovieDatabase.getTitle(m.getItem()) + "\t" + m.getValue() + "\n" + MovieDatabase.getDirector(m.getItem()) + "\n"));
    }

    public static void printSimilarRatingsByGenreAndMinutes()
    {
        FourthRatings ratings = init();

        AllFilters filter = new AllFilters();
        filter.addFilter(new GenreFilter("Adventure"));
        filter.addFilter(new MinutesFilter(100,200));
        ratings.getSimilarRatingsByFilter("65", 10, 5, filter).forEach(m -> System.out.println(MovieDatabase.getTitle(m.getItem()) + "\t" + MovieDatabase.getMinutes(m.getItem()) + "\t" + m.getValue() + "\n" + MovieDatabase.getGenres(m.getItem()) + "\n"));
    }

 public static void printSimilarRatingsByYearAfterAndMinutes()
    {
        FourthRatings ratings = init();

        AllFilters filter = new AllFilters();
        filter.addFilter(new YearAfterFilter(2000));
        filter.addFilter(new MinutesFilter(80,100));
        ratings.getSimilarRatingsByFilter("65", 10, 5, filter).forEach(m -> System.out.println(MovieDatabase.getTitle(m.getItem()) + "\t" + MovieDatabase.getYear(m.getItem()) + "\t" + MovieDatabase.getMinutes(m.getItem()) + "\t" + m.getValue() +  "\n"));
    }


}
