package com.cs310.ubc.meetupscheduler.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Interface for ASync calls to get DataObjects. See DataObject and DataObjectServiceImpl for more details on 
 * DataObjects and the following methods.
 */
@RemoteServiceRelativePath("dataobject")
public interface DataObjectService extends RemoteService {
	public HashMap<String, String> add(String table, HashMap<String, String> fields) throws ServerException;
	public void remove(String table, String query);
	public void remove(String table);
	public ArrayList<HashMap<String, String>> update(String table, String query, HashMap<String, String> newValues) throws ServerException;
	public ArrayList<HashMap<String, String>> update(String table, HashMap<String, String> newValues) throws ServerException;
	public ArrayList<HashMap<String, String>> get(String table, String query) throws ServerException;
	public ArrayList<HashMap<String, String>> get(String table) throws ServerException;
}
