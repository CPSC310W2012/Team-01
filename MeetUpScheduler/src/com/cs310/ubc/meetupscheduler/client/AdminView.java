package com.cs310.ubc.meetupscheduler.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.layout.client.Layout;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.Widget;

public class AdminView extends View {
	  private FormPanel form;
	  private TextArea ta;
	  private ScrollPanel sp;
	  
	public AdminView() {
		createFileUploadForm();
	}
	
	public Widget createPage() {
		return form;
	}

	private void createFileUploadForm() {
	    // Create a FormPanel and point it at a service.
	    form = new FormPanel();
	    form.setAction(GWT.getModuleBaseURL()+"filereader");
	    form.setEncoding(FormPanel.ENCODING_MULTIPART);
	    form.setMethod(FormPanel.METHOD_POST);

	    // Create a panel to hold all of the form widgets.
	    final VerticalPanel panel = new VerticalPanel();
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
	    initTextArea();
	    panel.add(sp);

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
		ta = new TextArea();
        ta.setVisible(false);
	    ta.setCharacterWidth(150);
	    ta.setHeight("20 px");
	    ta.setVisibleLines(50);
	    ta.setReadOnly(true);
	    ta.setAlignment(TextAlignment.JUSTIFY);
	    
	    sp = new ScrollPanel(ta);
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

}
