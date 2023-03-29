package com.shopme.admin.user;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
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
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);
		
		Paragraph paragraph = new Paragraph("List of users", font);
		
	paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(paragraph);
		document.close();
	}

}
