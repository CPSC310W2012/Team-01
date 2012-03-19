package com.cs310.ubc.meetupscheduler.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public abstract class View implements IsWidget {
	
	@Override
	public abstract Widget asWidget();

}