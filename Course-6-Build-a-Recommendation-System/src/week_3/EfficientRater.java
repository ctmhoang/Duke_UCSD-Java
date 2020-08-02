package week_3;

import week_1.Rating;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class EfficientRater implements Rater
{

    private final String myID;
    private final HashMap<String, Rating> myRatings; //id is key

    public EfficientRater(String id)
    {
        myID = id;
        myRatings = new HashMap<>();
    }

    @Override
    public void addRating(String item, double rating)
    {
        myRatings.putIfAbsent(item, new Rating(item, rating));
    }

    @Override
    public boolean hasRating(String item)
    {
        return myRatings.containsKey(item);
    }

    @Override
    public String getID()
    {
        return myID;
    }

    @Override
    public double getRating(String item)
    {
        return Optional.ofNullable(myRatings.get(item)).map(Rating::getValue).orElseGet(() -> (double) -1);
    }

    @Override
    public int numRatings()
    {
        return myRatings.size();
    }

    @Override
    public ArrayList<String> getItemsRated()
    {
        return new ArrayList<>(myRatings.keySet());
    }
}
