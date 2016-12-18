package com.gmv.pre.services;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.bson.Document;

import com.gmv.pre.structs.UserDefDoc;

@Path ("/userdef")
public class UserDef {

	@Path ("/get")
	@GET
	public Response getUserDef (@QueryParam ("id") String userDefID) {
		UserDefDoc udef = new UserDefDoc (userDefID, "");
		udef.read(userDefID);
		return Response.status(200).entity(udef.toJSON()).build();
	}
	
	@Path ("/getAll")
	@GET
	public Response getAll () {
		Document udef = new Document ("User Defs", UserDefDoc.readAll());
		return Response.status(200).entity(udef.toJson()).build();
	}

	@Path ("/add")
	@POST
	public Response addUserDef (@QueryParam ("id") String userDefID, 
								@QueryParam ("name") String userDefName,
								@QueryParam ("desc") String desc) {
		UserDefDoc udef = new UserDefDoc (userDefID, userDefName);
		udef.setDescription(desc);
		udef.write();
		return Response.status(200).entity(udef.toJSON()).build();
	}
}
