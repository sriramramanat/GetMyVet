package com.gmv.pre.structs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.bson.Document;

import com.gmv.pre.definitions.DatabaseDefinitions;
import com.gmv.pre.definitions.FieldDefinitions;
import com.gmv.pre.definitions.RoleDefinitions;
import com.mongodb.BasicDBList;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * @author sriramr
 *
 */

public class ProcedureDoc {
	private static String __dbName = "";
	private static String __collName = "";
	
	protected String __id = null;
	private String __name = null;
	private String __type = null;
	private String __category = null;
	private String __description = null;
	private String __core = null;
	private int __variantLevel = 0;
	private boolean __variantsAvailable = false;
	private String __reason = null;
	private String __whoCanDo = null;
	private double __procTime;
	private double __resTime;
	private double __regionalPrice;
	private ArrayList<Document> __altCode;
	private ArrayList<Document> __appliesTo;
	private ArrayList<Document> __variants;
	protected ArrayList<Document> __included;
	private ArrayList<String> __includedParts;
	private ArrayList<String> __possibleInclusions;
	private ArrayList<String> __possibleUpgradeParts;
	
	public static void initDBVariables (String dbName, String collName) {
		__dbName = dbName;
		__collName = collName;
	}
	
	public ProcedureDoc () {
		initAndSetDefaults ();
	}
	
	public ProcedureDoc (String id) {
		initAndSetDefaults ();
		addApplicability ("all", "any", 0, 1000, 0, 1000);
		__id = id;
		read (id);
	}
	
	protected void initAndSetDefaults () {
		// init arrays
		__altCode = new ArrayList<Document>();
		__appliesTo = new ArrayList<Document>();
		__included = new ArrayList<Document>();
		__variants = new ArrayList<Document>();
		__possibleInclusions = new ArrayList<String>();
		__includedParts = new ArrayList<String>();
		__possibleUpgradeParts = new ArrayList<String>();
		
		// set defaults
		__id = "";
		__name = "";
		__type = "";
		__category = "";
		__description = "";
		__core = __id;
		__variantLevel = 0;
		__reason = "Well";
		__whoCanDo = RoleDefinitions.ROLE_001;
		__procTime = 0;
		__resTime = 0;
		__regionalPrice = 0;
	}

	public Document toDocument () {
		Document doc = new Document (FieldDefinitions.UNIQUE_ID, __id);
		doc.append(FieldDefinitions.NAME, __name);
		doc.append(FieldDefinitions.TYPE, __type);
		doc.append(FieldDefinitions.CATEGORY, __category);
		doc.append(FieldDefinitions.DESCRIPTION, __description);
		if (__core == null || __core.isEmpty()) {__core = __id;}
		doc.append(FieldDefinitions.VARIANT_LEVEL, __variantLevel);
		doc.append(FieldDefinitions.VARIANTS_AVAILABLE, __variantsAvailable);
		doc.append(FieldDefinitions.VARIANTS, __variants);
		doc.append(FieldDefinitions.REASON, __reason);
		doc.append(FieldDefinitions.WHO_CAN_DO, __whoCanDo);
		doc.append(FieldDefinitions.PROC_TIME, __procTime);
		doc.append(FieldDefinitions.RES_TIME, __resTime);
		doc.append(FieldDefinitions.CORE_PROC, __core);
		doc.append(FieldDefinitions.REGIONAL_PRICE, __regionalPrice);
		doc.append(FieldDefinitions.ALT_CODE, __altCode);
		doc.append(FieldDefinitions.APPLICABILITY, __appliesTo);
		doc.append(FieldDefinitions.INCLUDED_PROC, __included);
		doc.append(FieldDefinitions.INCLUSION_COUNT, __included.size());
		doc.append(FieldDefinitions.INCLUDED_PARTS, __includedParts);
		doc.append(FieldDefinitions.POSSIBLE_ADDITIONS, __possibleInclusions);
		doc.append(FieldDefinitions.POSSIBLE_PART_UPGRADES, __possibleUpgradeParts);
		return doc;
	}
	
	public Document toExtendedDocument () {
		Document doc = new Document (FieldDefinitions.UNIQUE_ID, __id);
		doc.append(FieldDefinitions.NAME, __name);
		doc.append(FieldDefinitions.TYPE, __type);
		doc.append(FieldDefinitions.CATEGORY, __category);
		doc.append(FieldDefinitions.DESCRIPTION, __description);
		if (__core == null || __core.isEmpty()) {__core = __id;}
		doc.append(FieldDefinitions.VARIANT_LEVEL, __variantLevel);
		doc.append(FieldDefinitions.VARIANTS_AVAILABLE, __variantsAvailable);
		doc.append(FieldDefinitions.VARIANTS, __variants);
		doc.append(FieldDefinitions.REASON, __reason);
		doc.append(FieldDefinitions.WHO_CAN_DO, __whoCanDo);
		doc.append(FieldDefinitions.PROC_TIME, __procTime);
		doc.append(FieldDefinitions.RES_TIME, __resTime);
		doc.append(FieldDefinitions.CORE_PROC, __core);
		doc.append(FieldDefinitions.REGIONAL_PRICE, __regionalPrice);
		doc.append(FieldDefinitions.ALT_CODE, __altCode);
		doc.append(FieldDefinitions.APPLICABILITY, __appliesTo);
		ArrayList<Document> tempList = new ArrayList<Document>();
		for (int i = 0; i < __included.size(); i ++) {
			Document d = __included.get(i);
			ProcedureDoc pd = new ProcedureDoc(d.getString("Procedure"));
			tempList.add(pd.toDocument());
		}

		doc.append(FieldDefinitions.INCLUDED_PROC, tempList);
		doc.append(FieldDefinitions.INCLUDED_PARTS, __includedParts);
		doc.append(FieldDefinitions.POSSIBLE_ADDITIONS, __possibleInclusions);
		doc.append(FieldDefinitions.POSSIBLE_PART_UPGRADES, __possibleUpgradeParts);
		return doc;
	}
	
	public void fromDocument (Document doc) {
		__id = doc.getString(FieldDefinitions.UNIQUE_ID);
		__name = doc.getString(FieldDefinitions.NAME);
		__type = doc.getString(FieldDefinitions.TYPE);
		__category = doc.getString(FieldDefinitions.CATEGORY);
		__description = doc.getString(FieldDefinitions.DESCRIPTION);
		__variantLevel = doc.getInteger(FieldDefinitions.VARIANT_LEVEL, 0);
		__variantsAvailable = doc.getBoolean (FieldDefinitions.VARIANTS_AVAILABLE);
		__variants = (ArrayList<Document>) (doc.get(FieldDefinitions.VARIANTS));
		__reason = doc.getString(FieldDefinitions.REASON);
		__whoCanDo = doc.getString(FieldDefinitions.WHO_CAN_DO);
		__procTime = doc.getDouble(FieldDefinitions.PROC_TIME);
		__resTime = doc.getDouble(FieldDefinitions.RES_TIME);
		__core = doc.getString(FieldDefinitions.CORE_PROC);
		__regionalPrice = doc.getDouble (FieldDefinitions.REGIONAL_PRICE);
		if (__core == null || __core.isEmpty()) {__core = __id;}
		__altCode = (ArrayList<Document>) (doc.get(FieldDefinitions.ALT_CODE));
		__appliesTo = (ArrayList<Document>) (doc.get(FieldDefinitions.APPLICABILITY));
		__included = (ArrayList<Document>) (doc.get(FieldDefinitions.INCLUDED_PROC));
		__includedParts = (ArrayList<String>) (doc.get(FieldDefinitions.INCLUDED_PARTS));
		__possibleInclusions = (ArrayList<String>)(doc.get(FieldDefinitions.POSSIBLE_ADDITIONS));
		__possibleUpgradeParts = (ArrayList<String>)(doc.get(FieldDefinitions.POSSIBLE_PART_UPGRADES));
	}
	
	public String toJSON () {
		return toDocument().toJson();
	}

	public void read (String id) {
		if (__dbName == "" || __collName == "")
			return;
		
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoDatabase db = client.getDatabase(__dbName);
		if (db != null) {
			MongoCollection<Document> coll = db.getCollection(__collName);
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
	
	public void write () {
		if (__dbName == "" || __collName == "")
			return;
		
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoDatabase db = client.getDatabase(__dbName);
		if (db != null) {
			MongoCollection<Document> coll = db.getCollection(__collName);
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
	
	public String getAllByName (String name) {
		String toReturn = "";
		if (__dbName == "" || __collName == "")
			return toReturn;
		
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoDatabase db = client.getDatabase(__dbName);
		if (db != null) {
			MongoCollection<Document> coll = db.getCollection(__collName);
			if (coll != null) {
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
			}
		}
		client.close();
		return toReturn;
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
	
	public boolean getVariantsAvailable () {
		return __variantsAvailable;
	}
	
	public void setVariantsAvailable (boolean available) {
		__variantsAvailable = available;
	}
	
	public void addVariant (int level, String id) {
		Document d = new Document ("Level", level);
		d.append("Id", id);
		__variants.add(d);
	}
	
	public void removeVariant (int level, String id) {
		Document d = new Document ("Level", level);
		d.append("Id", id);
		__variants.remove(d);
	}
	
	public ArrayList<Document> getVariants () {
		return __variants;
	}
	
	public String getReason () {
		return __reason;
	}
	
	public void setReason (String reason) {
		__reason = reason;
	}
	
	public String getWhoCanDo () {
		return __whoCanDo;
	}
	
	public void setWhoCanDo (String whoCanDo) {
		__whoCanDo = whoCanDo;
	}
	
	public double getProcedureTime () {
		return __procTime;
	}
	
	public void setProcedureTime (double procTime) {
		__procTime = procTime;
	}
	
	public double getResultsTime () {
		return __resTime;
	}
	
	public void setResultsTime (double resTime) {
		__resTime = resTime;
	}
	
	public double getRegionalPrice () {
		return __regionalPrice;
	}
	
	public void setRegionalPrice (double price) {
		__regionalPrice = price;
	}

	public String getCore () {
		return __core;
	}
	
	public void setCore (String core) {
		__core = core;
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
	
	public void addApplicability (String breed, String gender, int minAge, int maxAge, double minWeight, double maxWeight) {
		String lowercase_breed = breed.toLowerCase();
		Document allDoc = new Document ("Type", "all");
		allDoc.append("Breed", "any");
		allDoc.append("Size", "any");
		allDoc.append("Category", "any");
		allDoc.append("Gender", "any");
		allDoc.append("Min age", minAge);
		allDoc.append("Max age", maxAge);
		allDoc.append("Min Weight(lbs)", 0);
		allDoc.append("Max Weight(lbs)", 1000);

		if (lowercase_breed.equals("all")) {
			__appliesTo.clear();
			__appliesTo.add(allDoc);
			return;
		} 
		
		if (lowercase_breed.contains("dog")) {
			allDoc.put("Type", "dog");
		} else if (lowercase_breed.contains("cat")) {
			allDoc.put("Type", "dog");
		}
		
		if (lowercase_breed.contains("large")) {
			allDoc.put("Size", "large");
		} else if (lowercase_breed.contains("medium")) {
			allDoc.put("Size", "medium");
		} else if (lowercase_breed.contains("small")) {
			allDoc.put("Size", "small");
		}
		
		if (lowercase_breed.contains("female")) {
			allDoc.put("Gender", "female");
		} else if (lowercase_breed.contains("male")) {
			allDoc.put("Gender", "male");
		} else if (!gender.isEmpty()) {
			allDoc.put("Gender", gender.toLowerCase());
		}
		
		if (minAge > 0) {
			allDoc.put("Min age", minAge);
		}
		
		if (maxAge > 0) {
			allDoc.put("Max age", maxAge);
		}
		
		if (minWeight > 0) {
			allDoc.put("Min Weight(lbs)", minWeight);
		}
		
		if (maxWeight > 0) {
			allDoc.put("Max Weight(lbs)", maxWeight);
		}
	
		//let's see if the user provided any known breed
		StringTokenizer st = new StringTokenizer(breed);
		while (st.hasMoreTokens())
		{
			String br = st.nextToken();
			if (BreedDoc.doesExist(br)) {
				//breed exists. Add it
				Document breedDoc = BreedDoc.getByName(br);
				String type = breedDoc.getString(FieldDefinitions.TYPE);
				if (type.contains("Canine")) {
					allDoc.put("Type", "dog");
				} else if (type.contains("Feline")) {
					allDoc.put("Type", "cat");
				}
				allDoc.put("Breed", breedDoc.getString(FieldDefinitions.BREED));
				allDoc.put("Category", breedDoc.getString(FieldDefinitions.CATEGORY));
				allDoc.put("Size", breedDoc.getString(FieldDefinitions.SIZE_CAT));
			}
		}
		__appliesTo.add(allDoc);
	}
	
	public ArrayList<Document> getApplicability () {
		return __appliesTo;
	}
	
	public void removeApplicability (String breed) {
		// TODO
		// 1. See if the input string is a container such as 'all dogs', 'all males' etc.
		// 2. See if the breed exists in the breed table.
		__appliesTo.remove(breed);
	}
	
	public int getNumInclusions () {
		return __included.size();
	}
	
	public int getNumOptional () {
		int count = 0;
		for (Document d : __included) {
			if (!d.getBoolean("Mandatory", false)) {
				count++;
			}
		}
		return count;
	}
	
	private void addInclusion (Document toInsert, int order) {
		int count = __included.size();
		if (order > count + 1) {
			//insert placeholders till count is reached
			for (int i = count; i < order - 1; i++) {
				Document placeholder = new Document ("Procedure", "GMV_PROC_PH");
				placeholder.append ("Name", "Placeholder");
				placeholder.append ("Mandatory", false);
				placeholder.append ("Referral Required", false);
				placeholder.append ("Day", "TBD");
				placeholder.append ("Price %", 0.0);
				placeholder.append ("Step Number", i + 1);
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

	public void addInclusion (String procID, String day, int order, boolean canExit) {
		addInclusion (procID, false, false, day, order, canExit);
	}
	
	public void addInclusion (String procID, 
								boolean mandatory, 
								boolean referralRquired,
								String day,
								int order,
								boolean canExit) {
		if (__dbName == "" || __collName == "")
			return;
		
		Document toInsert = new Document ("Procedure", procID);
		toInsert.append ("Mandatory", mandatory);
		toInsert.append ("Referral Required", referralRquired);
		toInsert.append("Day", day);
		toInsert.append("Can Exit", canExit);
		toInsert.append ("Price %", 100);
//		if (canExit) {
//			toInsert.append("Exit Refund", 0);
//		} else {
//			toInsert.append("Exit Refund", 100.0);
//		}
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoDatabase db = client.getDatabase(__dbName);
		MongoCollection<Document> coll = db.getCollection(__collName);
		Document toFind = new Document(FieldDefinitions.UNIQUE_ID, procID);
		FindIterable<Document> iterable = coll.find(toFind);
		Iterator<Document> iter = iterable.iterator ();
		
		Document allDoc = new Document ("Type", "all");
		allDoc.append("Breed", "any");
		allDoc.append("Size", "any");
		allDoc.append("Category", "any");
		allDoc.append("Gender", "any");
		allDoc.append("Min age", 0);
		allDoc.append("Max age", 100);
		allDoc.append("Min Weight(lbs)", 0);
		allDoc.append("Max Weight(lbs)", 1000);

		if (iter.hasNext()) {
			// okay, the inclusion is already part of the system, let's add it
			ProcedureDoc procedureDoc = new ProcedureDoc();
			procedureDoc.fromDocument(iter.next()); // this is the procedure to be added.
			toInsert.append("Name", procedureDoc.__name);
			if (procedureDoc.__appliesTo.contains(allDoc) || __appliesTo.contains(allDoc)) {
				//no problem. add it.
				addInclusion (toInsert, order);
			} else {
				// iterate through the procedure to be included
				for (int i = 0; i < procedureDoc.__appliesTo.size(); i++) {
					Document appliesTo = procedureDoc.__appliesTo.get(i);
					if (__appliesTo.contains(appliesTo)) {
						addInclusion (toInsert, order);
						break;
					}
				}
			}
		}

		client.close();
	}
	
	public void setPercentOfTotalPrice (int stepNumber, double percent) {
		for (int i = 0; i < __included.size(); i++) {
			Document d = __included.get(i);
			if (d.getInteger("Step Number") == stepNumber) {
				d.put("Price %", percent);
				__included.set(i, d);
				break;
			}
		}
	}

//	public void setExitRefund (int stepNumber, double refund) {
//		for (int i = 0; i < __included.size(); i++) {
//			Document d = __included.get(i);
//			if (d.getInteger("Step Number") == stepNumber) {
//				d.put("Can Exit", true);
//				d.put("Exit Refund", refund);
//				__included.set(i, d);
//				break;
//			}
//		}
//	}
	
	public void setReferralRequirement (String procedureID, boolean referral) {
		for (int i = 0; i < __included.size(); i ++) {
			Document d = __included.get(i);
			String dProcID = d.getString("Procedure");
			if (dProcID.equals(procedureID)) {
				d.append("Referral Required", referral);
				__included.set(i, d);
			}
		}
	}
	
	public void setMandatory (String procedureID, boolean mandatory) {
		for (int i = 0; i < __included.size(); i ++) {
			Document d = __included.get(i);
			String dProcID = d.getString("Procedure");
			if (dProcID.equals(procedureID)) {
				d.append("Mandatory", mandatory);
				__included.set(i, d);
			}
		}
	}

	public void setTimeTillNext (String procedureID, double timeTillNext) {
/*		for (int i = 0; i < __included.size(); i ++) {
			Document d = __included.get(i);
			String dProcID = d.getString("Procedure");
			if (dProcID.equals(procedureID)) {
				d.append("Time till Next", timeTillNext);
				__included.set(i, d);
			}
		}
*/	}
	
	public void setMultiplicity (String procedureID, int n) {
		for (int i = 0; i < __included.size(); i ++) {
			Document d = __included.get(i);
			String dProcID = d.getString("Procedure");
			if (dProcID.equals(procedureID)) {
				d.append("Multiplicity", n);
				__included.set(i, d);
			}
		}
	}

	public ArrayList<Document> getIncludedProcedures () {
		return __included;
	}
	
	public ArrayList<String> getIncludedParts () {
		return __includedParts;
	}
	
	public void removeInclusion (String procID) {
		Document toDelete = new Document ("Procedure", procID);
		__included.remove(toDelete);
	}

	public void addPossibleInclusion (String procThatCanBeIncluded) {
		if (!__possibleInclusions.contains(procThatCanBeIncluded))
			__possibleInclusions.add(procThatCanBeIncluded);
	}
	
	public void removePossibleInclusion (String procThatCanBeIncluded) {
		__possibleInclusions.remove(procThatCanBeIncluded);
	}
	
	public boolean canInclude (String procID) {
		return __possibleInclusions.contains(procID);
	}
	
	public void addPart (String partID) {
		__includedParts.add(partID);
	}
	
	public void removePart (String partID) {
		__includedParts.remove(partID);
	}
	
	public void addPartUpgrade (String partID) {
		__possibleUpgradeParts.add(partID);
	}
	
	public void removePartUpgrade (String partID) {
		__possibleUpgradeParts.remove(partID);
	}
	
	public boolean canIncludeProcedure (String procID) {
		for (int i = 0; i < __included.size(); i++) {
			Document d = __included.get(i);
			String incl = d.getString("Procedure");
			if (incl.equals(procID)) {
				return true;
			}
		}
		if (__possibleInclusions.contains(procID))
			return true;

		return false;
	}
	
	public boolean canIncludePart (String partID) {
		if (__includedParts.contains(partID) || __possibleUpgradeParts.contains(partID))
			return true;
		
		return false;
	}
	
	public static Document getCount () {
		long all=0, atomic=0, bundles=0, sick=0, well=0, dogs=0, cats=0; 
		Document doc = new Document ();
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoDatabase db = client.getDatabase(__dbName);
		if (db != null) {
			MongoCollection<Document> coll = db.getCollection(__collName);
			if (coll != null) {
				all = coll.count();
				atomic = coll.count(new Document(FieldDefinitions.INCLUSION_COUNT, 0));
				bundles = all - atomic;
				sick = coll.count(new Document(FieldDefinitions.REASON, "Sick"));
				well = all - sick;
				ArrayList<String> forDogs = new ArrayList<String>();
				forDogs.add("all");
				forDogs.add("any");
				forDogs.add("dog");
				dogs = coll.count(new Document("Applies To.Type", new Document("$in", forDogs)));
				ArrayList<String> forCats = new ArrayList<String>();
				forCats.add("all");
				forCats.add("any");
				forCats.add("cat");
				cats = coll.count(new Document("Applies To.Type", new Document("$in", forCats)));
			}
		}
		doc.append("Total", (int)all);
		doc.append("Atomic", (int)atomic);
		doc.append("Bundles", (int)bundles);
		doc.append("Sickness", (int)sick);
		doc.append("Wellness", (int)well);
		doc.append("Dogs", (int)dogs);
		doc.append("Cats", (int)cats);
		client.close();
		return doc;
	}
	
	public static ArrayList<Document> getTopSelling (int limit) {
		ArrayList<Document> ret = new ArrayList<Document> ();
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoDatabase db = client.getDatabase(__dbName);
		MongoCollection<Document> coll = db.getCollection(DatabaseDefinitions.BUNDLE_COLL);
		if (coll != null) {
			AggregateIterable<Document> output = coll.aggregate(Arrays.asList(
					new Document("$group", new Document ("_id", "$Template Name").append("numSold", new Document("$sum", "$Number Sold")).
																				  append("avgPrice", new Document("$avg", "$Base Price"))),
					new Document("$sort", new Document ("numSold", 1))
					)
					);
			int count = 1;
			for (Document d : output) {
				ret.add(d);
				if (count++ > limit) {break;}
			}
		}
		client.close();
		return ret;
	}
	
	public static double getAveragePriceForRank (String templateID, double anchorRank) {
		double ret = 0;
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoDatabase db = client.getDatabase(__dbName);
		MongoCollection<Document> coll = db.getCollection(DatabaseDefinitions.BUNDLE_COLL);
		if (coll != null) {
			Document match = new Document ("Template", templateID);
			if (anchorRank != 0) {
				match.append("Provider Rank.Composite Ranking", new Document ("$gte", anchorRank - 0.5)
																			.append("$lte", anchorRank + 0.5));
			}
			AggregateIterable<Document> out = coll.aggregate(Arrays.asList(
							new Document("$match", match),
							new Document("$group", new Document ("_id", "$Template Name").append("numSold", new Document("$sum", "$Number Sold")).
									  append("avgPrice", new Document("$avg", "$Base Price")))
							)
					);
			for (Document d : out) {
				ret = d.getDouble("avgPrice");
			}
		}
		client.close();
		return ret;
	}
	
	public static double getAveragePriceForTopSelling (String templateID, int topWhat) {
		double ret = 0;
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoDatabase db = client.getDatabase(__dbName);
		MongoCollection<Document> coll = db.getCollection(DatabaseDefinitions.BUNDLE_COLL);
		if (coll != null) {
			Document match = new Document ("Template", templateID);
			AggregateIterable<Document> out = coll.aggregate(Arrays.asList(
							new Document("$match", match),
							new Document("$sort", new Document("Number Sold", -1)),
							new Document("$limit", topWhat),
							new Document("$group", new Document ("_id", "$Template Name").append("numSold", new Document("$sum", "$Number Sold")).
									  append("avgPrice", new Document("$avg", "$Base Price")))
							)
					);
			for (Document d : out) {
				ret = d.getDouble("avgPrice");
			}
		}
		client.close();
		return ret;
	}
	
	public static String getVariant (String coreID, int variantLevel) {
		String ret = "";
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoDatabase db = client.getDatabase(__dbName);
		MongoCollection<Document> coll = db.getCollection(DatabaseDefinitions.PROCEDURE_COLL);
		if (coll != null) {
			Document match = new Document ("Core", coreID);
			match.append("Variants Available", true);
			match.append("Variant Level", variantLevel);
			FindIterable<Document> out = coll.find(match);
			Iterator<Document> iter = out.iterator();
			while (iter.hasNext()) {
				Document d = iter.next();
				ret = d.getString("UniqueID");
			}
		}
		client.close();
		return ret;
	}
	
	/**
	 * @param templateID - the template ID
	 * @param lat - latitude
	 * @param lon - longitude
	 * @param dist - max distance
	 * @return - Nearby average by state (if the 2dsphere happens to span state boundaries)
	 * Example return - [{ "_id" : { "Template" : "Puppy Wellness Bundle", "State" : "DC" }, "numSold" : 1310, "avgPrice" : 2209.78596537435 }]
	 */
	public static ArrayList<Document> getNearbyAverage (String templateID, double lat, double lon, double dist) {
		ArrayList<Document> ret = new ArrayList<Document>();
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoDatabase db = client.getDatabase(__dbName);
		MongoCollection<Document> coll = db.getCollection(DatabaseDefinitions.BUNDLE_COLL);
		if (coll != null) {
			BasicDBList dblList = new BasicDBList ();
			dblList.add(lon);
			dblList.add(lat);
			Document geom = new Document ("type", "Point");
			geom.append("coordinates", dblList);
			Document near = new Document ("near", geom);
			near.append("distanceField", "dist");
			near.append("maxDistance", dist);
			near.append("includeLocs", "Rendering Location");
			near.append("spherical", true);
			
			Document match = new Document ("Template", templateID);

//					System.out.println(new Document("$geoNear", near).toJson());
//					System.out.println (new Document("$match", match).toJson());
//					System.out.println (new Document("$sort", new Document("Number Sold", 1)).toJson());
//					System.out.println (new Document("$group", new Document ("_id", new Document("Template", "$Template Name").append("State", "$Rendering Address.State")).append("numSold", new Document("$sum", "$Number Sold")).
//							  append("avgPrice", new Document("$avg", "$Base Price"))).toJson()
//					);
			AggregateIterable<Document> out = coll.aggregate(Arrays.asList(
							new Document("$geoNear", near),
							new Document("$match", match),
							new Document("$sort", new Document("Number Sold", 1)),
							new Document("$group", new Document ("_id", new Document("Template", "$Template Name").append("State", "$Rendering Address.State")).append("numSold", new Document("$sum", "$Number Sold")).
									  append("avgPrice", new Document("$avg", "$Base Price")))
							)
					);
			for (Document d : out) {
				ret.add(d);
			}
		}
		client.close();
		return ret;
	}

	public static ArrayList<String> getAllProcedureIDs () {
		ArrayList<String> allProcedures = new ArrayList<String>();
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoDatabase db = client.getDatabase(__dbName);
		MongoCollection<Document> coll = db.getCollection(__collName);
		if (coll != null) {
			FindIterable<Document> all = coll.find();
			Iterator<Document> iter = all.iterator();
			while (iter.hasNext()) {
				Document proc = iter.next();
				String id = proc.getString(FieldDefinitions.UNIQUE_ID);
				allProcedures.add(id);
			}
		}
		
		client.close();
		return allProcedures;
	}
	
	public static Map<String, String> getBundlesForSuperset (Map<String, String> superSet) {
		HashMap<String, String> retVal = new HashMap<String, String>();
		Set<String> keys = superSet.keySet();
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoDatabase db = client.getDatabase(__dbName);
		MongoCollection<Document> coll = db.getCollection(__collName);
		if (coll != null) {
			FindIterable<Document> all = coll.find();
			Iterator<Document> iter = all.iterator();
			while (iter.hasNext()) {
				Document proc = iter.next();
				String id = proc.getString(FieldDefinitions.UNIQUE_ID);
				String name = proc.getString(FieldDefinitions.NAME);
				int inclCount = proc.getInteger("Inclusion Count", 0);
				if (inclCount > 0) {
					ArrayList<Document> included = (ArrayList<Document>)proc.get("Included");
					if (included != null) {
						ArrayList<String> inclIds = new ArrayList<String>();
						for (Document temp : included) {
							String inclId = temp.getString("Procedure");
							inclIds.add(inclId);
						}
						if (keys.containsAll(inclIds)) {
							retVal.put(id, name);
						}
					}
				} else {
					String procID = proc.getString(FieldDefinitions.UNIQUE_ID);
					if (keys.contains(procID)) {
						retVal.put(id, name);
					}
				}
			}
		}

		client.close();
		return retVal;
	}
	
	public static ArrayList<Document> getAll () {
		ArrayList<Document> all = new ArrayList<Document>();
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoDatabase db = client.getDatabase(__dbName);
		MongoCollection<Document> coll = db.getCollection(DatabaseDefinitions.PROCEDURE_COLL);
		FindIterable<Document> out = coll.find();
		Iterator<Document> iter = out.iterator();
		while (iter.hasNext()) {
			all.add(iter.next());
		}
		client.close();
		return all;
	}
	
	public static double getNationalAverage (String templateID) {
		double ret = 0;
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoDatabase db = client.getDatabase(__dbName);
		MongoCollection<Document> coll = db.getCollection(DatabaseDefinitions.BUNDLE_COLL);
		if (coll != null) {
			Document match = new Document ("Template", templateID);
			AggregateIterable<Document> out = coll.aggregate(Arrays.asList(
							new Document("$match", match),
							new Document("$group", new Document ("_id", "$Template Name").append("numSold", new Document("$sum", "$Number Sold")).
									  append("avgPrice", new Document("$avg", "$Base Price")))
							)
					);
			for (Document d : out) {
				ret = d.getDouble("avgPrice");
			}
		}
		client.close();
		return ret;
	}
	
	public static int getNumberSold (String templateID) {
		int ret = 0;
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoDatabase db = client.getDatabase(__dbName);
		MongoCollection<Document> coll = db.getCollection(DatabaseDefinitions.BUNDLE_COLL);
		if (coll != null) {
			Document match = new Document ("Template", templateID);
			AggregateIterable<Document> out = coll.aggregate(Arrays.asList(
							new Document("$match", match),
							new Document("$group", new Document ("_id", "$Template Name").append("numSold", new Document("$sum", "$Number Sold")).
									  append("avgPrice", new Document("$avg", "$Base Price")))
							)
					);
			for (Document d : out) {
				ret = d.getInteger("numSold", 0);
			}
		}
		client.close();
		return ret;
	}

	public static Document getPriceStats (String id) {
		Document ret = new Document ();
		return ret;
	}
	
	public static void populateCollection () {
		createAndAddAtomicProcedures();
		createAndAddBundles();
	}
	
	public static void createAndAddAtomicProcedures () {
		ProcedureDoc pd = new ProcedureDoc ("GMV_PROC_PH");
		pd.setName("A generic placeholder procedure");
		pd.setType("Placeholder");
		pd.addApplicability("all", "any", 0, 1000, 0, 1000);
		pd.setVariantLevel(0);
		pd.setProcedureTime(0);
		pd.setResultsTime(0);
		pd.setRegionalPrice (0);
		pd.setDescription("This is a generic placeholder procedure");
		pd.setReason ("Well");
		pd.setWhoCanDo(RoleDefinitions.ROLE_001);
		pd.write();
		
		pd = new ProcedureDoc ("GMV_PROC_A_100");
		pd.setName("Consultation - Type 1");
		pd.setType("Consultation");
		pd.addApplicability("all", "any", 0, 1000, 0, 1000);
		pd.setVariantLevel(0);
		pd.setProcedureTime(1);
		pd.setResultsTime(0);
		pd.setRegionalPrice (175);
		pd.setDescription("This is a wellness consultation");
		pd.setReason ("Well");
		pd.setWhoCanDo(RoleDefinitions.ROLE_008);
		pd.write();

		pd = new ProcedureDoc ("GMV_PROC_A_101");
		pd.setName("Consultation - Type 2");
		pd.setType("Consultation - Type 2");
		pd.addApplicability("all", "any", 0, 1000, 0, 1000);
		pd.setVariantLevel(1);
		pd.setProcedureTime(1);
		pd.setResultsTime(0);
		pd.setRegionalPrice (175);
		pd.setDescription("Sickness Consult");
		pd.setReason ("Sick");
		pd.setWhoCanDo(RoleDefinitions.ROLE_008);
		pd.write();

		pd = new ProcedureDoc ("GMV_PROC_A_102");
		pd.setName("Consultation - Type 3");
		pd.setType("Consultation - Type 3");
		pd.addApplicability("all", "any", 0, 1000, 0, 1000);
		pd.setVariantLevel(2);
		pd.setProcedureTime(1);
		pd.setResultsTime(0);
		pd.setRegionalPrice (175);
		pd.setDescription("Wellness Consult");
		pd.setReason ("Well");
		pd.setWhoCanDo(RoleDefinitions.ROLE_008);
		pd.write();

		pd = new ProcedureDoc ("GMV_PROC_A_103");
		pd.setName("Consultation - Type 4");
		pd.setType("Consultation - Type 4");
		pd.addApplicability("all", "any", 0, 1000, 0, 1000);
		pd.setVariantLevel(2);
		pd.setProcedureTime(1);
		pd.setResultsTime(0);
		pd.setRegionalPrice (175);
		pd.setDescription("Wellness Consult");
		pd.setReason ("Well");
		pd.setWhoCanDo(RoleDefinitions.ROLE_008);
		pd.write();

		pd = new ProcedureDoc ("GMV_PROC_A_104");
		pd.setName("Sedation for Imaging");
		pd.setType("Sedation");
		pd.addApplicability("all", "any", 0, 1000, 0, 1000);
		pd.setVariantLevel(0);
		pd.setProcedureTime(1);
		pd.setResultsTime(0);
		pd.setRegionalPrice (65);
		pd.setDescription("Sedation for Imaging");
		pd.setReason ("Well");
		pd.setWhoCanDo(RoleDefinitions.ROLE_015);
		pd.write();

		pd = new ProcedureDoc ("GMV_PROC_A_200");
		pd.setName("Blood Count");
		pd.setType("Blood Work");
		pd.addApplicability("all", "any", 0, 1000, 0, 1000);
		pd.setVariantLevel(0);
		pd.setProcedureTime(1);
		pd.setResultsTime(8);
		pd.setRegionalPrice (175);
		pd.setDescription("Blood Work");
		pd.setReason ("Well");
		pd.setWhoCanDo(RoleDefinitions.ROLE_011);
		pd.write();

		pd = new ProcedureDoc ("GMV_PROC_A_201");
		pd.setName("Blood Count");
		pd.setType("Blood Work");
		pd.addApplicability("all", "any", 0, 1000, 0, 1000);
		pd.setVariantLevel(1);
		pd.setProcedureTime(1);
		pd.setResultsTime(8);
		pd.setRegionalPrice (175);
		pd.setDescription("Blood Work");
		pd.setReason ("Sick");
		pd.setWhoCanDo(RoleDefinitions.ROLE_011);
		pd.write();

		pd = new ProcedureDoc ("GMV_PROC_A_202");
		pd.setName("Blood Count");
		pd.setType("Blood Work");
		pd.addApplicability("all", "any", 0, 1000, 0, 1000);
		pd.setVariantLevel(0);
		pd.setProcedureTime(0);
		pd.setResultsTime(8);
		pd.setRegionalPrice (175);
		pd.setDescription("Blood Work");
		pd.setReason ("Sick");
		pd.setWhoCanDo(RoleDefinitions.ROLE_011);
		pd.write();

		pd = new ProcedureDoc ("GMV_PROC_A_203");
		pd.setName("Urinalysis");
		pd.setType("Urine Test");
		pd.addApplicability("all", "any", 0, 1000, 0, 1000);
		pd.setVariantLevel(0);
		pd.setProcedureTime(1);
		pd.setResultsTime(8);
		pd.setRegionalPrice (85);
		pd.setDescription("Urine Analysis");
		pd.setReason ("Sick");
		pd.setWhoCanDo(RoleDefinitions.ROLE_012);
		pd.write();

		pd = new ProcedureDoc ("GMV_PROC_A_204");
		pd.setName("Urinalysis");
		pd.setType("Urine Test");
		pd.addApplicability("all", "any", 0, 1000, 0, 1000);
		pd.setVariantLevel(1);
		pd.setProcedureTime(1);
		pd.setResultsTime(8);
		pd.setRegionalPrice (175);
		pd.setDescription("Urine Analysis");
		pd.setReason ("Sick");
		pd.setWhoCanDo(RoleDefinitions.ROLE_012);
		pd.write();

		pd = new ProcedureDoc ("GMV_PROC_A_205");
		pd.setName("Imaging Procedure");
		pd.setType("Imaging");
		pd.addApplicability("all", "any", 0, 1000, 0, 1000);
		pd.setVariantLevel(0);
		pd.setDescription("X Ray");
		pd.setProcedureTime(1);
		pd.setResultsTime(8);
		pd.setRegionalPrice (225);
		pd.setReason ("Sick");
		pd.setWhoCanDo(RoleDefinitions.ROLE_013);
		pd.write();
		
		pd = new ProcedureDoc ("GMV_PROC_A_206");
		pd.setName("Imaging Procedure");
		pd.setType("Imaging");
		pd.addApplicability("all", "any", 0, 1000, 0, 1000);
		pd.setVariantLevel(1);
		pd.setDescription("CT Scan");
		pd.setProcedureTime(1);
		pd.setResultsTime(8);
		pd.setRegionalPrice (225);
		pd.setReason ("Sick");
		pd.setWhoCanDo(RoleDefinitions.ROLE_014);
		pd.write();

		pd = new ProcedureDoc ("GMV_PROC_A_300");
		pd.setName("Rehab");
		pd.setType("Rehab");
		pd.addApplicability("all", "any", 0, 1000, 0, 1000);
		pd.setVariantLevel(0);
		pd.setDescription("Rehab");
		pd.setProcedureTime(1);
		pd.setResultsTime(8);
		pd.setRegionalPrice (135);
		pd.setReason ("Rehab");
		pd.setWhoCanDo(RoleDefinitions.ROLE_009);
		pd.write();

		pd = new ProcedureDoc ("GMV_PROC_A_501");
		pd.setName("TPLO Surgery");
		pd.setType("TPLO Surgery");
		pd.addApplicability("all", "any", 0, 1000, 0, 1000);
		pd.addApplicability("male doberman", "male", 36, 48, 50, 70);
		pd.setVariantLevel(1);
		pd.setDescription("TPLO Surgery");
		pd.setProcedureTime(8);
		pd.setResultsTime(0);
		pd.setRegionalPrice (2500);
		pd.setReason ("Sick");
		pd.setWhoCanDo(RoleDefinitions.ROLE_008);
		pd.write();
	}
	
	public static void createAndAddBundles() {
		ProcedureDoc pd = new ProcedureDoc ("GMV_PROC_B_101");
		pd.setName("TPLO - Standard");
		pd.setType("TPLO Surgery");
		pd.addApplicability("large dogs", "any", 0, 1000, 0, 1000);
		pd.setVariantLevel(0);
		pd.setDescription("TPLO Surgery Bundle");
		pd.setReason("Sick");
		pd.addInclusion("GMV_PROC_A_100", true, false, "0", -1, true);
		pd.setTimeTillNext("GMV_PROC_A_100", 0);
		pd.addInclusion("GMV_PROC_A_200", true, true, "0", -1, false);
		pd.setTimeTillNext("GMV_PROC_A_200", 0);
		pd.addInclusion("GMV_PROC_A_203", true, true, "0", -1, false);
		pd.setTimeTillNext("GMV_PROC_A_203", 0);
		pd.addInclusion("GMV_PROC_A_104", true, true, "0", -1, false);
		pd.setTimeTillNext("GMV_PROC_A_104", 0);
		pd.addInclusion("GMV_PROC_A_205", true, true, "0", -1, false);
		pd.setTimeTillNext("GMV_PROC_A_205", 42);

		pd.addInclusion("GMV_PROC_A_501", true, true, "x", -1, true);
		pd.addInclusion("GMV_PROC_A_100", true, true, "x + 42", -1, false);
		pd.setTimeTillNext("GMV_PROC_A_100", 28);
		pd.addInclusion("GMV_PROC_A_104", true, true, "x + 42", -1, false);
		pd.setTimeTillNext("GMV_PROC_A_104", 0);
		pd.addInclusion("GMV_PROC_A_205", true, true, "x + 42", -1, true);
		pd.setTimeTillNext("GMV_PROC_A_205", 0);

		pd.addInclusion("GMV_PROC_A_100", true, true, "x + 70", -1, false);
		pd.setTimeTillNext("GMV_PROC_A_100", 42);
		pd.addInclusion("GMV_PROC_A_104", true, true, "x + 70", -1, false);
		pd.setTimeTillNext("GMV_PROC_A_104", 0);
		pd.addInclusion("GMV_PROC_A_205", true, true, "x + 70", -1, true);
		pd.setTimeTillNext("GMV_PROC_A_205", 0);
		
		pd.addInclusion("GMV_PROC_A_300", false, true, "TBD", -1, false);

		pd.addPossibleInclusion("GMV_PROC_A_300");
		pd.addPart("GMV_PART_003");
		pd.write();
		
		pd = new ProcedureDoc ("GMV_PROC_B_102");
		pd.setName("TPLO - Large Dog");
		pd.setType("TPLO Surgery");
		pd.addApplicability("large dogs", "any", 0, 1000, 0, 1000);
		pd.setVariantLevel(0);
		pd.setDescription("TPLO Surgery Bundle");
		pd.setReason("Sick");
		pd.addInclusion("GMV_PROC_A_100", true, false, "0", -1, true);
		pd.setTimeTillNext("GMV_PROC_A_100", 0);
		pd.addInclusion("GMV_PROC_A_200", true, true, "0", -1, false);
		pd.setTimeTillNext("GMV_PROC_A_200", 0);
		pd.addInclusion("GMV_PROC_A_203", true, true, "0", -1, false);
		pd.setTimeTillNext("GMV_PROC_A_203", 0);
		pd.addInclusion("GMV_PROC_A_104", true, true, "0", -1, false);
		pd.setTimeTillNext("GMV_PROC_A_104", 0);
		pd.addInclusion("GMV_PROC_A_205", true, true, "0", -1, true);
		pd.setTimeTillNext("GMV_PROC_A_205", 42);

		pd.addInclusion("GMV_PROC_A_501", true, true, "x", -1, true);
		pd.addInclusion("GMV_PROC_A_100", true, true, "x + 42", -1, false);
		pd.setTimeTillNext("GMV_PROC_A_100", 28);
		pd.addInclusion("GMV_PROC_A_104", true, true, "x + 42", -1, false);
		pd.setTimeTillNext("GMV_PROC_A_104", 0);
		pd.addInclusion("GMV_PROC_A_205", true, true, "x + 42", -1, true);
		pd.setTimeTillNext("GMV_PROC_A_205", 0);

		pd.addInclusion("GMV_PROC_A_100", true, true, "x + 70", -1, false);
		pd.setTimeTillNext("GMV_PROC_A_100", 42);
		pd.addInclusion("GMV_PROC_A_104", true, true, "x + 70", -1, false);
		pd.setTimeTillNext("GMV_PROC_A_104", 0);
		pd.addInclusion("GMV_PROC_A_205", true, true, "x + 70", -1, true);
		pd.setTimeTillNext("GMV_PROC_A_205", 0);
		
		pd.addInclusion("GMV_PROC_A_300", false, true, "TBD", -1, false);

		pd.addPossibleInclusion("GMV_PROC_A_300");
		pd.addPart("GMV_PART_004");
		pd.write();
		
		pd = new ProcedureDoc ("Inclusion_Test");
		pd.addInclusion("GMV_PROC_A_100", true, true, "x", -1, false);
		pd.addInclusion("GMV_PROC_A_102", true, true, "x", -1, false);
		pd.addInclusion("GMV_PROC_A_104", true, true, "x", -1, false);
		pd.addInclusion("GMV_PROC_A_101", true, true, "x", 2, false);
		pd.addInclusion("GMV_PROC_A_103", true, true, "x", 7, false);
		pd.write();
	}
}
