package com.gmv.pre.structs;

import java.util.ArrayList;

import org.bson.Document;

public class PracticeDoc {
	
	private String __id;
	private String __name;
	private String __website;
	private ArrayList<String> __socialMediaLinks;
	private String __specialty;
	private String __secondarySpecialty;
	private ArrayList<Document> __addresses;
	private Document __location; // for $geoNear or $near ops
	private ArrayList<Document> __hoursOfOp;
	
	public PracticeDoc () {
		__socialMediaLinks = new ArrayList<String> ();
		__addresses = new ArrayList<Document> ();
		__hoursOfOp = new ArrayList<Document> ();
	}
	
	public String getID () {
		return __id;
	}
	
	public void setID (String id) {
		__id = id;
	}
	
	public String getName () {
		return __name;
	}
	
	public void setName (String name) {
		__name = name;
	}
	
	public String getWebsite () {
		return __website;
	}
	
	public void setWebsite (String website) {
		__website = website;
	}
	
	public ArrayList<String> getSocialMediaLinks () {
		return __socialMediaLinks;
	}
	
	public void addSocMediaLink (String link) {
		if (!__socialMediaLinks.contains(link))
			__socialMediaLinks.add(link);
	}
	
	public void removeSocMediaLink (String link) {
		if (__socialMediaLinks.contains(link))
			__socialMediaLinks.remove(link);
		
	}
}
