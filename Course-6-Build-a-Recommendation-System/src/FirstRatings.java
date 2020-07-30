import edu.duke.FileResource;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class FirstRatings
{
    public static void main(String[] args)
    {
//        testLoadMovies();
        testLoadRaters();
    }

    public static ArrayList<Movie> loadMovies(String filename)
    {
        FileResource resource = new FileResource(filename);
        ArrayList<Movie> res = new ArrayList<>();

        for (CSVRecord data : resource.getCSVParser())
        {
//            id - a String variable representing the IMDB ID of the movie
            String id = data.get("id");
//            title - a String variable for the movie’s title
            String title = data.get("title");
//            year - an integer representing the year
            String year = data.get("year");
//            genres - one String of one or more genres separated by commas
            String genres = data.get("genre");
//            director - one String of one or more directors of the movie separated by commas
            String director = data.get("director");
//            country - one String of one or more countries the film was made in, separated by commas
            String country = data.get("country");
//            minutes - an integer for the length of the movie
            int minutes = Integer.parseInt(data.get("minutes"));
//            poster - a String that is a link to an image of the movie poster if one exists, or “N/A” if no poster exists
            String poster = data.get("poster");
            res.add(new Movie(id, title, year, genres, director, country, poster, minutes));
        }
        return res;
    }

    public static ArrayList<Rater> loadRaters(String filename)
    {
        FileResource resource = new FileResource(filename);
        List<Rater> res = new LinkedList<>();

        for (CSVRecord data : resource.getCSVParser())
        {
            String id = data.get("rater_id");
            Rater tmp = null;
            try
            {
                tmp = res.get(Integer.parseInt(id) - 1); // id start at 1 using base 0
            } catch (IndexOutOfBoundsException e)
            {
                tmp = new Rater(id);
                res.add(tmp);
            }

            String movieID = data.get("movie_id");
            if(tmp.getRating(movieID) == -1)
                tmp.addRating(movieID, Double.parseDouble(data.get("rating")));


        }
        return new ArrayList<>(res);
    }

    public static void testLoadMovies()
    {
        ArrayList<Movie> tmp = loadMovies("data/ratedmovies_short.csv");
        System.out.println("The number of the movies in data is " + tmp.size());
//        tmp.forEach(System.out::println);
        System.out.println("Include Genre Type: " + includeGenreType("Comedy", tmp));
        System.out.println("Has length greater than: " + hasLengthGreaterThan(150, tmp));
        int num;
        System.out.println("Maximum number of movies by any director is " + (num = getMaximumNumberOfDirectorsInAMovie(tmp)));
        System.out.println("Has number of author: " + getTheNumberOfDirectorsOfMoviesHasTheSameNumberOfDirecter(num, tmp));
    }

    public static int includeGenreType(String genre, ArrayList<Movie> movies)
    {
        String fstChar;
        genre = genre.trim().toLowerCase();
        String finalGenre = genre.replaceFirst(fstChar = String.valueOf(genre.charAt(0)), fstChar.toUpperCase());
        return (int) movies.stream().filter(m -> m.getGenres().contains(finalGenre)).count();
    }

    public static int hasLengthGreaterThan(int length, ArrayList<Movie> movies)
    {
        return (int) movies.stream().filter(m -> m.getMinutes() > length).count();
    }

    public static int getMaximumNumberOfDirectorsInAMovie(ArrayList<Movie> movies)
    {
        return movies.stream().map(m -> m.getDirector().split(",")).max(Comparator.comparingInt(a -> a.length)).get().length;
    }

    public static int getTheNumberOfDirectorsOfMoviesHasTheSameNumberOfDirecter(int num, ArrayList<Movie> movies)
    {
        Set<String> director = movies.stream().parallel().map(m -> m.getDirector().split(",")).filter(d -> d.length == num).flatMap(Arrays::stream).collect(Collectors.toSet());
        return director.size();
    }

    public static void testLoadRaters(){
        ArrayList<Rater> tmp = loadRaters("data/ratings_short.csv");
        System.out.println("The total number of raters in file is " + tmp.size());
        tmp.stream().map(FirstRatings::printRater).forEach(System.out::println);

        int id = 2;
        Rater r = tmp.get(id-1);
        System.out.println("Rater whose id is " + id + " has rated " + getNumberOfRating(r) + " movies");

        int max = getMaxNumberOfRatingsIn(tmp);
        System.out.println("Maximum number of ratings is " + max);
        ArrayList<String> maxRater = getRaterWithRating(max,tmp);
        System.out.println("Raters 's ids have maximum number of ratings are " );
        System.out.println(maxRater);
        System.out.println("has size: " + maxRater.size());
        System.out.println();

        String mvid = "1798709";
        System.out.println("Number of rating whose movie's id " + mvid + " has is " + getNumberOfRatersForAMovie(mvid, tmp));

        System.out.println("All movies have been rated are " + getQuantityOfMoviesRated(tmp));
    }

    private static String printRater(Rater r){
        StringBuilder s = new StringBuilder();
        ArrayList<String> moviesRated = r.getItemsRated();
        s.append(r.getID()).append(" ").append(moviesRated.size()).append("\n");
        moviesRated.stream().map(r::getRating).map(Object::toString).forEach(str -> s.append(str).append("\n"));
        return s.toString();
    }

    public static int getNumberOfRating(Rater r){
        return r.getItemsRated().size();
    }

    public static int getMaxNumberOfRatingsIn(ArrayList<Rater> raters)
    {
        return raters.stream().mapToInt(r -> r.getItemsRated().size()).max().getAsInt();
    }

    public static ArrayList<String> getRaterWithRating(int num, ArrayList<Rater> raters){
        return raters.stream().filter(r -> r.getItemsRated().size()  == num).map(Rater::getID).collect(Collectors.toCollection(ArrayList::new));
    }

    public static int getNumberOfRatersForAMovie(String id, ArrayList<Rater> raters){
        return (int) raters.stream().filter(r -> r.getRating(id) != -1).count();
    }

    public static int getQuantityOfMoviesRated(ArrayList<Rater> raters){
        return (int) raters.stream().flatMap(r -> r.getItemsRated().stream()).distinct().count();
    }

}
