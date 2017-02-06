package com.gmv.pre.core;

import java.util.ArrayList;

import org.bson.Document;

import com.gmv.pre.structs.BundleDoc;
import com.gmv.pre.structs.ProcedureDoc;

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
		ArrayList<String> providerNames = new ArrayList<String>();
		ArrayList<Integer> offices = new ArrayList<Integer>();
		ArrayList<Integer> providers = new ArrayList<Integer>();
		ArrayList<Integer> variants = new ArrayList<Integer>();
		ArrayList<Double> prices = new ArrayList<Double>();
		ArrayList<Integer> included = new ArrayList<Integer>();
		ArrayList<Integer> cores = new ArrayList<Integer>();
		ArrayList<Document> rankings = new ArrayList<Document>();
		ArrayList<Double> cancelPercent = new ArrayList<Double>();

		names.add(bundle1.getName());
		providerNames.add(bundle1.getProviderName());
		offices.add(bundle1.getNumOffices());
		providers.add(bundle1.getNumProviders());
		variants.add(bundle1.getVariantLevel());
		prices.add(bundle1.getBasePrice());
		included.add(bundle1.getIncludedCount());
		cores.add(bundle1.getCoreCount());
		rankings.add(bundle1.getProviderRank());
		cancelPercent.add(bundle1.getCancellationPercent());
		
		if (b2 != null && !b2.equals("")) {
			names.add(bundle2.getName());
			providerNames.add(bundle2.getProviderName());
			offices.add(bundle2.getNumOffices());
			providers.add(bundle2.getNumProviders());
			variants.add(bundle2.getVariantLevel());
			prices.add(bundle2.getBasePrice());
			included.add(bundle2.getIncludedCount());
			cores.add(bundle2.getCoreCount());
			rankings.add(bundle2.getProviderRank());
			cancelPercent.add(bundle2.getCancellationPercent());
		}
		
		if (b3 != null && !b3.equals("")) {
			names.add(bundle3.getName());
			providerNames.add(bundle3.getProviderName());
			offices.add(bundle3.getNumOffices());
			providers.add(bundle3.getNumProviders());
			variants.add(bundle3.getVariantLevel());
			prices.add(bundle3.getBasePrice());
			included.add(bundle3.getIncludedCount());
			cores.add(bundle3.getCoreCount());
			rankings.add(bundle3.getProviderRank());
			cancelPercent.add(bundle3.getCancellationPercent());
		}

		comparison.append("Name", names);
		comparison.append("Provider", providerNames);
		comparison.append("Provider Rank", rankings);
		comparison.append("Number of Inclusions", included);
		comparison.append("Number of Core procedures", cores);
		comparison.append("Variant Level", variants);
		comparison.append("Price", prices);
		comparison.append("Cancellation Percent", cancelPercent);
		comparison.append("Number Offices", offices);
		comparison.append("Number Providers", providers);
		return comparison;
	}
	
	public static Document compareVariants (String id) {
		Document comparison = new Document();
		ProcedureDoc pd = new ProcedureDoc (id);
		System.out.println(pd.toJSON());
		ArrayList<Document> variants = pd.getVariants();
		for (Document variant : variants) {
			int level = variant.getInteger("Level", 0);
			String tier = "Bronze";
			if (level == 1) {tier = "Silver";}
			if (level == 2) {tier = "Gold";}
			Document variantInfo = new Document ();
			
			String vid = variant.getString("Id");
			ProcedureDoc variantProc = new ProcedureDoc (vid);
			
			variantInfo.append("Name", variantProc.getName());
			variantInfo.append("NumProcedures", variantProc.getNumInclusions());
			variantInfo.append("NumOptional", variantProc.getNumOptional());
			variantInfo.append("AveragePrice", ProcedureDoc.getNationalAverage(variantProc.getID()));
			variantInfo.append("FullBundle", variantProc.toDocument());
			
			comparison.append(tier, variantInfo);
		}
		return comparison;
	}
}
