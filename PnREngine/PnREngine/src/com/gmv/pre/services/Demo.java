package com.gmv.pre.services;

import java.io.BufferedReader;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;


import com.gmv.pre.definitions.DatabaseDefinitions;
import com.gmv.pre.helpers.ImportDatabase;

@Path ("/demo")
public class Demo {
	private BufferedReader br;

	@Path ("/initDemo")
	@GET
	public Response initDemo (@QueryParam ("breed") String breedFile,
							  @QueryParam ("practice") String pracFile) {
		// init canineDB
		ImportDatabase idb = new ImportDatabase ();
		String userDir = System.getProperty("user.home");
		String breedFp = userDir + "/" + breedFile;
		String pracFp = userDir + "/" + pracFile;
		
		//idb.importDogBreedsToDB(breedFp, DatabaseDefinitions.NOSQL_DB, DatabaseDefinitions.BREED_COLL);
		idb.importPracticesToDB_new(pracFp, DatabaseDefinitions.NOSQL_DB, DatabaseDefinitions.PRAC_COLL);
		
		String msg = "Imported breeds from - " + breedFp + "\nPractices from - " + pracFp;
		return Response.status(200).entity(msg).build();
	}
	
	@Path ("/importBreedsNewJSONFormat")
	@GET
	public Response importBreedsNewJSONFormat (@QueryParam("breed") String breedFile) {
		String userDir = System.getProperty("user.home");
		String breedFp = userDir + "/" + breedFile;
		ImportDatabase idb = new ImportDatabase ();
		idb.importDogBreedsToDB_newFormat(breedFp, DatabaseDefinitions.NOSQL_DB, DatabaseDefinitions.BREED_COLL);

		return Response.status(200).entity("Imported breeds").build();
	}
}
