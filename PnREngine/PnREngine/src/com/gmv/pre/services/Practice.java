package com.gmv.pre.services;

import java.util.ArrayList;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;

import com.gmv.pre.structs.PracticeDoc;

@Path("/provider")
public class Practice {

	@Path("/get")
	@Produces({MediaType.APPLICATION_JSON})
	@GET
	public Response getPractice (@QueryParam("id") String id) {
		PracticeDoc pd = PracticeDoc.readPractice(id);
		return Response.status(200).entity(pd.toJSON()).build();
	}
	
	@Path("/getBundleCount")
	@Produces({MediaType.APPLICATION_JSON})
	@GET
	public Response getBundleCount (@QueryParam("id") String id) {
		Document d = PracticeDoc.getBundleCount(id);
		return Response.status(200).entity(d.toJson()).build();
	}

	@Path("/getBundles")
	@Produces({MediaType.APPLICATION_JSON})
	@GET
	public Response getBundles(@QueryParam("id") String id) {
		Document d = PracticeDoc.getBundles(id);
		return Response.status(200).entity(d.toJson()).build();
	}

	@Path("/getPartners")
	@Produces({MediaType.APPLICATION_JSON})
	@GET
	public Response getPartners(@QueryParam("id") String id) {
		PracticeDoc pd = PracticeDoc.readPractice(id);
		ArrayList<String> partners = pd.getPartners();
		Document d = new Document ("Partners", partners);
		return Response.status(200).entity(d.toJson()).build();
	}

	@Path("/whatCanBeOffered")
	@Produces({MediaType.APPLICATION_JSON})
	@GET
	public Response whatCanBeOffered(@QueryParam("id") String id) {
		Map<String, String> canBeOffered = PracticeDoc.whatElseCanBeOffered(id);
		Document ret = new Document ();
		ArrayList<Document> list = new ArrayList<Document>();
		for (Map.Entry<String, String> entry : canBeOffered.entrySet()) {
			Document d = new Document ("id", entry.getKey());
			d.append("name", entry.getValue());
			list.add(d);
		}
		ret.append("CanBeOffered", list);
		return Response.status(200).entity(ret.toJson()).build();
	}
}
