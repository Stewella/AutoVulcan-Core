package com.prosigmaka.catra.diglett;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.prosigmaka.catra.diglett.model.dto.OptiDto;
import com.prosigmaka.catra.diglett.model.projection.OptiProjection;

public class ExcelExporter {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<OptiProjection> dtos;

	public ExcelExporter(List<OptiProjection> dtos) {
		this.dtos = dtos;
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("Data");
	}

	private void writeHeaderRow() {
		Row row = sheet.createRow(0);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(11);
		style.setFont(font);
		
		
		Cell cell = row.createCell(0);
		cell.setCellValue("Nama Client");
		cell.setCellStyle(style);
		sheet.autoSizeColumn(0);

		cell = row.createCell(1);
		cell.setCellValue("Posisi dibutuhkan");
		cell.setCellStyle(style);
		sheet.autoSizeColumn(1);

		cell = row.createCell(2);
		cell.setCellValue("# Candidate Lolos");
		cell.setCellStyle(style);
		sheet.autoSizeColumn(2);

		cell = row.createCell(3);
		cell.setCellValue("# Candidate On Proccess");
		cell.setCellStyle(style);
		sheet.autoSizeColumn(3);

		cell = row.createCell(4);
		cell.setCellValue("# Candidate Tidak Lolos");
		cell.setCellStyle(style);
		sheet.autoSizeColumn(4);


	}

	private void writeDataRows() {
		int idx = 1;
		for (OptiProjection dto : dtos) {
			Row row = sheet.createRow(idx++);

			Cell cell = row.createCell(0);
			cell.setCellValue(dto.getNamaClient());
			sheet.autoSizeColumn(0);

			cell = row.createCell(1);
			cell.setCellValue(dto.getPosisi());
			sheet.autoSizeColumn(1);
			
			cell = row.createCell(2);
			if(dto.getWin()==null) {
				cell.setCellValue(0);
			}
			if(dto.getWin()!=null) {
				cell.setCellValue(dto.getWin());				
			}
			cell = row.createCell(3);
			
			
			if(dto.getOnProcess()==null) {
				cell.setCellValue(0);
			}
			if(dto.getOnProcess()!=null) {
				cell.setCellValue(dto.getOnProcess());
			}
			sheet.autoSizeColumn(3);
			
			cell = row.createCell(4);
			if(dto.getDrop()==null) {
				cell.setCellValue(0);
				
			}
			if(dto.getDrop()!=null) {
				cell.setCellValue(dto.getDrop());
			}
			sheet.autoSizeColumn(4);

		}

	}

	public void export(HttpServletResponse response) throws IOException {
		writeHeaderRow();
		writeDataRows();

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();

	}

}
