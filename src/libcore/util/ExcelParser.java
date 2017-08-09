package libcore.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelParser {
	private XSSFWorkbook workbook;

	public ExcelParser(File file) {
		try {
			workbook = new XSSFWorkbook(new FileInputStream(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private XSSFSheet getSheet(XSSFWorkbook workBook, int index) {
		return workBook.getSheetAt(index);
	}

	private XSSFSheet getSheet(XSSFWorkbook workBook, String name) {
		return workBook.getSheet(name);
	}

	public List<List<String>> getDatasInSheet(int sheetIndex) {
		List<List<String>> results = new ArrayList<List<String>>();
		XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
		int rowCount = sheet.getLastRowNum();
		if (rowCount < 0) {
			return results;
		} else {
			for (int i = 0; i <= rowCount; i++) {
				XSSFRow row = sheet.getRow(i);
				if (row != null) {
					List<String> rowData = new ArrayList<String>();
					int cellCount = row.getLastCellNum();
					for (int cellIndex = 0; cellIndex < cellCount; cellIndex++) {
						XSSFCell cell = row.getCell(cellIndex);
						Object cellStr = this.getCellData(cell);
						String ce = cellStr == null ? "" : cellStr.toString();
						rowData.add(ce);
					}
					results.add(rowData);
				}
			}
			return results;
		}
	}

	private Object getCellData(XSSFCell cell) {
		Object result = null;
		try {
			if (cell != null) {
				int cellType = cell.getCellType();
				switch (cellType) {
				case HSSFCell.CELL_TYPE_STRING:
					result = cell.getRichStringCellValue().getString();
					break;
				case HSSFCell.CELL_TYPE_NUMERIC:
					if (DateUtil.isCellDateFormatted(cell)) {
						// 读取日期格式
						result = cell.getDateCellValue().toString();
					} else {
						result = cell.getNumericCellValue();
					}
					break;
				case HSSFCell.CELL_TYPE_FORMULA:
					try {
						result = String.valueOf(cell.getNumericCellValue());
					} catch (IllegalStateException e) {
						result = String.valueOf(cell.getRichStringCellValue());
					}
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN:
					result = cell.getBooleanCellValue();
					break;
				case HSSFCell.CELL_TYPE_BLANK:
					result = null;
					break;
				case HSSFCell.CELL_TYPE_ERROR:
					result = null;
					break;
				default:
					result = null;
					System.out.println("NO type matched! ");
					break;
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}

		return result;
	}

	public static void main(String[] args) {
		ExcelParser excelParser = new ExcelParser(new File("F:\\work\\my\\Module1-input-投资部_待确认.xlsx"));
		List<List<String>> results = excelParser.getDatasInSheet(8);
		int dataSize = results.size();
		for (int i = 0; i < dataSize; i++) {
			List<String> row = results.get(i);
			for (int j = 0; j < row.size(); j++) {
				String cell = row.get(j);
				System.out.print(cell + "\t");
			}
			System.out.println();
		}
		
	}
}
