package com.gmv.pre.helpers;

import javax.ws.rs.core.Response;

public class ResponseBuilder {
	public static Response buildSuccess () {
		return Response.status(200).build();
	}
	
	public static Response buildSuccess (String msg) {
		return Response.status(200).entity(msg).build();
	}
	
	public static Response buildDBError () {
		return Response.status(500).build();
	}

	public static Response buildJSONParseError(String str) {
		return Response.status(500).entity("Error in parsing JSON " + str).build();
	}
	
	public static Response buildMissingFieldError (String field) {
		return Response.status(500).entity("Missing Field : " + field + " in JSON").build();
	}

	public static Response buildError(String msg) {
		return Response.status(500).entity(msg).build();
	}
}
