package com.gmv.pre.structs;

import java.util.ArrayList;
import java.util.Iterator;

import org.bson.Document;

import com.gmv.pre.definitions.DatabaseDefinitions;
import com.gmv.pre.definitions.FieldDefinitions;
import com.gmv.pre.definitions.RoleDefinitions;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class PractitionerDoc {
	private static String __dbName = "";
	private static String __collName = "";

	private String __id;
	private String __name;
	private String __roleName;
	private String __email;
	private String __phone;
	private ArrayList<String> __offices;
	
	public static void initDBVariables (String dbName, String collName) {
		__dbName = dbName;
		__collName = collName;
	}
	
	public PractitionerDoc () {
		initAndSetDefaults ();
	}
	
	public PractitionerDoc (String id) {
		initAndSetDefaults ();
		__id = id;
		Read (id);
	}
	
	private void initAndSetDefaults () {
		// set defaults
		__id = "";
		__name = "";
		__roleName = RoleDefinitions.ROLE_001;
		__email = "gmv@gmv.com";
		__phone = "1234567890";
		
		__offices = new ArrayList<String>();
	}

	public Document toDocument () {
		Document doc = new Document (FieldDefinitions.UNIQUE_ID, __id);
		doc.append(FieldDefinitions.NAME, __name);
		doc.append(FieldDefinitions.ROLE_NAME, __roleName);
		doc.append(FieldDefinitions.EMAIL, __email);
		doc.append(FieldDefinitions.PHONE, __phone);
		doc.append(FieldDefinitions.PRACTICE_LOC, __offices);
		return doc;
	}
	
	public void fromDocument (Document doc) {
		__id = doc.getString(FieldDefinitions.UNIQUE_ID);
		__name = doc.getString(FieldDefinitions.NAME);
		__roleName = doc.getString(FieldDefinitions.ROLE_NAME);
		__email = doc.getString(FieldDefinitions.EMAIL);
		__phone = doc.getString(FieldDefinitions.PHONE);
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
		if (__id == null || __id == "") {__id = __name;}
		Document match = new Document (FieldDefinitions.UNIQUE_ID, __id);
		if (coll.count(match) > 0) {
			coll.findOneAndReplace(match, doc);
		} else {
			coll.insertOne(doc);
		}
		client.close();
	}
	
	public String getAllByName (String name) {
		String toReturn = "";
		if (__dbName == "" || __collName == "")
			return toReturn;
		
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoCollection<Document> coll = client.getDatabase(__dbName).getCollection(__collName);
		Document docToFind = new Document (FieldDefinitions.NAME, name);
		FindIterable<Document> found = coll.find(docToFind);
		Iterator<Document> iter = found.iterator ();
		toReturn = "[";
		while (iter.hasNext()) {
			Document doc = iter.next ();
			toReturn += doc.toJson();
			if (iter.hasNext()) {toReturn += ",";}
		}
		toReturn += "]";
		return toReturn;
	}
	
	public String getName () {
		return __name;
	}
	
	public void setName (String name) {
		__name = name;
	}
	
	public String getRoleName () {
		return __roleName;
	}
	
	public void setRoleName (String name) {
		__roleName = name;
	}
	
	public String getEmail () {
		return __email;
	}
	
	public void setEmail (String name) {
		__email = name;
	}
	
	public String getPhone () {
		return __phone;
	}
	
	public void setPhone (String name) {
		__phone = name;
	}
	
	public void addOffice (String officeID) {
		if (__offices.contains(officeID))
			return;
		__offices.add(officeID);
	}

	public void removeOffice (String officeID) {
		if (__offices.contains(officeID))
			return;
		__offices.remove(officeID);
	}
}
