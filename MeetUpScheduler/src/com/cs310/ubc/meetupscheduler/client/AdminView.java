package com.cs310.ubc.meetupscheduler.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.Widget;

public class AdminView extends Composite implements View {
	  private FormPanel form;
	  private TextArea ta  = new TextArea();
	  private ScrollPanel scrollPan = new ScrollPanel(ta);;
	  private SimplePanel viewPanel = new SimplePanel();
	  private Element nameSpan = DOM.createSpan();
	  
	public AdminView() {
		viewPanel.getElement().appendChild(nameSpan);
        initWidget(viewPanel);
		createFileUploadForm();
	}
	
	@Override
	public Widget asWidget() {
		return form;
	}

	private void createFileUploadForm() {
	    // Create a FormPanel and point it at a service.
	    form = new FormPanel();
	    form.setAction(GWT.getModuleBaseURL()+"filereader");
	    form.setEncoding(FormPanel.ENCODING_MULTIPART);
	    form.setMethod(FormPanel.METHOD_POST);
	    

	    
	    // Create a panel to hold all of the form widgets.
	    VerticalPanel vPanel = new VerticalPanel();
	    vPanel.getElement().setId("adminVertPan");
	    vPanel.add(new HTML("<h1>Administrator View: Data Upload</h1>"));
	    HorizontalPanel hPanel = new HorizontalPanel();
	    hPanel.getElement().setId("adminHorPan");


	    form.setWidget(vPanel);
	    vPanel.add(hPanel);
        
	    // Create a FileUpload widget.
	    final FileUpload upload = new FileUpload();
	    upload.getElement().setId("adminFileUp");
	    upload.setName("uploadFormElement");
	   
	    hPanel.add(upload);

	    // 'submit' button.
		   Button subBut = new Button("Submit", new ClickHandler() {
			      public void onClick(ClickEvent event) {
			        form.submit();
			      }
			    });
		subBut.getElement().setId("adminSubBut");
		hPanel.add(subBut);
	    initTextArea();
	    vPanel.add(scrollPan);

	    // Form event handlers
	    form.addSubmitHandler(new FormPanel.SubmitHandler() {
	      public void onSubmit(SubmitEvent event) {
	        if (upload.getFilename() == null || upload.getFilename().isEmpty()) {
	        	  Window.alert("You must select a XML file to continue");
	        	  event.cancel();
	        }
	        //TODO: CAROLINE can set the filetype in the file chooser?
	        else if (!isXML(upload.getFilename())) {
	        	  Window.alert("Only XML files are supported.");
	        	  event.cancel();
	        }
	      }

	    });
	    form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
	      public void onSubmitComplete(SubmitCompleteEvent event) {
		    ta.setVisible(true);
	    	ta.setText(event.getResults());
	      }
	    });
		
	}

	private void initTextArea() {
        ta.setVisible(false);
	    ta.setCharacterWidth(150);
	    ta.setHeight("20 px");
	    ta.setVisibleLines(20);
	    ta.setReadOnly(true);
	    ta.setAlignment(TextAlignment.JUSTIFY);
	}

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

	@Override
	public void setName(String name) {
		nameSpan.setInnerText("Admin " + name);
	}


}
