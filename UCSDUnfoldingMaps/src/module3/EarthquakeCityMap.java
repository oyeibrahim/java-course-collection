package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	//private static final boolean offline = false;
	private static final boolean offline = true;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	
	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();
	    
	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    // These print statements show you (1) all of the relevant properties 
	    // in the features, and (2) how to get one property and use it
	    if (earthquakes.size() > 0) {
	    	PointFeature f = earthquakes.get(0);
	    	System.out.println(f.getProperties());
	    	Object magObj = f.getProperty("magnitude");
	    	float mag = Float.parseFloat(magObj.toString());
	    	// PointFeatures also have a getLocation method
	    }
	    
	    //colors were transfered to work below
	    
	    //TODO: Add code here as appropriate
	    for (PointFeature f : earthquakes){
	    	//Add SimplePointMarker returned to ArrayList markers
	    	markers.add(createMarker(f));
	    }
	    //add the markers arraylist to the map using addMarker
	    map.addMarkers(markers);
	}
	//colors transfered here
	// Here is an example of how to use Processing's color method to generate 
    // an int that represents the color yellow.  
    int yellow = color(255, 255, 0);
    int red = color(255,0,0);
    int blue = color(0,0,255);
    int green = color (0,255,0);
	// A suggested helper method that takes in an earthquake feature and 
	// returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	private SimplePointMarker createMarker(PointFeature feature)
	{
		//change PointFeature with its location to a SimplePointMarker
		SimplePointMarker mk = new SimplePointMarker(feature.getLocation());
		//get the magnitude
		Object magObj = feature.getProperty("magnitude");
		//change to string and then to float
    	float mag = Float.parseFloat(magObj.toString());
    	//paint according to magnitude
    	if(mag < 4.0){
    		mk.setColor(blue);
    		mk.setRadius(4);
    	}
    	else if(mag >=4.0 && mag <= 4.9){
    		mk.setColor(yellow);
    		mk.setRadius(8);
    	}
    	else if(mag > 5.0){
    		mk.setColor(red);
    		mk.setRadius(12);
    	}
    	else{
    		mk.setColor(green);
    		mk.setRadius(14);
    	}
    	
    	
		// finish implementing and use this method, if it helps.
		return mk;
	}
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() 
	{	
		// Remember you can use Processing's graphics methods here
		fill(255,255,255);
		rect(20, 50, 160, 300, 20);
		

		textSize(20);
		fill(10);
		text("KEY", 50, 85);
		
		fill(255,0,0);
		ellipse(50, 125, 12, 12);
		textSize(12);
		fill(50);
		text("5.0+ Magnitude", 75, 130);
		
		
		fill(255, 255, 0);
		ellipse(50, 175, 8, 8);
		textSize(12);
		fill(50);
		text("4.0+ Magnitude", 75, 180);
		
		fill(0,0,255);
		ellipse(50, 225, 4, 4);
		textSize(12);
		fill(50);
		text("Below 4.0", 75, 230);
		
		fill(0,255,0);
		ellipse(50, 275, 14, 14);
		textSize(12);
		fill(10);
		text("Others", 75, 280);
	}
}
