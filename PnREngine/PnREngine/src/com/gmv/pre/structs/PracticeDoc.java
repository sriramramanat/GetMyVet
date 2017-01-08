package com.gmv.pre.structs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import org.bson.Document;

import com.gmv.pre.definitions.DatabaseDefinitions;
import com.gmv.pre.definitions.FieldDefinitions;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class PracticeDoc {
	
	private String __id;
	private String __name;
	private String __website;
	private ArrayList<String> __socialMediaLinks;
	private String __specialty;
	private ArrayList<String> __secondarySpecialty;
	private String __primaryPhone;
	private String __primaryFax;
	private String __email;
	private String __street1;
	private String __street2;
	private String __city;
	private String __state;
	private String __zip;
	private Document __location; // for $geoNear or $near ops
	private ArrayList<Document> __practitioners;
	private ArrayList<Document> __addresses;
	private ArrayList<Document> __hoursOfOp;
	private ArrayList<String> __animalsTreated;
	private Document __rank;
	
	public PracticeDoc () {
		__socialMediaLinks = new ArrayList<String> ();
		__addresses = new ArrayList<Document> ();
		__hoursOfOp = new ArrayList<Document> ();
		__practitioners = new ArrayList<Document> ();
		__animalsTreated = new ArrayList<String> ();
		__secondarySpecialty = new ArrayList<String> ();
		__socialMediaLinks = new ArrayList<String> ();
		__rank = new Document();
		__location = new Document ();
		fillOpenHours();
	}
	
	public Document toDocument () {
		Document doc = new Document (FieldDefinitions.UNIQUE_ID, __id);
		doc.append(FieldDefinitions.NAME, __name);
		doc.append(FieldDefinitions.PRIMARY_ADDRESS, getPrimaryAddress());
		doc.append(FieldDefinitions.PHONE, __primaryPhone);
		doc.append(FieldDefinitions.FAX, __primaryFax);
		doc.append(FieldDefinitions.EMAIL, __email);
		doc.append(FieldDefinitions.WEBSITE, __website);
		doc.append(FieldDefinitions.SOCIAL_MEDIA, __socialMediaLinks);
		doc.append(FieldDefinitions.PRIMARY_SPECIALTY, __specialty);
		doc.append(FieldDefinitions.SECONDARY_SPECIALTY, __secondarySpecialty);
		doc.append(FieldDefinitions.HOURS_OF_OPERATION, __hoursOfOp);
		doc.append(FieldDefinitions.ANIMALS_TREATED, __animalsTreated);
		doc.append(FieldDefinitions.PRACTITIONERS, __practitioners);
		doc.append(FieldDefinitions.RANKING, __rank);
		doc.append(FieldDefinitions.OTHER_ADDRESSES, __addresses);
		doc.append(FieldDefinitions.LOCATION, __location);
		return doc;
	}
	
	@SuppressWarnings("unchecked")
	public void fromDocument (Document doc) {
		__id = doc.getString(FieldDefinitions.UNIQUE_ID);
		__name = doc.getString(FieldDefinitions.NAME);
		__primaryPhone = doc.getString(FieldDefinitions.PHONE);
		{
			Document address = (Document)(doc.get(FieldDefinitions.PRIMARY_ADDRESS));
			__street1 = address.getString(FieldDefinitions.STREET1);
			__street2 = address.getString(FieldDefinitions.STREET2);
			__city = address.getString(FieldDefinitions.CITY);
			__state = address.getString(FieldDefinitions.STATE);
			__zip = address.getString(FieldDefinitions.ZIP);
		}
		__primaryFax = doc.getString(FieldDefinitions.FAX);
		__email = doc.getString(FieldDefinitions.EMAIL);
		__website = doc.getString(FieldDefinitions.WEBSITE);
		__socialMediaLinks = (ArrayList<String>)(doc.get(FieldDefinitions.SOCIAL_MEDIA));
		__specialty = doc.getString(FieldDefinitions.PRIMARY_SPECIALTY);
		__secondarySpecialty = (ArrayList<String>)(doc.get(FieldDefinitions.SECONDARY_SPECIALTY));
		__hoursOfOp = (ArrayList<Document>)(doc.get(FieldDefinitions.HOURS_OF_OPERATION));
		__practitioners = (ArrayList<Document>)(doc.get(FieldDefinitions.PRACTITIONERS));
		__addresses = (ArrayList<Document>)(doc.get(FieldDefinitions.OTHER_ADDRESSES));
		__animalsTreated = (ArrayList<String>)(doc.get(FieldDefinitions.ANIMALS_TREATED));
		__rank = (Document)(doc.get(FieldDefinitions.RANKING));
		__location = (Document)(doc.get(FieldDefinitions.LOCATION));
	}
	
	public String toJSON () {
		return toDocument().toJson();
	}

	public void read (String id) {
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoCollection<Document> coll = client.getDatabase(DatabaseDefinitions.NOSQL_DB).getCollection(DatabaseDefinitions.PRAC_COLL);
		FindIterable<Document> docs = coll.find(new Document(FieldDefinitions.UNIQUE_ID, id));
		Iterator<Document> iter = docs.iterator ();
		if (iter.hasNext()) {
			Document doc = iter.next();
			fromDocument (doc);
		}
		client.close();
	}
	
	public void write () {
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoCollection<Document> coll = client.getDatabase(DatabaseDefinitions.NOSQL_DB).getCollection(DatabaseDefinitions.PRAC_COLL);
		Document doc = toDocument ();
		Document match = new Document (FieldDefinitions.UNIQUE_ID, __id);
		if (coll.count(match) > 0) {
			coll.findOneAndReplace(match, doc);
		} else {
			coll.insertOne(doc);
		}
		client.close();
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
	
	public String getSpecialty () {
		return __specialty;
	}

	public void setSpecialty (String sp) {
		__specialty = sp;
	}

	public ArrayList<String> get2ndSpecialty () {
		return __secondarySpecialty;
	}

	public void add2ndSpecialty (String sp) {
		if(!__secondarySpecialty.contains(sp))
			__secondarySpecialty.add(sp);
	}
	
	public void remove2ndSpecialty (String sp) {
		ListIterator<String> it = __secondarySpecialty.listIterator();
		while (it.hasNext()) {
			String temp = it.next();
			if (temp.equals(sp)) {
				it.remove();
				break;
			}
		}
	}

	public String getPhone () {
		return __primaryPhone;
	}

	public void setPhone (String phone) {
		__primaryPhone = phone;
	}

	public String getFax () {
		return __primaryFax;
	}

	public void setFax (String fax) {
		__primaryFax = fax;
	}

	public String getEmail () {
		return __email;
	}

	public void setEmail (String mail) {
		__email = mail;
	}
	
	public void setPrimaryAddress (Document primaryAddress) {
		__street1 = primaryAddress.getString ("Street");
		__street2 = primaryAddress.getString ("Street 2");
		__city = primaryAddress.getString ("City");
		__state = primaryAddress.getString ("State");
		__zip = primaryAddress.getString ("Zip");
	}
	
	public Document getPrimaryAddress () {
		Document address = new Document ("Street", __street1);
		address.append("Street 2", __street2);
		address.append("City", __city);
		address.append("State", __state);
		address.append("Zip", __zip);
		return address;
	}
	
	public Document getAddressByType (String type) {
		if (type.equals("Primary"))
			return getPrimaryAddress();

		Document address = null;
		for (int i = 0; i < __addresses.size(); i++) {
			Document doc = __addresses.get(i);
			String localType = doc.getString("Type");
			if (localType.equals(type)) {
				address = (Document)(doc.get("Address"));
				break;
			}
		}
		return address;
	}
	
	public Document getAddressById (String id) {
		Document address = null;
		for (int i = 0; i < __addresses.size(); i++) {
			Document doc = __addresses.get(i);
			String localType = doc.getString("Address ID");
			if (localType.equals(id)) {
				address = (Document)(doc.get("Address"));
				break;
			}
		}
		return address;
	}
	
	public void addAddress (String type, Document address) {
		int idCount = __addresses.size() + 1;
		String id = __id + "_office_" + idCount;
		Document doc = new Document ("Type", type);
		doc.append("Address", address);
		doc.append("Address ID", id);
		__addresses.add(doc);
	}
	
	public void setLocation (Document location) {
		__location = location;
	}

	public void fillOpenHours () {
		Document d = new Document ("Day", "Sunday");
		d.append("Is Open", false);
		__hoursOfOp.add(d);
		addHoursOfOp ("Monday", 9, 18);
		addHoursOfOp ("Tuesday", 9, 18);
		addHoursOfOp ("Wednesday", 9, 18);
		addHoursOfOp ("Thursday", 9, 18);
		addHoursOfOp ("Friday", 9, 18);
		addHoursOfOp ("Saturday", 11, 13);
	}
	
	public void addHoursOfOp (String day, int openHours, int closeHours) {
		Document d = new Document ("Day", day);
		if (closeHours > openHours)
			d.append("Is Open", true);
		else // practice is closed
			d.append("Is Open", false);
		d.append("Open", openHours);
		d.append("Close", closeHours);
		for (int i = 0; i < __hoursOfOp.size(); i++) {
			Document h = __hoursOfOp.get(i);
			String _day = h.getString("Day");
			if (_day.equals(day)) {
				// day is already in the array.
				__hoursOfOp.set (i, d);
				return;
			}
		}
		
		__hoursOfOp.add(d);
	}
	
	public void addAnimalsTreated (String animal) {
		if (__animalsTreated.contains(animal))
			return;
		
		__animalsTreated.add(animal);
	}
	
	public void setWaitTimeRank (double rank) {
		__rank.put(FieldDefinitions.WAIT_TIME_RANKING, rank);
	}
	
	public void setBedSideRnak (double rank) {
		__rank.put(FieldDefinitions.BED_SIDE_MANNERS_RANKING, rank);
	}
	
	public void setCompositeRank (double rank) {
		__rank.put(FieldDefinitions.COMPOSITE_RANKING, rank);
	}
	
	public static PracticeDoc createSample () {
		PracticeDoc pd = new PracticeDoc ();
		pd.setID("test practice");
		pd.setName("Sample Practice");
		pd.setWebsite("http://www.example.com");
		pd.addSocMediaLink("http://www.facebook.com/test_practice");
		pd.addSocMediaLink("http://www.twitter.com/test_practice");
		pd.addHoursOfOp ("Tuesday", 0, 0);
		
		Document address = new Document ("Street", "331 Amherst Street");
		address.append("City", "Buffalo");
		address.append("State", "NY");
		address.append("Zip", "14217");
		pd.setPrimaryAddress(address);
		pd.addAnimalsTreated("cat");
		pd.addAnimalsTreated("dog");
		return pd;
	}
}
