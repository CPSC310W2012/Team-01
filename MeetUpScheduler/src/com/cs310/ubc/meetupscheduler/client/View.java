package com.cs310.ubc.meetupscheduler.client;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public abstract class View {
	//TODO: panel type returned? Might look nice to return the panel view wrapped in a DecoratorPanel. DS
	public abstract Widget createPage();
}
