package com.gmv.pre.structs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.bson.Document;

import com.gmv.pre.definitions.DatabaseDefinitions;
import com.gmv.pre.definitions.FieldDefinitions;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class UserDoc {

	public String __id;
	public UserDefDoc __userDef;
	public String __fName;
	public String __lName;
	public String __email;
	public String __phone;
	public String __bio;
	public ArrayList<Document> __additionalFields;
	
	public UserDoc (String id, String userDef) {
		__id = id;
		__userDef = UserDefDoc.get(userDef);
	}
	
	public Document toDocument () {
		Document doc = new Document (FieldDefinitions.UNIQUE_ID, __id);
		doc.append(FieldDefinitions.USER_TYPE, __userDef.toJSON());
		doc.append(FieldDefinitions.FIRST_NAME, __fName);
		doc.append(FieldDefinitions.LAST_NAME, __lName);
		doc.append(FieldDefinitions.EMAIL, __email);
		doc.append(FieldDefinitions.PHONE, __phone);
		doc.append(FieldDefinitions.BIOGRAPHY, __bio);
		if (__additionalFields != null) {
			for (int i = 0; i < __additionalFields.size(); i++) {
				Document field = __additionalFields.get(i);
				Set<String> keys = field.keySet();
				Iterator<String> it = keys.iterator();
				while (it.hasNext()) {
					String key = it.next();
					Object val = field.get(key);
					doc.append(key, val);
				}
			}
		}
		return doc;
	}
	
	public String toJSON () {
		return toDocument().toJson();
	}
	
	public void fromDocument (Document doc) {
		__id = doc.getString(FieldDefinitions.UNIQUE_ID);
		__fName = doc.getString(FieldDefinitions.FIRST_NAME);
		__lName = doc.getString(FieldDefinitions.LAST_NAME);
		__email = doc.getString(FieldDefinitions.EMAIL);
		__phone = doc.getString(FieldDefinitions.PHONE);
		__bio = doc.getString(FieldDefinitions.BIOGRAPHY);
	}

	public void read (String id) {
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoCollection<Document> coll = client.getDatabase(DatabaseDefinitions.NOSQL_DB).
												getCollection(DatabaseDefinitions.USER_COLL);
		FindIterable<Document> docs = coll.find(new Document(FieldDefinitions.UNIQUE_ID, id));
		Iterator<Document> iter = docs.iterator ();
		if (iter.hasNext()) {
			Document doc = iter.next();
			fromDocument (doc);
		}
		client.close();
	}
	
	public void write () {
		Document doc = toDocument ();		
		MongoClient client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		MongoCollection<Document> coll = client.getDatabase(DatabaseDefinitions.NOSQL_DB).
												getCollection(DatabaseDefinitions.USER_COLL);
		Document match = new Document (FieldDefinitions.UNIQUE_ID, __id);
		if (coll.count(match) > 0) {
			coll.replaceOne (match, doc); 
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
		return __fName + " " + __lName;
	}
	
	public String getFirstName () {
		return __fName;
	}
	
	public void setFirstName (String name) {
		__fName = name;
	}

	public String getLastName () {
		return __lName;
	}
	
	public void setLastName (String name) {
		__lName = name;
	}
	
	public String getEmail () {
		return __email;
	}
	
	public void setEmail (String email) {
		__email = email;
	}
	
	public String getPhone () {
		return __phone;
	}
	
	public void setPhone (String phone) {
		__phone = phone;
	}
	
	public String getBio () {
		return __bio;
	}
	
	public void setBio (String bio) {
		__bio = bio;
	}
	
	public void addField (String field, Object fieldVal) {
		if (__additionalFields == null) {
			__additionalFields = new ArrayList<Document> ();
		}
		
		__additionalFields.add(new Document (field, fieldVal));
	}
}
