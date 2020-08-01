import java.util.ArrayList;

/**
 * Write a description of class Rater here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */


public interface Rater
{
    void addRating(String item, double rating);

    boolean hasRating(String item);

    String getID();

    double getRating(String item);

    int numRatings();

    ArrayList<String> getItemsRated();
}
