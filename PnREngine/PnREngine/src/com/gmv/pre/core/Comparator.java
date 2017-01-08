package com.gmv.pre.core;

import java.util.ArrayList;

import org.bson.Document;

import com.gmv.pre.structs.BundleDoc;

public class Comparator {

	public static Document compareBundles (String b1, String b2, String b3) {
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
		return comparison;
	}
}
