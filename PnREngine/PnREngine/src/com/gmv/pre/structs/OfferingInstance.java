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

public class OfferingInstance {

	private static String __dbName = "";
	private static String __collName = "";

	private String __id;
	private OfferingDoc __offering;
	private Date __timeOfSale;
	private Date __timeOfCommencement;
	private String __sellerID;
	private String __buyerID;
	private int __currentStep;
	private Date __currentStepTime;
	private ArrayList<Document> __completedSteps;
	private ArrayList<Document> __upcomingSteps;
	private ArrayList<Document> __skippedSteps;
	private ArrayList<Document> __replacedSteps;
	private ArrayList<Document> __addedSteps;
	
	public static void initDBVariables (String dbName, String collName) {
		__dbName = dbName;
		__collName = collName;
	}
	
	private void initAndSetDefaults () {
		__id = "";
		__offering = null;
		__timeOfSale = new Date ();
		__timeOfCommencement = new Date ();
		__sellerID = "";
		__buyerID = "";
		__currentStep = 1;
		__currentStepTime = new Date ();
		__completedSteps = new ArrayList<Document>();
		__upcomingSteps = new ArrayList<Document>();
		__skippedSteps = new ArrayList<Document>();
		__replacedSteps = new ArrayList<Document>();
		__addedSteps = new ArrayList<Document>();
		
	}
	public OfferingInstance () {
		initAndSetDefaults ();
	}
	
	public Document toDocument () {
		Document doc = new Document (FieldDefinitions.UNIQUE_ID, __id);
		doc.append(FieldDefinitions.OFFERING, __offering.toDocument());
		doc.append(FieldDefinitions.TIME_OF_SALE, __timeOfSale);
		doc.append(FieldDefinitions.TIME_OF_COMMENCEMENT, __timeOfCommencement);
		doc.append("Seller", __sellerID);
		doc.append("Buyer", __buyerID);
		doc.append("Current Step", __currentStep);
		doc.append("Next Appointment", __currentStepTime);
		doc.append("Completed Steps", __completedSteps);
		doc.append("Upcoming Steps", __upcomingSteps);
		doc.append("Skipped Steps", __skippedSteps);
		doc.append("Replaced Steps", __replacedSteps);
		doc.append("Added Steps", __addedSteps);
		return doc;
	}
	
	public void fromDocument (Document doc) {
		__id = doc.getString(FieldDefinitions.UNIQUE_ID);
		Document offering = (Document)doc.get(FieldDefinitions.OFFERING);
		__offering = new OfferingDoc();
		if (offering != null) {
			__offering.fromDocument(offering);
		}
		__timeOfSale = (Date)doc.get(FieldDefinitions.TIME_OF_SALE);
		__timeOfCommencement = (Date)doc.get(FieldDefinitions.TIME_OF_COMMENCEMENT);
		__sellerID = doc.getString("Seller");
		__buyerID = doc.getString("Buyer");
		__currentStep = doc.getInteger("Current Step");
		__currentStepTime = (Date)doc.get("Next Appointment");
		__completedSteps = (ArrayList<Document>)doc.get("Completed Steps");
		__upcomingSteps = (ArrayList<Document>)doc.get("Upcoming Steps");
		__skippedSteps = (ArrayList<Document>)doc.get("Skipped Steps");
		__replacedSteps = (ArrayList<Document>)doc.get("Replaced Steps");
		__addedSteps = (ArrayList<Document>)doc.get("Added Steps");
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
}
