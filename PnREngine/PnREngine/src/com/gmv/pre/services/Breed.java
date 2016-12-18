package com.gmv.pre.services;

import java.util.ArrayList;
import java.util.Iterator;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;

import com.gmv.pre.definitions.DatabaseDefinitions;
import com.gmv.pre.definitions.FieldDefinitions;
import com.gmv.pre.structs.BreedDoc;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

@Path ("/breed")
public class Breed {
	@Path ("/get")
	@GET
	@Produces (MediaType.APPLICATION_JSON)
	public Response get (@QueryParam("uid") String id) {
		BreedDoc breed = new BreedDoc (id);
		return Response.status(200).entity(breed.toJSON()).build();
	}
	
	@Path("/getAll")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll () {
		String out = "[";
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoCollection<Document> coll = client.getDatabase(DatabaseDefinitions.NOSQL_DB)
				.getCollection(DatabaseDefinitions.BREED_COLL);
		FindIterable<Document> docs = coll.find();
		Iterator<Document> iter = docs.iterator ();
		while (iter.hasNext()) {
			Document doc = iter.next();
			out += doc.toJson();
			if (iter.hasNext()) {
				out += ",";
			}
		}
		out += "]";
		client.close();
		return Response.status(200).entity(out).build();
	}
	
	@Path ("/put")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response put (@QueryParam("uid") String id, String jsonStr) {
		Document parsedDoc = Document.parse(jsonStr);
		BreedDoc breed = new BreedDoc ();
		breed.fromDocument(parsedDoc);
		breed.Write();
		return Response.status(200).entity(breed.toJSON()).build();
	}

	@Path ("/getName")
	@GET
	public Response getName (@QueryParam("uid") String id) {
		String name = "";
		BreedDoc breed = new BreedDoc (id);
		name = breed.getName();
		return Response.status(200).entity(name).build();
	}

	@Path ("/setName")
	@POST
	public Response setName (@QueryParam("uid") String id,
							 @QueryParam("name") String name) {
		String msg = "Set name to " + name;
		BreedDoc breed = new BreedDoc (id);
		breed.setName(name);
		breed.Write();
		return Response.status(200).entity(msg).build();
	}
	
	@Path ("/setUniqueId")
	@POST
	public Response setId (@QueryParam("uid") String id,
							 @QueryParam("nid") String newID) {
		String msg = "Set Unique ID to " + newID;
		BreedDoc breed = new BreedDoc (id);
		breed.setID(newID);
		breed.Write();
		return Response.status(200).entity(msg).build();
	}

	@Path ("/getType")
	@GET
	public Response getType (@QueryParam("uid") String id) {
		String msg = "";
		BreedDoc breed = new BreedDoc (id);
		msg = breed.getType();
		return Response.status(200).entity(msg).build();
	}
	
	@Path ("/setType")
	@POST
	public Response setType (@QueryParam("uid") String id,
						 @QueryParam("type") String type) {
		String msg = "Set type to " + type;
		BreedDoc breed = new BreedDoc (id);
		breed.setType(type);
		breed.Write();
		return Response.status(200).entity(msg).build();
	}
	
	@Path ("/getCategory")
	@GET
	public Response getCategory (@QueryParam("uid") String id) {
		String msg = "";
		BreedDoc breed = new BreedDoc (id);
		msg = breed.getCategory();
		return Response.status(200).entity(msg).build();
	}
	
	@Path ("/setCategory")
	@POST
	public Response setCategory (@QueryParam("uid") String id,
								 @QueryParam("cat") String cat) {
		String msg = "Set category to " + cat;
		BreedDoc breed = new BreedDoc (id);
		breed.setCategory(cat);
		breed.Write();
		return Response.status(200).entity(msg).build();
	}
	
	@Path ("/getLifeExpectancy")
	@GET
	public Response getLifeExpectancy (@QueryParam("uid") String id) {
		String msg = "";
		BreedDoc breed = new BreedDoc (id);
		msg += breed.getLifeExpectancy();
		return Response.status(200).entity(msg).build();
	}
	
	@Path ("/setLifeExpectancy")
	@POST
	public Response setLifeExpectancy (@QueryParam("uid") String id,
										@QueryParam("lex") String lexp) {
		String msg = "Set life expectancy to " + lexp;
		try {
			Double life = Double.parseDouble(lexp);
			BreedDoc breed = new BreedDoc (id);
			breed.setLifeExpectancy(life);
			breed.Write();
		} catch (Exception e) {
			return Response.status(500).entity("lex is not a number").build();
		}
		return Response.status(200).entity(msg).build();
	}
	
	@Path ("/getPopularityRank")
	@GET
	public Response getPopularityRank (@QueryParam("uid") String id) {
		String msg = "";
		BreedDoc breed = new BreedDoc (id);
		msg += breed.getPopularityRank();
		return Response.status(200).entity(msg).build();
	}
	
	@Path ("/setPopularityRank")
	@POST
	public Response setPopularityRank (@QueryParam("uid") String id,
										@QueryParam("rank") int rank) {
		String msg = "Set popularity rank to " + rank;
		BreedDoc breed = new BreedDoc (id);
		breed.setPopularityRank(rank);
		breed.Write();
		return Response.status(200).entity(msg).build();
	}

	@Path ("/getSizeCategory")
	@GET
	public Response getSizeCategory  (@QueryParam("uid") String id) {
		String msg = "";
		BreedDoc breed = new BreedDoc (id);
		msg = breed.getSizeCategory();
		return Response.status(200).entity(msg).build();
	}
	
	@Path ("/setSizeCategory")
	@POST
	public Response setSizeCategory (@QueryParam("uid") String id,
									@QueryParam("size") String size) {
		String msg = "Set size category to " + size;
		BreedDoc breed = new BreedDoc (id);
		breed.setSizeCategory (size);
		breed.Write();
		return Response.status(200).entity(msg).build();
	}
	
	@Path ("/getLifetimeCost")
	@GET
	public Response getLifetimeCost  (@QueryParam("uid") String id) {
		String msg = "";
		BreedDoc breed = new BreedDoc (id);
		msg += breed.getLifetimeCost();
		return Response.status(200).entity(msg).build();
	}
	
	@Path ("/setLifetimeCost")
	@POST
	public Response setLifetimeCost (@QueryParam("uid") String id,
									@QueryParam("cost") String cost) {
		String msg = "Set lifetime cost to " + cost;
		try {
			Double dcost = Double.parseDouble(cost);
			BreedDoc breed = new BreedDoc (id);
			breed.setLifetimeCost(dcost);
			breed.Write();
		} catch (Exception e) {
			return Response.status(500).entity("cost is not a number").build();
		}
		return Response.status(200).entity(msg).build();
	}
	
	@Path ("/getAltCode")
	@GET
	public Response getAltCode (@QueryParam("uid") String id) {
		BreedDoc breed = new BreedDoc (id);
		ArrayList<Document> altCodes = breed.getAltCode();
		String msg = "[";
		int count = altCodes.size();
		for (int i = 0; i < count; i++) {
			Document altCode = altCodes.get(i);
			msg = msg + altCode.toJson();
			if (i < count)
				msg = msg + ",";
		}
		msg += "]";
		return Response.status(200).entity(msg).build();
	}

	@Path ("/addAltCode")
	@POST
	public Response addAltCode (@QueryParam("uid") String id,
								@QueryParam("code") String code,
								@QueryParam("provider") String provider) {
		String msg = "Added alt Code " + code;
		BreedDoc breed = new BreedDoc (id);
		breed.addAltCode (code, provider);
		breed.Write();
		return Response.status(200).entity(msg).build();
	}
	
	@Path ("/removeAltCode")
	@POST
	public Response removeAltCode (@QueryParam("uid") String id,
									@QueryParam("code") String code,
									@QueryParam("provider") String provider) {
		String msg = "Removed alt Code " + code;
		BreedDoc breed = new BreedDoc (id);
		breed.removeAltCode (code, provider);
		breed.Write();
		return Response.status(200).entity(msg).build();
	}

	@Path ("/getCongenitalIssues")
	@GET
	public Response getCongenitalIssues (@QueryParam("uid") String id) {
		BreedDoc breed = new BreedDoc (id);
		ArrayList<Document> issues = breed.getCongenitalIssues();
		String msg = "[";
		int count = issues.size();
		for (int i = 0; i < count; i++) {
			Document issue = issues.get(i);
			msg = msg + issue.toJson();
			if (i < count) {
				msg = msg + ",";
			}
		}
		msg += "]";
		return Response.status(200).entity(msg).build();
	}

	@Path ("/addCongenitalIssue")
	@POST
	public Response addCongenitalIssue (@QueryParam("uid") String id,
								        @QueryParam("area") String area,
								        @QueryParam("issue") String issue) {
		String msg = "Added issue " + issue;
		BreedDoc breed = new BreedDoc (id);
		breed.addCongenitalIssue (area, issue);
		breed.Write();
		return Response.status(200).entity(msg).build();
	}
	
	@Path ("/removeCongenitalIssue")
	@POST
	public Response removeCongenitalIssue (@QueryParam("uid") String id,
											@QueryParam("area") String area,
											@QueryParam("issue") String issue) {
		String msg = "Removed issue " + issue;
		BreedDoc breed = new BreedDoc (id);
		breed.removeCongenitalIssue (area, issue);
		breed.Write();
		return Response.status(200).entity(msg).build();
	}
}
