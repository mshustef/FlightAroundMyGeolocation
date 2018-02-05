package main.java;

import org.opensky.api.OpenSkyApi;
import org.opensky.model.OpenSkyStates;
import org.opensky.model.StateVector;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by MaxShustef on 2/4/18.
 */

public class FlightRunner {

    public static ArrayList<StateVector> flights = new ArrayList<StateVector>();
    public static final int maxArrayBuffer = 200;

    //Configuration:
    //----------------------------------------------------------------

    //Login credentials to Opensky Network
    public static final String openskyUname = "username";
    public static final String openskyPass = "password";
    //Desired radius (in miles) to search for flights
    public static final int radiusFromGeo = 100;
    //Latitude and longitude of point of interest (preset to Statue of Liberty, NJ, USA)
    public static final Double homeLat = 40.6892;
    public static final Double homeLong = -74.0445;
    //Time between calls to OpenSkyAPI(frequencies under 2 minutes could be problematic) - in milliseconds
    public static final int sleepTime = 120000;

    //----------------------------------------------------------------

    //Determine whether flight is within desired radius
    public static boolean ourArea(Double latitude, Double longitude){
        if(DistanceCalculator.distance(latitude, longitude, homeLat, homeLong, "M") < radiusFromGeo){
            return true;
        }
        return false;
    }

    //Ensure that the ArrayList size is below desired buffer
    public static void checkArrayList(){
        while(flights.size()>maxArrayBuffer){
            flights.remove(0);
            //System.out.println("REMOVED");
        }
    }

    //Determine if flight already exists in ArrayList (whether to add & print)
    public static int add(StateVector stateVector){
        String icao24 = stateVector.getIcao24();
        int index = 0;
        for(StateVector compareTo: flights){
            if(compareTo.getIcao24().equals(icao24)){
                return index;
            }
            index++;
        }
        return -1;
    }

    //Format for printing flight info
    public static void printFlight(StateVector stateVector){
        System.out.println("CALLSIGN: " + stateVector.getCallsign() + "| ICAO24: " + stateVector.getIcao24() + " | SQUAWK: " + stateVector.getSquawk());
        System.out.println("LATITUDE: " + stateVector.getLatitude() + "\u00b0 | LONGITUDE: " + stateVector.getLongitude() + "\u00b0 | GEO ALTITUDE: " + stateVector.getGeoAltitude() + "m | BARO ALTITUDE: " + stateVector.getBaroAltitude() + "m");
        System.out.println("HEADING: " + stateVector.getHeading() + "\u00b0 | SPEED: " + stateVector.getVelocity() + "m/s | VERTICAL RATE: " + stateVector.getVerticalRate() + "m/s");
        System.out.println("ORIGIN COUNTRY: " + stateVector.getOriginCountry() + " | ON GROUND: " + stateVector.isOnGround() + " | SPI: " + stateVector.isSpi());
        System.out.println("");
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        OpenSkyApi a = new OpenSkyApi(openskyUname, openskyPass);
        OpenSkyStates os;
        while(true) {
            os = a.getStates(0, null);
            for (StateVector stateVector : os.getStates()) {
                Double longitude = stateVector.getLongitude();
                Double latitude = stateVector.getLatitude();
                if (!(longitude == null) && !(latitude == null)) {
                    if (ourArea(latitude, longitude)) {
                        int add = add(stateVector);
                        if (add == -1) {
                            flights.add(stateVector);
                            printFlight(stateVector);
                            //System.out.println("ADDED");
                            checkArrayList();
                        }
                        else{
                            flights.remove(add);
                            flights.add(add, stateVector);
                            printFlight(stateVector);
                            //System.out.println("REPLACED");
                            checkArrayList();
                        }
                    }
                }
            }
            Thread.sleep(sleepTime);

        }

    }

}
