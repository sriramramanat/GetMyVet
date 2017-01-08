package com.gmv.pre.definitions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import com.mongodb.ServerAddress;

public class DatabaseDefinitions {
	public final static String NOSQL_DB = "gmv_no_sql";
	public static final String GEO_LOCATION_COLL = "geoloc";
	public static final String USERDEF_COLL = "userdef";
	public static final String USER_COLL = "users";
	public final static String BREED_COLL = "breeds";
	public final static String PRAC_COLL = "practices";
	public static final String PROCEDURE_COLL = "procedures";
	public static final String PET_COLL = "pets";
	public static final String OWNER_COLL = "owners";
	public static final String PART_COLL = "parts";
	public static final String PRACTITIONER_COLL = "practitioner";
	public static final String OFFERING_COLL = "offering";
	public static final String BUNDLE_COLL = "sellable_bundles";
	
	public static final String BREED_COLL_VER = "1.0";
	public static final String PRAC_COLL_VER = "1.0";
	public static final String PROCEDURE_COLL_VER = "1.0";
	public static final String OFFERING_COLL_VER = "1.0";
	public static final String PRACTITIONER_COLL_VER = "1.0";
	
	public static final ArrayList<ServerAddress> MONGO_SERVER_LIST = new ArrayList<ServerAddress>();
	public static final String DEFAULT_SERVER_CONF_FILE = "/etc/gmv/config/mongo_srv.csv";
	
	public static void readServerConf (String confFile) {
		if (confFile == null || confFile.isEmpty()) {
			confFile = DEFAULT_SERVER_CONF_FILE;
		}
		BufferedReader br = null;
        try {
			br = new BufferedReader(new FileReader(confFile));
			String line = br.readLine();
			while(line!=null){
			     String[] b = line.split(",");
			     if (b.length == 2) {
				     String server = b[0];
				     int port = Integer.parseInt(b[1]);
				     ServerAddress sa = new ServerAddress (server, port);
				     MONGO_SERVER_LIST.add(sa);
			     }
			     line = br.readLine();
			}
		} catch (Exception e) {
			//may be file is not present, just add localhost
			MONGO_SERVER_LIST.add(new ServerAddress("localhost", 27017));
		}
	}
}
