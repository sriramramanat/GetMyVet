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
import com.gmv.pre.definitions.ServerDefinitions;
import com.gmv.pre.structs.BreedDoc;
import com.gmv.pre.structs.PartDoc;
import com.gmv.pre.structs.ProcedureDoc;
import com.gmv.pre.structs.UserDefDoc;
import com.gmv.pre.structs.PetDoc;

@Path ("/init")
public class Init {
	@Path ("/initDBVars")
	@POST
	public Response initDBVars (@QueryParam ("db") String db,
								@QueryParam ("breed_coll") String breed_coll,
								@QueryParam ("procedure_coll") String proc_coll,
								@QueryParam ("pet_coll") String pet_coll,
								@QueryParam ("practice_coll") String prac_coll,
								@QueryParam ("owner_coll") String owner_coll,
								@QueryParam ("part_coll") String part_coll,
								@QueryParam ("server_conf") String server_conf) {
		
		if (db == null || db.isEmpty()) {
			db = DatabaseDefinitions.NOSQL_DB;
		}
		if (breed_coll == null || breed_coll.isEmpty()) {
			breed_coll = DatabaseDefinitions.BREED_COLL;
		}
		if (prac_coll == null || prac_coll.isEmpty()) {
			prac_coll = DatabaseDefinitions.PRAC_COLL;
		}
		if (proc_coll == null || proc_coll.isEmpty()) {
			proc_coll = DatabaseDefinitions.PROCEDURE_COLL;
		}
		if (pet_coll == null || pet_coll.isEmpty()) {
			pet_coll = DatabaseDefinitions.PET_COLL;
		}
		if (owner_coll == null || owner_coll.isEmpty()) {
			owner_coll = DatabaseDefinitions.OWNER_COLL;
		}
		if (part_coll == null || part_coll.isEmpty()) {
			part_coll = DatabaseDefinitions.PART_COLL;
		}
		if (server_conf == null || server_conf.isEmpty()) {
			server_conf = DatabaseDefinitions.DEFAULT_SERVER_CONF_FILE;
		}
		
		DatabaseDefinitions.readServerConf(server_conf);
		// Begin initializing variables
		BreedDoc.initDBVariables(db, breed_coll);
		ProcedureDoc.initDBVariables(db, proc_coll);
		PetDoc.initDBVariables(db, pet_coll, breed_coll, owner_coll);
		PartDoc.initDBVariables(db, part_coll);
		String msg = "Initialized database variables";
		return Response.status(200).entity(msg).build();
	}

	@Path ("/initDBVars2")
	@POST
	public Response initDBVars2 (@QueryParam ("db") String db,
								@QueryParam ("breed_coll") String breed_coll,
								@QueryParam ("procedure_coll") String proc_coll,
								@QueryParam ("pet_coll") String pet_coll,
								@QueryParam ("practice_coll") String prac_coll,
								@QueryParam ("owner_coll") String owner_coll,
								@QueryParam ("part_coll") String part_coll,
								@QueryParam ("server_conf") String server_conf) {
		
		if (db == null || db.isEmpty()) {
			db = "gmv_no_sql";
		}
		if (breed_coll == null || breed_coll.isEmpty()) {
			breed_coll = DatabaseDefinitions.BREED_COLL;
		}
		if (prac_coll == null || prac_coll.isEmpty()) {
			prac_coll = DatabaseDefinitions.PRAC_COLL;
		}
		if (proc_coll == null || proc_coll.isEmpty()) {
			proc_coll = "procedure2";
		}
		if (pet_coll == null || pet_coll.isEmpty()) {
			pet_coll = DatabaseDefinitions.PET_COLL;
		}
		if (owner_coll == null || owner_coll.isEmpty()) {
			owner_coll = DatabaseDefinitions.OWNER_COLL;
		}
		if (part_coll == null || part_coll.isEmpty()) {
			part_coll = DatabaseDefinitions.PART_COLL;
		}
		if (server_conf == null || server_conf.isEmpty()) {
			server_conf = DatabaseDefinitions.DEFAULT_SERVER_CONF_FILE;
		}
		
		DatabaseDefinitions.readServerConf(server_conf);
		
		// Begin initializing variables
		BreedDoc.initDBVariables(db, breed_coll);
		ProcedureDoc.initDBVariables(db, proc_coll);
		PetDoc.initDBVariables(db, pet_coll, breed_coll, owner_coll);
		PartDoc.initDBVariables(db, part_coll);
		String msg = "Initialized database variables";
		return Response.status(200).entity(msg).build();
	}

	@Path ("/version")
	@GET
	@Produces ({MediaType.APPLICATION_JSON})
	public Response getVersionInfo () {
		Document doc = new Document ("Web server Version", ServerDefinitions.WEBSERVER_VER);
		doc.append("Breed Table", DatabaseDefinitions.BREED_COLL_VER);
		doc.append("Practice Table", DatabaseDefinitions.PRAC_COLL_VER);
		doc.append("Procedure Table", DatabaseDefinitions.PROCEDURE_COLL_VER);
		doc.append("Offering Table", DatabaseDefinitions.OFFERING_COLL_VER);
		return Response.status(200).entity(doc.toJson()).build();
	}
	
}
