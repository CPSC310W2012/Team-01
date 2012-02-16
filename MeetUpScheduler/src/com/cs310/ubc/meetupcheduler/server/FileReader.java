package com.cs310.ubc.meetupcheduler.server;

import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.io.InputStream;
import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileReader extends HttpServlet {
  private static final Logger log =
      Logger.getLogger(FileReader.class.getName());

  public void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    try {
      ServletFileUpload upload = new ServletFileUpload();
      res.setContentType("text/html; charset=UTF-8");

      FileItemIterator iterator = upload.getItemIterator(req);

      while (iterator.hasNext()) {
        FileItemStream item = iterator.next();
        InputStream stream = item.openStream();

        if (item.isFormField()) {
          log.warning("Got a form field: " + item.getFieldName());
        } else {
          log.warning("Got an uploaded file: " + item.getFieldName() +
                      ", name = " + item.getName());   
         
          ParkDataParser xmlParser = new ParkDataParser();
          xmlParser.parseXML(stream);
          
          //TODO: Caroline better msg
          res.getOutputStream().print("DONE");
        }
      }
    } catch (Exception ex) {
      throw new ServletException(ex);
    }
  }
}
