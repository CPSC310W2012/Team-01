package com.cs310.ubc.meetupscheduler.client;


import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface View extends IsWidget {
	
	@Override
	public abstract Widget asWidget();

	void setName(String name);


}