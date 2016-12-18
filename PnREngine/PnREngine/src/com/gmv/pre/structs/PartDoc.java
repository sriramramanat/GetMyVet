package com.gmv.pre.structs;

import java.util.ArrayList;
import java.util.Iterator;

import org.bson.Document;

import com.gmv.pre.definitions.DatabaseDefinitions;
import com.gmv.pre.definitions.FieldDefinitions;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class PartDoc {
	private static String __dbName = "";
	private static String __collName = "";
	
	private String __id = null;
	private String __name = null;
	private String __type = null;
	private String __category = null;
	private String __description = null;
	private int __variantLevel = 0;
	private ArrayList<Document> __altCode;
	double __standardPrice;

	public static void initDBVariables (String dbName, String collName) {
		__dbName = dbName;
		__collName = collName;
	}

	public PartDoc () {
		initAndSetDefaults ();
	}
	
	public PartDoc (String id) {
		initAndSetDefaults ();
		__id = id;
		Read (id);
	}
	
	private void initAndSetDefaults () {
		// init array
		__altCode = new ArrayList<Document>();

		// set defaults
		__id = "";
		__name = "";
		__type = "";
		__category = "";
		__description = "";
		__variantLevel = 0;
		__standardPrice = 0;
	}

	public Document toDocument () {
		Document doc = new Document (FieldDefinitions.UNIQUE_ID, __id);
		doc.append(FieldDefinitions.NAME, __name);
		doc.append(FieldDefinitions.TYPE, __type);
		doc.append(FieldDefinitions.CATEGORY, __category);
		doc.append(FieldDefinitions.DESCRIPTION, __description);
		doc.append(FieldDefinitions.VARIANT_LEVEL, __variantLevel);
		doc.append(FieldDefinitions.ALT_CODE, __altCode);
		doc.append(FieldDefinitions.BASE_PRICE, __standardPrice);
		return doc;
	}
	
	public void fromDocument (Document doc) {
		__id = doc.getString(FieldDefinitions.UNIQUE_ID);
		__name = doc.getString(FieldDefinitions.NAME);
		__type = doc.getString(FieldDefinitions.TYPE);
		__category = doc.getString(FieldDefinitions.CATEGORY);
		__description = doc.getString(FieldDefinitions.DESCRIPTION);
		__variantLevel = doc.getInteger(FieldDefinitions.VARIANT_LEVEL, 0);
		__altCode = (ArrayList<Document>) (doc.get(FieldDefinitions.ALT_CODE));
		__standardPrice = doc.getDouble(FieldDefinitions.BASE_PRICE);
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

	public String getDescription () {
		return __description;
	}
	
	public void setDescription (String desc) {
		__description = desc;
	}
	
	public int getVariantLevel () {
		return __variantLevel;
	}
	
	public void setVariantLevel (int vl) {
		__variantLevel = vl;
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
	
	public double getStandardPrice () {
		return __standardPrice;
	}

	public void setStandardPrice (double stdPrice) {
		__standardPrice = stdPrice;
	}
}
