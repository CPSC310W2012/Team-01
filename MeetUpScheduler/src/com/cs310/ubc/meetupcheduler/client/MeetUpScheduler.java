package com.cs310.ubc.meetupcheduler.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MeetUpScheduler implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 * TODO: not implemented
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";
	
	  private TabPanel tabPanel;
	  private FormPanel form;


	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
	    tabPanel = new TabPanel();
	    initTabPanel();
	    RootPanel.get().add(tabPanel);
	  }


	private void initTabPanel() {
		tabPanel.add(new HTML("<h1>Page 0 Content: Page 0</h1>"), " Page 0 ");
	    tabPanel.add(new HTML("<h1>Page 1 Content: Page 1</h1>"), " Page 1 ");
	    tabPanel.add(new HTML("<h1>Page 2 Content: Page 2</h1>"), " Page 2 ");

	    tabPanel.addSelectionHandler(new SelectionHandler<Integer>(){
	      public void onSelection(SelectionEvent<Integer> event) {
	        // TODO Auto-generated method stub
	        History.newItem("page" + event.getSelectedItem());
	      }});

	    History.addValueChangeHandler(new ValueChangeHandler<String>() {
	      public void onValueChange(ValueChangeEvent<String> event) {
	        String historyToken = event.getValue();

	        // Parse the history token
	        try {
	          if (historyToken.substring(0, 4).equals("page")) {
	            String tabIndexToken = historyToken.substring(4, 5);
	            int tabIndex = Integer.parseInt(tabIndexToken);
	            // Select the specified tab panel
	            tabPanel.selectTab(tabIndex);
	          } else {
	            tabPanel.selectTab(0);
	          }

	        } catch (IndexOutOfBoundsException e) {
	          tabPanel.selectTab(0);
	        }
	      }
	    });

	    tabPanel.selectTab(0);
	    tabPanel.setSize("85%", "100%");
	    //set pages here
	    createFileUploadForm();
	    tabPanel.add(form, "Upload XML file");
	}
	//TODO: CAROLINE refactor this into Admin Page class
	private void createFileUploadForm() {
	    // Create a FormPanel and point it at a service.
	    form = new FormPanel();
	    form.setAction(GWT.getModuleBaseURL()+"filereader");

	    form.setEncoding(FormPanel.ENCODING_MULTIPART);
	    form.setMethod(FormPanel.METHOD_POST);

	    // Create a panel to hold all of the form widgets.
	    VerticalPanel panel = new VerticalPanel();
	    form.setWidget(panel);

	    // Create a FileUpload widget.
	    final FileUpload upload = new FileUpload();
	    upload.setName("uploadFormElement");
	   
	    panel.add(upload);

	    // 'submit' button.
	    panel.add(new Button("Submit", new ClickHandler() {
	      public void onClick(ClickEvent event) {
	        form.submit();
	      }
	    }));

	    // Form event handlers
	    form.addSubmitHandler(new FormPanel.SubmitHandler() {
	      public void onSubmit(SubmitEvent event) {
	        if (upload.getFilename() == null || upload.getFilename().isEmpty()) {
	        	  Window.alert("You must select a XML file to continue");
	        	  event.cancel();
	        }
	        //TODO: CAROLINE can set the filetype in the file chooser?
	        else if (!isXML(upload.getFilename())) {
	        	  Window.alert("Only XML files are supported at this time.");
	        	  event.cancel();
	        }
	      }

	    });
	    form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
	      public void onSubmitComplete(SubmitCompleteEvent event) {
	        // complete event
	        Window.alert(event.getResults());
	      }
	    });
		
	}
	//TODO: CAROLINE refactor this into Admin Page class
	private boolean isXML(String filename) {
		int dot = filename.indexOf(".");
		
		String pastDotStr = filename.substring(dot + 1);
		
		int lastdot = pastDotStr.indexOf(".");
		
		if (lastdot != -1) {
			return isXML(pastDotStr.substring(lastdot + 1));
		}
		
		else {

			if (pastDotStr.trim().toLowerCase().equals("xml")) {
				return true;
			}
		return false;
		}
	}

	
	}
