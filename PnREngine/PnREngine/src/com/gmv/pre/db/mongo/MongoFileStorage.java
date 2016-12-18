package com.gmv.pre.db.mongo;

import java.io.File;
import java.io.IOException;

import org.bson.Document;

import com.gmv.pre.definitions.DatabaseDefinitions;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

public class MongoFileStorage {
	private DB __coll = null;
	private String __collName = "";
	private MongoClient __client = null;

	public MongoFileStorage (String dbName, String collName) {
		__client = new MongoClient (DatabaseDefinitions.MONGO_SERVER_LIST);
		if (__client != null) {
			__coll = __client.getDB (dbName);
			__collName = collName;
		}
	}
	
	public void saveFileToMongo (String fileToSave, String userName) {
		File saveFile = new File (fileToSave);
		GridFS gfs = new GridFS(__coll, __collName);
		GridFSInputFile gfsFile;
		try {
			gfsFile = gfs.createFile(saveFile);
			String fileName = userName.toLowerCase() + "-" + saveFile.getName();
	        gfsFile.setFilename(fileName);
	        gfsFile.save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    public void getSingleFile (String fileToFind) {
        GridFS gfs = new GridFS(__coll, __collName);
        GridFSDBFile imageForOutput = gfs.findOne(fileToFind);
        System.out.println(imageForOutput);
    }
 
     
    public void listAllFiles (String userName) {
        GridFS gfs = new GridFS(__coll, __collName);
        DBCursor cursor = gfs.getFileList(new BasicDBObject("filename", new BasicDBObject("$regex", userName.toLowerCase())));
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }
    }
    
    public void saveToFileSystem(String fileToRetrieve, String userName, String dir) {
		GridFS gfs = new GridFS(__coll, __collName);
		GridFSDBFile fileOut = gfs.findOne(fileToRetrieve);
		String outFileName = dir + "/" + userName + "-" + fileToRetrieve;
		try {
			if (fileOut != null) {
				fileOut.writeTo(outFileName);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
