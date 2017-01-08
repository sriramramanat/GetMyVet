package com.gmv.pre.definitions;

public class QueryDefaults {
	public final static int ZIP = 48109;
	public final static int MAXDISTANCE = 1000;
	public final static int NUM_OFFICES = 100;
	public final static int NUM_PROVIDERS = 100;
	public static final double CANCEL_PENALTY = 100;
	public static final long MIN_RESULTS = 5;
	public static final double MAX_PRICE = 1000000;
	public static final Object STRICT_CANCEL_PENALTY = 0.2;
	public static final Object MODERATE_CANCEL_PENALTY = 0.1;
	public static final Object FLEXIBLE_CANCEL_PENALTY = 0.05;
	
	public static final String COORDINATES = "coordinates";
	public static final String GEOMETRY = "$geometry";
	public static final String NEAR = "$near";
	public static final String MAX_DISTANCE = "$maxDistance";
	public static final String MIN_DISTANCE = "$minDistance";
}
