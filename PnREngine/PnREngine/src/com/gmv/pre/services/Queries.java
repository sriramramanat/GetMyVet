package com.gmv.pre.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;

import com.gmv.pre.db.mongo.MongoInterface;
import com.gmv.pre.definitions.DatabaseDefinitions;
import com.gmv.pre.definitions.FieldDefinitions;
import com.gmv.pre.definitions.QueryDefaults;
import com.gmv.pre.helpers.ZipToCoord;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

@Path("/q")
public class Queries {
	
	private Document createAggregateGroup () {
		Document aggregateGroup = new Document ();
		Document providerDeets = new Document ("_id", "$Bundle Info");
		providerDeets.append("Available Times", new Document ("$push", "$Available Timeslots"));
		aggregateGroup.append("$group", providerDeets);
		return aggregateGroup;
	}
	
	private Document createAggregateUnwind () {
		Document aggregateUnwind = new Document ("$unwind", "$Available Timeslots");
		return aggregateUnwind;
	}
	
	private Document createGeoNear (int zip, long maxDistance) {
		ZipToCoord ztc = new ZipToCoord (zip);
		ztc.getCoordsForZip ();

		// construct geoNear
		BasicDBList dblList = new BasicDBList ();
		dblList.add(ztc.lon);
		dblList.add(ztc.lat);
		Document geom = new Document ("type", "Point");
		geom.append("coordinates", dblList);
		Document near = new Document ("near", geom);
		near.append("distanceField", "dist");
		near.append("maxDistance", maxDistance);
		near.append("includeLocs", "Bundle Info.Provider Location");
		near.append("spherical", true);

		return near;
	}
	
	private Document create2dQuery (int zip, long distance) {
		BasicDBList dblList = new BasicDBList ();
		ZipToCoord ztc = new ZipToCoord (zip);
		ztc.getCoordsForZip ();
		dblList.add(ztc.lon);
		dblList.add(ztc.lat);
		
		Document geom = new Document ("type", "Point");
		geom.append("coordinates", dblList);
		
		Document near = new Document ("$geometry", geom);
		near.append("$maxDistance", distance);
		
		Document nearObj = new Document ("$near", near);
		Document loc = new Document ("loc", nearObj);
		return loc;
	}
	
	private Document constructQuery (int zip, 
									 long maxDistance, 
									 String bundle, 
									 double crank,
									 int numOffices,
									 int numProviders,
									 double cancelPenalty,
									 double minPrice,
									 double maxPrice,
									 String catOrDog,
									 String startDay,
									 String endDay,
									 String preferredTime,
									 String name,
									 boolean finacereq,
									 boolean promo,
									 double age,
									 double weight,
									 String gender,
									 String breed) {
		Document queryDoc = new Document ();
		queryDoc.append("Bundle Info.Ranking.Composite Ranking", new BasicDBObject("$gt", crank));
		queryDoc.append("Bundle Info.Number Offices", new BasicDBObject("$lte", numOffices));
		queryDoc.append("Bundle Info.Number Providers", new BasicDBObject("$lte", numProviders));
		queryDoc.append("Bundle Info.Cancel Penalty", new BasicDBObject("$lte", cancelPenalty));
		queryDoc.append("Available Timeslots.Promo Price", new BasicDBObject("$lte", maxPrice).append("$gte", minPrice));
		queryDoc.append("Bundle Info.Financing Available", finacereq);
		if (promo)
			queryDoc.append("Bundle Info.Promo Available", promo);

		Calendar cal = Calendar.getInstance();

	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date st_date;
		Date e_date;
		boolean defaultDate = false;
		try {
			st_date = sdf.parse(startDay);
			e_date = sdf.parse(endDay);
		} catch (Exception e) {
			defaultDate = true;
			st_date = new Date ();
			e_date = new Date();
		}
		
		Date startDate = st_date;
		Date endDate = e_date;

		cal.setTime(st_date);
		if (preferredTime != null && !preferredTime.isEmpty()) {
			if (preferredTime.contains("morning")) {
				cal.add(Calendar.HOUR_OF_DAY, 9);
				startDate = cal.getTime();
				cal.add(Calendar.HOUR_OF_DAY, 3);
				endDate = cal.getTime();
			} else if (preferredTime.contains("afternoon")) {
				cal.add(Calendar.HOUR_OF_DAY, 12);
				startDate = cal.getTime();
				cal.add(Calendar.HOUR_OF_DAY, 3);
				endDate = cal.getTime();
			} else if (preferredTime.contains("evening")) {
				cal.add(Calendar.HOUR_OF_DAY, 15);
				startDate = cal.getTime();
				cal.add(Calendar.HOUR_OF_DAY, 3);
				endDate = cal.getTime();
			} else {
				cal.add(Calendar.HOUR_OF_DAY, 9);
				startDate = cal.getTime();
				cal.add(Calendar.HOUR_OF_DAY, 9);
				endDate = cal.getTime();
			}
		}
		if (startDay != null && startDay != null && !startDay.isEmpty()) {
			queryDoc.append("Available Timeslots.Time", new BasicDBObject("$gte", startDate).
													append("$lte", endDate));
		}
		queryDoc.append("Available Timeslots.Available", true);
		
		//type requirement
		ArrayList<String> typeMatches = new ArrayList<String>();
		typeMatches.add("all");
		typeMatches.add("any");
		typeMatches.add(catOrDog.toLowerCase());
		queryDoc.append("Bundle Info.Template.Applies To.Type", new BasicDBObject("$in", typeMatches));
		
		//age requirement
		queryDoc.append("Bundle Info.Template.Applies To.Min age", new BasicDBObject("$lte", age));
		queryDoc.append("Bundle Info.Template.Applies To.Max age", new BasicDBObject("$gte", age));
		
		//weight requirement
		queryDoc.append("Bundle Info.Template.Applies To.Min Weight(lbs)", new BasicDBObject("$lte", weight));
		queryDoc.append("Bundle Info.Template.Applies To.Max Weight(lbs)", new BasicDBObject("$gte", weight));
		
		//gender requirement
		if (gender != null && !gender.isEmpty()) {
			ArrayList<String> genderMatches = new ArrayList<String>();
			genderMatches.add("all");
			genderMatches.add("any");
			genderMatches.add(gender.toLowerCase());
			queryDoc.append("Bundle Info.Template.Applies To.Gender", new BasicDBObject("$in", genderMatches));
		}
		
		//breed requirement
		if (breed != null && !breed.isEmpty()) {
			ArrayList<String> breedMatches = new ArrayList<String>();
			breedMatches.add("all");
			breedMatches.add("any");
			breedMatches.add(breed);
			queryDoc.append("Bundle Info.Template.Applies To.Breed", new BasicDBObject("$in", breedMatches));
		}

		if (bundle != null && bundle != "") {
			// user wants a bundle, let's get that
			queryDoc.append("Bundle Info.Template.Name", new BasicDBObject("$regex", bundle).append("$options", "i"));
		}
		
		if (name != null && !name.isEmpty()) {
			queryDoc.append("Bundle Info.Provider Name", new BasicDBObject("$regex", name).append("$options", "i"));
		}
		
		return queryDoc;
	}

	@Path("/findNear")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response findNear (@QueryParam ("zip") int zip,
							  @QueryParam ("maxdist") long maxDistance,
							  @QueryParam ("bundle") String bundle,
							  @QueryParam ("crank") double rank,
							  @QueryParam("numoffices") int numOffices,
							  @QueryParam("numproviders") int numProviders,
							  @QueryParam("cancelpenalty")double cancelPenalty,
							  @QueryParam("maxprice")double maxprice,
							  @QueryParam("minprice")double minprice,
							  @QueryParam("for")String catOrDog,
							  @QueryParam("sortby")String sortby,
							  @QueryParam("startDate")String startDay,
							  @QueryParam("endDate") String endDay,
							  @QueryParam("timepref")String timePref,
							  @QueryParam("vetpref") String name,
							  @QueryParam("financereq") boolean finacereq,
							  @QueryParam("promo") boolean promo,
							  @QueryParam("age") double age,
							  @QueryParam("weight") double weight,
							  @QueryParam("breed") String breed,
							  @QueryParam("gender") String gender) {
		
		// default query parameters not provided
		Date startAPITime = new Date ();
		if (zip == 0) {zip = QueryDefaults.ZIP;}
		if (maxDistance == 0) {maxDistance = QueryDefaults.MAXDISTANCE;}
		if (numOffices == 0) {numOffices = QueryDefaults.NUM_OFFICES;}
		if (numProviders == 0) {numProviders = QueryDefaults.NUM_PROVIDERS;}
		if (cancelPenalty == 0) {cancelPenalty = QueryDefaults.CANCEL_PENALTY;}
		if (maxprice == 0) {maxprice = QueryDefaults.MAX_PRICE;}
		if (catOrDog == null || catOrDog.isEmpty()) {catOrDog = "dog";}
		if (sortby == null || sortby.isEmpty()) {sortby = "priceascending";}
		Document sortOrder = new Document ();
		if (sortby.equals("priceascending")) {
			sortOrder.append("Available Timeslots.Promo Price", 1);
		} else if (sortby.equals("pricedescending")) {
			sortOrder.append("Available Timeslots.Promo Price", -1);
		} else if (sortby.equals("wrankascending")) {
			sortOrder.append("Bundle Info.Ranking.Wait Time Ranking", 1);
		} else if (sortby.equals("wrankdescending")) {
			sortOrder.append("Bundle Info.Ranking.Wait Time Ranking", -1);
		} else if (sortby.equals("mrankascending")) {
			sortOrder.append("Bundle Info.Ranking.Bed Side Manner Ranking", 1);
		} else if (sortby.equals("mrankdescending")) {
			sortOrder.append("Bundle Info.Ranking.Bed Side Manner Ranking", -1);
		}
		sortOrder.append("When", 1);

		Document aggregateGroup = createAggregateGroup ();
		Document aggregateUnwind = createAggregateUnwind ();
		Document queryDoc = constructQuery (zip, maxDistance, bundle, rank, numOffices, numProviders, cancelPenalty, minprice, maxprice, catOrDog, startDay, endDay, timePref, name, finacereq, promo, age, weight, gender, breed);

		MongoInterface iface = new MongoInterface (DatabaseDefinitions.NOSQL_DB, DatabaseDefinitions.OFFERING_COLL);
		Document doc = iface.getQueryResults(createGeoNear(zip, maxDistance), queryDoc, aggregateGroup, aggregateUnwind, sortOrder);
		Document masterOut = new Document ("Your Search", doc);
		Document suggestedQueries = new Document ();

		//Relax price
		Document relaxPrice = constructQuery(zip, maxDistance, bundle, rank, numOffices, numProviders, cancelPenalty, minprice, maxprice*1.20, catOrDog, startDay, endDay, timePref, name, finacereq, promo, age, weight, gender, breed);
		long nRes = iface.getDocCount(relaxPrice);
		String sRes = nRes + " Matches";
		suggestedQueries.append("Rleax price (20% more)", sRes);

		// Relax Distance
		Document relaxDist = constructQuery(zip, maxDistance*5, bundle, rank, numOffices, numProviders, cancelPenalty, minprice, maxprice, catOrDog, startDay, endDay, timePref, name, finacereq, promo, age, weight, gender, breed);
		nRes = iface.getDocCount(relaxDist);
		sRes = nRes + " Matches";
		suggestedQueries.append("Rleax distance (5x)", sRes);

		// Relax ranking
		Document relaxRank = constructQuery(zip, maxDistance, bundle, rank, numOffices, numProviders, cancelPenalty, minprice, maxprice, catOrDog, startDay, endDay, timePref, name, finacereq, promo, age, weight, gender, breed);
		nRes = iface.getDocCount(relaxRank);
		sRes = nRes + " Matches";
		suggestedQueries.append("Rleax Rank (1 * lower)", sRes);

		// Relax number of offices
		Document relaxOffices = constructQuery(zip, maxDistance, bundle, rank, numOffices + 1, numProviders, cancelPenalty, minprice, maxprice, catOrDog, startDay, endDay, timePref, name, finacereq, promo, age, weight, gender, breed);
		nRes = iface.getDocCount(relaxOffices);
		sRes = nRes + " Matches";
		suggestedQueries.append("Rleax number of offices (+1 office)", sRes);
		
		// Relax date and time
		Document relaxDate = constructQuery(zip, maxDistance, bundle, rank, numOffices, numProviders, cancelPenalty, minprice, maxprice, catOrDog, "", "", "", name, finacereq, promo, age, weight, gender, breed);
		nRes = iface.getDocCount(relaxDate);
		sRes = nRes + " Matches";
		suggestedQueries.append("Rleax Date (next 5 days)", sRes);
		masterOut.append("Recommended Searches", suggestedQueries);
		
		Date finishAPITime = new Date ();
		double timeTaken = finishAPITime.getTime() - startAPITime.getTime();
		
		masterOut.append("Time Taken", timeTaken);
		return Response.status(200).entity(masterOut.toJson()).build();
	}
	
	@GET
	@Path ("/count")
	@Produces (MediaType.APPLICATION_JSON)
	public Response count (@QueryParam("table")String table) {
		Document doc = new Document();
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoCollection<Document> coll = client.getDatabase(DatabaseDefinitions.NOSQL_DB).getCollection(DatabaseDefinitions.BREED_COLL);
		doc.append("Breeds", coll.count());
		coll = client.getDatabase(DatabaseDefinitions.NOSQL_DB).getCollection(DatabaseDefinitions.PROCEDURE_COLL);
		doc.append("Procedures", coll.count());
		coll = client.getDatabase(DatabaseDefinitions.NOSQL_DB).getCollection(DatabaseDefinitions.PRAC_COLL);
		doc.append("Practices", coll.count());
		coll = client.getDatabase(DatabaseDefinitions.NOSQL_DB).getCollection(DatabaseDefinitions.OFFERING_COLL);
		doc.append("Offerings", coll.count());
		client.close();
		return Response.status(200).entity(doc.toJson()).build();
	}
	
	
	@GET
	@Produces ({MediaType.APPLICATION_JSON})
	@Path ("/findProviders")
	public Response findProviders (@QueryParam ("zip") int zip,
									@QueryParam("dist") long dist,
									@QueryParam("for") String animal,
									@QueryParam("rank") double rank) {
		
		/* Construct a query like this - 
		 * find(
		 * 		{"loc" : {
		 * 					$near : {
		 * 							$geometry : {
		 * 										"type" : "Point", 
		 * 										"coordinates" : [lon, lat] 
		 * 										}, 
		 * 							$maxDistance : 500
		 * 							}
		 * 				 }
		 * 		}
		 * new Document ("loc",
		 * 					new Document ("$near",
		 * 									new Document ("$geometry",
		 * 													new Document ("type", "Point").
		 * 													append("coordinates", new BasicDBList ())
		 * 												).append("$maxDistance", 500)
		 * 									)
		 * 				)
		 * )*/
		Date start = new Date();

		//Convert zip to coordinates
		ZipToCoord ztc = new ZipToCoord (zip);
		ztc.getCoordsForZip ();
		BasicDBList dblList = new BasicDBList ();
		dblList.add(ztc.lon);
		dblList.add(ztc.lat);
		
		Document toFind = new Document ("Location" , new Document ("$near" , new Document ("$geometry" , new Document ("type" , "Point").append("coordinates" , dblList)).append("$maxDistance" , dist)));
		if (animal != null && !animal.isEmpty()) {
			BasicDBList animalsTreated = new BasicDBList();
			animalsTreated.add("all");
			animalsTreated.add("any");
			animalsTreated.add(animal);
			toFind.append("Animals Treated", new Document("$in", animalsTreated));
		}
		toFind.append("Ranking.Composite Ranking", new Document("$gt", rank));
		MongoClient client = new MongoClient();
		MongoCollection<Document> coll = client.getDatabase("gmv_no_sql").getCollection("practices");
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
		client.close();
		return Response.status(200).entity(queryOut.toJson()).build();
	}
	
	@GET
	@Produces ({MediaType.APPLICATION_JSON})
	@Path ("/findBundles")
	public Response findBundles (@QueryParam ("zip") int zip,
									@QueryParam("dist") long dist,
									@QueryParam("for") String animal,
									@QueryParam("bundle") String bundle,
									@QueryParam("wrank") double wrank,
									@QueryParam("mrank") double mrank,
									@QueryParam("crank") double crank,
									@QueryParam("minprice") double minprice,
									@QueryParam("maxprice") double maxprice,
									@QueryParam("numoffices") int numoffices,
									@QueryParam("numproviders") int numproviders,
									@QueryParam("financereq") boolean financereq,
									@QueryParam("promo") boolean promo,
									@QueryParam("cancelpolicy") String cancelpolicy,
									@QueryParam("vetpref") String vetpref,
									@QueryParam("age") double age,
									@QueryParam("weight") double weight,
									@QueryParam("gender") String gender,
									@QueryParam("breed") String breed,
									@QueryParam("start") String sday,
									@QueryParam("end") String eday,
									@QueryParam("time") String time) {
		
		/* Construct a query like this - 
		 * find(
		 * 		{"loc" : {
		 * 					$near : {
		 * 							$geometry : {
		 * 										"type" : "Point", 
		 * 										"coordinates" : [lon, lat] 
		 * 										}, 
		 * 							$maxDistance : 500
		 * 							}
		 * 				 }
		 * 		}
		 * new Document ("loc",
		 * 					new Document ("$near",
		 * 									new Document ("$geometry",
		 * 													new Document ("type", "Point").
		 * 													append("coordinates", new BasicDBList ())
		 * 												).append("$maxDistance", 500)
		 * 									)
		 * 				)
		 * )*/
		Date start = new Date();

		//Convert zip to coordinates
		ZipToCoord ztc = new ZipToCoord (zip);
		ztc.getCoordsForZip ();
		BasicDBList dblList = new BasicDBList ();
		dblList.add(ztc.lon);
		dblList.add(ztc.lat);
		
		Document toFind = new Document (FieldDefinitions.RENDERING_LOC, new Document ("$near" , new Document ("$geometry" , new Document ("type" , "Point").append("coordinates" , dblList)).append("$maxDistance" , dist)));

		if (animal != null && !animal.isEmpty()) {
			BasicDBList animalsTreated = new BasicDBList();
			animalsTreated.add("all");
			animalsTreated.add("any");
			animalsTreated.add(animal);
			toFind.append("Applies To.Type", new Document("$in", animalsTreated));
		}
		
		//age requirement
		if (age > 0) {
			toFind.append("Applies To.Min age", new BasicDBObject("$lte", age));
			toFind.append("Applies To.Max age", new BasicDBObject("$gte", age));
		}
		
		//weight requirement
		if (weight > 0) {
			toFind.append("Applies To.Min Weight(lbs)", new BasicDBObject("$lte", weight));
			toFind.append("Applies To.Max Weight(lbs)", new BasicDBObject("$gte", weight));
		}
		
		//gender
		if (gender != null && !gender.isEmpty()) {
			BasicDBList genderList = new BasicDBList();
			genderList.add("all");
			genderList.add("any");
			genderList.add(gender);
			toFind.append("Applies To.Gender", new Document ("$in", genderList)); 
		}
		
		//breed
		if (breed != null && !breed.isEmpty()) {
			BasicDBList breedList = new BasicDBList();
			breedList.add("all");
			breedList.add("any");
			breedList.add(breed);
			toFind.append("Applies To.Breed", new Document ("$in", breedList)); 
		}

		//default all query parameters
		// bundle
		if (bundle == null || bundle.isEmpty()) {
			bundle = "TPLO";
		}
		toFind.append("Template Name", new BasicDBObject("$regex", bundle).append("$options", "i"));
		
		// ranking
		String rankField = FieldDefinitions.PROVIDER_RANK + ".";
		toFind.append(rankField + FieldDefinitions.COMPOSITE_RANKING, new Document("$gte", crank));
		toFind.append(rankField + FieldDefinitions.WAIT_TIME_RANKING, new Document("$gte", wrank));
		toFind.append(rankField + FieldDefinitions.BED_SIDE_MANNERS_RANKING, new Document("$gte", mrank));

		// price
		if (maxprice < 0.1 || maxprice < minprice) {
			maxprice = 100000;
		}
		toFind.append(FieldDefinitions.BASE_PRICE, new Document("$gte", minprice).append("$lte", maxprice));
		
		// number of offices
		if (numoffices < 1) {
			numoffices = 1000;
		}
		toFind.append(FieldDefinitions.NUM_OFFICES, new Document ("$lte", numoffices));
		
		// number of providers
		if (numproviders < 1) {
			numproviders = 1000;
		}
		toFind.append(FieldDefinitions.NUM_PROVIDERS, new Document ("$lte", numproviders));
		
		// finance requirement
		if (financereq) {
			toFind.append(FieldDefinitions.FINANCING_AVAILABLE, financereq);
		}
		
		// promo available
		if (promo) {
			toFind.append(FieldDefinitions.PROMO_AVAILABLE, promo);
		}

		// cancellation policy
		if (cancelpolicy != null && !cancelpolicy.isEmpty()) {
			if (cancelpolicy.equals("Strict")) {
				toFind.append(FieldDefinitions.CANCEL_PENALTY, new BasicDBObject("$lte", QueryDefaults.STRICT_CANCEL_PENALTY));
			} else if (cancelpolicy.equals("Moderate")) {
				toFind.append(FieldDefinitions.CANCEL_PENALTY, new BasicDBObject("$lte", QueryDefaults.MODERATE_CANCEL_PENALTY));
			} else if (cancelpolicy.equals("Flexible")) {
				toFind.append(FieldDefinitions.CANCEL_PENALTY, new BasicDBObject("$lte", QueryDefaults.FLEXIBLE_CANCEL_PENALTY));
			}
		}

		// preferred vet
		if (vetpref != null && !vetpref.isEmpty()) {
			toFind.append(FieldDefinitions.PROVIDER_NAME, new BasicDBObject("$regex", vetpref).append("$options", "i"));
		}
		
		// time pref
		boolean useday = false;
		Date startDay = null;
		Date endDay = null;
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (sday != null && !sday.isEmpty()) {
			try {
				startDay = sdf.parse(sday);
				if (eday != null && !eday.isEmpty()) {
					endDay = sdf.parse(eday);
				} else {
					Calendar cal = Calendar.getInstance();
					cal.setTime(startDay);
					cal.add(Calendar.DAY_OF_MONTH, 5);
					endDay = cal.getTime();
				}
				useday = true;
			} catch (Exception e) {
			}
		}
		
		if (useday) {
			toFind.append("Available Timeslots.Time", new BasicDBObject ("$gte", startDay).append("$lte", endDay));
			toFind.append("Available Timeslots.Available", true);
		}

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
		queryOut.append("Query Run", toFind.toJson());
		client.close();
		return Response.status(200).entity(queryOut.toJson()).build();
	}

}
