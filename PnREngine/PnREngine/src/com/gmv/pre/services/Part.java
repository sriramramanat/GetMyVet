package com.gmv.pre.services;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;

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
import com.gmv.pre.structs.PartDoc;
import com.gmv.pre.structs.PetDoc;
import com.gmv.pre.structs.ProcedureDoc;

@Path ("/part")
public class Part {
	@Path ("/add")
	@POST
	public Response add (@QueryParam("uid") String id) {
		if (id== null || id.isEmpty())
			return Response.status(500).entity("Missing uid").build();
		
		PartDoc pd = new PartDoc (id);
		pd.write();
		return Response.status(200).entity(id).build();
	}

	@Path ("/get")
	@GET
	@Produces ({MediaType.APPLICATION_JSON})
	public Response get (@QueryParam("uid") String id) {
		if (id== null || id.isEmpty())
			return Response.status(500).entity("Missing uid").build();
		
		PartDoc pd = new PartDoc (id);
		return Response.status(200).entity(pd.toJSON()).build();
	}

	@Path ("/getByName")
	@GET
	@Produces (MediaType.APPLICATION_JSON)
	public Response getByName (@QueryParam("name") String name) {
		PartDoc pd = new PartDoc();
		return Response.status(200).entity(pd.getAllByName(name)).build();
	}
}
