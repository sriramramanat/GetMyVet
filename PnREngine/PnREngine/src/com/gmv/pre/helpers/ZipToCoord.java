package com.gmv.pre.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.bson.Document;

import com.gmv.pre.definitions.DatabaseDefinitions;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ZipToCoord {
	
	public double lat;
	public double lon;
	public String __country;
	public String __city;
	public String __county;
	public String __state;
	public int __zip;
	
	public ZipToCoord (int in_zip) {
		lat = 0;
		lon = 0;
		__city = "";
		__county = "";
		__state = "";
		__country ="US";
		__zip = in_zip;
		getCoords ();
	}
	
	public ZipToCoord (String city, String state) {
		lat = 0;
		lon = 0;
		__city = city;
		__state = state;
		__county = "";
		__country ="US";
		__zip = 0;
		getCoords ();
	}
	
	public ZipToCoord (String city, String county, String state) {
		lat = 0;
		lon = 0;
		__city = city;
		__state = state;
		__county = county;
		__country ="US";
		__zip = 0;
		getCoords ();
	}

	public ZipToCoord (int in_zip, String in_country) {
		lat = 0;
		lon = 0;
		__city = "";
		__state = "";
		__county = "";
		__country = in_country;
		__zip = in_zip;
		getCoords ();
	}

	/**
	 * @author sriramr
	 * Convert US zipcode to latitude and longitude. The first query happens internally in
	 * a stored table, failing which http://nominatim.openstreetmap.org/ is queried and the results stored
	 */
	protected boolean getCachedCoords () {
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoDatabase db = client.getDatabase(DatabaseDefinitions.NOSQL_DB);
		MongoCollection<Document> coll = db.getCollection(DatabaseDefinitions.GEO_LOCATION_COLL);
		Document d = new Document ("zip", __zip);
		d.append("city", __city);
		d.append("county", __county);
		d.append("state", __state);
		d.append("country", __country);
		if (coll.count(d) > 0) {
			FindIterable<Document> iterable = coll.find(d);
			Iterator<Document> iter = iterable.iterator ();
			if (iter.hasNext()) {
				Document match = iter.next ();
				lon = match.getDouble("lon");
				lat = match.getDouble("lat");
				client.close();
				return true;
			}
		}
		client.close();
		return false;
	}
	
	protected void addCoordsToCache () {
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoDatabase db = client.getDatabase(DatabaseDefinitions.NOSQL_DB);
		MongoCollection<Document> coll = db.getCollection(DatabaseDefinitions.GEO_LOCATION_COLL);
		Document d = new Document ("zip", __zip);
		d.append("city", __city);
		d.append("county", __county);
		d.append("state", __state);
		d.append("country", __country);
		d.append("lat", lat);
		d.append("lon", lon);
		System.out.println(d.toJson());
		coll.insertOne(d);
		client.close();
	}

	public void getCoords () {
		// find if it is cached
		if (getCachedCoords()) {
			return;
		}
		
		String queryParam = "countrycodes=" + __country;
		if (__zip != 0) {
			queryParam += "&q=" + __zip;
		}
		if (!__city.isEmpty()) {
			queryParam += "&city=" + __city;
		}
		if (!__county.isEmpty()) {
			queryParam += "&county=" + __county;
		}
		if (!__state.isEmpty()) {
			queryParam += "&state=" + __state;
		}

		try {
			String urlstr = "http://nominatim.openstreetmap.org/search?" + queryParam;
			URL url = new URL(urlstr);
			System.out.println(url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				return;
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				if (output.contains("\"lat\"")) {
					StringTokenizer tok = new StringTokenizer (output);
					if (tok.countTokens() > 1) {
						tok.nextToken(":");
						String tok2 = tok.nextToken();
						tok2 = tok2.replace('"', ' ');
						tok2 = tok2.replace(',', ' ');
						try {
							lat = Double.parseDouble(tok2);
						} catch (NumberFormatException e) {e.printStackTrace();}
					}
				}
				if (output.contains("\"lon\"")) {
					StringTokenizer tok = new StringTokenizer (output);
					if (tok.countTokens() > 1) {
						tok.nextToken(":");
						String tok2 = tok.nextToken();
						tok2 = tok2.replace('"', ' ');
						tok2 = tok2.replace(',', ' ');
						try {
							lon = Double.parseDouble(tok2);
						} catch (NumberFormatException e) {e.printStackTrace();}
					}
				}
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// add to the cache
		addCoordsToCache ();
	}
}