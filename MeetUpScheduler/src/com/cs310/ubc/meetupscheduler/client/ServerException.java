package com.cs310.ubc.meetupscheduler.client;

import java.io.Serializable;
/**
 * A generic serializable exception throw from the server to the client. The details of the server side exception
 * are preserved in the message and stackTrace fields, but the object can be serialized and passed via an RPC.
 * @author Connor
 *
 */
public class ServerException extends Exception implements Serializable {
	private static final long serialVersionUID = 2596118152758578029L;
	
	public ServerException() { 
		//Serializable objects need a constructor with no objects.
	}
	
	public ServerException(String message) { }
	
	public ServerException(String message, StackTraceElement[] stackTrace) {
		super(message);
		this.setStackTrace(stackTrace);
	}
}
