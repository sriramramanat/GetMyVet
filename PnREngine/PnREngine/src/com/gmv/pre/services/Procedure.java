package com.gmv.pre.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;

import com.gmv.pre.definitions.DatabaseDefinitions;
import com.gmv.pre.helpers.ImportDatabase;
import com.gmv.pre.structs.BreedDoc;
import com.gmv.pre.structs.ProcedureDoc;

@Path ("/procedure")
public class Procedure {
	@Path ("/get")
	@GET
	@Produces (MediaType.APPLICATION_JSON)
	public Response get (@QueryParam("uid") String id) {
		ProcedureDoc proc = new ProcedureDoc (id);
		return Response.status(200).entity(proc.toJSON()).build();
	}

	@Path ("/getAll")
	@GET
	@Produces (MediaType.APPLICATION_JSON)
	public Response get () {
		ArrayList<Document> all = ProcedureDoc.getAll();
		Document d = new Document ("Procedures", all);
		return Response.status(200).entity(d.toJson()).build();
	}

	@Path ("/getCount")
	@GET
	@Produces (MediaType.APPLICATION_JSON)
	public Response getCount () {
		Document doc = ProcedureDoc.getCount();
		return Response.status(200).entity(doc.toJson()).build();
	}

	@Path ("/getPopular")
	@GET
	@Produces (MediaType.APPLICATION_JSON)
	public Response getPopular (@QueryParam("limit") int limit) {
		if (limit <= 0) {limit = 20;} //default limit
		ArrayList<Document> docs = ProcedureDoc.getTopSelling(limit);
		Document ret = new Document ("Popular", docs);
		return Response.status(200).entity(ret.toJson()).build();
	}

	@Path ("/getByName")
	@GET
	@Produces (MediaType.APPLICATION_JSON)
	public Response getByName (@QueryParam("name") String name) {
		ProcedureDoc pd = new ProcedureDoc();
		return Response.status(200).entity(pd.getAllByName(name)).build();
	}

	@Path ("/getName")
	@GET
	public Response getName (@QueryParam("uid") String id) {
		String name = "";
		ProcedureDoc proc = new ProcedureDoc (id);
		name = proc.getName();
		return Response.status(200).entity(name).build();
	}
	
	@Path ("/setName")
	@POST
	public Response setName (@QueryParam ("uid") String id,
							 @QueryParam ("name") String name) {
		String msg = "Set name to " + name;
		ProcedureDoc procedure = new ProcedureDoc (id);
		procedure.setName(name);
		procedure.write();
		return Response.status(200).entity(msg).build();
	}

	@Path ("/getType")
	@GET
	public Response getType (@QueryParam("uid") String id) {
		String type = "";
		ProcedureDoc proc = new ProcedureDoc (id);
		type = proc.getType();
		return Response.status(200).entity(type).build();
	}

	@Path ("/setType")
	@POST
	public Response setType (@QueryParam ("uid") String id,
							 @QueryParam ("type") String type) {
		String msg = "Set Type to " + type;
		ProcedureDoc procedure = new ProcedureDoc (id);
		procedure.setType(type);
		procedure.write();
		return Response.status(200).entity(msg).build();
	}
	
	@Path ("/getCategory")
	@GET
	public Response getCategory (@QueryParam("uid") String id) {
		String category = "";
		ProcedureDoc proc = new ProcedureDoc (id);
		category = proc.getCategory();
		return Response.status(200).entity(category).build();
	}

	@Path ("/setCategory")
	@POST
	public Response setCategory (@QueryParam ("uid") String id,
							 		@QueryParam ("category") String category) {
		String msg = "Set category to " + category;
		ProcedureDoc procedure = new ProcedureDoc (id);
		procedure.setCategory(category);
		procedure.write();
		return Response.status(200).entity(msg).build();
	}
	
	@Path ("/getDescription")
	@GET
	public Response getDescription (@QueryParam("uid") String id) {
		String Description = "";
		ProcedureDoc proc = new ProcedureDoc (id);
		Description = proc.getDescription();
		return Response.status(200).entity(Description).build();
	}

	@Path ("/setDescription")
	@POST
	public Response setDescription (@QueryParam ("uid") String id,
						 			@QueryParam ("desc") String Description) {
		String msg = "Set Description to " + Description;
		ProcedureDoc procedure = new ProcedureDoc (id);
		procedure.setDescription(Description);
		procedure.write();
		return Response.status(200).entity(msg).build();
	}

	@Path ("/getCore")
	@GET
	public Response getCore (@QueryParam("uid") String id) {
		String Core = "";
		ProcedureDoc proc = new ProcedureDoc (id);
		Core = proc.getCore();
		return Response.status(200).entity(Core).build();
	}

	@Path ("/setCore")
	@POST
	public Response setCore (@QueryParam ("uid") String id,
							 @QueryParam ("core") String Core) {
		String msg = "Set Core to " + Core;
		ProcedureDoc procedure = new ProcedureDoc (id);
		procedure.setCore(Core);
		procedure.write();
		return Response.status(200).entity(msg).build();
	}

	@Path ("/addAltCode")
	@POST
	public Response addAltCode (@QueryParam("uid") String id,
								@QueryParam("code") String code,
								@QueryParam("provider") String provider) {
		String msg = "Added alt Code " + code;
		ProcedureDoc Procedure = new ProcedureDoc (id);
		Procedure.addAltCode (code, provider);
		Procedure.write();
		return Response.status(200).entity(msg).build();
	}
	
	@Path ("/removeAltCode")
	@POST
	public Response removeAltCode (@QueryParam("uid") String id,
									@QueryParam("code") String code,
									@QueryParam("provider") String provider) {
		String msg = "Removed alt Code " + code;
		ProcedureDoc Procedure = new ProcedureDoc (id);
		Procedure.removeAltCode (code, provider);
		Procedure.write();
		return Response.status(200).entity(msg).build();
	}
	
	@Path ("/addInclusion")
	@POST
	public Response addInclusion (@QueryParam("uid") String id,
								@QueryParam("proc") String code,
								@QueryParam("mandatory") boolean mandatory,
								@QueryParam("referral") boolean referral,
								@QueryParam("day") String day,
								@QueryParam("order")int order,
								@QueryParam("canexit")boolean canExit){
		String msg = "Included procedure " + code;
		ProcedureDoc Procedure = new ProcedureDoc (id);
		Procedure.addInclusion (code, mandatory, referral, day, order, canExit);
		Procedure.write();
		return Response.status(200).entity(msg).build();
	}
	
	@Path ("/removeInclusion")
	@POST
	public Response removeInclusion (@QueryParam("uid") String id,
									@QueryParam("proc") String code) {
		String msg = "Included procedure " + code;
		ProcedureDoc Procedure = new ProcedureDoc (id);
		Procedure.removeInclusion (code);
		Procedure.write();
		return Response.status(200).entity(msg).build();
	}

	@Path ("/addPart")
	@POST
	public Response addPart (@QueryParam("uid") String id,
							 @QueryParam("partid") String part){
		String msg = "Included part " + part;
		ProcedureDoc Procedure = new ProcedureDoc (id);
		Procedure.addPart (part);
		Procedure.write();
		return Response.status(200).entity(msg).build();
	}

	@Path ("/removePart")
	@POST
	public Response removePart (@QueryParam("uid") String id,
								@QueryParam("partid") String part){
		String msg = "Remove part " + part;
		ProcedureDoc Procedure = new ProcedureDoc (id);
		Procedure.removePart (part);
		Procedure.write();
		return Response.status(200).entity(msg).build();
	}

	@Path ("/addIncludedPart")
	@POST
	public Response addPartUpgrade (@QueryParam("uid") String id,
							 		@QueryParam("partid") String part){
		String msg = "Included part upgrade " + part;
		ProcedureDoc Procedure = new ProcedureDoc (id);
		Procedure.addPartUpgrade (part);
		Procedure.write();
		return Response.status(200).entity(msg).build();
	}

	@Path ("/removeIncludedPart")
	@POST
	public Response removePartUpgrade (@QueryParam("uid") String id,
									   @QueryParam("partid") String part){
		String msg = "Remove part " + part;
		ProcedureDoc Procedure = new ProcedureDoc (id);
		Procedure.removePartUpgrade (part);
		Procedure.write();
		return Response.status(200).entity(msg).build();
	}
	
	@Path ("/getNumberSold")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getNumberSold (@QueryParam("id") String id) {
		int numSold = ProcedureDoc.getNumberSold (id);
		Document d = new Document ("NumSold", numSold);
		return Response.status(200).entity(d.toJson()).build();
	}

	@Path ("/getPriceStats")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getPricingStats (@QueryParam("id") String id) {
		Document priceStats = ProcedureDoc.getPriceStats (id);
		return Response.status(200).entity(priceStats.toJson()).build();
	}

	@Path ("/getAverageByRank")
	@GET
	@Produces (MediaType.APPLICATION_JSON)
	public Response getAverageByRank (@QueryParam("id") String id, 
									  @QueryParam("rank") double rank) {
		double d = ProcedureDoc.getAveragePriceForRank(id, rank);
		Document avg = new Document ("Average", d);
		return Response.status(200).entity(avg.toJson()).build();
	}

	@Path ("/getNearbyAverage")
	@GET
	@Produces (MediaType.APPLICATION_JSON)
	public Response getNearbyAverage (@QueryParam("id") String id, 
								      @QueryParam("lat") double lat,
								      @QueryParam("lon") double lon,
								      @QueryParam("dist") double dist) {
		ArrayList<Document> d = ProcedureDoc.getNearbyAverage(id, lat, lon, dist);
		Document avg = new Document ("Average", d);
		return Response.status(200).entity(avg.toJson()).build();
	}

	@Path ("/getNationalAverage")
	@GET
	@Produces (MediaType.APPLICATION_JSON)
	public Response getNationalAverage (@QueryParam("id") String id) {
		double d = ProcedureDoc.getNationalAverage(id);
		Document avg = new Document ("Average", d);
		return Response.status(200).entity(avg.toJson()).build();
	}

	@Path ("/getAverageForTopSelling")
	@GET
	@Produces (MediaType.APPLICATION_JSON)
	public Response getAverageForTopSelling (@QueryParam("id") String id, 
									  @QueryParam("top") int topHowMany) {
		double d = ProcedureDoc.getAveragePriceForTopSelling(id, topHowMany);
		Document avg = new Document ("Average", d);
		return Response.status(200).entity(avg.toJson()).build();
	}

	@Path ("/getVariant")
	@GET
	@Produces (MediaType.APPLICATION_JSON)
	public Response getVariant (@QueryParam("core") String core, 
							    @QueryParam("variant") int variant) {
		String variantID = ProcedureDoc.getVariant(core, variant);
		Document avg = new Document ("VariantID", variantID);
		return Response.status(200).entity(avg.toJson()).build();
	}

}
