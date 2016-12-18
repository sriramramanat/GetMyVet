package com.gmv.pre.structs;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.bson.Document;

import com.gmv.pre.definitions.DatabaseDefinitions;
import com.gmv.pre.definitions.FieldDefinitions;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

/**
 * @author sriramr
 * A pet is an instance of a breed.
 *
 */


public class PetDoc {
	private static String __dbName = "";
	private static String __collName = "";
	private static String __breedCollName = "";
	private static String __ownerCollName = "";

	private String __id;
	private String __name;
	private String __ownerID;
	private String __breedID;
	private String __gender;
	private ArrayList<Document> __history;
	private Date __dateOfAdoption;
	
	public static void initDBVariables (String dbName, String collName) {
		__dbName = dbName;
		__collName = collName;
		__breedCollName = "breeds";
		__ownerCollName = "owners";
	}
	
	public static void initDBVariables (String dbName, String collName, String breedCollName, String ownerCollName) {
		__dbName = dbName;
		__collName = collName;
		__breedCollName = breedCollName;
		__ownerCollName = ownerCollName;
	}
	
	public PetDoc (String id) {
		initAndSetDefaults ();
		__id = id;
		Read (__id);
	}
	
	public PetDoc (String ownerID, String petName, String breedType, String gender) {
		initAndSetDefaults ();
		__id = ownerID + "_" + petName;
		__name = petName;
		// TODO - what if owner does not exist?
		__ownerID = ownerID;
		// TODO - what if breed does not exist??
		__breedID = breedType;
		__gender = gender;
	}
	
	private void initAndSetDefaults () {
		// init arrays
		__history = new ArrayList<Document>();
		
		// set defaults
		__id = "";
		__name = "";
		__ownerID = "";
		__breedID = "";
		__gender = "";
		__dateOfAdoption = new Date ();
	}

	public Document toDocument () {
		Document doc = new Document (FieldDefinitions.UNIQUE_ID, __id);
		doc.append(FieldDefinitions.OWNER_ID, __ownerID);
		doc.append(FieldDefinitions.BREED_ID, __breedID);
		doc.append(FieldDefinitions.DATE_ADOPTED, __dateOfAdoption);
		doc.append(FieldDefinitions.HISTORY, __history);
		doc.append(FieldDefinitions.GENDER, __gender);
		return doc;
	}
	
	public void fromDocument (Document doc) {
		__id = doc.getString(FieldDefinitions.UNIQUE_ID);
		__name = doc.getString(FieldDefinitions.NAME);
		__ownerID = doc.getString(FieldDefinitions.OWNER_ID);
		__breedID = doc.getString(FieldDefinitions.BREED_ID);
		__dateOfAdoption = doc.getDate(FieldDefinitions.DATE_ADOPTED);
		__history = (ArrayList<Document>) (doc.get(FieldDefinitions.HISTORY));
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
		MongoCollection<Document> coll = client.getDatabase(__dbName).getCollection(__collName);
		Document doc = toDocument ();
		Document match = new Document (FieldDefinitions.UNIQUE_ID, __id);
		if (coll.count(match) > 0) {
			coll.findOneAndReplace(match, doc);
		} else {
			coll.insertOne(doc);
		}
		client.close();
	}
	
	public String getID () {
		return __id;
	}
	
	public void changeOwner (String newOwner) {
		__ownerID = newOwner;
	}
	
	public String getGender () {
		return __gender;
	}
	
	public String getBreed () {
		return __breedID;
	}
	
	public Date getDateOfAdoption () {
		return __dateOfAdoption;
	}
	
	public void setDateOfAdoption (Date doA) {
		__dateOfAdoption = doA;
	}
	
	public void addToHistory (Date date, String procedurePerformed, String performedBy) {
		Document history = new Document ("Date", date);
		history.append ("Procedure", procedurePerformed);
		history.append ("Performed By", performedBy);
		history.append ("Owned By", __ownerID);
		if (!__history.contains(history))
			__history.add(history);
	}
	
	public void removeFromHistory (Date date, String procedurePerformed) {
		Document history = new Document ("Date", date);
		history.append ("Procedure", procedurePerformed);
		__history.remove(history);
	}
}
