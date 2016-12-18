package com.gmv.pre.helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.StringTokenizer;

import org.bson.BsonArray;
import org.bson.BsonDouble;
import org.bson.Document;

import com.gmv.pre.definitions.DatabaseDefinitions;
import com.gmv.pre.definitions.FieldDefinitions;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ImportDatabase {

	private static String gold1 = "{ \"OfferingID\" : \"PROC_001_GOLD\", \"Core\" : \"GMV_PROC_001\", \"Upstream Additions\" : [{\"ProcID\" : \"GMV_PROC_002\", \"Rendered By\" : \"GMV_PRACTICE_20\", \"Rendered At\" : \"GMV_PRACTICE_1\"}, {\"ProcID\" : \"GMV_PROC_003\", \"Rendered By\" : \"GMV_PRACTICE_15\", \"Rendered At\" : \"GMV_PRACTICE_0\"}], \"Base Price\" : 100.95, \"Promo Price\" : {\"Weekdays\" : 80.95}}";
	private static String silver1 = "{ \"OfferingID\" : \"PROC_001_SILVER\", \"Core\" : \"GMV_PROC_001\", \"Upstream Additions\" : [{\"ProcID\" : \"GMV_PROC_002\", \"Rendered By\" : \"GMV_PRACTICE_20\", \"Rendered At\" : \"GMV_PRACTICE_1\"}, {\"ProcID\" : \"GMV_PROC_003\", \"Rendered By\" : \"GMV_PRACTICE_15\", \"Rendered At\" : \"GMV_PRACTICE_0\"}], \"Base Price\" : 100.95, \"Promo Price\" : {\"Weekdays\" : 80.95}}";

	private static String gold2 = "{ \"OfferingID\" : \"PROC_002_GOLD\", \"Core\" : \"GMV_PROC_002\", \"Upstream Additions\" : [{\"ProcID\" : \"GMV_PROC_003\", \"Rendered By\" : \"GMV_PRACTICE_30\", \"Rendered At\" : \"GMV_PRACTICE_1\"}, {\"ProcID\" : \"GMV_PROC_003\", \"Rendered By\" : \"GMV_PRACTICE_15\", \"Rendered At\" : \"GMV_PRACTICE_0\"}], \"Base Price\" : 100.95, \"Promo Price\" : {\"Weekdays\" : 80.95}}";
	private static String silver2 = "{ \"OfferingID\" : \"PROC_002_SILVER\", \"Core\" : \"GMV_PROC_002\", \"Upstream Additions\" : [{\"ProcID\" : \"GMV_PROC_002\", \"Rendered By\" : \"GMV_PRACTICE_14\", \"Rendered At\" : \"GMV_PRACTICE_1\"}, {\"ProcID\" : \"GMV_PROC_003\", \"Rendered By\" : \"GMV_PRACTICE_15\", \"Rendered At\" : \"GMV_PRACTICE_0\"}], \"Base Price\" : 100.95, \"Promo Price\" : {\"Weekdays\" : 80.95}}";

	public ImportDatabase () {
		
	}
	/**
	 * @author sriramr
	 * Imports a list of breeds to the database. The input should be csv that contains
	 * a) Name (such as Labrador Retriever or Chihuahua)
	 * b) Category (such as hound, terrier etc.)
	 * For now "Events" and "AltCode" fields are empty.
	 * Appended to the input for each record are -
	 * a) A unique ID (a simple GMV_CANINE_BREED_count++)
	 * b) type field (set as canine)
	 */
	public void importDogBreedsToDB (String csvFileName, String dbName, String collName) {
		// Open up Mongo Connection
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoDatabase db = client.getDatabase (dbName);
		MongoCollection<Document> coll = db.getCollection (collName);
		
		int id = 1;
		// Open up the CSV file and read every record
        String splitBy = ",";
        try {
			BufferedReader br = new BufferedReader(new FileReader(csvFileName));
			String line = br.readLine();
			while(line!=null){
			     String[] b = line.split(splitBy);
			     if (b.length < 2) {
			    	 // missing either name or category. go to the next line
			    	 line = br.readLine(); 
		    	 } else if (b[0] == FieldDefinitions.NAME) {
		    		 // Header line. Ignore this and go to the next line
		    		 line = br.readLine();
		    	 }
			     else {
				     Document d = new Document (FieldDefinitions.NAME, b[0]);
				     d.append(FieldDefinitions.CATEGORY, b[1]);
				     String uid = FieldDefinitions.CANINE_PREFIX + id++;
				     d.append(FieldDefinitions.UNIQUE_ID, uid);
				     d.append(FieldDefinitions.TYPE, FieldDefinitions.CANINE);
				     coll.insertOne(d);
				     line=br.readLine();
				}
			}
			br.close();
        } catch (Exception e) {
        	client.close();
        	e.printStackTrace();
        }
        client.close();
	}
	
	/**
	 * @author sriramr
	 * Imports a list of breeds to the database. The input should be csv or JSON
	 * For now "Events" and "AltCode" fields are empty.
	 * Appended to the input for each record are -
	 * a) A unique ID (a simple GMV_FELINE_BREED_count++)
	 * b) type field (set as feline)
	 */
	public void importFelineBreedsToDB (String csvFileName, String dbName, String collName) {
		// Open up Mongo Connection
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoDatabase db = client.getDatabase (dbName);
		MongoCollection<Document> coll = db.getCollection (collName);
		
		int id = 1;
		// Open up the CSV file and read every record
        String splitBy = ",";
        try {
			BufferedReader br = new BufferedReader(new FileReader(csvFileName));
			String line = br.readLine();
			while(line!=null){
			     String[] b = line.split(splitBy);
			     if (b.length < 2) {
			    	 // missing either name or category. go to the next line
			    	 line = br.readLine(); 
		    	 } else if (b[0] == FieldDefinitions.NAME) {
		    		 // Header line. Ignore this and go to the next line
		    		 line = br.readLine();
		    	 }
			     else {
				     Document d = new Document (FieldDefinitions.NAME, b[0]);
				     d.append(FieldDefinitions.CATEGORY, b[1]);
				     String uid = FieldDefinitions.CANINE_PREFIX + id++;
				     d.append(FieldDefinitions.UNIQUE_ID, uid);
				     d.append(FieldDefinitions.TYPE, FieldDefinitions.FELINE);
				     coll.insertOne(d);
				     line=br.readLine();
				}
			}
			br.close();
        } catch (Exception e) {
        	client.close();
        	e.printStackTrace();
        }
        client.close();
	}
	
		
	/**
	 * @author sriramr
	 * Imports a list of practices to the database. The input should be csv containing
	 * a) Name of the practice
	 * b) longitude
	 * c) latitude
	 * The following fields will be added
	 * a) Unique ID (GMV_PRAC_count++)
	 * b) Ranking (a random number generated from 0-5)
	 * c)
	 */
	public void importPracticesToDB (String csvFileName, String dbName, String collName) {
		// Open up Mongo Connection
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoDatabase db = client.getDatabase (dbName);
		MongoCollection<Document> coll = db.getCollection (collName);
		
		int id = 1;
		// Open up the CSV file and read every record
        String splitBy = ",";
        try {
			BufferedReader br = new BufferedReader(new FileReader(csvFileName));
			String line = br.readLine();
			while(line!=null){
			     String[] b = line.split(splitBy);
			     if (b.length < 3) {
			    	 // missing either name, latitude or longitude. go to the next line
			    	 line = br.readLine(); 
		    	 } else if (b[1].contains(FieldDefinitions.LONGITUDE) ||
		    			 	b[2].contains(FieldDefinitions.LATITUDE)) {
		    		 // Header line. Ignore this and go to the next line
		    		 line = br.readLine();
		    	 }
			     else {
				     Document d = new Document (FieldDefinitions.NAME, b[0]);
				     BsonDouble latitude = new BsonDouble(Double.parseDouble(b[2]));
				     BsonDouble longitude = new BsonDouble (Double.parseDouble(b[1]));
				     Document loc = new Document ("type", "Point");
				     loc.append("coordinates", new BsonArray(Arrays.asList(longitude, latitude)));
				     d.append ("loc", loc);
				     d.append (FieldDefinitions.UNIQUE_ID, FieldDefinitions.PRACTICE_PREFIX + id++);
				     d.append (FieldDefinitions.RANKING, Math.random() * 5);
				     coll.insertOne(d);
				     line=br.readLine();
				}
			}
			br.close();
        } catch (Exception e) {
        	client.close();
        	e.printStackTrace();
        }
        coll.createIndex(new BasicDBObject ("loc", "2dsphere"));
        client.close();
	}

	public void importPracticesToDB_new (String csvFileName, String dbName, String collName) {
		// Open up Mongo Connection
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoDatabase db = client.getDatabase (dbName);
		MongoCollection<Document> coll = db.getCollection (collName);
		if (db == null || coll == null) {
			return;
		}

		Document gold1Doc = Document.parse(gold1);
		Document silver1Doc = Document.parse(silver1);
		Document gold2Doc = Document.parse(gold2);
		Document silver2Doc = Document.parse(silver2);
		int id = 1;
		// Open up the CSV file and read every record
        String splitBy = ",";
        String name = "", city = "", state = "", zip = "", longitude = "", latitude = "";
        try {
			BufferedReader br = new BufferedReader(new FileReader(csvFileName));
			String line = br.readLine();
			while(line!=null){
			     String[] b = line.split(splitBy);
			     if (b.length < 6) {
			    	 // missing one of the fields - just move on
			    	 line = br.readLine(); 
		    	 } else if (b[4].contains(FieldDefinitions.LONGITUDE) ||
		    			 	b[5].contains(FieldDefinitions.LATITUDE)) {
		    		 // Header line. Ignore this and go to the next line
		    		 line = br.readLine();
		    	 }
			     else {
			    	 name = b[0];
			    	 city = b[1];
			    	 state = b[2];
			    	 zip = b[3];
			    	 longitude = b[4];
			    	 latitude = b[5];
				     Document d = new Document (FieldDefinitions.NAME, name);
				     d.append("City", city);
				     d.append("State", state);
				     d.append("Zip", zip);
				     BsonDouble longitude_d = new BsonDouble (Double.parseDouble(longitude));
				     BsonDouble latitude_d = new BsonDouble(Double.parseDouble(latitude));
				     Document loc = new Document ("type", "Point");
				     loc.append("coordinates", new BsonArray(Arrays.asList(longitude_d, latitude_d)));
				     d.append ("loc", loc);
				     d.append (FieldDefinitions.UNIQUE_ID, FieldDefinitions.PRACTICE_PREFIX + id++);
				     d.append (FieldDefinitions.RANKING, Math.random() * 5);
					if (Math.random() > 0.5) {
						d.append("Offering", Arrays.asList(gold1Doc, silver1Doc));
					} else {
						d.append("Offering", Arrays.asList(gold2Doc, silver2Doc));
					}
				     line=br.readLine();
				     coll.insertOne(d);
				}
			}
			br.close();
        } catch (Exception e) {
        	coll.insertOne(new Document("Exception", e.getMessage()).append("Exception String", e.toString()));
        	client.close();
        	e.printStackTrace();
        }
        coll.createIndex(new BasicDBObject ("loc", "2dsphere"));
        client.close();
	}

	public void importDogBreedsToDB_newFormat(String breedFp, String dbName, String collName) {
		// Open up Mongo Connection
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoDatabase db = client.getDatabase (dbName);
		MongoCollection<Document> coll = db.getCollection (collName);
		
		int id = 1;

		try {
			String json = null;
			BufferedReader br = new BufferedReader(new FileReader(breedFp));
			String line = br.readLine();
			while(line!=null) {
				if (line.contains("//begin")) {
					// open the json
					json = "{";
				} else if (line.contains("//end")) {
					json += "}";
					Document breed = Document.parse(json);
				    String uid = FieldDefinitions.CANINE_PREFIX + id++;
				    breed.append(FieldDefinitions.UNIQUE_ID, uid);
				    breed.append(FieldDefinitions.TYPE, FieldDefinitions.CANINE);
				    coll.insertOne(breed);
					
				} else {
					json += line;
				}
				line = br.readLine();
			}
		} catch (Exception e) {
			client.close();
			e.printStackTrace();
		}
	}

}
