package com.shopme.admin.category;

import java.util.Date;
import java.util.List;



import java.text.SimpleDateFormat;
import java.io.IOException;
import java.text.DateFormat;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.Category;

import jakarta.servlet.http.HttpServletResponse;



public class CategoryCsvExporter extends AbstractExporter{

	public void export(List<Category> listCategories, 
			HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", ".csv", "_categories");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), 
				CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"Category ID", "Category Name"}; 
	    String[] fieldMapping = {"id", "name"};
		csvWriter.writeHeader(csvHeader);
		
		for(Category category : listCategories) {
			category.setName(category.getName().replace("--", " "));
			csvWriter.write(category, fieldMapping);
		}
	csvWriter.close();
	}
}

