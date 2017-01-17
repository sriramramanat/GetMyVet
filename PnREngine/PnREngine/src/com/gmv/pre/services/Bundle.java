package com.gmv.pre.services;

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
import com.gmv.pre.structs.BundleDoc;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

@Path("/bundle")
public class Bundle {

	@Path("/get")
	@Produces({MediaType.APPLICATION_JSON})
	@GET
	
	public Response getBundle (@QueryParam("id") String id) {
		BundleDoc bd = BundleDoc.readBundle(id);
		return Response.status(200).entity(bd.toJSON()).build();
	}
	
}
