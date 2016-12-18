package com.gmv.pre.TestApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.BsonArray;
import org.bson.BsonDouble;
import org.bson.Document;

import com.gmv.pre.db.mongo.MongoFileStorage;
import com.gmv.pre.db.mongo.MongoInterface;
import com.gmv.pre.definitions.DatabaseDefinitions;
import com.gmv.pre.definitions.FieldDefinitions;
import com.gmv.pre.definitions.QueryDefaults;
import com.gmv.pre.definitions.RoleDefinitions;
import com.gmv.pre.helpers.ImportDatabase;
import com.gmv.pre.helpers.ZipToCoord;
import com.gmv.pre.structs.BreedDoc;
import com.gmv.pre.structs.BundleDoc;
import com.gmv.pre.structs.OfferingDoc;
import com.gmv.pre.structs.PartDoc;
import com.gmv.pre.structs.PetDoc;
import com.gmv.pre.structs.PractitionerDoc;
import com.gmv.pre.structs.ProcedureDoc;
import com.gmv.pre.structs.UserDefDoc;
import com.gmv.pre.structs.UserDoc;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

public class TestMain {
	
	private static OfferingDoc bronze = new OfferingDoc ();
	private static OfferingDoc silver = new OfferingDoc ();
	private static OfferingDoc gold = new OfferingDoc ();
	private static ArrayList<Document> __availableTimes = new ArrayList<Document>();
	
	public static void addTimeslot (Date date, double price) {
		Document doc = new Document ("Time", date);
		doc.append("Time (Pretty)", date.toString());
		doc.append("Promo Price", price);
		doc.append("Available", true);
		__availableTimes.add(doc);
	}
	
	public static void helperAddTimeslotsForMonth (int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2016, month, 1);
		for (int i = 0; i < 30; i ++) {
			calendar.set(Calendar.HOUR_OF_DAY, 9);
			for (int j = 0; j < 9; j++) {
				Date day = calendar.getTime();
				if (calendar.get(Calendar.DAY_OF_WEEK) > 5)
					addTimeslot(day, 5000*Math.random());
				else
					addTimeslot (day, 7500*Math.random());
				calendar.add(Calendar.HOUR_OF_DAY, 1);
			}
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
	}

	public static void initBundles () {
		ProcedureDoc bundle = new ProcedureDoc ("GMV_PROC_B_102");
		bronze.setTemplate(bundle);
		bronze.addTimeSlots(__availableTimes);
		silver.setTemplate(bundle);
		silver.addTimeSlots(__availableTimes);
		gold.setTemplate(bundle);
		gold.addTimeSlots(__availableTimes);
	}
	private static void initDBVars () {
		DatabaseDefinitions.readServerConf("");
		String db = "gmv_no_sql";
		String breed_coll = DatabaseDefinitions.BREED_COLL;
		String prac_coll = DatabaseDefinitions.PRAC_COLL;
		String proc_coll = DatabaseDefinitions.PROCEDURE_COLL;
		String part_coll = DatabaseDefinitions.PART_COLL;
		String pet_coll = DatabaseDefinitions.PET_COLL;
		String owner_coll = DatabaseDefinitions.OWNER_COLL;
		String practitioner_coll = DatabaseDefinitions.PRACTITIONER_COLL;
		// Begin initializing variables
		BreedDoc.initDBVariables(db, breed_coll);
		ProcedureDoc.initDBVariables(db, proc_coll);
		PartDoc.initDBVariables(db, part_coll);
		PetDoc.initDBVariables(db, pet_coll, breed_coll, owner_coll);
		PractitionerDoc.initDBVariables (db, practitioner_coll);
		OfferingDoc.initDBVariables(db, "offering");
	}
	
	public static void createPractitioners () {
		PractitionerDoc pd = new PractitionerDoc ();
		pd.setName("Trey Parker");
		pd.setRoleName(RoleDefinitions.ROLE_001);
		pd.setEmail("email@email.com");
		pd.setPhone("+1234567890");
		pd.addOffice ("GMV_PRACTICE_001_OFFICE_001");
		pd.addOffice ("GMV_PRACTICE_001_OFFICE_002");
		pd.addOffice ("GMV_PRACTICE_001_OFFICE_003");
		pd.Write();
		
		pd = new PractitionerDoc ();
		pd.setName("Matt Stone");
		pd.setRoleName(RoleDefinitions.ROLE_002);
		pd.setEmail("email@email.com");
		pd.setPhone("+1234567890");
		pd.addOffice ("GMV_PRACTICE_001_OFFICE_001");
		pd.addOffice ("GMV_PRACTICE_001_OFFICE_002");
		pd.addOffice ("GMV_PRACTICE_001_OFFICE_003");
		pd.Write();
		
		pd = new PractitionerDoc ();
		pd.setName("Eric Cartman");
		pd.setRoleName(RoleDefinitions.ROLE_003);
		pd.setEmail("email@email.com");
		pd.setPhone("+1234567890");
		pd.addOffice ("GMV_PRACTICE_001_OFFICE_001");
		pd.addOffice ("GMV_PRACTICE_001_OFFICE_002");
		pd.addOffice ("GMV_PRACTICE_001_OFFICE_003");
		pd.Write();
		
		pd = new PractitionerDoc ();
		pd.setName("Kyle Broflovski");
		pd.setRoleName(RoleDefinitions.ROLE_004);
		pd.setEmail("email@email.com");
		pd.setPhone("+1234567890");
		pd.addOffice ("GMV_PRACTICE_001_OFFICE_001");
		pd.addOffice ("GMV_PRACTICE_001_OFFICE_002");
		pd.addOffice ("GMV_PRACTICE_001_OFFICE_003");
		pd.Write();
		
		pd = new PractitionerDoc ();
		pd.setName("Stan Marsh");
		pd.setRoleName(RoleDefinitions.ROLE_005);
		pd.setEmail("email@email.com");
		pd.setPhone("+1234567890");
		pd.addOffice ("GMV_PRACTICE_001_OFFICE_001");
		pd.addOffice ("GMV_PRACTICE_001_OFFICE_002");
		pd.addOffice ("GMV_PRACTICE_001_OFFICE_003");
		pd.Write();

		pd = new PractitionerDoc ();
		pd.setName("Kenny McCormick");
		pd.setRoleName(RoleDefinitions.ROLE_006);
		pd.setEmail("email@email.com");
		pd.setPhone("+1234567890");
		pd.addOffice ("GMV_PRACTICE_002_OFFICE_001");
		pd.Write();
		
		pd = new PractitionerDoc ();
		pd.setName("Butters Scotch");
		pd.setRoleName(RoleDefinitions.ROLE_007);
		pd.setEmail("email@email.com");
		pd.setPhone("+1234567890");
		pd.addOffice ("GMV_PRACTICE_002_OFFICE_001");
		pd.Write();	

		pd = new PractitionerDoc ();
		pd.setName("Wendy Testaburger");
		pd.setRoleName(RoleDefinitions.ROLE_008);
		pd.setEmail("email@email.com");
		pd.setPhone("+1234567890");
		pd.addOffice ("GMV_PRACTICE_002_OFFICE_001");
		pd.Write();
		
		pd = new PractitionerDoc ();
		pd.setName("Token Black");
		pd.setRoleName(RoleDefinitions.ROLE_009);
		pd.setEmail("email@email.com");
		pd.setPhone("+1234567890");
		pd.addOffice ("GMV_PRACTICE_002_OFFICE_002");
		pd.Write();

		pd = new PractitionerDoc ();
		pd.setName("Randy Marsh");
		pd.setRoleName(RoleDefinitions.ROLE_010);
		pd.setEmail("email@email.com");
		pd.setPhone("+1234567890");
		pd.addOffice ("GMV_PRACTICE_003_OFFICE_001");
		pd.addOffice ("GMV_PRACTICE_003_OFFICE_002");
		pd.Write();
	}
	
	public static void createAndAddParts () {
		PartDoc pd = new PartDoc ("GMV_PART_001");
		pd.setName ("Dental Filling");
		pd.setType ("Dental Filling");
		pd.setVariantLevel (0);
		pd.setDescription("Plaster Filling");
		pd.Write();
		
		pd = new PartDoc ("GMV_PART_002");
		pd.setName ("Dental Filling");
		pd.setType ("Dental Filling");
		pd.setVariantLevel (1);
		pd.setDescription("Ceramic Filling");
		pd.Write();
		
		pd = new PartDoc ("GMV_PART_003");
		pd.setName ("Standard Plate");
		pd.setType ("Plate");
		pd.setVariantLevel (0);
		pd.setDescription("Standard Plate");
		pd.setStandardPrice(2900);
		pd.Write();
	
		pd = new PartDoc ("GMV_PART_004");
		pd.setName ("Large Plate");
		pd.setType ("Plate");
		pd.setVariantLevel (1);
		pd.setDescription("Large Plate");
		pd.setStandardPrice(3500);
		pd.Write();
	}
	
	public static void importPracticesToDB (String csvFileName, String dbName, String collName) {
		// Open up Mongo Connection
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		for (int i = 0; i < DatabaseDefinitions.MONGO_SERVER_LIST.size(); i++) {
			ServerAddress sa = DatabaseDefinitions.MONGO_SERVER_LIST.get(i);
			System.out.println("Server config - " + sa.toString());
		}
		MongoDatabase db = client.getDatabase (dbName);
		MongoCollection<Document> coll = db.getCollection (collName);
/*		MongoCollection<Document> offeringColl = db.getCollection("offering");
*/		if (db == null || coll == null) {
			client.close();
			return;
		}

		int id = 1;
		// Open up the CSV file and read every record
        String splitBy = ",";
        String name = "", street = "", city = "", state = "", zip = "", phone = "", longitude = "", latitude = "";
        try {
			BufferedReader br = new BufferedReader(new FileReader(csvFileName));
			String line = br.readLine();
			while(line!=null){
			     String[] b = line.split(splitBy);
			     if (b.length < 8) {
			    	 // missing one of the fields - just move on
			    	 line = br.readLine(); 
		    	 } else if (b[4].contains(FieldDefinitions.LONGITUDE) ||
		    			 	b[5].contains(FieldDefinitions.LATITUDE)) {
		    		 // Header line. Ignore this and go to the next line
		    		 line = br.readLine();
		    	 }
			     else {
			    	 String uid = FieldDefinitions.PRACTICE_PREFIX + id++;
			    	 name = b[0];
			    	 city = b[1];
			    	 state = b[2];
			    	 zip = b[3];
			    	 longitude = b[4];
			    	 latitude = b[5];
			    	 street = b[6];
			    	 phone = b[7];
				     Document d = new Document (FieldDefinitions.NAME, name);
				     d.append("Street", street);
				     d.append("City", city);
				     d.append("State", state);
				     d.append("Zip", zip);
				     d.append("Phone", phone);
				     BsonDouble longitude_d = new BsonDouble (Double.parseDouble(longitude));
				     BsonDouble latitude_d = new BsonDouble(Double.parseDouble(latitude));
				     Document loc = new Document ("type", "Point");
				     loc.append("coordinates", new BsonArray(Arrays.asList(longitude_d, latitude_d)));
				     d.append ("loc", loc);
				     d.append (FieldDefinitions.UNIQUE_ID, uid);
				     double wTimeRank = Math.random() * 5;
				     double bRank = Math.random() * 5;
				     double cRank = 0.5 * wTimeRank + 0.5 * bRank;
				     ArrayList<String> animals = new ArrayList<String>();
				     if (wTimeRank > bRank) {
				    	 animals.add("dog");
				    	 animals.add("cat");
				     } else {
				    	 animals.add("cat");
				     }
				     Document ranking = new Document (FieldDefinitions.WAIT_TIME_RANKING, wTimeRank);
				     ranking.append(FieldDefinitions.BED_SIDE_MANNERS_RANKING, bRank);
				     ranking.append(FieldDefinitions.COMPOSITE_RANKING, cRank);
				     d.append (FieldDefinitions.RANKING, ranking);
				     d.append(FieldDefinitions.ANIMALS_TREATED, animals);
				     line=br.readLine();
				     coll.insertOne(d);
				     if (state.contains("NY") || state.contains("NJ") || state.contains("MD") || state.contains("DC")) {
				     
				     //now insert offerings for this. First get a random number
/*				     double randomNum = Math.random();
				     //set name and ID
				     bronze.setID(uid + "-TPLO_Bronze");
				     bronze.setName(uid + "-TPLO_Bronze");
				     silver.setID(uid + "-TPLO_Silver");
				     silver.setName(uid + "-TPLO_Silver");
				     gold.setID(uid + "-TPLO_Silver");
				     gold.setName(uid + "-TPLO_Silver");
				     
					//set base and promo prices
					if (randomNum > 0.5) {
						bronze.setFinancingAvailable(true);
					}
					bronze.setBasePrice(5000 * randomNum);
					bronze.setPromoPrice(4000 * randomNum);
					bronze.setPartner("GMV_PROC_A_300", "GMV_PRACT");
					bronze.setOffice("GMV_PROC_A_203", "Main Street Lab Services");
					bronze.setCancellationPenalty(10);
					bronze.setDescription("bronze or standard version 1 extra office and 1 extra provider");
					bronze.setProviderDetails(uid, name, loc, ranking);
					offeringColl.insertOne(bronze.toDocument());

					if (randomNum > 0.5) {
						silver.setFinancingAvailable(true);
					}
					silver.setBasePrice(6000 * randomNum);
					silver.setPromoPrice(4750 * randomNum);
					silver.setTransferPrice(2500);
					silver.setPartner("GMV_PROC_A_300", "Physio Therapist");
					silver.addAddons("GMV_PROC_A_300", 4);
					silver.setCancellationPenalty(5);
					silver.setDescription("od1 - silver - slight upgrade - single office and 1 extra provider");
					silver.setProviderDetails(uid, name, loc, ranking);
					offeringColl.insertOne(silver.toDocument());

					if (randomNum > 0.5) {
						gold.setFinancingAvailable(true);
					}
					gold.setBasePrice(7000 * randomNum);
					gold.setPromoPrice(5500 * randomNum);
					gold.setTransferPrice(2500);
					gold.addAddons("GMV_PROC_A_300", 8);
					gold.setCancellationPenalty(0);
					gold.setDescription("od1 - gold - single office, single provider, best value");
					gold.setProviderDetails(uid, name, loc, ranking);
					offeringColl.insertOne(gold.toDocument());
*/					
/*					ArrayList<Document> offerings = new ArrayList<Document>();
					offerings.add(bronze.toDocument());
					offerings.add(silver.toDocument());
					offerings.add(gold.toDocument());
					offeringColl.insertMany(offerings);
					offerings.clear();
*/				}
		     }
			}
			br.close();
        } catch (Exception e) {
        	StringWriter sw = new StringWriter();
        	PrintWriter pw = new PrintWriter(sw);
        	e.printStackTrace(pw);
        	coll.insertOne(new Document("Exception", e.getMessage()).append("Exception String", sw.toString()));
        	client.close();
        	e.printStackTrace();
        	return;
        }
        coll.createIndex(new BasicDBObject ("loc", "2dsphere"));
		String providerLoc = "Bundle Info." + FieldDefinitions.PROVIDER_LOC;
/*        offeringColl.createIndex(new BasicDBObject (providerLoc, "2dsphere"));
*/        client.close();
	}

	private static Document constructQuery (int zip, 
			 long maxDistance, 
			 String bundle, 
			 double rank,
			 int numOffices,
			 int numProviders,
			 double cancelPenalty,
			 double maxPrice,
			 String catOrDog,
			 int start,
			 int end) {
// get coordinates for zip
ZipToCoord ztc = new ZipToCoord (zip);
ztc.getCoordsForZip ();

// construct the query
BasicDBList dblList = new BasicDBList ();
dblList.add(ztc.lon);
dblList.add(ztc.lat);

Document geom = new Document ("type", "Point");
geom.append("coordinates", dblList);

Document near = new Document ("$geometry", geom);
near.append("$maxDistance", maxDistance);

Document nearObj = new Document ("$near", near);
Document queryDoc = new Document ("Provider Location", nearObj);
queryDoc.append("Ranking.Wait Time Ranking", new BasicDBObject("$gt", rank));
queryDoc.append("Number Offices", new BasicDBObject("$lte", numOffices));
queryDoc.append("Number Providers", new BasicDBObject("$lte", numProviders));
queryDoc.append("Cancel Penalty", new BasicDBObject("$lte", cancelPenalty));
queryDoc.append("Base Price", new BasicDBObject("$lte", maxPrice));

Calendar cal = Calendar.getInstance();

Date startDate = new Date ();
cal.setTime(startDate);
cal.add(Calendar.DATE, start);
startDate = cal.getTime();

Date endDate = new Date ();
cal.setTime(endDate);
cal.add(Calendar.DATE, end);
endDate = cal.getTime();
queryDoc.append("When", new BasicDBObject("$gte", startDate).append("$lte", endDate));

if (bundle != null && bundle != "") {
// user wants a bundle, let's get that
queryDoc.append("Template.UniqueID", bundle);
queryDoc.append("Template.Applies To", new BasicDBObject("$regex", catOrDog));
}                                    

return queryDoc;
}

	public static void addUserDefinitions () {
		UserDefDoc udef = new UserDefDoc ("bowner", "Pet Owner"); udef.write();
		udef = new UserDefDoc ("bfamily", "Family Member"); udef.write();
		udef = new UserDefDoc ("sowner", "Practice Owner"); udef.write();
		udef = new UserDefDoc ("soffice", "Admin - Front Office"); udef.write();
		udef = new UserDefDoc ("sbilling", "Admin - Billing"); udef.write();
		udef = new UserDefDoc ("sscheduling", "Admin - Scheduling"); udef.write();
		udef = new UserDefDoc ("snurse", "Clinical - Nurse"); udef.write();
		udef = new UserDefDoc ("slab", "Clinical - Lab"); udef.write();
		udef = new UserDefDoc ("simaging", "Clinical - Imaging"); udef.write();
		udef = new UserDefDoc ("svet", "Clinical - Vet"); udef.write();
		udef = new UserDefDoc ("sanesthetist", "Clinical - Anesthetist"); udef.write();
		udef = new UserDefDoc ("ssurgeon", "Clinical - Surgeon"); udef.write();

	}
	
	public static void addVetUsers () {
		UserDoc vet = new UserDoc("jdoe", "bowner");
		vet.setFirstName("John");
		vet.setLastName("Doe");
		vet.setBio("A reputed vet practitioner in the tri-state area");
		vet.setPhone("2013456789");
		vet.setEmail("jdoe@example.com");
		ArrayList<Document> education = new ArrayList<Document>();
		education.add(new Document ("Degree", "BS").append("School", "University of Maryland").append("Graduation", 2002));
		education.add(new Document ("Degree", "MS").append("School", "University of Virginia").append("Graduation", 2008));
		vet.addField("Education", education);
		
		ArrayList<Document> experience = new ArrayList<Document>();
		experience.add(new Document ("Employer", "Vet Care Associates").append("Role", "Practice Vet").append("From", 2002).append("To", 2006));
		experience.add(new Document ("Employer", "Happy Vets").append("Role", "Senior Vet").append("From", 2006).append("To", 2008));
		experience.add(new Document ("Employer", "John's Vet Care").append("Role", "Director").append("From", 2008).append("To", "-"));
		vet.addField("Experience", experience);
		vet.write();
	}
	public static void main (String[] args) {
		initDBVars ();
/*		addUserDefinitions ();
		addVetUsers();
*/		BundleDoc.initDBVariables("gmv_no_sql", "sellable_bundles");
		for (int i = 1; i <= 10; i=i+1) {
			String pracID = "GMV_PRACTICE_" + i;
			String bundleID = pracID + "TPLO_Surgery_Bundle";
			BundleDoc bd = BundleDoc.createBundle(bundleID + "_b", pracID, "GMV_PROC_B_102");
			bd.setName("TPLO Bronze");
			bd.addInclusion("GMV_PROC_A_300", false, true, "x + 70", -1, true);
			bd.addEntryTime("Monday", true);
			bd.addEntryTime("Tuesday", true);
			bd.addEntryTime("Wednesday", true);
			bd.addEntryTime("Thursday", true);
			bd.addEntryTime("Friday", true);
			bd.addEntryTime("Saturday", true);
			bd.addCoreTime("Monday", true);
			bd.addCoreTime("Wednesday", true);
			bd.write();
			
			bd = BundleDoc.createBundle(bundleID + "_s", pracID, "GMV_PROC_B_102");
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
			bd.write();

			bd = BundleDoc.createBundle(bundleID + "_g", pracID, "GMV_PROC_B_102");
			bd.setName("TPLO Gold");
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
			bd.write();
		}

		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoCollection<Document> coll = client.getDatabase(DatabaseDefinitions.NOSQL_DB).getCollection("sellable_bundles");
		String providerLoc = "Bundle Info." + FieldDefinitions.RENDERING_LOC;
        coll.createIndex(new BasicDBObject (providerLoc, "2dsphere"));
        client.close();
		
		findBundle (99503, 10000, "dog");
		//importPracticesToDB("C:/sriramr/gmv_feed/dummy_vet_location_data.csv", "gmv_no_sql", DatabaseDefinitions.PRAC_COLL);		
/*		ProcedureDoc.createAndAddAtomicProcedures();
		ProcedureDoc.createAndAddBundles();
		OfferingDoc.createAndAddOfferings();
*/		
		//initBundles();
/*		ImportDatabase idb = new ImportDatabase ();
		idb.importDogBreedsToDB_newFormat("C:/Users/srira/breeds.json", DatabaseDefinitions.NOSQL_DB, DatabaseDefinitions.BREED_COLL);
*/		
		//createAndAddParts();
//		createPractitioners();
	}
	
	public static void main3 (String[] args) {
		initDBVars ();
		MongoFileStorage mfs = new MongoFileStorage ("history", "docus2");
		mfs.saveFileToMongo("C:/Users/srira/Downloads/16105956696_AFPxxxxx9R_A44.pdf", "sriramr");
		mfs.saveFileToMongo("C:/Users/srira/Downloads/circular_25_Resonance_2016__1470728590108.pdf", "sriramr");
		mfs.saveFileToMongo("C:/Users/srira/Downloads/16105956696_AFPxxxxx9R_A44.pdf", "sriramr");
		mfs.saveFileToMongo("C:/Users/srira/Downloads/circular_27_SA1_Portions_Grade2__1471431801557 (1).pdf", "sriramr");
		mfs.saveFileToMongo("C:/Users/srira/Downloads/circular_27_SA1_Portions_Grade2__1471431801557.pdf", "sramanat");
		
		mfs.listAllFiles("sramanat");
		
		mfs.saveToFileSystem("circular_25_Resonance_2016__1470728590108.pdf", "sriramr", "C:/Users/srira/Desktop/");
	}
	
	public static void findProvider (int zip, long dist, String animal, double rank) {
		ZipToCoord ztc = new ZipToCoord (zip);
		ztc.getCoordsForZip ();
		BasicDBList dblList = new BasicDBList ();
		dblList.add(ztc.lon);
		dblList.add(ztc.lat);
		
		Document toFind = new Document ("loc" , new Document ("$near" , new Document ("$geometry" , new Document ("type" , "Point").append("coordinates" , dblList)).append("$maxDistance" , dist)));
		BasicDBList animalsTreated = new BasicDBList();
		if (animal != null && !animal.isEmpty()) {
			animalsTreated.add(animal);
		}
		Document d = new Document("Animals Treated", new Document("$in", animalsTreated));
		toFind.append("Ranking.Composite Ranking", new Document("$gt", rank));
		MongoClient client = new MongoClient();
		MongoCollection<Document> coll = client.getDatabase("gmv_no_sql").getCollection("practices");
		long count = coll.count(toFind);
		System.out.println("Count - " + count);
		client.close();
	}
	
	public static void findBundle (int zip, long dist, String animal) {
		ZipToCoord ztc = new ZipToCoord (zip);
		ztc.getCoordsForZip ();
		BasicDBList dblList = new BasicDBList ();
		dblList.add(ztc.lon);
		dblList.add(ztc.lat);
		
		Document toFind = new Document ("Rendering Location" , new Document ("$near" , new Document ("$geometry" , new Document ("type" , "Point").append("coordinates" , dblList)).append("$maxDistance" , dist)));
		BasicDBList animalsTreated = new BasicDBList();
		if (animal != null && !animal.isEmpty()) {
			animalsTreated.add(animal);
		}
		MongoClient client = new MongoClient();
		MongoCollection<Document> coll = client.getDatabase("gmv_no_sql").getCollection("sellable_bundles");
        coll.createIndex(new BasicDBObject ("Rendering Location", "2dsphere"));
		long count = coll.count(toFind);
		System.out.println("Count - " + count);
		client.close();
	}
}
