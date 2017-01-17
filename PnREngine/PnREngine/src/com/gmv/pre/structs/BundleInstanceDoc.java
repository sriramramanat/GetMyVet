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

public class BundleInstanceDoc {
	private String __id;
	private String __sellerID;
	private String __buyerID;
	private String __bundleID;
	private double __salePrice;
	private Date __timeOfSale;

	private Date __timeOfCommencement;
	private int __currentStepNumber;
	private String __currentStepID;
	private Date __currentStepTime;
	
	private ArrayList<Document> __includedSteps;
	private ArrayList<Document> __completedSteps;
	private ArrayList<Document> __upcomingSteps;
	private ArrayList<Document> __skippedSteps;
	private ArrayList<Document> __replacedSteps;
	private ArrayList<Document> __addedSteps;
	
	public enum PROCSTAT {NOT_STARTED ("Not Started"), 
						 IN_PROGRESS ("In Progress"), 
						 SKIPPED ("Skipped"), 
						 NOT_DONE ("Not Done"),
						 VET_COMPLETE ("Vet Complete"),
						 PET_COMPLETE ("Pet Complete"),
						 DONE ("Done");
		private final String __statusString;
		private PROCSTAT (String s) {
			__statusString = s;
		}
		
		@Override
		public String toString () {
			return __statusString;
		}
	} 
	
	private void initAndSetDefaults () {
		__id = "";
		__timeOfSale = new Date ();
		__timeOfCommencement = new Date ();
		__sellerID = "";
		__buyerID = "";
		__currentStepID = "GMV_PROC_001";
		__currentStepNumber = 1;
		__currentStepTime = new Date ();
		__includedSteps = new ArrayList<Document>();
		__completedSteps = new ArrayList<Document>();
		__upcomingSteps = new ArrayList<Document>();
		__skippedSteps = new ArrayList<Document>();
		__replacedSteps = new ArrayList<Document>();
		__addedSteps = new ArrayList<Document>();
		
	}
	
	public BundleInstanceDoc (String bundleID, String buyerID) {
		
	}

	public BundleInstanceDoc () {
		initAndSetDefaults ();
	}
	
	public Document toDocument () {
		Document doc = new Document (FieldDefinitions.UNIQUE_ID, __id);
//		doc.append(FieldDefinitions.OFFERING, __bundle.toDocument());
		doc.append("Seller", __sellerID);
		doc.append("Buyer", __buyerID);
		doc.append("Bundle", __bundleID);
		doc.append(FieldDefinitions.TIME_OF_SALE, __timeOfSale);
		doc.append("Sale Price", __salePrice);

		doc.append(FieldDefinitions.TIME_OF_COMMENCEMENT, __timeOfCommencement);
		doc.append("Included Steps", __includedSteps);
		doc.append("Current Step", __currentStepID);
		doc.append("Current Step Number", __currentStepNumber);
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
		__sellerID = doc.getString("Seller");
		__buyerID = doc.getString("Buyer");
		__bundleID = doc.getString("Bundle");
		__timeOfSale = (Date)doc.get(FieldDefinitions.TIME_OF_SALE);
		__salePrice = doc.getDouble("Sale Price");

		//		Document offering = (Document)doc.get(FieldDefinitions.OFFERING);
//		__bundle = BundleDoc.createBundle ();
//		if (offering != null) {
//			__bundle.fromDocument(offering);
//		}
		__timeOfCommencement = (Date)doc.get(FieldDefinitions.TIME_OF_COMMENCEMENT);
		__currentStepID = doc.getString("Current Step");
		__currentStepNumber = doc.getInteger("Current Step Number");
		__currentStepTime = (Date)doc.get("Next Appointment");
		__includedSteps = (ArrayList<Document>)doc.get("Included Steps");
		__completedSteps = (ArrayList<Document>)doc.get("Completed Steps");
		__upcomingSteps = (ArrayList<Document>)doc.get("Upcoming Steps");
		__skippedSteps = (ArrayList<Document>)doc.get("Skipped Steps");
		__replacedSteps = (ArrayList<Document>)doc.get("Replaced Steps");
		__addedSteps = (ArrayList<Document>)doc.get("Added Steps");
	}

	public void read (String id) {
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoCollection<Document> coll = client.getDatabase(DatabaseDefinitions.NOSQL_DB).getCollection(DatabaseDefinitions.SOLD_BUNDLES_COLL);
		FindIterable<Document> docs = coll.find(new Document(FieldDefinitions.UNIQUE_ID, id));
		Iterator<Document> iter = docs.iterator ();
		if (iter.hasNext()) {
			Document doc = iter.next();
			fromDocument (doc);
		}
		client.close();
	}
	
	public void write () {
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoCollection<Document> coll = client.getDatabase(DatabaseDefinitions.NOSQL_DB).getCollection(DatabaseDefinitions.SOLD_BUNDLES_COLL);
		Document doc = toDocument ();
		Document match = new Document (FieldDefinitions.UNIQUE_ID, __id);
		if (coll.count(match) > 0) {
			coll.findOneAndReplace(match, doc);
		} else {
			coll.insertOne(doc);
		}
		client.close();
	}
	
	public String getId () {
		return __id;
	}
	
	public void setId (String id) {
		__id = id;
	}
	
	public String getBuyerId () {
		return __buyerID;
	}
	
	public void setBuyerId (String buyerId) {
		__buyerID = buyerId;
	}
	
	public String getSellerId () {
		return __sellerID;
	}
	
	public void setSellerId (String id) {
		__sellerID = id;
	}
	
	public String getBundleId () {
		return __bundleID;
	}
	
	public void setBundleId (String id) {
		__bundleID = id;
		populateBundleInfo ();
	}
	
	private void populateBundleInfo () {
		BundleDoc bd = BundleDoc.readBundle(__bundleID);
		ArrayList<Document> included = bd.getAllInclusions();
		for (int i = 0; i < included.size(); i++) {
			Document d = included.get(i);
			Document toInclude = new Document (FieldDefinitions.STEP_NUMBER, d.getInteger(FieldDefinitions.STEP_NUMBER));
			toInclude.append(FieldDefinitions.PROCEDURE, d.getString(FieldDefinitions.PROCEDURE));
			toInclude.append(FieldDefinitions.CAN_EXIT, d.getBoolean(FieldDefinitions.CAN_EXIT));
//			toInclude.append(FieldDefinitions.EXIT_PENALTY, `d.getDouble(FieldDefinitions.EXIT_PENALTY));
			toInclude.append(FieldDefinitions.IS_CORE, d.getBoolean(FieldDefinitions.IS_CORE));
			toInclude.append(FieldDefinitions.PROCEDURE_STATUS, PROCSTAT.NOT_STARTED.toString());
			toInclude.append(FieldDefinitions.VET_NOTES, "");
			toInclude.append(FieldDefinitions.PET_NOTES, "");
			__includedSteps.add(toInclude);
		}
	}
	
	public double getSalePrice () {
		return __salePrice;
	}
	
	public void setSalePrice (double price) {
		__salePrice = price;
	}
	
	public Date getSaleTime () {
		return __timeOfSale;
	}
	
	public void setSaleTime (Date saleTime) {
		__timeOfSale = saleTime;
	}
	
	public Date getCommencementTime () {
		return __timeOfCommencement;
	}
	
	public void setCommencementTime (Date commencementTime) {
		__timeOfCommencement = commencementTime;
	}
	
	public int getCurrentStepNumber () {
		return __currentStepNumber;
	}
	
	public void setCurrentStepNumber (int n) {
		__currentStepNumber = n;
	}
	
	public String getCurrentStep () {
		return __currentStepID;
	}
	
	public void setCurrentStep (String step) {
		__currentStepID = step;
	}
	
	public Date getCurrentStepTime () {
		return __currentStepTime;
	}
	
	public void setCurrentStepTime (Date time) {
		__currentStepTime = time;
	}
}
