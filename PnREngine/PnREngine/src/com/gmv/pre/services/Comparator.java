package com.gmv.pre.services;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;

import com.gmv.pre.structs.BundleDoc;

@Path ("/compare")
public class Comparator {
	@Path ("/bundles")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response compareBundles (@QueryParam("b1") String b1,
									@QueryParam("b2") String b2,
									@QueryParam("b3") String b3) {
		Document comparison = com.gmv.pre.core.Comparator.compareBundles(b1, b2, b3);
		return Response.status(200).entity(comparison.toJson()).build();
	}
}
