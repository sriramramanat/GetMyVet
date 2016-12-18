package com.gmv.pre.structs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.bson.Document;

import com.gmv.pre.definitions.DatabaseDefinitions;
import com.gmv.pre.definitions.FieldDefinitions;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class OfferingDoc {
	private static String __dbName = "";
	private static String __collName = "";

	private double __basePrice;
	private boolean __promoAvailable;
	private double __transferPrice;
	private double __cancelPercent;
	private ProcedureDoc __template;
	private String __providerID;
	private String __providerName;
	private Document __providerRank;
	private Document __providerLocation;
	private String __id;
	private String __name;
	private String __description;
	private ArrayList<Document> __additions;
	private int __offices;
	private int __providers;
	private boolean __financingOption;
	private ArrayList<Document> __availableTimes;
	
	public static void initDBVariables (String dbName, String collName) {
		__dbName = dbName;
		__collName = collName;
	}

	public OfferingDoc () {
		initAndSetDefaults ();
	}
	
	public void fillProviderDetails () {
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoCollection<Document> coll = client.getDatabase(__dbName).getCollection(DatabaseDefinitions.PRAC_COLL);
		FindIterable<Document> docs = coll.find(new Document(FieldDefinitions.UNIQUE_ID, __providerID));
		Iterator<Document> iter = docs.iterator ();
		if (iter.hasNext()) {
			Document doc = iter.next();
			__providerID = doc.getString(FieldDefinitions.UNIQUE_ID);
			__providerName = doc.getString(FieldDefinitions.NAME);
			__providerLocation = (Document)(doc.get("loc"));
			__providerRank = (Document)(doc.get(FieldDefinitions.RANKING));
		}
		client.close();
	}
	
	public OfferingDoc (String templateID, String id, String providerID) {
		initAndSetDefaults ();
		__template = new ProcedureDoc (templateID);
		__id = id;
		__providerID = providerID;
		fillProviderDetails ();
	}
	
	protected void initAndSetDefaults () {
		__id = "";
		__name = "";
		__description = "";
		__providerID = "";
		__providerName = "";
		__providerLocation = new Document ();
		__providerRank = new Document ();
		__basePrice = 0;
		__promoAvailable = false;
		__transferPrice = 0;
		__cancelPercent = 0;
		__offices = 1;
		__providers = 1;
		__additions = new ArrayList<Document>();
		__financingOption = false;
		__availableTimes = new ArrayList<Document> ();
		__template = new ProcedureDoc();
	}

	public Document toDocument () {
		Document doc = new Document ();
		doc.append(FieldDefinitions.NAME, __name);
		doc.append(FieldDefinitions.DESCRIPTION, __description);
		doc.append(FieldDefinitions.TEMPLATE, __template.toDocument());
		doc.append(FieldDefinitions.BASE_PRICE, __basePrice);
		doc.append(FieldDefinitions.PROMO_AVAILABLE, __promoAvailable);
		doc.append(FieldDefinitions.TRANSFER_PRICE, __transferPrice);
		doc.append(FieldDefinitions.ADDITIONS, __additions);
		doc.append(FieldDefinitions.CANCEL_PENALTY, __cancelPercent);
		doc.append(FieldDefinitions.NUM_OFFICES, __offices);
		doc.append(FieldDefinitions.NUM_PROVIDERS, __providers);
		doc.append(FieldDefinitions.PROVIDER_ID, __providerID);
		doc.append(FieldDefinitions.PROVIDER_NAME, __providerName);
		doc.append(FieldDefinitions.PROVIDER_LOC, __providerLocation);
		doc.append(FieldDefinitions.RANKING, __providerRank);
		doc.append(FieldDefinitions.FINANCING_AVAILABLE, __financingOption);
		Document bundleInfo = new Document ("Bundle Info", doc);
		bundleInfo.append(FieldDefinitions.UNIQUE_ID, __id);
		bundleInfo.append(FieldDefinitions.AVAILABLE_TIMES, __availableTimes);
		return bundleInfo;
	}
	
	public void fromDocument (Document bundleInfo) {
		Document doc = (Document)bundleInfo.get("Bundle Info");
		__id = bundleInfo.getString(FieldDefinitions.UNIQUE_ID);
		Document template = (Document)doc.get(FieldDefinitions.TEMPLATE);
		__name = doc.getString(FieldDefinitions.NAME);
		__description = doc.getString(FieldDefinitions.DESCRIPTION);
		__template.fromDocument(template);
		__basePrice = doc.getDouble(FieldDefinitions.BASE_PRICE);
		__promoAvailable = doc.getBoolean (FieldDefinitions.PROMO_AVAILABLE);
		__transferPrice = doc.getDouble(FieldDefinitions.TRANSFER_PRICE);
		__additions = (ArrayList<Document>) (doc.get(FieldDefinitions.ADDITIONS));
		__cancelPercent = doc.getDouble(FieldDefinitions.CANCEL_PENALTY);
		__offices = doc.getInteger(FieldDefinitions.NUM_OFFICES, 1);
		__providers = doc.getInteger(FieldDefinitions.NUM_PROVIDERS, 1);
		__providerID = doc.getString(FieldDefinitions.PROVIDER_ID);
		__providerRank = (Document)doc.get(FieldDefinitions.RANKING);
		__financingOption = doc.getBoolean(FieldDefinitions.FINANCING_AVAILABLE, false);
		__availableTimes = (ArrayList<Document>)bundleInfo.get(FieldDefinitions.AVAILABLE_TIMES);
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

	public double getBasePrice () {
		return __basePrice;
	}
	
	public void setBasePrice (double basePrice) {
		__basePrice = basePrice;
	}
	
	public boolean isPromoAvailable () {
		return __promoAvailable;
	}
	
	public void setPromoAvailable (boolean promoAvailable) {
		__promoAvailable = promoAvailable;
	}
	
	public double getTransferPrice () {
		return __transferPrice;
	}
	
	public void setTransferPrice (double transferPrice) {
		__transferPrice = transferPrice;
	}
	
	public void setPartner (String procedureID, String partnerID) {
		ArrayList<Document> included = __template.getIncludedProcedures();
		for (int i = 0; i < included.size(); i ++) {
			Document d = included.get(i);
			String dProcID = d.getString("Procedure");
			if (dProcID.equals(procedureID)) {
				d.append("Partner", partnerID);
				included.set(i, d);
				__providers++;
			}
		}
	}
	
	public void setOffice (String procedureID, String officeID) {
		ArrayList<Document> included = __template.getIncludedProcedures();
		for (int i = 0; i < included.size(); i ++) {
			Document d = included.get(i);
			String dProcID = d.getString("Procedure");
			if (dProcID.equals(procedureID)) {
				d.append("Office", officeID);
				included.set(i, d);
				__offices++;
			}
		}
	}
	
	public void setMultiplicity (String procedureID, int n) {
		__template.setMultiplicity(procedureID, n);
	}
	

	public void addAddons (String procedureID, int multiplicity) {
		if (!__template.canInclude(procedureID)) {
			System.out.println (procedureID + " cannot be included");
			return; //this cannot be included
		}
		Document doc = new Document ("Procedure", procedureID);
		doc.append("Multiplicity", multiplicity);
		__additions.add(doc);
	}
	
	public void setExitPenalty (int stepNumber, double refund) {
		if (__template != null) {
			__template.setExitPenalty (stepNumber, refund);
		}
	}
	
	public double getCancellationPenalty () {
		return __cancelPercent;
	}
	
	public void setCancellationPenalty (double cancel) {
		__cancelPercent = cancel;
	}
	
	public boolean isFinancingAvailable () {
		return __financingOption;
	}
	
	public void setFinancingAvailable (boolean financing) {
		__financingOption = financing;
	}
	
	public void addTimeslot (Date date, double price) {
		Document doc = new Document ("Time", date);
		doc.append("Time (Pretty)", date.toString());
		doc.append("Promo Price", price);
		doc.append("Available", true);
		for (int i = 0; i < __availableTimes.size(); i++) {
			Document at = __availableTimes.get(i);
			if (date.equals(at.get("Time"))) {
				// this date is already present. just update it
				__availableTimes.set(i, doc);
				return;
			}
		}
		__availableTimes.add(doc);
	}
	
	public void bookTime (Date date, boolean isAvailable) {
		for (int i = 0; i < __availableTimes.size(); i++) {
			Document at = __availableTimes.get(i);
			if (date.equals(at.get("Time"))) {
				// this date is already present. just update it
				at.put("Available", isAvailable);
				__availableTimes.set(i, at);
				return;
			}
		}
	}
	
	public void helperAddTimeslotsForMonth (int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2016, month, 1);
		for (int i = 0; i < 30; i ++) {
			calendar.set(Calendar.HOUR_OF_DAY, 9);
			for (int j = 0; j < 9; j++) {
				Date day = calendar.getTime();
				if (calendar.get(Calendar.DAY_OF_WEEK) > 5)
					addTimeslot(day, __basePrice * 0.7);
				else
					addTimeslot (day, __basePrice);
				calendar.add(Calendar.HOUR_OF_DAY, 1);
			}
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
	}
	
	public void setTemplate (ProcedureDoc pd) {
		__template = pd;
	}
	
	public void setProviderDetails (String provID, String provName, Document loc, Document rank) {
		__providerID = provID;
		__providerName = provName;
		__providerLocation = loc;
		__providerRank = rank;
	}
	
	public void addTimeSlots (ArrayList<Document> timeSlots) {
		__availableTimes = timeSlots;
	}
	
	public static void createAndAddOfferings () {
		
		if (__dbName == "" || __collName == "")
			return;
		
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoCollection<Document> coll = client.getDatabase(__dbName).getCollection(__collName);

		// add 3 offering for every provider
		for (int i = 1; i < 2000 ; i= i + 5) {
			double randomNum = Math.random();

			String provider = "GMV_PRACTICE_" + i;
			String offeringID = provider + "-" + "TPLO_Bronze";
			/* od1 - bronze or standard version 1 extra office and 1 extra provider */
			OfferingDoc od1 = new OfferingDoc ("GMV_PROC_B_102", offeringID, provider);
			od1.setBasePrice(5000 * randomNum);
			od1.setPromoAvailable(false);
			od1.setName(offeringID);
			if (randomNum > 0.5) {
				od1.setFinancingAvailable(true);
			}
			od1.setTransferPrice(2500);
			od1.setPartner("GMV_PROC_A_300", "GMV_PRACT");
			od1.setOffice("GMV_PROC_A_203", "Main Street Lab Services");
			od1.setCancellationPenalty(10);
			od1.setExitPenalty(1, 5);
			od1.setExitPenalty(5, 20);
			od1.setExitPenalty(6, 70);
			od1.setExitPenalty(9, 80);
			od1.setExitPenalty(12, 95);
			od1.setExitPenalty(13, 100);
			od1.setDescription("bronze or standard version 1 extra office and 1 extra provider");
			od1.helperAddTimeslotsForMonth(Calendar.DECEMBER);
			coll.insertOne(od1.toDocument());;

			offeringID = provider + "-" + "TPLO_Silver"; 
			/* od1 - silver - slight upgrade - single office and 1 extra provider */
			OfferingDoc od2 = new OfferingDoc ("GMV_PROC_B_102", offeringID, provider);
			od2.setName(offeringID);
			od2.setBasePrice(6000 * randomNum);
			od2.setPromoAvailable(true);
			od2.setTransferPrice(2500);
			if (randomNum > 0.5) {
				od2.setFinancingAvailable(true);
			}
			od2.setPartner("GMV_PROC_A_300", "Physio Therapist");
			od2.addAddons("GMV_PROC_A_300", 4);
			od2.setCancellationPenalty(5);
			od2.setExitPenalty(1, 5);
			od2.setExitPenalty(5, 20);
			od2.setExitPenalty(6, 70);
			od2.setExitPenalty(9, 80);
			od2.setExitPenalty(12, 95);
			od2.setExitPenalty(13, 100);
			od2.setDescription("od1 - silver - slight upgrade - single office and 1 extra provider");
			od2.helperAddTimeslotsForMonth(Calendar.DECEMBER);
			coll.insertOne(od2.toDocument());;

			offeringID = provider + "-" + "TPLO_Gold"; 
			OfferingDoc od3 = new OfferingDoc ("GMV_PROC_B_102", offeringID, provider);
			od3.setName(offeringID);
			od3.setBasePrice(7000 * randomNum);
			od3.setPromoAvailable(true);
			od3.setTransferPrice(2500);
			od3.addAddons("GMV_PROC_A_300", 8);
			od3.setCancellationPenalty(0);
			od3.setExitPenalty(1, 5);
			od3.setExitPenalty(5, 20);
			od3.setExitPenalty(6, 70);
			od3.setExitPenalty(9, 80);
			od3.setExitPenalty(12, 95);
			od3.setExitPenalty(13, 100);
			od3.setDescription("od1 - gold - single office, single provider, best value");
			if (randomNum > 0.5) {
				od1.setFinancingAvailable(true);
			}
			od3.helperAddTimeslotsForMonth(Calendar.DECEMBER);
			coll.insertOne(od3.toDocument());;
		}
		String providerLoc = "Bundle Info." + FieldDefinitions.PROVIDER_LOC;
        coll.createIndex(new BasicDBObject (providerLoc, "2dsphere"));
        client.close();
	}
}
