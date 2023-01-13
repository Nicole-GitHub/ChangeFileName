package com;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ChangeFileName {
	private static final String className = ChangeFileName.class.getName();
	
	/**
	 * 修改檔案名稱(單一)
	 * @param outputPath
	 * @param folderName
	 * @throws Exception
	 */
	public static void run(String outputPath, String folderName) throws Exception {
		Workbook workbook = null;
		try {
			workbook = Tools.getWorkbook(outputPath + "ChangeFileName.xlsx");
			List<Map<String, String>> mapList = parser(workbook.getSheet("工作表1"));

			for (Map<String, String> forMap : mapList) {
				String fileNameBefore = forMap.get("FileNameBefore").toString();
				String fileNameAfter = forMap.get("FileNameAfter").toString();

//				System.out.println("FileNameBefore:" + fileNameBefore + ",\r\n FileNameAfter:" + fileNameAfter);
				File f = new File(outputPath + folderName + fileNameBefore);
				if (f.exists())
					f.renameTo(new File(outputPath + folderName + fileNameAfter));
				else
					throw new Exception("no file:" + f);

				System.out.println("已完成檔案: " + fileNameBefore);
			}
		} catch (Exception ex) {
			throw new Exception(className + " run Error: \n" + ex);
		} finally {
			if (workbook != null)
				workbook.close();
		}
	}
	
	/**
	 * 取得 Excel 內容
	 * @param sheet
	 * @return
	 * @throws Exception
	 */
	private static List<Map<String, String>> parser(Sheet sheet) throws Exception {
		Row row = null;
		Cell cell = null;
		int rowcount = 0;
		
		List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;

		try {
			// 找出欲解析的資料有幾行
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				row = sheet.getRow(i);
				cell = row == null ? null : row.getCell(3);
				rowcount = i;
				if (cell == null || !Tools.cellNotBlank(cell))
					break;
			}

			// 解析資料內容(從第二ROW開頭爬)
			for (int r = 1; r <= rowcount; r++) {
				map = new HashMap<String, String>();
				row = sheet.getRow(r);

				String fileNameBefore = Tools.getCellValue(row, 2, "原檔名");
				String fileNameAfter = Tools.getCellValue(row, 3, "更改後檔名");

				map.put("FileNameBefore", fileNameBefore);
				map.put("FileNameAfter", fileNameAfter);
				mapList.add(map);
			}
			
		} catch (Exception ex) {
			throw new Exception(className + " parser Error: \n" + ex);
		}

		System.out.println("Parser Done!");
		return mapList;
	}
}
