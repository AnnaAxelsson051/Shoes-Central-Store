package com.shopme.admin.user;

import java.io.IOException;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

//Api from open github library to create a new pdf document
public class UserPdfExporter  extends AbstractExporter{
	
	public void export (List<User> listUsers, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response,  "application/pdf", ".pdf");
	
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
	
		document.open();
		document.add(new Paragraph("List of users"));
		document.close();
	}

}
