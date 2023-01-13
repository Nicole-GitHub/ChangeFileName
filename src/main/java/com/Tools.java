package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Tools {
	private static final String className = Tools.class.getName();

	/**
	 * 取得 Excel的Workbook
	 * 
	 * @param path
	 * @return
	 */
	protected static Workbook getWorkbook(String path) {
		Workbook workbook = null;
		InputStream inputStream = null;
		try {
			File f = new File(path);
			inputStream = new FileInputStream(f);
			String aux = path.substring(path.lastIndexOf(".") + 1);
			if ("XLS".equalsIgnoreCase(aux)) {
				workbook = new HSSFWorkbook(inputStream);
			} else if ("XLSX".equalsIgnoreCase(aux)) {
				workbook = new XSSFWorkbook(inputStream);
			} else {
				throw new Exception("檔案格式錯誤");
			}

		} catch (Exception ex) {
			// 因output時需要用到，故不可寫在finally內
			try {
				if (workbook != null)
					workbook.close();
			} catch (IOException e) {
				throw new RuntimeException(className + " getWorkbook Error: \n" + e);
			}

			throw new RuntimeException(className + " getWorkbook Error: \n" + ex);
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				throw new RuntimeException(className + " getWorkbook Error: \n" + e);
			}
		}
		return workbook;
	}

	/**
	 * 取Excel欄位值
	 * 
	 * @param sheet
	 * @param rownum
	 * @param cellnum
	 * @param fieldName
	 * @return
	 * @throws Exception 
	 */
	protected static String getCellValue(Row row, int cellnum, String fieldName) throws Exception {
		try {
			if (!Tools.cellNotBlank(row.getCell(cellnum))) {
				return "";
			} else if (row.getCell(cellnum).getCellType() == Cell.CELL_TYPE_NUMERIC) {
				return String.valueOf((int) row.getCell(cellnum).getNumericCellValue()).trim();
			} else if (row.getCell(cellnum).getCellType() == Cell.CELL_TYPE_STRING
					|| row.getCell(cellnum).getCellType() == Cell.CELL_TYPE_FORMULA) {
				return row.getCell(cellnum).getStringCellValue().trim();
			} 
		} catch (Exception ex) {
			throw new Exception(className + " getCellValue " + fieldName + " 格式錯誤");
		}
		return "";
	}
	
	/**
     * 不為空
     */
	protected static boolean cellNotBlank(Cell cell) {
		return cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK;
	}
	
	/**
	 * 讀取檔案內容並轉成utf-8
	 * @param filePathName
	 * @return
	 * @throws IOException
	 */
	protected static String readFileContent(String filePathName) throws IOException {
		String temp = "";
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		try {
			File file = new File(filePathName);// 檔案路徑(包括檔名稱)
			// 將檔案內容讀入輸入流
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis,"utf-8");
			br = new BufferedReader(isr);
			StringBuffer buffer = new StringBuffer();
			// 讀取檔案內容
			while((temp = br.readLine()) != null) {
				buffer.append(temp);
				// 行與行之間的分隔符 相當於“\n”
				buffer.append(System.getProperty("line.separator"));
			}
			
			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) br.close();
			if (isr != null) isr.close();
			if (fis != null) fis.close();
		}
		return "";
	}
}
