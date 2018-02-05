# Flight Radar

Track all flights within a specified radius from a specified geolocation in a continuous output stream using OpenSky API.

## **To install:**

Retrieve OpenSkyAPI from [https://github.com/openskynetwork/opensky-api]

```java
mvn clean install
```

## **Before running:**

Please edit the configuration section of the FlightRunner class with your desired point of interest and credentials for OpenSky Network.

```java
//Login credentials to Opensky Network
public static final String openskyUname = "username";
public static final String openskyPass = "password";

//Latitude and longitude of point of interest (preset to Statue of Liberty, NJ, USA)
public static final Double homeLat = 40.6892;
public static final Double homeLong = -74.0445;
```
For continuous running, set maximum line buffer in your terminal to prevent memory issues.
