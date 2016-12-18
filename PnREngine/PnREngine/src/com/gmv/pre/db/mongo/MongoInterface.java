package com.gmv.pre.db.mongo;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.gmv.pre.definitions.DatabaseDefinitions;
import com.gmv.pre.definitions.FieldDefinitions;
import com.mongodb.AggregationOptions;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

import java.util.ArrayList;
import java.util.Iterator;

import javax.ws.rs.core.Response;

import org.bson.Document;
import org.json.JSONObject;


public class MongoInterface {
	
	public MongoCollection<Document> coll = null;
	private MongoClient __client = null;

	public MongoInterface (String dbName, String collName) {
		__client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		if (__client != null) {
			MongoDatabase db = __client.getDatabase(dbName);
			if (db != null) {
				coll = db.getCollection(collName);
			}
		}
	}
	
	public void close () {
		if (__client != null) {
			__client.close();
		}
	}
	
	public void insert (Document doc) {
		if (coll != null && doc != null) {
			coll.insertOne(doc);
		}
	}

	public void insertOrUpdate(String uid, JSONObject jsonObj) {
		Document doc = Document.parse(jsonObj.toString());
		if (coll != null) {
			BasicDBObject match = new BasicDBObject (FieldDefinitions.UNIQUE_ID, uid);
			long count = coll.count(match);
			if (count == 0) {
				coll.insertOne(doc);
			} else {
				coll.updateOne(match, doc);
			}
		}
	}
	
	public String getAllDocsInJSON () {
		String outJSON = "[";
		FindIterable<Document> iterable = coll.find();
		Iterator<Document> iter = iterable.iterator ();
		while (iter.hasNext()) {
			Document doc = iter.next ();
			outJSON += doc.toJson();
			if (iter.hasNext())
				outJSON += ",";
		}
		outJSON += "]";
		return outJSON;
	}
	
	public String getAllDocsInJSON (String key, String val) {
		Document toFind = new Document (key, val);
		return getAllDocsInJSON (toFind);
	}
	
	public String getAllDocsInJSON (Document toFind) {
		Document outDoc = new Document ();
		ArrayList<Document> outList = new ArrayList<Document>();
		int count = 0;
		FindIterable<Document> iterable = coll.find(toFind);
		Iterator<Document> iter = iterable.iterator ();
		while (iter.hasNext()) {
			Document doc = iter.next ();
			outList.add(doc);
			count++;
		}
		outDoc.append("Count", count);
		outDoc.append("Query Results", outList);
		return outDoc.toJson();
	}
	
	public Document getQueryResults (Document toFind) {
		Document outDoc = new Document ();
		ArrayList<Document> outList = new ArrayList<Document>();
		int count = 0;
		FindIterable<Document> iterable = coll.find(toFind);
		Iterator<Document> iter = iterable.iterator ();
		while (iter.hasNext()) {
			Document doc = iter.next ();
			outList.add(doc);
			count++;
		}
		outDoc.append("Count", count);
		outDoc.append("Query Results", outList);
		return outDoc;
	}

	public Document getQueryResults (Document toFind, Document sortOrder) {
		Document outDoc = new Document ();
		ArrayList<Document> outList = new ArrayList<Document>();
		int count = 0;
		FindIterable<Document> iterable = coll.find(toFind);
		iterable.sort(sortOrder);
		Iterator<Document> iter = iterable.iterator ();
		while (iter.hasNext()) {
			Document doc = iter.next ();
			outList.add(doc);
			count++;
		}
		outDoc.append("Count", count);
		outDoc.append("Query Results", outList);
		return outDoc;
	}
	
	public Document getQueryResults (Document geoNear, Document aggregateMatch, Document aggregateGroup, Document aggregateUnwind, Document sortOrder) {
		Document outDoc = new Document ();
		ArrayList<Document> outList = new ArrayList<Document>();
		int count = 0;
		ArrayList<Document> aggregatePipe = new ArrayList<Document>();
		aggregatePipe.add(new Document ("$geoNear", geoNear));
		aggregatePipe.add(aggregateUnwind);
		aggregatePipe.add(new Document ("$match", aggregateMatch));
		aggregatePipe.add(new Document ("$sort", sortOrder));
		//aggregatePipe.add(new Document ("$limit", 1000));
		aggregatePipe.add(aggregateGroup);
		AggregateIterable<Document> aggOut = coll.aggregate(aggregatePipe).allowDiskUse(true);
		Iterator<Document> iter = aggOut.iterator();
		while (iter.hasNext()) {
			Document doc = iter.next ();
			outList.add(doc);
			count++;
		}
		outDoc.append("Count", count);
		outDoc.append("Query Results", outList);
		return outDoc;
	}

	public FindIterable<Document> getAllDocs (Document toFind) {
		return coll.find(toFind);
	}
	
	public long getDocCount (Document toFind) {
		return coll.count(toFind);
	}
	
	public Document getFirstMatch (Document toFind) {
		Document outDoc = null;
		FindIterable<Document> iterable = coll.find(toFind);
		Iterator<Document> iter = iterable.iterator ();
		if (iter.hasNext()) {
			outDoc = iter.next ();
		}
		return outDoc;
	}

	
	public String find (String key, String val) {
		String outJSON = "";
		BasicDBObject toFind = new BasicDBObject (key, val);
		FindIterable<Document> iterable = coll.find(toFind);
		Iterator<Document> iter = iterable.iterator ();
		while (iter.hasNext()) {
			Document doc = iter.next ();
			outJSON = doc.toJson();
		}
		return outJSON;
	}
	public void addStringToArray (String key, Object object, String arrayName, String strToInsert) {
		BasicDBObject match = new BasicDBObject (key, object);
		BasicDBObject update = new BasicDBObject ("$push", new BasicDBObject(arrayName, strToInsert));
		coll.updateOne (match, update);
	}
	
	public void addToArray (String key, Object object, String arrayName, Object objToInsert) {
		BasicDBObject match = new BasicDBObject (key, object);
		Document docToInsert = null;
		if (objToInsert instanceof Document) {
			docToInsert = (Document)(objToInsert);
		} else if (objToInsert instanceof String) {
			addStringToArray (key, object, arrayName, (String)(objToInsert));
			return;
		} else {
			docToInsert = Document.parse(objToInsert.toString());
		}
		BasicDBObject update = new BasicDBObject ("$push", new BasicDBObject(arrayName, docToInsert));
		coll.updateOne (match, update);
	}
	
	public void setField (String key, Object keyVal, String fieldName, Object fieldVal) {
		BasicDBObject match = new BasicDBObject (key, keyVal);
		BasicDBObject update = new BasicDBObject ("$set", new BasicDBObject (fieldName, fieldVal.toString()));
		coll.updateOne(match, update);
	}
	
	public Boolean exists (String key, Object val) {
		Boolean ret = false;
		BasicDBObject match = new BasicDBObject (key, val);
		long count = coll.count(match); 
		if (count != 0) {
			ret = true;
		}
		return ret;
	}

	public Response addToSubArray(String procID) {
		return Response.status(500).entity("Match Not Found").build();
	}
}
