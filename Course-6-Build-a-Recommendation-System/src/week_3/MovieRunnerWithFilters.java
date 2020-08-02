package week_3;

import week_1.Rating;

import java.util.ArrayList;
import java.util.Comparator;

public class MovieRunnerWithFilters
{
    public static void main(String[] args)
    {
//        printAverageRatings();
//        printAverageRatingsByYear();
//        printAverageRatingsByGenre();
//        printAverageRatingsByMinutes();
//        printAverageRatingsByDirectors();
//        printAverageRatingsByYearAfterAndGenre();
        printAverageRatingsByDirectorsAndMinutes();
    }

    private static ThirdRatings common()
    {
        ThirdRatings ratings = new ThirdRatings("data/ratings_short.csv");
        System.out.println("Number of raters is " + ratings.getRaterSize());

        MovieDatabase.initialize("ratedmovies_short.csv");
        System.out.println("Number of movies is " + MovieDatabase.size());
        return ratings;
    }

    public static void printAverageRatings()
    {
        ThirdRatings ratings = common();

        ArrayList<Rating> tmp = ratings.getAverageRatings(1);
        System.out.println("Found: " + tmp.size());
        tmp.sort(Comparator.comparingDouble(Rating::getValue));
        tmp.stream().map(r -> r.getValue() + " " + MovieDatabase.getTitle(r.getItem())).forEach(System.out::println);
    }


    public static void printAverageRatingsByYear()
    {
        ThirdRatings ratings = common();

        ArrayList<Rating> tmp = ratings.getAverageRatingsByFilter(1, new YearAfterFilter(2000));
        System.out.println("Found: " + tmp.size());
        tmp.sort(Comparator.comparingDouble(Rating::getValue));
        tmp.stream().map(r -> r.getValue() + " " + MovieDatabase.getTitle(r.getItem())).forEach(System.out::println);
    }

    public static void printAverageRatingsByGenre()
    {
        ThirdRatings ratings = common();

        ArrayList<Rating> tmp = ratings.getAverageRatingsByFilter(1, new GenreFilter("Crime"));
        System.out.println("Found: " + tmp.size());
        tmp.sort(Comparator.comparingDouble(Rating::getValue));
        tmp.stream().map(r -> r.getValue() + " " + MovieDatabase.getTitle(r.getItem()) + "\n\t" + MovieDatabase.getGenres(r.getItem())).forEach(System.out::println);
    }

    public static void printAverageRatingsByMinutes()
    {
        ThirdRatings ratings = common();

        ArrayList<Rating> tmp = ratings.getAverageRatingsByFilter(1, new MinutesFilter(110, 170));
        System.out.println("Found: " + tmp.size());
        tmp.sort(Comparator.comparingDouble(Rating::getValue));
        tmp.stream().map(r -> r.getValue() + " " + MovieDatabase.getTitle(r.getItem()) + "\n\t" + MovieDatabase.getMinutes(r.getItem())).forEach(System.out::println);
    }

    public static void printAverageRatingsByDirectors()
    {
        ThirdRatings ratings = common();

        ArrayList<Rating> tmp = ratings.getAverageRatingsByFilter(1, new DirectorsFilter("Charles Chaplin,Michael Mann,Spike Jonze"));
        System.out.println("Found: " + tmp.size());
        tmp.sort(Comparator.comparingDouble(Rating::getValue));
        tmp.stream().map(r -> r.getValue() + " " + MovieDatabase.getTitle(r.getItem()) + "\n\t" + MovieDatabase.getDirector(r.getItem())).forEach(System.out::println);
    }

    public static void printAverageRatingsByYearAfterAndGenre()
    {
        ThirdRatings ratings = common();

        AllFilters al = new AllFilters();
        al.addFilter(new YearAfterFilter(1980));
        al.addFilter(new GenreFilter("Romance"));
        ArrayList<Rating> tmp = ratings.getAverageRatingsByFilter(1, al);
        System.out.println("Found: " + tmp.size());
        tmp.sort(Comparator.comparingDouble(Rating::getValue));
        tmp.stream().map(r -> r.getValue() + " " + MovieDatabase.getTitle(r.getItem()) + "\n\t" + MovieDatabase.getYear(r.getItem()) + "\t" + MovieDatabase.getGenres(r.getItem())).forEach(System.out::println);
    }

    public static void printAverageRatingsByDirectorsAndMinutes()
    {
        ThirdRatings ratings = common();

        AllFilters al = new AllFilters();
        al.addFilter(new MinutesFilter(30,170));
        al.addFilter(new DirectorsFilter("Spike Jonze,Michael Mann,Charles Chaplin,Francis Ford Coppola"));
        ArrayList<Rating> tmp = ratings.getAverageRatingsByFilter(1, al);
        System.out.println("Found: " + tmp.size());
        tmp.sort(Comparator.comparingDouble(Rating::getValue));
        tmp.stream().map(r -> r.getValue() + " " + MovieDatabase.getTitle(r.getItem()) + "\n\t" + MovieDatabase.getMinutes(r.getItem()) + "\t" + MovieDatabase.getDirector(r.getItem())).forEach(System.out::println);
    }

}
