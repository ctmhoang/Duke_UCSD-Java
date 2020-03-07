package Final;

import java.util.*;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

/**
 * EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 *
 * @author Your name here
 * Date: July 17, 2015
 */
public class FinalMap extends PApplet {

    // We will use member variables, instead of local variables, to store the data
    // that the setUp and draw methods will need to access (as well as other methods)
    // You will use many of these variables, but the only one you should need to add
    // code to modify is countryQuakes, where you will store the number of earthquakes
    // per country.

    // You can ignore this.  It's to get rid of eclipse warnings
    private static final long serialVersionUID = 1L;

    // IF YOU ARE WORKING OFFILINE, change the value of this variable to true
    private static final boolean offline = false;

    /**
     * This is where to find the local tiles, for working without an Internet connection
     */
    public static String mbTilesString = "blankLight-1-3.mbtiles";


    //feed with magnitude 2.5+ Earthquakes
    private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

    // The files containing city names and info and country names and info
    private String cityFile = "city-data.json";
    private String countryFile = "countries.geo.json";

    // The map
    private UnfoldingMap map;

    // Markers for each city
    private List<Marker> cityMarkers;
    // Markers for each earthquake
    private List<Marker> quakeMarkers;

    // A List of country markers
    private List<Marker> countryMarkers;

    // NEW IN MODULE 5
    private CommonMarker lastSelected;
    private Marker lastClicked;

    // Airport Map Marker
    private List<Marker> airportMarkers;
    List<Marker> routeMarkers;

    //LifeExP Marker
    Map<String, Float> lifeExpByCountry;

    //Listen for mouseCLicked in Buttons
    boolean buttonsClicked = false;
    String buttonContent;

    final int HOZ_GAP_BUTTONS = 70, FIRST_BUTTON_Y_AXIS = 310;


    public void setup() {
        // (1) Initializing canvas and map tiles
        size(900, 700, OPENGL);
        if (offline) {
            map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
            earthquakesURL = "2.5_week.atom";  // The same feed, but saved August 7, 2015
        } else {
//            map = new UnfoldingMap(this, 200, 50, 650, 600, new Google.GoogleMapProvider());
            map = new UnfoldingMap(this, 200, 50, 650, 600, new OpenStreetMap.OpenStreetMapProvider());
            // IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
            //earthquakesURL = "2.5_week.atom";
        }
        MapUtils.createDefaultEventDispatcher(this, map);

        // FOR TESTING: Set earthquakesURL to be one of the testing files by uncommenting
        // one of the lines below.  This will work whether you are online or offline
        //earthquakesURL = "test1.atom";
        earthquakesURL = "test2.atom";

        // Uncomment this line to take the quiz
        //earthquakesURL = "quiz2.atom";


        // (2) Reading in earthquake data and geometric properties
        //     STEP 1: load country features and markers
        List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
        countryMarkers = MapUtils.createSimpleMarkers(countries);
        countryMarkers.forEach(k -> k.setHidden(true));

        //     STEP 2: read in city data
        List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
        cityMarkers = new ArrayList<Marker>();
        for (Feature city : cities) {
            cityMarkers.add(new CityMarker(city));
        }

        //     STEP 3: read in earthquake RSS feed
        List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
        quakeMarkers = new ArrayList<Marker>();

        for (PointFeature feature : earthquakes) {
            //check if LandQuake
            Marker addMarker;
            if (isLand(feature)) {
                addMarker = new LandQuakeMarker(feature);
            }
            // OceanQuakes
            else {
                addMarker = new OceanQuakeMarker(feature);
            }
            addMarker.setHidden(true);
            quakeMarkers.add(addMarker);
        }

        //COPY FROM AirportMap Class
        // get features from airport data
        List<PointFeature> features = ParseFeed.parseAirports(this, "airports.dat");

        // list for markers, hashmap for quicker access when matching with routes
        airportMarkers = new ArrayList<Marker>();
        HashMap<Integer, Location> airports = new HashMap<Integer, Location>();

        // create markers from features
        for (PointFeature feature : features) {
            AirportMarker m = new AirportMarker(feature);

            m.setRadius(5);
            m.setHidden(true);
            airportMarkers.add(m);

            // put airport in hashmap with OpenFlights unique id for key
            airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
        }
        // parse route data
        List<ShapeFeature> routes = ParseFeed.parseRoutes(this, "routes.dat");
        routeMarkers = new ArrayList<Marker>();
        for (ShapeFeature route : routes) {

            // get source and destination airportIds
            int source = Integer.parseInt((String) route.getProperty("source"));
            int dest = Integer.parseInt((String) route.getProperty("destination"));

            // get locations for airports on route
            if (airports.containsKey(source) && airports.containsKey(dest)) {
                route.addLocation(airports.get(source));
                route.addLocation(airports.get(dest));
            }

            SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());

            //System.out.println(sl.getProperties());

            //UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
            sl.setHidden(true);
            routeMarkers.add(sl);
        }
        //END COPY FROM AirportMap

        //LIFE EXP COPY

        lifeExpByCountry = loadLifeExpectancyFromCSV("LifeExpectancyWorldBankModule3.csv");


        //LIFE EXP END
        // could be used for debugging
        //printQuakes();
        //sortAndPrint(20);

        // (3) Add markers to map
        //     NOTE: Country markers are not added to the map.  They are used
        //           for their geometric properties
        map.addMarkers(quakeMarkers);
        map.addMarkers(cityMarkers);
        map.addMarkers(airportMarkers);
        map.addMarkers(routeMarkers);
        map.addMarkers(countryMarkers);
        shadeCountries();
        // End setup
        System.out.println("DONE");
    }

    public void draw() {
        background(100);
        map.draw();
        addKey();
        showButtons();
        if (lastClicked != null) {
            showInformation();
        }

    }

    //Get Buttons Clicked Information
    public void updateClickButtons() {
        float x = mouseX;
        if (x > 25 && x < 25 + 150) {
            float y = mouseY;
            if (!buttonsClicked) {
                if (y > FIRST_BUTTON_Y_AXIS && y < FIRST_BUTTON_Y_AXIS + 60) {
                    buttonsClicked = true;
                    buttonContent = "EQ";
                    setQuakeMarkersHidden(false);
                } else if (y > FIRST_BUTTON_Y_AXIS + HOZ_GAP_BUTTONS && y < FIRST_BUTTON_Y_AXIS + HOZ_GAP_BUTTONS + 60) {
                    buttonsClicked = true;
                    buttonContent = "LE";
                    setCountryMarkersHidden(false);
                    setCityMarkersHidden(true);
                } else if (y > FIRST_BUTTON_Y_AXIS + HOZ_GAP_BUTTONS * 2 && y < FIRST_BUTTON_Y_AXIS + HOZ_GAP_BUTTONS * 2 + 60) {
                    buttonsClicked = true;
                    buttonContent = "AP";
                    setAirportMarkersHidden(false);
                    setRoutesMarkersHidden(false);
                }
            } else {

                if (y > FIRST_BUTTON_Y_AXIS && y < FIRST_BUTTON_Y_AXIS + HOZ_GAP_BUTTONS * 2 + 60) {
                    if (buttonContent.equals("EQ")) {
                        setQuakeMarkersHidden(true);
                    }
                    if (buttonContent.equals("LE")) {

                        setCountryMarkersHidden(true);
                        setCityMarkersHidden(false);
                    }
                    if (buttonContent.equals("AP")) {
                        setAirportMarkersHidden(true);
                        setRoutesMarkersHidden(true);
                    }
                    buttonsClicked = false;
                    buttonContent = null;
                }
            }
        }
    }

    private void setQuakeMarkersHidden(boolean b) {
        for (Marker marker : quakeMarkers) {
            marker.setHidden(b);
        }
    }

    private void setCountryMarkersHidden(boolean b) {
        for (Marker marker : countryMarkers) {
            marker.setHidden(b);
        }
    }

    private void setCityMarkersHidden(boolean b) {
        for (Marker marker : cityMarkers) {
            marker.setHidden(b);
        }
    }

    private void setAirportMarkersHidden(boolean b) {
        for (Marker marker : airportMarkers) {
            marker.setHidden(b);
        }
    }

    private void setRoutesMarkersHidden(boolean b) {
        for (Marker marker : routeMarkers) {
            marker.setHidden(b);
        }
    }


    // shows information for clicked earthquake/city
    private void showInformation() {
        //Todo: add info for airport and country
        if (buttonsClicked) {
            int xbase = 25;
            int ybase = FIRST_BUTTON_Y_AXIS + HOZ_GAP_BUTTONS * 3;
            if (buttonContent.equals("EQ")) {
                fill(255, 250, 240);
                rect(xbase, ybase, 150, 130, 7);

                if (lastClicked instanceof CityMarker) {
                    fill(0);
                    textAlign(LEFT, CENTER);
                    textSize(13);
                    text(lastClicked.getStringProperty("name"), xbase + 25, ybase + 10);
                    textSize(11);

                    int numOfEQs = 0;
                    float totalMag = 0;
                    Marker mostRecent = null;
                    for (Marker eq : quakeMarkers) {
                        if (!eq.isHidden()) {
                            numOfEQs++;
                            totalMag += Float.parseFloat(eq.getProperty("magnitude").toString());
                            if (eq.getProperty("age").equals("Past Hour")) {
                                mostRecent = eq;
                            } else if (eq.getProperty("age").equals("Past Day")) {
                                mostRecent = eq;
                            }
                            if (mostRecent == null) {
                                mostRecent = eq;
                            }
                        }
                    }

                    fill(0);

                    if (numOfEQs == 0) {
                        fill(23, 135, 150);
                        text("There are no earthquake\nhappened in this area", xbase + 5, ybase + 35);
                    } else {
                        text("Number of Nearby EQs: ", xbase + 5, ybase + 35);
                        text(numOfEQs, textWidth("Number of Nearby EQs: ") + xbase + 5, ybase + 35);

                        text("Avg. Magnitude: ", xbase + 5, ybase + 50);
                        text((totalMag / numOfEQs), textWidth("Avg. Magnitude: ") + xbase + 5, ybase + 50);

                        text("The Most Recent EQ: ", xbase + 5, ybase + 65);
                        fill(255, 0, 0);
                        if (mostRecent == null) {
                            fill(23, 135, 150);
                            text("No earthquakes have occurred\nin this area recently", xbase + 5, ybase + 90);
                        } else {
                            text(formatText(mostRecent.getProperty("title").toString()), xbase + 5, ybase + 100);
                        }
                    }
                } else {
                    fill(0);
                    EarthquakeMarker eqMarker = (EarthquakeMarker) lastClicked;
                    String info = lastClicked.getProperty("title").toString();

                    //Get formatted string to put in legend
                    String formattedTitle = formatText(info);
                    text(formattedTitle, xbase + 2, ybase + 35);
                    text("Cities that are affected:", xbase + 5, ybase + 85);
                    int ybasse = ybase + 105;
                    int num = 1;
                    fill(255, 0, 0);
                    for (Marker cm : cityMarkers) {
                        if (!cm.isHidden()) {
                            text(num + "- " + cm.getProperty("name").toString() + ", " + cm.getProperty("country"), xbase + 10, ybasse);
                            ybasse += 20;
                            num++;
                        }
                    }
                    if (num == 1) {
                        text("None are affected.", xbase + 10, ybasse);
                    }
                }
            } else if (buttonContent.equals("LE")) {
                fill(255, 250, 240);
                rect(xbase, ybase, 150, 130, 7);
                fill(0);
                textAlign(LEFT, CENTER);
                textSize(15);
                text(formatText(lastClicked.getStringProperty("name")), xbase + 25, ybase + 25);
                textSize(13);
                float avgAge = getLifeExpPerCountry();
                text(avgAge != -1 ? "Avg lifespan: " + avgAge : "Do not have data in\n this region", xbase + 5, ybase + 75);
            } else {
                fill(255, 250, 240);
                rect(xbase, ybase, 150, 130, 7);
                fill(0);
                textAlign(LEFT, CENTER);
                textSize(13);
                text(formatText(removeDoubleQuotes(lastClicked.getStringProperty("name"))), xbase + 25, ybase + 15);
                textSize(11);
                text("City: " + removeDoubleQuotes(lastClicked.getStringProperty("city")), xbase + 5, ybase + 50);
                text("Country: " + removeDoubleQuotes(lastClicked.getStringProperty("country")), xbase + 5, ybase + 80);
                text("Airport Code: " + removeDoubleQuotes(lastClicked.getStringProperty("code")), xbase + 5, ybase + 110);
            }
        }
    }

    private float getLifeExpPerCountry() {
        String currentClickedCountry = lastClicked.getId();
        for (Marker marker : countryMarkers) {
            // Find data for country of the current marker
            if (lifeExpByCountry.containsKey(currentClickedCountry)) {
                return lifeExpByCountry.get(currentClickedCountry);
            }
        }
        return -1;
    }

    private String formatText(String info) {
        StringBuilder formattedText = new StringBuilder(info);
        int maxChars = 13;
        if (info.length() > maxChars) {
            formattedText.delete(0, formattedText.length());
            int count = 0;
            String[] words = info.split("\\s+");
            for (String word : words) {
                if (word.length() + count > maxChars) {
                    formattedText.append("\n");
                    count = 0;
                }
                count += word.length();
                formattedText.append(word).append(" ");
            }
        }
        return formattedText.toString();
    }


    private void sortAndPrint(int numToPrint) {
        List<EarthquakeMarker> markers = (List<EarthquakeMarker>) (List<?>) ((ArrayList<Marker>) quakeMarkers).clone();
        Collections.sort(markers);
        for (int i = 0; i < numToPrint; i++)
            System.out.println(markers.get(i));
    }

    /**
     * Event handler that gets called automatically when the
     * mouse moves.
     */
    @Override
    public void mouseMoved() {
        // clear the last selection
        if (lastSelected != null) {
            lastSelected.setSelected(false);
            lastSelected = null;

        }
        if (buttonContent == null || !buttonContent.equals("LE")) selectMarkerIfHover(cityMarkers);
        if (buttonContent != null) {
            if (buttonContent.equals("EQ") || buttonContent.equals("AP")) {
                if (buttonContent.equals("EQ")) {
                    selectMarkerIfHover(quakeMarkers);
                } else {
                    selectMarkerIfHover(airportMarkers);
                }
            }
        }
        //loop();
    }

    // If there is a marker selected
    private void selectMarkerIfHover(List<Marker> markers) {
        // Abort if there's already a marker selected
        if (lastSelected != null) {
            return;
        }


        for (Marker m : markers) {
            CommonMarker marker = (CommonMarker) m;
            if (marker.isInside(map, mouseX, mouseY)) {
                lastSelected = marker;
                marker.setSelected(true);
                return;
            }
        }
    }

    /**
     * The event handler for mouse clicks
     * It will display an earthquake and its threat circle of cities
     * Or if a city is clicked, it will display all the earthquakes
     * where the city is in the threat circle
     */
    @Override
    public void mouseClicked() {
        updateClickButtons();
        if (lastClicked != null) {
            if (buttonContent != null) {
                if (buttonContent.equals("EQ")) {
                    unhideMakersEQMode();
                } else if (buttonContent.equals("LE")) {
                    setCountryMarkersHidden(false);
                } else {
                    setRoutesMarkersHidden(true);
                    setAirportMarkersHidden(false);
                }
            }
            lastClicked = null;
        }
        if (buttonContent != null) {
            if (buttonContent.equals("EQ")) {
                checkEarthquakesForClick();
                checkCitiesForClick();
            } else if (buttonContent.equals("LE")) {
                getCountryClicked();
            } else {
                getAirPortClicked();
            }
        }
    }

    private void getAirPortClicked() {
        if (lastClicked != null) return;
        // Loop over the earthquake markers to see if one of them is selected
        for (Marker marker : airportMarkers) {
            if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
                lastClicked = marker;
                //Trim quotes in airport code
                String airportCode = removeDoubleQuotes(lastClicked.getStringProperty("code"));
                Set<String> visibleAirPortCode = new HashSet<>();
                visibleAirPortCode.add(airportCode);
                for (Marker route : routeMarkers) {
                    if (route.getStringProperty("SID").equals(airportCode)) {
                        route.setHidden(false);
                        visibleAirPortCode.add(route.getStringProperty("DID"));
                    } else route.setHidden(true);
                }
                for (Marker airport : airportMarkers) {
                    if (!visibleAirPortCode.contains(airport.getStringProperty("code").substring(1, airport.getStringProperty("code").length() - 1))) {
                        airport.setHidden(true);
                    }
                }

                return;
            }
        }
    }

    private String removeDoubleQuotes(String s)
    {
        //Just trim 1 char at beginning and 1 char at the end of the string
        return s.substring(1,s.length()-1);
    }

    private void getCountryClicked() {
        if (lastClicked != null) return;
        // Loop over the earthquake markers to see if one of them is selected
        for (Marker marker : countryMarkers) {
            if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
                lastClicked = marker;
                // Hide all the other earthquakes and hide
                for (Marker country : countryMarkers) {
                    if (country != lastClicked) {
                        country.setHidden(true);
                    }
                }
                return;
            }
        }
    }

    // Helper method that will check if a city marker was clicked on
    // and respond appropriately
    private void checkCitiesForClick() {
        if (lastClicked != null) return;
        // Loop over the earthquake markers to see if one of them is selected
        for (Marker marker : cityMarkers) {
            if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
                lastClicked = (CommonMarker) marker;
                // Hide all the other earthquakes and hide
                for (Marker mhide : cityMarkers) {
                    if (mhide != lastClicked) {
                        mhide.setHidden(true);
                    }
                }
                for (Marker mhide : quakeMarkers) {
                    EarthquakeMarker quakeMarker = (EarthquakeMarker) mhide;
                    if (quakeMarker.getDistanceTo(marker.getLocation())
                            > quakeMarker.threatCircle()) {
                        quakeMarker.setHidden(true);
                    }
                }
                return;
            }
        }
    }

    // Helper method that will check if an earthquake marker was clicked on
    // and respond appropriately
    private void checkEarthquakesForClick() {
        if (lastClicked != null) return;
        // Loop over the earthquake markers to see if one of them is selected
        for (Marker m : quakeMarkers) {
            EarthquakeMarker marker = (EarthquakeMarker) m;
            if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
                lastClicked = marker;
                // Hide all the other earthquakes and hide
                for (Marker mhide : quakeMarkers) {
                    if (mhide != lastClicked) {
                        mhide.setHidden(true);
                    }
                }
                for (Marker mhide : cityMarkers) {
                    if (mhide.getDistanceTo(marker.getLocation())
                            > marker.threatCircle()) {
                        mhide.setHidden(true);
                    }
                }
                return;
            }
        }
    }

    // loop over and unhide all markers
    private void unhideMakersEQMode() {
        for (Marker marker : quakeMarkers) {
            marker.setHidden(false);
        }

        for (Marker marker : cityMarkers) {
            marker.setHidden(false);
        }
    }


    // helper method to draw key in GUI
    private void addKey() {
        fill(200);
        text("@author: Adam", 600, 25);
        int xbase = 25;
        int ybase = 50;
        fill(255, 250, 240);
        rect(xbase, ybase, 150, 250, 7);
        textAlign(LEFT, CENTER);

        if (buttonsClicked) {
            if (buttonContent.equals("EQ")) {
                //DEFAULT LEGEND
                // Remember you can use Processing's graphics methods here

                fill(0);
                textSize(12);
                text("Earthquake Key", xbase + 25, ybase + 25);

                fill(150, 30, 30);
                int tri_xbase = xbase + 35;
                int tri_ybase = ybase + 50;
                triangle(tri_xbase, tri_ybase - CityMarker.TRI_SIZE, tri_xbase - CityMarker.TRI_SIZE,
                        tri_ybase + CityMarker.TRI_SIZE, tri_xbase + CityMarker.TRI_SIZE,
                        tri_ybase + CityMarker.TRI_SIZE);

                fill(0, 0, 0);
                textAlign(LEFT, CENTER);
                text("City Marker", tri_xbase + 15, tri_ybase);

                text("Land Quake", xbase + 50, ybase + 70);
                text("Ocean Quake", xbase + 50, ybase + 90);
                text("Size ~ Magnitude", xbase + 25, ybase + 110);

                fill(255, 255, 255);
                ellipse(xbase + 35,
                        ybase + 70,
                        10,
                        10);
                rect(xbase + 35 - 5, ybase + 90 - 5, 10, 10);

                fill(color(255, 255, 0));
                ellipse(xbase + 35, ybase + 140, 12, 12);
                fill(color(0, 0, 255));
                ellipse(xbase + 35, ybase + 160, 12, 12);
                fill(color(255, 0, 0));
                ellipse(xbase + 35, ybase + 180, 12, 12);

                textAlign(LEFT, CENTER);
                fill(0, 0, 0);
                text("Shallow", xbase + 50, ybase + 140);
                text("Intermediate", xbase + 50, ybase + 160);
                text("Deep", xbase + 50, ybase + 180);

                text("Past hour", xbase + 50, ybase + 200);

                fill(255, 255, 255);
                int centerx = xbase + 35;
                int centery = ybase + 200;
                ellipse(centerx, centery, 12, 12);

                strokeWeight(2);
                line(centerx - 8, centery - 8, centerx + 8, centery + 8);
                line(centerx - 8, centery + 8, centerx + 8, centery - 8);
            } else if (buttonContent.equals("AP")) {
                textSize(12);
                fill(11);
                ellipse(xbase + 35, ybase + 100, 5, 5);
                text("Airport", xbase + 50, ybase + 100);
                text("Route", xbase + 50, ybase + 130);
                fill(130);
                line(ybase, ybase + 130, 70, ybase + 130);
            } else if (buttonContent.equals("LE")) {
                textSize(12);
                fill(11);
                text("Approximate 40", xbase + 40, ybase + 100);
                text("Approximate 100", xbase + 40, ybase + 130);

                fill(194, 77, 23);
                ellipse(xbase + 20, ybase + 100, 15, 15);
                fill(29, 115, 136);
                ellipse(xbase + 20, ybase + 130, 15, 15);

            }

        } else {
            fill(0);
            textSize(13);
            text("SELECT A MODE\nTO DISPLAY \nINFORMATION HERE", xbase + 11, ybase + 110);
        }
    }


    // Checks whether this quake occurred on land.  If it did, it sets the
    // "country" property of its PointFeature to the country where it occurred
    // and returns true.  Notice that the helper method isInCountry will
    // set this "country" property already.  Otherwise it returns false.
    private boolean isLand(PointFeature earthquake) {

        // IMPLEMENT THIS: loop over all countries to check if location is in any of them
        // If it is, add 1 to the entry in countryQuakes corresponding to this country.
        for (Marker country : countryMarkers) {
            if (isInCountry(earthquake, country)) {
                return true;
            }
        }

        // not inside any country
        return false;
    }

    // prints countries with number of earthquakes
    // You will want to loop through the country markers or country features
    // (either will work) and then for each country, loop through
    // the quakes to count how many occurred in that country.
    // Recall that the country markers have a "name" property,
    // And LandQuakeMarkers have a "country" property set.
    private void printQuakes() {
        int totalWaterQuakes = quakeMarkers.size();
        for (Marker country : countryMarkers) {
            String countryName = country.getStringProperty("name");
            int numQuakes = 0;
            for (Marker marker : quakeMarkers) {
                EarthquakeMarker eqMarker = (EarthquakeMarker) marker;
                if (eqMarker.isOnLand()) {
                    if (countryName.equals(eqMarker.getStringProperty("country"))) {
                        numQuakes++;
                    }
                }
            }
            if (numQuakes > 0) {
                totalWaterQuakes -= numQuakes;
                System.out.println(countryName + ": " + numQuakes);
            }
        }
        System.out.println("OCEAN QUAKES: " + totalWaterQuakes);
    }


    // helper method to test whether a given earthquake is in a given country
    // This will also add the country property to the properties of the earthquake feature if
    // it's in one of the countries.
    // You should not have to modify this code
    private boolean isInCountry(PointFeature earthquake, Marker country) {
        // getting location of feature
        Location checkLoc = earthquake.getLocation();

        // some countries represented it as MultiMarker
        // looping over SimplePolygonMarkers which make them up to use isInsideByLoc
        if (country.getClass() == MultiMarker.class) {

            // looping over markers making up MultiMarker
            for (Marker marker : ((MultiMarker) country).getMarkers()) {

                // checking if inside
                if (((AbstractShapeMarker) marker).isInsideByLocation(checkLoc)) {
                    earthquake.addProperty("country", country.getProperty("name"));

                    // return if is inside one
                    return true;
                }
            }
        }

        // check if inside country represented by SimplePolygonMarker
        else if (((AbstractShapeMarker) country).isInsideByLocation(checkLoc)) {
            earthquake.addProperty("country", country.getProperty("name"));

            return true;
        }
        return false;
    }

    // shows airport/airport route buttons on click
    private void showButtons() {
        int space = 25;
        textSize(13);

        fill(255, 250, 240);
        rect(25, FIRST_BUTTON_Y_AXIS, 150, 60, 7);
        rect(25, FIRST_BUTTON_Y_AXIS + HOZ_GAP_BUTTONS, 150, 60, 7);
        rect(25, FIRST_BUTTON_Y_AXIS + HOZ_GAP_BUTTONS * 2, 150, 60, 7);

        fill(0);
        textAlign(LEFT, CENTER);
        text("EARTHQUAKES MODE", space + 11, FIRST_BUTTON_Y_AXIS + space);
        text("LIFE EXPECTANCY MODE", space + 2, FIRST_BUTTON_Y_AXIS + HOZ_GAP_BUTTONS + space);
        text("AIRPORT MODE", space + 27, FIRST_BUTTON_Y_AXIS + HOZ_GAP_BUTTONS * 2 + space);
        if (buttonsClicked) {
            fill(127, 251, 136);
            switch (buttonContent) {
                case "EQ":
                    rect(25, FIRST_BUTTON_Y_AXIS, 150, 60, 7);
                    fill(54, 10, 61);
                    text("EARTHQUAKES MODE", space + 11, FIRST_BUTTON_Y_AXIS + space);
                    break;
                case "LE":
                    rect(25, FIRST_BUTTON_Y_AXIS + HOZ_GAP_BUTTONS, 150, 60, 7);
                    fill(54, 10, 61);
                    text("LIFE EXPECTANCY MODE", space + 2, FIRST_BUTTON_Y_AXIS + HOZ_GAP_BUTTONS + space);
                    break;
                case "AP":
                    rect(25, FIRST_BUTTON_Y_AXIS + HOZ_GAP_BUTTONS * 2, 150, 60, 7);
                    fill(54, 10, 61);
                    text("AIRPORT MODE", space + 27, FIRST_BUTTON_Y_AXIS + HOZ_GAP_BUTTONS * 2 + space);
                    break;
            }
        }
    }


    //Helper method to color each country based on life expectancy
    //Red-orange indicates low (near 40)
    //Blue indicates high (near 100)
    private void shadeCountries() {
        for (Marker marker : countryMarkers) {
            // Find data for country of the current marker
            String countryId = marker.getId();
            if (lifeExpByCountry.containsKey(countryId)) {
                float lifeExp = lifeExpByCountry.get(countryId);
                // Encode value as brightness (values range: 40-90)
                int colorLevel = (int) map(lifeExp, 40, 90, 10, 255);
                marker.setColor(color(255 - colorLevel, 100, colorLevel));
            } else {
                marker.setColor(color(150, 150, 150));
            }
        }
    }

    //Helper method to load life expectancy data from file
    private Map<String, Float> loadLifeExpectancyFromCSV(String fileName) {
        Map<String, Float> lifeExpMap = new HashMap<String, Float>();

        String[] rows = loadStrings(fileName);
        for (String row : rows) {
            // Reads country name and population density value from CSV row
            // NOTE: Splitting on just a comma is not a great idea here, because
            // the csv file might have commas in their entries, as this one does.
            // We do a smarter thing in ParseFeed, but for simplicity,
            // we just use a comma here, and ignore the fact that the first field is split.
            String[] columns = row.split(",");
            if (columns.length == 6 && !columns[5].equals("..")) {
                lifeExpMap.put(columns[4], Float.parseFloat(columns[5]));
            }
        }

        return lifeExpMap;
    }

}
