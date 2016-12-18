package com.gmv.pre.structs;

import java.util.ArrayList;
import java.util.Iterator;

import org.bson.Document;

import com.gmv.pre.definitions.DatabaseDefinitions;
import com.gmv.pre.definitions.FieldDefinitions;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class UserDefDoc {
	private String __id;
	private String __name;
	private String __description;
	
	public UserDefDoc () {
		
	}
	
	public UserDefDoc (String id, String name) {
		__id = id;
		__name = name;
		__description = name;
	}
	
	public String getID () {
		return __id;
	}
	
	public void setID (String id) {
		__id = id;
	}

	public String getName () {
		return __name;
	}
	
	public void setName (String name) {
		__name = name;
	}

	public String getDescription () {
		return __description;
	}
	
	public void setDescription (String desc) {
		__description = desc;
	}
	
	public Document toDocument () {
		Document doc = new Document (FieldDefinitions.UNIQUE_ID, __id);
		doc.append(FieldDefinitions.NAME, __name);
		doc.append(FieldDefinitions.DESCRIPTION, __description);
		return doc;
	}
	
	public String toJSON () {
		return toDocument().toJson();
	}
	
	public void fromDocument (Document doc) {
		__id = doc.getString(FieldDefinitions.UNIQUE_ID);
		__name = doc.getString(FieldDefinitions.NAME);
		__description = doc.getString(FieldDefinitions.DESCRIPTION);
	}

	public void read (String id) {
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoCollection<Document> coll = client.getDatabase(DatabaseDefinitions.NOSQL_DB).
												getCollection(DatabaseDefinitions.USERDEF_COLL);
		FindIterable<Document> docs = coll.find(new Document(FieldDefinitions.UNIQUE_ID, id));
		Iterator<Document> iter = docs.iterator ();
		if (iter.hasNext()) {
			Document doc = iter.next();
			fromDocument (doc);
		}
		client.close();
	}
	
	public void write () {
		Document doc = toDocument ();		
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoCollection<Document> coll = client.getDatabase(DatabaseDefinitions.NOSQL_DB).
												getCollection(DatabaseDefinitions.USERDEF_COLL);
		Document match = new Document (FieldDefinitions.UNIQUE_ID, __id);
		if (coll.count(match) > 0) {
			coll.replaceOne (match, doc); 
		} else {
			coll.insertOne(doc);
		}
		client.close();
	}
	
	public static ArrayList<Document> readAll () {
		ArrayList<Document> allDocs = new ArrayList<Document>();
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoCollection<Document> coll = client.getDatabase(DatabaseDefinitions.NOSQL_DB).
												getCollection(DatabaseDefinitions.USERDEF_COLL);
		FindIterable<Document> docs = coll.find();
		Iterator<Document> iter = docs.iterator ();
		while (iter.hasNext()) {
			allDocs.add(iter.next());
		}
		client.close();
		return allDocs;
	}
	
	public static UserDefDoc get (String id) {
		UserDefDoc udef = new UserDefDoc ();
		udef.read(id);
		return udef;
	}
}
