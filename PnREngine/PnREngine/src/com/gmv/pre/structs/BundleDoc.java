package com.gmv.pre.structs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.bson.Document;

import com.gmv.pre.definitions.DatabaseDefinitions;
import com.gmv.pre.definitions.FieldDefinitions;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class BundleDoc {

	private String __id;
	private String __name;
	private String __description;
	private String __template;
	private ProcedureDoc __templateDoc;
	private String __providerID;
	private String __providerName;
	private Document __providerAddress;
	private Document __providerLoc;
	private String __providerPhone;
	private Document __providerRank;
	private boolean __financingAvailable;
	private Document __renderingAddress;
	private Document __renderingLoc;
	int __numProviders;
	int __numOffices;
	private ArrayList<Document> __included;
	private double __listPrice;
	private double __listDiscount;
	private double __basePrice; /* AKA GMV Price */
	private double __transferPrice;
	private double __cancellationPercent;
	private boolean __promoAvailable;
	private ArrayList<Document> __entryTime;
	private ArrayList<Document> __coreTime;
	private ArrayList<Document> __includedParts;
	int __variantLevel;
	private ArrayList<Document> __appointments;
	private int __numSold;
	
	public static BundleDoc readBundle (String bundleID) {
		BundleDoc bd = new BundleDoc ();
		bd.read(bundleID);
		return bd;
	}
	
	public static BundleDoc readTempBundle (String bundleID) {
		BundleDoc bd = new BundleDoc ();
		bd.readFromTemp(bundleID);
		return bd;
	}
	
	public static BundleDoc createBundle () {
		BundleDoc bd = new BundleDoc ();
		return bd;
	}
	
	public static BundleDoc createBundle (String bundleID, String provider, String procedure) {
		BundleDoc bd = new BundleDoc ();
		if (bundleID == null || bundleID.isEmpty()) {
			bundleID = provider + "_" + procedure;
		}
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
		bd.__listPrice = 100.0;
		bd.__listDiscount = 0.1;
		bd.__basePrice = bd.__listDiscount * bd.__listPrice;
		bd.__cancellationPercent = 0.1;
		bd.__transferPrice = 75.0;
		bd.__financingAvailable = false;
		bd.__variantLevel = bd.__templateDoc.getVariantLevel();
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
		// initialize arrays & objects
		__providerAddress = new Document ();
		__renderingAddress = new Document ();
		__included = new ArrayList<Document>();
		__appointments = new ArrayList<Document>();
		__entryTime = new ArrayList<Document>();;
		__coreTime = new ArrayList<Document>();;
		__includedParts = new ArrayList<Document>();
		__numSold = 0;
	}
	
	public void fillProviderDetails (String providerID) {
		__providerID = providerID;
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		if (client != null) {
			MongoDatabase db = client.getDatabase(DatabaseDefinitions.NOSQL_DB);
			if (db != null) {
				MongoCollection<Document> coll = db.getCollection(DatabaseDefinitions.PRAC_COLL);
				if (coll != null) {
					FindIterable<Document> practices = coll.find(new Document(FieldDefinitions.UNIQUE_ID, providerID));
					Iterator<Document> iter = practices.iterator ();
					if (iter.hasNext()) {
						Document practice = iter.next();
						__providerName = practice.getString("Name");
						__providerAddress = (Document)practice.get(FieldDefinitions.PRIMARY_ADDRESS);
						__providerPhone = practice.getString(FieldDefinitions.PHONE);
						__providerLoc = (Document)(practice.get(FieldDefinitions.LOCATION));
						__providerRank = (Document)(practice.get(FieldDefinitions.RANKING));
					} else {
						// invalidate provider id
						__providerID = "";
					}
				}
			}
			client.close();
		}
	}
	
	public void copyProviderToRenderer () {
		__renderingAddress = __providerAddress;
		__renderingLoc = __providerLoc;
	}
	
	public void fillProcedureDetails (String procedureID) {
		__template = procedureID;
		__templateDoc = new ProcedureDoc (procedureID);
		__name = __templateDoc.getName();
		flattenTemplate ();
	}
	
	public void flattenTemplate () {
		__included = new ArrayList<Document>();
		ArrayList<Document> includedProcs = __templateDoc.getIncludedProcedures();
		for (int i = 0; i < includedProcs.size(); i++) {
			Document incl = includedProcs.get(i);
			incl.append("Is Core", true);
			incl.append("Entry Price", 0.0);
			incl.append("Exit Refund", 0.0);
			incl.append("Payout", 0.0);
			incl.append("Exit Payout", 0.0);
			incl.append(FieldDefinitions.LIST_PRICE, 0.0);
			incl.append(FieldDefinitions.LIST_DISCOUNT, 0.0);
			incl.append(FieldDefinitions.UTILIZATION, 1);
			incl.append("GMV Price", 0.0);
			incl.append(FieldDefinitions.RENDERED_BY, __providerID);
			incl.append(FieldDefinitions.RENDERING_ADDRESS, __renderingAddress);
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
		doc.append(FieldDefinitions.TEMPLATE_FULL, __templateDoc.toDocument());
		doc.append(FieldDefinitions.TEMPLATE_NAME, __templateDoc.getName());
		doc.append(FieldDefinitions.APPLICABILITY, __templateDoc.getApplicability());
		doc.append(FieldDefinitions.CORE_PROC, __templateDoc.getCore());
		doc.append(FieldDefinitions.REASON, __templateDoc.getReason());
		doc.append(FieldDefinitions.PROVIDER_ID, __providerID);
		doc.append(FieldDefinitions.PROVIDER_NAME, __providerName);
		doc.append(FieldDefinitions.PROVIDER_ADDRESS, __providerAddress);
		doc.append(FieldDefinitions.PHONE, __providerPhone);
		doc.append(FieldDefinitions.PROVIDER_LOC, __providerLoc);
		doc.append(FieldDefinitions.PROVIDER_RANK, __providerRank);
		doc.append(FieldDefinitions.RENDERING_ADDRESS, __renderingAddress);
		doc.append(FieldDefinitions.RENDERING_LOC, __renderingLoc);
		doc.append(FieldDefinitions.FINANCING_AVAILABLE, __financingAvailable);
		doc.append(FieldDefinitions.NUM_PROVIDERS, __numProviders);
		doc.append(FieldDefinitions.NUM_OFFICES, __numOffices);
		doc.append(FieldDefinitions.LIST_PRICE, __listPrice);
		doc.append(FieldDefinitions.LIST_DISCOUNT, __listDiscount);
		doc.append(FieldDefinitions.BASE_PRICE, __basePrice);
		doc.append(FieldDefinitions.CANCEL_PENALTY, __cancellationPercent);
		doc.append(FieldDefinitions.TRANSFER_PRICE, __transferPrice);
		doc.append(FieldDefinitions.PROMO_AVAILABLE, __promoAvailable);
		doc.append(FieldDefinitions.VARIANT_LEVEL, __variantLevel);
		doc.append(FieldDefinitions.ENTRY_TIME, __entryTime);
		doc.append(FieldDefinitions.CORE_TIME, __coreTime);
		doc.append(FieldDefinitions.INCLUDED_PROC, __included);
		doc.append(FieldDefinitions.INCLUDED_PARTS, __includedParts);
		doc.append(FieldDefinitions.AVAILABLE_TIMES, __appointments);
		doc.append(FieldDefinitions.NUMBER_SOLD, __numSold);
		return doc;
	}
	
	public void fromDocument (Document doc) {
		__id = doc.getString(FieldDefinitions.UNIQUE_ID);
		__name = doc.getString(FieldDefinitions.NAME);
		__description = doc.getString(FieldDefinitions.DESCRIPTION);
		__template = doc.getString(FieldDefinitions.TEMPLATE);
		String templateID = doc.getString(FieldDefinitions.TEMPLATE_NAME);
		__templateDoc = new ProcedureDoc (templateID);
		__providerID = doc.getString(FieldDefinitions.PROVIDER_ID);
		__providerName = doc.getString(FieldDefinitions.PROVIDER_NAME);
		__providerAddress = (Document)(doc.get(FieldDefinitions.PROVIDER_ADDRESS));
		__providerPhone = doc.getString(FieldDefinitions.PHONE);
		__providerLoc = (Document)(doc.get(FieldDefinitions.PROVIDER_LOC));
		__providerRank = (Document)(doc.get(FieldDefinitions.PROVIDER_RANK));
		__renderingAddress = (Document)(doc.get(FieldDefinitions.RENDERING_ADDRESS));
		__renderingLoc = (Document)(doc.get(FieldDefinitions.RENDERING_LOC));
		__financingAvailable = doc.getBoolean(FieldDefinitions.FINANCING_AVAILABLE);
		__numProviders = doc.getInteger(FieldDefinitions.NUM_PROVIDERS);
		__numOffices = doc.getInteger(FieldDefinitions.NUM_OFFICES);
		__listPrice = doc.getDouble(FieldDefinitions.LIST_PRICE);
		__listDiscount = doc.getDouble(FieldDefinitions.LIST_DISCOUNT);
		__basePrice = doc.getDouble(FieldDefinitions.BASE_PRICE);
		__cancellationPercent = doc.getDouble(FieldDefinitions.CANCEL_PENALTY);
		__transferPrice = doc.getDouble(FieldDefinitions.TRANSFER_PRICE);
		__promoAvailable = doc.getBoolean(FieldDefinitions.PROMO_AVAILABLE);
		__variantLevel = doc.getInteger(FieldDefinitions.VARIANT_LEVEL);
		__entryTime = (ArrayList<Document>)(doc.get(FieldDefinitions.ENTRY_TIME));
		__coreTime = (ArrayList<Document>)(doc.get(FieldDefinitions.CORE_TIME));
		__included = (ArrayList<Document>)(doc.get(FieldDefinitions.INCLUDED_PROC));
		__includedParts = (ArrayList<Document>)(doc.get(FieldDefinitions.INCLUDED_PARTS));
		__appointments = (ArrayList<Document>)(doc.get(FieldDefinitions.AVAILABLE_TIMES));
		__numSold = doc.getInteger(FieldDefinitions.NUMBER_SOLD, 0);
	
	}
	
	public String toJSON () {
		return toDocument().toJson();
	}

	public void read (String id) {
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		if (client != null) {
			MongoDatabase db = client.getDatabase(DatabaseDefinitions.NOSQL_DB);
			if (db != null) {
				MongoCollection<Document> coll = db.getCollection(DatabaseDefinitions.BUNDLE_COLL);
				if (coll != null) {
					FindIterable<Document> docs = coll.find(new Document(FieldDefinitions.UNIQUE_ID, id));
					Iterator<Document> iter = docs.iterator ();
					if (iter.hasNext()) {
						Document doc = iter.next();
						fromDocument (doc);
					}
				}
			}
			client.close();
		}
	}
	
	public void readFromTemp (String id) {
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		if (client != null) {
			MongoDatabase db = client.getDatabase(DatabaseDefinitions.NOSQL_DB);
			if (db != null) {
				MongoCollection<Document> coll = db.getCollection(DatabaseDefinitions.BUNDLE_TEMP_COLL);
				if (coll != null) {
					FindIterable<Document> docs = coll.find(new Document(FieldDefinitions.UNIQUE_ID, id));
					Iterator<Document> iter = docs.iterator ();
					if (iter.hasNext()) {
						Document doc = iter.next();
						fromDocument (doc);
					}
				}
			}
			client.close();
		}
	}

	public void write () {
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		if (client != null) {
			MongoDatabase db = client.getDatabase(DatabaseDefinitions.NOSQL_DB);
			if (db != null) {
				MongoCollection<Document> coll = db.getCollection(DatabaseDefinitions.BUNDLE_COLL);
				if (coll != null) {
					Document doc = toDocument ();
					Document match = new Document (FieldDefinitions.UNIQUE_ID, __id);
					if (coll.count(match) > 0) {
						coll.findOneAndReplace(match, doc);
					} else {
						coll.insertOne(doc);
					}
				}
			}
			client.close();
		}
	}
	
	public void writeToTemp () {
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		if (client != null) {
			MongoDatabase db = client.getDatabase(DatabaseDefinitions.NOSQL_DB);
			if (db != null) {
				MongoCollection<Document> coll = db.getCollection(DatabaseDefinitions.BUNDLE_TEMP_COLL);
				if (coll != null) {
					Document doc = toDocument ();
					Document match = new Document (FieldDefinitions.UNIQUE_ID, __id);
					if (coll.count(match) > 0) {
						coll.findOneAndReplace(match, doc);
					} else {
						coll.insertOne(doc);
					}
				}
			}
			client.close();
		}
	}
	
	public void update (Document updateDoc) {
		String str = updateDoc.getString("TotalPrice");
		double price = Double.parseDouble(str);
		setBasePrice(price);
		
		str = updateDoc.getString("Discount");
		double discount = Double.parseDouble(str);
		setListDiscount(discount);
		
		ArrayList<String> renderedBy = (ArrayList<String>)(updateDoc.get("RenderedBy"));
		ArrayList<String> renderedAt = (ArrayList<String>)(updateDoc.get("RenderedAt"));
		ArrayList<String> listPrice = (ArrayList<String>)(updateDoc.get("ListPrice"));
		ArrayList<String> gmvPrice = (ArrayList<String>)(updateDoc.get("GMVPrice"));
		ArrayList<String> utilization = (ArrayList<String>)(updateDoc.get("Utilization"));
		
		int count = renderedBy.size();
		for (int i = 0; i < count; i++) {
			String partner = renderedBy.get(i);
			String office = renderedAt.get(i);
			if (partner.equals("self")) {
				if (!office.equals("default")) {
					setOffice(i+1, office);
				}
			} else {
				setRenderer (i+1, partner, office);
			}
			
			Document incl = __included.get(i);
			str = listPrice.get(i);
			incl.put(FieldDefinitions.LIST_PRICE, Double.parseDouble(str));
			incl.put(FieldDefinitions.LIST_DISCOUNT, discount);
			str = utilization.get(i);
			incl.put(FieldDefinitions.UTILIZATION, Double.parseDouble(str)/100);
			str = gmvPrice.get(i);
			incl.put(FieldDefinitions.GMV_PRICE, Double.parseDouble(str));
		}
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
	
	public int getNumberSold () {
		return __numSold;
	}
	
	public void setNumberSold (int numSold) {
		__numSold = numSold;
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
		return __providerAddress.getString(FieldDefinitions.STREET1);
	}

	public void setProviderStreetAddress (String providerStreet) {
		__providerAddress.put(FieldDefinitions.STREET1, providerStreet);
	}

	public String getProviderStreetAddress2 () {
		return __providerAddress.getString(FieldDefinitions.STREET2);
	}

	public void setProviderStreetAddress2 (String providerStreet) {
		__providerAddress.put(FieldDefinitions.STREET2, providerStreet);
	}

	public String getProviderCity () {
		return __providerAddress.getString(FieldDefinitions.CITY);
	}

	public void setProviderCity (String providerCity) {
		__providerAddress.put(FieldDefinitions.CITY, providerCity);
	}

	public String getProviderState () {
		return __providerAddress.getString(FieldDefinitions.STATE);
	}

	public void setProviderState (String providerState) {
		__providerAddress.put(FieldDefinitions.STATE, providerState);
	}

	public String getProviderZip () {
		return __providerAddress.getString(FieldDefinitions.ZIP);
	}

	public void setProviderZip (String providerZip) {
		__providerAddress.put(FieldDefinitions.ZIP, providerZip);
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
		return __renderingAddress.getString(FieldDefinitions.STREET1);
	}

	public void setRenderingStreetAddress (String renderingStreet) {
		__renderingAddress.put(FieldDefinitions.STREET1, renderingStreet);
	}

	public String getRenderingStreetAddress2 () {
		return __renderingAddress.getString(FieldDefinitions.STREET2);
	}

	public void setRenderingStreetAddress2 (String renderingStreet) {
		__renderingAddress.put(FieldDefinitions.STREET2, renderingStreet);
	}

	public String getRenderingCity () {
		return __renderingAddress.getString(FieldDefinitions.CITY);
	}

	public void setRenderingCity (String renderingCity) {
		__renderingAddress.put(FieldDefinitions.CITY, renderingCity);
	}

	public String getRenderingState () {
		return __renderingAddress.getString(FieldDefinitions.STATE);
	}

	public void setRenderingState (String renderingState) {
		__renderingAddress.put(FieldDefinitions.STATE, renderingState);
	}

	public String getRenderingZip () {
		return __renderingAddress.getString(FieldDefinitions.ZIP);
	}

	public void setRenderingZip (String renderingZip) {
		__renderingAddress.put(FieldDefinitions.ZIP, renderingZip);
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

	public double getListPrice () {
		return __listPrice;
	}

	public void setListPrice (double price) {
		__listPrice = price;
	}

	public double getListDiscount () {
		return __listDiscount;
	}

	public void setListDiscount (double discount) {
		__listDiscount = discount;
	}

	public double getTransferPrice () {
		return __transferPrice;
	}

	public void setTransferPrice (double price) {
		__transferPrice = price;
	}
	
	public double getCancellationPercent () {
		return __cancellationPercent;
	}
	
	public void setCancellationPercent (double percent) {
		__cancellationPercent = percent;
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
				placeholder.append(FieldDefinitions.STEP_NUMBER, i + 1);
				__included.add(placeholder);
			}
			toInsert.append(FieldDefinitions.STEP_NUMBER, order);
			__included.add(toInsert);
			return;
		}
		if (order == -1) {
			toInsert.append(FieldDefinitions.STEP_NUMBER, count + 1);
			__included.add(toInsert);
		} else if (order > 0 && order <= count){
			toInsert.append(FieldDefinitions.STEP_NUMBER, order);
			int nPos = order - 1;
			__included.add(nPos, toInsert);
			//insert is complete. bump step numbers of the rest
			for (int i = order; i < __included.size(); i++) {
				Document d = __included.get(i);
				int oldOrder = d.getInteger(FieldDefinitions.STEP_NUMBER, i);
				d.put(FieldDefinitions.STEP_NUMBER, oldOrder + 1);
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
		toInsert.append(FieldDefinitions.LIST_PRICE, 0.0);
		toInsert.append(FieldDefinitions.LIST_DISCOUNT, 0.0);
		toInsert.append(FieldDefinitions.UTILIZATION, 1);
		toInsert.append(FieldDefinitions.GMV_PRICE, 0.0);
		toInsert.append(FieldDefinitions.RENDERED_BY, __providerID);
		toInsert.append(FieldDefinitions.RENDERING_ADDRESS, __renderingAddress);

		if (!canExit) {
			toInsert.append("Exit Refund", 0);
		} else {
			toInsert.append("Exit Refund", 100);
		}
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoDatabase db = client.getDatabase(DatabaseDefinitions.NOSQL_DB);
		if (db != null) {
			MongoCollection<Document> coll = db.getCollection(DatabaseDefinitions.PROCEDURE_COLL);
			if (coll != null) {
				Document toFind = new Document(FieldDefinitions.UNIQUE_ID, procID);
				FindIterable<Document> iterable = coll.find(toFind);
				Iterator<Document> iter = iterable.iterator ();
				
				if (iter.hasNext()) {
					// okay, the inclusion is already part of the system, let's add it
					ProcedureDoc procedureDoc = new ProcedureDoc();
					procedureDoc.fromDocument(iter.next()); // this is the procedure to be added.
					addInclusion (toInsert, order);
				}
			}
		}
		
		client.close();

		return true;
	}
	
	private void renumberSteps () {
		int stepNumber = 1;
		for (int i = 0; i < __included.size(); i++) {
			Document doc = __included.get(i);
			doc.put(FieldDefinitions.STEP_NUMBER, stepNumber);
			__included.set(i, doc);
			stepNumber++;
		}
	}
	
	public void removeInclusion (int stepNumber) {
		for (int i = 0 ; i < __included.size(); i++) {
			Document doc = __included.get(i);
			int sNumber = doc.getInteger(FieldDefinitions.STEP_NUMBER, 0);
			if (sNumber == stepNumber) {
				__included.remove(i);
				break;
			}
		}
		renumberSteps();
	}
	
	public ArrayList<Document> getAllInclusions () {
		return __included;
	}
	
	public int getIncludedCount() {
		return __included.size();
	}
	
	public int getCoreCount () {
		int nCores = 0;
		for (int i = 0 ; i < __included.size(); i++) {
			Document doc = __included.get(i);
			if (doc.getBoolean ("Is Core")) {
				nCores++;
			}
		}
		return nCores;
	}
	
	public void setRenderer (int stepNumber, String providerID, String addressID) {
		// find the step
		Document procedure = null;
		int index = -1;
		for (int i = 0 ; i < __included.size(); i++) {
			procedure = __included.get(i);
			int sNumber = procedure.getInteger(FieldDefinitions.STEP_NUMBER, 0);
			if (sNumber == stepNumber) {
				index = i;
				break;
			}
		}
		
		if (procedure == null || index == -1) // nothing to do here
			return;
		
		PracticeDoc pd = new PracticeDoc ();
		pd.read(providerID);
		
		procedure.append(FieldDefinitions.RENDERED_BY, providerID);
		procedure.append(FieldDefinitions.RENDERING_ADDRESS, pd.getAddressById(addressID));
		__included.set(index, procedure);
	}
	
	public void setOffice (int stepNumber, String addressID) {
		// find the step
		Document procedure = null;
		int index = -1;
		for (int i = 0 ; i < __included.size(); i++) {
			procedure = __included.get(i);
			int sNumber = procedure.getInteger(FieldDefinitions.STEP_NUMBER, 0);
			if (sNumber == stepNumber) {
				index = i;
				break;
			}
		}
		
		if (procedure == null || index == -1) // nothing to do here
			return;
		
		PracticeDoc pd = new PracticeDoc ();
		pd.read(__providerID);
		
		procedure.append(FieldDefinitions.RENDERING_ADDRESS, pd.getAddressById(addressID));
		__included.set(index, procedure);
		
	}
	
	public void setOffice (int stepNumber, Document address) {
		// find the step
		Document procedure = null;
		int index = -1;
		for (int i = 0 ; i < __included.size(); i++) {
			procedure = __included.get(i);
			int sNumber = procedure.getInteger(FieldDefinitions.STEP_NUMBER, 0);
			if (sNumber == stepNumber) {
				index = i;
				break;
			}
		}
		
		if (procedure == null || index == -1) // nothing to do here
			return;
		
		procedure.append(FieldDefinitions.RENDERING_ADDRESS, address);
		__included.set(index, procedure);
		
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
	
	public void addAppointmentSlot (Date date, boolean available, double promoPrice) {
		Document doc = new Document ("Time", date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if (hour < 12) {
			doc.append(FieldDefinitions.TIME_OF_DAY, FieldDefinitions.MORNING);
		} else if (hour > 12 && hour < 15) {
			doc.append(FieldDefinitions.TIME_OF_DAY, FieldDefinitions.AFTERNOON);
		} else {
			doc.append(FieldDefinitions.TIME_OF_DAY, FieldDefinitions.EVENING);
		}
		doc.append("Available", available);
		if (promoPrice > 0) {
			doc.append("Price", promoPrice);
		} else {
			doc.append("Price", __basePrice);
		}
		__appointments.add(doc);
	}
	
	public void setPromoPrice (Date date, double promoPrice) {
		for (int i = 0; i < __appointments.size(); i++) {
			Document doc = __appointments.get(i);
			if (doc.get("Time").equals(date)) {
				doc.put("Price", promoPrice);
				__appointments.set(i, doc);
				return;
			}
		}
	}

	public void setAvailability (Date date, boolean availability) {
		for (int i = 0; i < __appointments.size(); i++) {
			Document doc = __appointments.get(i);
			if (doc.get("Time").equals(date)) {
				doc.put("Available", availability);
				__appointments.set(i, doc);
				return;
			}
		}
	}
	
	public BundleInstanceDoc buy (String buyerID) {
		BundleInstanceDoc bid = new BundleInstanceDoc ();
		return bid;
	}
	
	public static void populateCollection2 () {
		String[] templateIDs = {"GMV_PROC_AC_101",
				"GMV_PROC_AC_102",
				"GMV_PROC_AC_103",
				"GMV_PROC_AC_104",
				"GMV_PROC_AL_201",
				"GMV_PROC_AL_202",
				"GMV_PROC_AL_203",
				"GMV_PROC_AI_301",
				"GMV_PROC_AI_302",
				"GMV_PROC_AI_303",
				"GMV_PROC_AI_304",
				"GMV_PROC_AI_305",
				"GMV_PROC_AI_306",
				"GMV_PROC_AS_501",
				"GMV_PROC_AS_502",
				"GMV_PROC_AS_503",
				"GMV_PROC_AS_504",
				"GMV_PROC_AS_505",
				"GMV_PROC_AS_506",
				"GMV_PROC_AS_507",
				"GMV_PROC_AS_508",
				"GMV_PROC_AS_509",
				"GMV_PROC_AR_601",
				"GMV_PROC_AR_602",
				"GMV_PROC_AR_603",
				"GMV_PROC_AR_604",
				"GMV_PROC_AR_605",
				"GMV_PROC_AH_701",
				"GMV_PROC_AV_801",
				"GMV_PROC_AV_802",
				"GMV_PROC_AV_803",
				"GMV_PROC_AV_805",
				"GMV_PROC_AV_806",
				"GMV_PROC_AV_807",
				"GMV_PROC_AV_808",
				"GMV_PROC_AV_809",
				"GMV_PROC_AV_810",
				"GMV_PROC_AV_811",
				"GMV_PROC_AV_812",
				"GMV_PROC_AV_813",
				"GMV_PROC_AV_814",
				"GMV_PROC_BS_101",
				"GMV_PROC_BS_102",
				"GMV_PROC_BS_103",
				"GMV_PROC_BS_104",
				"GMV_PROC_BS_105",
				"GMV_PROC_BS_106",
				"GMV_PROC_BS_107",
				"GMV_PROC_BS_108",
				"GMV_PROC_BS_109",
				"GMV_PROC_BS_110",
				"GMV_PROC_BS_111",
				"GMV_PROC_BS_112",
				"GMV_PROC_BS_113",
				"GMV_PROC_BS_114",
				"GMV_PROC_BS_115",
				"GMV_PROC_BS_116",
				"GMV_PROC_BS_117",
				"GMV_PROC_BS_118",
				"GMV_PROC_BS_119",
				"GMV_PROC_BS_120",
				"GMV_PROC_BS_121",
				"GMV_PROC_BS_122",
				"GMV_PROC_BS_123",
				"GMV_PROC_BS_124",
				"GMV_PROC_BS_125",
				"GMV_PROC_BS_126",
				"GMV_PROC_BS_127",
				"GMV_PROC_BW_101",
				"GMV_PROC_BW_102",
			};
		for (int i = 0; i <= 200; i++) {
			String pracID = "GMV_PRACTICE_" + i;
			String officeID = "GMV_PRACTICE_" + i + "_office_1";
			int partnerIdx = i + 1;
//			String partnerID = "GMV_PRACTICE_" + partnerIdx;
//			String partnerOffice = "GMV_PRACTICE_" + partnerIdx + "_office_2";
			for (int j = 0; j < 10; j++) {
				int templateIdx = (int)(Math.random() * 70);
				String bundleID = pracID + templateIDs[templateIdx];
				BundleDoc bd = BundleDoc.createBundle(bundleID, pracID, templateIDs[templateIdx]);
				if (bd.__providerID.isEmpty())
					continue;

				double random = Math.random();
				double promoPrice = 4500 * random;
				bd.setBasePrice(5000 * random);
				bd.setTransferPrice(4000 * random);
				bd.addEntryTime("Monday", true);
				bd.addEntryTime("Tuesday", true);
				bd.addEntryTime("Wednesday", true);
				bd.addEntryTime("Thursday", true);
				bd.addEntryTime("Friday", true);
				bd.addEntryTime("Saturday", true);
				bd.addCoreTime("Monday", true);
				bd.addCoreTime("Wednesday", true);
				bd.setPromoAvailable(false);
				bd.setCancellationPercent(0.2);
				bd.setFinancingAvailable(false);
				bd.setNumberSold((int)Math.floor(random * 100));
				bd.write();
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoCollection<Document> coll = client.getDatabase(DatabaseDefinitions.NOSQL_DB).getCollection(DatabaseDefinitions.BUNDLE_COLL);
        coll.createIndex(new BasicDBObject (FieldDefinitions.RENDERING_LOC, "2dsphere"));
        client.close();
	}
	
	public static void populateCollection () {
		for (int i = 0; i <= 100; i = i + 1) {
			String pracID = "GMV_PRACTICE_" + i;
			String officeID = "GMV_PRACTICE_" + i + "_office_1";
			int prev = i - 1;
			String partnerID = "GMV_PRACTICE_" + prev;
			String partnerOffice = "GMV_PRACTICE_" + prev + "_office_2";
			String bundleID = pracID + "TPLO_Surgery_Bundle";
			BundleDoc bd = BundleDoc.createBundle(bundleID + "_b", pracID, "GMV_PROC_BS_101");
			if (bd.__providerID.isEmpty())
				continue;
			bd.setName("TPLO Bronze");
			double random = Math.random();
			double basePrice = 5000 * random;
			double promoPrice = 4500 * random;
			bd.setBasePrice(basePrice);
			bd.setTransferPrice(4000 * random);
			if (i > 1) {
				bd.setRenderer(3, partnerID, partnerOffice);
				bd.setNumOffices(2);
				bd.setNumProviders(2);
			}
			bd.setOffice(7, officeID);
			bd.addInclusion("GMV_PROC_A_300", false, true, "x + 70", -1, true);
			bd.addEntryTime("Monday", true);
			bd.addEntryTime("Tuesday", true);
			bd.addEntryTime("Wednesday", true);
			bd.addEntryTime("Thursday", true);
			bd.addEntryTime("Friday", true);
			bd.addEntryTime("Saturday", true);
			bd.addCoreTime("Monday", true);
			bd.addCoreTime("Wednesday", true);
			bd.setPromoAvailable(false);
			bd.setCancellationPercent(0.2);
			bd.setFinancingAvailable(false);
			bd.setNumberSold((int)Math.floor(random * 100));
			Calendar cal = Calendar.getInstance();
			cal.set(2017, 0, 12, 9, 0);
			Date date = cal.getTime();
			bd.addAppointmentSlot(date, true, basePrice);

			cal.add(Calendar.HOUR_OF_DAY, 1);
			date = cal.getTime();
			bd.addAppointmentSlot(date, true, basePrice);
			
			cal.add(Calendar.HOUR_OF_DAY, 1);
			date = cal.getTime();
			bd.addAppointmentSlot(date, true, basePrice);

			cal.add(Calendar.DAY_OF_MONTH, 1);
			date = cal.getTime();
			bd.addAppointmentSlot(date, true, basePrice);

			cal.add(Calendar.HOUR_OF_DAY, 1);
			date = cal.getTime();
			bd.addAppointmentSlot(date, true, basePrice);

			cal.add(Calendar.DAY_OF_MONTH, 1);
			date = cal.getTime();
			bd.addAppointmentSlot(date, true, promoPrice);
			bd.write();
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			bd = BundleDoc.createBundle(bundleID + "_s", pracID, "GMV_PROC_BW_101");
			bd.setVariantLevel(2);
			basePrice = 6000 * random;
			promoPrice = 5500 * random;
			bd.setBasePrice(6000 * random);
			bd.setTransferPrice(5000 * random);
			bd.setName("TPLO Silver");
			bd.addInclusion("GMV_PROC_A_300", false, true, "x + 70", -1, true);
			bd.addInclusion("GMV_PROC_A_300", false, true, "x + 70", -1, true);
			bd.addEntryTime("Monday", true);
			bd.addEntryTime("Tuesday", true);
			bd.addEntryTime("Wednesday", true);
			bd.addEntryTime("Thursday", true);
			bd.addEntryTime("Friday", true);
			bd.addEntryTime("Saturday", true);
			bd.addCoreTime("Monday", true);
			bd.addCoreTime("Wednesday", true);
			bd.setPromoAvailable(true);
			bd.setCancellationPercent(0.1);
			bd.setFinancingAvailable(true);
			bd.setNumberSold((int)Math.floor(random * 72));
			bd.addAppointmentSlot(date, true, basePrice);

			cal.add(Calendar.HOUR_OF_DAY, 1);
			date = cal.getTime();
			bd.addAppointmentSlot(date, true, basePrice);
			
			cal.add(Calendar.HOUR_OF_DAY, 1);
			date = cal.getTime();
			bd.addAppointmentSlot(date, true, basePrice);

			cal.add(Calendar.DAY_OF_MONTH, 1);
			date = cal.getTime();
			bd.addAppointmentSlot(date, true, promoPrice);

			cal.add(Calendar.HOUR_OF_DAY, 1);
			date = cal.getTime();
			bd.addAppointmentSlot(date, true, basePrice);

			cal.add(Calendar.DAY_OF_MONTH, 1);
			date = cal.getTime();
			bd.addAppointmentSlot(date, true, promoPrice);

			bd.write();
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			bd = BundleDoc.createBundle(bundleID + "_g", pracID, "GMV_PROC_BS_119");
			bd.setName("TPLO Gold");
			basePrice = 7000 * random;
			promoPrice = 6500 * random;
			bd.setBasePrice(7000 * random);
			bd.setTransferPrice(6000 * random);
			bd.addInclusion("GMV_PROC_A_300", false, true, "x + 70", -1, true);
			bd.addInclusion("GMV_PROC_A_300", false, true, "x + 70", -1, true);
			bd.addInclusion("GMV_PROC_A_300", false, true, "x + 70", -1, true);
			bd.addInclusion("GMV_PROC_A_300", false, true, "x + 70", -1, true);
			bd.addEntryTime("Monday", true);
			bd.addEntryTime("Tuesday", true);
			bd.addEntryTime("Wednesday", true);
			bd.addEntryTime("Thursday", true);
			bd.addEntryTime("Friday", true);
			bd.addEntryTime("Saturday", true);
			bd.addCoreTime("Monday", true);
			bd.addCoreTime("Wednesday", true);
			bd.addCoreTime("Friday", true);
			bd.setPromoAvailable(true);
			bd.setCancellationPercent(0.05);
			bd.setFinancingAvailable(true);
			bd.setNumberSold((int)Math.floor(random * 131));
			bd.addAppointmentSlot(date, true, basePrice);

			cal.add(Calendar.HOUR_OF_DAY, 1);
			date = cal.getTime();
			bd.addAppointmentSlot(date, true, basePrice);
			
			cal.add(Calendar.HOUR_OF_DAY, 1);
			date = cal.getTime();
			bd.addAppointmentSlot(date, true, basePrice);

			cal.add(Calendar.DAY_OF_MONTH, 1);
			date = cal.getTime();
			bd.addAppointmentSlot(date, true, promoPrice);

			cal.add(Calendar.HOUR_OF_DAY, 1);
			date = cal.getTime();
			bd.addAppointmentSlot(date, true, basePrice);

			cal.add(Calendar.DAY_OF_MONTH, 1);
			date = cal.getTime();
			bd.addAppointmentSlot(date, true, promoPrice);
			bd.setVariantLevel(3);
			bd.write();
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
