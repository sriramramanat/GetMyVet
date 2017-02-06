package com.gmv.pre.services;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.Consumes;
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
import com.gmv.pre.helpers.JSONBuilder;
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
	
	@Path("/create")
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	public Response createBundle (@QueryParam("pid") String pracID,
								  @QueryParam("bid") String bundleID) {
		String bundleUID = pracID + "_" + bundleID;
		BundleDoc bd = BundleDoc.createBundle(bundleUID, pracID, bundleID);
		bd.writeToTemp();
		return Response.status(200).entity(bd.toJSON()).build();
	}

	@Path("/setPrice")
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	public Response setPrice (@QueryParam("id") String id,
								  @QueryParam("price") double price) {
		BundleDoc bd = BundleDoc.readBundle(id);
		if (bd == null) {
			return Response.status(500).entity("Bundle Not found").build();
		}
		bd.setBasePrice(price);
		bd.write();
		return Response.status(200).entity(bd.toJSON()).build();
	}

	@Path("/setTransferPrice")
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	public Response setTransferPrice (@QueryParam("id") String id,
								  @QueryParam("price") double price) {
		BundleDoc bd = BundleDoc.readBundle(id);
		if (bd == null) {
			return Response.status(500).entity("Bundle Not found").build();
		}
		bd.setTransferPrice(price);
		bd.write();
		return Response.status(200).entity(bd.toJSON()).build();
	}

	@Path("/setNumberSold")
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	public Response setNumberSold (@QueryParam("id") String id,
								  @QueryParam("numsold") int numSold) {
		BundleDoc bd = BundleDoc.readBundle(id);
		if (bd == null) {
			return Response.status(500).entity("Bundle Not found").build();
		}
		bd.setNumberSold(numSold);
		bd.write();
		return Response.status(200).entity(bd.toJSON()).build();
	}

	@Path("/setCancelPercent")
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	public Response setCancelPercent (@QueryParam("id") String id,
								  @QueryParam("cancelpercent") double cPercent) {
		BundleDoc bd = BundleDoc.readBundle(id);
		if (bd == null) {
			return Response.status(500).entity("Bundle Not found").build();
		}
		bd.setCancellationPercent(cPercent);
		bd.write();
		return Response.status(200).entity(bd.toJSON()).build();
	}

	@Path("/setEntryTime")
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	public Response setEntryTime (@QueryParam("id") String id,
								  @QueryParam("day") String day,
								  @QueryParam("open") boolean open) {
		BundleDoc bd = BundleDoc.readBundle(id);
		if (bd == null) {
			return Response.status(500).entity("Bundle Not found").build();
		}
		bd.addEntryTime(day, open);
		bd.write();
		return Response.status(200).entity(bd.toJSON()).build();
	}

	@Path("/setCoreTime")
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	public Response setCoreTime (@QueryParam("id") String id,
								  @QueryParam("day") String day,
								  @QueryParam("open") boolean open) {
		BundleDoc bd = BundleDoc.readBundle(id);
		if (bd == null) {
			return Response.status(500).entity("Bundle Not found").build();
		}
		bd.addCoreTime(day, open);
		bd.write();
		return Response.status(200).entity(bd.toJSON()).build();
	}

	@Path("/addApptTimeSlot")
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	public Response addApptTimeSlot (@QueryParam("id") String id,
									 @QueryParam("date") String date,
									 @QueryParam("hour") int hour,
									 @QueryParam("min") int min,
									 @QueryParam("available") boolean open,
									 @QueryParam("promoprice") double price) {
		BundleDoc bd = BundleDoc.readBundle(id);
		if (bd == null) {
			return Response.status(500).entity("Bundle Not found").build();
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-mm-dd");
		try {
			Date d = sdf.parse(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.HOUR_OF_DAY, hour);
			cal.add(Calendar.MINUTE, min);
			d = cal.getTime();
			bd.addAppointmentSlot(d, open, price);
			bd.write();
		} catch (Exception e) {
			return Response.status(500).entity(e.getMessage()).build();
		}
		return Response.status(200).entity(bd.toJSON()).build();
	}

	@Path("/setOffice")
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	public Response setOffice (@QueryParam("id") String id,
								  @QueryParam("stepnum") int stepNumber,
								  @QueryParam("office") String officeID) {
		BundleDoc bd = BundleDoc.readBundle(id);
		if (bd == null) {
			return Response.status(500).entity("Bundle Not found").build();
		}
		bd.setOffice(stepNumber, officeID);
		bd.write();
		return Response.status(200).entity(bd.toJSON()).build();
	}

	@Path("/setPartner")
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	public Response setPartner (@QueryParam("id") String id,
								@QueryParam("stepnum") int stepNumber,
								@QueryParam("partner") String partnerID,
								@QueryParam("office") String officeID) {
		BundleDoc bd = BundleDoc.readBundle(id);
		if (bd == null) {
			return Response.status(500).entity("Bundle Not found").build();
		}
		bd.setRenderer(stepNumber, partnerID, officeID);
		bd.write();
		return Response.status(200).entity(bd.toJSON()).build();
	}

	@Path("/updateTempBundle")
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public Response updateTempBundle (@QueryParam("id")String id,
										String data) {
		System.out.println (data);
		BundleDoc bd = BundleDoc.readTempBundle(id);
		Document json = Document.parse(data);
		bd.update(json);
		bd.writeToTemp();
		return Response.status(200).entity(bd.toJSON()).build();
	}
}	
