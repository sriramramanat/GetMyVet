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
		Document comparison = new Document ();
		BundleDoc bundle1 = BundleDoc.createBundle("", "", "");
		bundle1.read(b1);
		BundleDoc bundle2 = BundleDoc.createBundle("", "", "");
		bundle2.read(b2);
		BundleDoc bundle3 = BundleDoc.createBundle("", "", "");
		bundle3.read(b3);
		
		ArrayList<String> ids = new ArrayList<String>();
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<Integer> offices = new ArrayList<Integer>();
		ArrayList<Integer> providers = new ArrayList<Integer>();
		ArrayList<Integer> variants = new ArrayList<Integer>();
		ArrayList<Double> prices = new ArrayList<Double>();

		names.add(bundle1.getName());
		offices.add(bundle1.getNumOffices());
		providers.add(bundle1.getNumProviders());
		variants.add(bundle1.getVariantLevel());
		prices.add(bundle1.getBasePrice());
		
		if (b2 != null && !b2.equals("")) {
			names.add(bundle2.getName());
			offices.add(bundle2.getNumOffices());
			providers.add(bundle2.getNumProviders());
			variants.add(bundle2.getVariantLevel());
			prices.add(bundle2.getBasePrice());
		}
		
		if (b3 != null && !b3.equals("")) {
			names.add(bundle3.getName());
			offices.add(bundle3.getNumOffices());
			providers.add(bundle3.getNumProviders());
			variants.add(bundle3.getVariantLevel());
			prices.add(bundle3.getBasePrice());
		}

		comparison.append("Name", names);
		comparison.append("Number Offices", offices);
		comparison.append("Number Providers", providers);
		comparison.append("Variant Level", variants);
		comparison.append("Price", prices);
		return Response.status(200).entity(comparison.toJson()).build();
	}
}
