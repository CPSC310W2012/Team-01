package com.cs310.ubc.meetupscheduler.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous Interface for calls to get DataObjects. See DataObject and DataObjectServiceImpl for more details on DataObjects and the following methods.
 * @author Connor
 *
 */
public interface DataObjectServiceAsync {
	public void add(String table, HashMap<String, String> fields, AsyncCallback<HashMap<String, String>> callback);
	public void get(String table, String query, AsyncCallback<ArrayList<HashMap<String,String>>> callback);
	public void remove(String table, String query, AsyncCallback<Void> callback);
	public void update(String table, String query, String column, String newValue, AsyncCallback<ArrayList<HashMap<String, String>>> callback);
}
