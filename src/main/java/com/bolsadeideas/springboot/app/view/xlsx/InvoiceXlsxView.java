package com.bolsadeideas.springboot.app.view.xlsx;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.bolsadeideas.springboot.app.models.entity.Invoice;
import com.bolsadeideas.springboot.app.models.entity.InvoiceLine;

@Component("invoices/view.xlsx")
public class InvoiceXlsxView extends AbstractXlsxView{

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest req,
			HttpServletResponse res) throws Exception {

		MessageSourceAccessor messages = getMessageSourceAccessor();
		
		Invoice invoice = (Invoice) model.get("invoice");
		final String filename = invoice.getClient().getName() + "_" + invoice.getClient().getSurname() 
				+ "_" + invoice.getCreatedAt() + ".xlsx";
		res.setHeader("Content-Disposition", "attachment; filename=\""+ filename + "\"");	// File name
		Sheet sheet = workbook.createSheet("Factura Spring");

		Row row = sheet.createRow(0);	// First row
		Cell cell = row.createCell(0);	// First column
		cell.setCellValue(messages.getMessage("text.invoice.view.data.client"));

		row = sheet.createRow(1);
		cell = row.createCell(0);
		cell.setCellValue(invoice.getClient().getName() + " " + invoice.getClient().getSurname());

		row = sheet.createRow(2);
		cell = row.createCell(0);
		cell.setCellValue(invoice.getClient().getEmail());

		sheet.createRow(4).createCell(0).setCellValue(messages.getMessage("text.invoice.view.data.invoice"));
		sheet.createRow(5).createCell(0).setCellValue(messages.getMessage("text.invoice.id") + ": " + invoice.getId());
		sheet.createRow(6).createCell(0).setCellValue(messages.getMessage("text.invoice.description") + ": " + invoice.getDescription());
		sheet.createRow(7).createCell(0).setCellValue("Fecha: " + invoice.getCreatedAt());

		CellStyle theaderStyle = workbook.createCellStyle();
		theaderStyle.setBorderBottom(BorderStyle.MEDIUM);
		theaderStyle.setBorderLeft(BorderStyle.MEDIUM);
		theaderStyle.setBorderTop(BorderStyle.MEDIUM);
		theaderStyle.setBorderRight(BorderStyle.MEDIUM);
		theaderStyle.setFillForegroundColor(IndexedColors.GOLD.index);
		theaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		CellStyle tbodyStyle = workbook.createCellStyle();
		tbodyStyle.setBorderBottom(BorderStyle.THIN);
		tbodyStyle.setBorderLeft(BorderStyle.THIN);
		tbodyStyle.setBorderTop(BorderStyle.THIN);
		tbodyStyle.setBorderRight(BorderStyle.THIN);

		Row header = sheet.createRow(9);
		header.createCell(0).setCellValue(messages.getMessage("text.product.product"));
		header.createCell(1).setCellValue(messages.getMessage("text.product.price"));
		header.createCell(2).setCellValue(messages.getMessage("text.product.quantity"));
		header.createCell(3).setCellValue(messages.getMessage("text.product.total"));
		header.getCell(0).setCellStyle(theaderStyle);
		header.getCell(1).setCellStyle(theaderStyle);
		header.getCell(2).setCellStyle(theaderStyle);
		header.getCell(3).setCellStyle(theaderStyle);
		int rownum = 10;
		for(InvoiceLine line : invoice.getLines()) {
			row = sheet.createRow(rownum++);
			cell = row.createCell(0);
			cell.setCellValue(line.getProduct().getName());
			cell.setCellStyle(tbodyStyle);
			cell = row.createCell(1);
			cell.setCellValue(line.getProduct().getPrice());
			cell.setCellStyle(tbodyStyle);
			cell = row.createCell(2);
			cell.setCellValue(line.getQuantity());
			cell.setCellStyle(tbodyStyle);
			cell = row.createCell(3);
			cell.setCellValue(line.calculatePrice());
			cell.setCellStyle(tbodyStyle);
		}

		Row total = sheet.createRow(rownum);
		cell = total.createCell(2);
		cell.setCellStyle(tbodyStyle);
		cell.setCellValue("Total");
		cell = total.createCell(3);
		cell.setCellStyle(tbodyStyle);
		cell.setCellValue(invoice.getTotal());

	}

}
