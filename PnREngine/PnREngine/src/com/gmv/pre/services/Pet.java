package com.gmv.pre.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
import com.gmv.pre.structs.PetDoc;

@Path ("/pet")
public class Pet {
	@Path ("/add")
	@POST
	public Response add (@QueryParam("owner") String owner,
						@QueryParam("name") String pet,
						@QueryParam("breed") String breed,
						@QueryParam("gender") String gender) {
		if (owner == null || owner.isEmpty())
			return Response.status(500).entity("Missing owner field").build();
		if (pet == null || pet.isEmpty())
			return Response.status(500).entity("Missing pet's name field").build();
		if (breed == null || breed.isEmpty())
			return Response.status(500).entity("Missing pet's breed field").build();
		if (gender == null || gender.isEmpty())
			return Response.status(500).entity("Missing pet's gender field").build();
		
		PetDoc pd = new PetDoc (owner, pet, breed, gender);
		pd.Write();
		return Response.status(200).entity(pd.getID()).build();
	}

	@Path ("/get")
	@GET
	@Produces (MediaType.APPLICATION_JSON)
	public Response get (@QueryParam("uid") String id) {
		PetDoc pd = new PetDoc (id);
		return Response.status(200).entity(pd.toJSON()).build();
	}
	
	@Path ("/getGender")
	@GET
	public Response getGender (@QueryParam("uid") String id) {
		String gender = "";
		PetDoc pd = new PetDoc(id);
		gender = pd.getGender ();
		return Response.status(200).entity(gender).build();
	}
	
	@Path ("/changeOwner")
	@POST
	public Response changeOwner (@QueryParam("uid") String id,
								@QueryParam("newowner") String owner) {
		PetDoc pd = new PetDoc(id);
		pd.changeOwner (owner);
		pd.Write();
		return Response.status(200).entity(pd.toJSON()).build();
	}
	
	@Path ("/setDateOfBirth")
	@POST
	public Response setDateOfBirth (@QueryParam("uid") String id,
									@QueryParam("dob") String dob) {
		DateFormat df = new SimpleDateFormat("MM.dd.yyyy");
		try {
			Date date = df.parse(dob);
			PetDoc pd = new PetDoc(id);
			pd.setDateOfAdoption(date);
			pd.Write();
			return Response.status(200).entity(pd.toJSON()).build();
		} catch (Exception e) {
			return Response.status(200).entity("error parsing date").build();
		}
	}
	
	@Path ("/addToHistory")
	@POST
	public Response addToHistory (@QueryParam("uid") String id,
								  @QueryParam("dob") String dob,
								  @QueryParam("proc") String procId,
								  @QueryParam("doneby") String doneBy) {
		DateFormat df = new SimpleDateFormat("MM.dd.yyyy");
		try {
			Date date = df.parse(dob);
			PetDoc pd = new PetDoc(id);
			pd.addToHistory(date, procId, doneBy);
			pd.Write();
			return Response.status(200).entity(pd.toJSON()).build();
		} catch (Exception e) {
			return Response.status(200).entity("error parsing date").build();
		}
	}
	
	@Path ("/getLifeStageReco")
	@GET
	public Response getLifeStageReco (@QueryParam("uid") String id) {
		// Find the pet from the pet DB
		PetDoc pd = new PetDoc (id);
		String breedID = pd.getBreed();
		String gender = pd.getGender();
		
		BreedDoc bd = new BreedDoc (breedID);
		
		// got the breed for this pet
		// let's find the recommended procedures for this breed at this time
		
		
		Response r = Response.status(200).entity(bd.toJSON()).build();
		return r;
	}

	@Path ("/getBreedReco")
	@GET
	public Response getBreedReco (@QueryParam("uid") String id,
								  @QueryParam("organ") String organ) {
		// Find the pet from the pet DB
		PetDoc pd = new PetDoc (id);
		String breedID = pd.getBreed();
		String gender = pd.getGender();
		
		BreedDoc bd = new BreedDoc (breedID);
		
		// got the breed for this pet
		// let's find the recommended procedures for this breed at this time
		
		
		Response r = Response.status(200).entity(bd.toJSON()).build();
		return r;
	}

	@Path ("/getCongenitalIssues")
	@GET
	@Produces (MediaType.APPLICATION_JSON)
	public Response getCongenitalIssues (@QueryParam("uid") String id) {
		PetDoc pd = new PetDoc (id);
		String breedID = pd.getBreed();
		BreedDoc breed = new BreedDoc (breedID);
		String str = "[";
		ArrayList<Document> list = breed.getCongenitalIssues();
		for (Document d : list) {
			str += d.toJson();
			str += ",";
		}
		str += "]";
		return Response.status(200).entity(str).build();
	}
}
