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
	public String country;
	public int zip;
	
	public ZipToCoord (int in_zip) {
		lat = 0;
		lon = 0;
		country ="US";
		zip = in_zip;
	}

	public ZipToCoord (int in_zip, String in_country) {
		lat = 0;
		lon = 0;
		country = in_country;
		zip = in_zip;
	}

	/**
	 * @author sriramr
	 * Convert US zipcode to latitude and longitude. The first query happens internally in
	 * a stored table, failing which http://nominatim.openstreetmap.org/ is queried and the results stored
	 */
	protected boolean getCachedCoordsForZip () {
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoDatabase db = client.getDatabase("gmv_db");
		MongoCollection<Document> coll = db.getCollection("geoLoc");
		Document d = new Document ("zip", zip);
		d.append("country", country);
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
	
	protected void addCoordsForZip () {
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoDatabase db = client.getDatabase(DatabaseDefinitions.NOSQL_DB);
		MongoCollection<Document> coll = db.getCollection(DatabaseDefinitions.GEO_LOCATION_COLL);
		Document d = new Document ("zip", zip);
		d.append("country", country);
		d.append("lat", lat);
		d.append("lon", lon);
		coll.insertOne(d);
		client.close();
	}

	public void getCoordsForZip () {
		// find if it is cached
		if (getCachedCoordsForZip()) {
			return;
		}
		
		String queryParam = "q=" + zip + "&countrycodes=" + country;
		try {
			String urlstr = "http://nominatim.openstreetmap.org/search?" + queryParam;
			URL url = new URL(urlstr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				return;
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			while ((output = br.readLine()) != null) {
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
		addCoordsForZip ();
	}

}