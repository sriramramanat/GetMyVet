package com.gmv.pre.structs;

import java.util.ArrayList;
import java.util.Iterator;

import org.bson.Document;

import com.gmv.pre.definitions.DatabaseDefinitions;
import com.gmv.pre.definitions.FieldDefinitions;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class BundleDoc {

	private String __id;
	private String __name;
	private String __description;
	private String __template;
	private ProcedureDoc __templateDoc;
	private String __providerID;
	private String __providerName;
	private String __providerStAddress;
	private String __providerCity;
	private String __providerState;
	private String __providerZip;
	private Document __providerLoc;
	private String __providerPhone;
	private Document __providerRank;
	private boolean __financingAvailable;
	private String __renderingStAddress;
	private String __renderingCity;
	private String __renderingState;
	private String __renderingZip;
	private Document __renderingLoc;
	int __numProviders;
	int __numOffices;
	private ArrayList<Document> __included;
	private double __basePrice;
	private double __transferPrice;
	private boolean __promoAvailable;
	private ArrayList<Document> __entryTime;
	private ArrayList<Document> __coreTime;
	private ArrayList<Document> __includedParts;
	int __variantLevel;
	
	private static String __dbName = "";
	private static String __collName = "";

	public static void initDBVariables (String dbName, String collName) {
		__dbName = dbName;
		__collName = collName;
	}

	public static BundleDoc createBundle (String bundleID, String provider, String procedure) {
		BundleDoc bd = new BundleDoc ();
		bd.__id = bundleID;
		// Fill provider details
		bd.fillProviderDetails (provider);
		
		// for now rendering address is the same as provider
		bd.copyProviderToRenderer ();

		// fill template details
		bd.fillProcedureDetails(procedure);

		// set other fields to their default values
		bd.__name = bundleID;
		bd.__description = bundleID;
		bd.__numProviders = 1;
		bd.__numOffices = 1;
		bd.__promoAvailable = false;
		bd.__basePrice = 100.0;
		bd.__transferPrice = 75.0;
		bd.__financingAvailable = false;
		bd.__variantLevel = 1;
		bd.addEntryTime ("Monday", false);
		bd.addEntryTime ("Tuesday", false);
		bd.addEntryTime ("Wednesday", false);
		bd.addEntryTime ("Thursday", false);
		bd.addEntryTime ("Friday", false);
		bd.addEntryTime ("Saturday", false);
		bd.addEntryTime ("Sunday", false);
		bd.addCoreTime ("Monday", false);
		bd.addCoreTime ("Tuesday", false);
		bd.addCoreTime ("Wednesday", false);
		bd.addCoreTime ("Thursday", false);
		bd.addCoreTime ("Friday", false);
		bd.addCoreTime ("Saturday", false);
		bd.addCoreTime ("Sunday", false);
		return bd;
	}
	
	private BundleDoc () {
	}
	
	public void fillProviderDetails (String providerID) {
		__providerID = providerID;
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoCollection<Document> coll = client.getDatabase(__dbName).getCollection(DatabaseDefinitions.PRAC_COLL);
		FindIterable<Document> practices = coll.find(new Document(FieldDefinitions.UNIQUE_ID, providerID));
		Iterator<Document> iter = practices.iterator ();
		if (iter.hasNext()) {
			Document practice = iter.next();
			__providerName = practice.getString("Name");
			__providerStAddress = practice.getString("Street");
			__providerCity = practice.getString("City");
			__providerState = practice.getString("State");
			__providerZip = practice.getString("Zip");
			__providerPhone = practice.getString("Phone");
			__providerLoc = (Document)(practice.get("loc"));
			__providerRank = (Document)(practice.get("Ranking"));
			__providerPhone = practice.getString("Phone");
		}
		client.close();
	}
	
	public void copyProviderToRenderer () {
		__renderingStAddress = __providerStAddress;
		__renderingCity = __providerCity;
		__renderingState = __providerState;
		__renderingZip = __providerZip;
		__renderingLoc = __providerLoc;
	}
	
	public void fillProcedureDetails (String procedureID) {
		__template = procedureID;
		__templateDoc = new ProcedureDoc (procedureID);
		flattenTemplate ();
	}
	
	public void flattenTemplate () {
		__included = new ArrayList<Document>();
		ArrayList<Document> includedProcs = __templateDoc.getIncludedProcedures();
		for (int i = 0; i < includedProcs.size(); i++) {
			Document incl = includedProcs.get(i);
			incl.append("Is Core", true);
			incl.append("Can Exit", false);
			incl.append("Exit Penalty", 0.0);
			incl.append("Entry Price", 0.0);
			__included.add(incl);
		}
		
		ArrayList<String> includedParts = __templateDoc.getIncludedParts();
		__includedParts = new ArrayList<Document>();
		for (int i = 0; i < includedParts.size(); i++) {
			Document includedPart = new Document ("Part ID", includedParts.get(i));
			includedPart.append("Is Core", true);
			__includedParts.add(includedPart);
		}
	}

	public Document toDocument () {
		Document doc = new Document (FieldDefinitions.UNIQUE_ID, __id);
		doc.append(FieldDefinitions.NAME, __name);
		doc.append(FieldDefinitions.DESCRIPTION, __description);
		doc.append(FieldDefinitions.TEMPLATE, __template);
		doc.append(FieldDefinitions.TEMPLATE_NAME, __templateDoc.getName());
		doc.append(FieldDefinitions.APPLICABILITY, __templateDoc.getApplicability());
		doc.append(FieldDefinitions.PROVIDER_ID, __providerID);
		doc.append(FieldDefinitions.PROVIDER_NAME, __providerName);
		doc.append(FieldDefinitions.PROVIDER_ADDRESS, __providerStAddress);
		doc.append(FieldDefinitions.PROVIDER_CITY, __providerCity);
		doc.append(FieldDefinitions.PROVIDER_STATE, __providerState);
		doc.append(FieldDefinitions.PROVIDER_ZIP, __providerZip);
		doc.append(FieldDefinitions.PHONE, __providerPhone);
		doc.append(FieldDefinitions.PROVIDER_LOC, __providerLoc);
		doc.append(FieldDefinitions.PROVIDER_RANK, __providerRank);
		doc.append(FieldDefinitions.RENDERING_ADDRESS, __renderingStAddress);
		doc.append(FieldDefinitions.RENDERING_CITY, __renderingCity);
		doc.append(FieldDefinitions.RENDERING_STATE, __renderingState);
		doc.append(FieldDefinitions.RENDERING_ZIP, __renderingZip);
		doc.append(FieldDefinitions.RENDERING_LOC, __renderingLoc);
		doc.append(FieldDefinitions.FINANCING_AVAILABLE, __financingAvailable);
		doc.append(FieldDefinitions.NUM_PROVIDERS, __numProviders);
		doc.append(FieldDefinitions.NUM_OFFICES, __numOffices);
		doc.append(FieldDefinitions.BASE_PRICE, __basePrice);
		doc.append(FieldDefinitions.TRANSFER_PRICE, __transferPrice);
		doc.append(FieldDefinitions.PROMO_AVAILABLE, __promoAvailable);
		doc.append(FieldDefinitions.VARIANT_LEVEL, __variantLevel);
		doc.append(FieldDefinitions.ENTRY_TIME, __entryTime);
		doc.append(FieldDefinitions.CORE_TIME, __coreTime);
		doc.append(FieldDefinitions.INCLUDED_PROC, __included);
		doc.append(FieldDefinitions.INCLUDED_PARTS, __includedParts);
		return doc;
	}
	
	public void fromDocument (Document doc) {
	}
	
	public String toJSON () {
		return toDocument().toJson();
	}

	public void read (String id) {
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
	
	public void write () {
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

	public ProcedureDoc getTemplate () {
		return __templateDoc;
	}

	public String getTemplateID () {
		return __template;
	}

	public void setTemplateID (String templateID) {
		__template = templateID;
		fillProcedureDetails (templateID);
	}

	public String getProviderID () {
		return __providerID;
	}

	public void setProviderID (String providerID) {
		__providerID = providerID;
		fillProviderDetails (providerID);
	}

	public String getProviderName () {
		return __providerName;
	}

	public void setProviderName (String providerName) {
		__providerName = providerName;
	}

	public String getProviderStreetAddress () {
		return __providerStAddress;
	}

	public void setProviderStreetAddress (String providerStreet) {
		__providerStAddress = providerStreet;
	}

	public String getProviderCity () {
		return __providerCity;
	}

	public void setProviderCity (String providerCity) {
		__providerCity = providerCity;
	}

	public String getProviderState () {
		return __providerState;
	}

	public void setProviderState (String providerState) {
		__providerState = providerState;
	}

	public String getProviderZip () {
		return __providerZip;
	}

	public void setProviderZip (String providerZip) {
		__providerZip = providerZip;
	}

	public String getProviderPhone () {
		return __providerPhone;
	}

	public void setProviderPhone (String providerPhone) {
		__providerPhone = providerPhone;
	}

	public Document getProviderLocation () {
		return __providerLoc;
	}

	public void setProviderLocation (Document loc) {
		__providerLoc = loc;
	}

	public Document getProviderRank () {
		return __providerRank;
	}

	public void setProviderRank (Document rank) {
		__providerRank = rank;
	}
	public String getRenderingStreetAddress () {
		return __renderingStAddress;
	}

	public void setRenderingStreetAddress (String renderingStreet) {
		__renderingStAddress = renderingStreet;
	}

	public String getRenderingCity () {
		return __renderingCity;
	}

	public void setRenderingCity (String renderingCity) {
		__renderingCity = renderingCity;
	}

	public String getRenderingState () {
		return __renderingState;
	}

	public void setRenderingState (String renderingState) {
		__renderingState = renderingState;
	}

	public String getRenderingZip () {
		return __renderingZip;
	}

	public void setRenderingZip (String renderingZip) {
		__renderingZip = renderingZip;
	}
	public boolean isFinancingAvailable () {
		return __financingAvailable;
	}

	public void setFinancingAvailable (boolean financingAvailable) {
		__financingAvailable = financingAvailable;
	}

	public double getBasePrice () {
		return __basePrice;
	}

	public void setBasePrice (double price) {
		__basePrice = price;
	}

	public double getTransferPrice () {
		return __transferPrice;
	}

	public void setTransferPrice (double price) {
		__transferPrice = price;
	}

	public boolean isPromoAvailable () {
		return __promoAvailable;
	}

	public void setPromoAvailable (boolean promoAvailable) {
		__promoAvailable = promoAvailable;
	}

	public int getNumProviders () {
		return __numProviders;
	}

	public void setNumProviders (int n) {
		__numProviders = n;
	}

	public int getNumOffices () {
		return __numOffices;
	}

	public void setNumOffices (int n) {
		__numOffices = n;
	}

	public int getVariantLevel () {
		return __variantLevel;
	}

	public void setVariantLevel (int level) {
		__variantLevel = level;
	}
	
	private void addInclusion (Document toInsert, int order) {
		int count = __included.size();
		if (order > count + 1) {
			//insert placeholders till count is reached
			for (int i = count; i < order - 1; i++) {
				Document placeholder = new Document ("Procedure", "GMV_PROC_PH");
				placeholder.append ("Mandatory", false);
				placeholder.append ("Referral Required", false);
				placeholder.append("Day", "TBD");
				placeholder.append("Step Number", i + 1);
				__included.add(placeholder);
			}
			toInsert.append("Step Number", order);
			__included.add(toInsert);
			return;
		}
		if (order == -1) {
			toInsert.append("Step Number", count + 1);
			__included.add(toInsert);
		} else if (order > 0 && order <= count){
			toInsert.append("Step Number", order);
			int nPos = order - 1;
			__included.add(nPos, toInsert);
			//insert is complete. bump step numbers of the rest
			for (int i = order; i < __included.size(); i++) {
				Document d = __included.get(i);
				int oldOrder = d.getInteger("Step Number", i);
				d.put("Step Number", oldOrder + 1);
				__included.set(i, d);
			}
		}
	}

	public boolean addInclusion (String procID, String day, int order, boolean canExit) {
		return addInclusion (procID, false, false, day, order, canExit);
	}
	
	public boolean addInclusion (String procID, 
								boolean mandatory, 
								boolean referralRquired,
								String day,
								int order,
								boolean canExit) {
		// find if this is a possible inclusion or is already included
		if (!__templateDoc.canIncludeProcedure(procID))
			return false;

		Document toInsert = new Document ("Procedure", procID);
		toInsert.append ("Mandatory", mandatory);
		toInsert.append ("Referral Required", referralRquired);
		toInsert.append("Day", day);
		toInsert.append("Can Exit", canExit);
		toInsert.append("Is Core", false);
		toInsert.append("Entry Price", 0.0);
		if (canExit) {
			toInsert.append("Exit Penalty", 0);
		} else {
			toInsert.append("Exit Penalty", 100);
		}
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoCollection<Document> coll = client.getDatabase(__dbName).getCollection(DatabaseDefinitions.PROCEDURE_COLL);
		Document toFind = new Document(FieldDefinitions.UNIQUE_ID, procID);
		FindIterable<Document> iterable = coll.find(toFind);
		Iterator<Document> iter = iterable.iterator ();
		
		if (iter.hasNext()) {
			// okay, the inclusion is already part of the system, let's add it
			ProcedureDoc procedureDoc = new ProcedureDoc();
			procedureDoc.fromDocument(iter.next()); // this is the procedure to be added.
			addInclusion (toInsert, order);
		}
		
		client.close();

		return true;
	}
	
	private void renumberSteps () {
		int stepNumber = 1;
		for (int i = 0; i < __included.size(); i++) {
			Document doc = __included.get(i);
			doc.put("Step Number", stepNumber);
			__included.set(i, doc);
			stepNumber++;
		}
	}
	
	public void removeInclusion (int stepNumber) {
		for (int i = 0 ; i < __included.size(); i++) {
			Document doc = __included.get(i);
			int sNumber = doc.getInteger("Step Number", 0);
			if (sNumber == stepNumber) {
				__included.remove(i);
				break;
			}
		}
		renumberSteps();
	}
	
	public void addPart (String partID) {
		if (__templateDoc.canIncludePart(partID)) {
			Document doc = new Document ("Part ID", partID);
			doc.append("Is Core", false);
			__includedParts.add(doc);
		}
	}
	
	public void addEntryTime (String day, boolean available) {
		if (__entryTime == null)
			__entryTime = new ArrayList<Document>();
		for (int i = 0; i < __entryTime.size(); i++) {
			Document doc = __entryTime.get(i);
			String d = doc.getString("Day");
			if (d.equals(day)) {
				doc.put("Available", available);
				__entryTime.set(i, doc);
				return;
			}
		}
		Document doc = new Document ("Day", day).append("Available", available);
		__entryTime.add(doc);
	}
	
	public void addCoreTime (String day, boolean available) {
		if (__coreTime == null)
			__coreTime = new ArrayList<Document>();
		for (int i = 0; i < __coreTime.size(); i++) {
			Document doc = __coreTime.get(i);
			String d = doc.getString("Day");
			if (d.equals(day)) {
				doc.put("Available", available);
				__coreTime.set(i, doc);
				return;
			}
		}
		Document doc = new Document ("Day", day).append("Available", available);
		__coreTime.add(doc);
	}
}
