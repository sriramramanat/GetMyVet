package com.gmv.pre.structs;

import java.util.ArrayList;
import java.util.Iterator;

import org.bson.Document;

import com.gmv.pre.definitions.DatabaseDefinitions;
import com.gmv.pre.definitions.FieldDefinitions;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

/**
 * @author sriramr
 *
 */

public class BreedDoc {
	private static String __dbName = "";
	private static String __collName = "";
	
	private class Event {
		public double __time;
		ArrayList<String> __procedures;
	}

	private String __id;
	private String __name;
	private String __type;
	private String __category;
	private double __lifeExpectancy;
	private String __sizeCategory;
	private double __lifetimeCost;
	private int __popularityRank;
	private ArrayList<Document> __altCode;
	private ArrayList<Document> __congenitalIssues;
	private ArrayList<Event> __commonEvents;
	private ArrayList<Event> __maleEvents;
	private ArrayList<Event> __femaleEvents;

	public static void initDBVariables (String dbName, String collName) {
		__dbName = dbName;
		__collName = collName;
	}
	
	public static boolean doesExist (String breedName) {
		if (__dbName == "" || __collName == "")
			return false;
		
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoCollection<Document> coll = client.getDatabase(__dbName).getCollection(__collName);
		FindIterable<Document> docs = coll.find(new Document(FieldDefinitions.BREED, new Document("$regex", breedName).append("$options", "i")));
		Iterator<Document> iter = docs.iterator ();
		if (iter.hasNext()) {
			client.close();
			return true;
		}
		client.close();
		return false;
	}
	
	public static Document getByName (String breedName) {
		Document out = null;
		if (__dbName == "" || __collName == "")
			return out;
		
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoCollection<Document> coll = client.getDatabase(__dbName).getCollection(__collName);
		FindIterable<Document> docs = coll.find(new Document(FieldDefinitions.BREED, new Document("$regex", breedName).append("$options", "i")));
		Iterator<Document> iter = docs.iterator ();
		if (iter.hasNext()) {
			out = iter.next();
		}
		client.close();
		return out;
	}
	
	public BreedDoc () {
		initAndSetDefaults ();
	}
	
	public BreedDoc (String id) {
		initAndSetDefaults ();
		__id = id;
		Read (id);
	}
	
	private void initAndSetDefaults () {
		// init arrays
		__altCode = new ArrayList<Document>();
		__congenitalIssues = new ArrayList<Document>();
		__commonEvents = new ArrayList<Event>();
		__maleEvents = new ArrayList<Event>();
		__femaleEvents = new ArrayList<Event>();
		
		// set defaults
		__id = "";
		__name = "";
		__type = "";
		__category = "";
		__sizeCategory = "";
		__lifeExpectancy = 5.0;
		__lifetimeCost = 5000;
		__popularityRank = 0;
	}

	public Document toDocument () {
		Document doc = new Document (FieldDefinitions.UNIQUE_ID, __id);
		doc.append(FieldDefinitions.BREED, __name);
		doc.append(FieldDefinitions.TYPE, __type);
		doc.append(FieldDefinitions.CATEGORY, __category);
		doc.append(FieldDefinitions.LIFE_EXPECTANCY, __lifeExpectancy);
		doc.append(FieldDefinitions.SIZE_CAT, __sizeCategory);
		doc.append(FieldDefinitions.LIFETIME_COST, __lifetimeCost);
		doc.append(FieldDefinitions.ALT_CODE, __altCode);
		doc.append(FieldDefinitions.CONGENITAL_ISSUES, __congenitalIssues);
		doc.append(FieldDefinitions.POPULARITY_RANK, __popularityRank);
		return doc;
	}
	
	public void fromDocument (Document doc) {
		__id = doc.getString(FieldDefinitions.UNIQUE_ID);
		__name = doc.getString(FieldDefinitions.BREED);
		__type = doc.getString(FieldDefinitions.TYPE);
		__category = doc.getString(FieldDefinitions.CATEGORY);
		__sizeCategory = doc.getString(FieldDefinitions.SIZE_CAT);
		__popularityRank = doc.getInteger(FieldDefinitions.POPULARITY_RANK);
		ArrayList<Document> tempArray = (ArrayList<Document>) (doc.get(FieldDefinitions.ALT_CODE));
		if (tempArray != null) {__altCode = tempArray; }
		tempArray = (ArrayList<Document>)(doc.get(FieldDefinitions.CONGENITAL_ISSUES));
		if (tempArray != null) {__congenitalIssues = tempArray;}
		Object lifeExpectancy = doc.get(FieldDefinitions.LIFE_EXPECTANCY);
		Object lifetimeCost = doc.get(FieldDefinitions.LIFETIME_COST);
		try {
			__lifeExpectancy = Double.parseDouble(lifeExpectancy.toString());
			String costStr = lifetimeCost.toString();
			costStr = costStr.replace (",", "");
			__lifetimeCost = Double.parseDouble(costStr);
		} catch (Exception e) { /* nothing to do. let the defaults kick in */ }
	}
	
	public String toJSON () {
		return toDocument().toJson();
	}

	public void Read (String id) {
		if (__dbName == "" || __collName == "")
			return;
		
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoCollection<Document> coll = client.getDatabase(__dbName).getCollection(__collName);
		FindIterable<Document> docs = coll.find(new Document(FieldDefinitions.UNIQUE_ID, id));
		Iterator<Document> iter = docs.iterator ();
		if (iter.hasNext()) {
			Document doc = iter.next();
			fromDocument (doc);
		}
		client.close();
	}
	
	public void Write () {
		if (__dbName == "" || __collName == "")
			return;

		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		try {
			MongoCollection<Document> coll = client.getDatabase(__dbName).getCollection(__collName);
			Document doc = toDocument ();
			Document match = new Document (FieldDefinitions.UNIQUE_ID, __id);
			if (coll.count(match) > 0) {
				coll.findOneAndReplace(match, doc);
			} else {
				coll.insertOne(doc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		client.close();
	}
	
	public String getName () {
		return __name;
	}
	
	public void setName (String name) {
		__name = name;
	}
	
	public String getID () {
		return __id;
	}
	
	public void setID(String id) {
		__id = id;
	}

	public String getType () {
		return __type;
	}
	
	public void setType (String type) {
		__type = type;
	}
	
	public String getCategory () {
		return __category;
	}
	
	public void setCategory (String category) {
		__category = category;
	}
	
	public int getPopularityRank () {
		return __popularityRank;
	}
	
	public void setPopularityRank (int popRank) {
		__popularityRank = popRank;
	}

	public double getLifeExpectancy () {
		return __lifeExpectancy;
	}
	
	public void setLifeExpectancy (double lifeExpectancy) {
		__lifeExpectancy = lifeExpectancy;
	}
	
	public String getSizeCategory () {
		return __sizeCategory;
	}
	
	public void setSizeCategory (String sCat) {
		__sizeCategory = sCat;
	}
	
	public double getLifetimeCost () {
		return __lifetimeCost;
	}
	
	public void setLifetimeCost (double lifetimeCost) {
		__lifetimeCost = lifetimeCost;
	}
	
	public ArrayList<Document> getAltCode () {
		return __altCode;
	}
	
	public void addAltCode (String code, String provider) {
		Document altCode = new Document ("Code", code);
		altCode.append("Provider", provider);
		if (__altCode == null) {
			__altCode = new ArrayList<Document>();
		}
		if (!__altCode.contains(altCode))
			__altCode.add(altCode);
	}
	
	public void removeAltCode (String code, String provider) {
		Document altCode = new Document ("Code", code);
		altCode.append("Provider", provider);
		if (__altCode == null) {
			return;
		}
		__altCode.remove(altCode);
	}
	
	public ArrayList<Document> getCongenitalIssues () {
		return __congenitalIssues;
	}
	
	public void addCongenitalIssue (String part, String issue) {
		Document altCode = new Document ("Area", part);
		altCode.append("Issue", issue);
		if (__congenitalIssues == null) {
			__congenitalIssues = new ArrayList<Document>();
		}
		if (!__congenitalIssues.contains(altCode))
			__congenitalIssues.add(altCode);
	}
	
	public void removeCongenitalIssue (String part, String issue) {
		Document altCode = new Document ("Area", part);
		altCode.append("Issue", issue);
		if (__congenitalIssues == null) {
			return;
		}
		__congenitalIssues.remove(altCode);
	}
	
}
