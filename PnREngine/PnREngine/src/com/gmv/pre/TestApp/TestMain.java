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
import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.BsonArray;
import org.bson.BsonDouble;
import org.bson.Document;

import com.gmv.pre.core.Comparator;
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
import com.gmv.pre.structs.BundleInstanceDoc;
import com.gmv.pre.structs.OfferingDoc;
import com.gmv.pre.structs.PartDoc;
import com.gmv.pre.structs.PetDoc;
import com.gmv.pre.structs.PracticeDoc;
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

	public enum TM {t1, t2};
	public static void main (String[] args) {
		initDBVars ();
//		ImportDatabase idb = new ImportDatabase();
//		idb.importDogBreedsToDB_newFormat("C:/Users/srira/breeds.json", DatabaseDefinitions.NOSQL_DB, DatabaseDefinitions.BREED_COLL);
//		idb.importPracticesToDB("C:/sriramr/gmv_feed/dummy_vet_location_data.csv", DatabaseDefinitions.NOSQL_DB, DatabaseDefinitions.PRAC_COLL);
//		ProcedureDoc.populateCollection();
//		PartDoc.populateCollection();
//		BundleDoc.populateCollection();
//		findBundle (20748, 10000, "dog", "tplo", 10000, 100000, 2);
//		BundleInstanceDoc bid = new BundleInstanceDoc ();
//		bid.setId("Bundle Instance ID");
//		bid.setBundleId("GMV_PRACTICE_7TPLO_Surgery_Bundle_b");
//		bid.setSellerId("GMV_PRACTICE_7");
//		bid.setBuyerId("Sriram");
//		bid.setSalePrice(3500);
//		System.out.println(bid.toDocument().toJson());
//		
		ZipToCoord zc = new ZipToCoord ("Washington", "DC");
		System.out.println (zc.lat);
		System.out.println (zc.lon);
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
	
	public static void createAndBundles () {
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoCollection<Document> coll = client.getDatabase(DatabaseDefinitions.NOSQL_DB).getCollection(DatabaseDefinitions.PRAC_COLL);
		FindIterable<Document> practices = coll.find();
		Iterator<Document> practices_iter = practices.iterator();
		int count = 0;
		while (practices_iter.hasNext()) {
			Document practice = practices_iter.next();
			String practice_id = practice.getString(FieldDefinitions.UNIQUE_ID);
			String bundleID = practice_id + "TPLO_Surgery_Bundle";

			BundleDoc bd = BundleDoc.createBundle(bundleID + "_b", practice_id, "GMV_PROC_B_102");
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

			bd = BundleDoc.createBundle(bundleID + "_s", practice_id, "GMV_PROC_B_102");
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

			bd = BundleDoc.createBundle(bundleID + "_g", practice_id, "GMV_PROC_B_102");
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
			
			count++;
			
			if (count > 50)
				break;
		}
		client.close();
	}
	
	public static void findBundle (int zip, int dist, String animal, String bundle, double minprice, double maxprice, double rank) {
		Date start = new Date();
		ZipToCoord ztc = new ZipToCoord (zip);
		BasicDBList dblList = new BasicDBList ();
		dblList.add(ztc.lon);
		dblList.add(ztc.lat);
		
		Document toFind = new Document ();
		toFind.append(FieldDefinitions.RENDERING_LOC, new Document ("$near" , new Document ("$geometry" , new Document ("type" , "Point").append("coordinates" , dblList)).append("$maxDistance" , dist)));

		//default all query parameters
		if (animal == null || animal.isEmpty()) {
			animal = "dog";
		}
		toFind.append("Applies To.Type", animal); //new Document("$in", animal));

		// bundle
		if (bundle == null || bundle.isEmpty()) {
			bundle = "TPLO";
		}
		toFind.append("Template Name", new BasicDBObject("$regex", bundle).append("$options", "i"));
		
		// ranking
		toFind.append("Provider Rank.Composite Ranking", new Document("$gte", rank));

		// price
		toFind.append(FieldDefinitions.BASE_PRICE, new Document("$gte", minprice).append("$lte", maxprice));
		
		MongoClient client = new MongoClient();
		MongoCollection<Document> coll = client.getDatabase(DatabaseDefinitions.NOSQL_DB).getCollection(DatabaseDefinitions.BUNDLE_COLL);
		FindIterable<Document> iterable = coll.find(toFind);
		Iterator<Document> iter = iterable.iterator ();
		ArrayList<Document> queryList = new ArrayList<Document>();
		while (iter.hasNext()) {
			Document doc = iter.next ();
			queryList.add(doc);
		}
		Date end = new Date();
		Document queryOut = new Document();
		queryOut.append("Time Taken", end.getTime()-start.getTime());
		queryOut.append("Count", queryList.size());
		queryOut.append("Matches", queryList);
		System.out.println(toFind.toJson());
		System.out.println(queryList.size());
		client.close();
	}
}
