package com.gmv.pre.helpers;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.json.JSONObject;

public class JSONBuilder {
	public String inStr;
	public JSONObject jsonObj;
	public Response resp;
	public Map<String, Object> parsedVals;
	
	private void init () {
		inStr = null;
		jsonObj = null;
		resp = null;
		parsedVals = new HashMap<String, Object>();
	}
	public JSONBuilder (String str, String... strings) {
		init ();
		try {
			jsonObj = new JSONObject (str);
			for (String key : strings) {
				try {
					Object val = jsonObj.get(key);
					parsedVals.put (key, val);
				} catch (Exception e1) {
					resp = ResponseBuilder.buildMissingFieldError(key);
				}
			}
		} catch (Exception e2) {
			resp = ResponseBuilder.buildJSONParseError ("");
		}
	}
	
	public void parse(String...strings) {
		for (String key : strings) {
			try {
				Object val = jsonObj.get(key);
				parsedVals.put(key, val);
			} catch (Exception e) {
				resp = ResponseBuilder.buildMissingFieldError(key);
				return;
			}
		}
	}

	public void optParse(String... strings) {
		for (String key : strings) {
			try {
				Object val = jsonObj.get(key);
				parsedVals.put(key, val);
			} catch (Exception e) { }
		}
	}
}
