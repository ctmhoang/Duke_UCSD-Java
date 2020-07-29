import edu.duke.FileResource;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.util.*;
import java.util.stream.Collectors;


public class FirstRatings
{
    public static void main(String[] args)
    {
        testLoadMovies();
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
}
