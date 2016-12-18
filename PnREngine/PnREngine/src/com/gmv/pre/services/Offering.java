package com.gmv.pre.services;

import java.util.Iterator;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;

import com.gmv.pre.db.mongo.MongoInterface;
import com.gmv.pre.definitions.DatabaseDefinitions;
import com.gmv.pre.definitions.FieldDefinitions;
import com.gmv.pre.helpers.ZipToCoord;
import com.gmv.pre.structs.OfferingDoc;
import com.gmv.pre.structs.PartDoc;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;

@Path("/offering")
public class Offering {
	public static String gold1 = "{ \"OfferingID\" : \"PROC_001_GOLD\", \"Core\" : \"GMV_PROC_001\", \"Upstream Additions\" : [{\"ProcID\" : \"GMV_PROC_002\", \"Rendered By\" : \"GMV_PRACTICE_20\", \"Rendered At\" : \"GMV_PRACTICE_1\"}, {\"ProcID\" : \"GMV_PROC_003\", \"Rendered By\" : \"GMV_PRACTICE_15\", \"Rendered At\" : \"GMV_PRACTICE_0\"}], \"Base Price\" : 100.95, \"Promo Price\" : {\"Weekdays\" : 80.95}}";
	public static String silver1 = "{ \"OfferingID\" : \"PROC_001_SILVER\", \"Core\" : \"GMV_PROC_001\", \"Upstream Additions\" : [{\"ProcID\" : \"GMV_PROC_002\", \"Rendered By\" : \"GMV_PRACTICE_20\", \"Rendered At\" : \"GMV_PRACTICE_1\"}, {\"ProcID\" : \"GMV_PROC_003\", \"Rendered By\" : \"GMV_PRACTICE_15\", \"Rendered At\" : \"GMV_PRACTICE_0\"}], \"Base Price\" : 100.95, \"Promo Price\" : {\"Weekdays\" : 80.95}}";

	public static String gold2 = "{ \"OfferingID\" : \"PROC_002_GOLD\", \"Core\" : \"GMV_PROC_002\", \"Upstream Additions\" : [{\"ProcID\" : \"GMV_PROC_003\", \"Rendered By\" : \"GMV_PRACTICE_30\", \"Rendered At\" : \"GMV_PRACTICE_1\"}, {\"ProcID\" : \"GMV_PROC_003\", \"Rendered By\" : \"GMV_PRACTICE_15\", \"Rendered At\" : \"GMV_PRACTICE_0\"}], \"Base Price\" : 100.95, \"Promo Price\" : {\"Weekdays\" : 80.95}}";
	public static String silver2 = "{ \"OfferingID\" : \"PROC_002_SILVER\", \"Core\" : \"GMV_PROC_002\", \"Upstream Additions\" : [{\"ProcID\" : \"GMV_PROC_002\", \"Rendered By\" : \"GMV_PRACTICE_14\", \"Rendered At\" : \"GMV_PRACTICE_1\"}, {\"ProcID\" : \"GMV_PROC_003\", \"Rendered By\" : \"GMV_PRACTICE_15\", \"Rendered At\" : \"GMV_PRACTICE_0\"}], \"Base Price\" : 100.95, \"Promo Price\" : {\"Weekdays\" : 80.95}}";
	
	@Path ("/get")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getOffering (@QueryParam("uid") String id) {
		if (id== null || id.isEmpty())
			return Response.status(500).entity("Missing uid").build();
		
		OfferingDoc od = new OfferingDoc ();
		od.Read(id);
		return Response.status(200).entity(od.toJSON()).build();
	}
	
	@Path("/aeddRandomOfferingToPractices")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response aeddRandomOfferingToPractices (@QueryParam("zip") int zip,
										@QueryParam("maxdist") int maxDistance) {

		Document goldOffering1 = Document.parse(gold1);
		Document silverOffering1 = Document.parse(silver1);
		Document goldOffering2 = Document.parse(gold2);
		Document silverOffering2 = Document.parse(silver2);

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
		Document loc = new Document ("loc", nearObj);

		MongoInterface iface = new MongoInterface (DatabaseDefinitions.NOSQL_DB, DatabaseDefinitions.PRAC_COLL);
		FindIterable<Document> iterable = iface.coll.find(loc);
		Iterator<Document> iter = iterable.iterator ();
		while (iter.hasNext()) {
			Document doc = iter.next ();
			
			if (Math.random() > 0.5) {
				BasicDBObject update = new BasicDBObject ("$push", new BasicDBObject("Offering", goldOffering1));
				BasicDBObject match = new BasicDBObject (FieldDefinitions.UNIQUE_ID, doc.get(FieldDefinitions.UNIQUE_ID));
				iface.coll.updateOne (match, update);
				update = new BasicDBObject ("$push", new BasicDBObject("Offering", silverOffering1));
				iface.coll.updateOne (match, update);
			} else {
				BasicDBObject update = new BasicDBObject ("$push", new BasicDBObject("Offering", goldOffering2));
				BasicDBObject match = new BasicDBObject (FieldDefinitions.UNIQUE_ID, doc.get(FieldDefinitions.UNIQUE_ID));
				iface.coll.updateOne (match, update);
				update = new BasicDBObject ("$push", new BasicDBObject("Offering", silverOffering2));
				iface.coll.updateOne (match, update);
			}
		}

		return Response.status(200).entity("").build();
	}

	@Path("/add")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public Response addOffering (@QueryParam ("pracID") String practiceID, String inJSON) {
		// See if pracID is present in the db. Else throw an error
		MongoInterface iface = new MongoInterface (DatabaseDefinitions.NOSQL_DB, DatabaseDefinitions.PRAC_COLL);
		if (!iface.exists(FieldDefinitions.UNIQUE_ID, practiceID)) {
			return Response.status(500).entity("Practice does not exist").build();
		}
		
		//Convert inJSON to JSONObject
		Document doc = Document.parse(inJSON);
		BasicDBObject update = new BasicDBObject ("$push", new BasicDBObject("Offering", doc));
		BasicDBObject match = new BasicDBObject (FieldDefinitions.UNIQUE_ID, practiceID);
		iface.coll.updateOne (match, update);
		return Response.status(200).entity("Added Offering").build();
	}
}
